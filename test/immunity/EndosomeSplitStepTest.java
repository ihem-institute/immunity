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

public class EndosomeSplitStepTest {

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
	public void testSplit() {
		this.endosome.area = 20000;
		this.endosome.volume = 80000;
		this.endosome.rabContent.clear();
		this.endosome.rabContent.put("RabA", 15000d);
		this.endosome.rabContent.put("RabD", 5000d);
		this.endosome.membraneContent.put("Tf", 10000d);
			
			for (int i = 0; i<10; i++){
				System.out.println("\nTEST   antes   area "+ endosome.area+ "volume "+endosome.volume +
				"\n "+this.endosome.getSolubleContent()+ " membrane content "+this.endosome.getMembraneContent()
				+"\n "+this.endosome.getRabContent());
				EndosomeSplitStep.split(endosome);
				System.out.println("\nTEST   despues  area "+ endosome.area+ "volume "+endosome.volume +
				"\n "+this.endosome.getSolubleContent()+ " membrane content "+this.endosome.getMembraneContent()
				+"\n "+this.endosome.getRabContent());
				//		assertSame(this.endosome.solubleContent, this.endosome.solubleContent);
		//		assertNotSame(initial, this.endosome.solubleContent);
		}
		}
	}


