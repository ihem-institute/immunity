package immunity;

public class EndosomeSwelling {

	public static void endosomeSwell(Endosome endosome) {
		double cc = 10d; // Minimum half high of a cistern or an ellipsoid

		if (endosome.c < cc){
			////			 * AREA (to find a (radius cylinder, r half height of cistern)
			// here we want to calculate a, the perpendicular axis of the rotating cylinder
			// cylinder area =  2*PI* a^2 (the two circles) + 2*PI*r*2* a (the side) 
			// to solve the cuadratic equation 	2*PI* x^2 + 2*PI*r*2* x - area = 0
			double aq = 2d*Math.PI;
			double bq = 4*Math.PI*cc;
			double cq = -endosome.area;
			double dq =  bq * bq - 4 * aq * cq;
			double aa = (-bq + Math.sqrt(dq))/(2*aq);
			if (aa <= 0){
				aa = (-bq - Math.sqrt(dq))/(2*aq);
			}
			////		    root2 = (-b - Math.sqrt(d))/(2*a);
			////		    VOLUME
			//			double volume = Math.PI*root1*root1*2*Cell.rcyl;			
			// from the volume obtain c, the rotation axis
			endosome.c = cc;
			endosome.a = aa;
			endosome.volume = Math.PI * aa * aa * 2 * cc;// from volume of cylinder of aa radius v = PI*(aa^2)*2*cc			

		}


	}



}
