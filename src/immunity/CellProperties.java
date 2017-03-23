package immunity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
// This class contains the properties of the cell.  It is loaded by the CellBuilder with the same
// CSV file  used for the inital organelles.  It is updated by the UpdateParameters class.
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
	public HashMap<String, Set<String>> rabTropism = new HashMap<String, Set<String>>();
	public HashMap<String, Double> mtTropism = new HashMap<String, Double>();
	public HashMap<String, String> colorRab = new HashMap<String, String>();
	public HashMap<String, String> colorContent = new HashMap<String, String>();
	Set<String> membraneMet = new HashSet<String>();
	Set<String> solubleMet = new HashSet<String>();
	Set<String> rabSet = new HashSet<String>();
	
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
	public HashMap<String, Set<String>> getRabTropism() {
		return rabTropism;
	}
	public HashMap<String, Double> getMtTropism() {
		return mtTropism;
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
}
