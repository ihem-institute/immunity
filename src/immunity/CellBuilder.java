package immunity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			loadFromExcel();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Microtubules

		for (int i = 0; i < 10; i++) {
			context.add(new MT(space, grid));
		}

		// Endosomes
		// RabA is Rab5
		int endosome_rabA_count = (Integer) params
				.getValue("endosome_rabA_count");
		for (int i = 0; i < endosome_rabA_count; i++) {
			HashMap<String, Double> rabContent = new HashMap<String, Double>();
			HashMap<String, Double> membraneContent = new HashMap<String, Double>();
			HashMap<String, Double> solubleContent = new HashMap<String, Double>();
			rabContent.put("RabA", 4d * Math.PI * 30d * 30d);
			membraneContent.put("Tf", 4d * Math.PI * 30d * 30d);
			solubleContent.put("ova", 4d / 3d * Math.PI * 30d * 30d * 30d);
			context.add(new Endosome(space, grid, rabContent, membraneContent,
					solubleContent));
			System.out.println(membraneContent + " " + solubleContent
					+ rabContent);
		}
		// RabB is Rab22
		int endosome_rabB_count = (Integer) params
				.getValue("endosome_rabB_count");
		for (int i = 0; i < endosome_rabB_count; i++) {
			HashMap<String, Double> rabContent = new HashMap<String, Double>();
			HashMap<String, Double> membraneContent = new HashMap<String, Double>();
			HashMap<String, Double> solubleContent = new HashMap<String, Double>();
			rabContent.put("RabB", 4d * Math.PI * 30d * 30d);
			membraneContent.put("Tf", 0.0d);
			solubleContent.put("ova", 0.0d);
			context.add(new Endosome(space, grid, rabContent, membraneContent,
					solubleContent));
		}
		// RabC is Rab11
		int endosome_rabC_count = (Integer) params
				.getValue("endosome_rabC_count");
		for (int i = 0; i < endosome_rabC_count; i++) {
			HashMap<String, Double> rabContent = new HashMap<String, Double>();
			HashMap<String, Double> membraneContent = new HashMap<String, Double>();
			HashMap<String, Double> solubleContent = new HashMap<String, Double>();
			rabContent.put("RabC", 4d * Math.PI * 30d * 30d);
			membraneContent.put("Tf", 0.0d);
			solubleContent.put("ova", 0.0d);
			context.add(new Endosome(space, grid, rabContent, membraneContent,
					solubleContent));
			System.out.println(membraneContent + " " + solubleContent
					+ rabContent);
		}
		// RabD is Rab7
		int endosome_rabD_count = (Integer) params
				.getValue("endosome_rabD_count");
		for (int i = 0; i < endosome_rabD_count; i++) {
			HashMap<String, Double> rabContent = new HashMap<String, Double>();
			HashMap<String, Double> membraneContent = new HashMap<String, Double>();
			HashMap<String, Double> solubleContent = new HashMap<String, Double>();
			rabContent.put("RabD", 4d * Math.PI * 30d * 30d);
			membraneContent.put("Tf", 0.0d);
			solubleContent.put("ova", 0.0d);
			solubleContent.put("p1", 4d / 3d * Math.PI * 30d * 30d * 30d);
			context.add(new Endosome(space, grid, rabContent, membraneContent,
					solubleContent));
			System.out.println(membraneContent + " " + solubleContent
					+ rabContent);
		}
		// RabE is Rab of secretory pathway
		int endosome_rabE_count = (Integer) params
				.getValue("endosome_rabE_count");
		for (int i = 0; i < endosome_rabE_count; i++) {
			HashMap<String, Double> rabContent = new HashMap<String, Double>();
			HashMap<String, Double> membraneContent = new HashMap<String, Double>();
			HashMap<String, Double> solubleContent = new HashMap<String, Double>();
			rabContent.put("RabE", 4d * Math.PI * 30d * 30d);
			membraneContent.put("Tf", 0.0d);
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

	public static void loadFromCsv() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(
				"C:/users/lmayorga/desktop/inputIntrTransp.csv"));
		scanner.useDelimiter(";");
		while (scanner.hasNext()) {
			System.out.print(scanner.next() + "anda");
		}
		scanner.close();
	}

	private static void loadFromExcel() throws IOException {
		// open the excel file
		HashMap<String, Double> cellK = new HashMap<String, Double>();
		HashMap<String, Double> initRabCell = new HashMap<String, Double>();
		//HashMap<String, HashMap<String, Double>> dataCell = new HashMap<String, HashMap<String, Double>>();
		Workbook book = new XSSFWorkbook(new FileInputStream(
				"C:/users/lmayorga/desktop/inputIntrTransp.xlsx"));
		// get the first worksheet
		Sheet sheet = book.getSheetAt(0);
		int ID_COL = 0;
		for (Row row : sheet) {

			if (row.getRowNum() >= 0 && row.getRowNum() < 8) {
				String a = row.getCell(0).getStringCellValue();
				String b = row.getCell(1).getStringCellValue();
				Double c = row.getCell(2).getNumericCellValue();
				System.out.println(a+b+c);

				
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
				System.out.println(a + cellK.toString() + initRabCell.toString());
			}
		}

	}
}
