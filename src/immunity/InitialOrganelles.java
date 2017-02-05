package immunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InitialOrganelles {

	private static InitialOrganelles instance;

	public static InitialOrganelles getInstance() {
		if (instance == null) {
			instance = new InitialOrganelles();

		}
		return instance;
	}

	public ArrayList<String> diffOrganelles = new ArrayList<String>();
	public HashMap<String, HashMap<String, Double>> initOrgProp = new HashMap<String, HashMap<String, Double>>();
	public HashMap<String, HashMap<String, Double>> initRabContent = new HashMap<String, HashMap<String, Double>>();
	public HashMap<String, HashMap<String, Double>> initSolubleContent = new HashMap<String, HashMap<String, Double>>();
	public HashMap<String, HashMap<String, Double>> initMembraneContent = new HashMap<String, HashMap<String, Double>>();

	public void loadMaps() {
		for (int i = 1; i < 6; i++) {
			initOrgProp.put("kind" + i, null);
			initRabContent.put("kind" + i, null);
			initSolubleContent.put("kind" + i, null);
			initMembraneContent.put("kind" + i, null);
		}

	}

	public ArrayList<String> getDiffOrganelles() {
		return diffOrganelles;
	}

	public HashMap<String, HashMap<String, Double>> getInitOrgProp() {
		return initOrgProp;
	}

	public HashMap<String, HashMap<String, Double>> getInitRabContent() {
		return initRabContent;
	}

	public HashMap<String, HashMap<String, Double>> getInitSolubleContent() {
		return initSolubleContent;
	}

	public HashMap<String, HashMap<String, Double>> getInitMembraneContent() {
		return initMembraneContent;
	}

}
