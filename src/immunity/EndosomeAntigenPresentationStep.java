package immunity;

import java.util.HashMap;
import java.util.Set;

public class EndosomeAntigenPresentationStep {
	
	public static void antigenPresentation(Endosome endosome) {

		AntigenPresentation antigenPresentation = AntigenPresentation
				.getInstance();

		Set<String> metabolites = antigenPresentation.getInstance()
				.getMetabolites();
		HashMap<String, Double> localM = new HashMap<String, Double>();
		for (String met : metabolites) {
			if (endosome.membraneContent.containsKey(met)) {
				double metValue = Math
						.abs(Math.round(endosome.membraneContent.get(met)));
				antigenPresentation.setInitialConcentration(met, metValue);
				localM.put(met, metValue);
			} else if (endosome.solubleContent.containsKey(met)) {
				double metValue = Math.abs(Math.round(endosome.solubleContent.get(met)));
				antigenPresentation.setInitialConcentration(met, metValue);
				localM.put(met, metValue);
			} else {
				antigenPresentation.setInitialConcentration(met, 0.0);
				localM.put(met, 0.0);
			}
		}
		System.out.println("AntigenPresentation INICIAL ");
		for (String met :metabolites){
		System.out.println(met+ " "+localM.get(met));
		}
		/*
		 * double sm1 = localM.get("ova"); double sc1 = localM.get("p1"); double
		 * sm2 = localM.get("preP"); double sc2 = localM.get("p2"); if ((sm1 ==
		 * 0 || sc1 == 0) && (sm2 == 0 || sc2 == 0)) return;
		 */

		antigenPresentation.runTimeCourse();
		
		for (String met : metabolites) {
			/*
			 * if (membraneContent.containsKey(met)) { double metValue =
			 * (double) Math.abs(Math
			 * .round(antigenPresentation.getConcentration(met)));
			 * localM.put(met, metValue); membraneContent.put(met, metValue); }
			 * else if (solubleContent.containsKey(met)) { double metValue =
			 * (double) Math.abs(Math
			 * .round(antigenPresentation.getConcentration(met)));
			 * localM.put(met, metValue); solubleContent.put(met, metValue); }
			 * else {
			 */
			if (endosome.membraneMet.contains(met)) {
				double metValue = (double) Math.abs(Math
						.round(antigenPresentation.getConcentration(met)));
				endosome.membraneContent.put(met, metValue);
			} else if (endosome.solubleMet.contains(met)) {
				double metValue = (double) Math.abs(Math
						.round(antigenPresentation.getConcentration(met)));
				endosome.solubleContent.put(met, metValue);
			} else
				System.out.println("Met not found in " + endosome.membraneMet + " "
						+ endosome.solubleMet + " " + met);
		}
		System.out.println("AntigenPresentation FINAL ");
		for (String met :metabolites){
		System.out.println(met+ " "+localM.get(met)+" "+
		((double) Math.abs(Math.round(antigenPresentation.getConcentration(met)))));
		}

	}
}

