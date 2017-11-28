package immunity;

import java.util.HashMap;
import java.util.Set;

public class CellMetabolismStep {
//	call a Copasi for spices in the cytosol
	public static void cellMetabolim(Cell cell) {

		CellMetabolism cellMetabolism = CellMetabolism.getInstance();
		Set<String> metabolites = cellMetabolism.getMetabolites();
		for (String met : metabolites) {
		if (cell.getSolubleCell().containsKey(met)) {
			double metValue = Math.round
						(cell.getSolubleCell().get(met) * 1000d) / 1000d;
		
			cellMetabolism.setInitialConcentration(met, metValue);
		}
		}
	
		cellMetabolism.runTimeCourse();

		for (String met : metabolites){
			double metValue = cellMetabolism.getConcentration(met);
			cell.getSolubleCell().put(met, metValue);
			System.out.println("COPASI CELL METABOLISM " + met +" "+metValue);
		}

	}
}
	
