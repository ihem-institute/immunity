package immunity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import repast.simphony.context.Context;
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
				&& endosome.area > 1E5
				&& organelleName.contains("Golgi")){
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
			|| endosome.area< 1E05)
		{
			return;
		}
		else{
			System.out.println("ERGICCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC MATURATION  "+endosome.rabContent +endosome.xcoor+"   "+endosome.ycoor);
			// change the area and volume to a cistern
			// 500 nm radius, 20 nm high.  OJO NO CONSIDERO ESCALA, HABRIA QUE MEJORAR
			endosome.area = 2*Math.PI*Math.pow(500, 2)+ 2 * Math.PI * 500 * 20; 
			endosome.volume = Math.PI*Math.pow(500, 2)*20; 
			endosome.rabContent.clear();
			endosome.rabContent.put("RabB", endosome.area);
			return;
		}
	}

	private static void matureAmount(Endosome endosome, String maxRab) {
		/*
		 * Maturation triggered by the number of cisterns in the same spot. For ERGIC (RabA)
		 * it matures when a large structure in the Golgi area (xcoor in the center and ycoor near the bottom), 
		 * mature to cisGolgi and it is transported to cisGolgi grid spot.
		 * cisGolgi matures when another cisGolgi cistern is in the same grid spot
		 * 
		 */	
//		String maxRab = Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
		double value = endosome.rabContent.get(maxRab);
		switch (maxRab) {
		case "RabA": {
			if (endosome.xcoor < 10
				|| endosome.xcoor > 40
				|| endosome.ycoor > 5
				|| endosome.area < 1E05)
			{
				break;
			}
			else
			{
				System.out.println("ERGICCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC MATURATION  "+endosome.rabContent +endosome.xcoor+"   "+endosome.ycoor);
				endosome.rabContent.clear();
				endosome.rabContent.put("RabB", endosome.area);
				break;
			}
		}
		case "RabB": {
			System.out.println("GOLGI MATURATION  B"+endosome.rabContent +endosome.xcoor+endosome.ycoor);
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
						&& maxRab2 == "RabB"
						&& end.area >= 1E5){
					endosome.rabContent.clear();//put("RabB", 0d);
					endosome.rabContent.put("RabC", endosome.area);
					System.out.println("GOLGI MATURATION  BB"+endosome.rabContent +endosome.xcoor+endosome.ycoor);
					break;
				}
			}

			break;
		}
		case "RabC": {
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
						&& maxRab2 == "RabC"
						&& end.area >= 1E5){
					endosome.rabContent.clear();//put("RabB", 0d);
					endosome.rabContent.put("RabD", endosome.area);
					System.out.println("GOLGI MATURATION  CC"+endosome.rabContent +endosome.xcoor+endosome.ycoor);
					break;
				}
			}

			break;
		}
		case "RabD": {
			System.out.println("GOLGI MATURATION  D"+endosome.rabContent +endosome.xcoor+endosome.ycoor);
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
						&& maxRab2 == "RabD"
						&& end.area >= 1E5){
					endosome.rabContent.clear();//put("RabB", 0d);
					endosome.rabContent.put("RabE", endosome.area);
					System.out.println("GOLGI MATURATION  DD"+endosome.rabContent +endosome.xcoor+endosome.ycoor);
					break;
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


