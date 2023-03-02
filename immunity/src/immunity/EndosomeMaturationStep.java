package immunity;

import java.util.Collections;
import java.util.Map;

public class EndosomeMaturationStep {
	
	public static void matureCheck (Endosome endosome) {
//		if too young return
		if (Math.random() > endosome.tickCount / 3000) {return;}
		String maxRab = Collections.max(endosome.rabContent.entrySet(), Map.Entry.comparingByValue()).getKey();
//		if the maxRab is not prevalent, return
		if (endosome.rabContent.get(maxRab)/endosome.area < 0.5) return; //LUIS ERA 0.9
//		Maturation according to the maxRab. First argument (oldRab) is the Rab that matures to the second argument (newRab)
//		The third argument is the proportion of the total domain that matures.  Most for Rab5-Rab7 and the Golgi domains.  Only
//		10% for Rab5-Rab22).5% for RabB-RabC
		switch (maxRab)
		{
		case "RabA":
			if (Math.random()<0.9)
			mature(endosome, "RabA", "RabB", 0.05);// EE to SE era 0.1
			else
			mature(endosome, "RabA", "RabD", 0.9);// EE to LE
			break;
		case "RabI":
			mature(endosome, "RabI", "RabH", 0.8);//ERGIC to cisG was 0.9 for all Golgi
			break;
		case "RabH":
			mature(endosome, "RabH", "RabG", 0.8);//cisG to medialG
			break;
		case "RabG":
			mature(endosome, "RabG", "RabF", 0.8);//medialG to transG
			break;
		case "RabF":
			mature(endosome, "RabF", "RabE", 0.8);//transG to TGN
			break;
		case "RabB":
			mature(endosome, "RabB", "RabC", 0.04);//SE to RE era 0.05// luego 0.015 que anda bien
			break;			
			
		 default: return;
		}
		
//		double relativeRabA=endosome.rabContent.get("RabA")/endosome.area;
//		if(relativeRabA>0.9
//				&& Math.random()< endosome.tickCount / 5000) mature(endosome);
////			endosome.tickCount+=1;
//			//System.out.println("NOMBRE "+this.getName()+" Relative RabA  "+relativeRabA+" Cuenta  "+this.getTickCount());
		}

	public static void mature (Endosome endosome, String rabOldName, String rabNewName, double propMature) {
	//	System.out.println("MADUROOOOO");
	//	System.out.println("NOMBRE "+ rabOldName+rabNewName+" Cuenta  "+endosome.getTickCount()+" Area  "+endosome.getArea());
	//	System.out.println(endosome.getRabContent());
		
//		The logic is that a percentage (propMature) of the major domain matures
//		the rest is preserved. The remaining domain can prevent miss targeting of membrane cargoes
//		The tickCount is reset but not to zero.
		double rabOld=endosome.getRabContent().get(rabOldName);
		double rabNew = 0;
		if (!endosome.rabContent.containsKey(rabNewName)) rabNew = 0d;// checks if the organelle already has the new domain
		else rabNew=endosome.getRabContent().get(rabNewName);
		endosome.getRabContent().put(rabNewName, rabOld*propMature+rabNew);
		endosome.getRabContent().put(rabOldName, rabOld*(1-propMature));
//		The tickCount is reset to a certain value considering the the proportion of the 
//		maturation of the major domain.  This prevent that a small area maturation will reset the
//		tickCount to zero
		endosome.setTickCount((int) (endosome.tickCount*(1-propMature)));
//		System.out.println(endosome.getRabContent());
	}
	
}
