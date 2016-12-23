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
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	public static double rcyl = 10.0;
	public static double rEndo = 20.0;
	public static double mincyl = 6 * Math.PI * rcyl * rcyl;
	public static double vEndo = 4d / 3d * Math.PI * Math.pow(rEndo, 3);
	public static double sEndo = 4d * Math.PI * Math.pow(rEndo, 2);
	// mincyl surface = 1884.95 volume = 6283.18
	private static Cell instance;
	public double tMembrane;
	public HashMap<String, Double> rabCell = new HashMap();
	public HashMap<String, Double> membraneRecycle = new HashMap();
	public HashMap<String, Double> solubleRecycle = new HashMap();
	static {
		instance = new Cell();
	}

	/*
	 * public List<Endosome> pickEndosome(){ List<Endosome> endosome = new
	 * ArrayList<Endosome>(); for (Object obj : grid.getObjects()) { if (obj
	 * instanceof Endosome) { endosome.add((Endosome) obj); return endosome; } }
	 * return endosome; }
	 */
	public Cell() {
		double tMembrane = 0; // hidden constructor
		rabCell.put("RabA", 0.0d);
		rabCell.put("RabB", 0.0d);
		membraneRecycle.put(null, null);
		solubleRecycle.put(null, null);
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		//System.out.println("TOTAL CELL MEMBRANE" + tMembrane);
		//uptake();
	}

/*	public void uptake() {
		if (tMembrane < sEndo || rabCell.get("RabA") < sEndo)
			return;
		/*
		 * Endosome budEnd = new Endosome(this.space, this.grid, null, null,
		 * null); Context<Object> context = ContextUtils.getContext(this);
		 * context.add(budEnd);
		 
		HashMap<String, Double> rabContent = new HashMap<String, Double>();
		HashMap<String, Double> membraneContent = new HashMap<String, Double>();
		HashMap<String, Double> solubleContent = new HashMap<String, Double>();
		rabContent.put("RabA", 4d * Math.PI * 30d * 30d);
		membraneContent.put("Tf", 4d * Math.PI * 30d * 30d);
		solubleContent.put("dextran", 4d / 3d * Math.PI * 30d * 30d * 30d);
		Context<Object> context = ContextUtils.getContext(this);
		// Endosome budEndo = new Endosome(this.space, this.grid, null, null,
		// null);
		// context.add(budEndo);
		context.add(new Endosome(space, grid, rabContent, membraneContent,
				solubleContent));
		System.out.println(membraneContent + "NEW ENDOSOME UPTAKE"
				+ solubleContent + rabContent);
	}
*/
	public static Cell getInstance() {
		return instance;
	}

	public HashMap<String, Double> getRabCell() {
		return rabCell;
	}

	public double gettMembrane() {
		return tMembrane;
	}

	public void settMembrane(double tMembrane) {
		this.tMembrane = tMembrane;
	}

	public void setRabCell(HashMap<String, Double> rabCell) {
		this.rabCell = rabCell;
	}

	public HashMap<String, Double> getMembraneRecycle() {
		return membraneRecycle;
	}

	public void setMembraneRecycle(HashMap<String, Double> membraneRecycle) {
		this.membraneRecycle = membraneRecycle;
	}

	public HashMap<String, Double> getSolubleRecycle() {
		return solubleRecycle;
	}

	public void setSolubleRecycle(HashMap<String, Double> solubleRecycle) {
		this.solubleRecycle = solubleRecycle;
	}

	// public double getTMembrane() {
	// return tembrane;
	// }
}