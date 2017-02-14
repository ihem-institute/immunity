package immunity;

//import immunity.EndosomeStyle.MemCont;
//import immunity.EndosomeStyle.RabCont;
//import immunity.EndosomeStyle.SolCont;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.opengis.filter.identity.ObjectId;

import repast.simphony.context.Context;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.valueLayer.GridValueLayer;

/**
 * @author lmayorga
 *
 */
public class Endosome {
	// space

	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	// Endosomal
	CellProperties cellProperties = CellProperties.getInstance();
	HashMap<String, Double> cellK = cellProperties.getCellK();

	double area = 4d * Math.PI * 30d * 30d; // initial value, but should change
	double volume = 4d / 3d * Math.PI * 30d * 30d * 30d; // initial value, but
															// should change
	double size;// = Math.pow(volume * 3d / 4d / Math.PI, (1d / 3d));
	double speed;// = 5d / size; // initial value, but should change
	double heading;// = Math.random() * 360d; // initial value, but should
					// change
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
		area = initOrgProp.get("area");// 4d * Math.PI * 30d * 30d; // initial
										// value, but should change
		volume = initOrgProp.get("volume");// 4d / 3d * Math.PI * 30d * 30d *
											// 30d; // initial value, but
		size = Math.pow(volume * 3d / 4d / Math.PI, (1d / 3d));
		speed = 5d / size; // initial value, but should change
		heading = Math.random() * 360d; // initial value, but should change
		double mvb = 0; // number of internal vesicles
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		recycle();
		uptake();
		size();
		changeDirection();
		moveTowards();
		tether();
		fusion();
		split();
		internalVesicle();
		if (Math.random() < 0.001) 
		rabConversion();
		//rabConversionN();
		if (Math.random() < 0.001)
			antigenPresentation();
	}

	private void rabConversionN() {
		double iRabAm = 0.0;
		double iRabDm = 0.0;
		double iRabAc = 0.0;
		double iRabDc = 0.0;
		if (rabContent.containsKey("RabA")) {
			iRabAm = rabContent.get("RabA");
		} else
			iRabAm = 0.0;
		if (rabContent.containsKey("RabD")) {
			iRabDm = rabContent.get("RabD");
		} else
			iRabDm = 0.0;

		if (Cell.getInstance().getRabCell().containsKey("RabA")) {
			iRabAc = Cell.getInstance().getRabCell().get("RabA");
		} else
			iRabAc = 0.0;
		if (Cell.getInstance().getRabCell().containsKey("RabD")) {
			iRabDc = Cell.getInstance().getRabCell().get("RabD");
		} else
			iRabDc = 0.0;
		if (iRabAm <= 0 || iRabDc <= 0)
			return;

		double delta = iRabAm * 0.001;
		double fRabAm = iRabAm - delta;
		double fRabDm = iRabDm + delta;
		double fRabAc = iRabAc + delta;
		double fRabDc = iRabDc - delta;
		rabContent.put("RabA", fRabAm);
		rabContent.put("RabD", fRabDm);
		Cell.getInstance().getRabCell().put("RabA", fRabAc);
		Cell.getInstance().getRabCell().put("RabD", fRabDc);
		// TODO Auto-generated method stub
		System.out.println(this.hashCode() + "  RAB CONVERTION iRabAm "
				+ iRabAm + " iRabAc " + iRabAc + " iRabDm " + iRabDm
				+ " iRabDc " + iRabDc);
		System.out.println("RAB CONVERTION fRabAm " + fRabAm + " fRabAc "
				+ fRabAc + " fRabDm " + fRabDm + " fRabDc " + fRabDc);

	}

	private double distance(Endosome endosome, MT obj) {

		// If the line passes through two points P1=(x1,y1) and P2=(x2,y2) then
		// the distance of (x0,y0) from the line is:
		NdPoint pt = space.getLocation(this);
		double xpt = pt.getX();
		double ypt = pt.getY();
		double ymax = (double) ((MT) obj).getYend();
		double ymin = (double) ((MT) obj).getYorigin();
		double xmax = (double) ((MT) obj).getXend();
		double xmin = (double) ((MT) obj).getXorigin();
		// double a = Math.abs((ymax-ymin) * xpt - (xmax-xmin)*ypt + xmax*ymin -
		// ymax*xmin);
		double a = Math.abs((xmax - xmin) * (ymin - ypt) - (ymax - ymin)
				* (xmin - xpt));
		double b = Math.sqrt((ymax - ymin) * (ymax - ymin) + (xmax - xmin)
				* (xmax - xmin));
		double distance = a / b;
		// System.out.println("distance");
		// System.out.println(distance);
		return distance;
	}

	public void printEndosomes() {
		List<Endosome> endosomes = new ArrayList<Endosome>();
		for (Object obj : grid.getObjects()) {
			if (obj instanceof Endosome) {
				endosomes.add((Endosome) obj);
			}
		}
		for (Endosome endosome1 : endosomes) {
			// System.out.println(endosome1.rabContent+" " + object
			// endosome1.membraneContent+" " + endosome1.solubleContent);
		}
	}

	private void antigenPresentation() {

		AntigenPresentation antigenPresentation = AntigenPresentation
				.getInstance();

		Set<String> metabolites = antigenPresentation.getInstance()
				.getMetabolites();
		// System.out.println("METABOLITES INITIAL " + metabolites);

		// metabolites.add("RabAm");
		// metabolites.add("RabAc");
		// metabolites.add("RabBm");
		// metabolites.add("RabBc");
		HashMap<String, Double> localM = new HashMap<String, Double>();
		for (String met : metabolites) {
			localM.put(met, 0.0);
		}
		for (String met : metabolites) {

			if (membraneContent.containsKey(met)) {
				double metValue = Math
						.abs(Math.round(membraneContent.get(met)));
				localM.put(met, metValue);
				antigenPresentation.setInitialConcentration(met, metValue);
			} else if (solubleContent.containsKey(met)) {
				double metValue = Math.abs(Math.round(solubleContent.get(met)));
				localM.put(met, metValue);
				antigenPresentation.setInitialConcentration(met, metValue);
			} else {
				antigenPresentation.setInitialConcentration(met, 0.0);
				// System.out.println("COPASI INITIAL " + met + 0.0);
			}
		}

		/*
		 * double sm1 = localM.get("ova"); double sc1 = localM.get("p1"); double
		 * sm2 = localM.get("preP"); double sc2 = localM.get("p2"); if ((sm1 ==
		 * 0 || sc1 == 0) && (sm2 == 0 || sc2 == 0)) return;
		 */
		System.out.println("AntigenPresentation initial " + localM);
		antigenPresentation.runTimeCourse();
		for (String met : metabolites) {
			/*
			 * if (membraneContent.containsKey(met)) { double metValue =
			 * (double) Math.abs(Math
			 * .round(antigenPresentation.getConcentration(met)));
			 * localM.put(met, metValue); membraneContent.put(met, metValue); }
			 * else if (solubleContent.containsKey(met)) { double metValue =
			 * (double) Math.abs(Math
			 * .round(antigenPresentation.getConcentration(met)));
			 * localM.put(met, metValue); solubleContent.put(met, metValue); }
			 * else {
			 */
			if (membraneMet.contains(met)) {
				double metValue = (double) Math.abs(Math
						.round(antigenPresentation.getConcentration(met)));
				localM.put(met, metValue);
				membraneContent.put(met, metValue);
			} else if (solubleMet.contains(met)) {
				double metValue = (double) Math.abs(Math
						.round(antigenPresentation.getConcentration(met)));
				localM.put(met, metValue);
				solubleContent.put(met, metValue);
			} else
				System.out.println("Met not found in " + membraneMet + " "
						+ solubleMet + " " + met);
		}

		System.out.println("AntigenPresentation final " + localM);
	}

	private void rabConversion() {
//		List<Endosome> allEndosomes = new ArrayList<Endosome>();
//		for (Object obj : grid.getObjects()) {
//			if (obj instanceof Endosome) {
//				allEndosomes.add((Endosome) obj);
//			}
//		}
//		for (Endosome endosome : allEndosomes){
		RabConversion rabConversion = RabConversion.getInstance();

		Set<String> metabolites = RabConversion.getInstance().getMetabolites();
		// System.out.println("METABOLITES INITIAL " + metabolites);

		// metabolites.add("RabAm");
		// metabolites.add("RabAc");
		// metabolites.add("RabBm");
		// metabolites.add("RabBc");
		//
//		 double iRabAm = 0;
//		 double iRabDm = 0;
//		 if(rabContent.containsKey("RabA")){
//		 iRabAm = rabContent.get("RabA");
//		 } else iRabAm = 0d;
//		 if(rabContent.containsKey("RabD")){
//		 iRabDm = rabContent.get("RabD");
//		 } else iRabDm = 0d;
//		 double iRabAc = Cell.getInstance().getRabCell().get("RabA");
//		 double iRabDc = Cell.getInstance().getRabCell().get("RabD");

		HashMap<String, Double> localM = new HashMap<String, Double>();
		for (String met : metabolites) {
			localM.put(met, 0.0);
		}
		for (String met : metabolites) {

			if (met.endsWith("m")) {
				String Rab = met.substring(0, 4);
				if (this.rabContent.containsKey(Rab)) {
					double metValue = Math
							.round(this.rabContent.get(Rab) * 1000) / 1000;
					rabConversion.setInitialConcentration(met, metValue);
					localM.put(met, metValue);
					// System.out.println("COPASI INITIAl " + met
					// + rabContent.get(Rab));
				} else {
					rabConversion.setInitialConcentration(met, 0.0);
					// System.out.println("COPASI INITIAL " + met + 0.0);
				}
			}

			if (met.endsWith("c")) {
				String Rab = met.substring(0, 4);
				if (Cell.getInstance().getRabCell().containsKey(Rab)) {
					double metValue = Math.round(Cell.getInstance()
							.getRabCell().get(Rab) * 1000) / 1000;
					rabConversion.setInitialConcentration(met, metValue);
					localM.put(met, metValue);
					// System.out.println("COPASI INITIAL " + met
					// + Cell.getInstance().rabCell.get(Rab));
				} else {
					rabConversion.setInitialConcentration(met, 0.0);
					// System.out.println("COPASI INITIAL " + met + 0.0);

				}
			}
			// double iRabAc = Cell.getInstance().getRabCell().get("RabA");

		}

		double sm1 = localM.get("RabAm");
		double sc1 = localM.get("RabDc");
		double sm2 = localM.get("RabBm");
		double sc2 = localM.get("RabCc");
		if ((sm1 == 0 || sc1 == 0) && (sm2 == 0 || sc2 == 0))
			return;
		System.out.println("COPASI INITIAL  membrane "
				+ rabContent +" soluble "+ Cell.getInstance().getRabCell());
		rabConversion.runTimeCourse();
		for (String met : metabolites) {
			if (met.endsWith("m")) {
				String Rab = met.substring(0, 4);
				double metValue = rabConversion.getConcentration(met);
				rabContent.put(Rab, metValue);
				localM.put(met, metValue);
				// System.out.println("COPASI FINAL " + met +
				// rabContent.get(Rab));
			}
			if (met.endsWith("c")) {
				String Rab = met.substring(0, 4);
				double metValue = rabConversion.getConcentration(met);
				Cell.getInstance().getRabCell().put(Rab, metValue);
				localM.put(met, metValue);
				// System.out.println("COPASI FINAL " + met
				// + Cell.getInstance().getRabCell().get(Rab));
			}

		}

		System.out.println("COPASI FINAL membrane "
				+ rabContent +" soluble "+ Cell.getInstance().getRabCell());
		
		//Cell.getInstance().getRabCell() + "Membrane" + rabContent);
		// From here was to assess the problem of lack of constant Rabs along
		// the process.
		// Conclusion: the value of rab that is put in rabContent affect all the
		// endosomes!
//		 double fRabAm = 0;
//		 double fRabAc = 0;
//		 double fRabDc = 0;
//		 double fRabDm = 0;
//		 //if(rabContent.containsKey("RabA")){
//		 //fRabAm = rabConversion.getConcentration("RabAm");
//		 double delta = 0.01 * iRabAm;// iRabAm - fRabAm;
//		 fRabAm = iRabAm - delta;
//		 fRabAc = iRabAc + delta;
//		 fRabDc = iRabDc - delta;
//		 fRabDm = iRabDm + delta;
//		 rabContent.put("RabA", fRabAm);
//		 rabContent.put("RabD", fRabDm);
//		 Cell.getInstance().getRabCell().put("RabA", fRabAc);
//		 Cell.getInstance().getRabCell().put("RabD", fRabDc);
//		
//		
//		
//		 double rAc = Cell.getInstance().getRabCell().get("RabA");
//		 double rDc = Cell.getInstance().getRabCell().get("RabD");
//		 double rAm = rabContent.get("RabA");
//		 double rDm = rabContent.get("RabD");
//		
//		 // if (Math.abs(iRabAm+iRabAc - fRabAm - fRabAc) > 0.01 ){
//		 System.out.println(" RABCONVERTION    "+ ((iRabAm+iRabAc)-(fRabAm +
//		 fRabAc)));
//		 System.out.println(" RABCONVERTION    "+ ((iRabDm+iRabDc)-(fRabDm +
//		 fRabDc)));
//		 System.out.println(" RABCONVERTION    "+ ((iRabAm+iRabAc)-(rAm +
//		 rAc)));
//		 System.out.println(" RABCONVERTION    "+ ((iRabDm+iRabDc)-(rDm +
//		 rDc)));
		
		 // }
	}

	public List<MT> associateMt() {
		List<MT> mts = new ArrayList<MT>();
		for (Object obj : grid.getObjects()) {
			if (obj instanceof MT) {
				mts.add((MT) obj);
			}
		}
		// System.out.println(mts);
		return mts;

	}

	public double changeDirection() {
		if (Math.random() < 0.60) {
			// this.heading = -90;
			return this.heading;
		}
		if (Math.random() < 0.60) {
			this.heading = (this.heading + (0.5d - Math.random()) * 90d * 30d
					/ size()) % 360;
			return this.heading;
		}
		if (mts == null) {
			mts = associateMt();
		}
		int mtDir = 0;
		/*
		 * mtDirection decides if the endosome is going to muve to the (-) end
		 * of the MT (dyneine like or to the plus end (kinesine like). -1 goes
		 * to the nucleus, 1 to the PM
		 */
		mtDir = mtDirection();
		for (MT mt : mts) {
			double dist = distance(this, mt);
			if (dist < this.size / 30d) {
				this.heading = -mt.getMtheading() + 180f;
				NdPoint myPoint = space.getLocation(this);
				double x = myPoint.getX() - mtDir
						* Math.cos((heading - 90) * 2d * Math.PI / 360d) * dist;
				double y = myPoint.getY() - mtDir
						* Math.sin((heading - 90) * 2d * Math.PI / 360d) * dist;
				if (y > 50 || y < 0) {
					heading = -heading;
					y = 0;
					x = 0;

				}
				space.moveTo(this, x, y);
				grid.moveTo(this, (int) x, (int) y);

				// this.heading = -mt.getMtheading()+ 180f;

				// System.out.println("headingMT");
				// System.out.println(this.heading);
				return this.heading;
			}
		}
		return this.heading;
	}

	public double size() {
		double rsphere = Math.pow(this.volume * 3d / 4d / Math.PI, (1d / 3d));
		double size = rsphere; // cellscale ;calculate size proportional to
								// volume (radius of sphere with this volume)
		return size;
	}

	private double getCompatibility(String rabX, String rabY) {
		if (!rabCompatibility.containsKey(rabX + rabY)
				&& !rabCompatibility.containsKey(rabY + rabX))
			return 0;
		if (rabCompatibility.containsKey(rabX + rabY)) {
			// System.out.println("COMPATIB");
			// System.out.println(rabCompatibility.get(rabX+rabY));
			return rabCompatibility.get(rabX + rabY);

		} else {

			return rabCompatibility.get(rabY + rabX);
		}
	}

	private boolean compatibles(Endosome endosome1, Endosome endosome2) {
		double sum = 0;
		for (String key1 : endosome1.rabContent.keySet()) {
			for (String key2 : endosome2.rabContent.keySet()) {
				double comp = getCompatibility(key1, key2)
						* endosome1.rabContent.get(key1) / endosome1.area
						* endosome2.rabContent.get(key2) / endosome2.area;
				sum = sum + comp;
			}
		}
		// compatibility is a value between 0 and 1. Fusion
		// occurs with a probability proportional to the compatibility
		return Math.random() < sum;
	}

	// This method list all endosomes that are in the neighborhood of an
	// endosome
	//
	public void tether() {
		GridPoint pt = grid.getLocation(this);
		GridCellNgh<Endosome> nghCreator = new GridCellNgh<Endosome>(grid, pt,
				Endosome.class, 1, 1);

		List<Endosome> allEndosomes = new ArrayList<Endosome>();
		List<GridCell<Endosome>> cellList = nghCreator.getNeighborhood(true);
		for (GridCell<Endosome> gr : cellList) {
			// include if object is an endosome smaller than "this"
			// and is compatible
			for (Endosome end : gr.items()) {
				allEndosomes.add(end);
			}
		}
		// new list with just the compatible endosomes (same or compatible rabs)
		List<Endosome> endosomesToTether = new ArrayList<Endosome>();
		for (Endosome end : allEndosomes) {
			if (compatibles(this, (Endosome) end)) {
				endosomesToTether.add(end);
			}
		}
		// select the largest endosome
		Endosome largest = this;
		for (Endosome end : endosomesToTether) {
			if (end.size > largest.size) {
				largest = end;
			}
		}
		// assign the speed and heading of the largest endosome to the gropu
		for (Endosome end : endosomesToTether) {
			end.heading = largest.heading;
			end.speed = largest.speed;
			// System.out.println(endosomes_to_delete);
		}
	}

	public void fusion() {
		GridPoint pt = grid.getLocation(this);
		List<Endosome> endosomes_to_delete = new ArrayList<Endosome>();
		Iterable<Object> objectsAt = grid.getObjectsAt(pt.getX(), pt.getY());
		for (Object obj : objectsAt) {
			// include if object is an endosome smaller than "this"
			// and is compatible
			if (obj instanceof Endosome && obj != this
					&& ((Endosome) obj).volume <= this.volume) {
				if (compatibles(this, (Endosome) obj)) {
					endosomes_to_delete.add((Endosome) obj);
				}
				// System.out.println(endosomes_to_delete);
			}
		}
		for (Endosome endosome : endosomes_to_delete) {
			// System.out.println(this.area+"  AREAS A SUMAR AREAS A SUMAR"+
			// endosome.area);
			this.volume = this.volume + endosome.volume;
			this.area = this.area + endosome.area;
			initOrgProp.put("area", area);
			// System.out.println(this.area+"  AREAS FINAL");
			this.rabContent = sumRabContent(this, endosome);
			this.membraneContent = sumMembraneContent(this, endosome);
			this.solubleContent = sumSolubleContent(this, endosome);
			Context<Object> context = ContextUtils.getContext(endosome);
			context.remove(endosome);
		}
		double size = size();
		this.speed = 5 / size;

	}

	private HashMap<String, Double> sumRabContent(Endosome endosome1,
			Endosome endosome2) {
		// HashMap<String, Double> map3 = new HashMap<String, Double>();
		// map3.putAll(endosome1.rabContent);
		// map3.forEach((k, v) -> endosome2.rabContent.merge(k, v, (v1, v2) ->
		// v1 + v2));
		// return map3;

		HashMap<String, Double> rabSum = new HashMap<String, Double>();
		for (String key1 : endosome1.rabContent.keySet()) {
			if (endosome2.rabContent.containsKey(key1)) {
				double sum = endosome1.rabContent.get(key1)
						+ endosome2.rabContent.get(key1);
				rabSum.put(key1, sum);
			} else
				rabSum.put(key1, endosome1.rabContent.get(key1));
		}
		for (String key2 : endosome2.rabContent.keySet()) {
			if (!endosome1.rabContent.containsKey(key2)) {
				rabSum.put(key2, endosome2.rabContent.get(key2));
			}
		}

		// System.out.println("rabContentSum" + endosome1.rabContent);
		return rabSum;
	}

	private HashMap<String, Double> sumMembraneContent(Endosome endosome1,
			Endosome endosome2) {
		HashMap<String, Double> memSum = new HashMap<String, Double>();
		for (String key1 : endosome1.membraneContent.keySet()) {
			if (endosome2.membraneContent.containsKey(key1)) {
				double sum = endosome1.membraneContent.get(key1)
						+ endosome2.membraneContent.get(key1);
				memSum.put(key1, sum);
			} else
				memSum.put(key1, endosome1.membraneContent.get(key1));
		}
		for (String key2 : endosome2.membraneContent.keySet()) {
			if (!endosome1.membraneContent.containsKey(key2)) {
				double sum = endosome2.membraneContent.get(key2);
				memSum.put(key2, sum);
			}
		}
		// endosome1.membraneContent = memSum;
		// System.out.println("rabMembraneSum" + endosome1.membraneContent);
		return memSum;
	}

	private HashMap<String, Double> sumSolubleContent(Endosome endosome1,
			Endosome endosome2) {
		HashMap<String, Double> solSum = new HashMap<String, Double>();
		for (String key1 : endosome1.solubleContent.keySet()) {
			if (endosome2.solubleContent.containsKey(key1)) {
				double sum = endosome1.solubleContent.get(key1)
						+ endosome2.solubleContent.get(key1);
				solSum.put(key1, sum);
			} else
				solSum.put(key1, endosome1.solubleContent.get(key1));
		}
		for (String key2 : endosome2.solubleContent.keySet()) {
			if (!endosome1.solubleContent.containsKey(key2)) {
				double sum = endosome2.solubleContent.get(key2);
				solSum.put(key2, sum);
			}
		}
		// endosome1.solubleContent = solSum;
		// System.out.println("solubleContentSum" + endosome1.solubleContent);
		return solSum;
	}

	public void moveTowards() {
		// only move if we are not already in this grid location

		NdPoint myPoint = space.getLocation(this);
		double x = myPoint.getX() + Math.cos(heading * 2d * Math.PI / 360d)
				* this.speed;
		double y = myPoint.getY() + Math.sin(heading * 2d * Math.PI / 360d)
				* this.speed;
		if (y > 50 || y < 0) {
			heading = -heading;
			return;
		}
		space.moveTo(this, x, y);
		grid.moveTo(this, (int) x, (int) y);
	}

	// Split the endosome in two
	public void split() {
		String rabInTube = null;
		double vo = this.volume;
		double so = this.area;
		if (vo < 3 * 2 * Math.PI * Cell.rcyl * Cell.rcyl * Cell.rcyl)
			return; // if too small do not split. Volume of a cylinder of 2
					// cylinder radius long (almost a sphere)
		if (so < 2 * Cell.mincyl)
			return; // if the surface is less than two minimus tubules, abort
					// splitting
		if (so * so * so / (vo * vo) <= 36.01 * Math.PI)
			return; // organelle is an sphere
		// if s^3 / v^2 is equal to 36*PI then it is an sphere and cannot form a
		// tubule
		// if (vo / (so - 2 * Math.PI * Cell.rcyl * Cell.rcyl) <= Cell.rcyl / 2)
		// return;
		// too small volume for the surface for a tubule. Cannot form a tubule
		if (vo / (so - 2 * Math.PI * Cell.rcyl * Cell.rcyl) == Cell.rcyl / 2) {
			System.out.println("tubule");
			// return; // NEED TO GENERATE A SUBRUTINE TO SPLIT TUBULE-TUBULE
			// SUBRUTINE SHOULD SEND BACK VCYLINDER AND SCYLINDER-DONE. The same
			// procedures is used for both
		}

		// TRANSFORM IN A SUBRUTINE TO SPLIT VESICLE-TUBULE
		// procedure to split an organelle which is between a sphere (that
		// cannot be splitted) and a cylinder, that follow other rules.
		// calculate surface of smallest sphere

		double rsphere = Math.pow((vo * 3) / (4 * Math.PI), (1 / 3d));// calculate
		// the radius of the sphere with a given volume
		// System.out.println("vsphere");
		// System.out.println(vsphere);
		// System.out.println("rsphere");
		// System.out.println(rsphere);
		double ssphere = (4 * Math.PI * rsphere * rsphere);// area of a sphere
															// containing the
															// volume
		if ((so - ssphere) < Cell.mincyl * 1.9)
			return; // if not enough surface to contain the volume plus a
		// mininal tubuel, no split
		rabInTube = rabInTube(); // select a rab for the tubule
		if (rabInTube == null)
			return; // if non is selected, no fission
		// System.out.println("rabInTube" + rabInTube);

		/* initial minimum tubule to be formed */
		double scylinder = Cell.mincyl; // surface minimum cylinder 2*radius
										// cylinder high
		double vcylinder = 2 * Math.PI * Math.pow(Cell.rcyl, 3); // volume
		// minimum cylinder

		while ((so - ssphere - scylinder > 4 * Math.PI * Math.pow(Cell.rcyl, 2))
				// organelle area should be enough to cover the volume (ssphere)
				// to cover the cylinder already formed (scylinder) and to
				// elongate a two r cylinder (without caps)
				&& (rabContent.get(rabInTube) - scylinder > 4 * Math.PI
						* Math.pow(Cell.rcyl, 2))// the Rab area should b enough
				// to cover the minimum cylinder and to elongate a two r cylider
				&& (scylinder < 0.5 * so) // the area of the cylinder must not
											// be larger than 50% of the total
											// area
				&& ((vo - vcylinder - 2 * Math.PI * Math.pow(Cell.rcyl, 3))
						/ ((so - scylinder - 4 * Math.PI
								* Math.pow(Cell.rcyl, 2)) - 2 * Math.PI
								* Cell.rcyl * Cell.rcyl) > Cell.rcyl / 2)) {
			// volume left cannot be smaller than the volume
			// of the mincyl
			/*
			 * while there is enough membrane and enough rab surface, the tubule
			 * grows
			 */
			// System.out.println("in while" + (vo-vcylinder-2 * Math.PI*
			// Math.pow(Cell.rcyl, 3))/((so - scylinder-4 * Math.PI*
			// Math.pow(Cell.rcyl, 2)) - 2 * Math.PI * Cell.rcyl * Cell.rcyl));
			scylinder = scylinder + 4 * Math.PI * Math.pow(Cell.rcyl, 2);
			// add a cylinder without caps (the caps were considered in
			// the mincyl
			vcylinder = vcylinder + 2 * Math.PI * Math.pow(Cell.rcyl, 3);
			// add a volume
			// System.out.println(scylinder +"surface and volume"+ vcylinder);
		}
		// System.out.println("after while" + (vo-vcylinder)/((so - scylinder) -
		// 2 * Math.PI * Cell.rcyl * Cell.rcyl));

		/*
		 * the volume of the vesicle is formed subtracting the volume of the
		 * formed cylinder from the total volume idem for the area
		 * 
		 * From the information of vcylinder and scylinder, the organelle is
		 * splitted in two, a sphere and tubule (case 2) or in two almost
		 * tubules (a pice of the lateral surface must be used to close the
		 * tubules
		 */
		double vVesicle = vo - vcylinder;
		double sVesicle = so - scylinder;
		/*
		 * the final volume and surface of the vesicle are tested. If too small,
		 * the split aborts
		 */
		// if ((vVesicle < 2 * Math.PI * Cell.rcyl * Cell.rcyl *
		// Cell.rcyl)||(sVesicle < Cell.mincyl)) return;

		/*
		 * FORMATION 1ST ORGANELLE (referred as sphere) the rab-in-tubule of the
		 * tubule is substracted from the original rab-in-tube content of the
		 * organelle the final proportion of the rab-in-tubule in the vesicular
		 * organelle is obtained dividein by the total surface of the vesicle
		 */
		this.area = sVesicle;
		this.volume = vVesicle;
		/*
		 * CONTENT DISTRIBUTION Rab in the tubule is sustracted
		 */
		double rabLeft = this.rabContent.get(rabInTube) - scylinder;
		if (rabLeft < 0) {
			System.out.println(rabInTube + this.rabContent.get(rabInTube)
					+ "surfaceCy" + scylinder);
			System.out.println(rabContent);
		}
		this.rabContent.put(rabInTube, rabLeft);

		// MEMBRANE CONTENT IS DISTRIBUTED according rabTropism
		HashMap<String, Double> copyMembrane = new HashMap<String, Double>(
				this.membraneContent);
		// copyMembrane.putAll(this.membraneContent);
		for (String content : copyMembrane.keySet()) {
			if (!rabTropism.containsKey(content)
					|| !rabTropism.get(content).contains(rabInTube)) {// not a
				// specified tropism or no tropism for the rabInTube
				// hence, distribute according to
				// the surface ratio
				// For a membrane marker with no tropism for this split process,
				// the marker is at random located in one or
				// the other endosome, according to the sVesicle/so ratio
				if (content.equals("membraneMarker") &&
						(membraneContent.get("membreneMarker")> 0.9)) {
					if (Math.random() < sVesicle / so)
						this.membraneContent.put(content, 1.d);
				} 
				else {
					this.membraneContent.put(content, copyMembrane.get(content)
							* (sVesicle) / so);
				}

			} else {
				if (rabTropism.get(content).contains(rabInTube)
						|| rabTropism.get(content).contains("1")) {// a tropism
					// is specified.
					// If it is "1" always goes to the tubule.
					// if it is not "1" but is the Rab forming the tubule, goes
					// to the tubule

					if (copyMembrane.get(content) > scylinder) {
						this.membraneContent.put(content,
								copyMembrane.get(content) - scylinder);
					} else
						this.membraneContent.put(content, 0.0d);
				}
				if (rabTropism.get(content).contains("0")) {// if the tropism
					// is "0" goes to the sphere
					if (copyMembrane.get(content) > sVesicle) {
						this.membraneContent.put(content, sVesicle);
					} else
						this.membraneContent.put(content,
								copyMembrane.get(content));
				}
			}
		}
		// SOLUBLE CONTENT IS DISTRIBUTED according rabTropism
		HashMap<String, Double> copySoluble = new HashMap<String, Double>(
				this.solubleContent);
		// copySoluble.putAll(this.solubleContent);
		for (String content : copySoluble.keySet()) {
			if (!rabTropism.containsKey(content)
					|| !rabTropism.get(content).contains(rabInTube)) {// not a
				// specified tropism or no tropism for the rabInTube,
				// hence, distribute according to
				// the volume ratio
				if (content.equals("solubleMarker")&&
						(solubleContent.get("solubleMarker")> 0.9)) {
					if (Math.random() < vVesicle / vo)
						this.solubleContent.put(content, 1.d);
				} 
				else {
				this.solubleContent.put(content, copySoluble.get(content)
						* (vVesicle) / vo);
				}
			} else { // a tropism is specified. If it is "1" always goes to the
						// tubule.
				// if it is not "1" but is the Rab forming the tubule, goes to
				// the tubule
				if (rabTropism.get(content).contains(rabInTube)
						|| rabTropism.get(content).contains("1")) {

					if (copySoluble.get(content) > vcylinder) {
						this.solubleContent.put(content,
								copySoluble.get(content) - vcylinder);
					} else
						this.solubleContent.put(content, 0.0d);
				}
				if (rabTropism.get(content).contains("0")) { // if the tropism
																// is "0" goes
																// to the sphere

					if (copySoluble.get(content) > vVesicle) {
						this.solubleContent.put(content, vVesicle);
					} else
						this.solubleContent.put(content,
								copySoluble.get(content));
				}
			}

		}

		this.speed = 5 / size();
		moveTowards();

		/* the tubule is created as an independent endosome */
		HashMap<String, Double> newRabContent = new HashMap<String, Double>();
		newRabContent.put(rabInTube, scylinder);
		HashMap<String, Double> newInitOrgProp = new HashMap<String, Double>();
		newInitOrgProp.put("area", scylinder);
		newInitOrgProp.put("volume", vcylinder);
		HashMap<String, Double> newMembraneContent = new HashMap<String, Double>();
		for (String content : copyMembrane.keySet()) {
			newMembraneContent.put(content, copyMembrane.get(content)
					- this.membraneContent.get(content));
			// if (b.membraneContent.get(content) > b.area) {
			// //
			// System.out.println("redddddddddddddddddddddddddddd"+b.membraneContent.get(content)/b.area);
			//
			// }
		}
		HashMap<String, Double> newSolubleContent = new HashMap<String, Double>();
		for (String content : copySoluble.keySet()) {
			newSolubleContent.put(content, copySoluble.get(content)
					- this.solubleContent.get(content));
		}
		Endosome b = new Endosome(this.space, this.grid, newRabContent,
				newMembraneContent, newSolubleContent, newInitOrgProp);
		Context<Object> context = ContextUtils.getContext(this);
		context.add(b);
		b.area = scylinder;
		b.volume = vcylinder;
		b.size = Math.pow(b.volume * 3d / 4d / Math.PI, (1d / 3d));
		b.speed = 5 / b.size;
		b.heading = this.heading + 180;// contrary to the vesicle heading
		System.out
				.println("                                                 VESICLE B");
		System.out.println(b.area + " " + b.rabContent + " "
				+ b.membraneContent + " " + b.solubleContent + " "
				+ b.initOrgProp);
		NdPoint myPoint = space.getLocation(this);
		double x = myPoint.getX();
		double y = myPoint.getY();
		space.moveTo(b, x, y);
		grid.moveTo(b, (int) x, (int) y);
		moveTowards();
		// moveTowards();

	}

	public String rabInTube() {
		HashMap<String, Double> copyMap = new HashMap<String, Double>(
				this.rabContent);
		// copyMap.putAll(this.rabContent);
		String rab = null;
		// System.out.println("CopyMap "+copyMap);
		for (String rab1 : this.rabContent.keySet()) {
			if (copyMap.get(rab1) < Cell.mincyl) {
				copyMap.remove(rab1);
			}
		}
		if (copyMap.isEmpty()) {
			System.out.println("NINGUN RAB" + copyMap);
			return null;
		}

		if (copyMap.size() < 2) {

			for (String rab1 : copyMap.keySet()) {
				System.out.println("UNICO RAB " + copyMap);
				return rab1;
			}
		}

		else {
			while (rab == null) {
				for (String rab1 : copyMap.keySet()) {
					System.out.println(rab1 + tubuleTropism);
					if (Math.random() < tubuleTropism.get(rab1)) {
						// System.out.println(copyMap + "RabInTubeSelected" +
						// rab1);
						return rab1;
					}
				}
			}
		}

		return null;
	}

	public void internalVesicle() {

		// System.out.println("original endosome"+area+ " " + volume +
		// "original endosome rabs" + this.rabContent);
		// System.out.println("original Rab Cellular Content" + rabCell);
		double vo = this.volume;
		double so = this.area;
		// if (vo < 2 * Math.PI * Cell.rcyl * Cell.rcyl * Cell.rcyl)return; //if
		// too small do not split. Volume of a cylinder of 2 cylinder radius
		// long (almost a sphere)
		// if (so < 2 * Cell.mincyl) return; // if the surface is less than two
		// minimus tubules, abort splitting
		if (so * so * so / (vo * vo) <= 36.001 * Math.PI)
			return;
		// System.out.println("ESFERA" + so * so * so / (vo * vo));
		// if s^3 / v^2 is equal to 36*PI then it is an sphere and cannot form a
		// tubule

		double rIV = 15; // Internal vesicle radius
		double vIV = 4 / 3 * Math.PI * Math.pow(rIV, 3); // volume 14137.16
		double sIV = 4 * Math.PI * Math.pow(rIV, 2);// surface 2827.43

		if (vo < 2 * vIV)
			return;
		if (so < 2 * sIV)
			return;
		double vp = vo + vIV;
		double sp = so - sIV;
		if (sp * sp * sp / (vp * vp) <= 36 * Math.PI)
			return;// if the resulting surface cannot embrance the resulting
					// volume
		double rsphere = Math.pow((0.75 * vp / Math.PI), (1 / 3));
		double ssphere = 4 * Math.PI * Math.pow(rsphere, 2);
		if (ssphere >= sp)
			return;
		// double scylinder = so - ssphere;
		// if (scylinder < sIV * 1.10) return;//if the available membrane is
		// less than the surface of an internal vesicle, stop.
		// the factor 1.01 is to account for an increase in the surface required
		// because the increase in volume
		this.area = this.area - sIV;
		this.volume = this.volume + vIV;
		if (solubleContent.containsKey("mvb")) {
			double content = this.solubleContent.get("mvb") + 1;
			this.solubleContent.put("mvb", content);
		} else {
			this.solubleContent.put("mvb", 1d);
		}
//		Rabs proportinal to the sIV versus the surface of the organelle (so) are released into the cytosol 
		rabCell = Cell.getInstance().getRabCell();
		for (String key1 : this.rabContent.keySet()) {
			if (rabCell.containsKey(key1)) {
				double sum = this.rabContent.get(key1) * sIV / so
						+ rabCell.get(key1);
				rabCell.put(key1, sum);
			} else {
				double sum = this.rabContent.get(key1) * sIV / so;
				rabCell.put(key1, sum);
			}
		}

		// Cell.getInstance().setRabCell(rabCell);
		System.out.println("Rab Cellular Content"
				+ Cell.getInstance().getRabCell());
//		Rabs released to the cytosol are substracted from the rabContent of the organelle
		for (String rab : this.rabContent.keySet()) {
			double content1 = this.rabContent.get(rab) * (so - sIV) / so;
			this.rabContent.put(rab, content1);
		}
//		Membrane content is degraded
		for (String content : this.membraneContent.keySet()) {
			double mem = this.membraneContent.get(content) * (so - sIV) / so;
			this.membraneContent.put(content, mem);
		}
		// System.out.println("Left in endosome" +area+ "  " +volume+
		// "Rab left in endosome" + rabContent);
//		Free membrane is added to the cell
		cellMembrane = Cell.getInstance().gettMembrane() + sIV;
		Cell.getInstance().settMembrane(cellMembrane);
		System.out.println("total cell membrane"
				+ Cell.getInstance().gettMembrane());

	}

	public void recycle() {
		NdPoint myPoint = space.getLocation(this);
		// double x = myPoint.getX();
		double y = myPoint.getY();
		if (y < 49)
			return;
		double recyRabA = 0.0;
		double recyRabC = 0.0;
		if (rabContent.containsKey("RabA")) {
			recyRabA = rabContent.get("RabA") / area;
		}
		if (rabContent.containsKey("RabC")) {
			recyRabC = rabContent.get("RabC") / area;
		}
		double recyProb = 0.1 * recyRabA + recyRabC;
		if (Math.random() >= recyProb)
			return; // if not near the PM
					// or without a recycling Rab return
					// recycling Rabs are RabA (Rab5) and RabC (Rab11)
		else {
			// RECYCLE
			// Recycle membrane content
			HashMap<String, Double> membraneRecycle = Cell.getInstance()
					.getMembraneRecycle();
			for (String key1 : this.membraneContent.keySet()) {
				if (membraneRecycle.containsKey(key1)) {
					double sum = membraneRecycle.get(key1)
							+ membraneContent.get(key1);
					membraneRecycle.put(key1, sum);
				} else {
					membraneRecycle.put(key1, membraneContent.get(key1));
				}
			}

			//Cell.getInstance().setMembraneRecycle(membraneRecycle);
			this.membraneContent.clear();
			System.out.println("membrane Recycled"
					+ Cell.getInstance().getMembraneRecycle());

			// Recycle soluble content
			HashMap<String, Double> solubleRecycle = Cell.getInstance()
					.getSolubleRecycle();
			for (String key1 : this.solubleContent.keySet()) {
				if (solubleRecycle.containsKey(key1)) {
					double sum = solubleRecycle.get(key1)
							+ solubleContent.get(key1);
					solubleRecycle.put(key1, sum);
				} else {
					solubleRecycle.put(key1, solubleContent.get(key1));
				}
			}
			//Cell.getInstance().setSolubleRecycle(solubleRecycle);
			this.solubleContent.clear();
			System.out.println("soluble Recycled"
					+ Cell.getInstance().getSolubleRecycle());
		}
	}

	public void uptake() {
		Cell cell = Cell.getInstance();
		double tMembrane = cell.gettMembrane();
		rabCell = cell.getRabCell();
		// System.out.println("CELL RAB "+ rabCell+ "TMEMBRANE "+ tMembrane);
		if (!rabCell.containsKey("RabA"))
			return;
		double rabCellA = rabCell.get("RabA");
		if (tMembrane < Cell.sEndo || rabCellA < Cell.sEndo) {
			return;
		}
		/*
		 * Endosome budEnd = new Endosome(this.space, this.grid, null, null,
		 * null); Context<Object> context = ContextUtils.getContext(this);
		 * context.add(budEnd);
		 */
		HashMap<String, Double> rabContent = new HashMap<String, Double>(InitialOrganelles.getInstance()
				.getInitRabContent().get("kind1"));
		HashMap<String, Double> membraneContent = new HashMap<String, Double>(InitialOrganelles
				.getInstance().getInitMembraneContent().get("kind1"));
		HashMap<String, Double> solubleContent = new HashMap<String, Double>(InitialOrganelles
				.getInstance().getInitSolubleContent().get("kind1"));
		HashMap<String, Double> initOrgProp = new HashMap<String, Double>(InitialOrganelles.getInstance()
				.getInitOrgProp().get("kind1"));

		Context<Object> context = ContextUtils.getContext(this);
		Endosome bud = new Endosome(space, grid, rabContent, membraneContent,
				solubleContent, initOrgProp);
		context.add(bud);
		// context.add(new Endosome(space, grid, rabContent, membraneContent,
		// solubleContent));
		System.out.println(membraneContent + "NEW ENDOSOME UPTAKE"
				+ solubleContent + rabContent);
		// tMembrane = Cell.getInstance().gettMembrane();
		tMembrane = tMembrane - Cell.sEndo;
		rabCellA = rabCellA - Cell.sEndo;
		rabCell.put("RabA", rabCellA);
		Cell.getInstance().settMembrane(tMembrane);

		// Cell.getInstance().setRabCell(rabCell);

		bud.area = Cell.sEndo;
		bud.volume = Cell.vEndo;
		bud.size = Cell.rEndo;// radius of a sphere with the volume of the
								// cylinder
		bud.speed = 5 / bud.size;
		bud.heading = -90;// contrary to the vesicle heading
		// NdPoint myPoint = space.getLocation(bud);
		double rnd = Math.random();
		space.moveTo(bud, rnd * 50, 48.0);
		grid.moveTo(bud, (int) rnd * 50, 48);

		moveTowards();
		// moveTowards();

	}

	/*
	 * mtDirection decides if the endosome is going to muve to the (-) end of
	 * the MT (dyneine like or to the plus end (kinesine like). -1 goes to the
	 * nucleus, 1 to the PM
	 */
	public int mtDirection() {
		double mtd = 0d;
		int mtDirection = 0;
		for (String rab : rabContent.keySet()) {
			mtd = mtd + mtTropism.get(rab) * rabContent.get(rab) / area;
		}
		if (mtd > Math.random() * 2 - 1)
			return mtDirection = -1;
		else
			return mtDirection = 1;
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
			// System.out.println("mHCI content" + red);
			return blue;
		}
		if (solubleContent.containsKey(contentPlot)) {
			double blue = solubleContent.get(contentPlot) / volume;
			// System.out.println("mHCI content" + red);
			return blue;
		} else
			return 0;
	}

	// Edge color coded by Rabs
	// RabA (5) Green (0,255,0)
	// RabB (22) Red (255,0,0)
	// RabC (7) Olive (128,128,0)
	// RabD (11) Blue (0,0,255)
	// RabE (5) Purple (128,0,128)
	//
	public double getEdgeRed() {
		// double red = 0.0;
		String edgePlot = CellProperties.getInstance().getColorRab().get("red");

		if (rabContent.containsKey(edgePlot)) {
			double red = rabContent.get(edgePlot) / area;
			// System.out.println("mHCI content" + red);
			return red;
		} else
			return 0;
	}

	public double getEdgeGreen() {
		// double red = 0.0;
		String edgePlot = CellProperties.getInstance().getColorRab()
				.get("green");

		if (rabContent.containsKey(edgePlot)) {
			double green = rabContent.get(edgePlot) / area;
			// System.out.println("mHCI content" + red);
			return green;
		} else
			return 0;
	}

	public double getEdgeBlue() {
		// double red = 0.0;
		String edgePlot = CellProperties.getInstance().getColorRab()
				.get("blue");

		if (rabContent.containsKey(edgePlot)) {
			double blue = rabContent.get(edgePlot) / area;
			// System.out.println("mHCI content" + red);
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
		Parameters params = RunEnvironment.getInstance().getParameters();
		String rab = (String) params.getValue("Rab");
		String solCont = (String) params.getValue("soluble");
		Double sc = null;
		Double rc = null;
		if (solCont != null && rab != null) {
			if (solubleContent.containsKey(solCont)) {
				sc = solubleContent.get(solCont);
			} else
				return 0;
			if (rabContent.containsKey(rab)) {
				rc = rabContent.get(rab);
			} else
				return 0;
			if (sc != null && rc != null) {
				double solContRab = sc * rc / this.area;
				return solContRab;
			}
		}
		return 0;
	}

	public double getSolContRab2() { // (String solCont, String rab){
		Parameters params = RunEnvironment.getInstance().getParameters();
		String rab = (String) params.getValue("Rab2");
		String solCont = (String) params.getValue("soluble");
		Double sc = null;
		Double rc = null;
		if (solCont != null && rab != null) {
			if (solubleContent.containsKey(solCont)) {
				sc = solubleContent.get(solCont);
			} else
				return 0;
			if (rabContent.containsKey(rab)) {
				rc = rabContent.get(rab);
			} else
				return 0;
			if (sc != null && rc != null) {
				double solContRab = sc * rc / this.area;
				return solContRab;
			}
		}
		return 0;
	}

	public double getMemContRab(String memCont, String rab) {
		double memContRab = membraneContent.get(memCont) * rabContent.get(rab)
				/ this.area;
		return memContRab;
	}

}
