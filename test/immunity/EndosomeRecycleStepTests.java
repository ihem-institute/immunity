package immunity;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.RandomCartesianAdder;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.WrapAroundBorders;
import repast.simphony.util.collections.IndexedIterable;

public class EndosomeRecycleStepTests {

	private Endosome endosome;

	@Before
	public void setUp() throws Exception {
		Schedule schedule = new Schedule ();
		RunEnvironment.init( schedule , null , null , true );
		Context context = new DefaultContext();
		
		CellBuilder cellBuilder = new CellBuilder();
		context = cellBuilder.build(context);
		RunState.init().setMasterContext( context );
		IndexedIterable objects = context.getObjects(Endosome.class);
		this.endosome = (Endosome) objects.get(0);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRecyclerEnSuperficie() {
		// this.endosome....
		EndosomeRecycleStep.process(this.endosome);
		assertEquals("uno deberia ser 1", this.endosome.area, this.endosome.size);
		
	}
	@Test
	public void testRecyclerEnNoSuperficie() {
		// this.endosome....
		EndosomeRecycleStep.process(this.endosome);
		assertEquals("uno deberia ser 1", this.endosome.area, this.endosome.size);
		
	}

}
