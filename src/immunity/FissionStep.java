package immunity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import repast.simphony.context.Context;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.util.ContextUtils;
import repast.simphony.util.collections.Contains;

public class FissionStep {
	// Split the endosome in two
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	static double cellLimit = 3 * Cell.orgScale;
	
	public static void split(Endosome endosome) {	
		space = endosome.getSpace();
		grid = endosome.getGrid();

		
		String rabInTube = null;
		double vo = endosome.volume;
		double so = endosome.area;
		double volMincyl = 2 * Math.PI * Cell.rcyl * Cell.rcyl * Cell.rcyl;
		if (vo < 2 * volMincyl)
			return; // if too small to form two mincyl do not split. Volume of a cylinder of 2
					// cylinder radius long (almost a sphere)
		if (so < 2 * Cell.mincyl)
			return; // if the surface is less than two minimus tubules, abort
					// splitting
		double vv = vo - volMincyl;
		double ss = so - Cell.mincyl;
		if (ss * ss * ss / (vv * vv) <= 36.01 * Math.PI){ 
//			System.out.println("NO ALCANZA" + endosome.getRabContent());
			return;
		} // organelle is an sphere
// if s^3 / v^2 is equal to 36*PI then it is an sphere and cannot form a tubule
// if the area and volume after budding a minimal tubule is	less than 36*PI, then cannot form a tubule	
//		if (vo / (so - 2 * Math.PI * Cell.rcyl * Cell.rcyl) == Cell.rcyl / 2) {
//			System.out.println("tubuleTubule" + vo + " " + so);
//		}

		double rsphere = Math.pow((vo * 3) / (4 * Math.PI), (1 / 3d));// calculate
		// the radius of the sphere with a given volume

		double ssphere = (4 * Math.PI * rsphere * rsphere);// area of a sphere
															// containing the
															// volume
		if ((so - ssphere) < Cell.mincyl){
// if not enough surface to contain the volume plus a
// minimum tubule, no split
//			System.out.println("small tubule left " +so + "  " + ssphere + "  " + (so-ssphere));
			return; 
		}

		rabInTube = rabInTube(endosome); // select a rab for the tubule
		if (rabInTube == null) return; // if non is selected, no fission
		if (endosome.rabContent.get(rabInTube)<= Cell.mincyl) return; // the rab area is too small		
		double scylinder = Cell.mincyl; // surface minimum cylinder 2*radius
		// cylinder high
		double vcylinder = volMincyl; // volume	
		// minimum cylinder
		if (CellProperties.getInstance().getRabOrganelle().get(rabInTube).contains("Golgi"))
		{ // Golgi domain
			double probFission = (endosome.area - Cell.minCistern)/(2*Cell.maxCistern-Cell.minCistern);// hacer constante
			if ( Math.random() > probFission){
//		SET TO 0.9. TO BE ADJUSTED.  Small cisterns has lower probability of forming vesicles
				return;
			} 
			else
			{
				double[] areaVolume = areaVolumeCistern(endosome, rabInTube);
				scylinder = areaVolume[0];
				vcylinder = areaVolume[1];

			}
		}
		else //non Golgi domain
		{
			double[] areaVolume = areaVolumeTubule(endosome, rabInTube);
			scylinder = areaVolume[0];
			vcylinder = areaVolume[1];		
		}
	

		/*
		 * the volume of the vesicle is formed subtracting the volume of the
		 * formed cylinder from the total volume idem for the area
		 * 
		 * From the information of vcylinder and scylinder, the organelle is
		 * splitted in two, a sphere and tubule (case 2) or in two almost
		 * tubules (a pice of the lateral surface must be used to close the
		 * tubules
		 */
		double vVesicle = vo - vcylinder;
		if(vVesicle < 0 || vcylinder < 0){
			System.out.println(vVesicle +"surface and volume"+ vcylinder);	
		}
		double sVesicle = so - scylinder;
		/*
		 * FORMATION 1ST ORGANELLE (referred as sphere) the rab-in-tubule of the
		 * tubule is substracted from the original rab-in-tube content of the
		 * organelle the final proportion of the rab-in-tubule in the vesicular
		 * organelle is obtained dividein by the total surface of the vesicle
		 */
		endosome.area = sVesicle;
		endosome.volume = vVesicle;
		Endosome.endosomeShape(endosome);

		/*
		 * CONTENT DISTRIBUTION Rab in the tubule is sustracted
		 */
		double rabLeft = endosome.rabContent.get(rabInTube) - scylinder;
		if (rabLeft < 0) {
			System.out.println(rabInTube + endosome.rabContent.get(rabInTube)
					+ "surfaceCy" + scylinder);
//			System.out.println(endosome.rabContent);
		}
		endosome.rabContent.put(rabInTube, rabLeft);
		
		HashMap<String, Double> copyMembrane = new HashMap<String, Double>(
				endosome.membraneContent);
		membraneContentSplit(endosome, rabInTube, so, sVesicle);
		
		HashMap<String, Double> copySoluble = new HashMap<String, Double>(
				endosome.solubleContent);
		solubleContentSplit(endosome, rabInTube, vo, vVesicle);

		
		endosome.size = Math.pow(endosome.volume * 3d / 4d / Math.PI, (1d / 3d));

		endosome.speed = 1d / endosome.size;
//		Time series are re calculated in the next tick
		endosome.getRabTimeSeries().clear();
		endosome.getEndosomeTimeSeries().clear();

		HashMap<String, Double> newRabContent = new HashMap<String, Double>();
		newRabContent.put(rabInTube, scylinder);
		HashMap<String, Double> newInitOrgProp = new HashMap<String, Double>();
		newInitOrgProp.put("area", scylinder);
		newInitOrgProp.put("volume", vcylinder);
		HashMap<String, Double> newMembraneContent = new HashMap<String, Double>();
		for (String content : copyMembrane.keySet()) {
			newMembraneContent.put(content, copyMembrane.get(content)
					- endosome.membraneContent.get(content));
		}
		HashMap<String, Double> newSolubleContent = new HashMap<String, Double>();
		for (String content : copySoluble.keySet()) {
			newSolubleContent.put(content, copySoluble.get(content)
					- endosome.solubleContent.get(content));
		}
		Endosome b = new Endosome(endosome.getSpace(), endosome.getGrid(), newRabContent,
				newMembraneContent, newSolubleContent, newInitOrgProp);
		Context<Object> context = ContextUtils.getContext(endosome);
		context.add(b);
//		if (b.solubleContent.containsKey("ova")&& b.solubleContent.get("ova")>1000d){
//			System.out.println("OVA EN TUBULE  " + b.solubleContent.get("ova") + "  " + endosome.solubleContent.get("ova"));
//		
//		try {
//		TimeUnit.SECONDS.sleep(5);
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//		}
	
		
		b.area = scylinder;
		b.volume = vcylinder;
		Endosome.endosomeShape(b);
		b.size = Math.pow(b.volume * 3d / 4d / Math.PI, (1d / 3d));
		b.speed = 1d / b.size;
		Random rd = new Random();
//		Time series will be recalculated in the next tick
		b.getEndosomeTimeSeries().clear();
		b.getRabTimeSeries().clear();
		
		b.heading = endosome.heading + rd.nextGaussian() * 30d;
		// change the													// heading
		// of the old vesicle heading with a normal distribution
//		scale 750 nm is the 50 size space. Size in nm/15 is the size in the space scale
		double deltax = Math.cos(endosome.heading * 2d * Math.PI / 360d)
				* (endosome.size + b.size) * Cell.orgScale/15;
		double deltay = Math.sin(endosome.heading * 2d * Math.PI / 360d)
				* (endosome.size+ b.size)* Cell.orgScale/15;
		
		NdPoint myPoint = space.getLocation(endosome);
		double x = myPoint.getX()+ deltax;

		double y = myPoint.getY()+ deltay;
		if (y < cellLimit){
			y= cellLimit;
//			specific for Golgi transport.  To increase TGN volume, when split a pure TGN (RabE) tubule, near the nucleus, increase the volume in a random
//			way between 0 and the maximal volume (the volume of a sphere with the area of the tubule
//			if (rabInTube.equals("RabE")){
//				double radius = Math.sqrt(b.area/(4*Math.PI));
//				double maxVol = 4/3*Math.PI*Math.pow(radius, 3);
//				double deltaVol = maxVol-b.volume;
//				b.volume = b.volume + deltaVol;
//			}
		}
		
		if (y > 50 - cellLimit)y = 50-cellLimit;
		space.moveTo(b, x, y);
		grid.moveTo(b, (int) x, (int) y);


	}
	
