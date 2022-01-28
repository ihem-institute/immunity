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

public class PlasmaMembraneCopasiStep {
	
	public static void antPresTimeSeriesLoad(PlasmaMembrane plasmaMembrane){
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		if (plasmaMembrane.getPlasmaMembraneTimeSeries().isEmpty()){	
			System.out.println("recalcula Time series ");
			callReceptorDynamics(plasmaMembrane);
			timeSeriesLoadintoPlasmaMembrane(plasmaMembrane);

			return;
		} 
		if (tick >= Collections.max(plasmaMembrane.getPlasmaMembraneTimeSeries().keySet())) {
			timeSeriesLoadintoPlasmaMembrane(plasmaMembrane);
			plasmaMembrane.getPlasmaMembraneTimeSeries().clear();
			System.out.println("recalcula Time series por agotamiento");
			callReceptorDynamics(plasmaMembrane);

			return;
			}
		if (!plasmaMembrane.getPlasmaMembraneTimeSeries().containsKey(tick)) {
//			System.out.println("Return without UPDATED");
			return;
		}else {
			timeSeriesLoadintoPlasmaMembrane(plasmaMembrane);
			return;

		}
	}
	public static void timeSeriesLoadintoPlasmaMembrane(PlasmaMembrane plasmaMembrane){
//		PlasmaMembrane plasmaMembrane = PlasmaMembrane.getInstance();
//		values in plasmaMembraneTimeSeries are in mM.  Transform back in area and volume units multiplying
//		by area the membrane metabolites and by volume the soluble metabolites
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		HashMap<String, Double> presentValues = new HashMap<String, Double>(plasmaMembrane.getPlasmaMembraneTimeSeries().get(tick));
		HashMap<String, Double> pastValues = new HashMap<String, Double>();
		int pastTick = 0;
		if (tick == plasmaMembrane.plasmaMembraneTimeSeries.firstKey()){ ;// (int) (tick + 1 - Cell.timeScale/0.03);
		pastValues = presentValues;
		pastTick = tick;
		} else {
			pastTick = plasmaMembrane.plasmaMembraneTimeSeries.lowerKey(tick);
			pastValues = plasmaMembrane.plasmaMembraneTimeSeries.get(pastTick);
		}
		for (String met :presentValues.keySet()){
//			Organelle (endosomes/Golgi) metabolites are not considered. The reaction should be called from endosomes.
			String met1 = met;// met.substring(0, met.length()-2); now copasi uses the complete names
//			metabolites in the Cell are expressed in concentration. I am using the area ratio between PM and Cell 
//			for dilution of the metabilite that is released into the cell.  I may use volume ratio? 
//			Only a fraction of the metabolite in the cell participates
//			in copasi, hence the concentration are added to the existing values.
//			Since the PM is releasing Cyto metabolites at each tick, what must be incorporated is the delta with respect to the previous tick.
//			At tick = 0, nothing is released (pastValues = presentValues)
			if (met.endsWith("Cy")){
				 if (!Cell.getInstance().getSolubleCell().containsKey(met)){Cell.getInstance().getSolubleCell().put(met, 0.0);}
//								 System.out.println("TICK " + met+tick + "\n " + pastTick + "\n " + presentValues.get(met) + "\n " + pastValues.get(met) + "\n" + plasmaMembrane
//		 );
					double delta =  presentValues.get(met) - pastValues.get(met);
					double metValue = Cell.getInstance().getSolubleCell().get(met)
										+ delta * plasmaMembrane.getPlasmaMembraneArea()/Cell.getInstance().getCellArea();
								Cell.getInstance().getSolubleCell().put(met, metValue);
			}
			
			else if ((met.endsWith("Pm") || met.endsWith("En")) && ModelProperties.getInstance().getSolubleMet().contains(met1)){
				double metValue = presentValues.get(met)* plasmaMembrane.getPlasmaMembraneVolume();
				plasmaMembrane.solubleRecycle.put(met, metValue);
			}
			else if ((met.endsWith("Pm") || met.endsWith("En")) && ModelProperties.getInstance().getMembraneMet().contains(met1)) {
				double metValue = presentValues.get(met)* plasmaMembrane.getPlasmaMembraneArea();
				plasmaMembrane.membraneRecycle.put(met, metValue);
				 System.out.println("TICK " + met+tick + "\n " + pastTick + "\n " + presentValues.get(met) + "\n " + pastValues.get(met) + "\n" + metValue
);
			}
		}
		 System.out.println(PlasmaMembrane.getInstance().getMembraneRecycle() + " membrane Recycled \n" + plasmaMembrane.membraneRecycle);
		
	}
	
