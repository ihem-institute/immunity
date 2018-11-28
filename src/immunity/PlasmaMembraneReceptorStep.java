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
// if the content is cytosolic, increase the cell pull proportinal to the volume.  The content is
//			eliminated from plasma membrane
			
			if (Cell.getInstance().getSolubleCell().containsKey(met)){
				double metValue = Cell.getInstance().getSolubleCell().get(met)
						+presentValues.get(met)* plasmaMembrane.volume/Cell.volume;

				Cell.getInstance().getSolubleCell().put(met, metValue);
				plasmaMembrane.solubleRecycle.remove(met);
				}
//			else if (met == "pept"){
//
//				double metValue = Cell.getInstance().getSolubleCell().get(met)
//						+presentValues.get(met)* endosome.volume/Cell.volume;
//				Cell.getInstance().getSolubleCell().put("pept", metValue);
//				}
			else if (plasmaMembrane.membraneRecycle.containsKey(met)) {
			double metValue = presentValues.get(met)*plasmaMembrane.area;
			plasmaMembrane.membraneRecycle.put(met, metValue);
				} 
			else if (plasmaMembrane.solubleRecycle.containsKey(met)) {
			double metValue = presentValues.get(met)*plasmaMembrane.volume;
			plasmaMembrane.solubleRecycle.put(met, metValue);
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
			if (plasmaMembrane.getMembraneRecycle().containsKey(met)) {
				double metValue = plasmaMembrane.getMembraneRecycle().get(met)/plasmaMembrane.area;
				receptorDynamics.setInitialConcentration(met, Math.round(metValue*1E12d)/1E12d);
				localM.put(met, metValue);
//			} else if (endosome.rabContent.containsKey(met)) {
//				double metValue = Math.abs(endosome.rabContent.get(met))/endosome.area;
//				lipidMetabolism.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
//				localM.put(met, metValue);
			} else if (plasmaMembrane.getSolubleRecycle().containsKey(met)) {
				double metValue = plasmaMembrane.getSolubleRecycle().get(met)/plasmaMembrane.volume;
				receptorDynamics.setInitialConcentration(met, Math.round(metValue*1E12d)/1E12d);
				localM.put(met, metValue);
			} else if (Cell.getInstance().getSolubleCell().containsKey(met)) {
				double metValue = Cell.getInstance().getSolubleCell().get(met);
				receptorDynamics.setInitialConcentration(met, Math.round(metValue*1E12d)/1E12d);
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

