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
		// System.out.println("CELL RAB "+ rabCell+ "TMEMBRANE "+ tMembrane);
		if (!rabCell.containsKey("RabE"))
			return;
		double rabCellE = rabCell.get("RabE");
		// same area than an newly formed endosome but with half the volume
		if (tMembrane < Cell.sEndo || rabCellE < Cell.sEndo) {
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
		HashMap<String, Double> initOrgProp = new HashMap<String, Double>(
				InitialOrganelles.getInstance().getInitOrgProp().get("kind5"));

		Context<Object> context = ContextUtils.getContext(endosome);
		Endosome bud = new Endosome(space, grid, rabContent, membraneContent,
				solubleContent, initOrgProp);
		context.add(bud);
		// context.add(new Endosome(space, grid, rabContent, membraneContent,
		// solubleContent));
		System.out.println(membraneContent + "NEW ENDOSOME FROM ER"
				+ solubleContent + rabContent);
		// tMembrane = Cell.getInstance().gettMembrane();
		tMembrane = tMembrane - Cell.sEndo;
		rabCellE = rabCellE - Cell.sEndo;
		rabCell.put("RabE", rabCellE);
		Cell.getInstance().settMembrane(tMembrane);

		// Cell.getInstance().setRabCell(rabCell);

		bud.area = bud.initOrgProp.get("area");
		bud.volume = bud.initOrgProp.get("volume");
		bud.size = Cell.rEndo;// radius of a sphere with the volume of the new organelle
		bud.speed = Cell.orgScale/ bud.size;
		bud.heading = Math.random() * 360;
		// NdPoint myPoint = space.getLocation(bud);
		double rnd = Math.random();
		space.moveTo(bud, rnd * 50, 25.0);
		grid.moveTo(bud, (int) rnd * 50, 25);

		// moveTowards();
		// moveTowards();

	}

}
