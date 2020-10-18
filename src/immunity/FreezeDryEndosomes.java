package immunity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.lang3.RandomStringUtils;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.util.collections.IndexedIterable;
// This class contains the properties of the cell.  It is loaded with the same
// CSV file  used for the inital organelles.  It is updated by the UpdateParameters class.
public class FreezeDryEndosomes {
	
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

	
	public static void loadFromCsv(FreezeDryEndosomes frozenEndosomes) throws IOException {

		Scanner scanner = new Scanner(new File("inputFrozenEndosomes.csv"));
		scanner.useDelimiter(",");
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
					break;
				}
				case "initMembraneContent": {
					HashMap<String, Double> value = new HashMap<String, Double>();
					for (int i = 2; i < b.length; i = i + 2) {
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
	}
	
	
	
	public void writeToCsv() throws IOException {
		
		IndexedIterable<Endosome> collection = CellBuilder.getCollection();
		int index = 0;
		Writer output;	
	    double tick = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		String line ="tick " + tick + "\n";
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
		output.append(line);
		
		line = "";  
        line = line + "endosome"+index + ch + ",";
		String membraneContent = endosome.getMembraneContent().toString().replaceAll("=",","); 
		membraneContent = membraneContent.replace("{","");
		membraneContent = membraneContent.replace("}","");
        membraneContent = membraneContent.replace(" ","");
        line = line + "initMembraneContent" + "," + membraneContent;
        line = line + "\n";
		output.append(line);
		
		line = "";  
        line = line + "endosome"+index + ch + ",";        
        String solubleContent = endosome.getSolubleContent().toString().replaceAll("=",",");        
		solubleContent = solubleContent.replace("{","");
		solubleContent = solubleContent.replace("}","");
        solubleContent = solubleContent.replace(" ","");
        line = line + "initSolubleContent" + "," + solubleContent;
        line = line + "\n";
		output.append(line);
		index = index + 1;
		output.close();
	}
	}
	
}
