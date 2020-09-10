package immunity;


public class EndosomeMaturationStep {
	
	public static void matureCheck (Endosome endosome) {
		if (!endosome.rabContent.containsKey("RabA")) {return;}
		double relativeRabA=endosome.rabContent.get("RabA")/endosome.area;
		if(relativeRabA>0.9
				&& Math.random()< endosome.tickCount / 5000) mature(endosome);
//			endosome.tickCount+=1;
			//System.out.println("NOMBRE "+this.getName()+" Relative RabA  "+relativeRabA+" Cuenta  "+this.getTickCount());
		}
//		if (endosome.tickCount>1000) {
////			double z=Math.random();
////			if (z<endosome.p_EndosomeMaturationStep) {
//
////			}
//		}
//	}
	
	public static void mature (Endosome endosome) {
		//System.out.println("MADUROOOOO");
		//System.out.println("NOMBRE "+endosome.getName()+" Cuenta  "+endosome.getTickCount()+" Area  "+endosome.getArea());
		//System.out.println(endosome.getRabContent());
		double raba=endosome.getRabContent().get("RabA");
		double rabd = 0;
		if (!endosome.rabContent.containsKey("RabD")) rabd = 0d;
		else rabd=endosome.getRabContent().get("RabD");
		endosome.getRabContent().put("RabD", raba+rabd);
		endosome.getRabContent().put("RabA", 0d);
		endosome.setTickCount(0);
		//System.out.println(endosome.getRabContent());
	}
}
