package immunity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.context.Context;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class FusionStep {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	
	public static void fusion (Endosome endosome) {
// Select an organelle and decide is is large enough to recruit other proximal organelles that
// will fuse and be deleted. 
// Notice then that two vesicles will not fuse.  They only fuse with larger organelles.
// Then it assess if the organelle is a Golgi structure.
// GOLGI
// Golgi cisternae do not fuse among them unless they have the same maximal Rab domain (homotypic fusion)
// Golgi cisternae fuse with Golgi vesicles if compatible. Golgi fuse with other small or large 
// non Golgi structures. 
// NON GOLGI
// Non Gogli organelles will fuse with other Golgi or nonGolgi structures depending only in 
// membrane compatibility.
		
		
		
// The organelle selected must be larger than a vesicles
// rendo is the radius of a new endosome from PM and also of a new ERGIC from ER 
		double rendo = CellProperties.getInstance().getCellK().get("rendo");//35.0; // radius vesicle/ 15393,804
		if ((endosome.area <= 4 * Math.PI*Math.pow(rendo, 2))){return;};
		space = endosome.getSpace();
		grid = endosome.getGrid();
// assesses if the organelle selected is a Golgi structure.  For this it sum all the Golgi domains			
// different fusion rules if it is a Golgi organelle
		if (isGolgi(endosome)) fusionGolgi(endosome);
		else fusionNoGolgi(endosome);
		
	}

	private static void fusionNoGolgi(Endosome endosome) {
		GridPoint pt = grid.getLocation(endosome);
		// I calculated that the 50 x 50 grid is equivalent to a 750 x 750 nm
		// square
		// Hence, size/15 is in grid units
		int gridSize = (int) Math.round(endosome.size*Cell.orgScale / 15d);
		GridCellNgh<Endosome> nghCreator = new GridCellNgh<Endosome>(grid, pt,
				Endosome.class, gridSize, gridSize);
		// System.out.println("SIZE           "+gridSize);

		List<GridCell<Endosome>> cellList = nghCreator.getNeighborhood(true);
		List<Endosome> endosomes_to_delete = new ArrayList<Endosome>();
		for (GridCell<Endosome> gr : cellList) {
			// include all endosomes
			for (Endosome end : gr.items()) {
				if (end != endosome && (end.volume <= endosome.volume)
						&& (EndosomeAssessCompatibility.compatibles(endosome, end))) {
					endosomes_to_delete.add(end);
				}
				// System.out.println(endosomes_to_delete);
			}
		}
		for (Endosome endosome2 : endosomes_to_delete) {
			// System.out.println(endosome.area+"  AREAS A SUMAR AREAS A SUMAR"+
			// endosome.area);
			endosome.volume = endosome.volume + endosome2.volume;
			endosome.area = endosome.area + endosome2.area;
			//initOrgProp.put("area", area);
			// System.out.println(endosome.area+"  AREAS FINAL");
			endosome.rabContent = sumRabContent(endosome, endosome2);
			endosome.membraneContent = sumMembraneContent(endosome, endosome2);
			endosome.solubleContent = sumSolubleContent(endosome, endosome2);
			Context<Object> context = ContextUtils.getContext(endosome);
			context.remove(endosome2);
		}
		double rsphere = Math.pow(endosome.volume * 3d / 4d / Math.PI, (1d / 3d));
		double size = rsphere;
		endosome.speed = 1d/ size;
		Endosome.endosomeShape(endosome);
		endosome.getEndosomeTimeSeries().clear();
		endosome.getRabTimeSeries().clear();
//		The time series will be re-calculated by COPASI call in the next tick
//				
	}

	private static void fusionGolgi(Endosome endosome) {
		GridPoint pt = grid.getLocation(endosome);
		// I calculated that the 50 x 50 grid is equivalent to a 750 x 750 nm
		// square
		// Hence, size/15 is in grid units
		int gridSize = (int) Math.round(endosome.size*Cell.orgScale / 15d);
		GridCellNgh<Endosome> nghCreator = new GridCellNgh<Endosome>(grid, pt,
				Endosome.class, gridSize, gridSize);
		// System.out.println("SIZE           "+gridSize);

		List<GridCell<Endosome>> cellList = nghCreator.getNeighborhood(true);
		List<Endosome> endosomes_to_delete = new ArrayList<Endosome>();

// it is assumed that the selected endosome is large and a cistern
		for (GridCell<Endosome> gr : cellList) {
			// include all endosomes
			for (Endosome end : gr.items()) {
				if (end.equals(endosome)) continue;// if it is itself 
				double rendo = CellProperties.getInstance().getCellK().get("rendo");//35.0; // radius vesicle/ 15393,804
				boolean isGolgi2 = isGolgi(end);				
//If the second organelle is Golgi and it is large (cistern) only fuse if it is homotypic fusion
				if (isGolgi2 // it is a Golgi structure
						&& (end.area > 4 * Math.PI*Math.pow(rendo, 2))// it is larger than a vesicle
						&& // and they DO NOT share the same maximal Rab domain
  	 				    !(Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey().equals
						(Collections.max(end.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey()))
						){continue;};
				if (!EndosomeAssessCompatibility.compatibles(endosome, end))// they are not compatible
						{continue;};
			endosomes_to_delete.add(end);	
//			 System.out.println(
//						Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey()
//
//					+ " FUSIONA CON GOLGI   " + end.area + maxRab + isGolgi);
			}
		}
		for (Endosome endosome2 : endosomes_to_delete) {
			// System.out.println(endosome.area+"  AREAS A SUMAR AREAS A SUMAR"+
			// endosome.area);
			endosome.volume = endosome.volume + endosome2.volume;
			endosome.area = endosome.area + endosome2.area;
			//initOrgProp.put("area", area);
			// System.out.println(endosome.area+"  AREAS FINAL");
			endosome.rabContent = sumRabContent(endosome, endosome2);
			endosome.membraneContent = sumMembraneContent(endosome, endosome2);
			endosome.solubleContent = sumSolubleContent(endosome, endosome2);
			Context<Object> context = ContextUtils.getContext(endosome);
			context.remove(endosome2);
		}
		double rsphere = Math.pow(endosome.volume * 3d / 4d / Math.PI, (1d / 3d));
		double size = rsphere;
		endosome.speed = 1d/ size;
		Endosome.endosomeShape(endosome);
		endosome.getEndosomeTimeSeries().clear();
		endosome.getRabTimeSeries().clear();
//		The time series will be re-calculated by COPASI call in the next tick
//		

	}


	private static HashMap<String, Double> sumRabContent(Endosome endosome1,
			Endosome endosome2) {

		HashMap<String, Double> rabSum = new HashMap<String, Double>();
		for (String key1 : endosome1.rabContent.keySet()) {
			if (endosome2.rabContent.containsKey(key1)) {
				double sum = endosome1.rabContent.get(key1)
						+ endosome2.rabContent.get(key1);
				rabSum.put(key1, sum);
			} else
				rabSum.put(key1, endosome1.rabContent.get(key1));
		}
		for (String key2 : endosome2.rabContent.keySet()) {
			if (!endosome1.rabContent.containsKey(key2)) {
				rabSum.put(key2, endosome2.rabContent.get(key2));
			}
		}

		// System.out.println("rabContentSum" + endosome1.rabContent);
		return rabSum;
	}

	private static HashMap<String, Double> sumMembraneContent(Endosome endosome1,
			Endosome endosome2) {
		HashMap<String, Double> memSum = new HashMap<String, Double>();
		for (String key1 : endosome1.membraneContent.keySet()) {
			if (endosome2.membraneContent.containsKey(key1)) {
				double sum = endosome1.membraneContent.get(key1)
						+ endosome2.membraneContent.get(key1);
				memSum.put(key1, sum);
			} else
				memSum.put(key1, endosome1.membraneContent.get(key1));
		}
		for (String key2 : endosome2.membraneContent.keySet()) {
			if (!endosome1.membraneContent.containsKey(key2)) {
				double sum = endosome2.membraneContent.get(key2);
				memSum.put(key2, sum);
			}
		}

		return memSum;
	}

	private static HashMap<String, Double> sumSolubleContent(Endosome endosome1,
			Endosome endosome2) {
		HashMap<String, Double> solSum = new HashMap<String, Double>();
		for (String key1 : endosome1.solubleContent.keySet()) {
			if (endosome2.solubleContent.containsKey(key1)) {
				double sum = endosome1.solubleContent.get(key1)
						+ endosome2.solubleContent.get(key1);
				solSum.put(key1, sum);
			} else
				solSum.put(key1, endosome1.solubleContent.get(key1));
		}
		for (String key2 : endosome2.solubleContent.keySet()) {
			if (!endosome1.solubleContent.containsKey(key2)) {
				double sum = endosome2.solubleContent.get(key2);
				solSum.put(key2, sum);
			}
		}

		// System.out.println("solubleContentSum" + endosome1.solubleContent);
		return solSum;
	}
	
	
	private static boolean isGolgi(Endosome endosome) {
		double areaGolgi = 0d;
		for (String rab : endosome.getRabContent().keySet()){
			String name = CellProperties.getInstance().rabOrganelle.get(rab);
			if (name.contains("Golgi")) {areaGolgi = areaGolgi + endosome.getRabContent().get(rab);} 
		}
		boolean isGolgi = false;
		if (areaGolgi/endosome.area >= 0.5) {
			isGolgi = true;
		}
		return isGolgi;	
	}

}
