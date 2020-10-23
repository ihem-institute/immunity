package immunity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import repast.simphony.engine.schedule.ScheduledMethod;

public class UpdateParameters {

	private static UpdateParameters instance;
	//CAMBIO
	LocalPath mainpath=new LocalPath(); 
	String InputPath = mainpath.getPathInputIT();
	
	public static UpdateParameters getInstance() {
		if( instance == null ) {
			instance = new  UpdateParameters();
		}
		return instance;
	}

	private String oldFile = "";

	
	public String getOldFile() {
		return oldFile;
	}
	public void setOldFile(String oldFile) {
		this.oldFile = oldFile;
	}

	public UpdateParameters() {
//		this.space = sp;
//		this.grid = gr;				
	}

	@ScheduledMethod(start = 1, interval = 100)
	public void step(){
			try {
		testNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
}
	public void testNewFile() throws IOException {
		//CAMBIO
		File file = new File(InputPath);
		Path filePath = file.toPath();		
		BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
		String newFile = attr.lastModifiedTime().toString();
		if (newFile.equals(oldFile)){return;}
		else{
			try {
				loadFromCsv();
				
				CellProperties cellProperties = CellProperties.getInstance();
				
// The CellProperties are changed, but for parameters that are actualized only at the  				
//	beginning, I need to re-load values.  This is the case of initial rabs content in the 
//	Cell. This maight be useful for knocking down a Rab in the middle of an experiment
				Cell.getInstance().getRabCell().putAll(cellProperties.getInitRabCell());
											
				try {
					Thread.sleep(4000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			oldFile = newFile;
		}

		
		
	}
	

	
	public void loadFromCsv() throws FileNotFoundException {
		//CAMBIO
		Scanner scanner = new Scanner(new File(InputPath));
		scanner.useDelimiter(",");

		CellProperties cellProperties = CellProperties.getInstance();
		freezeDryOption: // this names the WHILE loop, so I can break from the loop when I want.  
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] b = line.split(",");
			switch (b[0]) {
			case "cellK": {
				for (int i = 1; i < b.length; i = i + 2) {
				cellProperties.getCellK().put(b[i], Double.parseDouble(b[i+1]));
				}
				
				break;
			}
			case "cellProperties": {
				for (int i = 1; i < b.length; i = i + 2) {
				cellProperties.getCellAgentProperties().put(b[i], Double.parseDouble(b[i+1]));
				}
				
				break;
			}
			case "plasmaMembraneProperties": {
				for (int i = 1; i < b.length; i = i + 2) {
				cellProperties.getPlasmaMembraneProperties().put(b[i], Double.parseDouble(b[i+1]));
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
				}
				break;
			}
			case "initPMmembraneRecycle": {
				for (int i = 1; i < b.length; i = i + 2) {
				cellProperties.getInitPMmembraneRecycle().put(b[i], Double.parseDouble(b[i+1]));
				}
				break;
			}
			case "initPMsolubleRecycle": {
				for (int i = 1; i < b.length; i = i + 2) {
				cellProperties.getInitPMsolubleRecycle().put(b[i], Double.parseDouble(b[i+1]));
				}
				break;
			}
			case "solubleCell": {
				for (int i = 1; i < b.length; i = i + 2) {
				cellProperties.getSolubleCell().put(b[i], Double.parseDouble(b[i+1]));
				}
				break;
			}
			case "rabCompatibility": {
				for (int i = 1; i < b.length; i = i + 2) {
					cellProperties.getRabCompatibility().put(b[i], Double.parseDouble(b[i+1]));
					}
				break;
			}
			case "tubuleTropism": {
				for (int i = 1; i < b.length; i = i + 2) {
					cellProperties.getTubuleTropism().put(b[i], Double.parseDouble(b[i+1]));
					}
				break;
			}
			case "rabTropism": {
				Set<String> rabT = new HashSet<String>();
				for (int i = 2; i < b.length; i++) {
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
					}
				break;
			}
			case "mtTropismRest": {
				for (int i = 1; i < b.length; i = i + 2) {
					cellProperties.getMtTropismRest().put(b[i], Double.parseDouble(b[i+1]));
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
//	
	}
	
	
}
