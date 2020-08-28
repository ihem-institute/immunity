package immunity;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.environment.RunState;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.util.collections.IndexedIterable;

public class EndosomeFusionStepTest {

	private Endosome endosome;
	private Endosome endosome2;

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
		this.endosome2 = (Endosome) objects.get(1);

	}
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFusion() {
//		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule ();
//		System.out.println("TEST  ADENTRO test 1 TEST  "+endosome.area);
		// this.endosome....
		this.endosome.rabContent.clear();
		this.endosome.rabContent.put("RabD", 50026.548);
		this.endosome.solubleContent.put("soluble", 1000d);
		this.endosome.membraneContent.put("membrane", 5d);
		this.endosome2.rabContent.clear();
		this.endosome2.rabContent.put("RabD", 50026.548);
		this.endosome2.solubleContent.put("soluble", 1000d);
		this.endosome2.membraneContent.put("membrane", 200d);
		this.endosome2.size = 30;
		this.endosome2.getGrid().moveTo(this.endosome2, 25, 25);
		for (int i = 0; i<20; i++){
			this.endosome.size = 10*(i+1);
			this.endosome.heading = -90+5*i;
			double yPosition = 49.999-i;
			this.endosome.getGrid().moveTo(this.endosome, 25, (int)yPosition);
			System.out.println("\nTEST   antes  "+this.endosome.solubleContent+"  "
			+this.endosome.membraneContent+"  ");
			FusionStep.fusion(endosome);
			System.out.println("TEST   despues "+this.endosome.solubleContent+"  "
					+this.endosome.membraneContent+"  ");
//			assertSame(this.endosome.solubleContent, this.endosome.solubleContent);
	//		assertNotSame(initial, this.endosome.solubleContent);

	}
	}

}
