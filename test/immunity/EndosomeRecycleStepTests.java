package immunity;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
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
import repast.simphony.engine.schedule.ISchedule;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
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
		RunState.init().setMasterContext(context);
		IndexedIterable objects = context.getObjects(Endosome.class);

		this.endosome = (Endosome) objects.get(0);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRecycle() {
//		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule ();
//		System.out.println("TEST  ADENTRO test 1 TEST  "+endosome.area);
		// this.endosome....

		for (int i = 0; i<5; i++){
			this.endosome.rabContent.clear();
			this.endosome.rabContent.put("RabA", 5026.548);
			this.endosome.solubleContent.put("ova", 1000d);
			ModelProperties.getInstance().rabRecyProb.put("RabA", 1d);
//			HashMap<String,Double> initial = new HashMap<String,Double>(this.endosome.solubleContent);
			double yPosition = 49.999-i;
			this.endosome.getSpace().moveTo(this.endosome, 25, yPosition);
			NdPoint myPoint = this.endosome.getSpace().getLocation(this.endosome);
			System.out.println("\nTEST   antes  "+this.endosome.rabContent
					+this.endosome.membraneContent+this.endosome.solubleContent);
			RecycleStep.recycle(this.endosome);
			System.out.println("TEST   despues "+this.endosome.rabContent
					+this.endosome.membraneContent+this.endosome.solubleContent);
			assertSame(this.endosome.solubleContent, this.endosome.solubleContent);
	//		assertNotSame(initial, this.endosome.solubleContent);
	}
	}
}
//	@Test
//	public void testRecyclerFueraSuperficie() {		
//		for (int i = 0; i<3; i++){
//			double yPosition = 49.999-i;
//			this.endosome.getSpace().moveTo(this.endosome, 25, yPosition);
//			NdPoint myPoint = this.endosome.getSpace().getLocation(this.endosome);
//			System.out.println("\nTEST   antes  "+this.endosome.rabContent
//					+this.endosome.membraneContent+this.endosome.solubleContent);
//			RecycleStep.recycle(this.endosome);
//			System.out.println("TEST   despues "+this.endosome.rabContent
//					+this.endosome.membraneContent+this.endosome.solubleContent);
//			 assertNull("el soluble deberia cambiar", this.endosome.solubleContent.get("ova"));
//		}
//		
//		assertEquals("uno deberia ser 1", this.endosome.area, this.endosome.size);
		
	
//	@Test
//	public void testRecyclerEnNoSuperficie() {
//		// this.endosome....
//		RecycleStep.recycle(this.endosome);
////		assertEquals("uno deberia ser 1", this.endosome.area, this.endosome.size);
//		
//	}