	private static double[] areaVolumeCistern(Endosome endosome, String rabInTube)
	{
		/*
		 * AREA / VOLUME OF CYLINDER. 
		 * TUBULE (LONG) VERSUS CISTERN (FLAT)
		 * a = radius flat cylinder; c = length long cylinder/2; 
		 * r fixed to Cell.rcyl, it is the radius of the tubules and the height/2 of the cistern
		 * 
		 * AREA
		 * tubule 2*PI*r^2 + 2*PI*r*2*c
		 * cistern 2*PI*a^2 + 2*PI*a*2*r
		 * 
		 * VOLUME
		 * tubule PI*r^2 *2*c
		 * cistern PI*a^2 *2*r
		 * 
		 * AREA/VOLUME
		 * tubule (area-(2*PI*r^2))/volume = 2/r  for long tubules area/volume = 2/r
		 * cistern area/volume = 1/r + 2/a for large cistern, were a>>r, area/volume = 1/r
		 * So, a long tubule has a area/volume ratio about twice of the cistern that can be build with the same area and volume
		 * The volume of the cistern need to be smaller, and for that, the height decreased.
		 * 

		 * If it is a Golgi Rab and RabArea > minCyl
		 * 1- NOT IMPLEMENTED If it is a cistern, generate Golgi vesicle with probability proportional to perimeter (2*PI*a) and return
		 * 2- if the vesicle was not generated or the organelle is not a cistern, split a cistern with the rab selected
		 * with the area of the selected rab, leaving at list a vesicle
		 *  */


		//{
//		    System.out.println("Roots are real and unequal");
//		    root1 = ( - b + Math.sqrt(d))/(2*a);
//		    root2 = (-b - Math.sqrt(d))/(2*a);
//		    System.out.println("First root is:"+root1);
//		    System.out.println("Second root is:"+root2);
		//}   
		
		if (Math.random()<0.9){// standard 0.9
// high probability of forming a single vesicle.  SET TO 0.9
			return new double[] {Cell.mincyl, 2 * Math.PI * Math.pow(Cell.rcyl, 3)};
		}
		else
		{
			double vo = endosome.volume;
			double so = endosome.area;
			double rsphere = Math.pow((vo * 3) / (4 * Math.PI), (1 / 3d));// calculate
			// the radius of the sphere with a given volume
			double ssphere = (4 * Math.PI * rsphere * rsphere);// area of a sphere
																// containing the
																// volume
			double scylinder = 0; 
			double vcylinder = 0;	
			do{
				vcylinder = vcylinder + 2 * Math.PI * Math.pow(Cell.rcyl, 3);// volume of minimal cylinder PI*rcyl^2*2*rcyl
				// add a minimal volume
				double aradius =Math.sqrt(vcylinder /(2*Math.PI*Cell.rcyl)); // from vcylinder = PI*aradius^2 * cistern height (2 rcyl)
				scylinder = 2*Math.PI*aradius*aradius + 4*Math.PI*aradius*Cell.rcyl;//from Scyl = 2*PI*aradius^2+4*PI*aradius*rcyl
System.out.println("SPLIT CISTERN vo"+vo+"  so  "+so+"  vcylinder "+vcylinder+"  scylinder "+ scylinder);
							
				// System.out.println(scylinder +"surface and volume"+ vcylinder);
			}
			while (
					(so - ssphere - scylinder > 4 * Math.PI * Math.pow(Cell.rcyl, 2))
					// organelle area should be enough to cover the volume (ssphere)
					// plus the cylinder already formed (scylinder) and to
					// elongate a two r cylinder (without caps)
					&&(endosome.rabContent.get(rabInTube) - scylinder > 4 * Math.PI * Math.pow(Cell.rcyl, 2))// the Rab area should b enough
					// to cover the minimum cylinder and to elongate a two r cylinder
					&& ((vo - vcylinder - 2 * Math.PI * Math.pow(Cell.rcyl, 3))>4 * Math.PI * Math.pow(Cell.rcyl, 3))
					&& ((vo - vcylinder)/((so-scylinder)-2*Math.PI*Cell.rcyl*Cell.rcyl)>Cell.rcyl/2)); 
// volume left cannot be smaller than the volume of the mincyl and cannot be less than the volume of a tubule with the remaining area
//				 * while there is enough membrane and enough rab surface, the tubule grows

			
//			double area = endosome.getRabContent().get(rabInTube);
//			if (endosome.area - area < Cell.mincyl) area = area - Cell.mincyl; 
//			{
////				 * AREA (to find a (radius cylinder, r half height of cistern)
////				 * cistern 2*PI* x^2 + 2*PI*r*2*r x  +  (-area) = 0
//				double aq = 2d*Math.PI;
//				double bq = 4*Math.PI*Cell.rcyl;
//				double cq = -area;
//				double dq =  bq * bq - 4 * aq * cq;
//				double root1 = ( - bq + Math.sqrt(dq))/(2*aq);
////			    root2 = (-b - Math.sqrt(d))/(2*a);
////			    VOLUME
//				double volume = Math.PI*root1*root1*2*Cell.rcyl;
//System.out.println("SPLIT CISTERN vo "+ vo +"  so  "+so+"  vcylinder "+vcylinder+"  scylinder "+ scylinder);
				return new double[] {scylinder, vcylinder};		
			}

//		return new double[] {Cell.mincyl, 2 * Math.PI * Math.pow(Cell.rcyl, 3)};
		}

	
	
