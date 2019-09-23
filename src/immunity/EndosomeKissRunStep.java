package immunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import repast.simphony.context.Context;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class EndosomeKissRunStep {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	
	public static void kissRun (Endosome endosome) {
		if (endosome.a <= endosome.c
				|| endosome.volume < 4*Math.PI*Math.pow(Cell.rcyl, 3)) return; // if it is not a cistern
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
		int gridSize = (int) Math.round(endosome.size*Cell.orgScale / 15d);
		GridCellNgh<Endosome> nghCreator = new GridCellNgh<Endosome>(grid, pt,
				Endosome.class, gridSize, gridSize);
		// System.out.println("SIZE           "+gridSize);

		List<GridCell<Endosome>> cellList = nghCreator.getNeighborhood(true);
		List<Endosome> cisterns = new ArrayList<Endosome>();
		for (GridCell<Endosome> gr : cellList) {
			// include all endosomes
			for (Endosome end : gr.items()) {
				if (end != endosome // if it is not itself
						&& (end.volume > 4*Math.PI*Math.pow(Cell.rcyl, 3)) // if it is smaller
						&&(end.a >= end.c) // if it is another cistern
						&& Math.random()<(EndosomeAssessCompatibility.compatibles(endosome, end)))// if it is compatible
				{
					cisterns.add(end);
				}
				// System.out.println(cisterns);
			}
		}
		for (Endosome cist : cisterns) {
			// System.out.println(endosome.area+"  AREAS A SUMAR AREAS A SUMAR"+
			// endosome.area);
//			exchangeMemContent(endosome, cist); no membrane cargo exchange
			exchangeSolContent(endosome, cist);
			cist.getEndosomeTimeSeries().clear();
		}
//		double rsphere = Math.pow(endosome.volume * 3d / 4d / Math.PI, (1d / 3d));
//		double size = rsphere;
//		endosome.speed = 1d/ size;
//		Endosome.endosomeShape(endosome);
		endosome.getEndosomeTimeSeries().clear();
//		endosome.getRabTimeSeries().clear();
//		The time series will be re-calculated by COPASI call in the next tick
//		

	}
private static void exchangeSolContent(Endosome cist1, Endosome cist2) {
	HashMap<String, Double> totalSolubleContent = new HashMap<String, Double>(cist1.solubleContent);
	HashMap<String, Set<String>> rabTropism = new HashMap<String, Set<String>>(
			CellProperties.getInstance().getRabTropism());
	cist2.solubleContent.forEach((k, v) -> totalSolubleContent.merge(k, v, Double::sum));//(v1, v2) -> v1 + v2));

//	System.out.println("Trop Number " + cist1.solubleContent + cist2.solubleContent + totalSolubleContent);
	double propVolume = 0;
	double totalVolume = cist1.volume + cist2.volume;
	for (String content : totalSolubleContent.keySet())	{
//		System.out.println("se clava en  " + content);
		if (rabTropism.get(content).contains("sph")) {continue;}
		else {
			propVolume = cist1.volume/totalVolume;
			
		}
		splitPropVolume(cist1, content, totalSolubleContent.get(content), cist2, propVolume);
	}
	
	}
private static void splitPropVolume(Endosome cist1, String content, Double contentValue, Endosome cist2, double propVolume) {
	
	if (content.equals("solubleMarker")){
			if (contentValue < 0.99){
				cist1.solubleContent.remove("solubleMarker");
				cist2.solubleContent.remove("solubleMarker");
			}
			else if (Math.random() < propVolume){
			cist1.solubleContent.put("solubleMarker", 1d);
			cist2.solubleContent.remove("solubleMarker");
			}
			else {
			cist1.solubleContent.remove("solubleMarker");
			cist2.solubleContent.put("solubleMarker", 1d);
			}
		} 
	else {
		cist1.solubleContent.put(content,contentValue * propVolume);
		cist2.solubleContent.put(content,contentValue * (1-propVolume));
	}
	
}

private static void exchangeMemContent(Endosome cist1, Endosome cist2) {
//	Suma ambas cisternas para tener un totalMembraneContent
	HashMap<String, Double> totalMembraneContent = new HashMap<String, Double>(cist1.membraneContent);
	cist2.membraneContent.forEach((k, v) -> totalMembraneContent.merge(k, v, Double::sum));//(v1, v2) -> v1 + v2));
	HashMap<String, Set<String>> rabTropism = new HashMap<String, Set<String>>(
			CellProperties.getInstance().getRabTropism());
//	Set<String> memCont = new HashSet<String>();
//	memCont.addAll(cist1.membraneContent.keySet());
//	memCont.addAll(cist2.membraneContent.keySet());
	double propSurf = 0;
	double totalArea = cist1.area + cist2.area;
	for (String content : totalMembraneContent.keySet())	{
//		System.out.println(totalMembraneContent);
//		if (totalMembraneContent.get(content) != totalMembraneContent.get(content) ) continue;
		if (!rabTropism.containsKey(content)){;
			propSurf = cist1.area/totalArea;
			splitPropSurface(cist1, content, totalMembraneContent.get(content), cist2, propSurf);	
		}
		else if (rabTropism.get(content).contains("sph")) {continue;} // if the content is big, cannot difuse through tubules
		else 
		// finally, if tropism it to a Rab membrane domain, the decision about where to go
		// requires to calculate the tropism to the vesicle (SUM of the content tropism to all the
		//	membrane domains in the vesicle.  The tropism is indicated by a string (e.g. RabA10) where
		//	the last two digits indicate the affinity for the Rab domain in a scale of 00 to 10.			
		{
		double cist1Trop = 0;
		for (String rabTrop : rabTropism.get(content)){
			if (rabTrop.equals("mvb") || rabTrop.equals("tub")) continue;//ignore "mvb" and "tub" for this distribution
//			System.out.println("se clava en  " + content);		
			String rab = rabTrop.substring(0, 4);
			if (cist1.rabContent.containsKey(rab)){
				cist1Trop = cist1Trop + cist1.rabContent.get(rab)/cist1.area*
						Integer.parseInt(rabTrop.substring(4, 6));
				//						System.out.println("Trop Number " + Integer.parseInt(rabTrop.substring(4, 6)));
			}
		}
		// the tropism to the tubule is directly the two digits of the Rab selected for the tubule 
		double cist2Trop = 0;
		for (String rabTrop : rabTropism.get(content)){
			if (rabTrop.equals("mvb") || rabTrop.equals("tub")) continue;//ignore "mvb" and "tub" for this distribution
			String rab = rabTrop.substring(0, 4);
			if (cist2.rabContent.containsKey(rab)){
				cist2Trop = cist2Trop + cist2.rabContent.get(rab)/cist2.area*
						Integer.parseInt(rabTrop.substring(4, 6));
				//						System.out.println("Trop Number " + Integer.parseInt(rabTrop.substring(4, 6)));
			}
		}

		
		
//		double sCylinder = so -sVesicle;
//		if (sphereTrop + tubuleTrop == 0 ) propSurf = sVesicle/so;
		if (cist1Trop + cist2Trop == 0 ) propSurf = cist1.area/totalArea;
		else propSurf = cist1.area * cist1Trop/(cist1.area * cist1Trop + cist2.area * cist2Trop);
		splitPropSurface(cist1, content, totalMembraneContent.get(content), cist2, propSurf);
		}
		}
	}

private static void splitPropSurface(Endosome cist1, String content, Double contentValue, Endosome cist2, double propSurf) {

	if (content.equals("membraneMarker")){
		if (contentValue < 0.99){
			cist1.membraneContent.remove("membraneMarker");
			cist2.membraneContent.remove("membraneMarker");
		}
		else if (Math.random() < propSurf){
			cist1.membraneContent.put("membraneMarker", 1d);
			cist2.membraneContent.remove("membraneMarker");
		}
		else {
			cist1.membraneContent.remove("membraneMarker");
			cist2.membraneContent.put("membraneMarker", 1d);
		}
	}

	else {
		double cist1Content =contentValue * propSurf;
		double cist2Content =contentValue * (1-propSurf);
//		if the content to the cist1 is larger than cist1 area, 
//		or if the content to the cist2 is larger than cist2 area, then put in the cistern
//		the cistern area.
		if (cist1Content > cist1.area) {
			cist1Content = cist1.area;// all that can fit in cist1 area
			cist2Content = contentValue - cist1Content;
		}
		else if (cist2Content > cist2.area) {
			cist2Content = cist2.area;// all that can fit in cist2 area
			cist1Content = contentValue - cist2Content;
		}
		
		cist1.membraneContent.put(content,cist1Content);
		cist2.membraneContent.put(content,cist2Content);
		
		System.out.println(contentValue + "  content    " + propSurf + "    " + cist1Content +" cist1Content "+ cist2Content +" cist2Content ");
	}
		

}



}
