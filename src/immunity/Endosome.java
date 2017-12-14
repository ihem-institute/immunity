package immunity;

//import immunity.EndosomeStyle.MemCont;
//import immunity.EndosomeStyle.RabCont;
//import immunity.EndosomeStyle.SolCont;
import java.util.Random;
import java.awt.Graphics;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.table.TableModel;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.Logger;
import org.opengis.filter.identity.ObjectId;

import gov.nasa.worldwind.formats.json.JSONDoc;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.essentials.RepastEssentials;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.ui.table.AgentTableFactory;
import repast.simphony.ui.table.SpreadsheetUtils;
import repast.simphony.ui.table.TablePanel;
import repast.simphony.util.ContextUtils;

import java.util.Random;

import repast.simphony.valueLayer.GridValueLayer;

/**
 * @author lmayorga
 *
 */
public class Endosome {
	final static Logger logger = Logger.getLogger(Endosome.class);

	// space

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	// Endosomal
	CellProperties cellProperties = CellProperties.getInstance();
	HashMap<String, Double> cellK = cellProperties.getCellK();

	double area = 4d * Math.PI * 30d * 30d; // initial value, but should change
	double volume = 4d / 3d * Math.PI * 30d * 30d * 30d; // initial value, but
															// should change
	double a = 0; // width of the ellipsoid representing the endosome
	double c = 0; // length;
	double size;// = Math.pow(volume * 3d / 4d / Math.PI, (1d / 3d));
	double speed;// = 5d / size; // initial value, but should change
	double heading = 0;// = Math.random() * 360d; // initial value, but should
						// change
	double cellLimit = 3 * Cell.orgScale;
	double mvb;// = 0; // number of internal vesices
	double cellMembrane;// = 0;
	Set<String> membraneMet = cellProperties.getMembraneMet();
	Set<String> solubleMet = cellProperties.getSolubleMet();
	Set<String> rabSet = cellProperties.getRabSet();
	HashMap<String, Double> rabCell = new HashMap<String, Double>();
	private List<MT> mts;
	// HashMap<String, Double> membraneMet = cellProperties.membraneMet();
	HashMap<String, Double> rabCompatibility = cellProperties
			.getRabCompatibility();
	HashMap<String, Double> tubuleTropism = cellProperties.getTubuleTropism();
	HashMap<String, Set<String>> rabTropism = cellProperties.getRabTropism();
	HashMap<String, Double> mtTropism = cellProperties.getMtTropism();
	HashMap<String, Double> rabContent = new HashMap<String, Double>();
	HashMap<String, Double> membraneContent = new HashMap<String, Double>();
	HashMap<String, Double> solubleContent = new HashMap<String, Double>();
	HashMap<String, Double> initOrgProp = new HashMap<String, Double>();
	
	HashMap<Integer, HashMap<String, Double>> antigenTimeSeries = new HashMap<Integer, HashMap<String, Double>>();
	HashMap<Integer, HashMap<String, Double>> LANCL2TimeSeries = new HashMap<Integer, HashMap<String, Double>>();
	HashMap<Integer, HashMap<String, Double>> rabTimeSeries = new HashMap<Integer, HashMap<String, Double>>();
//	Probabilities of events per tick.  Calculated from the t1/2 of each process
//	 as the inverse of time1/2(in seconds) / 0.015 * timeScale
//	0.015 is the fastest event (movement on MT, 1 uM/sec) that I use to calibrate
//	the tick duration. At time scale 1, I move the endosome 15 nm in a tick. 
//	At time scale 0.5, I move the endosome 30 nm (15/timeScale)
//	
	double p_EndosomeRecycleStep = 1d/(1d/0.015*Cell.timeScale);
	double p_EndosomeUptakeStep = 1d/(60d/0.015*Cell.timeScale);
	double p_EndosomeNewFromERStep = 1d/(60d/0.015*Cell.timeScale);
	double p_EndosomeInternalVesicleStep = 1d/(1d/0.015*Cell.timeScale);
	double p_EndosomeFusionStep =1d/(180d/0.015*Cell.timeScale);
	double p_EndosomeSplitStep = 1d/(0.1/0.015*Cell.timeScale);
	double p_EndosomeTetherStep = 1d/(1d/0.015*Cell.timeScale);
	double p_EndosomeLysosomalDigestionStep = 1d/(60d/0.015*Cell.timeScale);
	


	// constructor of endosomes with grid, space and a set of Rabs, membrane
	// contents,
	// and volume contents.
	public Endosome(ContinuousSpace<Object> sp, Grid<Object> gr,
			HashMap<String, Double> rabContent,
			HashMap<String, Double> membraneContent,
			HashMap<String, Double> solubleContent,
			HashMap<String, Double> initOrgProp) {
		this.space = sp;
		this.grid = gr;
		this.rabContent = rabContent;
//		EndosomeRabConversionStep.rabConversion(this);
		this.membraneContent = membraneContent;
		this.solubleContent = solubleContent;
		this.initOrgProp = initOrgProp;
		area = initOrgProp.get("area");// 4d * Math.PI * 30d * 30d; // initial
										// value, but should change
		volume = initOrgProp.get("volume");// 4d / 3d * Math.PI * 30d * 30d *
											// 30d; // initial value, but
		size = Math.pow(volume * 3d / 4d / Math.PI, (1d / 3d));
		speed = Cell.orgScale / size; // initial value, but should change
		heading = Math.random() * 360d; // initial value, but should change
		double mvb = 0; // number of internal vesicles


	}

