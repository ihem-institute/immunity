package immunity;

import java.util.HashMap;

import repast.simphony.engine.schedule.ScheduledMethod;

 public class Cell {
	public static double rcyl = 10.0;
	public static double mincyl = 6 * Math.PI * rcyl * rcyl;
	// mincyl surface = 1884.95 volume = 6283.18
	private static Cell instance;
	public double tMembrane;
	public HashMap<String, Double> rabCell = new HashMap();
    static {
        instance = new Cell();
    }

    public Cell() { 
    double tMembrane = 0;    // hidden constructor
    rabCell = null;
    }    
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
	System.out.println("TOTAL CELL MEMBRANE" + tMembrane);
	}
    public static Cell getInstance() {
        return instance;
    }
	public HashMap<String, Double> getRabCell() {
		return rabCell;
	}
//	public double getTMembrane() {
	//	return tembrane;
	//}
}

