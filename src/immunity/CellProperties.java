package immunity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CellProperties {
	private static CellProperties instance;
	
	public static CellProperties getInstance() {
		if( instance == null ) {
			instance = new  CellProperties();
		}
		return instance;
	}
	public HashMap<String, Double> cellK = new HashMap<String, Double>();
	public HashMap<String, Double> initRabCell = new HashMap<String, Double>();
	public HashMap<String, Double> rabCompatibility = new HashMap<String, Double>();
	public HashMap<String, Double> tubuleTropism = new HashMap<String, Double>();
	public HashMap<String, List<String>> rabTropism = new HashMap<String, List<String>>();
	public HashMap<String, Double> mtTropism = new HashMap<String, Double>();
	List<String> membraneMet = new ArrayList<String>();
	List<String> solubleMet = new ArrayList<String>();
	List<String> rabSet = new ArrayList<String>();
	//HashMap<String, Double> cellRab = new HashMap<String, Double>();
	
	public HashMap<String, Double> getCellK() {
		return cellK;
	}
	public HashMap<String, Double> getInitRabCell() {
		return initRabCell;
	}
	public HashMap<String, Double> getRabCompatibility() {
		return rabCompatibility;
	}
	public HashMap<String, Double> getTubuleTropism() {
		return tubuleTropism;
	}
	public HashMap<String, List<String>> getRabTropism() {
		return rabTropism;
	}
	public HashMap<String, Double> getMtTropism() {
		return mtTropism;
	}
	public List<String> getMembraneMet() {
		return membraneMet;
	}
	public List<String> getSolubleMet() {
		return solubleMet;
	}
	public List<String> getRabSet() {
		return rabSet;
	}
}