	public ContinuousSpace<Object> getSpace() {
		return space;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		String message = "MENSAJE";//(new JSONDoc(rabTimeSeries)).toString();
		if (logger.isDebugEnabled()) {
			logger.debug(message);			
		}
		endosomeShape(this);
//		EndosomeMove.changeDirection(this);
		EndosomeMove.moveTowards(this);
		if (Math.random()<p_EndosomeTetherStep)EndosomeTetherStep.tether(this);
		if (Math.random()<p_EndosomeRecycleStep)EndosomeRecycleStep.recycle(this);
		if (Math.random()<p_EndosomeUptakeStep)EndosomeUptakeStep.uptake(this);
		if (Math.random()<p_EndosomeNewFromERStep)EndosomeNewFromERStep.newFromEr(this);
		if (Math.random()<p_EndosomeTetherStep)EndosomeTetherStep.tether(this);
		if (Math.random()<p_EndosomeInternalVesicleStep)EndosomeInternalVesicleStep.internalVesicle(this);
		if (Math.random()<p_EndosomeFusionStep) EndosomeFusionStep.fusion(this);
		if (Math.random()<p_EndosomeSplitStep) EndosomeSplitStep.split(this);
		if (Math.random()<p_EndosomeLysosomalDigestionStep)EndosomeLysosomalDigestionStep.lysosomalDigestion(this);
//		Double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
//		if (tick%100 ==0) 
		if (Math.random() < 1)EndosomeRabConversionStep.rabTimeSeriesLoad(this);
		// rabConversionN();
		if (Math.random() < 1)EndosomeAntigenPresentationStep.antPresTimeSeriesLoad(this);
		
//		if (Math.random() < 1) EndosomeLANCL2metabolismStep.LANCL2TimeSeriesLoad(this);	

	}


	public static void endosomeShape(Endosome end) {
		double s = end.area;
		double v = end.volume;
		double rsphere = Math.pow((v * 3d) / (4d * Math.PI), (1d / 3d));
		double svratio = s / v; // ratio surface volume
		double aa = rsphere; // initial a from the radius of a sphere of volume
								// v
		double cc = aa;// initially, c=a
		// calculation from s/v for a cylinder that it is the same than for an
		// ellipsoid
		// s= 2PIa^2+2PIa*2c and v = PIa^2*2c hence s/v =(1/c)+(2/a)
		for (int i = 1; i < 5; i++) {// just two iterations yield an acceptable
										// a-c ratio for plotting
			aa = 2d / (svratio - 1d / cc);// from s/v ratio
			cc = v * 3d / (4d * Math.PI * aa * aa);// from v ellipsoid
		}
		if (aa<=0)System.out.println("PROBLEMA FORMA " + s +" "+v+"");
		end.a = aa;
		end.c = cc;
	}


	public double getArea() {
		return area;
	}

	public double getVolume() {
		if (volume < 1.0) {
			return volume;
		}
		return volume;
	}

	public double getSpeed() {
		return speed;
	}

	public double getHeading() {
		return heading;
	}

	public HashMap<String, Double> getRabContent() {
		return rabContent;
	}

	public HashMap<String, Double> getMembraneContent() {
		return membraneContent;
	}

	public HashMap<String, Double> getSolubleContent() {
		return solubleContent;
	}

	public Endosome getEndosome() {
		return this;
	}

	/*
	 * public static Cell getInstance() { return instance; }
	 * 
	 * 
	 * public HashMap<String, Double> getRabContent() { return rabContent; }
	 * /*public HashMap<String, Double> getMembraneContent() { return
	 * membraneContent; } public HashMap<String, Double> getSolubleContent() {
	 * return solubleContent; }
	 */
	public String getMvb() {
		if (solubleContent.containsKey("mvb")) {
			if (solubleContent.get("mvb") > 0.9) {
				int i = solubleContent.get("mvb").intValue();
				return String.valueOf(i);
			} else
				return null;
		} else
			return null;

	}

	public double getRed() {
		// double red = 0.0;
		String contentPlot = CellProperties.getInstance().getColorContent()
				.get("red");

		if (membraneContent.containsKey(contentPlot)) {
			double red = membraneContent.get(contentPlot) / area;
			if (red > 1)
				System.out.println("RED FUERA ESCALA " + " " + red + " "
						+ membraneContent.get(contentPlot) + "  " + area);
			if (red > 1)
				System.out.println("RED FUERA ESCALA " + " " + contentPlot);
			// System.out.println("mHCI content" + red);
			return red;
		}
		if (solubleContent.containsKey(contentPlot)) {
			double red = solubleContent.get(contentPlot) / volume;
			// System.out.println("mHCI content" + red);
			return red;
		} else
			return 0;
	}

