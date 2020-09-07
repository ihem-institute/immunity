package immunity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class UptakeStep2 {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	private static Object membreneMet;
	public static void uptake(Cell cell) {
		//		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		//		if (tick < 100) return;
		space = cell.getSpace();
		grid = cell.getGrid();
		/* ?
		 * Old logic.  The amount of domains of each Rab is compared with the initial value
		 * The domain with more difference is selected to generate a new organelle
		 * 	If the Rab selected is RabA, the new organelle is generated from 
		 *  plasma membrane. If not, the new organelle is generated according to the "kind" 
		 *  specified in the csv file for the initial organelles
		 *  
		 *  New logic.  The EE are created according to the PM membrane disponibility
		 * that is maintained by recycling of tubular EE. The maturation of EE is compensated by the
		 * membrane of the internal vesicles that are added to the PM (thinking in synthesis of membrane)
		 * The maturation also generate LE membrane that should be compensated by the reduction of the limiting
		 * membrane also by the generation of internal vesicles
		 * The old logic will be maintained until the system is stable
		 *  */

//		Cell cell = Cell.getInstance();
		HashMap<String, Double> totalRabs = new HashMap<String, Double>(Results.getInstance().getTotalRabs());
		HashMap<String, Double> initialTotalRabs = new HashMap<String, Double>(Results.getInstance().getInitialTotalRabs());
		//		System.out.println("totalRabs  "+totalRabs);

		HashMap<String, Double> deltaRabs = new HashMap<String, Double>();  
		HashMap<String, String> rabCode = new HashMap<String, String>();
		rabCode.put("RabA", "kind1");
		rabCode.put("RabB", "kind2");
		rabCode.put("RabC", "kind3");
		rabCode.put("RabD", "kind4");
		rabCode.put("RabE", "kind5");
		rabCode.put("RabF", "kind6");
		rabCode.put("RabG", "kind7");
		rabCode.put("RabH", "kind8");
		rabCode.put("RabI", "kind9");
//		NEW UPTAKE EVENT
		double areaPM = PlasmaMembrane.getInstance().getPlasmaMembraneArea();
		double initialAreaPM = PlasmaMembrane.getInstance().getInitialPlasmaMembraneArea();

		if(areaPM > initialAreaPM) {
		System.out.println(" 	NEW UPTAKE    " + areaPM + "    "+initialAreaPM);
		newUptake(cell,"RabA");
		System.out.println(" 	NEW UPTAKE    " + PlasmaMembrane.getInstance().getPlasmaMembraneArea() + "    "+initialAreaPM);}

		//		NEW SECRETORY EVENT
		double areaER = EndoplasmicReticulum.getInstance().getendoplasmicReticulumArea();
		double initialAreaER = EndoplasmicReticulum.getInstance().getendoplasmicReticulumArea();

		if (areaER > initialAreaER)
		newSecretion(cell,"RabI");
		
//		COMPENSATORY NEW ORGANELLE


		for (String rab : totalRabs.keySet()){
			//			System.out.println("ErrorRabs  "+ rab + "   " +initialTotalRabs.get(rab) +"     "+ totalRabs.get(rab));
			double value = initialTotalRabs.get(rab) - totalRabs.get(rab);
			deltaRabs.put(rab, value);
		}

		//	System.out.println("Initial Rabs  "+ initialTotalRabs + " \n delta Rabs"+ deltaRabs);
		double largeDelta = 0d;
		String selectedRab = "";
		for (String rab : deltaRabs.keySet()){
			if (deltaRabs.get(rab)>largeDelta) {
				selectedRab=rab;
				largeDelta=deltaRabs.get(rab);
			}	
		}
		//		System.out.println("selected Rab for uptake "+ selectedRab);
		//		If no rab was selected or the surface required is small (less than a sphere of 60 nm radius, 
		//		no uptake is required
		if (selectedRab.equals("")|| deltaRabs.get(selectedRab)<45000) return;
		//if the selected Rab correspond to Early Endosomes, new uptake
		String selectedOrganelle = ModelProperties.getInstance().getRabOrganelle().get(selectedRab);
		if (selectedOrganelle.equals("EE")){ 
			newUptake(cell,selectedRab);}
		if (selectedOrganelle.equals("ERGIC")){ 
			newSecretion(cell,selectedRab);}
		else {newOrganelle(cell, selectedRab, rabCode);}

	}
		
	private static void newSecretion(Cell cell, String selectedRab) {
		double cellLimit = 3d * Cell.orgScale;
		System.out.println("secretion " +	InitialOrganelles.getInstance().getInitOrgProp().get("kind9"));
		HashMap<String, Double> initOrgProp = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitOrgProp().get("kind9"));
		HashMap<String, Double> rabCell = cell.getRabCell();

		if (!rabCell.containsKey("RabI") || Math.random()>rabCell.get("RabI")){
			return;}
	//	double rabCellA = rabCell.get("RabA");
	// OJO I AM ASSUMING THAT RABI IS ERGIC AND THIS CANNOT BE ALWAYS TRUE
	// cytosolic RabI is always provided by the -> RabAc reaction.  Only in a KD will go down
	//		Then no new ERGIC if no RabI in cyto.  The uptake is proportional to the amount of RabI	
	//		Secretion generate a new RabI organelle.  The size is the radius in the csv file for maxRadius in kind9
	//		initial organelles.  The content is specified in concentration. One (1) is approx 1 mM.
	//		This units are converted to membrane or volume units by multiplying by the area (rabs and
	//		membrane content) or volume (soluble content).  For secretion it is controlled that there is enough
	//		membrane and there is RabI in the cell

		double maxRadius = initOrgProp.get("maxRadius");
		double minRadius = maxRadius/2;
		double a = RandomHelper.nextDoubleFromTo(minRadius,maxRadius);				
		double c = a + a  * Math.random() * initOrgProp.get("maxAsym");

		double f = 1.6075;
		double af= Math.pow(a, f);
		double cf= Math.pow(c, f);
		double area = 4d* Math.PI*Math.pow((af*af+af*cf+af*cf)/3, 1/f);
		double endoplasmicReticulum = EndoplasmicReticulum.getInstance().getendoplasmicReticulumArea() - area;
//		System.out.println("LUETO DE UPTAKE  "+ endoplasmicReticulum);
		EndoplasmicReticulum.getInstance().setendoplasmicReticulumArea(endoplasmicReticulum);
		double volume = 4d/3d*Math.PI*a*a*c;
		initOrgProp.put("area", area);
		double value = Results.instance.getTotalRabs().get("RabI");
		value = value + area;
		Results.instance.getTotalRabs().put("RabI", value);
		initOrgProp.put("volume", volume);
		HashMap<String, Double> rabContent = new HashMap<String, Double>();
	//			NEW ERGIC JUST RABI
		rabContent.put("RabI", area);
		
		/* Soluble and membrane content of the kind9
		 * To model molecules that cycle between ER and endosomes, such as cMHCI and mHCI
		 * the new endosome incorporates metabolites that are present in the ER.
		 * Each species has its own rate for internalization (secretion rate).
		 * Also, the new ERGIC cannot incorporate more than a given amount of ER metabolites
		 * In area units, no more than its surface (about 1 mM)
		 * 
		 * 
		 * */
		
		HashMap<String, Double> membraneContent = new HashMap<String,Double>();
		Set<String> membraneMet = new HashSet<String>(ModelProperties.getInstance().getMembraneMet());
		for (String mem : membraneMet){
			double valueInEn = 0d;
			double valueInER =0d;
			double valueInTotal = 0d;
		System.out.println(mem + ModelProperties.getInstance().getSecretionRate().get(mem) + "   UPTAKE DECREASE 1111  " + valueInER);
			if (EndoplasmicReticulum.getInstance().getMembraneRecycle().containsKey(mem))
			{
				double valueER = EndoplasmicReticulum.getInstance().getMembraneRecycle().get(mem);
				valueInER = valueER * ModelProperties.getInstance().getSecretionRate().get(mem) * area/ EndoplasmicReticulum.getInstance().getendoplasmicReticulumArea();	

				if (valueInER >= area) valueInER = area; // cannot incorporate more metabolite than its area
				membraneContent.put(mem, valueInER);
				//	System.out.println(mem + valueER + "   UPTAKE DECREASE 1111  " + valueInER);
				// decrease ER content
				EndoplasmicReticulum.getInstance().getMembraneRecycle().put(mem, valueER-valueInER);
			}
			/* FOR UPTAKE LOADING IN NEW ENDOSOMES
			 * if (InitialOrganelles.getInstance().getInitMembraneContent().get("kind1").
			 * containsKey(mem)) { valueInEn =
			 * InitialOrganelles.getInstance().getInitMembraneContent().get("kind1").get(mem
			 * )*area; valueInTotal = valueInEn + valueInER; } if (valueInTotal >= area)
			 * valueInTotal= area;
			 */
		}
		HashMap<String, Double> solubleContent = new HashMap<String,Double>();
		Set<String> solubleMet = new HashSet<String>(ModelProperties.getInstance().getSolubleMet());
		for (String sol : solubleMet){
			double valueInEn = 0d;
			double valueInER =0d;
			
			if (EndoplasmicReticulum.getInstance().getSolubleRecycle().containsKey(sol))
			{
				double valueER = EndoplasmicReticulum.getInstance().getSolubleRecycle().get(sol);
				valueInER = valueER * volume/ EndoplasmicReticulum.getInstance().getendoplasmicReticulumVolume();	

				if (valueInER >= volume) valueInER = volume;
				solubleContent.put(sol, valueInER);
				// decrease ER content
				EndoplasmicReticulum.getInstance().getSolubleRecycle().put(sol, valueER - valueInER);
			}
			/*
			 * if (InitialOrganelles.getInstance().getInitSolubleContent().get("kind1").
			 * containsKey(sol)) { valueInEn =
			 * InitialOrganelles.getInstance().getInitSolubleContent().get("kind1").get(sol)
			 * *volume; valueInEn = valueInEn + valueInER; } if (valueInEn >= volume)
			 * valueInEn= volume; solubleContent.put(sol, valueInEn);
			 */
		}

		solubleContent.put("proton", 3.98E-5*volume); //pH 7.4
	/*		
	 new ERGIC incorporate ER components in a proportion area new/area ER
	 */		
		Context<Object> context = ContextUtils.getContext(cell);
		ContinuousSpace<Object> space = cell.getSpace();
		Grid<Object> grid = cell.getGrid();
		Endosome bud = new Endosome(space, grid, rabContent, membraneContent,
				solubleContent, initOrgProp);
		context.add(bud);
		bud.speed = 1d / bud.size;
		bud.heading = 90;// heading up
		double rnd = Math.random();
		double upPosition = rnd* (4 * cellLimit);
		space.moveTo(bud, rnd * 50, upPosition);
		grid.moveTo(bud, (int) rnd * 50, (int) upPosition);
		
		//			System.out.println(area + "NEW UPTAKE" + bud.membraneContent);
		//			try {
		//			TimeUnit.SECONDS.sleep(5);
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
	EndoplasmicReticulum.getInstance().getendoplasmicReticulumTimeSeries().clear();
	}

	private static void newUptake(Cell cell, String selectedRab) {
		double cellLimit = 3d * Cell.orgScale;
		System.out.println("UPTAKE INITIAL ORGANELLES " +	InitialOrganelles.getInstance().getInitOrgProp().get("kind1"));
		HashMap<String, Double> initOrgProp = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitOrgProp().get("kind1"));
		
