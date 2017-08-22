package immunity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

import org.COPASI.CTimeSeries;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class EndosomeRabConversionStep {
	
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	
	public static void rabTimeSeriesLoad(Endosome endosome){
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		if (endosome.getRabTimeSeries().isEmpty()){			
			callRabConversion(endosome);
			timeSeriesLoadintoEndosome(endosome);
			System.out.println("RabConversion first time");
			return;
		} 
		if (tick >= Collections.max(endosome.getRabTimeSeries().keySet())) {
//			System.out.println("COLLECTION" + tick + " " + endosome.getRabTimeSeries().keySet());
			timeSeriesLoadintoEndosome(endosome);
			endosome.getRabTimeSeries().clear();
			callRabConversion(endosome);
			System.out.println("RabConversion called after 50 time series");
//			System.out.println("COLLECTION" + tick + " " + endosome.getRabTimeSeries().keySet());
			return;
			}
		if (!endosome.rabTimeSeries.containsKey(tick)) {
//			System.out.println("Return without UPDATED");
			return;
		}else {
			timeSeriesLoadintoEndosome(endosome);
			System.out.println("Rabs UPDATED FROM TIME SERIES");
			return;

		}
	}
	
	
	
	private static void timeSeriesLoadintoEndosome(Endosome endosome) {
//		values in rabTimeSeries are in mM.  Transform back in area and volume units multiplying
//		by area the membrane metabolites and by volume the soluble metabolites
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		HashMap<String, Double> presentValues = new HashMap<String, Double>(endosome.rabTimeSeries.get(tick));	

		for (String met : presentValues.keySet()) {
			if (met.endsWith("m")) {
				String Rab = met.substring(0, 4);
				double metValue = presentValues.get(met)*endosome.area;
				endosome.rabContent.put(Rab, metValue);
				// System.out.println("COPASI FINAL " + met +
				// rabContent.get(Rab));
			}
/*			if (met.endsWith("c")) {
				String Rab = met.substring(0, 4);
				double metValue = presentValues.get(met)* endosome.area * 3 * 1E-8;
				if (Cell.getInstance().getRabCell().containsKey(Rab))
				{
				metValue = metValue
							+Cell.getInstance().getRabCell().get(Rab);	
				}
				Cell.getInstance().getRabCell().put(Rab, metValue);

				// System.out.println("COPASI FINAL " + met
				// + Cell.getInstance().getRabCell().get(Rab));
			}
*/
		}

		System.out.println("COPASI FINAL membrane " + endosome.rabContent + " soluble "
				+ Cell.getInstance().getRabCell());
		
	System.out.println("Rabs UPDATED");
//	for (String met :presentValues.keySet()){
//	System.out.println(met+ " "+presentValues.get(met));
//		}
		
		
	}

	public static void callRabConversion(Endosome endosome) {

		RabConversion rabConversion = RabConversion.getInstance();

		Set<String> metabolites = RabConversion.getInstance().getMetabolites();
// New strategy for Rab conversion.  The Rabs in endosomes are passed divided by the area 
//(about 1 mM as calculated elsewhere). The Rabs in cytosol are in mM and are not transformed
		for (String met : metabolites) {
			if (met.endsWith("m")) {
				String Rab = met.substring(0, 4);
				if (endosome.rabContent.containsKey(Rab)) {
					double metValue = endosome.rabContent.get(Rab) / endosome.area;
					rabConversion.setInitialConcentration(met, Math.round(metValue*1000d)/1000d);
//					System.out.println("COPASI INITIAL MEMBRANE " + met + " " + metValue);

				} else {
					rabConversion.setInitialConcentration(met, 0.0);
					// System.out.println("COPASI INITIAL " + met + 0.0);
				}
			}

			if (met.endsWith("c")) {
				String Rab = met.substring(0, 4);
				if (Cell.getInstance().getRabCell().containsKey(Rab)) {
					double metValue = Cell.getInstance().getRabCell().get(Rab);
					System.out.println("COPASI INITIAL CYTOSOL " + Rab + " " + metValue +"  "+Cell.getInstance().getRabCell().get(Rab) );
					rabConversion.setInitialConcentration(met, Math.round(metValue*1000d)/1000d);
//					System.out.println("COPASI INITIAL CYTOSOL " + met + " " + metValue);
					// + Cell.getInstance().rabCell.get(Rab));
				} else {
					rabConversion.setInitialConcentration(met, 0.0);
					// System.out.println("COPASI INITIAL " + met + 0.0);

				}
			}
			if (met.equals("zero"))
				rabConversion.setInitialConcentration(met, 0);
			if (met.equals("Rab0"))
				rabConversion.setInitialConcentration(met, Rab0(endosome));
		}

		System.out.println("COPASI INITIAL  membrane " + endosome.rabContent
				+ " soluble " + Cell.getInstance().getRabCell());
		
		rabConversion.runTimeCourse();
				
		CTimeSeries timeSeries = rabConversion.getTrajectoryTask().getTimeSeries();
		int stepNro = (int) timeSeries.getRecordedSteps();
		int metNro = metabolites.size();
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		for (int time = 0; time < stepNro; time = time + 1){
			HashMap<String, Double> value = new HashMap<String, Double>();
			for (int met = 1; met < metNro +1; met = met +1){
				value.put(timeSeries.getTitle(met), timeSeries.getConcentrationData(time, met));
			}
			endosome.getRabTimeSeries().put((int) (tick+time*Cell.timeScale/0.015),value);
		}
		
//		System.out.println("RAB time series "+ tick +" " +endosome.getRabTimeSeries());
		
//		for (String met : metabolites) {
//			if (met.endsWith("m")) {
//				String Rab = met.substring(0, 4);
//				double metValue = rabConversion.getConcentration(met)*endosome.area;
//				endosome.rabContent.put(Rab, metValue);
//				// System.out.println("COPASI FINAL " + met +
//				// rabContent.get(Rab));
//			}
//			if (met.endsWith("c")) {
//				String Rab = met.substring(0, 4);
//				double metValue = rabConversion.getConcentration(met);
//				Cell.getInstance().getRabCell().put(Rab, metValue);
//
//				// System.out.println("COPASI FINAL " + met
//				// + Cell.getInstance().getRabCell().get(Rab));
//			}
//
//		}

		System.out.println("COPASI FINAL membrane " + endosome.rabContent + " soluble "
				+ Cell.getInstance().getRabCell());

	}

	private static double Rab0(Endosome endosome) {
		double sum = 0;
		for (String rab : endosome.rabContent.keySet()) {
			sum = sum + endosome.rabContent.get(rab);
		}
		double Rab0 = (endosome.area - sum)/endosome.area;
//		if (Rab0 > 0) Rab0=0;
		return Rab0;
	}

	
}
