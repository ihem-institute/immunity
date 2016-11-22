package immunity;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;

 public class MT {
	//globals
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	double xorigin = 0;
	double xend = 25;
	double yorigin = 0;
	double yend = 50;
	double mth = Math.atan((yend - yorigin)/(xend - xorigin));
	public double mtheading = - mth * 180 / Math.PI;
	
	// constructor
	public MT(ContinuousSpace<Object> sp, Grid<Object> gr) {
		this.space = sp;
		this.grid = gr;
		}
	
	@ScheduledMethod(start = 1, interval = 100)
	public void step() {
		changePosition();	
	}
	public void changePosition() {
		// move the origin and the end of the MT
		xorigin = RandomHelper.nextDoubleFromTo(10, 40);
		xend = RandomHelper.nextDoubleFromTo(10, 40);
		double mth = Math.abs(Math.atan((50)/(xend - xorigin)));
		mtheading = - mth * 180 / Math.PI;
		}

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

