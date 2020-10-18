package immunity;

import java.util.Random;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

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
	double volume = 4d / 3d * Math.PI * 30d * 30d * 30d; // initial value, but should change
	double a = 0; // width of the ellipsoid representing the endosome
	double c = 0; // length;
	double birthday = 0;// birthday in ticks from the maturation
	double size;// = Math.pow(volume * 3d / 4d / Math.PI, (1d / 3d));
	double speed;// = 5d / size; // initial value, but should change
	double heading = 0;// = Math.random() * 360d; // initial value, but should change
	
	double cellLimit = 3 * Cell.orgScale;
	double mvb;// = 0; // number of internal vesices
	double cellMembrane;// = 0;
	Set<String> membraneMet = cellProperties.getMembraneMet();
	Set<String> solubleMet = cellProperties.getSolubleMet();
	Set<String> rabSet = cellProperties.getRabSet();
	HashMap<String, Double> rabCell = new HashMap<String, Double>();
	private List<MT> mts;
	HashMap<String, Double> rabCompatibility = cellProperties.getRabCompatibility();
	HashMap<String, Double> tubuleTropism = cellProperties.getTubuleTropism();
	HashMap<String, Set<String>> rabTropism = cellProperties.getRabTropism();
	HashMap<String, Double> mtTropismTubule = cellProperties.getMtTropismTubule();
	HashMap<String, Double> mtTropismRest = cellProperties.getMtTropismRest();
	HashMap<String, Double> rabContent = new HashMap<String, Double>();
	HashMap<String, Double> membraneContent = new HashMap<String, Double>();
	HashMap<String, Double> solubleContent = new HashMap<String, Double>();
	HashMap<String, Double> initOrgProp = new HashMap<String, Double>();
	TreeMap<Integer, HashMap<String, Double>> endosomeTimeSeries = new TreeMap<Integer, HashMap<String, Double>>();
	TreeMap<Integer, HashMap<String, Double>> rabTimeSeries = new TreeMap<Integer, HashMap<String, Double>>();
