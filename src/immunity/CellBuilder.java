package immunity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import au.com.bytecode.opencsv.CSVReader;
//import immunity.Element;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;

public class CellBuilder implements ContextBuilder<Object> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * repast.simphony.dataLoader.ContextBuilder#build(repast.simphony.context
	 * .Context)
	 */

	@Override
	public Context build(Context<Object> context) {
		context.setId("immunity");

		NetworkBuilder<Object> netBuilder = new NetworkBuilder<Object>(
				"infection network", context, true);
		netBuilder.buildNetwork();

		ContinuousSpaceFactory spaceFactory = ContinuousSpaceFactoryFinder
				.createContinuousSpaceFactory(null);
		ContinuousSpace<Object> space = spaceFactory.createContinuousSpace(
				"space", context, new RandomCartesianAdder<Object>(),
				new repast.simphony.space.continuous.WrapAroundBorders(), 50,
				50);

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context,
				new GridBuilderParameters<Object>(new WrapAroundBorders(),
						new SimpleGridAdder<Object>(), true, 50, 50));

		Parameters params = RunEnvironment.getInstance().getParameters();

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

		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/*
		 * try { loadFromExcel(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		// Microtubules

		for (int i = 0; i < 10; i++) {
			context.add(new MT(space, grid));
		}

		// Endosomes
		// RabA is Rab5
		int endosome_rabA_count = 10;// (Integer)
										// params.getValue("endosome_rabA_count");
		for (int i = 0; i < endosome_rabA_count; i++) {
			HashMap<String, Double> rabContent = new HashMap<String, Double>();
			HashMap<String, Double> membraneContent = new HashMap<String, Double>();
			HashMap<String, Double> solubleContent = new HashMap<String, Double>();
			rabContent.put("RabA", 4d * Math.PI * 30d * 30d);
			// membraneContent.put("Tf", 4d * Math.PI * 30d * 30d);
			solubleContent.put("ova", 4d / 3d * Math.PI * 30d * 30d * 30d);
			context.add(new Endosome(space, grid, rabContent, membraneContent,
					solubleContent));
			System.out.println(membraneContent + " " + solubleContent
					+ rabContent);
		}
		// RabB is Rab22
		int endosome_rabB_count = 10;// (Integer)
										// params.getValue("endosome_rabB_count");
		for (int i = 0; i < endosome_rabB_count; i++) {
			HashMap<String, Double> rabContent = new HashMap<String, Double>();
			HashMap<String, Double> membraneContent = new HashMap<String, Double>();
			HashMap<String, Double> solubleContent = new HashMap<String, Double>();
			rabContent.put("RabB", 4d * Math.PI * 30d * 30d);
			// membraneContent.put("Tf", 0.0d);
			solubleContent.put("ova", 0.0d);
			context.add(new Endosome(space, grid, rabContent, membraneContent,
					solubleContent));
		}
		// RabC is Rab11
		int endosome_rabC_count = 10;// (Integer)
										// params.getValue("endosome_rabC_count");
		for (int i = 0; i < endosome_rabC_count; i++) {
			HashMap<String, Double> rabContent = new HashMap<String, Double>();
			HashMap<String, Double> membraneContent = new HashMap<String, Double>();
			HashMap<String, Double> solubleContent = new HashMap<String, Double>();
			rabContent.put("RabC", 4d * Math.PI * 30d * 30d);
			// membraneContent.put("Tf", 0.0d);
			solubleContent.put("ova", 0.0d);
			context.add(new Endosome(space, grid, rabContent, membraneContent,
					solubleContent));
			System.out.println(membraneContent + " " + solubleContent
					+ rabContent);
		}
		// RabD is Rab7
		int endosome_rabD_count = 10;// (Integer)
										// params.getValue("endosome_rabD_count");
		for (int i = 0; i < endosome_rabD_count; i++) {
			HashMap<String, Double> rabContent = new HashMap<String, Double>();
			HashMap<String, Double> membraneContent = new HashMap<String, Double>();
			HashMap<String, Double> solubleContent = new HashMap<String, Double>();
			rabContent.put("RabD", 4d * Math.PI * 30d * 30d);
			// membraneContent.put("Tf", 0.0d);
			solubleContent.put("ova", 0.0d);
			solubleContent.put("p1", 4d / 3d * Math.PI * 30d * 30d * 30d);
			context.add(new Endosome(space, grid, rabContent, membraneContent,
					solubleContent));
			System.out.println(membraneContent + " " + solubleContent
					+ rabContent);
		}
		// RabE is Rab of secretory pathway
		int endosome_rabE_count = 10;// (Integer)
										// params.getValue("endosome_rabE_count");
		for (int i = 0; i < endosome_rabE_count; i++) {
			HashMap<String, Double> rabContent = new HashMap<String, Double>();
			HashMap<String, Double> membraneContent = new HashMap<String, Double>();
			HashMap<String, Double> solubleContent = new HashMap<String, Double>();
			rabContent.put("RabE", 4d * Math.PI * 30d * 30d);
			// membraneContent.put("Tf", 0.0d);
			membraneContent.put("mHCI", 4d * Math.PI * 30d * 30d);
			membraneContent.put("p2", 4d * Math.PI * 30d * 30d);
			solubleContent.put("ova", 0.0d);
			context.add(new Endosome(space, grid, rabContent, membraneContent,
					solubleContent));
			System.out.println(membraneContent + " " + solubleContent
					+ rabContent);
		}
		// Cytosol
		for (int i = 0; i < 50; i++) {
			for (int j = 0; j < 50; j++) {
				HashMap<String, Double> cytoContent = new HashMap<String, Double>();
				context.add(new Cytosol(space, grid, cytoContent, i, j));
			}
		}
		// Cell
		context.add(Cell.getInstance());
		context.add(new Results(space, grid));

		// Locate the object in the space and grid
		for (Object obj : context) {
			if (obj instanceof Cytosol) {
				double xcoor = ((Cytosol) obj).getXcoor();
				double ycoor = ((Cytosol) obj).getYcoor();
				space.moveTo(obj, xcoor, ycoor);
				grid.moveTo(obj, (int) xcoor, (int) ycoor);
			}
			if (obj instanceof MT) {
				((MT) obj).changePosition();
			} else {
				NdPoint pt = space.getLocation(obj);
				space.moveTo(obj, pt.getX(), pt.getY());
				grid.moveTo(obj, (int) pt.getX(), (int) pt.getY());
			}
		}

		if (RunEnvironment.getInstance().isBatch()) {
			RunEnvironment.getInstance().endAt(20);
		}

		return context;
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
		// InitialOrganelles InOr = InitialOrganelles.getInstance();
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
			case "kind1": {
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

			case "kind2": {
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
			case "kind3": {
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
			case "kind4": {
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
			case "kind5": {
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

	private static void loadFromExcel() throws IOException {
		// open the excel file
		HashMap<String, Double> cellK = new HashMap<String, Double>();
		HashMap<String, Double> initRabCell = new HashMap<String, Double>();
		// HashMap<String, HashMap<String, Double>> dataCell = new
		// HashMap<String, HashMap<String, Double>>();
		Workbook book = new XSSFWorkbook(new FileInputStream(
				"C:/users/lmayorga/desktop/inputIntrTransp.xlsx"));
		// get the first worksheet
		Sheet sheet = book.getSheetAt(0);
		int ID_COL = 0;
		for (Row row : sheet) {

			if (row.getRowNum() > 0 && row.getRowNum() < 7) {
				String a = row.getCell(0).getStringCellValue();
				String b = row.getCell(1).getStringCellValue();
				Double c = row.getCell(2).getNumericCellValue();
				System.out.println(a + b + c);

				if (a.equals("cellK")) {
					cellK.put(b, c);
				}
				if (a.equals("initRabCell")) {
					initRabCell.put(b, c);
				}

				/*
				 * int age = (int) row.getCell(AGE_COL).getNumericCellValue();
				 * double energy =
				 * row.getCell(ENERGY_COL).getNumericCellValue();
				 * 
				 * //Person person = new Person(id, age, energy);
				 * //context.add(person); double x =
				 * row.getCell(X_COL).getNumericCellValue(); double y =
				 * row.getCell(Y_COL).getNumericCellValue();
				 * //space.moveTo(person, x, y);
				 */
				System.out.println(a + b + c);
				System.out.println(a + cellK.toString()
						+ initRabCell.toString());
			}
		}

	}
}
