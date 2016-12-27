package immunity;

import java.util.HashMap;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
public class Cytosol {

		private ContinuousSpace<Object> space;
		private Grid<Object> grid;
		//double area = 4d * Math.PI * 30d * 30d; // initial value, but should change
		public int xcoor;
		public int ycoor;
		HashMap<String, Double> cytoContent = new HashMap<String, Double>();
		//constructor
		public	Cytosol (ContinuousSpace<Object> sp, Grid<Object> gr, HashMap<String, Double> 
		cytoContent, int xcoor, int ycoor) {
		this.space = sp;
		this.grid = gr;
		this.xcoor = xcoor;
		this.ycoor = ycoor;
		this.cytoContent = cytoContent;
		}

		// In the future, the cytosol will have a more active role.
		//solute diffusion etc.
		@ScheduledMethod(start = 1, interval = 0)
		public void step() {
			//position(xcoor, ycoor);
			}
		
	
		public void position(int xcoor, int ycoor){
			space.moveTo(this, xcoor, ycoor);
			grid.moveTo(this, xcoor, ycoor);	
			}
		//GETTERS AND SETTERS
		public double getXcoor() {
				return xcoor;
			}
		public double getYcoor() {
			return ycoor;
		}
}


