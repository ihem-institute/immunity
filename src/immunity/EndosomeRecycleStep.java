package immunity;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class EndosomeRecycleStep {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	public static void recycle(Endosome endosome) {
		HashMap<String, Double> rabContent = new HashMap<String, Double>(endosome.getRabContent());
		HashMap<String, Double> membraneContent = new HashMap<String, Double>(endosome.getMembraneContent());
		HashMap<String, Double> solubleContent = new HashMap<String, Double>(endosome.getSolubleContent());
		double cellLimit = 3 * Cell.orgScale;
//		System.out.println("TEST  ADENTRO RECYCLE TEST  "+endosome.area +rabContent );
		NdPoint myPoint = endosome.getSpace().getLocation(endosome);
//		NdPoint myPoint = space.getLocation(endosome);
//		System.out.println("TEST  ADENTRO coor  "+myPoint.toString()+ (50 -cellLimit));
			double y = myPoint.getY();
//			if far from the PM no recycling
			if (y < 50-2*cellLimit)
				return;
//			if near the PM and having RabA, and having Tf, recycle only Tf
//			So, I am assuming that there is a Rab4 tubule getting Tf that goes to the PM
//			leaving the rest of the early endosome (RabA) in place
			
			if (endosome.rabContent.containsKey("RabA")
					&& Math.random() <= endosome.rabContent.get("RabA")/endosome.area 
					&& endosome.membraneContent.containsKey("Tf")){
								
				
				double tfValue = endosome.membraneContent.get("Tf");
				HashMap<String, Double> membraneRecycle = PlasmaMembrane.getInstance()
						.getMembraneRecycle();
				if (membraneRecycle.containsKey("Tf")) {
					double sum = membraneRecycle.get("Tf")
							+ tfValue;
					membraneRecycle.put("Tf", sum);
				} else {
					membraneRecycle.put("Tf", tfValue);}
				
				endosome.membraneContent.put("Tf", 0d);				
				return;
			}
			double recyProb = 0.0;
			for (String rab: endosome.rabContent.keySet()){
				recyProb = recyProb + endosome.rabContent.get(rab) / endosome.area 
				* CellProperties.getInstance().rabRecyProb.get(rab);
			}
			if (Math.random() >= recyProb){
				return;} // if not near the PM
						// or without a recycling Rab return
						// recycling Rabs are RabA and RabC (Rab11)
			else {
				// RECYCLE
				// Recycle membrane content
				HashMap<String, Double> membraneRecycle = PlasmaMembrane.getInstance()
						.getMembraneRecycle();
				for (String key1 : endosome.membraneContent.keySet()) {
					if (membraneRecycle.containsKey(key1)) {
						double sum = membraneRecycle.get(key1)
								+ membraneContent.get(key1);
						membraneRecycle.put(key1, sum);
					} else {
						membraneRecycle.put(key1, membraneContent.get(key1));
					}
				}

				endosome.membraneContent.clear();

				HashMap<String, Double> solubleRecycle = PlasmaMembrane.getInstance()
						.getSolubleRecycle();
				double endopH = endosome.solubleContent.get("proton");
				for (String key1 : endosome.solubleContent.keySet()) {
					if (solubleRecycle.containsKey(key1)) {
						double sum = solubleRecycle.get(key1)
								+ solubleContent.get(key1);
						solubleRecycle.put(key1, sum);
					} else {
						solubleRecycle.put(key1, solubleContent.get(key1));
					}
				}

				endosome.solubleContent.clear();
				endosome.solubleContent.put("proton", endopH);
				endosome.getEndosomeTimeSeries().clear();
				PlasmaMembrane.getInstance().getPlasmaMembraneTimeSeries().clear();
				double rcyl = CellProperties.getInstance().getCellK().get("rcyl");// radius tubule
				double h = (endosome.area-2*Math.PI*rcyl*rcyl)/(2*Math.PI*rcyl);// length of a tubule with the area of the recycled endosome
				endosome.volume = Math.PI*rcyl*rcyl*h; // new volume of the endosome, now converted in a tubule.
				endosome.heading = -90; //moving in the nucleus direction
//				to delete the recycled endosome.
//				Context<Object> context = ContextUtils.getContext(endosome);
//				context.remove(endosome);

			}
		}

	}

