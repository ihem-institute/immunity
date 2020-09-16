package immunity;

public class EndosomeAssessCompatibility {
	
	public static double compatibles(Endosome endosome1, Endosome endosome2) {
		double sum = 0;
		if (!endosome2.initOrgProp.containsKey("empty")){
			endosome2.initOrgProp.put("empty", 0d);	// empty = false
		}
		double empty = endosome2.initOrgProp.get("empty");
		for (String key1 : endosome1.rabContent.keySet()) {
			for (String key2 : endosome2.rabContent.keySet()) {
				if (empty ==0){ // empty is false so it has content and should fuse forward
					double comp = getCompatibility(key1, key2)
							* endosome1.rabContent.get(key1) / endosome1.area
							* endosome2.rabContent.get(key2) / endosome2.area;
					sum = sum + comp;
				}
				else { // empty is true so it has  no content and should fuse backward
//					if (!(key1 == key2)) continue; // new rule to make BW transport only homotypic (relax the bck transport)
					double comp = getCompatibility(key2, key1)
							* endosome1.rabContent.get(key1) / endosome1.area
							* endosome2.rabContent.get(key2) / endosome2.area;
					sum = sum + comp;
				}

			}
		}
		// compatibility is a value between 0 and 1. Fusion
		// occurs with a probability proportional to the compatibility

		//	System.out.println("COMBATIBILIDAD  "+sum +" "+endosome1.rabContent + " "+ endosome2.rabContent);
		return sum;
	}

	private static double getCompatibility(String rabX, String rabY) {
		if (!CellProperties.getInstance().rabCompatibility.containsKey(rabX + rabY)
				&& !CellProperties.getInstance().rabCompatibility.containsKey(rabY + rabX))
			return 0;
		if (CellProperties.getInstance().rabCompatibility.containsKey(rabX + rabY)) {
			// System.out.println("COMPATIB");
			// System.out.println(rabCompatibility.get(rabX+rabY));
			return CellProperties.getInstance().rabCompatibility.get(rabX + rabY);

		} else {

			return CellProperties.getInstance().rabCompatibility.get(rabY + rabX);
		}
	}

}
