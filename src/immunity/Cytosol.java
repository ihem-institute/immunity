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
		public int blue;
		HashMap<String, Double> cytoContent = new HashMap<String, Double>();
		//constructor
		public	Cytosol (ContinuousSpace<Object> sp, Grid<Object> gr, HashMap<String, Double> 
		cytoContent, int xcoor, int ycoor) {
		this.space = sp;
		this.grid = gr;
		this.xcoor = xcoor;
		this.ycoor = ycoor;
		this.blue = 240;
		this.cytoContent = cytoContent;
		}



		// In the future, the cytosol will have a more active role.
		//solute diffusion etc.
		@ScheduledMethod(start = 1, interval = 1)
		public void step() {
			this.changeColor();

			}
		
		public void position(int xcoor, int ycoor){
			space.moveTo(this, xcoor, ycoor);
			grid.moveTo(this, xcoor, ycoor);	
			}
		public void changeColor() {
			double c1 = 0;
			if (Cell.getInstance().getSolubleCell().containsKey("LANCL2")){
			c1 = Cell.getInstance().getSolubleCell().get("LANCL2");
			}
			double c2 = CellProperties.getInstance().getMembraneRecycle().get("pLANCL2");
			this.blue = (int) (c1/c2*255);
			}
		//GETTERS AND SETTERS
		public double getXcoor() {
				return xcoor;
			}
		public int getBlue() {
			return blue;
		}
		public double getYcoor() {
			return ycoor;
		}
}


