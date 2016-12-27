package immunity;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;

public class MT {
	// globals
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	double xorigin = 10;
	double xend = 40;
	double yorigin = 0;
	double yend = 50;
	double mth = Math.atan((yend - yorigin) / (xend - xorigin));
	public double mtheading = -mth * 180 / Math.PI;

	// constructor
	public MT(ContinuousSpace<Object> sp, Grid<Object> gr) {
		this.space = sp;
		this.grid = gr;
	}

	@ScheduledMethod(start = 1, interval = 100)
	public void step() {
		if (Math.random() < 0.1)
			changePosition();
	}

	public void changePosition() {
		// move the origin and the end of the MT
		xorigin = RandomHelper.nextDoubleFromTo(1, 49);
		xend = (RandomHelper.nextDoubleFromTo(xorigin - 10, xorigin + 10));
		double mth = Math.atan((50) / (xend - xorigin));
		System.out.println("a-tang");
		System.out.println(mth * 180 / Math.PI);
		if (mth < 0) {
			mth = 180 + (mth * 180 / Math.PI);
		} else
			mth = mth * 180 / Math.PI;
		mtheading = -mth;
		double y = 24.5;
		double x = xorigin + 25 * Math.cos(mtheading * Math.PI / 180);
		space.moveTo(this, x, y);
		grid.moveTo(this, (int) x, (int) y);
	}
	// GETTERS AND SETTERS
	public double getXorigin() {
		return xorigin;
	}

	public double getXend() {
		return xend;
	}

	public double getYorigin() {
		return yorigin;
	}

	public double getYend() {
		return yend;
	}

	public double getMtheading() {
		return mtheading;
	}

}
