package immunity;

import org.junit.Before;
import org.junit.Test;

public class RabConversionTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testRunTimeCourse() {
		RabConversion rabConversion = RabConversion.getInstance();
		
		rabConversion.setInitialConcentration("RabA", 1);
    	
    	// run time course
    	rabConversion.runTimeCourse();
    	
    	double rabA = rabConversion.getConcentration("RabA");
    	double rabB = rabConversion.getConcentration("RabB");
    	System.out.println(rabA);
    	System.out.println(rabB);

	}

}
