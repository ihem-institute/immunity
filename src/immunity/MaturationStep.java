package immunity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MaturationStep {
	public static void mature(Endosome endosome) {
/*
 * Specific maturation for ER ERIGIC
 * The idea is that if with low frequency, ER vesicles (from ERES)
 * are incorporated to ERGIC, they will mature to ERGIC (preventing fusion between ERGIC and ER)
 * Hence, ERGIC will not have ER domains that would trigger ER-ERGIC mixing
 * The same for ERGIC vesicles that with low frequency fuses with ER.  This domain will mature to ER
 * 
*/	HashMap<String, Double> rabContent = new HashMap<String, Double>(endosome.getRabContent());
	String maxRab = Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
	if (maxRab.equals("RabA") && rabContent.containsKey("RabB"))
	{
		Double rabBmatur = rabContent.get("RabB");
		Double valueRabA = rabContent.get("RabA") + rabBmatur;
		endosome.rabContent.put("RabA", valueRabA);
		endosome.rabContent.remove("RabB");
		return;
	}
	
	else if (!maxRab.equals("RabA") && rabContent.containsKey("RabA"))
	{
		Double rabAmatur = rabContent.get("RabA");
		Double valueRabMax = rabContent.get(maxRab) + rabAmatur;
		endosome.rabContent.put(maxRab, valueRabMax);
		endosome.rabContent.remove("RabA");
		return;
	}
	else return;
	}
}
	
//	if (rabContent.containsKey("RabE"))
//	{
//		if (!rabContent.containsKey("RabD")) rabContent.put("RabD", 0d); 
//		Double rabEmatur = rabContent.get("RabE")*0.1;
//		Double valueRabE = rabContent.get("RabE") - rabEmatur;
//		endosome.rabContent.put("RabE", valueRabE);
//		Double valueRabD = rabContent.get("RabD") + rabEmatur;
//		endosome.rabContent.put("RabD", valueRabD);
//		return;
//	}
//	else return;	
//		
//	}


