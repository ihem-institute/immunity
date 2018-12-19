package immunity;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Collections;
import org.COPASI.CModel;
import org.COPASI.CTimeSeries;
import org.apache.commons.lang3.StringUtils;

import repast.simphony.engine.environment.RunEnvironment;

public class EndosomeCopasiStep {
	
	public static void antPresTimeSeriesLoad(Endosome endosome){
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		if (endosome.getEndosomeTimeSeries().isEmpty()){			
			callLipidPresentation(endosome);
			timeSeriesLoadintoEndosome(endosome);

			return;
		} 
		if (tick >= Collections.max(endosome.getEndosomeTimeSeries().keySet())) {
			timeSeriesLoadintoEndosome(endosome);
			endosome.getEndosomeTimeSeries().clear();
			callLipidPresentation(endosome);

			return;
			}
		if (!endosome.endosomeTimeSeries.containsKey(tick)) {
//			System.out.println("Return without UPDATED");
			return;
		}else {

			timeSeriesLoadintoEndosome(endosome);
			return;

		}
	}
	public static void timeSeriesLoadintoEndosome(Endosome endosome){
//		values in endosomeTimeSeries are in mM.  Transform back in area and volume units multiplying
//		by area the membrane metabolites and by volume the soluble metabolites
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		HashMap<String, Double> presentValues = new HashMap<String, Double>(endosome.endosomeTimeSeries.get(tick));

		for (String met :presentValues.keySet()){
			// if the content is cytosolic, increase the cell pull proportinal to the volume.  The content is
			//			eliminated from endosome
			String met1 = met.substring(0, met.length()-2);
			if (StringUtils.endsWith(met, "En") && endosome.solubleMet.contains(met1)) {
				double metValue = presentValues.get(met)* endosome.volume;
				endosome.solubleContent.put(met1, metValue);
			}
			else if (StringUtils.endsWith(met, "En") && endosome.membraneMet.contains(met1)) {
				double metValue = presentValues.get(met)* endosome.area;
				endosome.membraneContent.put(met1, metValue);
			}
//			metabolites in the Cell are expressed in concentration. Only a fraction of the metabolite in the cell participates
//			in copasi, hence the concentration are added to the existing values.
			else if (StringUtils.endsWith(met, "Cy")){
				double metValue = Cell.getInstance().getSolubleCell().get(met1)
						+ presentValues.get(met)* endosome.volume/Cell.volume;
				Cell.getInstance().getSolubleCell().put(met1, metValue);
				//				endosome.solubleContent.remove(met1);
			}
//			Only a fraction of the metabolite in the plasma membrane participates
//			in copasi, hence the concentration are added to the existing values.
			else if (StringUtils.endsWith(met, "Pm") && PlasmaMembrane.getInstance().getSolubleRecycle().containsKey(met1)) {
				double metValue = PlasmaMembrane.getInstance().getSolubleRecycle().get(met1)
						+ presentValues.get(met)* endosome.volume;
				PlasmaMembrane.getInstance().getSolubleRecycle().put(met1, metValue);
			}
			else if (StringUtils.endsWith(met, "Pm") && PlasmaMembrane.getInstance().getMembraneRecycle().containsKey(met1)) {
				double metValue = PlasmaMembrane.getInstance().getMembraneRecycle().get(met1)
						+ presentValues.get(met)* endosome.area;
				PlasmaMembrane.getInstance().getMembraneRecycle().put(met1, metValue);
			}
		}
		
	}
	
	public static void callLipidPresentation(Endosome endosome) {
// Membrane and soluble metabolites are transformed from the area an volume units to mM.
// From my calculations (see Calculos), dividing these units by the area or the volume of the endosome, transform the 
//the values in mM.  Back from copasi, I recalculate the values to area and volume
//
		EndosomeCopasi lipidMetabolism = EndosomeCopasi
				.getInstance();

		Set<String> metabolites = lipidMetabolism.getInstance()
				.getMetabolites();
		HashMap<String, Double> localM = new HashMap<String, Double>();
		for (String met : metabolites) {
			String met1 = met.substring(0, met.length()-2);
//			for endosomes and other organelles, all the metabolites participate in the reaction
			if (StringUtils.endsWith(met, "En") && endosome.membraneContent.containsKey(met1)) {
				double metValue = endosome.membraneContent.get(met1)/endosome.area;
				lipidMetabolism.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
				localM.put(met, metValue);
			} else if (StringUtils.endsWith(met, "En") && endosome.solubleContent.containsKey(met1)) {
				double metValue = Math.abs(endosome.solubleContent.get(met1))/endosome.volume;
				lipidMetabolism.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
				localM.put(met, metValue);
//				for metabolites in the plasma membrane, only a fraction participate in the reaction and it is consumed 
//				for soluble metabolites, proportional to the volume and for membrane metabolites proportional to the area
			} else if (StringUtils.endsWith(met, "Pm") && PlasmaMembrane.getInstance().getMembraneRecycle().containsKey(met1)) {
				double metValue = PlasmaMembrane.getInstance().getMembraneRecycle().get(met1)/PlasmaMembrane.getInstance().area;
				double metLeft = metValue* (PlasmaMembrane.getInstance().area - endosome.area);
				PlasmaMembrane.getInstance().getMembraneRecycle().put(met1, metLeft);
				lipidMetabolism.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
				localM.put(met, metValue);
			} else if (StringUtils.endsWith(met, "Pm") && PlasmaMembrane.getInstance().getSolubleRecycle().containsKey(met1)) {
				double metValue = Math.abs(PlasmaMembrane.getInstance().getSolubleRecycle().get(met1))/PlasmaMembrane.getInstance().volume;
				double metLeft = metValue* (PlasmaMembrane.getInstance().volume - endosome.volume);
				PlasmaMembrane.getInstance().getSolubleRecycle().put(met1, metLeft);
				lipidMetabolism.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
				localM.put(met, metValue);
//				Rabs are included only for reactions that occurrs in a specific compartment. 
			} else if (StringUtils.startsWith(met, "Rab") && endosome.rabContent.containsKey(met)) {
				double metValue = Math.abs(endosome.rabContent.get(met))/endosome.area;
				lipidMetabolism.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
				localM.put(met, metValue);
//				for metabolites in the cell, only a fraction participate in the reaction and it is consumed 
//				metabolites in the cell are in concentration units and only soluble metabolites are considered
			} else if (StringUtils.endsWith(met, "Cy") && Cell.getInstance().getSolubleCell().containsKey(met1)) {
				double metValue = Cell.getInstance().getSolubleCell().get(met1);
				double metLeft = metValue*(Cell.getInstance().volume)/(Cell.getInstance().volume - endosome.volume);
				Cell.getInstance().getSolubleCell().put(met1, metLeft);
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
				endosome.getEndosomeTimeSeries().put((int) (tick+time*Cell.timeScale/0.03),value);
			}
		}
		
		}
	
}

