package immunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ISchedulableAction;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class PlasmaMembrane {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;

	// a single Cell is created
	private static PlasmaMembrane instance;
	static {
		instance = new PlasmaMembrane(space, grid);
	}
	

	public HashMap<String, Double> membraneRecycle = new HashMap<String, Double>(ModelProperties.getInstance().getInitPMmembraneRecycle()); // contains membrane recycled 
	public HashMap<String, Double> solubleRecycle = new HashMap<String, Double>();// contains soluble recycled
	public int pmcolor = 0;
	public int red = 0;
	public int green = 0;	
	public int blue = 0;
	private double plasmaMembraneVolume;
	private double plasmaMembraneArea;
	private double initialPlasmaMembraneVolume;
	private double initialPlasmaMembraneArea;
//	public int area = (int) (1500*400*(1/Cell.orgScale)*(1/Cell.orgScale)); //ModelProperties.getInstance().getPlasmaMembraneProperties().get("plasmaMembraneArea");// 
//	public int volume = (int) (1500*400*1000*(1/Cell.orgScale)*(1/Cell.orgScale)*(1/Cell.orgScale)); //ModelProperties.getInstance().getPlasmaMembraneProperties().get("plasmaMembraneVolume");//
	TreeMap<Integer, HashMap<String, Double>> plasmaMembraneTimeSeries = new TreeMap<Integer, HashMap<String, Double>>();
	public String plasmaMembraneCopasi = ModelProperties.getInstance().getCopasiFiles().get("plasmaMembraneCopasi");
// nm2 1500nm x 400nm. Space in repast at scale =1 and arbitrary height of the space projected
//	in 2D




	// Constructor
	public PlasmaMembrane(ContinuousSpace<Object> space, Grid<Object> grid) {
// Contains the contents that are in the plasma membrane.  It is modified by Endosome that uses and changes the PM
// contents.	tMembranes, membrane and soluble content recycling

		ModelProperties modelProperties = ModelProperties.getInstance();
		plasmaMembraneArea = ModelProperties.getInstance().getPlasmaMembraneProperties().get("plasmaMembraneArea");// 
		initialPlasmaMembraneArea = ModelProperties.getInstance().getPlasmaMembraneProperties().get("plasmaMembraneArea");// 	
		plasmaMembraneVolume = ModelProperties.getInstance().getPlasmaMembraneProperties().get("plasmaMembraneVolume");//
		initialPlasmaMembraneVolume = ModelProperties.getInstance().getPlasmaMembraneProperties().get("plasmaMembraneVolume");//

//		plasmaMembraneTimeSeries = null;
//		
//		membraneRecycle.putAll(modelProperties.initPMmembraneRecycle);
// PM now are in the csv file as proportions of the PM area and need to be multiplied by the area		
		for (String met : modelProperties.initPMmembraneRecycle.keySet() ){
		membraneRecycle.put(met, modelProperties.initPMmembraneRecycle.get(met)*plasmaMembraneArea);
		}
		System.out.println("PM membraneRecycle "+ membraneRecycle);
		for (String met : modelProperties.initPMsolubleRecycle.keySet() ){
		solubleRecycle.put(met, modelProperties.initPMsolubleRecycle.get(met)*plasmaMembraneVolume);
		}
		System.out.println("PM solubleRecycle "+ solubleRecycle);		
//		for (String met : modelProperties.solubleMet ){
//		solubleRecycle.put(met,  0.0);
//		}
//		System.out.println("solubleRecycle "+solubleRecycle);		
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		changeColor();
//		this.membraneRecycle = PlasmaMembrane.getInstance().getMembraneRecycle();
//		this.solubleRecycle = PlasmaMembrane.getInstance().getSolubleRecycle();
//		this.plasmaMembraneTimeSeries=PlasmaMembrane.getInstance().getPlasmaMembraneTimeSeries();
		if (Math.random() < 0 && plasmaMembraneCopasi.endsWith(".cps"))PlasmaMembraneCopasiStep.antPresTimeSeriesLoad(this);
//		this.changeColor();

		}
	
	public void changeColor() {
		double c1 = 0d;
		{
		c1 = membraneRecycle.get("chol");
		c1 = c1/plasmaMembraneArea;
//		if (c1>1) c1=1;
		pmcolor = (int) (c1*255);
		}

//		System.out.println(PlasmaMembrane.getInstance().getMembraneRecycle()+"\n COLOR PLASMA  " + pmcolor+" " + pmcolor);
	}
	

	// GETTERS AND SETTERS (to get and set Cell contents)
	public static PlasmaMembrane getInstance() {
		return instance;
	}
		public HashMap<String, Double> getMembraneRecycle() {
		return membraneRecycle;
	}

	public HashMap<String, Double> getSolubleRecycle() {
		return solubleRecycle;
	}

	public int getPmcolor() {
		return pmcolor;
	}

	public double getPlasmaMembraneArea() {
		return plasmaMembraneArea;
	}

	public double getPlasmaMembraneVolume() {
		return plasmaMembraneVolume;
	}
	
	public final TreeMap<Integer, HashMap<String, Double>> getPlasmaMembraneTimeSeries() {
		return plasmaMembraneTimeSeries;
	}
	
	public final void setPlasmaMembraneArea(double plasmaMembraneArea) {
		this.plasmaMembraneArea = plasmaMembraneArea;
	}

	public final double getInitialPlasmaMembraneVolume() {
		return initialPlasmaMembraneVolume;
	}

	public final double getInitialPlasmaMembraneArea() {
		return initialPlasmaMembraneArea;
	}



}