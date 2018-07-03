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

//			loadFromCsv();
		CellProperties cellProperties = CellProperties.getInstance();
		context.add(cellProperties);	
		Cell cell = Cell.getInstance();
		context.add(cell);
		context.add(new Results(space, grid, null, null));// 
		context.add(new UpdateParameters());
		context.add(new PlasmaMembrane(space, grid));
		
		context.add(new Scale(space, grid));

		System.out.println("CELL BUILDER CARGADO");
		System.out.println("VALOR "+ cellProperties.getCellK());
		System.out.println(cellProperties.initRabCell);
		System.out.println(cellProperties.initPMmembraneRecycle);
		System.out.println(cellProperties.rabCompatibility);
		System.out.println(cellProperties.membraneMet);
		System.out.println(cellProperties.solubleMet);
		System.out.println(cellProperties.tubuleTropism);
		System.out.println(cellProperties.rabTropism);
		System.out.println("VALOR mtTrop" + cellProperties.mtTropism);

//			InitialOrganelles inOr = InitialOrganelles.getInstance();
		InitialOrganelles initialOrganelles  = InitialOrganelles.getInstance();
		context.add(initialOrganelles);
		System.out
				.println(InitialOrganelles.getInstance().getInitOrgProp());
		System.out.println(InitialOrganelles.getInstance()
				.getInitRabContent());		
		System.out.println(InitialOrganelles.getInstance()
				.getInitMembraneContent());
		System.out.println(InitialOrganelles.getInstance()
				.getInitSolubleContent());

		/*
		 * try { loadFromExcel(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
		
//		Cell and recycled contents.  Total initial free membrane 
		
		// Microtubules

//		for (int i = 0; i < (int) 3/Cell.orgScale; i++) {
//			context.add(new MT(space, grid));
//		}
		context.add(new MT(space, grid));
//		context.add(new MT(space, grid));
//		context.add(new MT(space, grid));
		// Endosomes
		// RabA is Rab5.  Organelles are constructed with a given radius that depend on the type (EE, LE, Lys) and with a 
		// total surface.  These values were obtained of simulations that progressed by 40000 steps

		Set<String> diffOrganelles = InitialOrganelles.getInstance().getDiffOrganelles();
		System.out.println(diffOrganelles);
		int uno = 0;
		for (String kind : diffOrganelles){
			HashMap<String, Double> initOrgProp =  new HashMap<String, Double>(InitialOrganelles.getInstance().getInitOrgProp().get(kind));
			double totalArea = initOrgProp.get("area")/CellProperties.getInstance().getCellK().get("orgScale");
			double maxRadius = initOrgProp.get("radius");
			double minRadius = Cell.rcyl*1.1;
			while (totalArea > 32d*Math.PI*minRadius*minRadius){
				double a = 0d;
				double c = 0d;
//				more round vesicles for RabA and RabD.  Tubules for RabB, RabC and RabE
				if (kind.equals("kind1") || kind.equals("kind4")){
					a = RandomHelper.nextDoubleFromTo(minRadius,maxRadius);				
					c = a + a  * Math.random();
				}
				else if (kind.equals("kind6")){
					a = maxRadius;				
					c = a;
				}
				else{
					a = minRadius;				
					c = a + a * RandomHelper.nextDoubleFromTo(2,10);
				}
				double f = 1.6075;
				double af= Math.pow(a, f);
				double cf= Math.pow(c, f);
				double area = 4d* Math.PI*Math.pow((af*af+af*cf+af*cf)/3, 1/f);
				double volume = 0.9*4d/3d*Math.PI*a*a*c;
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
						if (mem.equals("membraneMarker")){
							membraneContent.put(mem, 1d);
							InitialOrganelles.getInstance().getInitSolubleContent().get(kind).remove("membraneMarker");
						}
						else {double mm = membraneContent.get(mem);
						membraneContent.put(mem, mm*area);
						}
					}
				
				HashMap<String, Double> solubleContent = new HashMap<String, Double>(InitialOrganelles.getInstance().getInitSolubleContent().get(kind));
					for (String sol : solubleContent.keySet()){
						if (sol.equals("solubleMarker")){
							solubleContent.put(sol, 1d);
							InitialOrganelles.getInstance().getInitSolubleContent().get(kind).remove("solubleMarker");
						}
						else {double ss = solubleContent.get(sol);
						solubleContent.put(sol, ss*volume);
						}
					}


				Endosome end = new Endosome(space, grid, rabContent, membraneContent,
						solubleContent, initOrgProp);
				context.add(end);
				Endosome.endosomeShape(end);
				
				System.out.println(membraneContent + " " + solubleContent + " " + rabContent+" " + initOrgProp);
uno = 1;
				break;
			}	
if (uno == 1) break;
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
//		for (int i = 0; i < 50; i++) {
//			for (int j = 0; j < 50; j++) {
//				HashMap<String, Double> cytoContent = new HashMap<String, Double>();
//				context.add(new Cytosol(space, grid, cytoContent, i, j));
//			}
//		}
		// Cell
		//context.add(Cell.getInstance());
		//context.add(CellProperties.getInstance());


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
			if (obj instanceof Scale) {
				space.moveTo(obj, Scale.getScale500nm()/2d-(0.4), 49.9);
//				System.out.println ("SCALE SCALE "+Scale.getScale500nm()/2d);
				grid.moveTo(obj, (int) (Scale.getScale500nm()/2d), (int) 49);
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

	
}
