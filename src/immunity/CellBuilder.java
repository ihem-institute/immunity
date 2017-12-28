package immunity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;


//import immunity.Element;
import repast.simphony.context.Context;
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
			System.out.println(cellProperties.initPMmembraneRecycle);
			System.out.println(cellProperties.rabCompatibility);
			System.out.println(cellProperties.membraneMet);
			System.out.println(cellProperties.solubleMet);
			System.out.println(cellProperties.tubuleTropism);
			System.out.println(cellProperties.rabTropism);
			System.out.println(cellProperties.mtTropism);

//			InitialOrganelles inOr = InitialOrganelles.getInstance();
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
		/*
		 * try { loadFromExcel(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		
//		Cell and recycled contents.  Total initial free membrane 
		Cell cell = Cell.getInstance();
		context.add(cell);
		
//		PlasmaMembrane plasmamembrane = PlasmaMembrane.getInstance();
		context.add(new PlasmaMembrane(space, grid));

		// Microtubules

		for (int i = 0; i < (int) 2/Cell.orgScale; i++) {
			context.add(new MT(space, grid));
		}

		// Endosomes
		// RabA is Rab5.  Organelles are constructed with a given radius that depend on the type (EE, LE, Lys) and with a 
		// total surface.  These values were obtained of simulations that progressed by 40000 steps

		Set<String> diffOrganelles = InitialOrganelles.getInstance().getDiffOrganelles();
		System.out.println(diffOrganelles);
		for (String kind : diffOrganelles){

			HashMap<String, Double> initOrgProp =  new HashMap<String, Double>(InitialOrganelles.getInstance().getInitOrgProp().get(kind));
			double totalArea = initOrgProp.get("area")/CellProperties.getInstance().getCellK().get("orgScale");
			double maxRadius = initOrgProp.get("radius");
			double minRadius = 40d;
			while (totalArea > 4*Math.PI*minRadius){
				double a = RandomHelper.nextDoubleFromTo(40d,maxRadius);
				double c = a + a  * Math.random();
				double f = 1.6075;
				double af= Math.pow(a, f);
				double cf= Math.pow(c, f);
				double area = 4d* Math.PI*Math.pow((af*af+af*cf+af*cf)/3, 1/f);
				double volume = 4d/3d*Math.PI*a*a*c;
				initOrgProp.put("area", area);
				initOrgProp.put("volume", volume);
				totalArea = totalArea-area;
				
				HashMap<String, Double> rabContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitRabContent().get(kind));
					for (String rab : rabContent.keySet()){
						double rr = rabContent.get(rab);
						rabContent.put(rab, rr*area);
					}
				HashMap<String, Double> membraneContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitMembraneContent().get(kind));
					for (String mem : membraneContent.keySet()){
						double mm = membraneContent.get(mem);
						membraneContent.put(mem, mm*area);
					}
				
				HashMap<String, Double> solubleContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitSolubleContent().get(kind));
					for (String sol : solubleContent.keySet()){
						double ss = solubleContent.get(sol);
						solubleContent.put(sol, ss*volume);
					}
				Endosome end = new Endosome(space, grid, rabContent, membraneContent,
						solubleContent, initOrgProp);
				context.add(end);
				Endosome.endosomeShape(end);
				System.out.println(membraneContent + " " + solubleContent + " " + rabContent+" " + initOrgProp);
		}
				
	}
//		this is used for standard starting
//			for (int i = 0; i < initOrgProp.get("number")/Cell.orgScale; i++) {
//				HashMap<String, Double> rabContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitRabContent().get(kind));
//				HashMap<String, Double> membraneContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitMembraneContent().get(kind));
//				HashMap<String, Double> solubleContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitSolubleContent().get(kind));
//				Endosome end = new Endosome(space, grid, rabContent, membraneContent,
//						solubleContent, initOrgProp);
//				context.add(end);
//				Endosome.endosomeShape(end);
//				System.out.println(membraneContent + " " + solubleContent + " " + rabContent+" " + initOrgProp);
//		}
		
		// Cytosol
		for (int i = 0; i < 50; i++) {
			for (int j = 0; j < 50; j++) {
				HashMap<String, Double> cytoContent = new HashMap<String, Double>();
				context.add(new Cytosol(space, grid, cytoContent, i, j));
			}
		}
		// Cell
		//context.add(Cell.getInstance());
		//context.add(CellProperties.getInstance());
		context.add(new Results(space, grid));// 
		context.add(new UpdateParameters());

		// Locate the object in the space and grid
		for (Object obj : context) {
			if (obj instanceof Cytosol) {
				double xcoor = ((Cytosol) obj).getXcoor();
				double ycoor = ((Cytosol) obj).getYcoor();
				space.moveTo(obj, xcoor, ycoor);
				grid.moveTo(obj, (int) xcoor, (int) ycoor);
			}
			if (obj instanceof PlasmaMembrane) {
				space.moveTo(obj, 24.5, 49.5);
				grid.moveTo(obj, (int) 24, (int) 49);
			}
			if (obj instanceof MT) {
				((MT) obj).changePosition((MT)obj);
			} 
			if (obj instanceof Endosome) {
				if(((Endosome) obj).getRabContent().containsKey("RabA")){
	//				NdPoint pt = space.getLocation(obj);
					double x = RandomHelper.nextDoubleFromTo(5d, 45d);
					double y = RandomHelper.nextDoubleFromTo(25d, 45d);
					space.moveTo(obj, x, y);
					grid.moveTo(obj, (int) x, (int) y);					
					}
				if(((Endosome) obj).getRabContent().containsKey("RabB")){
//					NdPoint pt = space.getLocation(obj);
					double x = RandomHelper.nextDoubleFromTo(5d, 45d);
					double y = RandomHelper.nextDoubleFromTo(25d, 45d);
					space.moveTo(obj, x, y);
					grid.moveTo(obj, (int) x, (int) y);					
					}
				if(((Endosome) obj).getRabContent().containsKey("RabC")){
//					NdPoint pt = space.getLocation(obj);
					double x = RandomHelper.nextDoubleFromTo(5d, 45d);
					double y = RandomHelper.nextDoubleFromTo(5d, 25d);
					space.moveTo(obj, x, y);
					grid.moveTo(obj, (int) x, (int) y);					
					}
				if(((Endosome) obj).getRabContent().containsKey("RabD")){
//					NdPoint pt = space.getLocation(obj);
					double x = RandomHelper.nextDoubleFromTo(5d, 45d);
					double y = RandomHelper.nextDoubleFromTo(5d, 25d);
					space.moveTo(obj, x, y);
					grid.moveTo(obj, (int) x, (int) y);					
					}
				if(((Endosome) obj).getRabContent().containsKey("RabE")){
//					NdPoint pt = space.getLocation(obj);
					double x = RandomHelper.nextDoubleFromTo(5d, 45d);
					double y = RandomHelper.nextDoubleFromTo(5d, 25d);
					space.moveTo(obj, x, y);
					grid.moveTo(obj, (int) x, (int) y);					
					}

			}
		}

		if (RunEnvironment.getInstance().isBatch()) {
			RunEnvironment.getInstance().endAt(20);
		}

		return context;
	}

	public void loadFromCsv() throws IOException {

		Scanner scanner = new Scanner(new File(
				"inputIntrTransp3.csv"));
		scanner.useDelimiter(",");
		CellProperties cellProperties = CellProperties.getInstance();

//		ObjectMapper objectMapper = new ObjectMapper();
//		try {
//			CellProperties config = objectMapper.readValue(new File(CellProperties.configFilename), CellProperties.class);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		// InitialOrganelles InOr = InitialOrganelles.getInstance();
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
					System.out.println(b[i]);
					if (b[i].contains("Rab")) {
						rabT.add(b[i]);
					}
				}
				cellProperties.getRabTropism().put(b[1], rabT);
				break;
			}
			case "mtTropism": {
				for (int i = 1; i < b.length; i = i + 2) {
					cellProperties.getMtTropism().put(b[i], Double.parseDouble(b[i+1]));
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
			
			// INITIAL ORGANELLES
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
			case "kind6": {
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
		System.out.println("INITIAL "+ InitialOrganelles.getInstance().initRabContent.toString());
		scanner.close();
	}
}
