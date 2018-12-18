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

public class CellCopasiStep {
	
	public static void antPresTimeSeriesLoad(Cell cell){
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		if (cell.getCellTimeSeries().isEmpty()){			
			callCellCopasi(cell);
			timeSeriesLoadintoCell(cell);
			return;
		} 
		if (tick >= Collections.max(cell.getCellTimeSeries().keySet())) {
			timeSeriesLoadintoCell(cell);
			cell.getCellTimeSeries().clear();
			callCellCopasi(cell);
			return;
			}
		if (!cell.getCellTimeSeries().containsKey(tick)) {
//			System.out.println("Return without UPDATED");
			return;
		}else {
			timeSeriesLoadintoCell(cell);
			return;

		}
	}
	public static void timeSeriesLoadintoCell(Cell cell){
//		values in receptorTimeSeries are in mM.  Transform back in area and volume units multiplying
//		by area the membrane metabolites and by volume the soluble metabolites
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		HashMap<String, Double> presentValues = new HashMap<String, Double>(cell.getCellTimeSeries().get(tick));

		for (String met :presentValues.keySet()){
//			Organelle (endosomes/Golgi) metabolites are not considered. The reaction should be called from endosomes.
			String met1 = met.substring(0, met.length()-2);
//			metabolites in the Cell are expressed in concentration. Only a fraction of the metabolite in the cell participates
//			in copasi, hence the concentration are added to the existing values.
			if (StringUtils.endsWith(met, "Cy")){
				double metValue = Cell.getInstance().getSolubleCell().get(met1);
				Cell.getInstance().getSolubleCell().put(met1, metValue);
			}
			
			else if (StringUtils.endsWith(met, "Pm") && PlasmaMembrane.getInstance().getSolubleRecycle().containsKey(met1)) {
				double metValue = presentValues.get(met)* PlasmaMembrane.getInstance().volume;
				PlasmaMembrane.getInstance().getSolubleRecycle().put(met1, metValue);
			}
			else if (StringUtils.endsWith(met, "Pm") && PlasmaMembrane.getInstance().getMembraneRecycle().containsKey(met1)) {
				double metValue = presentValues.get(met)* PlasmaMembrane.getInstance().area;
				PlasmaMembrane.getInstance().getMembraneRecycle().put(met1, metValue);
			}
		}
		
	}
	
	public static void callCellCopasi(Cell cell) {
// Membrane and soluble metabolites are transformed from the area an volume units to mM.
// From my calculations (see Calculos), dividing these units by the area or the volume of the endosome, transform the 
//the values in mM.  Back from copasi, I recalculate the values to area and volume
//
		CellCopasi cellCopasi = CellCopasi.getInstance();

		Set<String> metabolites = cellCopasi.getInstance()
				.getMetabolites();
		HashMap<String, Double> localM = new HashMap<String, Double>();
		
		for (String met : metabolites) {
			String met1 = met.substring(0, met.length()-2);

			 if (StringUtils.endsWith(met, "Pm") && PlasmaMembrane.getInstance().getMembraneRecycle().containsKey(met1)) {
				double metValue = PlasmaMembrane.getInstance().getMembraneRecycle().get(met1)/PlasmaMembrane.getInstance().area;
				cellCopasi.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
				localM.put(met, metValue);
			} else if (StringUtils.endsWith(met, "Pm") && PlasmaMembrane.getInstance().getSolubleRecycle().containsKey(met1)) {
				double metValue = Math.abs(PlasmaMembrane.getInstance().getSolubleRecycle().get(met1))/PlasmaMembrane.getInstance().volume;
				cellCopasi.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
				localM.put(met, metValue);
			} else if (StringUtils.endsWith(met, "Cy") && cell.getSolubleCell().containsKey(met1)) {
				double metValue = cell.getSolubleCell().get(met1);
				cellCopasi.setInitialConcentration(met, Math.round(metValue*1E9d)/1E9d);
				localM.put(met, metValue);
			} else {
				cellCopasi.setInitialConcentration(met, 0.0);
				localM.put(met, 0.0);
			}
		}
		cellCopasi.setInitialConcentration("protonCy", 1e-04);
		localM.put("protonCy", 1e-04);

		if (localM.get("proton")==null||localM.get("proton") < 1e-05){
			cellCopasi.setInitialConcentration("proton", 1e-04);
			localM.put("proton", 1e-04);
		}
		
		
	
System.out.println("LOCAL MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM " + localM);

		cellCopasi.runTimeCourse();
		

		CTimeSeries timeSeries = cellCopasi.getTrajectoryTask().getTimeSeries();
		int stepNro = (int) timeSeries.getRecordedSteps();
		int metNro = metabolites.size();
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		for (int time = 0; time < stepNro; time = time + 1){
			HashMap<String, Double> value = new HashMap<String, Double>();
			for (int met = 1; met < metNro +1; met = met +1){
				value.put(timeSeries.getTitle(met), timeSeries.getConcentrationData(time, met));
				cell.getCellTimeSeries().put((int) (tick+time*Cell.timeScale/0.03),value);
			}
		}
		
			

			

		}
	
}