	private static double[] areaVolumeTubule(Endosome endosome, String rabInTube)
	{
//		the organelles is assessed to be a tubule for the vo/so relationship. If it is a 
//		tubule, the rules for splitting are different
		double vo = endosome.volume;
		double so = endosome.area;
		double rsphere = Math.pow((vo * 3) / (4 * Math.PI), (1 / 3d));// calculate
		// the radius of the sphere with a given volume

		double ssphere = (4 * Math.PI * rsphere * rsphere);// area of a sphere
															// containing the
															// volume
		double scylinder = Cell.mincyl; // surface minimum cylinder 2*radius
		// cylinder high
		double vcylinder = 2 * Math.PI * Math.pow(Cell.rcyl, 3); // volume	

		
		if (vo / (so - 2 * Math.PI * Cell.rcyl * Cell.rcyl) <= Cell.rcyl / 2) {// should be 2
//			if it is a tubule
			if (endosome.rabContent.get(rabInTube)>= endosome.area/2){
//				if it is a tubule and the rab selected has enough area, divide in two
				vcylinder = endosome.volume/2;
				scylinder = endosome.area/2;

				System.out.println("tubule cut in two");
				 return new double[] {scylinder, vcylinder};
			}
			else {
//				if it is a tubule and the Rab selected is not enough, generate a tubule with the 
//				available Rab area
				scylinder = endosome.rabContent.get(rabInTube);
//				vcylinder = vo* endosome.rabContent.get(rabInTube)/endosome.area;
				double ss = scylinder - 2*Math.PI*Cell.rcyl*Cell.rcyl;//available surface without the two caps
				double h = ss/(2*Math.PI*Cell.rcyl);// height of the cylinder from the available surface
				vcylinder = Math.PI*Cell.rcyl*Cell.rcyl*h; // volume of the cylinder from the height
				System.out.println("tubule cut assymetric" + scylinder +" "+ vcylinder
						+" "+ vo);
				return new double[] {scylinder, vcylinder};
			}
		}
		else // following rules are for an organelle that is not a tubule
		{
		while ((so - ssphere - scylinder > 4 * Math.PI * Math.pow(Cell.rcyl, 2))
				// organelle area should be enough to cover the volume (ssphere)
				// to cover the cylinder already formed (scylinder) and to
				// elongate a two r cylinder (without caps)
				&& (endosome.rabContent.get(rabInTube) - scylinder > 4 * Math.PI
						* Math.pow(Cell.rcyl, 2))// the Rab area should b enough
				// to cover the minimum cylinder and to elongate a two r cylider
				&& (scylinder < 0.5 * so) // the area of the cylinder must not
											// be larger than 50% of the total
											// area
				&& ((vo - vcylinder - 2 * Math.PI * Math.pow(Cell.rcyl, 3))>4 * Math.PI * Math.pow(Cell.rcyl, 3))
				&& Math.random()< 0.9 //cut the tubule with a 10% probability in each step.  Prevent too long tubules
				) {
//			/ ((so - scylinder - 4 * Math.PI
//					* Math.pow(Cell.rcyl, 2)) - 2 * Math.PI
//					* Cell.rcyl * Cell.rcyl) > Cell.rcyl / 2
			// volume left cannot be smaller than the volume
			// of the mincyl
			/*
			 * while there is enough membrane and enough rab surface, the tubule
			 * grows
			 */

			scylinder = scylinder + 4 * Math.PI * Math.pow(Cell.rcyl, 2);
			// add a cylinder without caps (the caps were considered in
			// the mincyl
			vcylinder = vcylinder + 2 * Math.PI * Math.pow(Cell.rcyl, 3);
			// add a volume
			// System.out.println(scylinder +"surface and volume"+ vcylinder);
		}
		}
		return new double[] {scylinder, vcylinder};	
	}

