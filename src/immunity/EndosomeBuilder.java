package immunity;

import java.util.ArrayList;
import java.util.HashMap;

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

public class EndosomeBuilder implements ContextBuilder<Object> {

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
		int endosome_rabA_count = (Integer) params.getValue("endosome_rabA_count");
		for (int i = 0; i < endosome_rabA_count; i++) {
			HashMap<String, Double> rabContent = new HashMap<String, Double>();
			HashMap<String, Double> membraneContent = new HashMap<String, Double>();
			HashMap<String, Double> solubleContent = new HashMap<String, Double>();
			rabContent.put("RabA", 4d * Math.PI * 30d * 30d);
			membraneContent.put("Tf",4d * Math.PI * 30d * 30d);
			solubleContent.put("dextran",4d / 3d * Math.PI * 30d * 30d * 30d );
			context.add(new Endosome(space, grid, rabContent, membraneContent, solubleContent));
			 System.out.println(membraneContent+" "+solubleContent + rabContent);
		}
		int endosome_rabB_count = (Integer) params.getValue("endosome_rabB_count");
		for (int i = 0; i < endosome_rabB_count; i++) {
			HashMap<String, Double> rabContent = new HashMap<String, Double>();
			HashMap<String, Double> membraneContent = new HashMap<String, Double>();
			HashMap<String, Double> solubleContent = new HashMap<String, Double>();
			rabContent.put("RabB", 4d * Math.PI * 30d * 30d);
			membraneContent.put("Tf", 0.0d);
			solubleContent.put("dextran", 0.0d);
			context.add(new Endosome(space, grid, rabContent, membraneContent, solubleContent));
		}
		for (int i = 0; i < 50; i++) {
			for (int j = 0; j < 50; j++){
			HashMap<String, Double> cytoContent = new HashMap<String, Double>();
			context.add(new Cytosol(space, grid, cytoContent, i, j));
		}
		}
		for (int i = 0 ; i<5 ; i++){
			context.add(new MT(space, grid));
		}

		for (Object obj : context) {
			NdPoint pt = space.getLocation(obj);
			grid.moveTo(obj, (int) pt.getX(), (int) pt.getY());
		}
		for (Object obj : context) {
			if (obj instanceof MT){
			space.moveTo(obj, 25, 25);
			grid.moveTo(obj, 25, 25);	
			}

		}
		
		if (RunEnvironment.getInstance().isBatch()) {
			RunEnvironment.getInstance().endAt(20);
		}

		return context;
	}
}
