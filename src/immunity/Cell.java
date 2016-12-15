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
 	public HashMap<String, Double> membraneRecycle = new HashMap();
 	public HashMap<String, Double> solubleRecycle = new HashMap();
     static {
         instance = new Cell();
     }
 
     public Cell() { 
     double tMembrane = 0;    // hidden constructor
     rabCell.put("RabA", 0.0d);
     rabCell.put("RabB", 0.0d);
     membraneRecycle.put(null, null);
     solubleRecycle.put(null, null);     
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
 	
 //	public double getTMembrane() {
 	//	return tembrane;
 	//}
  }