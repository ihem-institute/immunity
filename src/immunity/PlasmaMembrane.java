package immunity;

import java.util.HashMap;
import java.util.TreeMap;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class PlasmaMembrane {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;

	// a single Cell is created
	private static PlasmaMembrane instance;
	static {
		instance = new PlasmaMembrane(space, grid);
	}
	

	public HashMap<String, Double> membraneRecycle = new HashMap<String, Double>(CellProperties.getInstance().getInitPMmembraneRecycle()); // contains membrane recycled 
	public HashMap<String, Double> solubleRecycle = new HashMap<String, Double>();// contains soluble recycled
	public int pmcolor = 0;
	public int red = 0;
	public int green = 0;	
	public int blue = 0;
	private double plasmaMembraneVolume;
	private double plasmaMembraneArea;
	TreeMap<Integer, HashMap<String, Double>> plasmaMembraneTimeSeries = new TreeMap<Integer, HashMap<String, Double>>();
	public String plasmaMembraneCopasi = CellProperties.getInstance().getCopasiFiles().get("plasmaMembraneCopasi");
	// nm2 1500nm x 400nm. Space in repast at scale =1 and arbitrary height of the space projected
	//	in 2D

	// Constructor
	public PlasmaMembrane(ContinuousSpace<Object> space, Grid<Object> grid) {
// Contains the contents that are in the cell.  It is modified by Endosome that uses and changes the cell
// contents.	tMembranes, membrane and soluble content recycling, cytosolic Rabs	

		CellProperties cellProperties = CellProperties.getInstance();
		plasmaMembraneArea = cellProperties.getPlasmaMembraneProperties().get("plasmaMembraneArea");// 
		plasmaMembraneVolume = cellProperties.getPlasmaMembraneProperties().get("plasmaMembraneVolume");//

// PM now are in the csv file as proportions of the PM area and need to be multiplied by the area		
		for (String met : cellProperties.initPMmembraneRecycle.keySet() ){
		membraneRecycle.put(met, cellProperties.initPMmembraneRecycle.get(met)*this.plasmaMembraneArea);
		}
		System.out.println("PM membraneRecycle "+ membraneRecycle);
		for (String met : cellProperties.initPMsolubleRecycle.keySet() ){
		solubleRecycle.put(met, cellProperties.initPMsolubleRecycle.get(met)*this.plasmaMembraneVolume);
		}
		System.out.println("PM solubleRecycle "+ solubleRecycle);			
	}
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		changeColor();
		this.membraneRecycle = PlasmaMembrane.getInstance().getMembraneRecycle();
		this.solubleRecycle = PlasmaMembrane.getInstance().getSolubleRecycle();
		this.plasmaMembraneTimeSeries=PlasmaMembrane.getInstance().getPlasmaMembraneTimeSeries();
		String name =  CellProperties.getInstance().getCopasiFiles().get("plasmaMembraneCopasi");
		if (Math.random() < 1 && name.endsWith(".cps"))PlasmaMembraneCopasiStep.antPresTimeSeriesLoad(PlasmaMembrane.getInstance());

		}
	
	public void changeColor() {
		double c1 = 0d;
		{
		c1 = c1/this.plasmaMembraneArea;
		if (c1 > 1d) c1 = 1d;
		this.pmcolor = (int) (c1*255);
		}

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


}