package immunity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
// This class contains the properties of the cell.  It is loaded by the CellBuilder with the same
// CSV file  used for the inital organelles.  It is updated by the UpdateParameters class.
public class CellProperties {
	
	public CellProperties() {
		super();
		// TODO Auto-generated constructor stub
	}
	public static final String configFilename = "config.json";
	
	private static CellProperties instance;
	
	public static CellProperties getInstance() {
		if( instance == null ) {
			instance = new  CellProperties();
		}
		return instance;
	}
//	Cell proterties that are loaded from a csv file by the CellBuilder class
	public HashMap<String, Double> cellK = new HashMap<String, Double>();
	public HashMap<String, Double> initRabCell = new HashMap<String, Double>();
	public HashMap<String, Double> solubleCell = new HashMap<String, Double>();
	public HashMap<String, Double> initPMmembraneRecycle = new HashMap<String, Double>();
	public HashMap<String, Double> rabCompatibility = new HashMap<String, Double>();
	public HashMap<String, Double> tubuleTropism = new HashMap<String, Double>();
	public HashMap<String, Set<String>> rabTropism = new HashMap<String, Set<String>>();
	public HashMap<String, Double> mtTropism = new HashMap<String, Double>();
	public HashMap<String, Double> rabRecyProb = new HashMap<String, Double>();
	public HashMap<String, String> colorRab = new HashMap<String, String>();
	public HashMap<String, String> colorContent = new HashMap<String, String>();
	Set<String> membraneMet = new HashSet<String>();

	Set<String> solubleMet = new HashSet<String>();
	Set<String> rabSet = new HashSet<String>();
	

//	GETTERS
	
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
	public HashMap<String, Set<String>> getRabTropism() {
		return rabTropism;
	}
	public HashMap<String, Double> getMtTropism() {
		return mtTropism;
	}
	public HashMap<String, Double> getRabRecyProb() {
		return rabRecyProb;
	}
	public HashMap<String, String> getColorRab() {
		return colorRab;
	}
	public HashMap<String, String> getColorContent() {
		return colorContent;
	}
	public Set<String> getMembraneMet() {
		return membraneMet;
	}
	public Set<String> getSolubleMet() {
		return solubleMet;
	}
	public Set<String> getRabSet() {
		return rabSet;
	}
	public HashMap<String, Double> getSolubleCell() {
		return solubleCell;
	}
	public HashMap<String, Double> getInitPMmembraneRecycle() {
		return initPMmembraneRecycle;
	}
	public Set<String> getmembraneMet() {
		// TODO Auto-generated method stub
		return null;
	}
}
