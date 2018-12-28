package immunity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.lang3.RandomStringUtils;

import repast.simphony.context.space.grid.ContextGrid;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.collections.IndexedIterable;
// This class contains the properties of the cell.  It is loaded with the same
// CSV file  used for the inital organelles.  It is updated by the UpdateParameters class.
public class FreezeDryEndosomes {
	
//	public frozenEndosomes() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
	public static final String configFilename = "config.json";
	
	private static FreezeDryEndosomes instance;
	//CAMBIO
	LocalPath mainpath=new LocalPath(); 
	String FreezeOutputPath = mainpath.getPathOutputFE(); 	
	
	public static FreezeDryEndosomes getInstance() {
		if( instance == null ) {
			instance = new  FreezeDryEndosomes();
			try {
				FreezeDryEndosomes.loadFromCsv(instance);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return instance;
	}
////	FreezeDryEndosomes are loaded from a csv file and use by the CellBuilder class
//	public HashMap<String, Double> cellK = new HashMap<String, Double>();
//	public HashMap<String, Double> initRabCell = new HashMap<String, Double>();
//	public HashMap<String, Double> solubleCell = new HashMap<String, Double>();
//	public HashMap<String, Double> initPMmembraneRecycle = new HashMap<String, Double>();
//	public HashMap<String, Double> rabCompatibility = new HashMap<String, Double>();
//	public HashMap<String, Double> tubuleTropism = new HashMap<String, Double>();
//	public HashMap<String, Set<String>> rabTropism = new HashMap<String, Set<String>>();
//	public HashMap<String, Double> mtTropism = new HashMap<String, Double>();
//	public HashMap<String, Double> rabRecyProb = new HashMap<String, Double>();
//	public HashMap<String, String> colorRab = new HashMap<String, String>();
//	public HashMap<String, String> colorContent = new HashMap<String, String>();
//	public HashMap<String, Double> membraneMet = new HashMap<String, Double>();
//
//	Set<String> solubleMet = new HashSet<String>();
//	Set<String> rabSet = new HashSet<String>();
	

//	GETTERS
//	
//	public HashMap<String, Double> getCellK() {
//		return cellK;
//	}
//	public HashMap<String, Double> getInitRabCell() {
//		return initRabCell;
//	}
//	public HashMap<String, Double> getRabCompatibility() {
//		return rabCompatibility;
//	}
//	public HashMap<String, Double> getTubuleTropism() {
//		return tubuleTropism;
//	}
//	public HashMap<String, Set<String>> getRabTropism() {
//		return rabTropism;
//	}
//	public HashMap<String, Double> getMtTropism() {
//		return mtTropism;
//	}
//	public HashMap<String, Double> getRabRecyProb() {
//		return rabRecyProb;
//	}
//	public HashMap<String, String> getColorRab() {
//		return colorRab;
//	}
//	public HashMap<String, String> getColorContent() {
//		return colorContent;
//	}
//	// membraneMet was originally a string set.  Now is a hashmap because
//	// the probability of internalization from PM was added.  So, there is
//	// two getters, one that return the keySet and another that return the hashmap
//	public Set<String> getMembraneMet() {
//		return membraneMet.keySet();
//	}
//	public HashMap<String, Double> getMembraneMetRec() {
//		return membraneMet;
//	}
//	public Set<String> getSolubleMet() {
//		return solubleMet;
//	}
//	public Set<String> getRabSet() {
//		return rabSet;
//	}
//	public HashMap<String, Double> getSolubleCell() {
//		return solubleCell;
//	}
//	public HashMap<String, Double> getInitPMmembraneRecycle() {
//		return initPMmembraneRecycle;
//	}
//	public Set<String> getmembraneMet() {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	public static void loadFromCsv(FreezeDryEndosomes frozenEndosomes) throws IOException {

		Scanner scanner = new Scanner(new File(
				"inputFrozenEndosomes.csv"));
		scanner.useDelimiter(",");

//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			frozenEndosomes config = objectMapper.readValue(new File(frozenEndosomes.configFilename), frozenEndosomes.class);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		// InitialOrganelles InOr = InitialOrganelles.getInstance();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] b = line.split(",");
			String subString = b[0].substring(0,2);
			switch (subString) {

			// INITIAL ORGANELLES kind 7 is for phagosomes
 // if the first two letters are "en", load data for an endosome
			case "en":
			{
				InitialOrganelles inOr = InitialOrganelles.getInstance();
//				System.out.println("AQUI PARA b0  "+b[0]);
				inOr.getDiffOrganelles().add(b[0]);
//				System.out.println("AQUI PARA  "+b[1]);
				switch (b[1]) {
				case "initOrgProp": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					inOr.getInitOrgProp().put(b[0], value);
					break;
				}
				case "initRabContent": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
//						System.out.println("AQUI PARA  "+b[i]+" "+ b[i + 1]);
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					inOr.getInitRabContent().put(b[0], value);
					break;
				}
				case "initSolubleContent": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					inOr.getInitSolubleContent().put(b[0], value);
					break;
				}
				case "initMembraneContent": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
	//					System.out.println("VALOR MALO" + b[i] + "" + b[i+1]);
						value.put(b[i], Double.parseDouble(b[i + 1]));
					}
					inOr.getInitMembraneContent().put(b[0], value);
					break;
				}
				default: {
					System.out.println("no a valid entry");
				}
				}
				break;
			}


			default: {
				System.out.println("no a valid entry");
			}
			}

		}
		scanner.close();
