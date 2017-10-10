package immunity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;

public class EndosomeMove {

	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	private static List<MT> mts;
	public static double cellLimit = 3 * Cell.orgScale;
	
	public static void moveTowards(Endosome endosome) {
		space = endosome.getSpace();
		grid = endosome.getGrid();
		/*
		 * Direction in Repast 0 to the right 180 to the left -90 down +90 up
		 * Move with random speed inversely proportional to the radius of an sphere with the endosome
		 * volume.  The speed of a small organelle of radius 20 nm is taken as unit.  
		To move, three situations are considered
		1- Near the borders, the movement is: speed random between 0 and a value that depends on the endosome size
		heading, the original heading plus a random number that depends on the momentum
		2- Away of microtubules is the same than near borders
		3- Near MT, the speed is fixed and the heading is in the direction of the Mt or 180 that of the Mt
		 */

		NdPoint myPoint = space.getLocation(endosome);
		
		double x = myPoint.getX();
		double y = myPoint.getY();
		

//	If near the borders, move random (only with 10% probability)
		if (y > 50-cellLimit || y < cellLimit) {
			changeDirectionRnd(endosome);
		}
		else
//			if not near the borders
		{
			boolean onMt = false;
			changeDirectionMt(endosome);

		}
		
//		Having the heading and speed, make the movement.  If out of the space, limit
//		the movement
			if (endosome.speed == 0) return;// random movement 90% of the time return speed=0
			myPoint = space.getLocation(endosome);
			x = myPoint.getX();
			y = myPoint.getY();
		    double xx = x + Math.cos(endosome.heading * Math.PI / 180d)
			* endosome.speed*Cell.orgScale/Cell.timeScale;
		    double yy = y + Math.sin(endosome.heading * Math.PI / 180d)
			* endosome.speed * Cell.orgScale/Cell.timeScale;	
		    if (yy >= 50-cellLimit) yy = 50 -cellLimit;
			if (yy <= 0+cellLimit) yy = cellLimit;
		
		space.moveTo(endosome, xx, yy);
		grid.moveTo(endosome, (int) xx, (int) yy);
	}
	
	public static void changeDirectionRnd(Endosome endosome) {
//		90% of the time, the speed is 0 and the endosome does not move
		if (Math.random()<0.9) {
			endosome.speed = 0;
			return;
		}
		double initialh = endosome.heading;
		endosomeShape(endosome);

// when near the borders or no MT is nearby, the organelle rotates randomly
// according with i) its present heading, ii) a gaussian random number (0+- 30degree/momentum) 
//	As unit momentum I take that of a sphere of radius 20.
//	Momentum of a ellipsoid = volume*(large radius^2 + small radius^2)/5.  For the sphere or radius 20
//	4/3*PI*r^3*(20^2+20^2)/5 = 26808257/5 = 5.361.651.
//		To prevent the tubules to move, I did not consider the volume in the calculation
//		then a 20 nm sphere has a "pseudo" momentum of 800
//NEW RULE FOR RANDOM CHANGE OF HEADING
//A free rnd movement 360.  The probability decrease with size
//An inertial movement.  Gaussian arround 0 with an angle that decreases with size
//An inertial movement depending on the momentum.  Gaussian around 0 or 180

			double momentum = (endosome.a * endosome.a + endosome.c * endosome.c)/800;
				// if (momentum < 0.5 && c>21) System.out.println("momentum  " +
				// momentum+" "+a+"  "+c);
			Random fRandom = new Random();
			double finalh = 0;
			if (Math.random() < 1d/endosome.size ) finalh = Math.random()*360;// rnd depending  size
			finalh = finalh + fRandom.nextGaussian() * 45d*1d/endosome.size;// inertial depending size
			int dir = 1;
			if (Math.random() < 0.5 ) {dir = -1;}
			finalh = finalh + fRandom.nextGaussian() * dir * 1d * 800d/momentum;// inertial depending momentum
			finalh = initialh + finalh;
			if (finalh <-180) endosome.heading = 360d - finalh;
			if (finalh > 180) endosome.heading = finalh- 360d;
					

				// if (initial - heading >
				// 90)System.out.println("GIRO sin MT "+initial+"  "+heading+"  "+momentum);
// The speed is random between 0 and a value inversely proportional to the endosome size
			endosome.speed = 20d/endosome.size*Math.random()* Cell.orgScale/Cell.timeScale;
			return;
		}
	
