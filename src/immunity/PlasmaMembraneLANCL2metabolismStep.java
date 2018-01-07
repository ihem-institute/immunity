package immunity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.COPASI.CTimeSeries;

import repast.simphony.engine.environment.RunEnvironment;

public class PlasmaMembraneLANCL2metabolismStep {
	
	public static void PMLANCL2TimeSeriesLoad(PlasmaMembrane plasmaMembrane){
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		System.out.println("PMLANCL2 time series "+ tick +" " +plasmaMembrane.getPMLANCL2TimeSeries());		

		if (plasmaMembrane.getPMLANCL2TimeSeries().isEmpty()){			
			callPMLANCL2metabolism(plasmaMembrane);
			timeSeriesLoadintoPM(plasmaMembrane);
			System.out.println("PMLANCL2 first time");
			return;
		} 
		if (tick >= Collections.max(plasmaMembrane.getPMLANCL2TimeSeries().keySet())) {
//			System.out.println("COLLECTION" + tick + " " + plasmaMembrane.getPMLANCL2TimeSeries().keySet());
			timeSeriesLoadintoPM(plasmaMembrane);
			plasmaMembrane.getPMLANCL2TimeSeries().clear();
			callPMLANCL2metabolism(plasmaMembrane);
			System.out.println("PMLANCL2 called after 50 time series");
//			System.out.println("COLLECTION" + tick + " " + plasmaMembrane.getPMLANCL2TimeSeries().keySet());
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
//		values in PMLANCL2TimeSeries are in mM.  Transform back in area  units multiplying
//		by area the membrane metabolites 
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		HashMap<String, Double> presentValues = new HashMap<String, Double>(plasmaMembrane.PMLANCL2TimeSeries.get(tick));
		

		double pLANCL2 = presentValues.get("pLANCL2")* plasmaMembrane.area;
		plasmaMembrane.membraneRecycle.put("pLANCL2", pLANCL2);
		double LANCL2 = presentValues.get("LANCL2")* plasmaMembrane.area;
		plasmaMembrane.membraneRecycle.put("LANCL2", LANCL2);
	
	System.out.println("PMLANCL2 UPDATED");
//	for (String met :presentValues.keySet()){
//	UPDATE LANCL2cyto using 
//	System.out.println(met+ " "+presentValues.get(met));
//		}
		
	}

	
	public static void callPMLANCL2metabolism(PlasmaMembrane plasmamembrane) {

		PMLANCL2metabolism pmLANCL2metabolism = PMLANCL2metabolism.getInstance();
		Set<String> metabolites = pmLANCL2metabolism.getMetabolites();
		for (String met : metabolites){
			pmLANCL2metabolism.setInitialConcentration(met, 0);
		}

		if (plasmamembrane.membraneRecycle.containsKey("pLANCL2")) {
		double metValue = plasmamembrane.membraneRecycle.get("pLANCL2")/plasmamembrane.area;
		pmLANCL2metabolism.setInitialConcentration("pLANCL2", metValue);
//		System.out.println("COPASI INITIAL PM pLANCL2  " +metValue);
		}

		if (plasmamembrane.membraneRecycle.containsKey("LANCL2")) {
		double	metValue = plasmamembrane.membraneRecycle.get("LANCL2")/plasmamembrane.area;
		
		pmLANCL2metabolism.setInitialConcentration("LANCL2", metValue);
//		System.out.println("COPASI INITIAL PM pLANCL2  " +metValue);
		}

		if (Cell.getInstance().getSolubleCell().containsKey("ABA")) {
		double metValue = Cell.getInstance().getSolubleCell().get("ABA");
		
		pmLANCL2metabolism.setInitialConcentration("ABA", metValue);
//		System.out.println("COPASI INITIAL  Cell ABA " + metValue);
		}
		
		if (Cell.getInstance().getSolubleCell().containsKey("LANCL2cyto")) {
		double	metValue = Cell.getInstance().getSolubleCell().get("LANCL2cyto");
		
		pmLANCL2metabolism.setInitialConcentration("LANCL2cyto", metValue);
//		System.out.println("COPASI INITIAL  Cell LANCL2cyto " + metValue);
		}	

		pmLANCL2metabolism.runTimeCourse();

		CTimeSeries timeSeries = pmLANCL2metabolism.getTrajectoryTask().getTimeSeries();
		int stepNro = (int) timeSeries.getRecordedSteps();
		int metNro = metabolites.size();
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		for (int time = 0; time < stepNro; time = time + 1){
			HashMap<String, Double> value = new HashMap<String, Double>();
			for (int met = 1; met < metNro +1; met = met +1){
				value.put(timeSeries.getTitle(met), timeSeries.getConcentrationData(time, met));
				plasmamembrane.getPMLANCL2TimeSeries().put((int) (tick+time*Cell.timeScale/0.03),value);
			}
		}
		
//		System.out.println("PMLANCL2 time series "+ tick +" " +plasmamembrane.getPMLANCL2TimeSeries());		
//		A protein released from a membrane increases the concentration in the cytosol in:
//		copasi concentration in mM * area of the organelle * 3*10^(-8) (mM)
		double metValue = 0;
		if (Cell.getInstance().getSolubleCell().containsKey("LANCL2cyto")){
		metValue = pmLANCL2metabolism.getConcentration("LANCL2cyto")* plasmamembrane.area * 3 * 1E-8
				+Cell.getInstance().getSolubleCell().get("LANCL2cyto");	
		} else {
		metValue =pmLANCL2metabolism.getConcentration("LANCL2cyto")*plasmamembrane.area* 3 * 1E-8;
		}
		Cell.getInstance().getSolubleCell().put("LANCL2", metValue);
//		System.out.println("COPASI Cell FINAL LANCL2cyto " + metValue);

		}

}
	
