package immunity;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
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

public class EndosomeUptakeStep {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	private static Object membreneMet;
	public static void uptake(Endosome endosome) {
//		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
//		if (tick < 100) return;
		space = endosome.getSpace();
		grid = endosome.getGrid();

		Cell cell = Cell.getInstance();
		HashMap<String, Double> totalRabs = new HashMap<String, Double>(Results.getInstance().getTotalRabs());
		HashMap<String, Double> initialTotalRabs = new HashMap<String, Double>(Results.getInstance().getInitialTotalRabs());
		System.out.println("totalRabs  "+totalRabs);

		HashMap<String, Double> deltaRabs = new HashMap<String, Double>();  
		HashMap<String, String> rabCode = new HashMap<String, String>();
		rabCode.put("RabA", "kind1");
		rabCode.put("RabB", "kind2");
		rabCode.put("RabC", "kind3");
		rabCode.put("RabD", "kind4");
		rabCode.put("RabE", "kind5");

		for (String rab : totalRabs.keySet()){
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
		if (selectedRab.equals("RabA")){ 
			newUptake(endosome, selectedRab);}
		else {newOrganelle(endosome, selectedRab, rabCode);}
	
	}
		
		private static void newUptake(Endosome endosome, String selectedRab) {
			double cellLimit = 3d * Cell.orgScale;
			HashMap<String, Double> initOrgProp = new HashMap<String, Double>(
					InitialOrganelles.getInstance().getInitOrgProp().get("kind1"));
//			double tMembrane = Cell.getInstance().gettMembrane();
			HashMap<String, Double> rabCell = Cell.getInstance().getRabCell();

			if (!rabCell.containsKey("RabA") || Math.random()>rabCell.get("RabA")){
				return;}
//			double rabCellA = rabCell.get("RabA");
	// cytosolic RabA is always provided by the -> RabAc reaction.  Only in a KD will go down
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
			
//			if (tMembrane < area) {
//				return;
//			}
			initOrgProp.put("area", area);
			double value = Results.instance.getTotalRabs().get("RabA");
			value = value + area;
			Results.instance.getTotalRabs().put("RabA", value);

			
			initOrgProp.put("volume", volume);
			HashMap<String, Double> rabContent = new HashMap<String, Double>();
//			UPTAKE ENDOSOME JUST RABA
			rabContent.put("RabA", area);
//			HashMap<String, Double> rabContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitRabContent().get("kind1"));
//					for (String rab : rabContent.keySet()){
//						double rr = rabContent.get(rab);
//						rabContent.put(rab, rr*area);
//					}
// 		Soluble and membrane content of the kind1, but cMHCI and mHCI depends now on PM content
			HashMap<String, Double> membraneContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitMembraneContent().get("kind1"));
					for (String mem : membraneContent.keySet()){
						double mm = membraneContent.get(mem);
						membraneContent.put(mem, mm*area);
					}
					double cMHCIvalueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get("cMHCI");
					double cMHCIvalue = 0d;
//					if (cMHCIvalueIn < area) cMHCIvalue = cMHCIvalueIn;
//					else cMHCIvalue = area;
					cMHCIvalue = cMHCIvalueIn * 1 * area/ (double) PlasmaMembrane.getInstance().area;
					membraneContent.put("cMHCI", cMHCIvalue);
					PlasmaMembrane.getInstance().getMembraneRecycle().put("cMHCI", cMHCIvalueIn - cMHCIvalue);
	
				
				HashMap<String, Double> solubleContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitSolubleContent().get("kind1"));
					for (String sol : solubleContent.keySet()){
						double ss = solubleContent.get(sol);
						solubleContent.put(sol, ss*volume);
			
					}
//			
//			System.out.println("UPTAKE ORGANELLES KIND 1 " + membraneContent  
//					+ solubleContent + rabContent);
//			try {
//				TimeUnit.SECONDS.sleep(5);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		
// new endosome incorporate PM components in a proportion area new/area PM
//					"Fully conformed MHC-I proteins internalize with the
//					rate 0.002–0.004 min−1 (0.2–0.4% loss of initially surface expressed
//					molecules per minute, i.e. 12–24% per hour) which is 5–8-fold lower
//					than IR of their open forms (0.011–0.022 min−1), 
//					Molecular Immunology 55 (2013) 149– 152 (Pero Lucin)"
//One 60 radius endosomes has an area of about 45000 nm.  This is about 7.5% of the 1500 x 400 nm of
//the PM considered at the 1 organelle scale.  So to internalize 0.3%. a factor of 0.04 (0.3%/7.5%) is applied.
//Hence, factor = endosome area/ PM area * 0.04 for cMHCIa and pept-mHCI, and endosome area/ PM area * 0.2 for mHCI
/*			double cMHCIvalueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get("cMHCI");
			double cMHCIvalue = cMHCIvalueIn * 0.4 * area/ (double) PlasmaMembrane.getInstance().area;
			membraneContent.put("cMHCI", cMHCIvalue);
			PlasmaMembrane.getInstance().getMembraneRecycle().put("cMHCI", cMHCIvalueIn - cMHCIvalue);
			
			double mHCIvalueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get("mHCI");
			double mHCIvalue = mHCIvalueIn * 1 * area/ (double) PlasmaMembrane.getInstance().area;
			membraneContent.put("mHCI", mHCIvalue);
			PlasmaMembrane.getInstance().getMembraneRecycle().put("mHCI", mHCIvalueIn - mHCIvalue);
*///			
//			for (String met : PlasmaMembrane.getInstance().getMembraneRecycle().keySet()){
//				double valueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get(met);
//				value = 0.001 * valueIn * initOrgProp.get("area")/PlasmaMembrane.getInstance().area;
//				if (value > area) {value = area;}

