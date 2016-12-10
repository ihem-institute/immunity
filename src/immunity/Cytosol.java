package immunity;

import java.util.HashMap;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;

public class Cytosol {
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	double area = 4d * Math.PI * 30d * 30d; // initial value, but should change
	int xcoor;
	int ycoor;
	HashMap<String, Double> cytoContent = new HashMap<String, Double>();
	//constructor
	public	Cytosol (ContinuousSpace<Object> sp, Grid<Object> gr, HashMap<String, Double> 
	cytoContent, int xcoor, int ycoor) {
	this.space = sp;
	this.grid = gr;
	xcoor = xcoor;
	ycoor = ycoor;
	this.cytoContent = cytoContent;
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {

	}

	public void position(){
	for (Object obj : grid.getObjects()) {
		if (obj instanceof Cytosol) {
		space.moveTo(obj, xcoor, ycoor);
		grid.moveTo(obj, xcoor, ycoor);	
		}

	}
	}
}
