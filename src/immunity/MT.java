package immunity;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class MT {
	// globals
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	double xorigin = 40d;
	double xend = 40d;
	double yorigin = 0d;
	double yend = 50d;
	double mth = Math.atan((yend - yorigin) / (xend - xorigin));
	public double mtheading = -mth * 180 / Math.PI;

	// constructor
	public MT(ContinuousSpace<Object> sp, Grid<Object> gr) {
		this.space = sp;
		this.grid = gr;
	}

	@ScheduledMethod(start = 1, interval = 100)
	public void step() {
		if (Math.random() <0.01)
			changePosition(this);
	}

	public void changePosition(MT mt) {
		//if (Math.random() < 0.1) return;
		// move the origin and the end of the MT
		xorigin = RandomHelper.nextDoubleFromTo(15, 35);
		if (xorigin <= 25) {xend = xorigin -RandomHelper.nextDoubleFromTo(0, xorigin);}
		else {xend = xorigin + RandomHelper.nextDoubleFromTo(0, 50-xorigin);}
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
		space.moveTo(mt, x, y);
		grid.moveTo(mt, (int) x, (int) y);
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