			solubleContent.put("proton", 3.98E-5*volume); //pH 7.4
			System.out.println("PLASMA MEMBRANE "+PlasmaMembrane.getInstance().getMembraneRecycle());

//			Cell.getInstance().settMembrane(tMembrane);

			// Cell.getInstance().setRabCell(rabCell);
			Context<Object> context = ContextUtils.getContext(endosome);

			Endosome bud = new Endosome(space, grid, rabContent, membraneContent,
					solubleContent, initOrgProp);
			context.add(bud);
//			tMembrane = tMembrane - bud.initOrgProp.get("area");
			bud.area = initOrgProp.get("area");
			bud.volume = initOrgProp.get("volume");
			bud.size = initOrgProp.get("maxRadius");// radius of a sphere with the volume of the
									// cylinder
			bud.speed = 1d / bud.size;
			bud.heading = -90;// heading down
			// NdPoint myPoint = space.getLocation(bud);
			double rnd = Math.random();
			double upPosition = 25 + rnd* (25 - 4 * cellLimit);
			endosome.getSpace().moveTo(bud, rnd * 50, upPosition);
			endosome.getGrid().moveTo(bud, (int) rnd * 50, (int) upPosition);
//			System.out.println(area + "NEW UPTAKE" + bud.membraneContent);
//			try {
//			TimeUnit.SECONDS.sleep(5);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	
	}

		private static void newOrganelle(Endosome endosome, String selectedRab, HashMap<String, String> rabCode) {
			String kind = rabCode.get(selectedRab);
			HashMap<String, Double> initOrgProp = new HashMap<String, Double>(
					InitialOrganelles.getInstance().getInitOrgProp().get(kind));
//		double tMembrane = Cell.getInstance().gettMembrane();

			double maxRadius = initOrgProp.get("maxRadius");
			double minRadius =Cell.rcyl*1.75;
			double a = 0d;
			double c = 0d;
//			more round vesicles for RabA and RabD.  Tubules for RabB, RabC and RabE
			if (selectedRab.equals("RabD")){
				a = RandomHelper.nextDoubleFromTo(minRadius,maxRadius);				
				c = a + a  * Math.random();
			}
			else{
				a = minRadius;				
				c = a + a * RandomHelper.nextDoubleFromTo(2,10);
			}
			double f = 1.6075;
			double af= Math.pow(a, f);
			double cf= Math.pow(c, f);
			double area = 4d* Math.PI*Math.pow((af*af+af*cf+af*cf)/3, 1/f);
			double volume = 0.9*4d/3d*Math.PI*a*a*c;
						
			double value = Results.instance.getTotalRabs().get(selectedRab);
			value = value + area;
			Results.instance.getTotalRabs().put(selectedRab, value);
			initOrgProp.put("area", area);
			initOrgProp.put("volume", volume);	
			HashMap<String, Double> rabContent = new HashMap<String, Double>();
			rabContent.put(selectedRab, area);
			HashMap<String, Double> membraneContent = new HashMap<String, Double>();
			HashMap<String, Double> solubleContent = new HashMap<String, Double>();
			HashSet<String> solubleMet = (HashSet<String>) CellProperties.getInstance().getSolubleMet();
			HashSet<String> membraneMet = (HashSet<String>) CellProperties.getInstance().getMembraneMet();
			for (String mem : membraneMet){
//				System.out.println(mem + "  MMEEMM " + selectedRab + "\n " + Results.getInstance().getContentDist());
				value = Results.getInstance().getContentDist().get(mem+selectedRab)
						/Results.getInstance().getTotalRabs().get(selectedRab);
				membraneContent.put(mem, value * area);
			}
			membraneContent.put("membraneMarker", 0d);
			for (String sol : solubleMet){
				value = Results.getInstance().getContentDist().get(sol+selectedRab)
						/Results.getInstance().getTotalVolumeRabs().get(selectedRab);
				solubleContent.put(sol, value * volume);
			}
			solubleContent.put("mvb", 0d);
			solubleContent.put("solubleMarker", 0d);
/*//			HashMap<String, Double> rabContent = new HashMap<String, Double>(InitialOrganelles.getInstance().
//					getInitRabContent().get(kind));
//					for (String rab : rabContent.keySet()){
//						double rr = rabContent.get(rab);
//						rabContent.put(rab, rr*area);
//					}
//
//			HashMap<String, Double> membraneContent = new HashMap<String, Double>(InitialOrganelles.getInstance().
//					getInitMembraneContent().get(kind));
//					for (String mem : membraneContent.keySet()){
//						double mm = membraneContent.get(mem);
//						membraneContent.put(mem, mm*area);
//					}
*/				
//			HashMap<String, Double> solubleContent = new HashMap<String, Double>(InitialOrganelles.
//					getInstance().getInitSolubleContent().get(kind));
//					for (String sol : solubleContent.keySet()){
//						double ss = solubleContent.get(sol);
//						solubleContent.put(sol, ss*volume);
//			
//					}
					
					// Cell.getInstance().setRabCell(rabCell);
			Context<Object> context = ContextUtils.getContext(endosome);

			Endosome bud = new Endosome(space, grid, rabContent, membraneContent,
							solubleContent, initOrgProp);
			context.add(bud);
			bud.area = initOrgProp.get("area");
			bud.volume = initOrgProp.get("volume");
			bud.size = initOrgProp.get("maxRadius");// radius of a sphere with the volume of the
											// cylinder
			bud.speed = 1d / bud.size;
			bud.heading = -90;// heading down
					// NdPoint myPoint = space.getLocation(bud);
			double rnd = Math.random();
			endosome.getSpace().moveTo(bud, rnd * 50, 10 + rnd* 30);
			endosome.getGrid().moveTo(bud, (int) rnd * 50, (int) (10 + rnd* 30));
			
//			System.out.println("NEW ORGANELLE " + selectedRab +" \n"+ membraneContent  
//					+ solubleContent + rabContent + "\n area" + area +Results.getInstance().getTotalRabs());
//			try {
//				TimeUnit.SECONDS.sleep(5);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		

	}
		
	
	


}
