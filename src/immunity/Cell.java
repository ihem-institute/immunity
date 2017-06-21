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

public class Cell {
	// a single Cell is created
	private static Cell instance;
	static {
		instance = new Cell();
	}
	
//	Cell characteristics
	public static double rcyl = CellProperties.getInstance().getCellK().get("rcyl");//10.0; // radius tubule
//	public radius new endosome formed by uptake = radius Kind1, generally 20.0; // 
	public static double mincyl = 6 * Math.PI * rcyl * rcyl; // surface minimum cylinder
	// two radius large (almost a sphere)
	public static double rIV = 15; // Internal vesicle radius
//	public static double vEndo = 4d / 3d * Math.PI * Math.pow(rEndo, 3); //volume new endosome
//	public static double sEndo = 4d * Math.PI * Math.pow(rEndo, 2); // surface new endosome
	// mincyl surface = 1884.95 volume = 6283.18
	public static double orgScale = CellProperties.getInstance().getCellK().get("orgScale");
//  When orgScale=1 zoom =0, when > 1 zoom in , when <1 zoom out
//	global cell and non-cell quantities
	public double tMembrane;// membrane that is not used in endosomes
	public HashMap<String, Double> rabCell = new HashMap<String, Double>();// contains rabs free in cytosol
	public HashMap<String, Double> membraneRecycle = new HashMap<String, Double>(); // contains membrane recycled 
	public HashMap<String, Double> solubleRecycle = new HashMap<String, Double>();// contains soluble recycled



	// Constructor
	public Cell() {
// Contains the contents that are in the cell.  It is modified by Endosome that uses and changes the cell
// contents.	tMembranes, membrane and soluble content recycling, cytosolic Rabs	
		CellProperties cellProperties = CellProperties.getInstance();
		for (String sol : cellProperties.solubleMet) {
			solubleRecycle.put(sol, 0d);
		}
		for (String sol : cellProperties.membraneMet) {
			membraneRecycle.put(sol, 0d);
		}	
		rabCell.putAll(cellProperties.initRabCell);
	}

	
	// GETTERS AND SETTERS (to get and set Cell contents)
	public static Cell getInstance() {
		return instance;
	}

	public double gettMembrane() {
		return tMembrane;
	}

	public void settMembrane(double tMembrane) {
		this.tMembrane = tMembrane;
	}
	
	public HashMap<String, Double> getRabCell() {
		return rabCell;
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

//	public void setSolubleRecycle(HashMap<String, Double> solubleRecycle) {
//		this.solubleRecycle = solubleRecycle;
//	}


}