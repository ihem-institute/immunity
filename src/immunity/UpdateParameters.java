package immunity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class UpdateParameters {

	private static UpdateParameters instance;
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	private String oldFile = "";
	public String getOldFile() {
		return oldFile;
	}
	public void setOldFile(String oldFile) {
		this.oldFile = oldFile;
	}

	public static UpdateParameters getInstance() {
		return instance;
	}

//	{
//		UpdateParameters instance = new UpdateParameters(grid, space);
//	}
	public UpdateParameters(Grid<Object> gr, ContinuousSpace<Object> sp) {
		this.space = sp;
		this.grid = gr;				
	}


	@ScheduledMethod(start = 1, interval = 100)
	public void step(){
			try {
		testNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
	public void testNewFile() throws IOException {
		File file = new File("C:/users/lmayorga/desktop/inputIntrTransp3.csv");
		Path filePath = file.toPath();		
		BasicFileAttributes attr = Files.readAttributes(filePath, BasicFileAttributes.class);
		String newFile = attr.lastModifiedTime().toString();
		if (newFile != oldFile){
			try {
				loadFromCsv();
				CellProperties cellProperties = CellProperties.getInstance();
				System.out.println(cellProperties.cellK);
				System.out.println(cellProperties.initRabCell);
				System.out.println(cellProperties.rabCompatibility);
				System.out.println(cellProperties.membraneMet);
				System.out.println(cellProperties.solubleMet);
				System.out.println(cellProperties.tubuleTropism);
				System.out.println(cellProperties.rabTropism);
				System.out.println(cellProperties.mtTropism);

				InitialOrganelles inOr = InitialOrganelles.getInstance();
				System.out
						.println(InitialOrganelles.getInstance().getInitOrgProp());
				System.out.println(InitialOrganelles.getInstance()
						.getInitRabContent());
				System.out.println(InitialOrganelles.getInstance()
						.getInitMembraneContent());
				System.out.println(InitialOrganelles.getInstance()
						.getInitSolubleContent());

			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			oldFile = newFile;
		}
		System.out.println("creationTime: " + attr.creationTime());
		System.out.println("lastAccessTime: " + attr.lastAccessTime());
		System.out.println("lastModifiedTime: " + attr.lastModifiedTime());
		
		
	}
	

	
	public void loadFromCsv() throws FileNotFoundException {
		// InitialOrganelles InOr = InitialOrganelles.getInstance();
		// for (int i = 1; i < 6; i++) {
		// InOr.getInitOrgProp().put("kind" + i, null);
		// InOr.getInitRabContent().put("kind" + i, null);
		// InOr.getInitSolubleContent().put("kind" + i, null);
		// InOr.getInitMembraneContent().put("kind" + i, null);
		// }

		Scanner scanner = new Scanner(new File(
				"C:/users/lmayorga/desktop/inputIntrTransp3.csv"));
		scanner.useDelimiter(",");
		// HashMap<String, Double> rabContent = new HashMap<String, Double>();
		// HashMap<String, Double> membraneContent = new HashMap<String,
		// Double>();
		// HashMap<String, Double> solubleContent = new HashMap<String,
		// Double>();
		CellProperties cellProperties = CellProperties.getInstance();
// INITIAL CELL PROPERTIES
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] b = line.split(",");
			switch (b[0]) {
			case "cellK": {
				cellProperties.getCellK().put(b[1], Double.parseDouble(b[2]));
				break;
			}
			case "initRabCell": {
				cellProperties.getInitRabCell().put(b[1],
						Double.parseDouble(b[2]));
				break;
			}
			case "rabCompatibility": {
				cellProperties.getRabCompatibility().put(b[1],
						Double.parseDouble(b[2]));
				break;
			}
			case "tubuleTropism": {
				cellProperties.getTubuleTropism().put(b[1],
						Double.parseDouble(b[2]));
				break;
			}
			case "rabTropism": {
				List<String> rabT = new ArrayList<String>();
				for (int i = 2; i < b.length; i++) {
					System.out.println(b[i]);
					if (b[i].contains("Rab")) {
						rabT.add(b[i]);
					}
				}
				cellProperties.getRabTropism().put(b[1], rabT);
				break;
			}
			case "mtTropism": {
				cellProperties.getMtTropism().put(b[1],
						Double.parseDouble(b[2]));
				break;
			}
			case "membraneMet": {
				for (int i = 1; i < b.length; i++) {
					cellProperties.getMembraneMet().add(b[i]);
				}
				break;
			}
			case "solubleMet": {
				for (int i = 1; i < b.length; i++) {
					cellProperties.getSolubleMet().add(b[i]);
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
			
//			// INITIAL ORGANELLES
//			case "kind1": {
//				InitialOrganelles inOr = InitialOrganelles.getInstance();
//				inOr.getDiffOrganelles().add(b[0]);
//				switch (b[1]) {
//				case "initOrgProp": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitOrgProp().put(b[0], value);
//					break;
//				}
//				case "initRabContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitRabContent().put(b[0], value);
//					break;
//				}
//				case "initSolubleContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitSolubleContent().put(b[0], value);
//					break;
//				}
//				case "initMembraneContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitMembraneContent().put(b[0], value);
//					break;
//				}
//				default: {
//					System.out.println("no a valid entry");
//				}
//				}
//				break;
//			}
//
//			case "kind2": {
//				InitialOrganelles inOr = InitialOrganelles.getInstance();
//				inOr.getDiffOrganelles().add(b[0]);
//				switch (b[1]) {
//				case "initOrgProp": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitOrgProp().put(b[0], value);
//					break;
//				}
//				case "initRabContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitRabContent().put(b[0], value);
//					break;
//				}
//				case "initSolubleContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitSolubleContent().put(b[0], value);
//					break;
//				}
//				case "initMembraneContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitMembraneContent().put(b[0], value);
//					break;
//				}
//				default: {
//					System.out.println("no a valid entry");
//				}
//				}
//				break;
//			}
//			case "kind3": {
//				InitialOrganelles inOr = InitialOrganelles.getInstance();
//				inOr.getDiffOrganelles().add(b[0]);
//				switch (b[1]) {
//				case "initOrgProp": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitOrgProp().put(b[0], value);
//					break;
//				}
//				case "initRabContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitRabContent().put(b[0], value);
//					break;
//				}
//				case "initSolubleContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitSolubleContent().put(b[0], value);
//					break;
//				}
//				case "initMembraneContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitMembraneContent().put(b[0], value);
//					break;
//				}
//				default: {
//					System.out.println("no a valid entry");
//				}
//				}
//				break;
//			}
//			case "kind4": {
//				InitialOrganelles inOr = InitialOrganelles.getInstance();
//				inOr.getDiffOrganelles().add(b[0]);
//				switch (b[1]) {
//				case "initOrgProp": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitOrgProp().put(b[0], value);
//					break;
//				}
//				case "initRabContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitRabContent().put(b[0], value);
//					break;
//				}
//				case "initSolubleContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitSolubleContent().put(b[0], value);
//					break;
//				}
//				case "initMembraneContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitMembraneContent().put(b[0], value);
//					break;
//				}
//				default: {
//					System.out.println("no a valid entry");
//				}
//				}
//				break;
//			}
//			case "kind5": {
//				InitialOrganelles inOr = InitialOrganelles.getInstance();
//				inOr.getDiffOrganelles().add(b[0]);
//				switch (b[1]) {
//				case "initOrgProp": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitOrgProp().put(b[0], value);
//					break;
//				}
//				case "initRabContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitRabContent().put(b[0], value);
//					break;
//				}
//				case "initSolubleContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitSolubleContent().put(b[0], value);
//					break;
//				}
//				case "initMembraneContent": {
//					HashMap<String, Double> value = new HashMap<String, Double>();
//					for (int i = 2; i < b.length; i = i + 2) {
//						value.put(b[i], Double.parseDouble(b[i + 1]));
//					}
//					inOr.getInitMembraneContent().put(b[0], value);
//					break;
//				}
//				default: {
//					System.out.println("no a valid entry");
//				}
//				}
//				break;
//			}
			default: {
				//System.out.println("no a valid entry");
			}
			}
		}
		scanner.close();
	}




	
	
}