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

public class EndosomeTetherStepTest {

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
	public void tether() {
//		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule ();
//		System.out.println("TEST  ADENTRO test 1 TEST  "+endosome.area);
		// this.endosome....

		for (int i = 0; i<10; i++){
			this.endosome.rabContent.clear();
			this.endosome.rabContent.put("RabA", 5026.548);
//			this.endosome2.rabContent.put("RabA", 2*5026.548);
			this.endosome.size = 5*(i+1);
//			this.endosome2.size = 110;
			this.endosome.heading = -90+5*i;
//			this.endosome2.heading = -45;
			
//			HashMap<String,Double> initial = new HashMap<String,Double>(this.endosome.solubleContent);
//			this.endosome.getSpace().moveTo(this.endosome, 25, 25);
			this.endosome.getSpace().moveTo(this.endosome, (int)Math.random()*50, (int)Math.random()*50);

			System.out.println("\nTEST   antes  "+this.endosome.heading+"  "
					+this.endosome.area);
			EndosomeTetherStep.tether(endosome);
//			EndosomeTetherStep.tether(endosome2);
			System.out.println("TEST   despues "+this.endosome.heading+"  "
					+this.endosome.area);
//			assertSame(this.endosome.solubleContent, this.endosome.solubleContent);
	//		assertNotSame(initial, this.endosome.solubleContent);
	}
	}

}
