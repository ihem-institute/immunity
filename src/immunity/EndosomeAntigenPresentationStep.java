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
		if (!endosome.antigenTimeSeries.containsKey(tick)) return;
		if (tick > Collections.max(endosome.antigenTimeSeries.keySet())) {
			antigenPresentation(endosome);
			}
		HashMap<String, Double> presentValues = new HashMap<String, Double>(endosome.antigenTimeSeries.get(tick));
		
		for (String met :presentValues.keySet()){
		if (endosome.membraneMet.contains(met)) {
			double metValue = (double) Math.abs(Math
					.round(presentValues.get(met)));
			endosome.membraneContent.put(met, metValue);
		} else if (endosome.solubleMet.contains(met)) {
			double metValue = (double) Math.abs(Math
					.round(presentValues.get(met)));
			endosome.solubleContent.put(met, metValue);
		} else
			System.out.println("Met not found in " + endosome.membraneMet + " "
					+ endosome.solubleMet + " " + met);
	}
	System.out.println("AntigenPresentation FINAL ");
	for (String met :presentValues.keySet()){
	System.out.println(met+ " "+presentValues.get(met));
		}
		
	}
	public static void antigenPresentation(Endosome endosome) {

		AntigenPresentation antigenPresentation = AntigenPresentation
				.getInstance();

		Set<String> metabolites = antigenPresentation.getInstance()
				.getMetabolites();
		int antPresTicksPerSec = 67;
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
		
//		CModel model = antigenPresentation.getModel();
		CTimeSeries timeSeries = antigenPresentation.getTrajectoryTask().getTimeSeries();
		int stepNro = (int) timeSeries.getRecordedSteps();
		int metNro = metabolites.size();
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		for (int time = 1; time < stepNro; time = time + 1){
			HashMap<String, Double> value = new HashMap<String, Double>();
			for (int met = 1; met < metNro +1; met = met +1){
				value.put(timeSeries.getTitle(met), timeSeries.getConcentrationData(time, met));
				endosome.getAntigenTimeSeries().put(tick+time*antPresTicksPerSec,value);
			}
		}
		
		System.out.println("AntigenPresentation time series "+ endosome.getAntigenTimeSeries());		
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

