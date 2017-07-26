package immunity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import repast.simphony.engine.environment.RunEnvironment;

public class PlasmaMembraneLANCL2metabolismStep {
	
	public static void PMLANCL2TimeSeriesLoad(PlasmaMembrane plasmaMembrane){
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		if (plasmaMembrane.getPMLANCL2TimeSeries().isEmpty()){			
			PMLANCL2metabolism(plasmaMembrane);
			timeSeriesLoadintoPM(plasmaMembrane);
			System.out.println("PMLANCL2 first time");
			return;
		} 
		if (tick > Collections.max(plasmaMembrane.getPMLANCL2TimeSeries().keySet())) {
			System.out.println("COLLECTION" + tick + " " + plasmaMembrane.getPMLANCL2TimeSeries().keySet());
			plasmaMembrane.getPMLANCL2TimeSeries().clear();
			PMLANCL2metabolism(plasmaMembrane);
			timeSeriesLoadintoPM(plasmaMembrane);
			System.out.println("PMLANCL2 called after 50 time series");
			System.out.println("COLLECTION" + tick + " " + plasmaMembrane.getPMLANCL2TimeSeries().keySet());
			return;
			}
		if (!plasmaMembrane.PMLANCL2TimeSeries.containsKey(tick)) {
//			System.out.println("Return without UPDATED");
			return;
		}else {
			timeSeriesLoadintoPM(plasmaMembrane);
			System.out.println("LANCL2 UPDATED FROM TIME SERIES");
			return;

		}
	}
	public static void timeSeriesLoadintoPM(PlasmaMembrane plasmaMembrane){
//		values in antigenTimeSeries are in mM.  Transform back in area and volume units multiplying
//		by area the membrane metabolites and by volume the soluble metabolites
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		HashMap<String, Double> presentValues = new HashMap<String, Double>(plasmaMembrane.PMLANCL2TimeSeries.get(tick));
		

		double pLANCL2 = presentValues.get("pLANCL2")* plasmaMembrane.area;
		plasmaMembrane.membraneRecycle.put("pLANCL2", pLANCL2);
		double LANCL2 = presentValues.get("LANCL2")* plasmaMembrane.area;
		plasmaMembrane.membraneRecycle.put("LANCL2", LANCL2);
	
	System.out.println("PMLANCL2 UPDATED");
//	for (String met :presentValues.keySet()){
//	System.out.println(met+ " "+presentValues.get(met));
//		}
		
	}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void PMLANCL2metabolism(PlasmaMembrane plasmamembrane) {

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
	
