package immunity;

import java.util.HashMap;
import java.util.TreeMap;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Cell {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;

	private static Cell instance;
	public static Cell getInstance() {
		if (instance == null) {
			instance = new Cell(space, grid);
		}
		return instance;
	}
	static CellProperties cellProperties = CellProperties.getInstance();
//	Cell characteristics
	public static double rcyl = cellProperties.getCellK().get("rcyl");//20.0; // radius tubule
	public static double minCistern = 4E5;
	public static double maxCistern = 1.6E6;
	public static double mincyl = 6 * Math.PI * rcyl * rcyl; // surface minimum cylinder
//  two radius large (almost a sphere)
	public static double rIV = rcyl; //Internal vesicle radius similar to tubule radius 
	public static double orgScale = cellProperties.getCellK().get("orgScale");
	public static double timeScale = cellProperties.getCellK().get("timeScale");
//  When orgScale=1 zoom =0, when > 1 zoom in , when <1 zoom out.
//	volume of the repast space  (1500 nm x 1500 nm) 400 nm deep (arbitrary heigth of the projection in 2D)
//	global cell and non-cell quantities
	public double tMembrane = 0;// membrane that is not used in endosomes
	public HashMap<String, Double> rabCell = new HashMap<String, Double>();// contains rabs free in cytosol
	public HashMap<String, Double> membraneCell = new HashMap<String, Double>(); // contains membrane factors within the cell 
	public HashMap<String, Double> solubleCell = new HashMap<String, Double>();// contains soluble factors within the cell
	TreeMap<Integer, HashMap<String, Double>> cellTimeSeries = new TreeMap<Integer, HashMap<String, Double>>();
	private double cellVolume;
	private double cellArea;
	private boolean scheduledUptake;


	// Constructor
	public Cell(ContinuousSpace<Object> space, Grid<Object> grid) {
	// Contains factors that are in the cell without specifying organelle or position.
	// It is modified by Endosome that uses and changes cytosolic Rabs
	// contents.	tMembranes, membrane and soluble content recycling,
		Cell.space = space;
		Cell.grid = grid;
		cellArea = cellProperties.getCellAgentProperties().get("cellArea");// 
		cellVolume = cellProperties.getCellAgentProperties().get("cellVolume");//

		solubleCell.putAll(cellProperties.getSolubleCell());
		rabCell.putAll(cellProperties.getInitRabCell());
		tMembrane = 10000000;//CellProperties.getInstance().cellK.get("tMembrane");

	}
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		String name = cellProperties.getCopasiFiles().get("cellCopasi");
		if (Math.random() < 0.1 && name.endsWith(".cps")){
			CellCopasiStep.antPresTimeSeriesLoad(this);
			// eventual use for cell metabolism
		}
		
	}

	@ScheduledMethod(start = 1, interval = 3000)
	public void uptake() {
		UptakeStep2.uptake(this);
	}
	
	// GETTERS AND SETTERS (to get and set Cell contents)


	public final boolean isScheduledUptake() {
		return scheduledUptake;
	}
	public final void setScheduledUptake(boolean scheduledUptake) {
		this.scheduledUptake = scheduledUptake;
	}
	public double gettMembrane() {
		return tMembrane;
	}
	public double getCellVolume() {
		return cellVolume;
	}
	public double getCellArea() {
		return cellArea;
	}
	public void settMembrane(double tMembrane) {
		this.tMembrane = tMembrane;
	}
	
	public HashMap<String, Double> getRabCell() {
		return rabCell;
	}
	
	public HashMap<String, Double> getMembraneCell() {
		return membraneCell;
	}
	public HashMap<String, Double> getSolubleCell() {
		return solubleCell;
	}
	public final TreeMap<Integer, HashMap<String, Double>> getCellTimeSeries() {
		return cellTimeSeries;
	}

	public ContinuousSpace<Object> getSpace() {
		return space;
	}
	public Grid<Object> getGrid() {
		return grid;
	}


}