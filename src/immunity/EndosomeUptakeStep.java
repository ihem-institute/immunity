package immunity;

import java.util.HashMap;

import repast.simphony.context.Context;
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
		if (!rabCell.containsKey("RabA"))
			return;
		double rabCellA = rabCell.get("RabA");
		if (tMembrane < initOrgProp.get("area") || rabCellA < initOrgProp.get("area")) {
			return;
		}

		HashMap<String, Double> rabContent = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitRabContent()
						.get("kind1"));
		HashMap<String, Double> membraneContent = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitMembraneContent()
						.get("kind1"));
		HashMap<String, Double> solubleContent = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitSolubleContent()
						.get("kind1"));
		
		// new endosome incorporate 10% of the recycled mHCI
		if (PlasmaMembrane.getInstance().getMembraneRecycle().containsKey("mHCI")) {
			double valueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get("mHCI");
			double value = valueIn*0.1;
			if (value >  initOrgProp.get("area")) {value = initOrgProp.get("area");}
			membraneContent.put("mHCI", value);
			PlasmaMembrane.getInstance().getMembraneRecycle().put("mHCI", valueIn - value);
		} else
			{membraneContent.put("mHCI", 0d);}

		
		// new endosome incorporate 10% of the plasma membrane pLANCL2
		System.out.println("PLASMA MEMBRANE "+PlasmaMembrane.getInstance().getMembraneRecycle());
		if (PlasmaMembrane.getInstance().getMembraneRecycle().containsKey("pLANCL2")) {
			double valueIn = PlasmaMembrane.getInstance().getMembraneRecycle().get("pLANCL2");
			double value = valueIn * 0.1;
			if (value >  initOrgProp.get("area")) {value = initOrgProp.get("area");}
			membraneContent.put("pLANCL2", value);
			PlasmaMembrane.getInstance().getMembraneRecycle().put("pLANCL2", valueIn-value);
		} else
			{membraneContent.put("pLANCL2", 0d);}
		
		Context<Object> context = ContextUtils.getContext(endosome);

		Endosome bud = new Endosome(space, grid, rabContent, membraneContent,
				solubleContent, initOrgProp);
		context.add(bud);
		// context.add(new Endosome(space, grid, rabContent, membraneContent,
		// solubleContent));
		System.out.println(membraneContent + "NEW ENDOSOME UPTAKE"
				+ solubleContent + rabContent);
		// tMembrane = Cell.getInstance().gettMembrane();
		tMembrane = tMembrane - bud.initOrgProp.get("area");
		rabCellA = rabCellA - bud.initOrgProp.get("area");
		rabCell.put("RabA", rabCellA);
		Cell.getInstance().settMembrane(tMembrane);

		// Cell.getInstance().setRabCell(rabCell);

		bud.area = initOrgProp.get("area");
		bud.volume = initOrgProp.get("volume");
		bud.size = initOrgProp.get("radius");// radius of a sphere with the volume of the
								// cylinder
		bud.speed = Cell.orgScale/ bud.size;
		bud.heading = -90;// heading down
		// NdPoint myPoint = space.getLocation(bud);
		double rnd = Math.random();
		endosome.getSpace().moveTo(bud, rnd * 50, 50 - 2 * cellLimit);
		endosome.getGrid().moveTo(bud, (int) rnd * 50, (int) (50 - 2 * cellLimit));
	}


}
