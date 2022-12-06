package immunity;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.Collections;

import org.COPASI.CCompartment;
import org.COPASI.CModel;
import org.COPASI.CTimeSeries;
import org.apache.commons.lang3.StringUtils;

import repast.simphony.engine.environment.RunEnvironment;

public class PlasmaMembraneCopasiStep {
	static double delta = 0;
	public static void antPresTimeSeriesLoad(PlasmaMembrane plasmaMembrane){
		double delta = 0;
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		if (plasmaMembrane.getPlasmaMembraneTimeSeries().isEmpty()){			
			callReceptorDynamics(plasmaMembrane);
			timeSeriesLoadintoPlasmaMembrane(plasmaMembrane);
//			
//						System.out.println("re calculate because uptake" );
//						try {
//						TimeUnit.SECONDS.sleep(5);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}

			return;
		} 
		if (tick >= Collections.max(plasmaMembrane.getPlasmaMembraneTimeSeries().keySet())) {
			timeSeriesLoadintoPlasmaMembrane(plasmaMembrane);
			plasmaMembrane.getPlasmaMembraneTimeSeries().clear();
			callReceptorDynamics(plasmaMembrane);
//			System.out.println("re calculate se acabaron" );
//			try {
//			TimeUnit.SECONDS.sleep(5);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

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
//		values in plasmaMembraneTimeSeries are in mM.  Transform back in area and volume units multiplying
//		by area the membrane metabolites and by volume the soluble metabolites
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
//		System.out.println("tick "+ plasmaMembrane.getPlasmaMembraneTimeSeries().get(tick).get("R-TfEn"));
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
			String met1 = met;//.substring(0, met.length()-2);
//			metabolites in the Cell are expressed in concentration. I am using the area ratio between PM and Cell 
//			for dilution of the metabilite that is released into the cell.  I may use volume ratio? 
//			Only a fraction of the metabolite in the cell participates
//			in copasi, hence the concentration are added to the existing values.
//			Since the PM is releasing Cyto metabolites at each tick, what must be incorporated is the delta with respect to the previous tick.
//			At tick = 0, nothing is released (pastValues = presentValues)
			if (StringUtils.endsWith(met, "Cy")){
				 if (!Cell.getInstance().getSolubleCell().containsKey(met1)){Cell.getInstance().getSolubleCell().put(met1, 0.0);}
					//			 System.out.println("TICK " + met+tick + "\n " + pastTick + "\n " + presentValues.get(met) + "\n " + pastValues.get(met) + "\n" + endosome
					//			 );
					double delta =  presentValues.get(met) - pastValues.get(met);
					double metValue = Cell.getInstance().getSolubleCell().get(met1)
										+ delta * PlasmaMembrane.getInstance().getPlasmaMembraneArea()/Cell.getInstance().getCellArea();
								Cell.getInstance().getSolubleCell().put(met1, metValue);
			}
			
			else if (StringUtils.endsWith(met, "Pm") || StringUtils.endsWith(met, "En") && ModelProperties.getInstance().getSolubleMet().contains(met1)){
				double metValue = presentValues.get(met)* PlasmaMembrane.getInstance().getPlasmaMembraneVolume();
				plasmaMembrane.getSolubleRecycle().put(met1, metValue);
			}
			else if (StringUtils.endsWith(met, "Pm") || StringUtils.endsWith(met, "En") && ModelProperties.getInstance().getMembraneMet().contains(met1)) {
				double metValue = presentValues.get(met)* PlasmaMembrane.getInstance().getPlasmaMembraneArea();
				plasmaMembrane.getMembraneRecycle().put(met1, metValue);
//							 System.out.println("TICK " + met+tick + "\n " + pastTick + "\n " + presentValues.get(met) + "\n " + pastValues.get(met) + "\n" + 
//							 plasmaMembrane.getMembraneRecycle());
			}
		}
		
	}
	