//		System.out.println("PROPIEDADES RAB A  "+initOrgProp);
//		System.out.println("A VER?" + InitialOrganelles.getInstance().getInitOrgProp().get("kind2"));
		HashMap<String, Double> rabCell = cell.getRabCell();

		if (!rabCell.containsKey("RabA") || Math.random()>rabCell.get("RabA")){
			return;}
	//	double rabCellA = rabCell.get("RabA");
	// OJO I AM ASSUMING THAT RABA IS EE AND THIS CANNOT BE ALWAYS TRUE
	// cytosolic RabA is always provided by the -> RabAc reaction.  Only in a KD will go down
	//		Then no uptake if no RabA in cyto.  The uptake is proportional to the amount of RabA	
	//		Uptake generate a new RabA organelle.  The size is the radius in the csv file for RabA
	//		initial organelles.  The content is specified in concentration. One (1) is approx 1 mM.
	//		This units are converted to membrane or volume units by multiplying by the area (rabs and
	//		membrane content) or volume (soluble content).  For uptake it is controlled that there is enough
	//		membrane and there is RabA in the cell

		double maxRadius = initOrgProp.get("maxRadius");
		double minRadius = maxRadius/2;
		double a = RandomHelper.nextDoubleFromTo(minRadius,maxRadius);				
		double c = a + a  * Math.random() * initOrgProp.get("maxAsym");

		double f = 1.6075;
		double af= Math.pow(a, f);
		double cf= Math.pow(c, f);
		double area = 4d* Math.PI*Math.pow((af*af+af*cf+af*cf)/3, 1/f);
		double plasmaMembrane = PlasmaMembrane.getInstance().getPlasmaMembraneArea() - area;