	private static void membraneContentSplit(Endosome endosome, String rabInTube, Double so, Double sVesicle) {
		// MEMBRANE CONTENT IS DISTRIBUTED according rabTropism
		// new with a copy of endosome.membraneContent;
		// copy rabTropism from CellProperties
		HashMap<String, Double> copyMembrane = new HashMap<String, Double>(
				endosome.membraneContent);
		HashMap<String, Set<String>> rabTropism = new HashMap<String, Set<String>>(
				CellProperties.getInstance().getRabTropism());
		for (String content : copyMembrane.keySet()) {
// if no especification, even distribution proportional to the area
			if (!rabTropism.containsKey(content)){
				splitPropSurface(endosome, content, so, sVesicle);	
			}
// if tropism to tubule, the content goes to tubule			
			else if (rabTropism.get(content).contains("tub")){
				splitToTubule(endosome, content, so, sVesicle);
			}
// if tropism to sphere, the content goes to the vesicle		
			else if (rabTropism.get(content).contains("sph")){
				splitToSphere(endosome, content, so, sVesicle);
			}
			else 
// finally, if tropism it to a Rab membrane domain, the decision about where to go
// requires to calculate the tropism to the vesicle (SUM of the content tropism to all the
//	membrane domains in the vesicle.  The tropism is indicated by a string (e.g. RabA10) where
//	the last two digits indicate the affinity for the Rab domain in a scale of 00 to 10.			
			{
			double sphereTrop = 0;
			for (String rabTrop : rabTropism.get(content)){
				if (rabTrop.equals("mvb")) continue;
					String rab = rabTrop.substring(0, 4);
					if (endosome.rabContent.containsKey(rab)){
						sphereTrop = sphereTrop + endosome.rabContent.get(rab)/endosome.area*
								Integer.parseInt(rabTrop.substring(4, 6));
//						System.out.println("Trop Number " + Integer.parseInt(rabTrop.substring(4, 6)));
					}
			}
// the tropism to the tubule is directly the two digits of the Rab selected for the tubule 
			double tubuleTrop = 0;
			for (String rabTrop : rabTropism.get(content)){
				if (rabTrop.equals("mvb")) continue;
				String rab = rabTrop.substring(0, 4);
				if (rab.equals(rabInTube)){
					tubuleTrop = Integer.parseInt(rabTrop.substring(4, 6));
				}
			}
//			System.out.println("sphere tubule " + sphereTrop +" "+ tubuleTrop+ " " +(tubuleTrop-sphereTrop));
// the tropismo is to vesicle or to tubule according to the following rules
// if not clear tropism to tubule or sphere, even distribution			
			if (tubuleTrop < 2d && sphereTrop < 2d ) 
				{	
//				System.out.println("to even ");
				splitPropSurface(endosome, content, so, sVesicle);	
				}
//	if the difference tubule-sphere is larger than 4 and sphere less than 2, to tubule		
			else if (sphereTrop < 2d || (tubuleTrop-sphereTrop) > 4d)
				{
//				System.out.println("to tubule ");
				splitToTubule(endosome, content, so, sVesicle);
				}
//	if the difference tubule-sphere is less than -4 and tubule less than 2, to vesicle			
			else if (tubuleTrop < 2d || (tubuleTrop-sphereTrop) < -4d)
				{
//				System.out.println("to sphere ");
				splitToSphere(endosome, content, so, sVesicle);
				}
//	if no clear differences, even distribution	
			else { 
//				System.out.println("to even even ");
				splitPropSurface(endosome, content, so, sVesicle);}
			}
	}
					
					

		
	}

