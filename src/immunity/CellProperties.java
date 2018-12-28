package immunity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
// This class contains the properties of the cell.  It is loaded with the same
// CSV file  used for the inital organelles.  It is updated by the UpdateParameters class.
public class CellProperties {
	
//	public CellProperties() {
//		super();
//		// TODO Auto-generated constructor stub
//	}
	public static final String configFilename = "config.json";
	
	private static CellProperties instance;
	
	public static CellProperties getInstance() {
		if( instance == null ) {
			instance = new  CellProperties();
			try {
				CellProperties.loadFromCsv(instance);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		return instance;
	}
//	Cell proterties that are loaded from a csv file by the CellBuilder class
	public HashMap<String, Double> cellK = new HashMap<String, Double>();
	public HashMap<String, Double> cellAgentProperties = new HashMap<String, Double>();
	public HashMap<String, Double> plasmaMembraneProperties = new HashMap<String, Double>();
	public HashMap<String, Double> initRabCell = new HashMap<String, Double>();
	public HashMap<String, Double> solubleCell = new HashMap<String, Double>();
	public HashMap<String, Double> initPMmembraneRecycle = new HashMap<String, Double>();
	public HashMap<String, Double> initPMsolubleRecycle = new HashMap<String, Double>();
	public HashMap<String, Double> rabCompatibility = new HashMap<String, Double>();
	public HashMap<String, Double> tubuleTropism = new HashMap<String, Double>();
	public HashMap<String, Set<String>> rabTropism = new HashMap<String, Set<String>>();
	public HashMap<String, Double> mtTropismTubule = new HashMap<String, Double>();
	public HashMap<String, Double> mtTropismRest = new HashMap<String, Double>();
	public HashMap<String, Double> rabRecyProb = new HashMap<String, Double>();
	public HashMap<String, String> colorRab = new HashMap<String, String>();
	public HashMap<String, String> colorContent = new HashMap<String, String>();
	public HashMap<String, String> copasiFiles = new HashMap<String, String>();
	public HashMap<String, Double> uptakeRate = new HashMap<String, Double>();
	public HashMap<String, String> rabOrganelle = new HashMap<String, String>();

	public Set<String> solubleMet = new HashSet<String>();
	public Set<String> membraneMet = new HashSet<String>();
	public Set<String> rabSet = new HashSet<String>();
	

//	GETTERS
	
	public HashMap<String, Double> getCellAgentProperties() {
		return cellAgentProperties;
	}
	public HashMap<String, Double> getPlasmaMembraneProperties() {
		return plasmaMembraneProperties;
	}
	public HashMap<String, String> getCopasiFiles() {
		return copasiFiles;
	}
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
	public HashMap<String, Double> getMtTropismTubule() {
		return mtTropismTubule;
	}
	public HashMap<String, Double> getMtTropismRest() {
		return mtTropismRest;
	}
	public HashMap<String, Double> getRabRecyProb() {
		return rabRecyProb;
	}
	public HashMap<String, String> getRabOrganelle() {
		return rabOrganelle;
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
	public HashMap<String, Double> getUptakeRate() {
		return uptakeRate;
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
	public HashMap<String, Double> getInitPMsolubleRecycle() {
		return initPMsolubleRecycle;
	}

	
	public static void loadFromCsv(CellProperties cellProperties) throws IOException {

		Scanner scanner = new Scanner(new File(
				"inputIntrTransp3.csv"));
		scanner.useDelimiter(",");

//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			CellProperties config = objectMapper.readValue(new File(CellProperties.configFilename), CellProperties.class);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		// InitialOrganelles InOr = InitialOrganelles.getInstance();
		freezeDryOption: // this names the WHILE loop, so I can break from the loop when I want.  
			//Something I did not know that it could be done
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] b = line.split(",");
			switch (b[0]) {
			case "cellK": {
				for (int i = 1; i < b.length; i = i + 2) {
				cellProperties.getCellK().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println(cellProperties.getCellK());
				}
				
				break;
			}
			case "cellProperties": {
				for (int i = 1; i < b.length; i = i + 2) {
				cellProperties.getCellAgentProperties().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println(cellProperties.getCellK());
				}
				
				break;
			}
			case "plasmaMembraneProperties": {
				for (int i = 1; i < b.length; i = i + 2) {
				cellProperties.getPlasmaMembraneProperties().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println(cellProperties.getCellK());
				}
				
				break;
			}
			case "cellCopasi": case "plasmaMembraneCopasi" : case "endosomeCopasi": case "rabCopasi":{
					cellProperties.getCopasiFiles().put(b[0], b[1]);

				break;
			}
			case "initRabCell": {
				for (int i = 1; i < b.length; i = i + 2) {
				cellProperties.getInitRabCell().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println(cellProperties.getInitRabCell());
				}
				break;
			}
			case "initPMmembraneRecycle": {
				for (int i = 1; i < b.length; i = i + 2) {
				cellProperties.getInitPMmembraneRecycle().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println(cellProperties.getMembraneRecycle());
				}
				break;
			}
			case "initPMsolubleRecycle": {
				for (int i = 1; i < b.length; i = i + 2) {
				cellProperties.getInitPMsolubleRecycle().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println(cellProperties.getMembraneRecycle());
				}
				break;
			}
			case "solubleCell": {
				for (int i = 1; i < b.length; i = i + 2) {
				cellProperties.getSolubleCell().put(b[i], Double.parseDouble(b[i+1]));
//				System.out.println(cellProperties.getSolubleCell());
				}
				break;
			}
			case "rabCompatibility": {
				for (int i = 1; i < b.length; i = i + 2) {
					cellProperties.getRabCompatibility().put(b[i], Double.parseDouble(b[i+1]));
					//System.out.println(cellProperties.getRabCompatibility());
					}
				break;
			}
			case "tubuleTropism": {
				for (int i = 1; i < b.length; i = i + 2) {
					cellProperties.getTubuleTropism().put(b[i], Double.parseDouble(b[i+1]));
					//System.out.println(cellProperties.getTubuleTropism()); 
					}
				break;
			}
			case "rabTropism": {
				Set<String> rabT = new HashSet<String>();
				for (int i = 2; i < b.length; i++) {
					//System.out.println(b[i]);
					if (b[i].length()>0) {
						rabT.add(b[i]);
					}
				}
				cellProperties.getRabTropism().put(b[1], rabT);
				break;
			}
			case "mtTropismTubule": {
				for (int i = 1; i < b.length; i = i + 2) {
					cellProperties.getMtTropismTubule().put(b[i], Double.parseDouble(b[i+1]));
					//System.out.println(cellProperties.getMtTropism());
					}
				break;
			}
			case "mtTropismRest": {
				for (int i = 1; i < b.length; i = i + 2) {
					cellProperties.getMtTropismRest().put(b[i], Double.parseDouble(b[i+1]));
					//System.out.println(cellProperties.getMtTropism());
					}
				break;
			}
			case "rabRecyProb": {
				for (int i = 1; i < b.length; i = i + 2) {
					cellProperties.getRabRecyProb().put(b[i], Double.parseDouble(b[i+1]));
					}
				break;
			}
			
			case "organelle": {
				for (int i = 1; i < b.length; i = i + 2) {
					cellProperties.getRabOrganelle().put(b[i], b[i+1]);
					}
				break;
			}
			case "uptakeRate": {
				for (int i = 1; i < b.length; i = i + 2) {
					cellProperties.getUptakeRate().put(b[i], Double.parseDouble(b[i+1]));
				}
				break;
			}
			case "solubleMet": {
				for (int i = 1; i < b.length; i++) {
					cellProperties.getSolubleMet().add(b[i]);
				}
				break;
			}
			case "membraneMet": {
				for (int i = 1; i < b.length; i++) {
					cellProperties.getMembraneMet().add(b[i]);
				}
				break;
			}
			case "rabSet": {
				for (int i = 1; i < b.length; i++) {
					cellProperties.getRabSet().add(b[i]);
				}
				break;
			}
			
			case "colorRab": {
				for (int i = 1; i < b.length; i = i + 2) {
					cellProperties.getColorRab().put(b[i], b[i + 1]);
				}
				break;
			}
			case "colorContent": {
				for (int i = 1; i < b.length; i = i + 2) {
					cellProperties.getColorContent().put(b[i], b[i + 1]);
				}
				break;
			}
			
			case "freezeDry":
				{
					FreezeDryEndosomes.getInstance();
					break freezeDryOption; // if freezeDry then exit because the initial organelles will be loaded in a 
					// different way
			}
			// INITIAL ORGANELLES kind 7 is for phagosomes
			case "kind1": case "kind2": case "kind3": case "kind4": case "kind5": case "kind6": case "kindLarge":
			{
				InitialOrganelles inOr = InitialOrganelles.getInstance();
				inOr.getDiffOrganelles().add(b[0]);
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
//					System.out.println("Proton is there?" + inOr.getInitialSolubleContent());
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
//		System.out.println("CP INITIAL cellProp"+ InitialOrganelles.getInstance().initRabContent.toString());
//		System.out.println("CP CELL PROPERITES CARGADO");
//		System.out.println("CP VALOR "+ cellProperties.cellK);
//		System.out.println(cellProperties.initRabCell);
//		System.out.println(cellProperties.initPMmembraneRecycle);
//		System.out.println(cellProperties.rabCompatibility);
//		System.out.println(cellProperties.membraneMet);
//		System.out.println(cellProperties.solubleMet);
//		System.out.println(cellProperties.tubuleTropism);
//		System.out.println(cellProperties.rabTropism);
//		System.out.println("CP VALOR cellProp" + cellProperties.mtTropism);
		System.out.println("CP PARA RAB A UPTAKE" +InitialOrganelles.getInstance().getInitOrgProp().get("kind1"));
	}
}