	public double getGreen() {
		// double red = 0.0;
		String contentPlot = CellProperties.getInstance().getColorContent()
				.get("green");

		if (membraneContent.containsKey(contentPlot)) {
			double green = membraneContent.get(contentPlot) / area;
			// System.out.println("mHCI content" + red);
			return green;
		}
		if (solubleContent.containsKey(contentPlot)) {
			double green = solubleContent.get(contentPlot) / volume;
			// System.out.println("mHCI content" + red);
			return green;
		} else
			return 0;
	}

	public double getBlue() {
		// double red = 0.0;
		String contentPlot = CellProperties.getInstance().getColorContent()
				.get("blue");

		if (membraneContent.containsKey(contentPlot)) {
			double blue = membraneContent.get(contentPlot) / area;
			if (blue > 1.1)
				System.out.println("BLUE FUERA ESCALA " + " " + blue + " "
						+ membraneContent.get(contentPlot) + "  " + area);
			if (blue > 1.1)
				System.out.println("BLUE FUERA ESCALA " + " " + contentPlot);

			return blue;
		}
		if (solubleContent.containsKey(contentPlot)) {
			double blue = solubleContent.get(contentPlot) / volume;
			if (blue > 1.1)
				System.out.println("BLUE FUERA ESCALA " + " " + blue + " "
						+ solubleContent.get(contentPlot) + "  " + area);
			if (blue > 1)
				System.out.println("BLUE FUERA ESCALA " + " " + contentPlot);
			// System.out.println("mHCI content" + red);
			return blue;
		} else
			return 0;
	}


	public double getEdgeRed() {
		String edgePlot = CellProperties.getInstance().getColorRab().get("red");

		if (rabContent.containsKey(edgePlot)) {
			double red = Math.round(rabContent.get(edgePlot)*1000 / area)/1000;
			return red;
		} else
			return 0;
	}

	public double getEdgeGreen() {
		String edgePlot = CellProperties.getInstance().getColorRab()
				.get("green");
		if (rabContent.containsKey(edgePlot)) {
			double green = Math.round(rabContent.get(edgePlot)*1000 / area)/1000;
			return green;
		} else
			return 0;
	}

	public double getEdgeBlue() {
		String edgePlot = CellProperties.getInstance().getColorRab()
				.get("blue");
		if (rabContent.containsKey(edgePlot)) {
			double blue = Math.round(rabContent.get(edgePlot)*1000 / area)/1000;
			return blue;
		} else
			return 0;
	}

	/*
	 * enum RabCont { RABA, RABB, RABC, RABD, RABE } public RabCont rabCont;
	 * 
	 * enum MemCont { TF, EGF, MHCI, PROT1, PROT2 } public MemCont memCont;
	 * 
	 * enum SolCont { OVA, DEXTRAN } public SolCont solCont;
	 */
	public double getSolContRab() { // (String solCont, String rab){
//		Parameters params = RunEnvironment.getInstance().getParameters();
//		String rab = (String) params.getValue("Rab");
//		String solCont = (String) params.getValue("soluble");
//		Double sc = null;
//		Double rc = null;
//		if (solCont != null && rab != null) {
//			if (solubleContent.containsKey(solCont)) {
//				sc = solubleContent.get(solCont);
//			} else
//				return 0;
//			if (rabContent.containsKey(rab)) {
//				rc = rabContent.get(rab);
//			} else
//				return 0;
//			if (sc != null && rc != null) {
//				double solContRab = sc * rc / this.area;
//				return solContRab;
//			}
//		}
		return 0;
	}

	public double getSolContRab2() { // (String solCont, String rab){
//		Parameters params = RunEnvironment.getInstance().getParameters();
//		String rab = (String) params.getValue("Rab2");
//		String solCont = (String) params.getValue("soluble");
//		Double sc = null;
//		Double rc = null;
//		if (solCont != null && rab != null) {
//			if (solubleContent.containsKey(solCont)) {
//				sc = solubleContent.get(solCont);
//			} else
//				return 0;
//			if (rabContent.containsKey(rab)) {
//				rc = rabContent.get(rab);
//			} else
//				return 0;
//			if (sc != null && rc != null) {
//				double solContRab = sc * rc / this.area;
//				return solContRab;
//			}
//		}
		return 0;
	}

	public double getMemContRab(String memCont, String rab) {
		double memContRab = membraneContent.get(memCont) * rabContent.get(rab)
				/ this.area;
		return memContRab;
	}

	public double getA() {
		return a;
	}

	public double getC() {
		return c;
	}

	public Grid<Object> getGrid() {
		return grid;
	}

	public HashMap<Integer, HashMap<String, Double>> getAntigenTimeSeries() {
		return antigenTimeSeries;
	}

	public HashMap<Integer, HashMap<String, Double>> getLANCL2TimeSeries() {
		return LANCL2TimeSeries;
	}

	public HashMap<Integer, HashMap<String, Double>> getRabTimeSeries() {
		return rabTimeSeries;
	}

}
