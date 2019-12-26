package immunity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class MaturationStep {
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	public static void mature(Endosome endosome) {
		space = endosome.getSpace();
		grid = endosome.getGrid();
		double membraneFlux = CellProperties.getInstance().cellK.get("membraneFlux");
		String maxRab = Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
		String organelleName = CellProperties.getInstance().rabOrganelle.get(maxRab);
		if (membraneFlux == 1d 
				&& endosome.area >= 4.5E5
				&& organelleName.contains("Golgi")){
	//		membraneFluxMatureSyn(endosome, maxRab);
			maturePush(endosome, maxRab);

		}
		else if (organelleName.contains("ERGIC")){// if ERGIC, mature to cisGolgi near the bottom
//			System.out.println("ERGIC MATURATION  "+endosome.rabContent +endosome.xcoor+endosome.ycoor);
			matureERGIC(endosome);
		}
		else if (organelleName.contains("TGN")){// not specified yet
//			
//			Context<Object> context = ContextUtils.getContext(endosome);
//			context.remove(endosome);
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
			endosome.rabContent.clear();  //put("RabA", 0d);
			endosome.rabContent.put("RabB", endosome.area);
			break;
		}
		case "RabB": {
//			endosome.rabContent.put("RabB", 0d);
//			endosome.rabContent.put("RabC", value);
			endosome.rabContent.clear();  //put("RabA", 0d);
			endosome.rabContent.put("RabC", endosome.area);
			break;
		}
		case "RabC": {
//			endosome.rabContent.put("RabC", 0d);
//			endosome.rabContent.put("RabD", value);
			endosome.rabContent.clear();  //put("RabA", 0d);
			endosome.rabContent.put("RabD", endosome.area);
			break;
		}
		case "RabD": {
//			endosome.rabContent.put("RabD", 0d);
//			endosome.rabContent.put("RabE", value);
			endosome.rabContent.clear();  //put("RabA", 0d);
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
				|| endosome.ycoor > 5
				//			|| endosome.area< 1E06
				)
		{
			return;
		}
		else{
			System.out.println("ERGICCCCCCCfffffffffffffffffffffffffffusion  "+endosome.rabContent +endosome.area);
			EndosomeFusionStep.fusion(endosome);
			System.out.println("ERGICCCCCCCAAAAAAAfterffffffffffusion  "+endosome.rabContent +endosome.area);

//			try {
//			TimeUnit.SECONDS.sleep(3);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}




			// After fusion with other endosomes, if large enough matures and returns
			// 500 nm radius, 20 nm high.  OJO NO CONSIDERO ESCALA, HABRIA QUE MEJORAR
			if (endosome.area >0.1* (2*Math.PI*Math.pow(500, 2)+ 2 * Math.PI * 500 * 20))
			{
				System.out.println("ERGICCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC MATURATION  "+endosome.rabContent +endosome.xcoor+"   "+endosome.ycoor);

				//			endosome.volume = Math.PI*Math.pow(500, 2)*20; 
				endosome.rabContent.clear();
				endosome.rabContent.put("RabB", endosome.area);
//				try {
//				TimeUnit.SECONDS.sleep(2);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			}
			return;
		}
	}

	private static void maturePush(Endosome endosome, String maxRab) {
		/*
		 * Maturation triggered by the number of cisterns in the same spot. For ERGIC (RabA)
		 * it matures when a large structure in the Golgi area (xcoor in the center and ycoor near the bottom), 
		 * mature to cisGolgi and it is transported to cisGolgi grid spot. ERIC in the area has large probability of fusion
		 * cisGolgi matures when another cisGolgi cistern is in the same grid spot and so on
		 * 
		 */	
//		String maxRab = Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
		double value = endosome.rabContent.get(maxRab);
		switch (maxRab) {
		case "RabA": {
				break;
		}
		case "RabB": {
			//			System.out.println(endosome.area+"GOLGI MATURATION INICIA B"+endosome+endosome.rabContent +endosome.xcoor+endosome.ycoor);
			GridPoint pt = grid.getLocation(endosome);
			List<Endosome> allEndosomesHere = new ArrayList<Endosome>();
			for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY())) {
				if (obj instanceof Endosome) {
					allEndosomesHere.add((Endosome) obj);
				}
			}
						System.out.println("LISTA DE TODOS LOS ENDOSOMAS"+ allEndosomesHere + endosome.area);
//						try {
//							TimeUnit.SECONDS.sleep(3);
//						} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
			for (Endosome end : allEndosomesHere) {

				String maxRab2 = Collections.max(end.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
								System.out.println("ENDOSOMA A EVALUAR "+ maxRab2 + end.rabContent+ end.area);

				if (end != endosome
						&& maxRab2.equals("RabB")
						&& end.area >= 4.5E5){//(2*Math.PI*Math.pow(500, 2)+ 2 * Math.PI * 500 * 20)){					
					endosome.rabContent.clear();//put("RabB", 0d);
					endosome.rabContent.put("RabC", endosome.area);
					System.out.println("GOLGI MATURATION aqui madura BB"+endosome.rabContent +endosome.membraneContent+endosome.solubleContent);
					try {
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					break;
				}
				
				else if(end != endosome
						&& maxRab2.equals("RabB")
//						&& end.area < 1.6E6)
				){
					endosome.volume = endosome.volume + end.volume;
					endosome.area = endosome.area + end.area;
					// System.out.println(endosome.area+"  AREAS FINAL");
					endosome.rabContent = sumRabContent(endosome, end);
					endosome.membraneContent = sumMembraneContent(endosome, end);
					endosome.solubleContent = sumSolubleContent(endosome, end);
					Context<Object> context = ContextUtils.getContext(endosome);
					context.remove(end);
					System.out.println("GOLGI FUSION  BB "+endosome.rabContent +endosome.membraneContent+endosome.solubleContent);
//										try {
//											TimeUnit.SECONDS.sleep(3);
//										} catch (InterruptedException e) {
//											// TODO Auto-generated catch block
//											e.printStackTrace();
//										}
				}
			}

			break;
		}
		case "RabC": {
//			if( 4>3) break;
			System.out.println("GOLGI MATURATION  C"+endosome.rabContent +endosome.xcoor+endosome.ycoor);
			GridPoint pt = grid.getLocation(endosome);
			List<Endosome> allEndosomes = new ArrayList<Endosome>();
			for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY())) {
				if (obj instanceof Endosome) {
					allEndosomes.add((Endosome) obj);
				}
			}
			for (Endosome end : allEndosomes) {
				String maxRab2 = Collections.max(end.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
				if (end != endosome
						&& maxRab2.equals("RabC")
						&& end.area >= 4.5E5){
					endosome.rabContent.clear();//put("RabB", 0d);
					endosome.rabContent.put("RabD", endosome.area);
					System.out.println("GOLGI MATURATION  CC"+endosome.rabContent +endosome.xcoor+endosome.ycoor);
					break;
				}
				else if(end != endosome
						&& maxRab2.equals("RabC")
						&& end.area < 1E6){
					endosome.volume = endosome.volume + end.volume;
					endosome.area = endosome.area + end.area;
					// System.out.println(endosome.area+"  AREAS FINAL");
					endosome.rabContent = sumRabContent(endosome, end);
					endosome.membraneContent = sumMembraneContent(endosome, end);
					endosome.solubleContent = sumSolubleContent(endosome, end);
					Context<Object> context = ContextUtils.getContext(endosome);
					context.remove(end);
					System.out.println("GOLGI FUSION  CC"+endosome.rabContent +endosome.xcoor+endosome.ycoor);

				}
			}

				break;
			}
		case "RabD": {
//			if( 4>3) break;
			System.out.println(endosome.area+"GOLGI MATURATION INICIA D"+endosome+endosome.rabContent +endosome.birthday);
			GridPoint pt = grid.getLocation(endosome);
			List<Endosome> allEndosomes = new ArrayList<Endosome>();
			for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY())) {
				if (obj instanceof Endosome) {
					allEndosomes.add((Endosome) obj);
				}
			}
			System.out.println("LISTA DE TODOS LOS ENDOSOMAS"+ allEndosomes);
//			try {
//				TimeUnit.SECONDS.sleep(3);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			for (Endosome end : allEndosomes) {
				String maxRab2 = Collections.max(end.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
				System.out.println("ENDOSOMA A EVALUAR "+ maxRab2 + end.rabContent+ end.birthday);
				if (end != endosome
						&& maxRab2.equals("RabD")
						&& end.area >= 4.5E5){
					endosome.rabContent.clear();//put("RabB", 0d);
					endosome.rabContent.put("RabE", endosome.area);
					System.out.println("GOLGI aqui madura  DD"+endosome.rabContent +endosome.xcoor+endosome.ycoor);
					break;
				}
				else if(end != endosome
						&& maxRab2.equals("RabD")
//						&& end.area < 1E6
						){
					endosome.volume = endosome.volume + end.volume;
					endosome.area = endosome.area + end.area;
					// System.out.println(endosome.area+"  AREAS FINAL");
					endosome.rabContent = sumRabContent(endosome, end);
					endosome.membraneContent = sumMembraneContent(endosome, end);
					endosome.solubleContent = sumSolubleContent(endosome, end);
					Context<Object> context = ContextUtils.getContext(endosome);
					context.remove(end);
					System.out.println("GOLGI aqui se FUSIONA  DD"+endosome.rabContent +endosome.xcoor+endosome.ycoor);

				}
			}

				break;
			}
		case "RabE": {
//			Context<Object> context = ContextUtils.getContext(endosome);
//			context.remove(endosome);
			break;
		}
		}
	}
	
	private static HashMap<String, Double> sumRabContent(Endosome endosome1,
			Endosome endosome2) {
		// HashMap<String, Double> map3 = new HashMap<String, Double>();
		// map3.putAll(endosome1.rabContent);
		// map3.forEach((k, v) -> endosome2.rabContent.merge(k, v, (v1, v2) ->
		// v1 + v2));
		// return map3;

		HashMap<String, Double> rabSum = new HashMap<String, Double>();
		for (String key1 : endosome1.rabContent.keySet()) {
			if (endosome2.rabContent.containsKey(key1)) {
				double sum = endosome1.rabContent.get(key1)
						+ endosome2.rabContent.get(key1);
				rabSum.put(key1, sum);
			} else
				rabSum.put(key1, endosome1.rabContent.get(key1));
		}
		for (String key2 : endosome2.rabContent.keySet()) {
			if (!endosome1.rabContent.containsKey(key2)) {
				rabSum.put(key2, endosome2.rabContent.get(key2));
			}
		}

		// System.out.println("rabContentSum" + endosome1.rabContent);
		return rabSum;
	}

	private static HashMap<String, Double> sumMembraneContent(Endosome endosome1,
			Endosome endosome2) {
		HashMap<String, Double> memSum = new HashMap<String, Double>();
		for (String key1 : endosome1.membraneContent.keySet()) {
			if (endosome2.membraneContent.containsKey(key1)) {
				double sum = endosome1.membraneContent.get(key1)
						+ endosome2.membraneContent.get(key1);
				memSum.put(key1, sum);
			} else
				memSum.put(key1, endosome1.membraneContent.get(key1));
		}
		for (String key2 : endosome2.membraneContent.keySet()) {
			if (!endosome1.membraneContent.containsKey(key2)) {
				double sum = endosome2.membraneContent.get(key2);
				memSum.put(key2, sum);
			}
		}
//		// endosome1.membraneContent = memSum;
//		
//		System.out.println("MemEnd 1" +endosome1.membraneContent +
//				"\n MemEnd 2"+ endosome2.membraneContent+ 
//				" \n MemSum" + memSum);
		return memSum;
	}

	private static HashMap<String, Double> sumSolubleContent(Endosome endosome1,
			Endosome endosome2) {
		HashMap<String, Double> solSum = new HashMap<String, Double>();
		for (String key1 : endosome1.solubleContent.keySet()) {
			if (endosome2.solubleContent.containsKey(key1)) {
				double sum = endosome1.solubleContent.get(key1)
						+ endosome2.solubleContent.get(key1);
				solSum.put(key1, sum);
			} else
				solSum.put(key1, endosome1.solubleContent.get(key1));
		}
		for (String key2 : endosome2.solubleContent.keySet()) {
			if (!endosome1.solubleContent.containsKey(key2)) {
				double sum = endosome2.solubleContent.get(key2);
				solSum.put(key2, sum);
			}
		}
		// endosome1.solubleContent = solSum;
		// System.out.println("solubleContentSum" + endosome1.solubleContent);
		return solSum;
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


