package immunity;

import java.util.HashMap;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Scale {
	
	private static ContinuousSpace<Object> space;
	private static Grid<Object> grid;
	private static int scale500nm = 0;
	private static String timeString = "";
	// a single Cell is created
	private static Scale instance;
	static {
		instance = new Scale(space, grid);
	}
	


	// Constructor
	public Scale (ContinuousSpace<Object> space, Grid<Object> grid) {
		scale500nm = (int)(50d*500d/1500d*Cell.orgScale);
		
	}
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {

		timeString = this.changeTime();

		}
	private String changeTime() {
		int tick = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
//		At time scale 1 and Organelle scale = 1, I move the endosome 1/50th of the space equivalent to
//		1500 nm (50 ticks to travel 1500 nm).  Since the velocity of an  organelle in a MT is 1000 nm/sec, one step are 30 nm  and hence
//		ONE TICKIS 0.03 SEC.
//		At time scale 0.5, I move the endosome 60 nm. Hence, one tick is 0.06 sec.
		int totalSecs = (int)(tick*0.03/Cell.timeScale);
		int hours = totalSecs / 3600;
		int minutes = (totalSecs % 3600) / 60;
		int seconds = totalSecs % 60;

		String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);

		return time;
	}
	public static int getScale500nm() {
		return scale500nm;
	}
	public static String getTimeString() {
		return timeString;
	}
	

}
