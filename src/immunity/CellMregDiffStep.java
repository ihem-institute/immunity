package immunity;

import java.util.HashMap;
import java.util.Set;

public class CellMregDiffStep {
	
	public static void mregDiff(Cell cell) {

		MregDiff mregDiff = MregDiff.getInstance();
		Set<String> metabolites = mregDiff.getMetabolites();
		for (String met : metabolites) {
		if (cell.getSolubleCell().containsKey(met)) {
			double metValue = Math.round
						(cell.getSolubleCell().get(met) * 1000d) / 1000d;
		
			mregDiff.setInitialConcentration(met, metValue);
		}
//		System.out.println("COPASI INITIAL PM pLANCL2  " +metValue);
		}
	
		mregDiff.runTimeCourse();

		for (String met : metabolites){
			double metValue = mregDiff.getConcentration(met);
			cell.getSolubleCell().put(met, metValue);
			System.out.println("COPASI CELL MREGDIFF " + met +" "+metValue);
		}

	}
}
	
