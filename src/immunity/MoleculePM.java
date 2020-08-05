package immunity;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.GridCell;
import repast.simphony.query.space.grid.GridCellNgh;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;

public class MoleculePM {
	
	// globals
	private ContinuousSpace<Object> spacePM;
	private Grid<Object> gridPM;

	public double heading = 180 / Math.PI;
	public int cluster = 0;
	public String type = null;
	public double speed = 1d;

	// constructor
	public MoleculePM (ContinuousSpace<Object> sp, Grid<Object> gr, String type) {
		this.spacePM = sp;
		this.gridPM = gr;
		this.type = type;
	}

	@ScheduledMethod(start = 1, interval = 10)
	public void step() {
		if (Math.random() <1) changePosition(this);
		if (Math.random() <1 && this.type.equals("lipid")) tether(this);
			
	}

	private void tether(MoleculePM moleculePM) {
//		Space space = moleculePM.spacePM;
//		grid = moleculePM.gridPM;
		GridPoint pt = moleculePM.gridPM.getLocation(moleculePM);
//		System.out.println("point "+pt);
		GridCellNgh<MoleculePM> nghCreator = new GridCellNgh<MoleculePM>(this.gridPM, pt,
			MoleculePM.class, 3, 3);
		// System.out.println("SIZE           "+gridSize);

		List<GridCell<MoleculePM>> moleculeList = nghCreator.getNeighborhood(true);
		if (moleculeList.size()<2) {
			moleculePM.cluster = 0;
			return;//if only one return
		}
		List<MoleculePM> moleculeToTether = new ArrayList<MoleculePM>();
		for (GridCell<MoleculePM> gr : moleculeList) {
			// include all endosomes
			for (MoleculePM mol : gr.items()) {
				if (moleculeAssessTethering(mol)) {
					moleculeToTether.add(mol);
					this.cluster = this.cluster + 1;
				}
			}
		}
		
		// new list with just the compatible endosomes (same or compatible rabs)
		if (moleculeToTether.size()<2)return; //if only one, return
		// select the largest endosome
		MoleculePM largest = moleculePM;
		for (MoleculePM mol : moleculeToTether) {
//			System.out.println(endosome.size+" "+end.size);
			if (mol.cluster > largest.cluster) {
				largest = mol;
			}
		}
		// assign the speed and heading of the largest endosome to the gropu

		for (MoleculePM mol : moleculeToTether) {

			Random r = new Random();
			double rr = r.nextGaussian();
			mol.heading = rr * 1d + largest.heading;
	//		EndosomeMove.moveTowards(end);
		}
	}
		
		
		// TODO Auto-generated method stub
		
		
	private boolean moleculeAssessTethering(MoleculePM mol) {
		// TODO Auto-generated method stub
		
		if (mol.cluster > this.cluster) {
			return false;	
		} 

		return true;
	}


	

	public void changePosition(MoleculePM molecule) {
		//if (Math.random() < 0.1) return;
		// move the origin and the end of the MT
//		spacePM.moveTo(molecule, x, y);
//		gridPM.moveTo(molecule, (int) x, (int) y);
		Random r = new Random();
		double rr = r.nextGaussian();
		molecule.heading = rr * 30d + molecule.heading;
		NdPoint myPoint = spacePM.getLocation(molecule);
		double x = myPoint.getX();
		double y = myPoint.getY();
	    double xx = x + Math.cos(molecule.heading * Math.PI / 180d)*
		molecule.speed*Cell.orgScale/Cell.timeScale;
	    double yy = y + Math.sin(molecule.heading * Math.PI / 180d)
		* molecule.speed * Cell.orgScale/Cell.timeScale;	
		gridPM.moveTo(molecule, (int) xx, (int) yy);
		spacePM.moveTo(molecule, xx, yy);
		
	
		
	}
	// GETTERS AND SETTERS



	public double getHeading() {
		return heading;
	}

	public int getCluster() {
		return cluster;
	}

	public String getType() {
		return type;
	}

	public double getSpeed() {
		return speed;
	}


	

}
