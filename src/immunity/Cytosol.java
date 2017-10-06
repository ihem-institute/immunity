package immunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
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
		this.blue = 0;
		this.cytoContent = cytoContent;
		}



		// In the future, the cytosol will have a more active role.
		//solute diffusion etc.
		@ScheduledMethod(start = 1, interval = 1)
		public void step() {
			this.changeColor();
			if (Math.random()<0.01) this.diffusion();

			}
		
		private void diffusion() {
			// TODO Auto-generated method stub
			GridPoint pt = grid.getLocation(this);

			GridCellNgh<Cytosol> nghCreator = new GridCellNgh<Cytosol>(grid, pt,
					Cytosol.class, 1, 1);
			// System.out.println("SIZE           "+gridSize);

			List<GridCell<Cytosol>> cellList = nghCreator.getNeighborhood(true);
			HashMap<String, Double> sumCytoContent = new HashMap<String, Double>();
			int count = 0;
			for (GridCell<Cytosol> gr : cellList) {
				// include all endosomes

				for (Cytosol cy : gr.items()) {
					count = count +1;
					for (String content : cy.cytoContent.keySet()){
						double value = 0;
						if (sumCytoContent.containsKey(content)){
							value = sumCytoContent.get(content)+ cy.cytoContent.get(content);
						}
						else {value = cy.cytoContent.get(content);}
						sumCytoContent.put(content, value);
					}
			
				}
			}
			for (String content : sumCytoContent.keySet()){
					double average = sumCytoContent.get(content)/count;
					sumCytoContent.put(content, average);
				}
	
		
		for (GridCell<Cytosol> gr : cellList) {
			for (Cytosol cy : gr.items()) {
				cy.cytoContent.putAll(sumCytoContent);
				}
			}
			
		}



//		public void position(int xcoor, int ycoor){
//			space.moveTo(this, xcoor, ycoor);
//			grid.moveTo(this, xcoor, ycoor);	
//			}
		public void changeColor() {
			double c1 = 0;
			if (this.cytoContent.containsKey("LANCL2cyto")){
			c1 = this.cytoContent.get("LANCL2cyto");
			}
			if (Cell.getInstance().getSolubleCell().containsKey("LANCL2cyto")){
			c1 = c1 + Cell.getInstance().getSolubleCell().get("LANCL2cyto");
			}
			c1=c1*240;
			if (c1 > 240){c1 = 240;}
			this.blue = (int) c1;
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



		public HashMap<String, Double> getCytoContent() {
			return cytoContent;
		}
}


