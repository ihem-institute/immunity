package immunity;

import java.util.HashMap;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class EndosomeInternalVesicleStep {
	
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	

	public static void internalVesicle(Endosome endosome) {	
		space = endosome.getSpace();
		grid = endosome.getGrid();
	
	double vo = endosome.volume;
	double so = endosome.area;
	// if it a sphere, cannot form an internal vesicle
	//System.out.println("ESFERA" + so * so * so / (vo * vo));
	if (so * so * so / (vo * vo) <= 36.001 * Math.PI)
		return;
	//System.out.println("s/v ratio total" + so * so * so / (vo * vo));
	// if s^3 / v^2 is equal to 36*PI then it is an sphere and cannot form a
	// tubule

	double rIV = Cell.rIV; // Internal vesicle radius
	double vIV = 4 / 3 * Math.PI * Math.pow(rIV, 3); // volume 14137.16
	double sIV = 4 * Math.PI * Math.pow(rIV, 2);// surface 2827.43

	if (vo < 2 * vIV)
		return;
	if (so < 2 * sIV)
		return;
	double vp = vo + vIV;
	double sp = so - sIV;
	if (sp * sp * sp / (vp * vp) <= 36 * Math.PI){ 
	//	System.out.println("s/v ratio available " + sp * sp * sp / (vp * vp));
		return;// if the resulting surface cannot embrance the resulting
				// volume
	}
	double rsphere = Math.pow((0.75 * vp / Math.PI), (1 / 3));
	double ssphere = 4 * Math.PI * Math.pow(rsphere, 2);
	if (ssphere >= sp)
		return;
	// double scylinder = so - ssphere;
	// if (scylinder < sIV * 1.10) return;//if the available membrane is
	// less than the surface of an internal vesicle, stop.
	// the factor 1.01 is to account for an increase in the surface required
	// because the increase in volume
	endosome.area = endosome.area - sIV;
	endosome.volume = endosome.volume + vIV;
	//endosomeShape(endosome);
	if (endosome.solubleContent.containsKey("mvb")) {
		double content = endosome.solubleContent.get("mvb") + 1;
		endosome.solubleContent.put("mvb", content);
	} else {
		endosome.solubleContent.put("mvb", 1d);
	}
	// Rabs proportinal to the sIV versus the surface of the organelle (so)
	// are released into the cytosol
	HashMap<String, Double> rabCell = Cell.getInstance().getRabCell();
	for (String key1 : endosome.rabContent.keySet()) {
		if (rabCell.containsKey(key1)) {
			double sum = endosome.rabContent.get(key1) * sIV / so
					+ rabCell.get(key1);
			rabCell.put(key1, sum);
		} else {
			double sum = endosome.rabContent.get(key1) * sIV / so;
			rabCell.put(key1, sum);
		}
	}

	// Cell.getInstance().setRabCell(rabCell);
	System.out.println("Rab Cellular Content"
			+ Cell.getInstance().getRabCell());
	// Rabs released to the cytosol are substracted from the rabContent of
	// the organelle
	for (String rab : endosome.rabContent.keySet()) {
		double content1 = endosome.rabContent.get(rab) * (so - sIV) / so;
		endosome.rabContent.put(rab, content1);
	}
	// Membrane content with mvb tropism is degraded (e.g. EGF)
	//this can be established in RabTropism adding in the EGF tropisms "mvb",
	for (String content : endosome.membraneContent.keySet()) {
		if (CellProperties.getInstance().getRabTropism().get(content).contains("mvb")) {
			double mem = endosome.membraneContent.get(content) - sIV;
			if (mem <= 0)
				mem = 0d;
			endosome.membraneContent.put(content, mem);
//			If not special tropism, the membrane content is incorporated 
//			into the internal vesicle proportional to the surface and degraded
		} else {
			double mem = endosome.membraneContent.get(content) * (so - sIV)
					/ so;
			endosome.membraneContent.put(content, mem);
		}
	}
	// Free membrane is added to the cell
	double cellMembrane = Cell.getInstance().gettMembrane() + sIV;
	Cell.getInstance().settMembrane(cellMembrane);
}

}
