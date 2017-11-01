package immunity;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import org.COPASI.CModel;
import org.COPASI.CTimeSeries;

import repast.simphony.engine.environment.RunEnvironment;

public class EndosomeAntigenPresentationStep {
	
	public static void antPresTimeSeriesLoad(Endosome endosome){
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		if (endosome.getAntigenTimeSeries().isEmpty()){			
			callAntigenPresentation(endosome);
			timeSeriesLoadintoEndosome(endosome);
			System.out.println("AntigenPresentation first time");
			return;
		} 
		if (tick >= Collections.max(endosome.getAntigenTimeSeries().keySet())) {
			System.out.println("COLLECTION" + tick + " " + endosome.getAntigenTimeSeries().keySet());
			timeSeriesLoadintoEndosome(endosome);
			endosome.getAntigenTimeSeries().clear();
			callAntigenPresentation(endosome);
			System.out.println("AntigenPresentation called after 50 time series");
//			System.out.println("COLLECTION" + tick + " " + endosome.getAntigenTimeSeries().keySet());
			return;
			}
		if (!endosome.antigenTimeSeries.containsKey(tick)) {
//			System.out.println("Return without UPDATED");
			return;
		}else {
			timeSeriesLoadintoEndosome(endosome);
			System.out.println("ANTIGEN UPDATED FROM TIME SERIES");
			return;

		}
	}
	public static void timeSeriesLoadintoEndosome(Endosome endosome){
//		values in antigenTimeSeries are in mM.  Transform back in area and volume units multiplying
//		by area the membrane metabolites and by volume the soluble metabolites
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		HashMap<String, Double> presentValues = new HashMap<String, Double>(endosome.antigenTimeSeries.get(tick));
		
		for (String met :presentValues.keySet()){
		if (endosome.membraneMet.contains(met)) {
			double metValue = presentValues.get(met)* endosome.area;
			endosome.membraneContent.put(met, metValue);
		} else if (endosome.solubleMet.contains(met)) {
			double metValue = presentValues.get(met)* endosome.volume;
			endosome.solubleContent.put(met, metValue);
		} //else
//			System.out.println("Met not found in " + endosome.membraneMet + " "
//					+ endosome.solubleMet + " " + met);
		if (met == "preP"){
			double metValue=0d;
			if (Cell.getInstance().getSolubleCell().containsKey(met)) {
			metValue = Cell.getInstance().getSolubleCell().get(met)
					+presentValues.get(met)* endosome.volume/Cell.volume;
			}
			else{
			metValue = presentValues.get(met)* endosome.volume/Cell.volume;
			}
			Cell.getInstance().getSolubleCell().put("preP", metValue);
		}
		if (met == "pept"){
			double metValue=0d;
			if (Cell.getInstance().getSolubleCell().containsKey(met)) {
			metValue = Cell.getInstance().getSolubleCell().get(met)
					+presentValues.get(met)* endosome.volume/Cell.volume;
			}
			else{
			metValue = presentValues.get(met)* endosome.volume/Cell.volume;
			}
			Cell.getInstance().getSolubleCell().put("pept", metValue);
		}
	}
//	System.out.println("AntigenPresentation UPDATED");
//	for (String met :presentValues.keySet()){
//	System.out.println(met+ " "+presentValues.get(met));
//		}
		
	}
	
	public static void callAntigenPresentation(Endosome endosome) {
// Membrane and soluble metabolites are transformed from the area an volume units to mM.
// From my calculations (see Calculos), dividing these units by the area or the volume of the endosome, transform the 
//the values in mM.  Back from copasi, I recalculate the values to area and volume
//
		AntigenPresentation antigenPresentation = AntigenPresentation
				.getInstance();

		Set<String> metabolites = antigenPresentation.getInstance()
				.getMetabolites();
		HashMap<String, Double> localM = new HashMap<String, Double>();
		for (String met : metabolites) {
			if (endosome.membraneContent.containsKey(met)) {
				double metValue = endosome.membraneContent.get(met)/endosome.area;
				antigenPresentation.setInitialConcentration(met, metValue);
				localM.put(met, metValue);
			} else if (endosome.solubleContent.containsKey(met)) {
				double metValue = endosome.solubleContent.get(met)/endosome.volume;
				antigenPresentation.setInitialConcentration(met, metValue);
				localM.put(met, metValue);
			} else if (Cell.getInstance().getSolubleCell().containsKey(met)) {
				double metValue = Cell.getInstance().getSolubleCell().get(met);
				antigenPresentation.setInitialConcentration(met, metValue);
				localM.put(met, metValue);
			} else {
				antigenPresentation.setInitialConcentration(met, 0.0);
				localM.put(met, 0.0);
			}
		}
		System.out.println("AntigenPresentation INICIAL ");
//		for (String met :metabolites){
//		System.out.println(met+ " "+localM.get(met));
//		}
		/*
		 * double sm1 = localM.get("ova"); double sc1 = localM.get("p1"); double
		 * sm2 = localM.get("preP"); double sc2 = localM.get("p2"); if ((sm1 ==
		 * 0 || sc1 == 0) && (sm2 == 0 || sc2 == 0)) return;
		 */

		antigenPresentation.runTimeCourse();
		
//		CModel model = antigenPresentation.getModel();
		CTimeSeries timeSeries = antigenPresentation.getTrajectoryTask().getTimeSeries();
		int stepNro = (int) timeSeries.getRecordedSteps();
		int metNro = metabolites.size();
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		for (int time = 0; time < stepNro; time = time + 1){
			HashMap<String, Double> value = new HashMap<String, Double>();
			for (int met = 1; met < metNro +1; met = met +1){
				value.put(timeSeries.getTitle(met), timeSeries.getConcentrationData(time, met));
				endosome.getAntigenTimeSeries().put((int) (tick+time*Cell.timeScale/0.015),value);
			}
		}
		
//		System.out.println("AntigenPresentation time series "+ tick +" " +endosome.getAntigenTimeSeries());		
//				

			
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
		/*	for (String met : metabolites){
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
		((double) Math.abs(Math.round(antigenPresentation.getConcentration(met)))));*/
		}
	
}

