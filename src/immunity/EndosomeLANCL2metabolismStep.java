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
	
	public static void LANCL2TimeSeriesLoad(Endosome endosome){
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();

		if (endosome.getLANCL2TimeSeries().isEmpty()){			
			callLANCL2metabolism(endosome);
			timeSeriesLoadintoEndosome(endosome);
			System.out.println("LANCL2 first time");
			return;
		} 
		if (tick >= Collections.max(endosome.getLANCL2TimeSeries().keySet())) {
//			System.out.println("COLLECTION" + tick + " " + endosome.getLANCL2TimeSeries().keySet());
			timeSeriesLoadintoEndosome(endosome);
			endosome.getLANCL2TimeSeries().clear();
			callLANCL2metabolism(endosome);
			System.out.println("LANCL2 called after 50 time series");
//			System.out.println("COLLECTION" + tick + " " + endosome.getLANCL2TimeSeries().keySet());
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
//		values in LANCL2TimeSeries are in mM.  Transform back in area and volume units multiplying
//		by area the membrane metabolites and by volume the soluble metabolites
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		HashMap<String, Double> presentValues = new HashMap<String, Double>(endosome.LANCL2TimeSeries.get(tick));
		

		double pLANCL2 = presentValues.get("pLANCL2")* endosome.area;
		endosome.membraneContent.put("pLANCL2", pLANCL2);
		double LANCL2 = presentValues.get("LANCL2")* endosome.area;
		endosome.membraneContent.put("LANCL2", LANCL2);
		double LANCL2cyto = 0.0;
		if (Cell.getInstance().getSolubleCell().containsKey("LANCL2cyto")){
		LANCL2cyto = presentValues.get("LANCL2cyto")* endosome.area * 3 * 1E-8 * Math.pow(Cell.orgScale, 3)
				+Cell.getInstance().getSolubleCell().get("LANCL2cyto");	
		} else {
		LANCL2cyto =presentValues.get("LANCL2cyto")*endosome.area* 3 * 1E-8 * Math.pow(Cell.orgScale, 3);
		}
		Cell.getInstance().getSolubleCell().put("LANCL2cyto", LANCL2cyto);
		
	System.out.println("LANCL2 UPDATED");
//	for (String met :presentValues.keySet()){
//	System.out.println(met+ " "+presentValues.get(met));
//		}
		
	}

	
	
	public static void callLANCL2metabolism(Endosome endosome) {
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
		
		lANCL2metabolism.setInitialConcentration("pLANCL2", metValue);
//		System.out.println("COPASI INITIAL endo pLANCL2  " +metValue);
		}
		if (endosome.membraneContent.containsKey("LANCL2")) {
			metValue = endosome.membraneContent.get("LANCL2")/endosome.area;
		
		lANCL2metabolism.setInitialConcentration("LANCL2", metValue);
//		System.out.println("COPASI INITIAL endo LANCL2  " +metValue);
		}
		if (Cell.getInstance().getSolubleCell().containsKey("ABA")) {
			metValue = Cell.getInstance().getSolubleCell().get("ABA");
		
			lANCL2metabolism.setInitialConcentration("ABA", metValue);
//			System.out.println("COPASI INITIAL  Cell ABA " + metValue);
		}

//		if (cyto.getCytoContent().containsKey("LANCL2")) {
//			metValue = cyto.getCytoContent().get("LANCL2");
////				metValue = Math.round
////								(cyto.getCytoContent().get("LANCL2") * 1000d) / 1000d;
//			}
//		lANCL2metabolism.setInitialConcentration("LANCL2", metValue);
//		System.out.println("COPASI INITIAL  CYTO LANCL2 " + metValue);


		lANCL2metabolism.runTimeCourse();
		
		CTimeSeries timeSeries =   lANCL2metabolism.getTrajectoryTask().getTimeSeries();
		int stepNro = (int) timeSeries.getRecordedSteps();
		int metNro = metabolites.size();
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		for (int time = 0; time < stepNro; time = time + 1){
			HashMap<String, Double> value = new HashMap<String, Double>();
			for (int met = 1; met < metNro +1; met = met +1){
				value.put(timeSeries.getTitle(met), timeSeries.getConcentrationData(time, met));
				endosome.getLANCL2TimeSeries().put((int) (tick+time*Cell.timeScale/0.03),value);
			}
		}
		
//		System.out.println("LANCL2 time series "+ tick +" " +endosome.getLANCL2TimeSeries());		
//		RELEASE TO THE CYTOSOL IS NOW OCCURRING DURING THE UPDATE FROM THE SERIES
//		A protein released from a membrane increases the concentration in the cytosol in:
//		copasi concentration in mM * area of the organelle * 3*10^(-8) (mM).  See file Calculos
//		So, the release of LANCL2 goes to a pool of cytosol LANCL2, that can be incorporated back
//		in the plasma membrane but not in endosomes.  In endosomes only is possible activation, 
//		inactivation and release
//		if (Cell.getInstance().getSolubleCell().containsKey("LANCL2cyto")){
//		metValue = lANCL2metabolism.getConcentration("LANCL2cyto")* endosome.area * 3 * 1E-8
//				+Cell.getInstance().getSolubleCell().get("LANCL2cyto");	
//		} else {
//			metValue =lANCL2metabolism.getConcentration("LANCL2cyto")*endosome.area* 3 * 1E-8;
//		}
//		Cell.getInstance().getSolubleCell().put("LANCL2cyto", metValue);
//		System.out.println("COPASI Cell FINAL LANCL2cyto " + metValue);
		
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

// I will simulate the release to a unit of cytosol. At present, PM incorporates cytoLANCL2
//		from a cellular pool, no the near by cytosol units.  So the release on local subunits is not
//		used for PM and at present is only stetic.  I may use it for transport to the nucleus?????
// in this case, the volume of the unit of cytosol is 15*15*15 nm  and the calculation is:
//	copasi values (mM) * area * 10^-3.  See file Cálculos for details
		if (cyto.getCytoContent().containsKey("LANCL2cyto")){
		metValue = lANCL2metabolism.getConcentration("LANCL2cyto")*endosome.area * 1E-3 * Math.pow(Cell.orgScale, 3)
				+ cyto.getCytoContent().get("LANCL2cyto");}
		else {
		metValue = lANCL2metabolism.getConcentration("LANCL2cyto")*endosome.area * 1E-3 * Math.pow(Cell.orgScale, 3);
			}	
		cyto.getCytoContent().put("LANCL2cyto", metValue);
	}		

}
	
