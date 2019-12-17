package immunity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import repast.simphony.context.Context;
import repast.simphony.util.ContextUtils;

public class MaturationStep {
	
	public static void mature(Endosome endosome) {
		double membraneFlux = CellProperties.getInstance().cellK.get("membraneFlux");
		String maxRab = Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
		String organelleName = CellProperties.getInstance().rabOrganelle.get(maxRab);
		if (membraneFlux != 1d) return; // if not membrane flux, no maturation
		if (endosome.area > 2*4*Math.PI*Cell.rcyl // if large enough (twice a vesicle)
				&& organelleName.contains("Golgi")) // and if it is the prevalent domain is a Golgi structure
		{			
//			System.out.println("GOLGI MATURATION  "+endosome.rabContent +endosome.xcoor+endosome.ycoor);

			membraneFluxMatureSyn(endosome, maxRab);

		}
		else if (organelleName.contains("ERGIC")){// if ERGIC, mature to cisGolgi near the bottom
//			System.out.println("ERGIC MATURATION  "+endosome.rabContent +endosome.xcoor+endosome.ycoor);
			matureERGIC(endosome);
		}
		else if (organelleName == "TGN"){// not specified yet
			return;
		}
		else{
			return;
		}
	}
	
	private static void membraneFluxMature(Endosome endosome, String maxRab) {
		double maturationTrigger = CellProperties.getInstance().cellK.get("maturationTrigger");
		double value = endosome.rabContent.get(maxRab);
		switch (maxRab) {
		case "RabA": {
			double totalRab = Results.getInstance().getTotalRabs().get("RabB");
			double initialTotalRabA = Results.getInstance().getInitialTotalRabs().get("RabA");
//			System.out.println(endosome.rabContent + "  totalRabB  "+totalRab);
			if (totalRab < maturationTrigger * initialTotalRabA) {
			endosome.rabContent.put("RabA", 0d);
			endosome.rabContent.put("RabB", value);}
			break;
		}
		case "RabB": {
			double totalRab = Results.getInstance().getTotalRabs().get("RabC");
			double initialTotalRabA = Results.getInstance().getInitialTotalRabs().get("RabA");
			//		System.out.println("totalRabs  "+totalRabs);
			if (totalRab < maturationTrigger * initialTotalRabA) {
			endosome.rabContent.put("RabB", 0d);
			endosome.rabContent.put("RabC", value);}
			break;
		}
		case "RabC": {
			double totalRab = Results.getInstance().getTotalRabs().get("RabD");
			double initialTotalRabA = Results.getInstance().getInitialTotalRabs().get("RabA");
			//		System.out.println("totalRabs  "+totalRabs);
			if (totalRab < maturationTrigger * initialTotalRabA) {
			endosome.rabContent.put("RabC", 0d);
			endosome.rabContent.put("RabD", value);}
			break;
		}
		case "RabD": {
			double totalRab = Results.getInstance().getTotalRabs().get("RabE");
			double initialTotalRabA = Results.getInstance().getInitialTotalRabs().get("RabA");
			//		System.out.println("totalRabs  "+totalRabs);
			if (totalRab <= initialTotalRabA) {
			endosome.rabContent.put("RabD", 0d);
			endosome.rabContent.put("RabE", value);}
			break;
		}
		case "RabE": {
			double totalRab = Results.getInstance().getTotalRabs().get("RabE");
			double initialTotalRabA = Results.getInstance().getInitialTotalRabs().get("RabA");
			//		System.out.println("totalRabs  "+totalRabs);
			if (totalRab > initialTotalRabA) {
			Context<Object> context = ContextUtils.getContext(endosome);
			context.remove(endosome);}
			break;
		}
		
		}

	}
	
	private static void membraneFluxMatureSyn(Endosome endosome, String maxRab) {
		double maturationTrigger = CellProperties.getInstance().cellK.get("maturationTrigger");
		double value = endosome.rabContent.get(maxRab);
		switch (maxRab) {
		case "RabA": {
//			endosome.rabContent.put("RabA", 0d);
//			endosome.rabContent.put("RabB", value);
			break;
		}
		case "RabB": {
			if(Math.random()<0.9) break;
			endosome.rabContent.clear();//put("RabB", 0d);
			endosome.rabContent.put("RabC", endosome.area);
			break;
		}
		case "RabC": {
			if(Math.random()<0.9) break;
			endosome.rabContent.clear();//put("RabC", 0d);
			endosome.rabContent.put("RabD", endosome.area);
			break;
		}
		case "RabD": {
			if(Math.random()<0.9) break;
			endosome.rabContent.clear(); //put("RabD", 0d);
			endosome.rabContent.put("RabE", endosome.area);
			break;
		}
		case "RabE": {
//			Context<Object> context = ContextUtils.getContext(endosome);
//			context.remove(endosome);
			break;
		}
		
		}

	}

	private static void specialMature(Endosome endosome) {
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


	private static void matureERGIC(Endosome endosome) {
		/*
		 * Specific maturation for ERIGIC
		 * When near the cisGolgi (xcoor in the center and ycoor near the bottom), mature to cisGolgi
		 */	
		if (endosome.xcoor < 10
			|| endosome.xcoor > 40
			|| endosome.ycoor > 5)
		{
			return;
		}
		else{
			System.out.println("ERGICCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC MATURATION  "+endosome.rabContent +endosome.xcoor+"   "+endosome.ycoor);
			endosome.rabContent.clear();
			endosome.rabContent.put("RabB", endosome.area);
			return;
		}
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


