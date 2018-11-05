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

import com.thoughtworks.xstream.annotations.XStreamOmitField;

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
	
	public double xcoor = 0d;
	public double ycoor = 0d;
	
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
	HashMap<Integer, HashMap<String, Double>> lipidTimeSeries = new HashMap<Integer, HashMap<String, Double>>();
	HashMap<Integer, HashMap<String, Double>> rabTimeSeries = new HashMap<Integer, HashMap<String, Double>>();
//	Probabilities of events per tick.  Calculated from the t1/2 of each process
//	 as the inverse of time1/2(in seconds) / 0.03 * timeScale
//	0.03 is the fastest event (movement on MT, 1 uM/sec) that I use to calibrate
//	the tick duration. At time scale 1, I move the endosome 30 nm in a tick (50 ticks to travel 1500 nm). Hence
//	one tick is equivalent to 0.03 seconds
//	At time scale 0.5, I move the endosome 60 nm (30/timeScale)
//	
	double p_EndosomeRecycleStep = 1d/(10d/0.03*Cell.timeScale);
	double p_EndosomeUptakeStep = 1d/(60d/0.03*Cell.timeScale);
//	double p_EndosomeNewFromERStep = 1d/(60d/0.03*Cell.timeScale);
	double p_EndosomeInternalVesicleStep = 1d/(5d/0.03*Cell.timeScale);// change from 2 to .1
	double p_EndosomeFusionStep =1d/(60d/0.03*Cell.timeScale);
	double p_EndosomeSplitStep = 1d/(0.4/0.03*Cell.timeScale);
	double p_EndosomeTetherStep = 1d/(1d/0.03*Cell.timeScale);
	double p_EndosomeLysosomalDigestionStep = 1d/(10d/0.03*Cell.timeScale);

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
//		System.out.println("area" + area + " "+initOrgProp);
		volume = initOrgProp.get("volume");// 4d / 3d * Math.PI * 30d * 30d *
											// 30d; // initial value, but
		size = Math.pow(volume * 3d / 4d / Math.PI, (1d / 3d));
		speed = Cell.orgScale / size; // initial value, but should change
		heading = Math.random() * 360d - 180; // initial value, but should change
		double mvb = 0; // number of internal vesicles


	}

	public final double getXcoor() {
		return xcoor;
	}

	public final void setXcoor(double xcoor) {
		this.xcoor = xcoor;
	}
	
	public final double getYcoor() {
		return ycoor;
	}
	public void setYcoor(double ycoor) {
		this.ycoor = ycoor;	
	}
	public ContinuousSpace<Object> getSpace() {
		return space;
	}
	public void setSpace(ContinuousSpace<Object> value) {
		space = value;
	}

	@ScheduledMethod(start = 1, interval = 1000)
	public void printRabTropism() {
//		System.out.println(" RAB TROPISMS " + cellProperties.getInstance().getRabTropism());
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
//		if (this.solubleContent.containsKey("mvb")) this.membraneContent.put("chol", 0d);
		if (Math.random()<p_EndosomeRecycleStep)EndosomeRecycleStep.recycle(this);
		if (Math.random()<p_EndosomeUptakeStep)EndosomeUptakeStep.uptake(this);
//		if (Math.random()<p_EndosomeNewFromERStep)EndosomeNewFromERStep.newFromEr(this);
		if (Math.random()<p_EndosomeTetherStep)EndosomeTetherStep.tether(this);
//		if (Math.random()<p_EndosomeInternalVesicleStep)EndosomeInternalVesicleStep.internalVesicle(this);
		if (Math.random()<p_EndosomeFusionStep) EndosomeFusionStep.fusion(this);
		if (Math.random()<p_EndosomeSplitStep) EndosomeSplitStep.split(this);
//		if (Math.random()<p_EndosomeLysosomalDigestionStep)EndosomeLysosomalDigestionStep.lysosomalDigestion(this);
//		Double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
//		if (tick%100 ==0) 
//		if (Math.random() < 1)EndosomeRabConversionStep.rabTimeSeriesLoad(this);
		// rabConversionN();
//		if (Math.random() < 1)EndosomeLipidMetabolismStep.antPresTimeSeriesLoad(this);
		

	}
	public List<Endosome> getAllEndosomes(){
		List<Endosome> allEndosomes = new ArrayList<Endosome>();
		for (Object obj : grid.getObjects()) {
			if (obj instanceof Endosome) {
				allEndosomes.add((Endosome) obj);
			}
		}
//		System.out.println("ALL ENDOSOMES FORM PLASMA MEMBRANE " +allEndosomes);
		return allEndosomes;
	}

	public static void endosomeShape(Endosome end) {
		double s = end.area;
		double v = end.volume;
		double rsphere = Math.pow((v * 3d) / (4d * Math.PI), (1d / 3d));
		double p =  1.6075;
//		double svratio = s / v; // ratio surface volume
		double aa = rsphere; // initial a from the radius of a sphere of volume
								// v
		double cc = aa;// initially, c=a
//		NOT USED
		// calculation from s/v for a cylinder that it is more or less the same than for an
		// ellipsoid
		// s= 2PIa^2+2PIa*2c and v = PIa^2*2c hence s/v =(1/c)+(2/a)
//		USED
//		Surface ellipsoid (tubule or disk) = 4*PI*(  ((a^2p + 2 a^p * c^p)/3)^(1/p)  )
//		where p =  1.6075
		double golgiArea = 0;
		for (String rab : end.rabContent.keySet())
		{
			if (CellProperties.getInstance().getRabOrganelle().get(rab).contains("Golgi"))
			{
				golgiArea = golgiArea + end.rabContent.get(rab);
			}
		}
		if (golgiArea/end.area > 0.5){
			for (int i = 0; i < 4; i++) {
//				for flat ellipsoid (Golgi cisternae)
////			 * AREA (to find a (radius cylinder, r half height of cistern)
////			 * cistern 2*PI* x^2 + 2*PI*r*2*r x  +  (-area) = 0
			double aq = 2d*Math.PI;
			double bq = 4*Math.PI*Cell.rcyl;
			double cq = -s;
			double dq =  bq * bq - 4 * aq * cq;
			aa = ( - bq + Math.sqrt(dq))/(2*aq);
////		    root2 = (-b - Math.sqrt(d))/(2*a);
////		    VOLUME
//			double volume = Math.PI*root1*root1*2*Cell.rcyl;			
			cc = v / (2d * Math.PI * aa * aa);// from volume of cylinder of aa radius v = PI*(aa^2)*2*cc			
//				//					APPROX NOT USED aa = 2d / (svratio - 1d / cc);// from s/v ratio
//				//				for elongated ellipsoid (tubule), from area of ellipsoid
//				cc=Math.pow((Math.pow((s/4/Math.PI),p)*3 - Math.pow(aa, 2*p))/(2*Math.pow(aa, p)),(1/p));
//				//				double cc= (s*3/(4*Math.PI*aa)-aa)/2; Aprox from DOI: 10.2307/3608515
//				aa = Math.sqrt(v*3d/(4d*Math.PI*cc));			
//				//					APPROX NOT USEDcc = 1d/(svratio-1d/aa);
//				//				System.out.println("FORMA s/v " + s/v +" c "+ cc +" a " + aa);
			}
//			System.out.println("FLAT FLAT  c  a  " + cc +" " + aa);
			end.a = aa;
			end.c = cc;
		}
		else 	
		{
			for (int i = 0; i < 4; i++) {
				//				APPROX NOT USED aa = 2d / (svratio - 1d / cc);// from s/v ratio
				//			for elongated ellipsoid (tubule), from area of ellipsoid
				cc=Math.pow((Math.pow((s/4/Math.PI),p)*3 - Math.pow(aa, 2*p))/(2*Math.pow(aa, p)),(1/p));
				//			double cc= (s*3/(4*Math.PI*aa)-aa)/2; Aprox from DOI: 10.2307/3608515
				aa = Math.sqrt(v*3d/(4d*Math.PI*cc));			
				//				APPROX NOT USEDcc = 1d/(svratio-1d/aa);
				//			System.out.println("FORMA s/v " + s/v +" c "+ cc +" a " + aa);
			}
//			System.out.println("LONG LONG  c  a  " + cc +" " + aa);
			//		if (aa<=0)System.out.println("PROBLEMA FORMA " + s +" "+v+"");
			end.a = aa;
			end.c = cc;
		}
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

//	To be used for freeze dry

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
//			if (red > 1)
//				System.out.println("RED FUERA ESCALA " + " " + red + " "
//						+ membraneContent.get(contentPlot) + "  " + area);
//			if (red > 1)
//				System.out.println("RED FUERA ESCALA " + " " + contentPlot);
//			// System.out.println("mHCI content" + red);
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
//			if (blue > 1.1)
//				System.out.println("BLUE FUERA ESCALA " + " " + blue + " "
//						+ membraneContent.get(contentPlot) + "  " + area);
//			if (blue > 1.1)
//				System.out.println("BLUE FUERA ESCALA " + " " + contentPlot);

			return blue;
		}
		if (solubleContent.containsKey(contentPlot)) {
			double blue = solubleContent.get(contentPlot) / volume;
//			if (blue > 1.1)
//				System.out.println("BLUE FUERA ESCALA " + " " + blue + " "
//						+ solubleContent.get(contentPlot) + "  " + area);
//			if (blue > 1)
//				System.out.println("BLUE FUERA ESCALA " + " " + contentPlot);
//			// System.out.println("mHCI content" + red);
			return blue;
		} else
			return 0;
	}


	public double getEdgeRed() {
		String edgePlot = CellProperties.getInstance().getColorRab().get("red");

		if (rabContent.containsKey(edgePlot)) {
			double red = rabContent.get(edgePlot)/area;
			return red;
		} else
			return 0;
	}

	public double getEdgeGreen() {
		String edgePlot = CellProperties.getInstance().getColorRab()
			.get("green");
		if (rabContent.containsKey(edgePlot)) {
			double green = rabContent.get(edgePlot)/ area;
			return green;
		} else
			return 0;
	}

	public double getEdgeBlue() {
		String edgePlot = CellProperties.getInstance().getColorRab()
			.get("blue");
		if (rabContent.containsKey(edgePlot)) {
			double blue = rabContent.get(edgePlot)/area;
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
//	public double getSolContRab() { // (String solCont, String rab){
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
//		return 0;
//	}

//	public double getSolContRab2() { // (String solCont, String rab){
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
//		return 0;
//	}

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

	public HashMap<Integer, HashMap<String, Double>> getLipidTimeSeries() {
		return lipidTimeSeries;
	}



	public HashMap<Integer, HashMap<String, Double>> getRabTimeSeries() {
		return rabTimeSeries;
	}

	public HashMap<String, Double> getInitOrgProp() {
		// TODO Auto-generated method stub
		return initOrgProp;
	}


	

}
