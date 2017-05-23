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

public class EndosomeUptakeStep {
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
	public void testUptake() {
//		ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule ();
//		System.out.println("TEST  ADENTRO test 1 TEST  "+endosome.area);
		// this.endosome....
		Cell.getInstance().rabCell.put("RabA", 3*5026.548);
		Cell.getInstance().settMembrane(3*5026.548);
		for (int i = 0; i<5; i++){

			System.out.println("\nTEST   antes t "+ Cell.getInstance().tMembrane + " cellrabs "+Cell.getInstance().rabCell);
			EndosomeUptaleStep.uptake(this.endosome);
			System.out.println("TEST   despues "+ Cell.getInstance().tMembrane + " cellrabs "+Cell.getInstance().rabCell);
			
			//		assertSame(this.endosome.solubleContent, this.endosome.solubleContent);
	//		assertNotSame(initial, this.endosome.solubleContent);
	}
	}




}