//		System.out.println(frozenEndosomes.solubleMet);
//		System.out.println(frozenEndosomes.tubuleTropism);
	}
	
	
	
	public void writeToCsv() throws IOException {
		
		IndexedIterable<Endosome> collection = CellBuilder.getCollection();
//		System.out.println("ALL ENDOSOMES"+collection);
		int index = 0;
		Writer output;	
	    double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		String line ="tick " + tick + "\n";
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/outputFrozenEndosomes.csv", true));
		output = new BufferedWriter(new FileWriter(FreezeOutputPath, true));
		output.append(line);
		output.close();
		for (Endosome endosome : collection) {
			line = "";
			String ch = RandomStringUtils.randomAlphabetic(1);
            line = line + "endosome"+index + ch + ",";
            line = line + "initOrgProp" + ",";
            line = line + "area" + "," + endosome.getArea()  + ",";
            line = line + "volume" + "," + endosome.getVolume() + ",";
            line = line + "xcoor" + "," + endosome.getXcoor() + ",";
            line = line + "ycoor" + "," + endosome.getYcoor() + ",";
		line = line + "\n";	
		output = new BufferedWriter(new FileWriter(FreezeOutputPath, true));
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/outputFrozenEndosomes.csv", true));
		output.append(line);
		line = "";
        line = line + "endosome"+index + ch + ",";
        String rabContent = endosome.getRabContent().toString().replace("=",",");
        rabContent = rabContent.replace("{","");
        rabContent = rabContent.replace("}","");
        rabContent = rabContent.replace(" ","");
        line = line + "initRabContent" + "," + rabContent;
        line = line + "\n";
//		Writer output;
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
		output.append(line);
		
		line = "";  
        line = line + "endosome"+index + ch + ",";
		String membraneContent = endosome.getMembraneContent().toString().replaceAll("=",","); 
		membraneContent = membraneContent.replace("{","");
		membraneContent = membraneContent.replace("}","");
        membraneContent = membraneContent.replace(" ","");
        line = line + "initMembraneContent" + "," + membraneContent;
        line = line + "\n";
//		Writer output;
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
		output.append(line);
		
		line = "";  
        line = line + "endosome"+index + ch + ",";        
        String solubleContent = endosome.getSolubleContent().toString().replaceAll("=",",");        
		solubleContent = solubleContent.replace("{","");
		solubleContent = solubleContent.replace("}","");
        solubleContent = solubleContent.replace(" ","");
        line = line + "initSolubleContent" + "," + solubleContent;
        line = line + "\n";
//		Writer output;
//		output = new BufferedWriter(new FileWriter("C:/Users/lmayo/workspace/immunity/ResultsIntrTransp3.csv", true));
		output.append(line);
		index = index + 1;
		output.close();
	}
	}
	
}