	public static void changeDirectionMt(Endosome endosome){
//		boolean onMt = false;
		if (mts == null) {
			mts = associateMt();
		}
		int mtDir = 0;
/*
 * mtDirection decides if the endosome is going to move to the (-) end
 * of the MT (dyneine like or to the plus end (kinesine like). -1 goes
 * to the nucleus, 1 to the PM
 * 
 */
// not used now mtDir = mtDirection(endosome);
		Collections.shuffle(mts);
		for (MT mt : mts) {
			double dist = distance(endosome, mt);
//			System.out.println("distance BEFORE "+ dist+"  " +mt.getMtheading());
//			The distance is in space units from 0 to 50. At scale 1, the space is 2250 nm.  At 
//			scale 0.5 it is 4500 nm.
//			Hence to convert to nm, I must multiply by 45 (2250/50) and divide by scale. An organelle will sense MT
//			at a distance less than its size.
			if (Math.abs(dist*45d/Cell.orgScale) < endosome.size) {


//direction is fixed to the surface for tubules and to the center for the rest
				if (endosome.volume/(endosome.area - 2*Math.PI*Cell.rcyl*Cell.rcyl) <=Cell.rcyl/2)
					{
					mtDir = 0;
					} // if no a tubule, goes to the nucleus
				else
					{
					mtDir = 1;
					}
//				Changes the heading to the heading of the MT
//				Moves the endosome to the MT position
				double mth = mt.getMtheading();
				double yy = dist * Math.sin((mth+90)* Math.PI/180);
				double xx = dist * Math.cos((mth+90)* Math.PI/180);
				NdPoint pt = space.getLocation(endosome);
				double xpt = pt.getX()-xx;
				double ypt = pt.getY()-yy;
			    if (ypt >= 50-cellLimit) ypt = 50 -cellLimit;
				if (ypt <= 0+cellLimit) ypt = cellLimit;
				space.moveTo(endosome, xpt, ypt);
				grid.moveTo(endosome, (int) xpt, (int) ypt);
				dist = distance(endosome, mt);
//				Changes the speed to a standard speed in MT independet of size
				endosome.speed = 1d*Cell.orgScale/Cell.timeScale;
				endosome.heading = -(mtDir * 180f + mt.getMtheading());
				return;
			}
		}
//		If no Mts, then random
		changeDirectionRnd(endosome);
		return ;
	}
	// Not used!!
	public static int mtDirection(Endosome endosome) {
		double mtd = 0d;
		int mtDirection = 0;
		for (String rab : endosome.rabContent.keySet()) {
			mtd = mtd + CellProperties.getInstance().mtTropism.get(rab) * endosome.rabContent.get(rab) / endosome.area;
		}
		if (mtd > Math.random() * 2 - 1)
			return mtDirection = -1;
		else
			return mtDirection = 1;
	}
	public static List<MT> associateMt() {
		List<MT> mts = new ArrayList<MT>();
		for (Object obj : grid.getObjects()) {
			if (obj instanceof MT) {
				mts.add((MT) obj);
			}
		}
		// System.out.println(mts);
		return mts;
	}

	public static void endosomeShape(Endosome end) {
		double s = end.area;
		double v = end.volume;
		double rsphere = Math.pow((v * 3) / (4 * Math.PI), (1 / 3d));
		double svratio = s / v; // ratio surface volume
		double aa = rsphere; // initial a from the radius of a sphere of volume
								// v
		double cc = aa;// initially, c=a
		// calculation from s/v for a cylinder that it is the same than for an
		// ellipsoid
		// s= 2PIa^2+2PIa*2c and v = PIa^2*2c hence s/v =(1/c)+(2/a)
		for (int i = 1; i < 5; i++) {// just two iterations yield an acceptable
										// a-c ratio for plotting
			aa = 2 / (svratio - 1 / cc);// from s/v ratio
			cc = v * 3 / (4 * Math.PI * aa * aa);// from v ellipsoid
		}
		end.a = aa;
		end.c = cc;
	}
	private static double distance(Endosome endosome, MT obj) {

		// If the line passes through two points P1=(x1,y1) and P2=(x2,y2) then
		// the distance of (x0,y0) from the line is calculate from wiki
		NdPoint pt = space.getLocation(endosome);
		double xpt = pt.getX();
		double ypt = pt.getY();
		double ymax = (double) ((MT) obj).getYend();
		double ymin = (double) ((MT) obj).getYorigin();
		double xmax = (double) ((MT) obj).getXend();
		double xmin = (double) ((MT) obj).getXorigin();
		// double a = Math.abs((ymax-ymin) * xpt - (xmax-xmin)*ypt + xmax*ymin -
		// ymax*xmin);
		double a = (xmax - xmin) * (ymin - ypt) - (ymax - ymin)
				* (xmin - xpt);
		double b = Math.sqrt((ymax - ymin) * (ymax - ymin) + (xmax - xmin)
				* (xmax - xmin));
		double distance = a / b;
//		The distance has a sign that it is used to move the organelle to the position in the Mt
		return distance;
	}
}
