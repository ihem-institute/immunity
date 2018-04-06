package immunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	

	public HashMap<String, Double> membraneRecycle = new HashMap<String, Double>(CellProperties.getInstance().getInitPMmembraneRecycle()); // contains membrane recycled 
	public HashMap<String, Double> solubleRecycle = new HashMap<String, Double>();// contains soluble recycled
	public int pmcolor = 0;
	public int red = 0;
	public int green = 0;	
	public int blue = 0;
	public int area = (int) (1500*400*(1/Cell.orgScale)*(1/Cell.orgScale)); 
// nm2 1500nm x 400nm. Space in repast at scale =1 and arbitrary height of the space projected
//	in 2D




	// Constructor
	public PlasmaMembrane(ContinuousSpace<Object> space, Grid<Object> grid) {
// Contains the contents that are in the cell.  It is modified by Endosome that uses and changes the cell
// contents.	tMembranes, membrane and soluble content recycling, cytosolic Rabs	

		CellProperties cellProperties = CellProperties.getInstance();
//		
//		membraneRecycle.putAll(cellProperties.initPMmembraneRecycle);
// PM now are in the csv file as proportions of the PM area and need to be multiplied by the area		
		for (String met : cellProperties.initPMmembraneRecycle.keySet() ){
		membraneRecycle.put(met, cellProperties.initPMmembraneRecycle.get(met) * area);
		}
		System.out.println("membraneRecycle "+ membraneRecycle);
		for (String met : cellProperties.solubleMet ){
		solubleRecycle.put(met,  0.0);
		}
			
	}
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		this.membraneRecycle = PlasmaMembrane.getInstance().getMembraneRecycle();
		this.solubleRecycle = PlasmaMembrane.getInstance().getSolubleRecycle();
//		this.changeColor();

		}
	
/*	public void changeColor() {
		double c1 = 0d;
		if (PlasmaMembrane.getInstance().getMembraneRecycle().containsKey("XX")){
		c1 = PlasmaMembrane.getInstance().getMembraneRecycle().get("XX")/area;
		this.pmcolor = (int) (c1*255);
		}
		else this.pmcolor = 0;
		System.out.println(PlasmaMembrane.getInstance().getMembraneRecycle()+"\n COLOR PLASMA  " + pmcolor+" " + pmcolor);
	}
*/	

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





}