	private static void solubleContentSplit(Endosome endosome, String rabInTube, double vo, double vVesicle){
		// SOLUBLE CONTENT IS DISTRIBUTED according rabTropism
		HashMap<String, Double> copySoluble = new HashMap<String, Double>(
						endosome.solubleContent);
		HashMap<String, Set<String>> rabTropism = new HashMap<String, Set<String>>(
						CellProperties.getInstance().getRabTropism());
				
		for (String content : copySoluble.keySet()) {
			if (!rabTropism.containsKey(content)) 
			{// not a
				// specified tropism or no tropism for the rabInTube,
				// hence, distribute according to
				// the volume ratio
				SolSplitPropVolume(endosome, content, vo, vVesicle);
			}

			else if (rabTropism.get(content).contains("tub")) 
			{
				SolSplitToTubule(endosome, content, vo, vVesicle);

			}
			else if (rabTropism.get(content).contains("sph")) { // if the tropism
				// is "0" goes
				// to the sphere

				SolSplitToSphere(endosome, content, vo, vVesicle);	

			}
			else{// rabtropism for the content is not "tub" or "sph", then distribute according to volume 
				SolSplitPropVolume(endosome, content, vo, vVesicle);
			}
		}


	}

	private static void SolSplitPropVolume(Endosome endosome, String content, double vo, double vVesicle){
		HashMap<String, Double> copySoluble = new HashMap<String, Double>(
				endosome.solubleContent);
//		HashMap<String, Set<String>> rabTropism = new HashMap<String, Set<String>>(
//				CellProperties.getInstance().getRabTropism());
		if (content.equals("solubleMarker")
				&& (endosome.solubleContent.get("solubleMarker") > 0.9)) {
			if (Math.random() < vVesicle / vo) endosome.solubleContent.put(content, 1d);
			} 
		else 
		{
			endosome.solubleContent.put(content, copySoluble.get(content)
					* (vVesicle) / vo);
		}

	}
	private static void SolSplitToSphere(Endosome endosome, String content, double vo, double vVesicle){
		HashMap<String, Double> copySoluble = new HashMap<String, Double>(
				endosome.solubleContent);
//		HashMap<String, Set<String>> rabTropism = new HashMap<String, Set<String>>(
//				CellProperties.getInstance().getRabTropism());
		
		
//		if (copySoluble.get(content) > vVesicle) {
//			endosome.solubleContent.put(content, vVesicle);
//		} else
			endosome.solubleContent.put(content,
					copySoluble.get(content));
	}
	
