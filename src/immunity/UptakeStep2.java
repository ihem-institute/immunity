package immunity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class UptakeStep2 {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	private static Object membreneMet;
	private static HashMap<String, Double> rabContent;
	public static void uptake(Cell cell) {
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		if (tick < 100) return;
		space = cell.getSpace();
		grid = cell.getGrid();
		HashMap<String, String> rabCode = new HashMap<String, String>();
		rabCode.put("RabA", "kind1");
		rabCode.put("RabB", "kind2");
		rabCode.put("RabC", "kind3");
		rabCode.put("RabD", "kind4");
		rabCode.put("RabE", "kind5");
		double membraneFlux = CellProperties.getInstance().cellK.get("membraneFlux"); // if on then a cistern is formed from zero for membrane flux model
		if (membraneFlux == 1d){
			double maturationTrigger = CellProperties.getInstance().cellK.get("maturationTrigger");
//
//			System.out.println("QWUW PA  "+Results.getInstance().getTotalRabs());
			double totalRabA = Results.getInstance().getTotalRabs().get("RabA");
			double initialTotalRabA = Results.getInstance().getInitialTotalRabs().get("RabA");
			//System.out.println("totalRabs  "+totalRabA);
			if (totalRabA > maturationTrigger * initialTotalRabA) {// new RabA cistern only if 50% has matured
				return;
			}
			else
			{
			String selectedRab = "RabA";
			newOrganelle(cell, selectedRab, rabCode, membraneFlux);
			return;
			}
		}
		else {
		HashMap<String, Double> totalRabs = new HashMap<String, Double>(Results.getInstance().getTotalRabs());
		HashMap<String, Double> initialTotalRabs = new HashMap<String, Double>(Results.getInstance().getInitialTotalRabs());
		HashMap<String, Double> deltaRabs = new HashMap<String, Double>();  


		for (String rab : totalRabs.keySet()){
			double value = initialTotalRabs.get(rab) - totalRabs.get(rab);
			deltaRabs.put(rab, value);
		}

		double largeDelta = 0d;
		String selectedRab = "";
		for (String rab : deltaRabs.keySet()){
			if (deltaRabs.get(rab)>largeDelta) {
				selectedRab=rab;
				largeDelta=deltaRabs.get(rab);
			}	
		}
		//		If no rab was selected or the surface required is small (less than a sphere of 60 nm radius, 
		//		no uptake is required
		if (selectedRab.equals("")|| deltaRabs.get(selectedRab)<45000) return;
		if (selectedRab.equals("RabA")){ 
			newUptake(cell, selectedRab);}
		else {newOrganelle(cell, selectedRab, rabCode, membraneFlux);}
		}
	}
		
		
	private static void newUptake(Cell cell, String selectedRab) {
		space = Cell.getInstance().getSpace();
		grid = Cell.getInstance().getGrid();
		double cellLimit = 3d * Cell.orgScale;
		HashMap<String, Double> initOrgProp = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitOrgProp().get("kind1"));
		
		HashMap<String, Double> rabCell = Cell.getInstance().getRabCell();

		if (!rabCell.containsKey("RabA") || Math.random()>rabCell.get("RabA")){
			return;}
	//      cytosolic RabA is always provided by the -> RabAc reaction.  Only in a KD will go down
	//		Then no uptake if no RabA in cyto.  The uptake is proportional to the amount of RabA	
	//		Uptake generate a new RabA organelle.  The size is the radius in the csv file for RabA
	//		initial organelles.  The content is specified in concentration. One (1) is approx 1 mM.
	//		This units are converted to membrane or volume units by multiplying by the area (rabs and
	//		membrane content) or volume (soluble content).  For uptake it is controlled that there is enough
	//		membrane and there is RabA in the cell

		double maxRadius = initOrgProp.get("maxRadius");
		double minRadius = Cell.rcyl*1.1;
		double a = RandomHelper.nextDoubleFromTo(minRadius,maxRadius);				
		double c = a + a  * Math.random() * initOrgProp.get("maxAsym");

		double f = 1.6075;
		double af= Math.pow(a, f);
		double cf= Math.pow(c, f);
		double area = 4d* Math.PI*Math.pow((af*af+af*cf+af*cf)/3, 1/f);
		double volume = 4d/3d*Math.PI*a*a*c;
		initOrgProp.put("area", area);
		double value = Results.instance.getTotalRabs().get("RabA");
		value = value + area;
		Results.instance.getTotalRabs().put("RabA", value);
		initOrgProp.put("volume", volume);

		HashMap<String, Double> rabContent = new HashMap<String, Double>();
	//			UPTAKE ENDOSOME JUST RABA
		rabContent.put("RabA", area);

	// 		Soluble and membrane content of the kind1, but cMHCI and mHCI depends now on PM content
		HashMap<String, Double> membraneContent = new HashMap<String,Double>();
		Set<String> membraneMet = new HashSet<String>(CellProperties.getInstance().getMembraneMet());
		for (String mem : membraneMet){
			double valueInEn = 0d;
			double valueInPM =0d;
			double valueInTotal = 0d;

			if (PlasmaMembrane.getInstance().getMembraneRecycle().containsKey(mem))
			{
				double valuePM = PlasmaMembrane.getInstance().getMembraneRecycle().get(mem);
				valueInPM = valuePM * CellProperties.getInstance().getUptakeRate().get(mem) * area/ PlasmaMembrane.getInstance().getPlasmaMembraneArea();	

				if (valueInPM >= area) 
				{
					membraneContent.put(mem, area);
					// decrease PM content
					PlasmaMembrane.getInstance().getMembraneRecycle().put(mem, valuePM - area);
					continue;
				}
				// decrease PM content
				PlasmaMembrane.getInstance().getMembraneRecycle().put(mem, valuePM-valueInPM);
				valueInTotal = valueInPM;
			}
			if (InitialOrganelles.getInstance().getInitMembraneContent().get("kind1").containsKey(mem))
			{
				valueInEn = InitialOrganelles.getInstance().getInitMembraneContent().get("kind1").get(mem)*area;
				valueInTotal = valueInEn + valueInPM;
			}
			if (valueInTotal >= area) 	
			{
				valueInTotal= area;
			}
			membraneContent.put(mem, valueInTotal);	

		}
		HashMap<String, Double> solubleContent = new HashMap<String,Double>();
		Set<String> solubleMet = new HashSet<String>(CellProperties.getInstance().getSolubleMet());
		for (String sol : solubleMet){
			double valueInEn = 0d;
			double valueInPM =0d;
			
			if (PlasmaMembrane.getInstance().getSolubleRecycle().containsKey(sol))
			{
				double valuePM = PlasmaMembrane.getInstance().getSolubleRecycle().get(sol);
				valueInPM = valuePM * volume/ PlasmaMembrane.getInstance().getPlasmaMembraneVolume();	

				if (valueInPM >= volume) 
				{
				solubleContent.put(sol, volume);
				// decrease PM content
				PlasmaMembrane.getInstance().getSolubleRecycle().put(sol, valuePM - volume);
				continue;
				}
				// decrease PM content
				PlasmaMembrane.getInstance().getSolubleRecycle().put(sol, valuePM-valueInPM);
			}
			if (InitialOrganelles.getInstance().getInitSolubleContent().get("kind1").containsKey(sol))
			{
				valueInEn = InitialOrganelles.getInstance().getInitSolubleContent().get("kind1").get(sol)*volume;
				valueInEn = valueInEn + valueInPM;
			}
			if (valueInEn >= volume) 	valueInEn= volume;
			solubleContent.put(sol, valueInEn);	

		}
		solubleContent.put("proton", 3.98E-5*volume); //pH 7.4
		/*	
		My problem is that I do not know the rate of uptake that depends on how much Kind1(Rab5) organelles are 
		switched to Kind4(Rab7).  I guess is that the rate will have to be relative.  1 for open MHCI and 0.15 for closed.
		*/					
	
		Context<Object> context = ContextUtils.getContext(cell);
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		if (tick == 30001) {
			double memValue = 8033;
			membraneContent.put("memL",memValue);
			membraneContent.put("memLS",memValue);
			membraneContent.put("memS",memValue);
			membraneContent.put("memCis",memValue);
			membraneContent.put("memMedial",memValue);
			membraneContent.put("memTrans",memValue);
			membraneContent.put("memTub",memValue);
			double solValue = 196257;
			solubleContent.put("solL", solValue);
			solubleContent.put("solS", solValue);
		}

		Endosome bud = new Endosome(space, grid, rabContent, membraneContent,
									solubleContent, initOrgProp);
		context.add(bud);
		// cylinder
		bud.speed = 1d / bud.size;
		bud.heading = -90;// heading down
		double rnd = Math.random();
		double upPosition = 25 + rnd* (25 - 4 * cellLimit);
		Cell.getInstance().getSpace().moveTo(bud, rnd * 50, upPosition);
		Cell.getInstance().getGrid().moveTo(bud, (int) rnd * 50, (int) upPosition);
		
		PlasmaMembrane.getInstance().getPlasmaMembraneTimeSeries().clear();
		
	}

	private static void newOrganelle(Cell cell, String selectedRab, HashMap<String, String> rabCode, double membraneFlux) {
		String kind = rabCode.get(selectedRab);
		HashMap<String, Double> initOrgProp = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitOrgProp().get(kind));
		HashMap<String, Double> membraneContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitMembraneContent().get(kind));
		HashMap<String, Double> solubleContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitSolubleContent().get(kind));
		HashMap<String, Double> rabContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitRabContent().get(kind));

		double area = 0d;
		double volume = 0d;
		if (membraneFlux == 1d){
			// if membraneFlux is ON then a new ERGIC like structure is formed as a kind1 organelle
			// It is a cistern 500 nm of radius and 20 nm high RabA with no membrane or soluble content 			

			double maxRadius = initOrgProp.get("maxRadius");
			//area of a cistern as a flat cylinder 20 nm high with a radius of maxRadius
			area = Math.PI*Math.pow(maxRadius, 2)*2d + 2d*maxRadius*Math.PI*20d; 
			volume = Math.PI*Math.pow(maxRadius, 2)* 20d;
			double value = Results.instance.getTotalRabs().get(selectedRab);
			value = value + area;
			Results.instance.getTotalRabs().put(selectedRab, value);
			initOrgProp.put("area", area);
			initOrgProp.put("volume", volume);
			
						for (String rab : rabContent.keySet()){
							double rr = rabContent.get(rab);
							rabContent.put(rab, rr*area);
						}
						for (String mem : membraneContent.keySet()){
							double mm = membraneContent.get(mem);
							membraneContent.put(mem, mm*area);
						}
					
						for (String sol : solubleContent.keySet()){
								double ss = solubleContent.get(sol);
							solubleContent.put(sol, ss*volume);
						}
		}
		else {
			double maxRadius = initOrgProp.get("maxRadius");
			double maxAsym = initOrgProp.get("maxAsym");
			double minRadius = Cell.rcyl*1.1;
			double a = RandomHelper.nextDoubleFromTo(minRadius,maxRadius);				
			double c = a + a  * Math.random()* maxAsym;
			double f = 1.6075;
			double af= Math.pow(a, f);
			double cf= Math.pow(c, f);
			area = 4d* Math.PI*Math.pow((af*af+af*cf+af*cf)/3, 1/f);
			volume = 4d/3d*Math.PI*a*a*c;
			double value = Results.instance.getTotalRabs().get(selectedRab);
			value = value + area;
			Results.instance.getTotalRabs().put(selectedRab, value);
			initOrgProp.put("area", area);
			initOrgProp.put("volume", volume);	
			rabContent.put(selectedRab, area);
			// If membraneFlux is not ON
	
			HashSet<String> solubleMet = new HashSet<String>(CellProperties.getInstance().getSolubleMet());
			HashSet<String> membraneMet = new HashSet<String>(CellProperties.getInstance().getMembraneMet());
			//	This is getting the keyset of the membrane metabolisms
			// MEMBRANE CONTENT.  For a new organelle, with the Rab that was selected to compensate lost, the membrane content is taken from the total
			// membrane content associated to this rab/total area of the rab.  This is an average of the membrane content associated to the specific
			// Rab.  Marker is set to zero.
			for (String mem : membraneMet){
				value = Results.getInstance().getContentDist().get(mem+selectedRab)
						/Results.getInstance().getTotalRabs().get(selectedRab);
				membraneContent.put(mem, value * area);
			}
			membraneContent.put("membraneMarker", 0d);
			// SOLUBLE CONTENT.  For a new organelle, with the Rab that was selected to compensate lost, the soluble content is taken from the total
			// soluble content associated to this rab/total volume surrounded by the rab.  This is an average of the soluble content associated to the specific
			// Rab.  Marker and mvb is set to zero
			for (String sol : solubleMet){
				value = Results.getInstance().getContentDist().get(sol+selectedRab)
						/Results.getInstance().getTotalVolumeRabs().get(selectedRab);
				solubleContent.put(sol, value * volume);
			}
			solubleContent.put("mvb", 0d);
			solubleContent.put("solubleMarker", 0d);
		}
		Context<Object> context = ContextUtils.getContext(cell);
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		
		if (tick == 30001) {
			double memValue = 8033;
			membraneContent.put("memL",memValue);
			membraneContent.put("memLS",memValue);
			membraneContent.put("memS",memValue);
			membraneContent.put("memCis",memValue);
			membraneContent.put("memMedial",memValue);
			membraneContent.put("memTrans",memValue);
			membraneContent.put("memTub",memValue);
			double solValue = 196257;
			solubleContent.put("solL", solValue);
			solubleContent.put("solS", solValue);
			
		}
		
		Endosome bud = new Endosome(space, grid, rabContent, membraneContent,
				solubleContent, initOrgProp);
		context.add(bud);		
		bud.area = area;
		bud.volume = volume; 
		// cylinder
		
		if (membraneFlux == 2d){
			bud.speed = 0d;
			bud.heading = -90;// heading down
			// NdPoint myPoint = space.getLocation(bud);
			double rnd = Math.random();
			cell.getSpace().moveTo(bud, 25, 5*rnd);
			cell.getGrid().moveTo(bud, 25, (int)(5*rnd));		
		}
		else{
			bud.speed = 1d / bud.size;
			bud.heading = -90;// heading down
			// NdPoint myPoint = space.getLocation(bud);
			double rnd = Math.random();
			cell.getSpace().moveTo(bud, rnd * 50, 10 + rnd* 30);
			cell.getGrid().moveTo(bud, (int) rnd * 50, (int) (10 + rnd* 30));
		}
	}
		
}
