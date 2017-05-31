package immunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class EndosomeTetherStep {

		private static ContinuousSpace<Object> space;
		private static Grid<Object> grid;
		
		public static void tether (Endosome endosome) {
			HashMap<String, Double> rabContent = new HashMap<String, Double>(endosome.getRabContent());
			HashMap<String, Double> membraneContent = new HashMap<String, Double>(endosome.getMembraneContent());
			HashMap<String, Double> solubleContent = new HashMap<String, Double>(endosome.getSolubleContent());
			space = endosome.getSpace();
			grid = endosome.getGrid();
			double cellLimit = 3 * Cell.orgScale;

		GridPoint pt = grid.getLocation(endosome);
		// I calculated that the 50 x 50 grid is equivalent to a 750 x 750 nm
		// square
		// Hence, size/15 is in grid units
		int gridSize = (int) Math.round(endosome.size*Cell.orgScale / 15);
		GridCellNgh<Endosome> nghCreator = new GridCellNgh<Endosome>(grid, pt,
				Endosome.class, gridSize, gridSize);
		// System.out.println("SIZE           "+gridSize);

		List<GridCell<Endosome>> cellList = nghCreator.getNeighborhood(true);
		if (cellList.size()<2)return;//if only one return
		List<Endosome> endosomesToTether = new ArrayList<Endosome>();
		for (GridCell<Endosome> gr : cellList) {
			// include all endosomes
			for (Endosome end : gr.items()) {
				if (EndosomeAssessCompatibility.compatibles(endosome, (Endosome) end)) {
					endosomesToTether.add(end);
				}
			}
		}
		
		// new list with just the compatible endosomes (same or compatible rabs)
		if (endosomesToTether.size()<2)return; //if only one, return
		// select the largest endosome
		Endosome largest = endosome;
		for (Endosome end : endosomesToTether) {
//			System.out.println(endosome.size+" "+end.size);
			if (end.size > largest.size) {
				largest = end;
			}
		}
		// assign the speed and heading of the largest endosome to the gropu

		for (Endosome end : endosomesToTether) {
			// end.heading = (end.heading * end.size + largest.heading *
			// largest.size)/
			// (end.size + largest.size);
			// end.speed = (end.speed * end.size + largest.speed *
			// largest.size)/
			// (end.size + largest.size);
			Random r = new Random();
			double rr = r.nextGaussian();
			end.heading = rr * 5d + largest.heading;
	//		EndosomeMove.moveTowards(end);
		}
	}
	
}
