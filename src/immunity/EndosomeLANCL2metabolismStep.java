package immunity;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import repast.simphony.query.space.grid.GridCell;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class EndosomeLANCL2metabolismStep {

	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	
	public static void LANCL2metabolism(Endosome endosome) {
		space = endosome.getSpace();
		grid = endosome.getGrid();
		LANCL2metabolism lANCL2metabolism = LANCL2metabolism.getInstance();
		double metValue = 0.0;
		if (endosome.membraneContent.containsKey("pLANCL2")) {
			metValue = Math.round
						(endosome.membraneContent.get("pLANCL2") * 1000) / 1000;
		}
		lANCL2metabolism.setInitialConcentration("pLANCL2", metValue);
		System.out.println("COPASI INITIAL endo pLANCL2  " +metValue);
		Cytosol cyto = null;
		metValue = 0.0;
		GridPoint pt = grid.getLocation(endosome);
		System.out.println(pt.toString());
		Iterable<Object> obj = grid.getObjectsAt(pt.getX(), pt.getY());
		for (Object xx : obj){
		 if (xx instanceof Cytosol){
			cyto = (Cytosol) xx;
			System.out.println(cyto.getCytoContent());
		 }
		}
		if (cyto.getCytoContent().containsKey("LANCL2")) {
			metValue = cyto.getCytoContent().get("LANCL2");
//				metValue = Math.round
//								(cyto.getCytoContent().get("LANCL2") * 1000) / 1000;
			}
		lANCL2metabolism.setInitialConcentration("LANCL2", metValue);
		System.out.println("COPASI INITIAL  CYTO LANCL2 " + metValue);
		metValue = 0.0;
		if (Cell.getInstance().getSolubleCell().containsKey("ABA")) {
			metValue = Math.round
					(Cell.getInstance().getSolubleCell().get("ABA") * 1000) / 1000;
		}
			lANCL2metabolism.setInitialConcentration("ABA", metValue);
			System.out.println("COPASI INITIAL  Cell ABA " + metValue);


		lANCL2metabolism.runTimeCourse();

		double rest = 0.0; // what is in the endosome cannot be larger than its area
		metValue = lANCL2metabolism.getConcentration("pLANCL2");
		if (metValue > endosome.area){
			rest = metValue - endosome.area;
			metValue=endosome.area;
			}
		endosome.membraneContent.put("pLANCL2", metValue);
		System.out.println("COPASI ENDO FINAL pLANCL2 " + metValue);
		
		metValue = lANCL2metabolism.getConcentration("LANCL2");
		cyto.getCytoContent().put("LANCL2", metValue+rest);
		System.out.println("COPASI Cyto FINAL LANCL2 " + metValue+rest);
		
		metValue = lANCL2metabolism.getConcentration("ABA");
		Cell.getInstance().getSolubleCell().put("ABA", metValue);
		System.out.println("COPASI Cell FINAL ABA " + metValue);
		}

}
	
