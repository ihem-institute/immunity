package immunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
//	This class contains the properties of the initial organelles.  It is modified
// by the CellProperties that gets the values from a CSV file 

public class InitialOrganelles {

	private static InitialOrganelles instance;

	public static InitialOrganelles getInstance() {
		if (instance == null) {
			instance = new InitialOrganelles();
		}
		return instance;
	}

	public HashSet<String> diffOrganelles= new HashSet<String>();
	public HashMap<String, HashMap<String, Double>> initOrgProp = new HashMap<String, HashMap<String, Double>>();
	public HashMap<String, HashMap<String, Double>> initRabContent = new HashMap<String, HashMap<String, Double>>();
	public HashMap<String, HashMap<String, Double>> initSolubleContent = new HashMap<String, HashMap<String, Double>>();
	public HashMap<String, HashMap<String, Double>> initMembraneContent = new HashMap<String, HashMap<String, Double>>();
	

	public HashSet<String> getDiffOrganelles() {
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
//		return new HashMap<String, HashMap<String, Double>>(initMembraneContent);
	}

	public String getInitialSolubleContent() {
		// TODO Auto-generated method stub
		return null;
	}

}