//		System.out.println("LUETO DE UPTAKE  "+ plasmaMembrane);
		PlasmaMembrane.getInstance().setPlasmaMembraneArea(plasmaMembrane);
		double volume = 4d/3d*Math.PI*a*a*c;
		initOrgProp.put("area", area);
		double value = Results.instance.getTotalRabs().get("RabA");
		value = value + area;
		Results.instance.getTotalRabs().put("RabA", value);
		initOrgProp.put("volume", volume);
//		System.out.println("PROPIEDADES RAB A  "+initOrgProp);
//		try {
//			Thread.sleep(4000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}


		HashMap<String, Double> rabContent = new HashMap<String, Double>();
	//			UPTAKE ENDOSOME JUST RABA
		rabContent.put("RabA", area);
		
		/* Soluble and membrane content of the kind1
		 * To model molecules that cycle between PM and endosomes, such as cMHCI and mHCI
		 * the new endosome incorporates metabolites that are present in the PM.
		 * Each species has its own rate for internalization (uptake rate).
		 * Also, the new endosome cannot incorporate more than a given amount of PM metabolites
		 * In area units, no more than its surface (about 1 mM)
		 * 
		 * 
		 * */
		
		HashMap<String, Double> membraneContent = new HashMap<String,Double>();
		Set<String> membraneMet = new HashSet<String>(ModelProperties.getInstance().getMembraneMet());
		for (String mem : membraneMet){
			double valueInEn = 0d;
			double valueInPM =0d;
			double valueInTotal = 0d;

			if (PlasmaMembrane.getInstance().getMembraneRecycle().containsKey(mem))
			{
				double valuePM = PlasmaMembrane.getInstance().getMembraneRecycle().get(mem);
				valueInPM = valuePM * ModelProperties.getInstance().getUptakeRate().get(mem) * area/ PlasmaMembrane.getInstance().getPlasmaMembraneArea();	

				if (valueInPM >= area) valueInPM = area; // cannot incorporate more metabolite than its area
				membraneContent.put(mem, valueInPM);
				//	System.out.println(mem + valuePM + "   UPTAKE DECREASE 1111  " + valueInPM);
				// decrease PM content
				PlasmaMembrane.getInstance().getMembraneRecycle().put(mem, valuePM-valueInPM);
			}
			/* FOR UPTAKE LOADING IN NEW ENDOSOMES
			 * if (InitialOrganelles.getInstance().getInitMembraneContent().get("kind1").
			 * containsKey(mem)) { valueInEn =
			 * InitialOrganelles.getInstance().getInitMembraneContent().get("kind1").get(mem
			 * )*area; valueInTotal = valueInEn + valueInPM; } if (valueInTotal >= area)
			 * valueInTotal= area;
			 */
		}
		HashMap<String, Double> solubleContent = new HashMap<String,Double>();
		Set<String> solubleMet = new HashSet<String>(ModelProperties.getInstance().getSolubleMet());
		for (String sol : solubleMet){
			double valueInEn = 0d;
			double valueInPM =0d;
			
			if (PlasmaMembrane.getInstance().getSolubleRecycle().containsKey(sol))
			{
				double valuePM = PlasmaMembrane.getInstance().getSolubleRecycle().get(sol);
				valueInPM = valuePM * volume/ PlasmaMembrane.getInstance().getPlasmaMembraneVolume();	

				if (valueInPM >= volume) valueInPM = volume;
				solubleContent.put(sol, valueInPM);
				// decrease PM content
				PlasmaMembrane.getInstance().getSolubleRecycle().put(sol, valuePM - valueInPM);
			}
			/*
			 * if (InitialOrganelles.getInstance().getInitSolubleContent().get("kind1").
			 * containsKey(sol)) { valueInEn =
			 * InitialOrganelles.getInstance().getInitSolubleContent().get("kind1").get(sol)
			 * *volume; valueInEn = valueInEn + valueInPM; } if (valueInEn >= volume)
			 * valueInEn= volume; solubleContent.put(sol, valueInEn);
			 */
		}

		solubleContent.put("proton", 3.98E-5*volume); //pH 7.4
	/*		
	 new endosome incorporate PM components in a proportion area new/area PM
						"Fully conformed MHC-I proteins internalize with the
						rate 0.002–0.004 min−1 (0.2–0.4% loss of initially surface expressed
						molecules per minute, i.e. 12–24% per hour) which is 5–8-fold slower
					than IR of their open forms (0.011–0.022 min−1), 
						Molecular Immunology 55 (2013) 149– 152 (Pero Lucin)"
	One 60 radius endosomes has an area of about 45000 nm.  This is about 7.5% of the 1500 x 400 nm of
	the PM considered at the 1 organelle scale.  So to internalize 0.3%. a factor of 0.04 (0.3%/7.5%) is applied.
	Hence, factor = endosome area/ PM area * 0.04 for cMHCIa and pept-mHCI, and endosome area/ PM area * 0.2 for mHCI
				
My problem is that I do not know the rate of uptake that depends on how much Kind1(Rab5) organelles are 
switched to Kind4(Rab7).  I guess is that the rate will have to be relative.  1 for open MHCI and 0.15 for closed.
	
				double cMHCIvalueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get("cMHCI");
		double cMHCIvalue = cMHCIvalueIn * 0.4 * area/ (double) PlasmaMembrane.getInstance().area;
		membraneContent.put("cMHCI", cMHCIvalue);
		PlasmaMembrane.getInstance().getMembraneRecycle().put("cMHCI", cMHCIvalueIn - cMHCIvalue);

		double mHCIvalueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get("mHCI");
		double mHCIvalue = mHCIvalueIn * 1 * area/ (double) PlasmaMembrane.getInstance().area;
		membraneContent.put("mHCI", mHCIvalue);
		PlasmaMembrane.getInstance().getMembraneRecycle().put("mHCI", mHCIvalueIn - mHCIvalue);		
					for (String met : PlasmaMembrane.getInstance().getMembraneRecycle().keySet()){
					double valueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get(met);
					value = 0.001 * valueIn * initOrgProp.get("area")/PlasmaMembrane.getInstance().area;
					if (value > area) {value = area;}
	 */		

		Context<Object> context = ContextUtils.getContext(cell);
		ContinuousSpace<Object> space = cell.getSpace();
		Grid<Object> grid = cell.getGrid();
		Endosome bud = new Endosome(space, grid, rabContent, membraneContent,
				solubleContent, initOrgProp);
		context.add(bud);
		bud.speed = 1d / bud.size;
		bud.heading = -90;// heading down
		double rnd = Math.random();
		double upPosition = 25 + rnd* (25 - 4 * cellLimit);
		space.moveTo(bud, rnd * 50, upPosition);
		grid.moveTo(bud, (int) rnd * 50, (int) upPosition);
		
		//			System.out.println(area + "NEW UPTAKE" + bud.membraneContent);
		//			try {
		//			TimeUnit.SECONDS.sleep(5);
		//		} catch (InterruptedException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
	PlasmaMembrane.getInstance().getPlasmaMembraneTimeSeries().clear();
		
	}

	private static void newOrganelle(Cell cell, String selectedRab, HashMap<String, String> rabCode) {
		String kind = rabCode.get(selectedRab);
		boolean isNewGolgi = ModelProperties.getInstance().getRabOrganelle().get(selectedRab).contains("Golgi");
		if(isNewGolgi) {// new Golgi organelle
			HashMap<String, Double> initOrgProp =  new HashMap<String, Double>(InitialOrganelles.getInstance().getInitOrgProp().get(kind));
			double totalArea = initOrgProp.get("area")/ModelProperties.getInstance().getCellK().get("orgScale");
			double maxRadius = initOrgProp.get("maxRadius");
			double maxAsym = initOrgProp.get("maxAsym");
			double minRadius = Cell.rcyl*1.1;
			double a = RandomHelper.nextDoubleFromTo(minRadius,maxRadius);// radius cylinder Gogli cisterna				
			double c = minRadius; //cylinder height
			double area = 2* Math.PI*Math.pow(a, 2)+ 2*Math.PI*a*c;
			double volume =Math.PI*Math.pow(a, 2)* c;
			initOrgProp.put("area", area);
			initOrgProp.put("volume", volume);
			double value = Results.instance.getTotalRabs().get(selectedRab);
			value = value + area;
			Results.instance.getTotalRabs().put(selectedRab, value);
			HashMap<String, Double> rabContent = new HashMap<String, Double>();
			rabContent.put(selectedRab, area);
			HashMap<String, Double> membraneContent = new HashMap<String, Double>();
			HashMap<String, Double> solubleContent = new HashMap<String, Double>();
			HashSet<String> solubleMet = new HashSet<String>(ModelProperties.getInstance().getSolubleMet());
			HashSet<String> membraneMet = new HashSet<String>(ModelProperties.getInstance().getMembraneMet());
//				This is getting the keyset of the membrane metabolisms
			// MEMBRANE CONTENT.  For a new organelle, with the Rab that was selected to compensate lost, the membrane content is taken from the total
			// membrane content associated to this rab/total area of the rab.  This is an average of the membrane content associated to the specific
			// Rab.  Marker is set to zero.
					for (String mem : membraneMet){
						//				System.out.println(mem + "  MMEEMM " + selectedRab + "\n " + Results.getInstance().getContentDist());
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
					Context<Object> context = ContextUtils.getContext(cell);
					ContinuousSpace<Object> space = cell.getSpace();
					Grid<Object> grid = cell.getGrid();
					Endosome bud = new Endosome(space, grid, rabContent, membraneContent,
							solubleContent, initOrgProp);
					context.add(bud);
					bud.area = area; 
					bud.volume = volume; 
					bud.speed = 1d / bud.size;
					bud.heading = -90;// heading down
					// NdPoint myPoint = space.getLocation(bud);
					double rnd = Math.random();
					space.moveTo(bud, rnd * 50, 5d);
					grid.moveTo(bud, (int) rnd * 50, (int) (5));


//					System.out.println(membraneContent + " " + solubleContent + " " + rabContent+" " + initOrgProp);
	
		}
		else {// new non Golgi organelles
		System.out.println(kind + " UPTAKE INITIAL ORGANELLES " +	InitialOrganelles.getInstance().getInitOrgProp().get(kind));

		HashMap<String, Double> initOrgProp = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitOrgProp().get(kind));
		
		//		double tMembrane = Cell.getInstance().gettMembrane();
		double maxRadius = initOrgProp.get("maxRadius");
		double maxAsym = initOrgProp.get("maxAsym");
		double minRadius = Cell.rcyl*1.1;
		double a = RandomHelper.nextDoubleFromTo(minRadius,maxRadius);				
		double c = a + a  * Math.random()* maxAsym;
		double f = 1.6075;
		double af= Math.pow(a, f);
		double cf= Math.pow(c, f);
		double area = 4d* Math.PI*Math.pow((af*af+af*cf+af*cf)/3, 1/f);
		double volume = 4d/3d*Math.PI*a*a*c;
		double value = Results.instance.getTotalRabs().get(selectedRab);
		value = value + area;
		Results.instance.getTotalRabs().put(selectedRab, value);
		initOrgProp.put("area", area);
		initOrgProp.put("volume", volume);	
		HashMap<String, Double> rabContent = new HashMap<String, Double>();
		rabContent.put(selectedRab, area);
		
		HashMap<String, Double> membraneContent = new HashMap<String, Double>();
		HashMap<String, Double> solubleContent = new HashMap<String, Double>();
		HashSet<String> solubleMet = new HashSet<String>(ModelProperties.getInstance().getSolubleMet());
		HashSet<String> membraneMet = new HashSet<String>(ModelProperties.getInstance().getMembraneMet());
//	This is getting the keyset of the membrane metabolisms
// MEMBRANE CONTENT.  For a new organelle, with the Rab that was selected to compensate lost, the membrane content is taken from the total
// membrane content associated to this rab/total area of the rab.  This is an average of the membrane content associated to the specific
// Rab.  Marker is set to zero.
		for (String mem : membraneMet){
			//				System.out.println(mem + "  MMEEMM " + selectedRab + "\n " + Results.getInstance().getContentDist());
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
		Context<Object> context = ContextUtils.getContext(cell);
		ContinuousSpace<Object> space = cell.getSpace();
		Grid<Object> grid = cell.getGrid();
		Endosome bud = new Endosome(space, grid, rabContent, membraneContent,
				solubleContent, initOrgProp);
		context.add(bud);
		bud.area = area; 
		bud.volume = volume;
		bud.speed = 1d / bud.size;
		bud.heading = -90;// heading down
		// NdPoint myPoint = space.getLocation(bud);
		double rnd = Math.random();
		space.moveTo(bud, rnd * 50, 10 + rnd* 30);
		grid.moveTo(bud, (int) rnd * 50, (int) (10 + rnd* 30));
		}
	}
		
}
