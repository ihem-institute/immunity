package immunity;

import java.util.HashMap;
import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;

public class EndosomeSplitStep {
	// Split the endosome in two
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	static double cellLimit = 3 * Cell.orgScale;
	
	public static void split(Endosome endosome) {	
		space = endosome.getSpace();
		grid = endosome.getGrid();

		
		String rabInTube = null;
		double vo = endosome.volume;
		double so = endosome.area;
		if (vo < 2 * 2 * Math.PI * Cell.rcyl * Cell.rcyl * Cell.rcyl)
			return; // if too small to form two mincyl do not split. Volume of a cylinder of 2
					// cylinder radius long (almost a sphere)
		if (so < 2 * Cell.mincyl)
			return; // if the surface is less than two minimus tubules, abort
					// splitting
		if (so * so * so / (vo * vo) <= 36.01 * Math.PI)
			return; // organelle is an sphere
		// if s^3 / v^2 is equal to 36*PI then it is an sphere and cannot form a
		// tubule
		// if (vo / (so - 2 * Math.PI * Cell.rcyl * Cell.rcyl) <= Cell.rcyl / 2)
		// return;
		// too small volume for the surface for a tubule. Cannot form a tubule
		if (vo / (so - 2 * Math.PI * Cell.rcyl * Cell.rcyl) == Cell.rcyl / 2) {
			System.out.println("tubule");
		}

		double rsphere = Math.pow((vo * 3) / (4 * Math.PI), (1 / 3d));// calculate
		// the radius of the sphere with a given volume

		double ssphere = (4 * Math.PI * rsphere * rsphere);// area of a sphere
															// containing the
															// volume
		if ((so - ssphere) < Cell.mincyl)
			return; // if not enough surface to contain the volume plus a
		// minimum tubule, no split
		rabInTube = rabInTube(endosome); // select a rab for the tubule
		if (rabInTube == null)
			return; // if non is selected, no fission

		/* initial minimum tubule to be formed */
		double scylinder = Cell.mincyl; // surface minimum cylinder 2*radius
										// cylinder high
		double vcylinder = 2 * Math.PI * Math.pow(Cell.rcyl, 3); // volume
		// minimum cylinder

		while ((so - ssphere - scylinder > 4 * Math.PI * Math.pow(Cell.rcyl, 2))
				// organelle area should be enough to cover the volume (ssphere)
				// to cover the cylinder already formed (scylinder) and to
				// elongate a two r cylinder (without caps)
				&& (endosome.rabContent.get(rabInTube) - scylinder > 4 * Math.PI
						* Math.pow(Cell.rcyl, 2))// the Rab area should b enough
				// to cover the minimum cylinder and to elongate a two r cylider
				&& (scylinder < 0.5 * so) // the area of the cylinder must not
											// be larger than 50% of the total
											// area
				&& ((vo - vcylinder - 2 * Math.PI * Math.pow(Cell.rcyl, 3))>2 * Math.PI * Math.pow(Cell.rcyl, 3))
				) {
//			/ ((so - scylinder - 4 * Math.PI
//					* Math.pow(Cell.rcyl, 2)) - 2 * Math.PI
//					* Cell.rcyl * Cell.rcyl) > Cell.rcyl / 2
			// volume left cannot be smaller than the volume
			// of the mincyl
			/*
			 * while there is enough membrane and enough rab surface, the tubule
			 * grows
			 */

			scylinder = scylinder + 4 * Math.PI * Math.pow(Cell.rcyl, 2);
			// add a cylinder without caps (the caps were considered in
			// the mincyl
			vcylinder = vcylinder + 2 * Math.PI * Math.pow(Cell.rcyl, 3);
			// add a volume
			// System.out.println(scylinder +"surface and volume"+ vcylinder);
		}

		/*
		 * the volume of the vesicle is formed subtracting the volume of the
		 * formed cylinder from the total volume idem for the area
		 * 
		 * From the information of vcylinder and scylinder, the organelle is
		 * splitted in two, a sphere and tubule (case 2) or in two almost
		 * tubules (a pice of the lateral surface must be used to close the
		 * tubules
		 */
		double vVesicle = vo - vcylinder;
		if(vVesicle < 0 || vcylinder < 0){
			System.out.println(vVesicle +"surface and volume"+ vcylinder);	
		}
		double sVesicle = so - scylinder;
		/*
		 * FORMATION 1ST ORGANELLE (referred as sphere) the rab-in-tubule of the
		 * tubule is substracted from the original rab-in-tube content of the
		 * organelle the final proportion of the rab-in-tubule in the vesicular
		 * organelle is obtained dividein by the total surface of the vesicle
		 */
		endosome.area = sVesicle;
		endosome.volume = vVesicle;
		Endosome.endosomeShape(endosome);

		/*
		 * CONTENT DISTRIBUTION Rab in the tubule is sustracted
		 */
		double rabLeft = endosome.rabContent.get(rabInTube) - scylinder;
		if (rabLeft < 0) {
			System.out.println(rabInTube + endosome.rabContent.get(rabInTube)
					+ "surfaceCy" + scylinder);
			System.out.println(endosome.rabContent);
		}
		endosome.rabContent.put(rabInTube, rabLeft);

		// MEMBRANE CONTENT IS DISTRIBUTED according rabTropism
		HashMap<String, Double> copyMembrane = new HashMap<String, Double>(
				endosome.membraneContent);
		// copyMembrane.putAll(endosome.membraneContent);
		for (String content : copyMembrane.keySet()) {
			if (!CellProperties.getInstance().getRabTropism().containsKey(content)
					|| !CellProperties.getInstance().getRabTropism().get(content).contains(rabInTube)) {// not a
				// specified tropism or no tropism for the rabInTube
				// hence, distribute according to
				// the surface ratio
				// For a membrane marker with no tropism for endosome split process,
				// the marker is at random located in one or
				// the other endosome, according to the sVesicle/so ratio
				if (content.equals("membraneMarker")
						&& (endosome.membraneContent.get("membreneMarker") > 0.9)) {
					if (Math.random() < sVesicle / so)
						endosome.membraneContent.put(content, 1.d);
				} else {
					endosome.membraneContent.put(content, copyMembrane.get(content)
							* (sVesicle) / so);
				}

			} else {
				if (CellProperties.getInstance().getRabTropism().get(content).contains(rabInTube)
						|| CellProperties.getInstance().getRabTropism().get(content).contains("1")) {// a tropism
					// is specified.
					// If it is "1" always goes to the tubule.
					// if it is not "1" but has tropism the Rab forming the
					// tubule, goes
					// to the tubule

					if (copyMembrane.get(content) > scylinder) {
						endosome.membraneContent.put(content,
								copyMembrane.get(content) - scylinder);
					} else
						endosome.membraneContent.put(content, 0.0d);
				}
				if (CellProperties.getInstance().getRabTropism().get(content).contains("0")) {// if the tropism
					// is "0" goes to the sphere
					if (copyMembrane.get(content) > sVesicle) {
						endosome.membraneContent.put(content, sVesicle);
					} else
						endosome.membraneContent.put(content,
								copyMembrane.get(content));
				}
			}
		}
		// SOLUBLE CONTENT IS DISTRIBUTED according rabTropism
		HashMap<String, Double> copySoluble = new HashMap<String, Double>(
				endosome.solubleContent);
		// copySoluble.putAll(endosome.solubleContent);
		for (String content : copySoluble.keySet()) {
			if (!CellProperties.getInstance().getRabTropism().containsKey(content)
					|| !CellProperties.getInstance().getRabTropism().get(content).contains(rabInTube)) {// not a
				// specified tropism or no tropism for the rabInTube,
				// hence, distribute according to
				// the volume ratio
				if (content.equals("solubleMarker")
						&& (endosome.solubleContent.get("solubleMarker") > 0.9)) {
					if (Math.random() < vVesicle / vo)
						endosome.solubleContent.put(content, 1.d);
				} else {
					endosome.solubleContent.put(content, copySoluble.get(content)
							* (vVesicle) / vo);
				}
			} else { // a tropism is specified. If it is "1" always goes to the
						// tubule.
				// if it is not "1" but is the Rab forming the tubule, goes to
				// the tubule
				if (CellProperties.getInstance().getRabTropism().get(content).contains(rabInTube)
						|| CellProperties.getInstance().getRabTropism().get(content).contains("1")) {

					if (copySoluble.get(content) > vcylinder) {
						endosome.solubleContent.put(content,
								copySoluble.get(content) - vcylinder);
					} else
						endosome.solubleContent.put(content, 0.0d);
				}
				if (CellProperties.getInstance().getRabTropism().get(content).contains("0")) { // if the tropism
																// is "0" goes
																// to the sphere

					if (copySoluble.get(content) > vVesicle) {
						endosome.solubleContent.put(content, vVesicle);
					} else
						endosome.solubleContent.put(content,
								copySoluble.get(content));
				}
			}

		}
		endosome.size = Math.pow(endosome.volume * 3d / 4d / Math.PI, (1d / 3d));
//		NEW CALL TO ACTUALIZE RAB CONVERSION IN THE NEW CONDITION
//		EndosomeRabConversionStep.rabConversion(endosome);
		endosome.speed = Cell.orgScale / endosome.size;
//		EndosomeAntigenPresentationStep.antigenPresentation(endosome);
		endosome.getAntigenTimeSeries().clear();
		endosome.getLANCL2TimeSeries().clear();

		// moveTowards();

		/* the tubule is created as an independent endosome */
		HashMap<String, Double> newRabContent = new HashMap<String, Double>();
		newRabContent.put(rabInTube, scylinder);
		HashMap<String, Double> newInitOrgProp = new HashMap<String, Double>();
		newInitOrgProp.put("area", scylinder);
		newInitOrgProp.put("volume", vcylinder);
		HashMap<String, Double> newMembraneContent = new HashMap<String, Double>();
		for (String content : copyMembrane.keySet()) {
			newMembraneContent.put(content, copyMembrane.get(content)
					- endosome.membraneContent.get(content));
		}
		HashMap<String, Double> newSolubleContent = new HashMap<String, Double>();
		for (String content : copySoluble.keySet()) {
			newSolubleContent.put(content, copySoluble.get(content)
					- endosome.solubleContent.get(content));
		}
		Endosome b = new Endosome(endosome.getSpace(), endosome.getGrid(), newRabContent,
				newMembraneContent, newSolubleContent, newInitOrgProp);
		Context<Object> context = ContextUtils.getContext(endosome);
		context.add(b);
		b.area = scylinder;
		b.volume = vcylinder;
		Endosome.endosomeShape(b);
		b.size = Math.pow(b.volume * 3d / 4d / Math.PI, (1d / 3d));
		b.speed = Cell.orgScale / b.size;
		Random rd = new Random();
//		EndosomeAntigenPresentationStep.antigenPresentation(b);
		b.getAntigenTimeSeries().clear();
		b.getLANCL2TimeSeries().clear();
		b.heading = endosome.heading + rd.nextGaussian() * 10d;// change the
															// heading
		// of the old vesicle heading with a normal distribution
		// System.out
		// .println("                                                 VESICLE B");
		// System.out.println(b.area + " " + b.rabContent + " "
		// + b.membraneContent + " " + b.solubleContent + " "
		// + b.initOrgProp);
//		scale 750 nm is the 50 size space. Size in nm/15 is the size in the space scale
		double deltax = Math.cos(endosome.heading * 2d * Math.PI / 360d)
				* (endosome.size + b.size) * Cell.orgScale/15;
		double deltay = Math.sin(endosome.heading * 2d * Math.PI / 360d)
				* (endosome.size+ b.size)* Cell.orgScale/15;
		
		NdPoint myPoint = space.getLocation(endosome);
		double x = myPoint.getX()+ deltax;

		double y = myPoint.getY()+ deltay;
		if (y < cellLimit)y= cellLimit;
		if (y > 50 - cellLimit)y = 50-cellLimit;
		space.moveTo(b, x, y);
		grid.moveTo(b, (int) x, (int) y);
		//moveTowards();

	}

	public static String rabInTube(Endosome endosome) {
		HashMap<String, Double> copyMap = new HashMap<String, Double>(
				endosome.rabContent);
		// copyMap.putAll(endosome.rabContent);
		String rab = null;
		// System.out.println("CopyMap "+copyMap);
		for (String rab1 : endosome.rabContent.keySet()) {
			if (copyMap.get(rab1) < Cell.mincyl) {
				copyMap.remove(rab1);
			}
		}
		if (copyMap.isEmpty()) {
			System.out.println("NINGUN RAB" + copyMap);
			return null;
		}

		if (copyMap.size() < 2) {

			for (String rab1 : copyMap.keySet()) {
				System.out.println("UNICO RAB " + copyMap);
				return rab1;
			}
		}

		else {
			while (rab == null) {
				for (String rab1 : copyMap.keySet()) {
//					System.out.println(rab1 + " "+ tubuleTropism);
					if (Math.random() < CellProperties.getInstance().getTubuleTropism().get(rab1)) {
						System.out.println(copyMap + "RabInTubeSelected" +
						rab1);
						return rab1;
					}
				}
			}
		}

		return null;
	}

}