	private static void SolSplitToTubule(Endosome endosome, String content, double vo, double vVesicle){
		HashMap<String, Double> copySoluble = new HashMap<String, Double>(
				endosome.solubleContent);
//		HashMap<String, Set<String>> rabTropism = new HashMap<String, Set<String>>(
//				CellProperties.getInstance().getRabTropism());
		double vcylinder = vo - vVesicle;
		if (copySoluble.get(content) > vcylinder) {
			endosome.solubleContent.put(content,
					copySoluble.get(content) - vcylinder);
		} else
			endosome.solubleContent.put(content, 0.0d);
	}
	
	
	private static void splitToTubule(Endosome endosome, String content, double so, double sVesicle) {
		HashMap<String, Double> copyMembrane = new HashMap<String, Double>(
				endosome.membraneContent);
//		HashMap<String, Set<String>> rabTropism = new HashMap<String, Set<String>>(
//				CellProperties.getInstance().getRabTropism());
		double scylinder = so - sVesicle;
		if (copyMembrane.get(content) > scylinder) {
			endosome.membraneContent.put(content,
					copyMembrane.get(content) - scylinder);
		} else
			endosome.membraneContent.put(content, 0.0d);
		// TODO Auto-generated method stub
		
	}

	private static void splitToSphere(Endosome endosome, String content, double so, double sVesicle) {
		HashMap<String, Double> copyMembrane = new HashMap<String, Double>(
				endosome.membraneContent);
//		HashMap<String, Set<String>> rabTropism = new HashMap<String, Set<String>>(
//				CellProperties.getInstance().getRabTropism());
		if (copyMembrane.get(content) > sVesicle) {
			endosome.membraneContent.put(content, sVesicle);
		} else
			endosome.membraneContent.put(content,
					copyMembrane.get(content));
		
	}

