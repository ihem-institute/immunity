package immunity;

import java.util.HashMap;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;

public class EndosomeRecycleStep {
	private static ContinuousSpace<Object> space;
	
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
			if (y < 50-2*cellLimit)
				return;
			double recyProb = 0.0;
			for (String rab: endosome.rabContent.keySet()){
				recyProb = recyProb + endosome.rabContent.get(rab) / endosome.area 
				* CellProperties.getInstance().rabRecyProb.get(rab);
			}
			if (Math.random() >= recyProb){
				return;} // if not near the PM
						// or without a recycling Rab return
						// recycling Rabs are RabA (Rab5), RabB(Rab22) and RabC (Rab11)
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

				// PlasmaMembrane.getInstance().setMembraneRecycle(membraneRecycle);
				endosome.membraneContent.clear();
				System.out.println("membrane Recycled"
						+ PlasmaMembrane.getInstance().getMembraneRecycle());

				// Recycle soluble content
				HashMap<String, Double> solubleRecycle = PlasmaMembrane.getInstance()
						.getSolubleRecycle();
				for (String key1 : endosome.solubleContent.keySet()) {
					if (solubleRecycle.containsKey(key1)) {
						double sum = solubleRecycle.get(key1)
								+ solubleContent.get(key1);
						solubleRecycle.put(key1, sum);
					} else {
						solubleRecycle.put(key1, solubleContent.get(key1));
					}
				}
				// PlasmaMembrane.getInstance().setSolubleRecycle(solubleRecycle);
				endosome.solubleContent.clear();
				endosome.getAntigenTimeSeries().clear();
				endosome.getLANCL2TimeSeries().clear();
				System.out.println("soluble Recycled"
						+ PlasmaMembrane.getInstance().getSolubleRecycle());
			}
		}

	}