//	Probabilities of events per tick.  Calculated from the t1/2 of each process
//	 as the inverse of time1/2(in seconds) / 0.03 * timeScale
//	0.03 is the fastest event (movement on MT, 1 uM/sec) that I use to calibrate
//	the tick duration. At time scale 1, I move the endosome 30 nm in a tick (50 ticks to travel 1500 nm). Hence
//	one tick is equivalent to 0.03 seconds
//	At time scale 0.5, I move the endosome 60 nm (30/timeScale)
//	

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
		this.membraneContent = membraneContent;
		this.solubleContent = solubleContent;
		this.initOrgProp = initOrgProp;
		area = initOrgProp.get("area");// 4d * Math.PI * 30d * 30d; // initial value, but should change
		volume = initOrgProp.get("volume");// 4d / 3d * Math.PI * 30d * 30d * 30d; // initial value, but
		size = Math.pow(volume * 3d / 4d / Math.PI, (1d / 3d));
		speed = Cell.orgScale / size; // initial value, but should change
		heading = Math.random() * 360d - 180; // initial value, but should change
		birthday  = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		
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
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		
		double p_EndosomeFusionStep =1d/(50d/0.03*Cell.timeScale);//used to be 60d
		double p_EndosomeSplitStep = 0.01;//1d/(5d/0.03*Cell.timeScale); // use to be 0.4
		double p_MaturationStep = 1d/(120d/0.03*Cell.timeScale);

		endosomeShape(this);
		EndosomeMove.moveTowards(this);
		if (Math.random()<p_EndosomeFusionStep) EndosomeFusionStep.fusion(this);
		if (Math.random()<p_EndosomeSplitStep) EndosomeSplitStep.split(this);
		
		String name =  cellProperties.getCopasiFiles().get("endosomeCopasi");
		if (Math.random() < 1 && name.endsWith(".cps"))EndosomeCopasiStep.antPresTimeSeriesLoad(this);
		Double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		Random random = new Random();
		double rnd = random.nextGaussian();
		double intrnd= rnd*100+3000;
		intrnd = 3000;
		double lastTick = cellProperties.cellK.get("lastTick");

		if (tick%3000 ==0) {
			MaturationStep.mature(this);	
		}
	}

	public static void endosomeShape(Endosome end) {
		double s = end.area;
		double v = end.volume;
		double rsphere = Math.pow((v * 3d) / (4d * Math.PI), (1d / 3d));
		double p =  1.6075;
		double aa = rsphere; // initial a from the radius of a sphere of volume
							 // v.  aa is the perpendicular axis of the rotation spheroid
		double cc = aa;		 // cc is the rotation axis.  initially, c=a
                             // Surface ellipsoid (tubule or disk) = 4*PI*(  ((a^2p + 2 a^p * c^p)/3)^(1/p)  )
                             // where p =  1.6075
		double golgiArea = 0;
		for (String rab : end.rabContent.keySet())
		{
			if (CellProperties.getInstance().getRabOrganelle().get(rab).contains("Golgi"))
			{
				golgiArea = golgiArea + end.rabContent.get(rab);
			}
		}
		if (golgiArea/end.area > 0.5){
			double transC = Cell.rcyl;
			for (int i = 0; i < 4; i++) {
	            // for flat ellipsoid (Golgi cisternae) from a flat cylinder
	            // * AREA (to find a (radius cylinder, r half height of cistern)
				// here we want to calculate a, the perpendicular axis of the rotating cylinder
				// hence a = x
				// cylinder area =  2*PI* a^2 (the two circles) + 2*PI*r*2*r a (the side) 
				// to solve the cuadratic equation 	2*PI* x^2 + 2*PI*r*2*r x - area = 0
				double aq = 2d*Math.PI;
				double bq = 4*Math.PI*transC;
				double cq = -s;
				double dq =  bq * bq - 4 * aq * cq;
				aa = ( - bq + Math.sqrt(dq))/(2*aq);
				// root2 = (-b - Math.sqrt(d))/(2*a);
				// VOLUME
				// double volume = Math.PI*root1*root1*2*Cell.rcyl;			
				// from the volume obtain c, the rotation axis
				transC = v / (2d * Math.PI * aa * aa);// from volume of cylinder of aa radius v = PI*(aa^2)*2*cc			
				// with the new cc iterate 4 times	
				// NOT USED double cc= (s*3/(4*Math.PI*aa)-aa)/2; Aprox from DOI: 10.2307/3608515
			}
			end.a = aa;
			end.c = transC;
		}
		else 	
		{
			for (int i = 0; i < 4; i++) {
				//for elongated ellipsoid (tubule), from area of ellipsoid
				// area = 4*PI* (a^2p + 2*a^p*c^p)^(1/p). Obtain c with an initial a.
				cc=Math.pow((Math.pow((s/4/Math.PI),p)*3 - Math.pow(aa, 2*p))/(2*Math.pow(aa, p)),(1/p));
				// NOT USED double cc= (s*3/(4*Math.PI*aa)-aa)/2; Aprox from DOI: 10.2307/3608515
				// with c, calculate aa using the volume equation
				// volume = 4/3*a^2*c
				aa = Math.sqrt(v*3d/(4d*Math.PI*cc));			
				// with the new a iterate 4 time
			}

			end.a = aa;
			end.c = cc;
		}
	}
	public double getArea() {
		return area;
	}
	public double getBirthday() {
		return birthday;
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
		String contentPlot = CellProperties.getInstance().getColorContent()
				.get("red");

		if (membraneContent.containsKey(contentPlot)) {
			double red = membraneContent.get(contentPlot) / area;
			return red;
		}
		if (solubleContent.containsKey(contentPlot)) {
			double red = solubleContent.get(contentPlot) / volume;
			return red;
		} else
			return 0;
	}

	public double getGreen() {
		String contentPlot = CellProperties.getInstance().getColorContent()
				.get("green");

		if (membraneContent.containsKey(contentPlot)) {
			double green = membraneContent.get(contentPlot) / area;
			return green;
		}
		if (solubleContent.containsKey(contentPlot)) {
			double green = solubleContent.get(contentPlot) / volume;
			return green;
		} else
			return 0;
	}

	public double getBlue() {
		String contentPlot = CellProperties.getInstance().getColorContent()
				.get("blue");

		if (membraneContent.containsKey(contentPlot)) {
			double blue = membraneContent.get(contentPlot) / area;
			return blue;
		}
		if (solubleContent.containsKey(contentPlot)) {
			double blue = solubleContent.get(contentPlot) / volume;
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

	public TreeMap<Integer, HashMap<String, Double>> getEndosomeTimeSeries() {
		return endosomeTimeSeries;
	}

	public TreeMap<Integer, HashMap<String, Double>> getRabTimeSeries() {
		return rabTimeSeries;
	}

	public HashMap<String, Double> getInitOrgProp() {
		return initOrgProp;
	}

}
