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
		// new endosome incorporate 10% of the recycled mHCI
		if (PlasmaMembrane.getInstance().membraneRecycle.containsKey("mHCI")) {
			double value = PlasmaMembrane.getInstance().membraneRecycle.get("mHCI");
			membraneContent.put("mHCI", 0.1 * value);
		} else
			membraneContent.put("mHCI", 0d);
		HashMap<String, Double> solubleContent = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitSolubleContent()
						.get("kind1"));
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