	public static void callReceptorDynamics(PlasmaMembrane plasmaMembrane) {
// Membrane and soluble metabolites are transformed from the area an volume units to mM.
// From my calculations (see Calculos), dividing these units by the area or the volume of the endosome, transform the 
//the values in mM.  Back from copasi, I recalculate the values to area and volume
//
		PlasmaMembraneCopasi receptorDynamics = PlasmaMembraneCopasi.getInstance();
//        int i, iMax = (int)model.getCompartments().size();
//        System.out.println("Number of Compartments: " + (new Integer(iMax)).toString());
//        System.out.println("Compartments: ");
//        for (i = 0;i < iMax;++i)
//        {
//            CCompartment compartment = model.getCompartment(i);
//            assert compartment != null;
//            model.getCompartment(i).setInitialValue(1.2e9);
//            System.out.println("compartimiento volumen \t" + compartment.getObjectName() + model.getCompartment(i).getInitialValue());
//        }
		
		int iMax = (int) receptorDynamics.getModel().getCompartments().size();
		for (int i = 0;i < iMax;++i)
        {
		if (receptorDynamics.getModel().getCompartment(i).getObjectName().equals("membrane"))
			receptorDynamics.getModel().getCompartment(i).setInitialValue(plasmaMembrane.getPlasmaMembraneArea());
//      
		else if (receptorDynamics.getModel().getCompartment(i).getObjectName().equals("soluble"))
			receptorDynamics.getModel().getCompartment(i).setInitialValue(plasmaMembrane.getPlasmaMembraneVolume());
		else 
			receptorDynamics.getModel().getCompartment(i).setInitialValue(1);
		System.out.println("compartimiento volumen \t" + receptorDynamics.getModel().getCompartment(i).getObjectName() + receptorDynamics.getModel().getCompartment(i).getInitialValue());
        }

		Set<String> metabolites = receptorDynamics.getMetabolites();
		HashMap<String, Double> localM = new HashMap<String, Double>();
//		System.out.println("PM MEMBRENE RECYCLE " + plasmaMembrane.getMembraneRecycle());
		
		for (String met : metabolites) {
//			System.out.println("metabolito que no anda" + met);
			String met1 = met; //.substring(0, met.length()-2);COPASI uses metabolite names  with the substring incorporated

//			if (met.endsWith("Pm") && plasmaMembrane.getMembraneRecycle().containsKey(met1)) {
//				double metValue = plasmaMembrane.getMembraneRecycle().get(met1)/PlasmaMembrane.getInstance().getPlasmaMembraneArea();
//				receptorDynamics.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
//				localM.put(met, metValue);
//			} else if (met.endsWith("Pm") && plasmaMembrane.getSolubleRecycle().containsKey(met1)) {
//				double metValue = Math.abs(plasmaMembrane.getSolubleRecycle().get(met1))/PlasmaMembrane.getInstance().getPlasmaMembraneVolume();
//				receptorDynamics.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
//				localM.put(met, metValue);
//			} else 
			if (met.endsWith("En") && plasmaMembrane.getMembraneRecycle().containsKey(met1)) {
				double metValue = plasmaMembrane.getMembraneRecycle().get(met1)/PlasmaMembrane.getInstance().getPlasmaMembraneArea();
				receptorDynamics.setInitialConcentration(met, sigFigs(metValue,6));
				localM.put(met, metValue);
			} else if (met.endsWith("En") && plasmaMembrane.getSolubleRecycle().containsKey(met1)) {
				double metValue = plasmaMembrane.getSolubleRecycle().get(met1)/PlasmaMembrane.getInstance().getPlasmaMembraneVolume();
				receptorDynamics.setInitialConcentration(met, sigFigs(metValue,6));
				localM.put(met, metValue);				
			} else if (met.endsWith("Cy") && Cell.getInstance().getSolubleCell().containsKey(met1)) {
				double metValue = Cell.getInstance().getSolubleCell().get(met1);
				double metLeft = metValue*(Cell.getInstance().getCellVolume() - PlasmaMembrane.getInstance().getPlasmaMembraneVolume())/(Cell.getInstance().getCellVolume());
				Cell.getInstance().getSolubleCell().put(met1, metLeft);
				receptorDynamics.setInitialConcentration(met, sigFigs(metValue,6));
				localM.put(met, metValue);
//			} else if (met.equals("area")) {
//				double metValue = plasmaMembrane.getPlasmaMembraneArea();
////				System.out.println(Cell.area + "volume cell "+Cell.volume);
//				receptorDynamics.setInitialConcentration(met, sigFigs(metValue,6));
//				localM.put(met, sigFigs(metValue,6));
//			} else if (met.equals("volume")) {
//				double metValue = plasmaMembrane.getPlasmaMembraneVolume();
////				System.out.println(Cell.area + "volume cell "+Cell.volume);
//				receptorDynamics.setInitialConcentration(met, sigFigs(metValue,6));
//				localM.put(met, sigFigs(metValue,6));
			} else {
				receptorDynamics.setInitialConcentration(met, 0.0);
				localM.put(met, 0.0);
			}
		}
//		For PM, no exchange of proton with the cytoplasm.  Arbitrarily protonCy = protonPm
		receptorDynamics.setInitialConcentration("protonCy", 3.98e-5);
		localM.put("protonCy", 3.98e-5);

	//	if (localM.get("protonEn")==null||localM.get("protonEn") < 1e-05){
			receptorDynamics.setInitialConcentration("protonEn", 3.98e-5);
			localM.put("protonEn", 3.98e-05);
	//	}
		
//			System.out.println(plasmaMembrane.getMembraneRecycle().get("pepMHCIEn")+" METABOLITES IN PM"+ localM);

	
//System.out.println("LOCAL MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM " + localM);

		receptorDynamics.runTimeCourse();
		

		CTimeSeries timeSeries = receptorDynamics.getTrajectoryTask().getTimeSeries();
//		System.out.println("time series " + timeSeries);
		int stepNro = (int) timeSeries.getRecordedSteps();
		int metNro = metabolites.size();
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		System.out.println("steps "+stepNro+"metNro " + metNro  + " tick " + tick);
		for (int time = 0; time < stepNro; time = time + 1){
			HashMap<String, Double> value = new HashMap<String, Double>();
			for (int met = 1; met < metNro +1; met = met +1){
				value.put(timeSeries.getTitle(met), timeSeries.getConcentrationData(time, met));
			}
//			System.out.println("tick "+tick+"time " + time  + " time series " + value);
			plasmaMembrane.getPlasmaMembraneTimeSeries().put((int) (tick+time*Cell.timeScale/0.03),value);

		}
		
//		delta = delta + (localM.get("pepMHCIEn")*plasmaMembrane.getPlasmaMembraneArea()+localM.get("pepEn")*plasmaMembrane.getPlasmaMembraneVolume())-
//				(timeSeries.getConcentrationData(1, 10)*plasmaMembrane.getPlasmaMembraneArea()+timeSeries.getConcentrationData(1, 4)*plasmaMembrane.getPlasmaMembraneVolume());
			
//		delta = delta + (localM.get("cMHCIEn")+localM.get("oMHCIEn")+localM.get("pepMHCIEn"))-
//				(timeSeries.getConcentrationData(1, 9)+timeSeries.getConcentrationData(1, 10)+timeSeries.getConcentrationData(1, 11));
//			System.out.println("DELTA PM "+ delta);
			

		}
	public static double sigFigs(double n, int sig) {
//		if (Math.abs(n) < 1E-20) return 0d;
//		else 
//		{
		Double mult = Math.pow(10, sig - Math.floor(Math.log(n) / Math.log(10) + 1));
		if (mult.isNaN()) return 0.0;
		else return Math.round(n * mult) / mult;
//	    }
	}
	
}

