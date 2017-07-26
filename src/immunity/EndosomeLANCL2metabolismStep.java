package immunity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.COPASI.CTimeSeries;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class EndosomeLANCL2metabolismStep {

	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	
	public static void LANCL2PresTimeSeriesLoad(Endosome endosome){
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		if (endosome.getLANCL2TimeSeries().isEmpty()){			
			LANCL2metabolism(endosome);
			timeSeriesLoadintoEndosome(endosome);
			System.out.println("LANCL2 first time");
			return;
		} 
		if (tick > Collections.max(endosome.getLANCL2TimeSeries().keySet())) {
			System.out.println("COLLECTION" + tick + " " + endosome.getAntigenTimeSeries().keySet());
			endosome.getLANCL2TimeSeries().clear();
			LANCL2metabolism(endosome);
			timeSeriesLoadintoEndosome(endosome);
			System.out.println("LANCL2 called after 50 time series");
			System.out.println("COLLECTION" + tick + " " + endosome.getAntigenTimeSeries().keySet());
			return;
			}
		if (!endosome.LANCL2TimeSeries.containsKey(tick)) {
//			System.out.println("Return without UPDATED");
			return;
		}else {
			timeSeriesLoadintoEndosome(endosome);
			System.out.println("LANCL2 UPDATED FROM TIME SERIES");
			return;

		}
	}
	public static void timeSeriesLoadintoEndosome(Endosome endosome){
//		values in antigenTimeSeries are in mM.  Transform back in area and volume units multiplying
//		by area the membrane metabolites and by volume the soluble metabolites
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		HashMap<String, Double> presentValues = new HashMap<String, Double>(endosome.LANCL2TimeSeries.get(tick));
		

		double pLANCL2 = presentValues.get("pLANCL2")* endosome.area;
		endosome.membraneContent.put("pLANCL2", pLANCL2);
		double LANCL2 = presentValues.get("LANCL2")* endosome.area;
		endosome.membraneContent.put("LANCL2", LANCL2);
	
	System.out.println("LANCL2 UPDATED");
//	for (String met :presentValues.keySet()){
//	System.out.println(met+ " "+presentValues.get(met));
//		}
		
	}

	
	
	public static void LANCL2metabolism(Endosome endosome) {
// Membrane and soluble metabolites are transformed from the area an volume units to mM to send to COPASI
// From my calculations (see Calculos), dividing these units by the area or the volume of the endosome, transform the 
//the values in mM.  Time series is in mM. From timeSeries I recalculate the values to area and volume
		space = endosome.getSpace();
		grid = endosome.getGrid();
		LANCL2metabolism lANCL2metabolism = LANCL2metabolism.getInstance();
		Set<String> metabolites = lANCL2metabolism.getMetabolites();
		for (String met : metabolites){
			lANCL2metabolism.setInitialConcentration(met, 0);
		}
		double metValue = 0.0;
		if (endosome.membraneContent.containsKey("pLANCL2")) {
			metValue = endosome.membraneContent.get("pLANCL2")/endosome.area;
		}
		lANCL2metabolism.setInitialConcentration("pLANCL2", metValue);
		System.out.println("COPASI INITIAL endo pLANCL2  " +metValue);
		if (endosome.membraneContent.containsKey("LANCL2")) {
			metValue = endosome.membraneContent.get("LANCL2")/endosome.area;
		}
		lANCL2metabolism.setInitialConcentration("LANCL2", metValue);
		System.out.println("COPASI INITIAL endo LANCL2  " +metValue);
		if (Cell.getInstance().getSolubleCell().containsKey("ABA")) {
			metValue = Cell.getInstance().getSolubleCell().get("ABA");
		}
			lANCL2metabolism.setInitialConcentration("ABA", metValue);
			System.out.println("COPASI INITIAL  Cell ABA " + metValue);
//		

//		if (cyto.getCytoContent().containsKey("LANCL2")) {
//			metValue = cyto.getCytoContent().get("LANCL2");
////				metValue = Math.round
////								(cyto.getCytoContent().get("LANCL2") * 1000) / 1000;
//			}
//		lANCL2metabolism.setInitialConcentration("LANCL2", metValue);
//		System.out.println("COPASI INITIAL  CYTO LANCL2 " + metValue);
		metValue = 0.0;


		lANCL2metabolism.runTimeCourse();
		
		CTimeSeries timeSeries =   lANCL2metabolism.getTrajectoryTask().getTimeSeries();
		int stepNro = (int) timeSeries.getRecordedSteps();
		int metNro = metabolites.size();
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		for (int time = 0; time < stepNro; time = time + 1){
			HashMap<String, Double> value = new HashMap<String, Double>();
			for (int met = 1; met < metNro +1; met = met +1){
				value.put(timeSeries.getTitle(met), timeSeries.getConcentrationData(time, met));
				endosome.getLANCL2TimeSeries().put(tick+time*67,value);
			}
		}
		
		System.out.println("LANCL2 time series "+ tick +" " +endosome.getAntigenTimeSeries().keySet());		
//				
		if (Cell.getInstance().getSolubleCell().containsKey("LANCL2cyto")){
		metValue = lANCL2metabolism.getConcentration("LANCL2cyto")*endosome.area/1E08
				+Cell.getInstance().getSolubleCell().get("LANCL2cyto");	
		} else {
			metValue =lANCL2metabolism.getConcentration("LANCL2cyto")*endosome.area/1E08;
		}
		Cell.getInstance().getSolubleCell().put("LANCL2", metValue);
		System.out.println("COPASI Cell FINAL LANCL2cyto " + metValue);
		
		Cytosol cyto = null;
		metValue = 0.0;
		GridPoint pt = grid.getLocation(endosome);
//		System.out.println(pt.toString());
		Iterable<Object> obj = grid.getObjectsAt(pt.getX(), pt.getY());
		for (Object xx : obj){
		 if (xx instanceof Cytosol){
			cyto = (Cytosol) xx;
////			System.out.println(cyto.getCytoContent());
		 }
		}
		
		if (Cell.getInstance().getSolubleCell().containsKey("LANCL2cyto")){
		metValue = lANCL2metabolism.getConcentration("LANCL2cyto")*endosome.area/1E08
				+Cell.getInstance().getSolubleCell().get("LANCL2cyto");	
		} else {
			metValue =lANCL2metabolism.getConcentration("LANCL2cyto")*endosome.area/1E08;
		}
		
		if (cyto.getCytoContent().containsKey("LANCL2cyto")){
		metValue = lANCL2metabolism.getConcentration("LANCL2cyto")*endosome.area
				+ cyto.getCytoContent().get("LANCL2cyto");}
		else {
		metValue = lANCL2metabolism.getConcentration("LANCL2cyto")*endosome.area;
			}	
		cyto.getCytoContent().put("LANCL2cyto", metValue);
	}		

}
	
