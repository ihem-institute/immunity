package immunity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class RecycleStep {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	
	public static void recycle(Endosome endosome) {
		HashMap<String, Double> rabContent = new HashMap<String, Double>(endosome.getRabContent());
		HashMap<String, Double> membraneContent = new HashMap<String, Double>(endosome.getMembraneContent());
		HashMap<String, Double> solubleContent = new HashMap<String, Double>(endosome.getSolubleContent());
		double cellLimit = 3 * Cell.orgScale;
		NdPoint myPoint = endosome.getSpace().getLocation(endosome);
			double y = myPoint.getY();
//			if far from the PM no recycling
			if (y < 50-2*cellLimit && y > 2*cellLimit) return;
//NEW RULES
//			if near the PM and larger domain is EE and is a tubule, recycle
//			So, I am assuming a fast recycling cycle probably with Rab4 tubules 
/* I will test the posibility of recycling of the membrane and having a balance of EE
 * and PM membrane.
 */		
		if (y >= 50-2*cellLimit)
		{
		String maxRab = Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
		String organelle = ModelProperties.getInstance().getRabOrganelle().get(maxRab);    
			if (organelle.equals("EE")) recycleEE(endosome, maxRab);
			if (organelle.equals("RE")) recycleRE(endosome, maxRab);
			if (organelle.equals("TGN")) recycleRE(endosome, maxRab); //same rules than for RE
			else return;
		}
		
//		if near the Nucleus and larger domain is ERGIC and it is a tubule, fuse back with ER 

		if (y <= 2*cellLimit)
		{
		String maxRab = Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
		String organelle = ModelProperties.getInstance().getRabOrganelle().get(maxRab);    
			if (organelle.equals("ERGIC")) recycleERGIC(endosome, maxRab);
			else return;
		}
		
		
		/*
		 * if (endosome.rabContent.containsKey("RabA") && Math.random() <=
		 * endosome.rabContent.get("RabA")/endosome.area &&
		 * endosome.membraneContent.containsKey("Tf")){
		 * 
		 * 
		 * double tfValue = endosome.membraneContent.get("Tf"); HashMap<String, Double>
		 * membraneRecycle = PlasmaMembrane.getInstance() .getMembraneRecycle(); if
		 * (membraneRecycle.containsKey("Tf")) { double sum = membraneRecycle.get("Tf")
		 * + tfValue; membraneRecycle.put("Tf", sum); } else { membraneRecycle.put("Tf",
		 * tfValue);}
		 * 
		 * endosome.membraneContent.put("Tf", 0d); return; } double recyProb = 0.0; for
		 * (String rab: endosome.rabContent.keySet()){ recyProb = recyProb +
		 * endosome.rabContent.get(rab) / endosome.area
		 * ModelProperties.getInstance().rabRecyProb.get(rab); } if (Math.random() >=
		 * recyProb){ return;} // if not near the PM // or without a recycling Rab
		 * return // recycling Rabs are RabA and RabC (Rab11) else { // RECYCLE //
		 * Recycle membrane content HashMap<String, Double> membraneRecycle =
		 * PlasmaMembrane.getInstance() .getMembraneRecycle(); for (String key1 :
		 * endosome.membraneContent.keySet()) { if (membraneRecycle.containsKey(key1)) {
		 * double sum = membraneRecycle.get(key1) + membraneContent.get(key1);
		 * membraneRecycle.put(key1, sum); } else { membraneRecycle.put(key1,
		 * membraneContent.get(key1)); } }
		 * 
		 * endosome.membraneContent.clear();
		 * 
		 * HashMap<String, Double> solubleRecycle = PlasmaMembrane.getInstance()
		 * .getSolubleRecycle(); double endopH = endosome.solubleContent.get("proton");
		 * for (String key1 : endosome.solubleContent.keySet()) { if
		 * (solubleRecycle.containsKey(key1)) { double sum = solubleRecycle.get(key1) +
		 * solubleContent.get(key1); solubleRecycle.put(key1, sum); } else {
		 * solubleRecycle.put(key1, solubleContent.get(key1)); } }
		 * 
		 * endosome.solubleContent.clear(); endosome.solubleContent.put("proton",
		 * endopH); endosome.getEndosomeTimeSeries().clear();
		 * PlasmaMembrane.getInstance().getPlasmaMembraneTimeSeries().clear(); double
		 * rcyl = ModelProperties.getInstance().getCellK().get("rcyl");// radius tubule
		 * double h = (endosome.area-2*Math.PI*rcyl*rcyl)/(2*Math.PI*rcyl);// length of
		 * a tubule with the area of the recycled endosome endosome.volume =
		 * Math.PI*rcyl*rcyl*h; // new volume of the endosome, now converted in a
		 * tubule. endosome.heading = -90; //moving in the nucleus direction // to
		 * delete the recycled endosome. // Context<Object> context =
		 * ContextUtils.getContext(endosome); // context.remove(endosome);
		 * 
		 * }
		 */		
		}

	private static void recycleERGIC(Endosome endosome, String maxRab) {
//		if near the PM and larger domain is ERGIC and is a tubule, fuse back with ER
		
		boolean isTubule = (endosome.volume/(endosome.area - 2*Math.PI*Cell.rcyl*Cell.rcyl) <=Cell.rcyl/2); // should be /2
		if (!isTubule) return;// if it is not a tubule no recycling
		double recyProb = endosome.rabContent.get(maxRab) / endosome.area; 
		if (Math.random() >= recyProb){
			return;}
		else {
// ER back transport
// Back membrane content
			HashMap<String, Double> membraneRecycle = EndoplasmicReticulum.getInstance()
					.getMembraneRecycle();
			for (String key1 : endosome.membraneContent.keySet()) {
				if (membraneRecycle.containsKey(key1)) {
					double sum = membraneRecycle.get(key1)
							+ endosome.membraneContent.get(key1);
					membraneRecycle.put(key1, sum);
				} else {
					membraneRecycle.put(key1, endosome.membraneContent.get(key1));
				}
			}
			endosome.membraneContent.clear();

			HashMap<String, Double> solubleRecycle = EndoplasmicReticulum.getInstance()
					.getSolubleRecycle();
//			double endopH = endosome.solubleContent.get("proton");
			for (String key1 : endosome.solubleContent.keySet()) {
				if (solubleRecycle.containsKey(key1)) {
					double sum = solubleRecycle.get(key1)
							+ endosome.solubleContent.get(key1);
					solubleRecycle.put(key1, sum);
				} else {
					solubleRecycle.put(key1, endosome.solubleContent.get(key1));
				}
			}

			EndoplasmicReticulum.getInstance().getendoplasmicReticulumTimeSeries().clear();
			double endoplasmicReticulum = endosome.area + EndoplasmicReticulum.getInstance().getendoplasmicReticulumArea();
			EndoplasmicReticulum.getInstance().setEndoplasmicReticulumArea(endoplasmicReticulum);
			System.out.println("RECYCLING OF ER  " + endoplasmicReticulum);
			/*
			 * double rcyl = ModelProperties.getInstance().getCellK().get("rcyl");// radius
			 * tubule double h = (endosome.area-2*Math.PI*rcyl*rcyl)/(2*Math.PI*rcyl);//
			 * length of a tubule with the area of the recycled endosome endosome.volume =
			 * Math.PI*rcyl*rcyl*h; // new volume of the endosome, now converted in a
			 * tubule. endosome.heading = -90; //moving in the nucleus direction
			 */
//			to delete the recycled endosome.
			Context<Object> context = ContextUtils.getContext(endosome);
			context.remove(endosome);

		}
		
	}

	private static void recycleRE(Endosome endosome, String maxRab) {
		//NEW RULES
//		if near the PM and it is a Recycling Endosome, kiss and run exocytosis (recycle all but the endosome is preserved
		double recyProb = endosome.rabContent.get(maxRab) / endosome.area; 
		if (Math.random() >= recyProb){
			return;}
		else {
			// Recycle membrane content
			HashMap<String, Double> membraneRecycle = PlasmaMembrane.getInstance()
					.getMembraneRecycle();
			for (String key1 : endosome.membraneContent.keySet()) {
				if (membraneRecycle.containsKey(key1)) {
					double sum = membraneRecycle.get(key1)
							+ endosome.membraneContent.get(key1);
					membraneRecycle.put(key1, sum);
				} else {
					membraneRecycle.put(key1, endosome.membraneContent.get(key1));
				}
			}

			endosome.membraneContent.clear();

			HashMap<String, Double> solubleRecycle = PlasmaMembrane.getInstance()
					.getSolubleRecycle();
			double endopH = endosome.solubleContent.get("proton");
			for (String key1 : endosome.solubleContent.keySet()) {
				if (solubleRecycle.containsKey(key1)) {
					double sum = solubleRecycle.get(key1)
							+ endosome.solubleContent.get(key1);
					solubleRecycle.put(key1, sum);
				} else {
					solubleRecycle.put(key1, endosome.solubleContent.get(key1));
				}
			}

			endosome.solubleContent.clear();
			endosome.solubleContent.put("proton", endopH);
			endosome.getEndosomeTimeSeries().clear();
			PlasmaMembrane.getInstance().getPlasmaMembraneTimeSeries().clear();
			double rcyl = ModelProperties.getInstance().getCellK().get("rcyl");// radius tubule
			double h = (endosome.area-2*Math.PI*rcyl*rcyl)/(2*Math.PI*rcyl);// length of a tubule with the area of the recycled endosome
			endosome.volume = Math.PI*rcyl*rcyl*h; // new volume of the endosome, now converted in a tubule.
			endosome.heading = -90; //moving in the nucleus direction
//			to delete the recycled endosome.
//			Context<Object> context = ContextUtils.getContext(endosome);
//			context.remove(endosome);
		}

		
	}

	private static void recycleEE(Endosome endosome, String maxRab) {
//NEW RULES
//		if near the PM and larger domain is EE and is a tubule, recycle
//		So, I am assuming a fast recycling cycle probably with Rab4 tubules 
/* I will test the posibility of recycling of the membrane and having a balance of EE
* and PM membrane.
* firt tests if it is a tubule (return) then higher probabilities to tubules with high proportion of EE domain
*/
		boolean isTubule = (endosome.volume/(endosome.area - 2*Math.PI*Cell.rcyl*Cell.rcyl) <=Cell.rcyl/2); // should be /2
		if (!isTubule) return;// if it is not a tubule no recycling
		double recyProb = endosome.rabContent.get(maxRab) / endosome.area; 
		if (Math.random() >= recyProb){
			return;}
		else {
// EE RECYCLING
// Recycle membrane content
			HashMap<String, Double> membraneRecycle = PlasmaMembrane.getInstance()
					.getMembraneRecycle();
			for (String key1 : endosome.membraneContent.keySet()) {
				if (membraneRecycle.containsKey(key1)) {
					double sum = membraneRecycle.get(key1)
							+ endosome.membraneContent.get(key1);
					membraneRecycle.put(key1, sum);
				} else {
					membraneRecycle.put(key1, endosome.membraneContent.get(key1));
				}
			}
			endosome.membraneContent.clear();

			HashMap<String, Double> solubleRecycle = PlasmaMembrane.getInstance()
					.getSolubleRecycle();
//			double endopH = endosome.solubleContent.get("proton");
			for (String key1 : endosome.solubleContent.keySet()) {
				if (solubleRecycle.containsKey(key1)) {
					double sum = solubleRecycle.get(key1)
							+ endosome.solubleContent.get(key1);
					solubleRecycle.put(key1, sum);
				} else {
					solubleRecycle.put(key1, endosome.solubleContent.get(key1));
				}
			}

//			endosome.solubleContent.clear();
//			endosome.solubleContent.put("proton", endopH);
//			endosome.getEndosomeTimeSeries().clear();
			PlasmaMembrane.getInstance().getPlasmaMembraneTimeSeries().clear();
			double plasmaMembrane = endosome.area + PlasmaMembrane.getInstance().getPlasmaMembraneArea();
			PlasmaMembrane.getInstance().setPlasmaMembraneArea(plasmaMembrane);
			System.out.println("RECYCLING OF EE  " + plasmaMembrane);
			/*
			 * double rcyl = ModelProperties.getInstance().getCellK().get("rcyl");// radius
			 * tubule double h = (endosome.area-2*Math.PI*rcyl*rcyl)/(2*Math.PI*rcyl);//
			 * length of a tubule with the area of the recycled endosome endosome.volume =
			 * Math.PI*rcyl*rcyl*h; // new volume of the endosome, now converted in a
			 * tubule. endosome.heading = -90; //moving in the nucleus direction
			 */
//			to delete the recycled endosome.
			Context<Object> context = ContextUtils.getContext(endosome);
			context.remove(endosome);

		}

		
	}

	}

