package immunity;

import java.util.HashMap;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class EndosomeUptakeStep {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	
	public static void uptake(Endosome endosome) {
		space = endosome.getSpace();
		grid = endosome.getGrid();
		double cellLimit = 3 * Cell.orgScale;
		Cell cell = Cell.getInstance();
		HashMap<String, Double> initOrgProp = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitOrgProp().get("kind1"));
		double tMembrane = cell.gettMembrane();
		HashMap<String, Double> rabCell = cell.getRabCell();
		// System.out.println("CELL RAB "+ rabCell+ "TMEMBRANE "+ tMembrane);
		if (!rabCell.containsKey("RabA") || Math.random()>rabCell.get("RabA"))
			return;
//		double rabCellA = rabCell.get("RabA");
// cytosolic RabA is always provided by the -> RabAc reaction.  Only in a KD will go down
//	Then no uptake if no RabA in cyto.  The uptake is proportional to the amount of RabA	
		if (tMembrane < initOrgProp.get("area")) {
			return;
		}

		double radius = initOrgProp.get("radius");
		double area = 4d* Math.PI*Math.pow(radius, 2d);
		double volume = 4d/3d*Math.PI*Math.pow(radius, 3d);
		initOrgProp.put("area", area);
		initOrgProp.put("volume", volume);			
		HashMap<String, Double> rabContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitRabContent().get("kind1"));
				for (String rab : rabContent.keySet()){
					double rr = rabContent.get(rab);
					rabContent.put(rab, rr*area);
				}
		HashMap<String, Double> membraneContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitMembraneContent().get("kind1"));
				for (String mem : membraneContent.keySet()){
					double mm = membraneContent.get(mem);
					membraneContent.put(mem, mm*area);
				}
			
			HashMap<String, Double> solubleContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitSolubleContent().get("kind1"));
				for (String sol : solubleContent.keySet()){
					double ss = solubleContent.get(sol);
					solubleContent.put(sol, ss*volume);
		
		
//		
//		
//		HashMap<String, Double> rabContent = new HashMap<String, Double>(
//				InitialOrganelles.getInstance().getInitRabContent()
//						.get("kind1"));
//		HashMap<String, Double> membraneContent = new HashMap<String, Double>(
//				InitialOrganelles.getInstance().getInitMembraneContent()
//						.get("kind1"));
//
//		HashMap<String, Double> solubleContent = new HashMap<String, Double>(
//				InitialOrganelles.getInstance().getInitSolubleContent()
//						.get("kind1"));
//		
		System.out.println("INITIAL ORGANELLES KIND 1 " + membraneContent  
				+ solubleContent + rabContent);
		
// new endosome incorporate PM components in a proportion area new/area PM
//to accumulte, I included the 0.001 factor
//	PlasmaMembrane.getInstance().getMembraneRecycle().put("vATPase", 60000d);
		
		for (String met : PlasmaMembrane.getInstance().getMembraneRecycle().keySet()){
			double valueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get(met);
			double value = 0.001 * valueIn * initOrgProp.get("area")/PlasmaMembrane.getInstance().area;
			if (value > initOrgProp.get("area")) {value = initOrgProp.get("area");	}		
			membraneContent.put(met, value);
			PlasmaMembrane.getInstance().getMembraneRecycle().put(met, valueIn - value);
			}
		membraneContent.put("cMHCI", initOrgProp.get("area"));
		membraneContent.put("vATPase", initOrgProp.get("area")/10.0);
		System.out.println("PLASMA MEMBRANE "+PlasmaMembrane.getInstance().getMembraneRecycle());

//
//		if (PlasmaMembrane.getInstance().getMembraneRecycle().containsKey("mHCI")) {
//			double valueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get("mHCI");
//			double value = valueIn * initOrgProp.get("area")/PlasmaMembrane.getInstance().area;
//			if (value >  initOrgProp.get("area")) {value = initOrgProp.get("area");}
//			membraneContent.put("mHCI", value);
//			PlasmaMembrane.getInstance().getMembraneRecycle().put("mHCI", valueIn - value);
//		} else
//			{membraneContent.put("mHCI", 0d);}

		
		// new endosome incorporate  plasma membrane pLANCL2 and LANCL2 in a proportion area new/area PM
//		if (PlasmaMembrane.getInstance().getMembraneRecycle().containsKey("pLANCL2")) {
//			double valueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get("pLANCL2");
//			double value = valueIn * initOrgProp.get("area")/PlasmaMembrane.getInstance().area;
//			if (value >  initOrgProp.get("area")) {value = initOrgProp.get("area");}
//			membraneContent.put("pLANCL2", value);
//			PlasmaMembrane.getInstance().getMembraneRecycle().put("pLANCL2", valueIn-value);
//		} else
//			{membraneContent.put("pLANCL2", 0d);}
//		
//		if (PlasmaMembrane.getInstance().getMembraneRecycle().containsKey("LANCL2")) {
//			double valueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get("LANCL2");
//			double value = valueIn * initOrgProp.get("area")/PlasmaMembrane.getInstance().area;
//			if (value >  initOrgProp.get("area")) {value = initOrgProp.get("area");}
//			membraneContent.put("LANCL2", value);
//			PlasmaMembrane.getInstance().getMembraneRecycle().put("LANCL2", valueIn-value);
//		} else
//			{membraneContent.put("LANCL2", 0d);}
		
		Context<Object> context = ContextUtils.getContext(endosome);

		Endosome bud = new Endosome(space, grid, rabContent, membraneContent,
				solubleContent, initOrgProp);
		context.add(bud);
		
		// context.add(new Endosome(space, grid, rabContent, membraneContent,
		// solubleContent));
		System.out.println("NEW ENDOSOME UPTAKE \n"
				+ solubleContent + rabContent + "\n");
		System.out.println(membraneContent);
		// tMembrane = Cell.getInstance().gettMembrane();
		tMembrane = tMembrane - bud.initOrgProp.get("area");
//		I decrease cytoRabA by 90%.   It will be recovered during RabConversion where
//		it is constantly supplied		
//		rabCellA = rabCellA * 0.1; //bud.initOrgProp.get("area");
//		rabCell.put("RabA", rabCellA);
		Cell.getInstance().settMembrane(tMembrane);

		// Cell.getInstance().setRabCell(rabCell);

		bud.area = initOrgProp.get("area");
		bud.volume = initOrgProp.get("volume");
		bud.size = initOrgProp.get("radius");// radius of a sphere with the volume of the
								// cylinder
		bud.speed = 1d / bud.size;
		bud.heading = -90;// heading down
		// NdPoint myPoint = space.getLocation(bud);
		double rnd = Math.random();
		endosome.getSpace().moveTo(bud, rnd * 50, 50 - 2 * cellLimit);
		endosome.getGrid().moveTo(bud, (int) rnd * 50, (int) (50 - 2 * cellLimit));
	}
	}


}
