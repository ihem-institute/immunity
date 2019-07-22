package immunity;

import java.util.HashMap;

public class MaturationStep {
	public static void mature(Endosome endosome) {

	HashMap<String, Double> rabContent = new HashMap<String, Double>(endosome.getRabContent());
	if (rabContent.containsKey("RabD"))
	{
		if (!rabContent.containsKey("RabE")) rabContent.put("RabE", 0d); 
		Double rabDmatur = rabContent.get("RabD")*0.01;
		Double valueRabD = rabContent.get("RabD") - rabDmatur;
		endosome.rabContent.put("RabD", valueRabD);
		Double valueRabE = rabContent.get("RabE") + rabDmatur;
		endosome.rabContent.put("RabE", valueRabE);
		return;
	}
	if (rabContent.containsKey("RabE"))
	{
		if (!rabContent.containsKey("RabD")) rabContent.put("RabD", 0d); 
		Double rabEmatur = rabContent.get("RabE")*0.1;
		Double valueRabE = rabContent.get("RabE") - rabEmatur;
		endosome.rabContent.put("RabE", valueRabE);
		Double valueRabD = rabContent.get("RabD") + rabEmatur;
		endosome.rabContent.put("RabD", valueRabD);
		return;
	}
	else return;	
		
	}

}