	public static void callReceptorDynamics(PlasmaMembrane plasmaMembrane) {
// Membrane and soluble metabolites are transformed from the area an volume units to mM.
// From my calculations (see Calculos), dividing these units by the area or the volume of the endosome, transform the 
//the values in mM.  Back from copasi, I recalculate the values to area and volume
//
		PlasmaMembraneCopasi receptorDynamics = PlasmaMembraneCopasi.getInstance();
		HashMap<String, Double> solubleRecycle = PlasmaMembrane.getInstance().getSolubleRecycle();
		HashMap<String, Double> membraneRecycle = PlasmaMembrane.getInstance().getMembraneRecycle();
//		HashMap<String, Double> solubleRecycle = plasmaMembrane.solubleRecycle;
//		HashMap<String, Double> membraneRecycle = plasmaMembrane.membraneRecycle;//()PlasmaMembrane.getInstance().getMembraneRecycle();//plasmaMembrane.getMembraneRecycle();

		Set<String> metabolites = receptorDynamics.getMetabolites();
		HashMap<String, Double> localM = new HashMap<String, Double>();
		System.out.println("PM MEMBRENE RECYCLE " + membraneRecycle + metabolites);
		
		for (String met : metabolites) {
//			System.out.println("metabolito que no anda" + met);
			String met1 = met; //.substring(0, met.length()-2);COPASI uses metabolite names  with the substring incorporated

			if (met.endsWith("Pm") && membraneRecycle.containsKey(met1)) {
				double metValue = membraneRecycle.get(met)/plasmaMembrane.getPlasmaMembraneArea();
				receptorDynamics.setInitialConcentration(met, sigFigs(metValue,6));
				localM.put(met, metValue);
			} else if (met.endsWith("Pm") && solubleRecycle.containsKey(met1)) {
				double metValue = solubleRecycle.get(met1)/plasmaMembrane.getPlasmaMembraneVolume();
				receptorDynamics.setInitialConcentration(met, sigFigs(metValue,6));
				localM.put(met, metValue);
			} else if (met.endsWith("En") && membraneRecycle.containsKey(met)) {
				double metValue = membraneRecycle.get(met)/plasmaMembrane.getPlasmaMembraneArea();
				receptorDynamics.setInitialConcentration(met, sigFigs(metValue,6));
				localM.put(met, metValue);
			} else if (met.endsWith("En") && solubleRecycle.containsKey(met1)) {
				double metValue = solubleRecycle.get(met1)/plasmaMembrane.getPlasmaMembraneVolume();
				receptorDynamics.setInitialConcentration(met, sigFigs(metValue,6));
				localM.put(met, metValue);				
			} else if (met.endsWith("Cy") && Cell.getInstance().getSolubleCell().containsKey(met1)) {
				double metValue = Cell.getInstance().getSolubleCell().get(met1);
				double metLeft = metValue*(Cell.getInstance().getCellVolume() - plasmaMembrane.getPlasmaMembraneVolume())/(Cell.getInstance().getCellVolume());
				Cell.getInstance().getSolubleCell().put(met1, metLeft);
				receptorDynamics.setInitialConcentration(met, sigFigs(metValue,6));
				localM.put(met, metValue);
			} else {
				receptorDynamics.setInitialConcentration(met, 0.0);
				localM.put(met, 0.0);
			}
		}
//		receptorDynamics.setInitialConcentration("protonCy", 1e-04);
//		localM.put("protonCy", 1e-04);

	//	if (localM.get("protonEn")==null||localM.get("protonEn") < 1e-05){
			receptorDynamics.setInitialConcentration("protonEn", 3.98e-5);
			receptorDynamics.setInitialConcentration("protonCy", 3.98e-5);
			localM.put("protonEn", 3.98e-05);
	//	}
		
		
	
System.out.println(membraneRecycle +""+"LOCAL MMMMMMM " + localM);

		receptorDynamics.runTimeCourse();
		

		CTimeSeries timeSeries = receptorDynamics.getTrajectoryTask().getTimeSeries();
		int stepNro = (int) timeSeries.getRecordedSteps();
		int metNro = metabolites.size();
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		for (int time = 0; time < stepNro; time = time + 1){
			HashMap<String, Double> value = new HashMap<String, Double>();
			for (int met = 1; met < metNro +1; met = met +1){
	//			value.put(timeSeries.getTitle(met), timeSeries.getConcentrationData(time, met));
				value.put(timeSeries.getTitle(met), sigFigs(timeSeries.getConcentrationData(time, met),6));
				plasmaMembrane.getPlasmaMembraneTimeSeries().put((int) (tick+time*Cell.timeScale/0.03),value);
			}
		}
		
			

			

		}
	public static double sigFigs(double n, int sig) {
//		if (Math.abs(n) < 1E-20) return 0d;
//		else 
//		{
		double mult = Math.pow(10, sig - Math.floor(Math.log(n) / Math.log(10) + 1));
	    return Math.round(n * mult) / mult;
//	    }
	}
	
}

