package immunity;

import java.util.HashMap;
import java.util.Set;

public class PlasmaMembraneLANCL2metabolismStep {
	
	public static void LANCL2metabolism(PlasmaMembrane plasmamembrane) {

		LANCL2metabolism lANCL2metabolism = LANCL2metabolism.getInstance();
		double metValue = 0.0;
		if (plasmamembrane.membraneRecycle.containsKey("pLANCL2")) {
			metValue = Math.round
						(plasmamembrane.membraneRecycle.get("pLANCL2") * 1000) / 1000;
		}
		lANCL2metabolism.setInitialConcentration("pLANCL2", metValue);
		System.out.println("COPASI INITIAL PM pLANCL2  " +metValue);

		metValue = 0.0;
		if (Cell.getInstance().getSolubleCell().containsKey("LANCL2")) {
			metValue = Math.round
					(Cell.getInstance().getSolubleCell().get("LANCL2") * 1000) / 1000;
		}
		lANCL2metabolism.setInitialConcentration("LANCL2", metValue);
		System.out.println("COPASI INITIAL  Cell LANCL2 " + metValue);
		
		metValue = 0.0;
		if (Cell.getInstance().getSolubleCell().containsKey("ABA")) {
			metValue = Math.round
					(Cell.getInstance().getSolubleCell().get("ABA") * 1000) / 1000;
		}
			lANCL2metabolism.setInitialConcentration("ABA", metValue);
			System.out.println("COPASI INITIAL  Cell ABA " + metValue);


		lANCL2metabolism.runTimeCourse();

		metValue = lANCL2metabolism.getConcentration("pLANCL2");

		plasmamembrane.membraneRecycle.put("pLANCL2", metValue);
		System.out.println("COPASI PM FINAL pLANCL2 " + metValue);
		
		metValue = lANCL2metabolism.getConcentration("LANCL2");
		Cell.getInstance().getSolubleCell().put("LANCL2", metValue);
		System.out.println("COPASI Cell FINAL LANCL2 " + metValue);
		
		metValue = lANCL2metabolism.getConcentration("ABA");
		Cell.getInstance().getSolubleCell().put("ABA", metValue);
		System.out.println("COPASI Cell FINAL ABA " + metValue);
		}

}
	