	private static void splitPropSurface(Endosome endosome, String content, Double so, Double sVesicle) {
			
		HashMap<String, Double> copyMembrane = new HashMap<String, Double>(
				endosome.membraneContent);

		if (content.equals("membraneMarker")
				&& (endosome.membraneContent.get("membraneMarker") > 0.9)) {
			if (Math.random() < sVesicle / so)
				endosome.membraneContent.put("membraneMarker", 1d);
			else {endosome.membraneContent.put("membraneMarker", 0d);}
			} 
		else {
			endosome.membraneContent.put(content, copyMembrane.get(content)
						* (sVesicle) / so);	
		}
			
	
	}

	public static String rabInTube(Endosome endosome) {
		HashMap<String, Double> copyMap = new HashMap<String, Double>(
				endosome.rabContent);
		// copyMap.putAll(endosome.rabContent);
		String rab = null;
		// System.out.println("CopyMap "+copyMap);
		for (String rab1 : endosome.rabContent.keySet()) {
			if (copyMap.get(rab1) < Cell.mincyl) {
				copyMap.remove(rab1);
			}
		}
		if (copyMap.isEmpty()) {
			System.out.println("NINGUN RAB " + copyMap);
			return null;
		}

		if (copyMap.size() < 2) {

			for (String rab1 : copyMap.keySet()) {
				System.out.println("UNICO RAB " + copyMap);
				return rab1;
			}
		}

		else {
			
			List<String> keys = new ArrayList(copyMap.keySet());
			Collections.shuffle(keys);			

//				Picks a Rab domain according to the relative "tubule tropism" of the Rab domains present
//				Rabs with larger tubule tropism have more probability of being selected


				double totalTubuleTropism = 0d;
//				add all the tubule tropisms of the rab domains
				for (String rab1 : keys) {
					totalTubuleTropism = totalTubuleTropism + CellProperties.getInstance().getTubuleTropism().get(rab1);
				}

// 				select a random number between 0 and total tubule tropism.  Notice that it can be zero
				double rnd = Math.random() * totalTubuleTropism;
				double tubuleTropism = 0d;
//				select a rab domain with a probability proportional to its tubule tropism
				for (String rab1 : keys){
				tubuleTropism = tubuleTropism + CellProperties.getInstance().getTubuleTropism().get(rab1);
					if (rnd <= tubuleTropism){
						System.out.println(copyMap + " RabInTubeSelected " + rab1);
						return rab1;
					}
				}

			}
			
		return null;// never used
			
			
			
//			List keys = new ArrayList(copyMap.keySet());
//			Collections.shuffle(keys);
//			while (rab == null) {
//				for (Object rab1 : keys) {
////					System.out.println(rab1 + " "+ tubuleTropism);
//					if (Math.random() < CellProperties.getInstance().getTubuleTropism().get(rab1)) {
//						System.out.println(copyMap + "RabInTubeSelected" + rab1);
//						return (String) rab1;
//					}
//				}
//			}
//		}
//
//		return null;
	}

}
