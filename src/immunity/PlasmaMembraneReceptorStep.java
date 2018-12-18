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

public class PlasmaMembraneReceptorStep {
	
	public static void antPresTimeSeriesLoad(PlasmaMembrane plasmaMembrane){
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		if (plasmaMembrane.getReceptorTimeSeries().isEmpty()){			
			callReceptorDynamics(plasmaMembrane);
			timeSeriesLoadintoPlasmaMembrane(plasmaMembrane);

			return;
		} 
		if (tick >= Collections.max(plasmaMembrane.getReceptorTimeSeries().keySet())) {
			timeSeriesLoadintoPlasmaMembrane(plasmaMembrane);
			plasmaMembrane.getReceptorTimeSeries().clear();
			callReceptorDynamics(plasmaMembrane);

			return;
			}
		if (!plasmaMembrane.getReceptorTimeSeries().containsKey(tick)) {
//			System.out.println("Return without UPDATED");
			return;
		}else {
			timeSeriesLoadintoPlasmaMembrane(plasmaMembrane);
			return;

		}
	}
	public static void timeSeriesLoadintoPlasmaMembrane(PlasmaMembrane plasmaMembrane){
//		values in receptorTimeSeries are in mM.  Transform back in area and volume units multiplying
//		by area the membrane metabolites and by volume the soluble metabolites
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		HashMap<String, Double> presentValues = new HashMap<String, Double>(plasmaMembrane.getReceptorTimeSeries().get(tick));
		
		for (String met :presentValues.keySet()){
//			Organelle (endosomes/Golgi) metabolites are not considered. The reaction should be called from endosomes.
			String met1 = met.substring(0, met.length()-2);
//			metabolites in the Cell are expressed in concentration. Only a fraction of the metabolite in the cell participates
//			in copasi, hence the concentration are added to the existing values.
			if (StringUtils.endsWith(met, "Cy")){
				double metValue = Cell.getInstance().getSolubleCell().get(met1)
						+ presentValues.get(met)* plasmaMembrane.volume/Cell.volume;
				Cell.getInstance().getSolubleCell().put(met1, metValue);
			}
			
			else if (StringUtils.endsWith(met, "Pm") && plasmaMembrane.getSolubleRecycle().containsKey(met1)) {
				double metValue = presentValues.get(met)* plasmaMembrane.volume;
				plasmaMembrane.getSolubleRecycle().put(met1, metValue);
			}
			else if (StringUtils.endsWith(met, "Pm") && plasmaMembrane.getMembraneRecycle().containsKey(met1)) {
				double metValue = presentValues.get(met)* plasmaMembrane.area;
				plasmaMembrane.getMembraneRecycle().put(met1, metValue);
			}
		}
		
	}
	
	public static void callReceptorDynamics(PlasmaMembrane plasmaMembrane) {
// Membrane and soluble metabolites are transformed from the area an volume units to mM.
// From my calculations (see Calculos), dividing these units by the area or the volume of the endosome, transform the 
//the values in mM.  Back from copasi, I recalculate the values to area and volume
//
		ReceptorDynamics receptorDynamics = ReceptorDynamics.getInstance();

		Set<String> metabolites = receptorDynamics.getInstance()
				.getMetabolites();
		HashMap<String, Double> localM = new HashMap<String, Double>();
		System.out.println("PM MEMBRENE RECYCLE " + plasmaMembrane.getMembraneRecycle());
		
		for (String met : metabolites) {
			System.out.println("metabolito que no anda" + met);
			String met1 = met.substring(0, met.length()-2);

			 if (StringUtils.endsWith(met, "Pm") && plasmaMembrane.getMembraneRecycle().containsKey(met1)) {
				double metValue = plasmaMembrane.getMembraneRecycle().get(met1)/plasmaMembrane.area;
				receptorDynamics.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
				localM.put(met, metValue);
			} else if (StringUtils.endsWith(met, "Pm") && plasmaMembrane.getSolubleRecycle().containsKey(met1)) {
				double metValue = Math.abs(plasmaMembrane.getSolubleRecycle().get(met1))/plasmaMembrane.volume;
				receptorDynamics.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
				localM.put(met, metValue);
			} else if (StringUtils.endsWith(met, "Cy") && Cell.getInstance().getSolubleCell().containsKey(met1)) {
				double metValue = Cell.getInstance().getSolubleCell().get(met1);
				double metLeft = metValue*(Cell.volume)/(Cell.volume - plasmaMembrane.volume);
				Cell.getInstance().getSolubleCell().put(met1, metLeft);
				receptorDynamics.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
				localM.put(met, metValue);
			} else {
				receptorDynamics.setInitialConcentration(met, 0.0);
				localM.put(met, 0.0);
			}
		}
		receptorDynamics.setInitialConcentration("protonCy", 1e-04);
		localM.put("protonCy", 1e-04);

		if (localM.get("proton")==null||localM.get("proton") < 1e-05){
			receptorDynamics.setInitialConcentration("proton", 1e-04);
			localM.put("proton", 1e-04);
		}
		
		
	
System.out.println("LOCAL MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM " + localM);

		receptorDynamics.runTimeCourse();
		

		CTimeSeries timeSeries = receptorDynamics.getTrajectoryTask().getTimeSeries();
		int stepNro = (int) timeSeries.getRecordedSteps();
		int metNro = metabolites.size();
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		for (int time = 0; time < stepNro; time = time + 1){
			HashMap<String, Double> value = new HashMap<String, Double>();
			for (int met = 1; met < metNro +1; met = met +1){
				value.put(timeSeries.getTitle(met), timeSeries.getConcentrationData(time, met));
				plasmaMembrane.getReceptorTimeSeries().put((int) (tick+time*Cell.timeScale/0.03),value);
			}
		}
		
			

			

		}
	
}

