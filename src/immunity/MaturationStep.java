package immunity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MaturationStep {
	public static void mature(Endosome endosome) {
		//		if too small do not mature
		if(endosome.area < Cell.minCistern) return;
		//matureSingleDomain(endosome);
		matureAllDomains(endosome);
	}

	public static void matureSingleDomain(Endosome endosome) {
		/*
		 * Select at random a Rab present in the cisterna and matures it to the next Rab.  RabE does not mature 
		 */	
		HashMap<String, Double> rabContent = endosome.getRabContent();
		Object randomDomain = rabContent.keySet().toArray()[new Random().nextInt(rabContent.keySet().toArray().length)];		 
		String randomRab = (String) randomDomain;
		//	 System.out.println("MADURACION MADURACION .........................                                       "
		//				+ randomRab + rabContent);
		switch (randomRab)	{
		case "RabA": {
			double value = rabContent.get("RabA");
			if (rabContent.containsKey("RabB")) value = value + rabContent.get("RabB");
			rabContent.put("RabB", value);		
			rabContent.put("RabA", 0d);
			break;
		}
		case "RabB": {
			double value = rabContent.get("RabB");
			if (rabContent.containsKey("RabC")) value = value + rabContent.get("RabC");
			rabContent.put("RabC", value);		
			rabContent.put("RabB", 0d);
			break;
		}
		case "RabC": {
			double value = rabContent.get("RabC");
			if (rabContent.containsKey("RabD")) value = value + rabContent.get("RabD");
			rabContent.put("RabD", value);		
			rabContent.put("RabC", 0d);
			break;
		}
		case "RabD": {
			double value = rabContent.get("RabD");
			if (rabContent.containsKey("RabE")) value = value + rabContent.get("RabE");
			rabContent.put("RabE", value);		
			rabContent.put("RabD", 0d);
			break;
		}
		case "RabE": {
			break;
		}
		}
		//	 System.out.println("MADURACION MADURACION .........................                                       "
		//				+ randomRab + rabContent);

	}

	public static void matureAllDomains(Endosome endosome) {
		/*
		 * Select at random a Rab present in the cisterna and matures it to the next Rab.  RabE does not mature 
		 */	
		HashMap<String, Double> rabContent = endosome.getRabContent();
		HashMap<String, Double> rabContentCopy = new HashMap<String, Double>(endosome.getRabContent());
						 System.out.println("MADURACION INICIAL.........................                                       "
									+ rabContent + endosome.getSolubleContent());

		if (rabContentCopy.containsKey("RabD")) {
			double value = rabContentCopy.get("RabD");
			if(rabContentCopy.containsKey("RabE")) value = value + rabContentCopy.get("RabE");
			rabContent.put("RabE", value);
			rabContent.remove("RabD");
		}
		if (rabContentCopy.containsKey("RabC")) {
			double value = rabContentCopy.get("RabC");
			rabContent.put("RabD", value);
			rabContent.remove("RabC");
		}
		if (rabContentCopy.containsKey("RabB")) {
			double value = rabContentCopy.get("RabB");
			rabContent.put("RabC", value);
			rabContent.remove("RabB");
		}
		if (rabContentCopy.containsKey("RabA")) {
			double value = rabContentCopy.get("RabA");
			rabContent.put("RabB", value);
			rabContent.remove("RabA");
		}

		//		
		//		for (String rab : rabContentCopy.keySet()) {

		//			switch (rab)	{
		//			case "RabA": {
		//				double value = rabContentCopy.get("RabA");
		//				rabContent.remove("RabA");
		//				rabContent.put("RabB", value);		
		//				break;
		//			}
		//			case "RabB": {
		//				double value = rabContentCopy.get("RabB");
		//				rabContent.put("RabC", value);		
		//
		//				break;
		//			}
		//			case "RabC": {
		//				double value = rabContentCopy.get("RabC");
		//				rabContent.put("RabD", value);		
		//
		//				break;
		//			}
		//			case "RabD": {
		//				double value = rabContentCopy.get("RabD");
		//				if (rabContentCopy.containsKey("RabE")) value = value + rabContent.get("RabE");
		//				rabContent.put("RabE", value);		
		//
		//				break;
		//			}
		//			case "RabE": {
		//				break;
		//			}
		//			}
		System.out.println("MADURACION MADURACION .........................                                       "
				+ "" + rabContent);
	}

}





	
//	
//	String maxRab = Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
//	if (maxRab.equals("RabA") && rabContent.containsKey("RabB"))
//	{
//		Double rabBmatur = rabContent.get("RabB");
//		Double valueRabA = rabContent.get("RabA") + rabBmatur;
//		endosome.rabContent.put("RabA", valueRabA);
//		endosome.rabContent.remove("RabB");
//		return;
//	}
//	
//	else if (!maxRab.equals("RabA") && rabContent.containsKey("RabA"))
//	{
//		Double rabAmatur = rabContent.get("RabA");
//		Double valueRabMax = rabContent.get(maxRab) + rabAmatur;
//		endosome.rabContent.put(maxRab, valueRabMax);
//		endosome.rabContent.remove("RabA");
//		return;
//	}
//	else return;


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


