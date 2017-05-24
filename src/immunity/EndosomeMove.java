package immunity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;

public class EndosomeMove {

	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	private static List<MT> mts;
	
	public static void moveTowards(Endosome endosome) {
		space = endosome.getSpace();
		grid = endosome.getGrid();
		/*
		 * Direction in Repast 0 to the right 180 to the left -90 down +90 up
		 */
		NdPoint myPoint = space.getLocation(endosome);
		double x = myPoint.getX() + Math.cos(endosome.heading * 2d * Math.PI / 360d)
				* endosome.speed;
		double y = myPoint.getY() + Math.sin(endosome.heading * 2d * Math.PI / 360d)
				* endosome.speed;
		// if reaches the bottom, changes the direction to horizontal. If the
		// original heading
		// was between 0 and -90 goes to the right, else to the left
		double cellLimit = 3 * Cell.orgScale;
		if (y > 50-cellLimit || y < cellLimit) {
			changeDirection(endosome);
			if (y > 50-cellLimit) y = 50 -cellLimit;
			if (y < cellLimit) y = cellLimit;
		}
//		if (myPoint.getY() - y > this.speed)
//			System.out.println(" SALTO     " + myPoint.getY() + " " + y + " "
//					+ (myPoint.getY() - y) + " "
//					+ (Math.sin(heading * 2d * Math.PI / 360d)) + "  "
//					+ this.speed);
		space.moveTo(endosome, x, y);
		grid.moveTo(endosome, (int) x, (int) y);
	}
	
	public static double changeDirection(Endosome endosome) {
		double initial = endosome.heading;
		space = endosome.getSpace();
		grid = endosome.getGrid();
		// When near the bottom or the top, the movement is random and depends on the
		// momentum
		NdPoint myPoint = space.getLocation(endosome);
		if (myPoint.getY() < 5*Cell.orgScale || 
				myPoint.getY() >50 - 2*Cell.orgScale
				|| Math.random()<0.01) {
			endosomeShape(endosome);
			double momentum = endosome.volume * (endosome.a * endosome.a + endosome.c * endosome.c) / 5 / 3E7;
			Random fRandom = new Random();
			endosome.heading = (endosome.heading + fRandom.nextGaussian() * 10d
					/ momentum)% 360;
			endosome.speed = Cell.orgScale/ endosome.size;
			if (initial - endosome.heading > 90)
				System.out.println("GIRO BOTTOM " + "  " + initial + "  "
						+ endosome.heading + "  " + momentum);
			return endosome.heading;
		}
		// when (not in the bottom or the top or a rnd probability)  and near a MT takes the direction of the MT
		// and
		// increases the speed to 1* Cell.orgScale
		if (mts == null) {
			mts = associateMt();
		}
		int mtDir = 0;
		/*
		 * mtDirection decides if the endosome is going to move to the (-) end
		 * of the MT (dyneine like or to the plus end (kinesine like). -1 goes
		 * to the nucleus, 1 to the PM
		 */
		mtDir = mtDirection(endosome);
		for (MT mt : mts) {
			double dist = distance(endosome, mt);
			if (dist < (endosome.size* Cell.orgScale) / 30d) {
//				endosomeShape(this);
//				volume cylinder = PI*rcyl^2*h; area = PI*2*rcyl*h + 2* PI*rcyl^2
//				hence for a cylinder of diameter = rcyl, volume / (area-2* PI*rcyl^2) = rcyl/2 
//				direction is fixed to to the surface for tubules and to the center for the rest
				if (endosome.volume/(endosome.area - 2*Math.PI*Cell.rcyl*Cell.rcyl) <=Cell.rcyl/2)
					mtDir = -1;
				else
					mtDir = 1;
				endosome.heading = -mtDir * mt.getMtheading() + 180f;
				endosome.speed = 1d*Cell.orgScale;
				// if (initial - heading > 90)System.out.println("GIRO MT "
				// +initial+"  "+heading);
				return endosome.heading;
			}
		}
		// when no MT is near the endosome, it rotate randomly
		// according with its momentum
		endosomeShape(endosome);
		double momentum = endosome.volume * (endosome.a * endosome.a + endosome.c * endosome.c) / 5 / 3E7;
		// if (momentum < 0.5 && c>21) System.out.println("momentum  " +
		// momentum+" "+a+"  "+c);

		Random fRandom = new Random();
		endosome.heading = (endosome.heading + fRandom.nextGaussian() * 10d / momentum) % 360;
		// if (initial - heading >
		// 90)System.out.println("GIRO sin MT "+initial+"  "+heading+"  "+momentum);
		return endosome.heading;

	}
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
		// the distance of (x0,y0) from the line is:
		NdPoint pt = space.getLocation(endosome);
		double xpt = pt.getX();
		double ypt = pt.getY();
		double ymax = (double) ((MT) obj).getYend();
		double ymin = (double) ((MT) obj).getYorigin();
		double xmax = (double) ((MT) obj).getXend();
		double xmin = (double) ((MT) obj).getXorigin();
		// double a = Math.abs((ymax-ymin) * xpt - (xmax-xmin)*ypt + xmax*ymin -
		// ymax*xmin);
		double a = Math.abs((xmax - xmin) * (ymin - ypt) - (ymax - ymin)
				* (xmin - xpt));
		double b = Math.sqrt((ymax - ymin) * (ymax - ymin) + (xmax - xmin)
				* (xmax - xmin));
		double distance = a / b;
		// System.out.println("distance");
		// System.out.println(distance);
		return distance;
	}
}
