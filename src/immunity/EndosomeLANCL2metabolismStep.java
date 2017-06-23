package immunity;

import java.util.HashMap;
import java.util.Set;

public class EndosomeLANCL2metabolismStep {
	
	public static void LANCL2metabolism(Endosome endosome) {

		LANCL2metabolism lANCL2metabolism = LANCL2metabolism.getInstance();

		Set<String> metabolites = LANCL2metabolism.getInstance().getMetabolites();

		for (String met : metabolites) {
				if (endosome.membraneContent.containsKey(met)) {
					double metValue = Math.round
							(endosome.membraneContent.get(met) * 1000) / 1000;
					lANCL2metabolism.setInitialConcentration(met, metValue);

				} else {
					lANCL2metabolism.setInitialConcentration(met, 0.0);
					// System.out.println("COPASI INITIAL " + met + 0.0);
				}
			}
		
	for (String met : metabolites) {
		if (Cell.getInstance().getMembraneCell().containsKey(met)) {
			double metValue = Math.round
					(Cell.getInstance().getMembraneCell().get(met) * 1000) / 1000;
			lANCL2metabolism.setInitialConcentration(met, metValue);
			System.out.println("COPASI INITIAL  LANCL2 " + met + " " + metValue);
		}
	}

		lANCL2metabolism.runTimeCourse();
		for (String met : metabolites) {
			if (CellProperties.getInstance().membraneMet.contains(met)){
				double metValue = lANCL2metabolism.getConcentration(met);
				endosome.membraneContent.put(met, metValue);
				System.out.println("COPASI LANCL2  ENDO FINAL " + met + " " + metValue);
				// rabContent.get(Rab));
			}
			else{
				double metValue = lANCL2metabolism.getConcentration(met);
				double value = metValue;
				if (Cell.getInstance().getSolubleCell().containsKey(met)){
					value = value + Cell.getInstance().getSolubleCell().get(met);
				}
				Cell.getInstance().getMembraneCell().put(met, value);
				System.out.println("COPASI soluble Cell FINAL " + met + " " + value);

			}

		}

	}
	
}
