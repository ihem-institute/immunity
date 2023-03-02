package immunity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class EndosomeInternalVesicleStep {

	

	public static void internalVesicle(Endosome endosome) {
		// if it is a sphere do not for  internal vesicles. Not enough membrane
//		if (1>0) return;
		double so = endosome.area;
		double vo = endosome.volume;
		boolean isSphere = (so * so * so / (vo * vo) <= 36.001 * Math.PI); 
		if (isSphere) return;
		// if it is a tubule do not for  internal vesicles
		boolean isTubule = (endosome.volume/(endosome.area - 2*Math.PI*Cell.rcyl*Cell.rcyl) <=Cell.rcyl/2);
		if (isTubule) return;
		double rIV = ModelProperties.getInstance().cellK.get("rcyl"); // Internal vesicle radius
		double vIV = 4 / 3 * Math.PI * Math.pow(rIV, 3); // volume 33510
		double sIV = 4 * Math.PI * Math.pow(rIV, 2);// surface 5026
		if (vo < 2 * vIV) // too small
			return;
		if (so < 2 * sIV)// too small
			return;
		//	Organelles with Rabs corresponding to EE, SE and LE can form internal vesicles
		String maxRab = Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
		String organelle = ModelProperties.getInstance().getRabOrganelle().get(maxRab);
		//		System.out.println("ORGANELLE  " + organelle);
		if (!organelle.equals("EE")
				&& !organelle.equals("SE")
				&& !organelle.equals("LE"))
		{
			return;
		}
		double vp = vo + vIV;
		double sp = so - sIV;
//		not enough membrane to contain the already present internal vesicles plus the new one
		if (endosome.solubleContent.containsKey("mvb")) {
			double mvbVolume = endosome.solubleContent.get("mvb")*vIV + vIV;
			if (sp * sp * sp / (mvbVolume * mvbVolume) <= 36 * Math.PI) return;

		} 

//	if after the formation of a new vesicles is a sphere (no extra membrane for the volume) stop
		if (sp * sp * sp / (vp * vp) <= 36 * Math.PI) return;

		//		System.out.println("INTERNAL VESICLE ORGANELLE" + organelle);
		/*
		 * if (endosome.getRabContent().containsKey("RabE") &&
		 * endosome.getRabContent().get("RabE")/endosome.area>0.5){ return; } //
		 * Organelles with pure RabC (Endosome Recycling Compartment) do not form
		 * internal vesicles if (endosome.getRabContent().containsKey("RabC") &&
		 * endosome.getRabContent().get("RabC")/endosome.area>0.95){ return; } // if
		 * (endosome.getRabContent().containsKey("RabB") // &&
		 * endosome.getRabContent().get("RabB")/endosome.area>0.95){ // return; // }
		 */
		//	lysSurface will allow large structures to form several internal vesicles
		// Other organelles form a single vesicle (notice that the for loop run at least once)

		int nroVesicles = 1;
//		if (organelle.equals("LE"))
//		//for LE, several vesicles can form in a single tick. For EE and SE only one vesicle
//		{
//			int lysSurface = (int) (endosome.getRabContent().get(maxRab)/sIV);
//			// about 1 times the area of and internal vesicle
//			for (int i = 1; i<=lysSurface ; i++)
//			{
//				if (Math.random()<0.5) continue; // in each loop there is a 50% chance of forming an internal vesicle 
//				vp = vp + vIV;
//				sp = sp - sIV;
//				//	if after the formation of the vesicles is a sphere (no extra membrane for the volume) stop
//				if (sp * sp * sp / (vp * vp) <= 36 * Math.PI) break;// if the resulting surface cannot embrance the resulting
//				else nroVesicles = nroVesicles + 1;
//				// volume
//			}
//		}
		HashMap<String, Set<String>> rabTropism = new HashMap<String, Set<String>>(
				ModelProperties.getInstance().getRabTropism());
		endosome.area = endosome.area - nroVesicles * sIV;
		endosome.volume = endosome.volume + nroVesicles * vIV;
//		System.out.println("Nro Vesicles " + nroVesicles +"  "+ endosome.area +"  "+ endosome.volume);
		Endosome.endosomeShape(endosome);
		if (endosome.solubleContent.containsKey("mvb")) {
			double content = endosome.solubleContent.get("mvb") + nroVesicles;
			endosome.solubleContent.put("mvb", content);
		} else {
			endosome.solubleContent.put("mvb", (double) nroVesicles);
		}
		// Rabs proportinal to the sIV versus the surface of the organelle (so)
		// are released into the cytosol.  Must be divided by the area to be in mM
		//	NEW RAB NET.  Since cyto  Rabs are generated by the -> Rabcyto, there is no point
		//	to add what is not in the endosomal membrane
		//	HashMap<String, Double> rabCell = Cell.getInstance().getRabCell();
		//	for (String key1 : endosome.rabContent.keySet()) {
		//		if (rabCell.containsKey(key1)) {
		//			double sum = endosome.rabContent.get(key1) * sIV / so /so
		//					+ rabCell.get(key1);
		//			rabCell.put(key1, sum);
		//		} else {
		//			double sum = endosome.rabContent.get(key1) * sIV / so /so;
		//			rabCell.put(key1, sum);
		//		}
		//	}

		// Cell.getInstance().setRabCell(rabCell);
		//	System.out.println("Rab Cellular Content"
		//			+ Cell.getInstance().getRabCell());
		// Rabs released to the cytosol are substracted from the rabContent of
		// the organelle
		for (String rab : endosome.rabContent.keySet()) {
			double content1 = endosome.rabContent.get(rab) * (so - nroVesicles * sIV) / so;
			endosome.rabContent.put(rab, content1);
		}

		// Membrane content with mvb tropism is degraded (e.g. EGF)
		//this can be established in RabTropism adding in the EGF tropisms "mvb",
		for (String content : endosome.membraneContent.keySet()) {
			System.out.println(endosome.membraneContent+"\n"+ content + "\n" + " CHOLESTEROL RAB TROPISM " + rabTropism.get(content)+ "  \n"+rabTropism);

			if(content.equals("membraneMarker")) {
				if (endosome.membraneContent.get("membraneMarker")>0.9){
					endosome.membraneContent.put("membraneMarker", 1d);
				}
				else{
					endosome.membraneContent.put("membraneMarker", 0d);
				}

			}
			else if (rabTropism.get(content).contains("mvb")){				

				double mem = endosome.membraneContent.get(content) - nroVesicles * sIV;

				if (mem <= 0) mem = 0d;
				endosome.membraneContent.put(content, mem);
				//			If not special tropism, the membrane content is incorporated 
				//			into the internal vesicle proportional to the surface and degraded
			} 
			else 
			{

				double mem = endosome.membraneContent.get(content) * (so - nroVesicles * sIV)
						/ so;
				endosome.membraneContent.put(content, mem);
			}
		}


		// Free membrane is added to PM
		//	double plasmaMembrane = PlasmaMembrane.getInstance().getPlasmaMembraneArea() + sIV;
		//	PlasmaMembrane.getInstance().setPlasmaMembraneArea(plasmaMembrane);
	}
}


