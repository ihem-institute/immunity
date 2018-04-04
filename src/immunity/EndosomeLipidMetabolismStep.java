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

public class EndosomeLipidMetabolismStep {
	
	public static void antPresTimeSeriesLoad(Endosome endosome){
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		if (endosome.getLipidTimeSeries().isEmpty()){			
			callLipidPresentation(endosome);
			timeSeriesLoadintoEndosome(endosome);

			return;
		} 
		if (tick >= Collections.max(endosome.getLipidTimeSeries().keySet())) {
			timeSeriesLoadintoEndosome(endosome);
			endosome.getLipidTimeSeries().clear();
			callLipidPresentation(endosome);

			return;
			}
		if (!endosome.lipidTimeSeries.containsKey(tick)) {
//			System.out.println("Return without UPDATED");
			return;
		}else {

			timeSeriesLoadintoEndosome(endosome);
			return;

		}
	}
	public static void timeSeriesLoadintoEndosome(Endosome endosome){
//		values in lipidTimeSeries are in mM.  Transform back in area and volume units multiplying
//		by area the membrane metabolites and by volume the soluble metabolites
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		HashMap<String, Double> presentValues = new HashMap<String, Double>(endosome.lipidTimeSeries.get(tick));
		for (String met :presentValues.keySet()){
			
			if (met.equals("preP")){
				double metValue = Cell.getInstance().getSolubleCell().get(met)
						+presentValues.get(met)* endosome.volume/Cell.volume;

				Cell.getInstance().getSolubleCell().put("preP", metValue);
				}
			else if (met == "pept"){

				double metValue = Cell.getInstance().getSolubleCell().get(met)
						+presentValues.get(met)* endosome.volume/Cell.volume;
				Cell.getInstance().getSolubleCell().put("pept", metValue);
				}
			else if (endosome.membraneMet.contains(met)) {
			double metValue = presentValues.get(met)* endosome.area;
			endosome.membraneContent.put(met, metValue);
				} 
			else if (endosome.solubleMet.contains(met)) {
			double metValue = presentValues.get(met)* endosome.volume;
			endosome.solubleContent.put(met, metValue);
				}
	}
	endosome.solubleContent.remove("preP");
	endosome.solubleContent.remove("pept");

		
	}
	
	public static void callLipidPresentation(Endosome endosome) {
// Membrane and soluble metabolites are transformed from the area an volume units to mM.
// From my calculations (see Calculos), dividing these units by the area or the volume of the endosome, transform the 
//the values in mM.  Back from copasi, I recalculate the values to area and volume
//
		LipidMetabolism lipidMetabolism = LipidMetabolism
				.getInstance();

		Set<String> metabolites = lipidMetabolism.getInstance()
				.getMetabolites();
		HashMap<String, Double> localM = new HashMap<String, Double>();
		for (String met : metabolites) {
			if (endosome.membraneContent.containsKey(met)) {
				double metValue = endosome.membraneContent.get(met)/endosome.area;
				lipidMetabolism.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
				localM.put(met, metValue);
			} else if (endosome.rabContent.containsKey(met)) {
				double metValue = Math.abs(endosome.rabContent.get(met))/endosome.area;
				lipidMetabolism.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
				localM.put(met, metValue);
			} else if (endosome.solubleContent.containsKey(met)) {
				double metValue = Math.abs(endosome.solubleContent.get(met))/endosome.volume;
				lipidMetabolism.setInitialConcentration(met, Math.round(metValue*1E12d)/1E12d);
				localM.put(met, metValue);
			} else if (Cell.getInstance().getSolubleCell().containsKey(met)) {
				double metValue = Cell.getInstance().getSolubleCell().get(met);
				lipidMetabolism.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
				localM.put(met, metValue);
			} else {
				lipidMetabolism.setInitialConcentration(met, 0.0);
				localM.put(met, 0.0);
			}
		}
		lipidMetabolism.setInitialConcentration("protonCy", 1e-04);
		localM.put("protonCy", 1e-04);

		if (localM.get("proton")==null||localM.get("proton") < 1e-05){
			lipidMetabolism.setInitialConcentration("proton", 1e-04);
			localM.put("proton", 1e-04);
		}


		lipidMetabolism.runTimeCourse();
		

		CTimeSeries timeSeries = lipidMetabolism.getTrajectoryTask().getTimeSeries();
		int stepNro = (int) timeSeries.getRecordedSteps();
		int metNro = metabolites.size();
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		for (int time = 0; time < stepNro; time = time + 1){
			HashMap<String, Double> value = new HashMap<String, Double>();
			for (int met = 1; met < metNro +1; met = met +1){
				value.put(timeSeries.getTitle(met), timeSeries.getConcentrationData(time, met));
				endosome.getLipidTimeSeries().put((int) (tick+time*Cell.timeScale/0.03),value);
			}
		}
		
			

			

		}
	
}

