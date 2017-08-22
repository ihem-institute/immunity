package immunity;

import java.util.HashMap;

import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class EndosomeNewFromERStep {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	

	public static void newFromEr(Endosome endosome) {	
		space = endosome.getSpace();
		grid = endosome.getGrid();
		
		
		Cell cell = Cell.getInstance();
		double tMembrane = cell.gettMembrane();
		HashMap<String, Double> rabCell = cell.getRabCell();
		HashMap<String, Double> initOrgProp = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitOrgProp().get("kind5"));
		// System.out.println("CELL RAB "+ rabCell+ "TMEMBRANE "+ tMembrane);
		if (!rabCell.containsKey("RabE"))
			return;
//		double rabCellE = rabCell.get("RabE");
//		NEW RAB NET. Since the rabs are supplied, RabEcyto only can decrease with a KO
		// same area than an newly formed endosome but with half the volume

		if (tMembrane < initOrgProp.get("area")) {
			return;
		}

		HashMap<String, Double> rabContent = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitRabContent()
						.get("kind5"));
		HashMap<String, Double> membraneContent = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitMembraneContent()
						.get("kind5"));
		HashMap<String, Double> solubleContent = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitSolubleContent()
						.get("kind5"));
		Context<Object> context = ContextUtils.getContext(endosome);
		Endosome bud = new Endosome(space, grid, rabContent, membraneContent,
				solubleContent, initOrgProp);
		context.add(bud);
		// context.add(new Endosome(space, grid, rabContent, membraneContent,
		// solubleContent));
		System.out.println(membraneContent + "NEW ENDOSOME FROM ER"
				+ solubleContent + rabContent);
		// tMembrane = Cell.getInstance().gettMembrane();
		tMembrane = tMembrane - initOrgProp.get("area");
//		I decrease cytoRabE by 90%.   It will be recovered during RabConversion where
//		it is constantly supplied		
//		rabCellE = rabCellE * 0.1; //bud.initOrgProp.get("area");
//		rabCellE = rabCellE - initOrgProp.get("area");
//		rabCell.put("RabE", rabCellE);
		Cell.getInstance().settMembrane(tMembrane);

		// Cell.getInstance().setRabCell(rabCell);

		bud.area = bud.initOrgProp.get("area");
		bud.volume = bud.initOrgProp.get("volume");
		bud.size = bud.initOrgProp.get("radius");// radius of a sphere with the volume of the new organelle
		bud.speed = 1d/ bud.size;
		bud.heading = Math.random() * 360;
		// NdPoint myPoint = space.getLocation(bud);
		double rnd = Math.random();
		space.moveTo(bud, rnd * 50, 25.0);
		grid.moveTo(bud, (int) rnd * 50, 25);

		// moveTowards();
		// moveTowards();

	}

}
