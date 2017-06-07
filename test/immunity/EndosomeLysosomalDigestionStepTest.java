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
import repast.simphony.util.collections.IndexedIterable;

public class EndosomeLysosomalDigestionStepTest {
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
	public void testInternalVesicle() {
		this.endosome.area = 20000;
		this.endosome.volume = 40000;
		this.endosome.rabContent.clear();
		this.endosome.rabContent.put("RabA", 5000d);

		this.endosome.membraneContent.put("Tf", 10000d);
			
			for (int i = 0; i<5; i++){
				this.endosome.rabContent.put("RabD", 3000d*i);
				this.endosome.solubleContent.put("mvb", (double)1000* i);
				System.out.println("\nTEST   antes    \n "+ this.endosome.volume + this.endosome.getSolubleContent()
						+ " membrane content "+this.endosome.getMembraneContent());
				EndosomeLysosomalDigestionStep.lysosomalDigestion(this.endosome);
				System.out.println("\nTEST   DESPUES    \n " + this.endosome.volume + this.endosome.getSolubleContent()
						+ " membrane content "+this.endosome.getMembraneContent());	
		//assertSame(this.endosome.solubleContent, this.endosome.solubleContent);
		//		assertNotSame(initial, this.endosome.solubleContent);
		}
		}	
	
	}

