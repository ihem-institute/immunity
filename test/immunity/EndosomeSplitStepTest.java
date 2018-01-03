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
		this.endosome.area = 85000;
		this.endosome.volume = 850000;
		this.endosome.rabContent.clear();
		this.endosome.rabContent.put("RabA", 35000d);
		this.endosome.rabContent.put("RabB", 50000d);
		this.endosome.membraneContent.put("Tf", 10000d);
		this.endosome.membraneContent.put("cMHCI", 10000d);
		this.endosome.solubleContent.put("mvb", 5d);
		this.endosome.membraneContent.put("mvb", 2d);
		this.endosome.solubleContent.put("ova", 500000d);
			
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


