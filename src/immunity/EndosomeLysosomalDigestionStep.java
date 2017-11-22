package immunity;

public class EndosomeLysosomalDigestionStep {

	
	public static void lysosomalDigestion(Endosome endosome) {
		// if low percentage of the membrane is RabD return
		if (!endosome.rabContent.containsKey("RabD")
				|| Math.random() > endosome.rabContent.get("RabD") / endosome.area)
			return;
		// soluble and membrane content is digested in a low percentage
		// (0.0001* proportion of RabD in the membrane)
		// volume is decreased proportional to the initial volume and also
		// considering the mvb that are digested
		double rabDratio = endosome.rabContent.get("RabD") / endosome.area;
		double volIV = 4 / 3 * Math.PI * Math.pow(Cell.rIV, 3.d);
		double deltaV = 0d;
		double initialMvb = 0d;
		double finalMvb = 0d;
		double finalSolMark = 0d;
		double finalMemMark = 0d;
		if (endosome.solubleContent.containsKey("mvb")) {
			initialMvb = endosome.solubleContent.get("mvb");
			if (Math.random() < 0.001 * rabDratio * initialMvb) {
				finalMvb = initialMvb - 1;
			} else {
				finalMvb = initialMvb;
			}
		}
		if (endosome.solubleContent.containsKey("solubleMarker")) {
				finalSolMark = endosome.solubleContent.get("solubleMarker");
			}
		if (endosome.membraneContent.containsKey("membraneMarker")) {
			finalMemMark = endosome.membraneContent.get("membraneMarker");
		}
		for (String sol : endosome.solubleContent.keySet()) {
				double solDigested = endosome.solubleContent.get(sol) * 0.0001
						* rabDratio;
				endosome.solubleContent.put(sol, endosome.solubleContent.get(sol) - solDigested);
			}
		if (endosome.solubleContent.containsKey("mvb"))
			endosome.solubleContent.put("mvb", finalMvb);
		if (endosome.solubleContent.containsKey("solubleMarker"))
			endosome.solubleContent.put("solubleMarker", finalSolMark);

		for (String mem : endosome.membraneContent.keySet()) {
				double memDigested = endosome.membraneContent.get(mem) * 0.0001
						* rabDratio;
				endosome.membraneContent
						.put(mem, endosome.membraneContent.get(mem) - memDigested);
			}
		if (endosome.membraneContent.containsKey("membraneMarker"))
			endosome.membraneContent.put("membraneMarker", finalMemMark);
			// volume is decreased
		if (endosome.solubleContent.containsKey("mvb")) {
				deltaV = (initialMvb - finalMvb) * volIV + endosome.volume * 0.0001
						* rabDratio;
			} else {
				deltaV = endosome.volume * 0.0001 * rabDratio;
			}
		endosome.volume = endosome.volume - deltaV;
		Endosome.endosomeShape(endosome);
	}
}
