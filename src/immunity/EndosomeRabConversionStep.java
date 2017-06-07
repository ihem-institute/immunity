package immunity;

import java.util.HashMap;
import java.util.Set;

public class EndosomeRabConversionStep {
	
	public static void rabConversion(Endosome endosome) {

		RabConversion rabConversion = RabConversion.getInstance();

		Set<String> metabolites = RabConversion.getInstance().getMetabolites();

		for (String met : metabolites) {
			if (met.endsWith("m")) {
				String Rab = met.substring(0, 4);
				if (endosome.rabContent.containsKey(Rab)) {
					double metValue = Math.round
							(endosome.rabContent.get(Rab) * 1000) / 1000;
					rabConversion.setInitialConcentration(met, metValue);

				} else {
					rabConversion.setInitialConcentration(met, 0.0);
					// System.out.println("COPASI INITIAL " + met + 0.0);
				}
			}

			if (met.endsWith("c")) {
				String Rab = met.substring(0, 4);
				if (Cell.getInstance().getRabCell().containsKey(Rab)) {
					double metValue = Math.round(Cell.getInstance()
							.getRabCell().get(Rab) * 1000) / 1000;
					rabConversion.setInitialConcentration(met, metValue);
					// System.out.println("COPASI INITIAL " + met
					// + Cell.getInstance().rabCell.get(Rab));
				} else {
					rabConversion.setInitialConcentration(met, 0.0);
					// System.out.println("COPASI INITIAL " + met + 0.0);

				}
			}
			if (met.equals("area"))
				rabConversion.setInitialConcentration(met, endosome.area);
			if (met.equals("Rab0"))
				rabConversion.setInitialConcentration(met, Rab0(endosome));
		}

		System.out.println("COPASI INITIAL  membrane " + endosome.rabContent
				+ " soluble " + Cell.getInstance().getRabCell());
		rabConversion.runTimeCourse();
		for (String met : metabolites) {
			if (met.endsWith("m")) {
				String Rab = met.substring(0, 4);
				double metValue = rabConversion.getConcentration(met);
				endosome.rabContent.put(Rab, metValue);
				// System.out.println("COPASI FINAL " + met +
				// rabContent.get(Rab));
			}
			if (met.endsWith("c")) {
				String Rab = met.substring(0, 4);
				double metValue = rabConversion.getConcentration(met);
				Cell.getInstance().getRabCell().put(Rab, metValue);

				// System.out.println("COPASI FINAL " + met
				// + Cell.getInstance().getRabCell().get(Rab));
			}

		}

		System.out.println("COPASI FINAL membrane " + endosome.rabContent + " soluble "
				+ Cell.getInstance().getRabCell());

	}

	private static double Rab0(Endosome endosome) {
		double sum = 0;
		for (String rab : endosome.rabContent.keySet()) {
			sum = sum + endosome.rabContent.get(rab);
		}
		double Rab0 = endosome.area - sum;
		return Rab0;
	}

	
}
