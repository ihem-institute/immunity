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
	

	public HashMap<String, Double> membraneRecycle = new HashMap<String, Double>(CellProperties.getInstance().getMembraneRecycle()); // contains membrane recycled 
	public HashMap<String, Double> solubleRecycle = new HashMap<String, Double>();// contains soluble recycled
	public int pmcolor = 0;
	public int red = 0;
	public int green = 0;	
	public int blue = 0;
	public double c2 = CellProperties.getInstance().getMembraneRecycle().get("pLANCL2");


	// Constructor
	public PlasmaMembrane(ContinuousSpace<Object> space, Grid<Object> grid) {
// Contains the contents that are in the cell.  It is modified by Endosome that uses and changes the cell
// contents.	tMembranes, membrane and soluble content recycling, cytosolic Rabs	
		CellProperties cellProperties = CellProperties.getInstance();
		membraneRecycle.putAll(cellProperties.membraneRecycle);
		System.out.println("membraneRecycle "+ membraneRecycle);
		for (String met : cellProperties.solubleMet ){
		solubleRecycle.put(met,  0.0);
		}
			
	}
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		if (Math.random() < 0.001){
			System.out.println("llamo PM LANCL2");
		//	PlasmaMembraneLANCL2metabolismStep.LANCL2metabolism(PlasmaMembrane.getInstance());			
		}
		this.changeColor();

		}
	
	public void changeColor() {
		double c1 = 0;
		if (PlasmaMembrane.getInstance().getMembraneRecycle().containsKey("pLANCL2")){
		c1 = PlasmaMembrane.getInstance().getMembraneRecycle().get("pLANCL2");
		}
		this.pmcolor = (int) (c1/c2*240);
		System.out.println(PlasmaMembrane.getInstance().getMembraneRecycle()+"\n COLOR PLASMA  " + pmcolor+" " + c1 +" " + c2);
		}
	
/*	I will consider only red for pLANCL2.  PROBLEM, what could be the area of the PM?
 * public double getRed() {
		// double red = 0.0;
		String contentPlot = CellProperties.getInstance().getColorContent()
				.get("red");

		if (membraneRecycle.containsKey(contentPlot)) {
			double red = membraneRecycle.get(contentPlot) / area;
			return red;
		} else {return 0;}
	}

	public double getGreen() {
		// double red = 0.0;
		String contentPlot = CellProperties.getInstance().getColorContent()
				.get("green");

		if (membraneRecycle.containsKey(contentPlot)) {
			double green = membraneRecycle.get(contentPlot) / area;
			// System.out.println("mHCI content" + red);
			return green;

		} else  {return 0;}
	}

	public double getBlue() {
		// double red = 0.0;
		String contentPlot = CellProperties.getInstance().getColorContent()
				.get("blue");

		if (membraneRecycle.containsKey(contentPlot)) {
			double blue = membraneRecycle.get(contentPlot) / area;
			return blue;
			} else {return 0;}
	}

*/	
	// GETTERS AND SETTERS (to get and set Cell contents)
	public static PlasmaMembrane getInstance() {
		return instance;
	}

	
//	public void setRabCell(HashMap<String, Double> rabCell) {
//	this.rabCell = rabCell;
//}

	public HashMap<String, Double> getMembraneRecycle() {
		return membraneRecycle;
	}

//	public void setMembraneRecycle(HashMap<String, Double> membraneRecycle) {
//		this.membraneRecycle = membraneRecycle;
//	}

	public HashMap<String, Double> getSolubleRecycle() {
		return solubleRecycle;
	}

	public int getPmcolor() {
		return pmcolor;
	}

//	public void setSolubleRecycle(HashMap<String, Double> solubleRecycle) {
//		this.solubleRecycle = solubleRecycle;
//	}


}