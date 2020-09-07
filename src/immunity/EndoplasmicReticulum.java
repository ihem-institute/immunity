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

public class EndoplasmicReticulum {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;

	// a single Cell is created
	private static EndoplasmicReticulum instance;
	static {
		instance = new EndoplasmicReticulum(space, grid);
	}
	

	public HashMap<String, Double> membraneRecycle = new HashMap<String, Double>(ModelProperties.getInstance().getInitERmembraneRecycle()); // contains membrane recycled 
	public HashMap<String, Double> solubleRecycle = new HashMap<String, Double>();// contains soluble recycled
	public int ercolor = 0;
	public int red = 0;
	public int green = 0;	
	public int blue = 0;
	private double endoplasmicReticulumVolume;
	private double endoplasmicReticulumArea;
	private double initialendoplasmicReticulumVolume;
	private double initialendoplasmicReticulumArea;
//	public int area = (int) (1500*400*(1/Cell.orgScale)*(1/Cell.orgScale)); //ModelProperties.getInstance().getendoplasmicReticulumProperties().get("endoplasmicReticulumArea");// 
//	public int volume = (int) (1500*400*1000*(1/Cell.orgScale)*(1/Cell.orgScale)*(1/Cell.orgScale)); //ModelProperties.getInstance().getendoplasmicReticulumProperties().get("endoplasmicReticulumVolume");//
	TreeMap<Integer, HashMap<String, Double>> endoplasmicReticulumTimeSeries = new TreeMap<Integer, HashMap<String, Double>>();
	public String endoplasmicReticulumCopasi = ModelProperties.getInstance().getCopasiFiles().get("endoplasmicReticulumCopasi");
// nm2 1500nm x 400nm. Space in repast at scale =1 and arbitrary height of the space projected
//	in 2D




	// Constructor
	public EndoplasmicReticulum(ContinuousSpace<Object> space, Grid<Object> grid) {
// Contains the contents that are in the plasma membrane.  It is modified by Endosome that uses and changes the ER
// contents.	tMembranes, membrane and soluble content recycling

		ModelProperties modelProperties = ModelProperties.getInstance();
		endoplasmicReticulumArea = ModelProperties.getInstance().getEndoplasmicReticulumProperties().get("endoplasmicReticulumArea");// 
		initialendoplasmicReticulumArea = ModelProperties.getInstance().getEndoplasmicReticulumProperties().get("endoplasmicReticulumArea");// 	
		endoplasmicReticulumVolume = ModelProperties.getInstance().getEndoplasmicReticulumProperties().get("endoplasmicReticulumVolume");//
		initialendoplasmicReticulumVolume = ModelProperties.getInstance().getEndoplasmicReticulumProperties().get("endoplasmicReticulumVolume");//

//		endoplasmicReticulumTimeSeries = null;
//		
//		membraneRecycle.putAll(modelProperties.initERmembraneRecycle);
// ER now are in the csv file as proportions of the ER area and need to be multiplied by the area		
		for (String met : modelProperties.initERmembraneRecycle.keySet() ){
		double value = modelProperties.initERmembraneRecycle.get(met)*endoplasmicReticulumArea;
		membraneRecycle.put(met, value);
		}
		System.out.println("ER membraneRecycle "+ membraneRecycle + endoplasmicReticulumArea);
		for (String met : modelProperties.initERsolubleRecycle.keySet() ){
		solubleRecycle.put(met, modelProperties.initERsolubleRecycle.get(met)*endoplasmicReticulumVolume);
		}
		System.out.println("ER solubleRecycle "+ solubleRecycle);		
//		for (String met : modelProperties.solubleMet ){
//		solubleRecycle.put(met,  0.0);
//		}
//		System.out.println("solubleRecycle "+solubleRecycle);		
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		changeColor();
//		this.membraneRecycle = endoplasmicReticulum.getInstance().getMembraneRecycle();
//		this.solubleRecycle = endoplasmicReticulum.getInstance().getSolubleRecycle();
//		this.endoplasmicReticulumTimeSeries=endoplasmicReticulum.getInstance().getendoplasmicReticulumTimeSeries();
//		if (Math.random() < 0 && endoplasmicReticulumCopasi.endsWith(".cps"))endoplasmicReticulumCopasiStep.antPresTimeSeriesLoad(this);
//		this.changeColor();

		}
	
	public void changeColor() {
		double c1 = 0d;
		{
		c1 = membraneRecycle.get("Tf");
		c1 = 20*c1/endoplasmicReticulumArea;
		if (c1>1) c1=1;
		ercolor = (int) (c1*255);
		}

//		System.out.println(endoplasmicReticulum.getInstance().getMembraneRecycle()+"\n COLOR PLASMA  " + ERcolor+" " + ercolor);
	}
	

	// GETTERS AND SETTERS (to get and set Cell contents)
	public static EndoplasmicReticulum getInstance() {
		return instance;
	}
		public HashMap<String, Double> getMembraneRecycle() {
		return membraneRecycle;
	}

	public HashMap<String, Double> getSolubleRecycle() {
		return solubleRecycle;
	}

	public int getErcolor() {
		return ercolor;
	}

	public double getendoplasmicReticulumArea() {
		return endoplasmicReticulumArea;
	}
	
	public void setEndoplasmicReticulumArea(double endoplasmicReticulumArea) {
		this.endoplasmicReticulumArea = endoplasmicReticulumArea;
		
	}


	public double getendoplasmicReticulumVolume() {
		return endoplasmicReticulumVolume;
	}
	
	public final TreeMap<Integer, HashMap<String, Double>> getendoplasmicReticulumTimeSeries() {
		return endoplasmicReticulumTimeSeries;
	}
	
	public final void setendoplasmicReticulumArea(double endoplasmicReticulumArea) {
		this.endoplasmicReticulumArea = endoplasmicReticulumArea;
	}

	public final double getInitialendoplasmicReticulumVolume() {
		return initialendoplasmicReticulumVolume;
	}

	public final double getInitialendoplasmicReticulumArea() {
		return initialendoplasmicReticulumArea;
	}


}