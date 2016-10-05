package immunity;

import java.util.ArrayList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.SpatialMath;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;
import repast.simphony.valueLayer.GridValueLayer;


public class Endosome {
	// globals
	public double rcyl = 10.0;
	public double mincyl = 6 * Math.PI * rcyl * rcyl;
	
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	double area = 30000; // initial value, but should change
	double volume = 200000; // initial value, but should change
	public double speed = 2; // initial value, but should change
	public double heading = 0; // initial value, but should change
	ArrayList<Element> areaElement = new ArrayList<Element>();
	ArrayList<Element> volumeElement = new ArrayList<Element>();
	public class Element {
		double proportion;
		String type;
		public Element(float pr, String t) {
			this.proportion = pr;
			this.type = t;
		}
	}
	// constructor 1 (without parameters)
	public	Endosome () {
		Element e = new Element(0.5f, "Rab1");
		this.areaElement.add(e );
	}
	
	// constructor 2 with a grid and space (does not work)
	public	Endosome (ContinuousSpace<Object> sp, Grid<Object> gr) {
		this.space = sp;
		this.grid = gr;
				}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		moveTowards();
		fusion();
		split();
		size();
	}
	public void size(){
		 double rsphere = Math.pow(this.volume * 3d / 4d / Math.PI, ( 1d / 3d ));
		 double size = rsphere  * 10d; // cellscale ;calculate size proportional to volume (radius of sphere with this volume)
	
	}
	public void fusion() {
		GridPoint pt = grid.getLocation(this);
		List<Endosome> endosomes_to_delete = new ArrayList<Endosome>();
		for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY())) {
			if (obj instanceof Endosome && obj != this) {
				endosomes_to_delete.add((Endosome) obj);
			}
		}
		  System.out.println("this Volume and Area");	 
		  System.out.println(this.volume);	 
		  System.out.println(this.area);
		for (Endosome endosome : endosomes_to_delete) {
			this.volume = this.volume + endosome.volume;
			this.area = this.area + endosome.area;
			Context<Object> context = ContextUtils.getContext(endosome);
			context.remove(endosome);
			  System.out.println("new Volume and Area");	 
			  System.out.println(this.volume);	 
			  System.out.println(this.area);
		}
	}
	
	public void moveTowards() {
		// only move if we are not already in this grid location

			NdPoint myPoint = space.getLocation(this);
			double x = myPoint.getX() + RandomHelper.nextDoubleFromTo(-5, 5);
			double y = myPoint.getY() + RandomHelper.nextDoubleFromTo(-5, 5);
		space.moveTo(this, x, y);
		grid.moveTo(this, (int)x, (int)y);
	}
	
	// Slit the endosome in two
	public void split(){
		double vo = this.volume;
		double so = this.area;
		if (vo < 2 * Math.PI * rcyl * rcyl * rcyl)return; //if too small do not split. Volume of a cylinder of 2 cylinder radius long (almost a sphere)
		if (so < 2 * mincyl) return; // if the surface is less than two minimus tubules, abort splitting
		if (so * so * so / (vo * vo) <= 36 * Math.PI) return; //if s^3 / v^2 is equal to 36*PI then it is an sphere and cannot form a tubule		
	 // procedure to split an organelle which is between a sphere (that cannot be splitted) and a cylinder, that follow other rules.
	  //calculate surface of smallest sphere
	  double vsphere = vo;
	  double deltaS = 2 * mincyl ;// deltaS is set large enough to pass the first round of calculation
	  double scylinder = 2 * Math.PI * rcyl * rcyl ;// the area of the cylinder with two "caps"
	  double vcylinder = 0;
	  double ssphere = 0;
	  int i = 0;
	  while (deltaS >= mincyl){
	  double tempscyl = scylinder;
	  double rsphere = Math.pow((vsphere * 3) / (4 * Math.PI) , (1/3d)) ;// calculate the radius of the sphere with a given volume
	  //System.out.println("vsphere");
	  //System.out.println(vsphere);
	  //System.out.println("rsphere");
	  //System.out.println(rsphere);
	  ssphere = (4 * Math.PI * rsphere * rsphere) ;//calculate the surface of the sphere with the radius calculated
	  scylinder = so - ssphere ;// calculate the remaining surface that can be used to form a tubule
	  double hcylinder = (scylinder - 2 * Math.PI * rcyl * rcyl)  / (2 * Math.PI * rcyl) ;//with the available surface, calculate the length of a two cap cylinder
	  vcylinder = Math.PI * rcyl * rcyl * hcylinder ;// calculate the volume of the cylinder
	  vsphere = (vo - vcylinder) ;// substract from the initial volume to estimate the remaining volume that is used to estimate a sphere radius
	  deltaS = tempscyl - scylinder ;// calculate the difference of surface between iterations
	  //System.out.println(vsphere);
	  //System.out.println(ssphere);
	  //System.out.println(vcylinder);
	  //System.out.println(scylinder);
	  }
	  /*; select a Rab domain to form the tubule.  Different criteria can be used.
	  //; The following choses a Rab at random and iterates until a Rab with enough surface to cover at least
	  //; a tubule quantum is found.  The smallest tubule (almost a vesicle) is two tubule-radius long and has a surface called "mincyl"
	  //; RANDOM
	  let mRabs matrix:submatrix mfactors 0 11 1 20 ;
	  let a matrix:to-row-list mRabs  ;print "list rabs" print a; submatrix to be converted to list.
	  if max item 0 a < mincyl [ set scylinder 0 stop] ; if the largest Rab domain is smaller than the tubule unit, stop
	  set RabN Rab-in-tubule  if size = max [size] of endosomes [write RabN]

	  let RabArea matrix:get mfactors 0 (11 + RabN) - 2 * pi * rcyl ^ 2 ; calculate the initial Rab area available (substract the two cylinder's caps)
	  set scylinder 2 * pi * rcyl ^ 2 ; initial surface: two caps

	  while [so - (ssphere + scylinder) >= (4 * pi * rcyl ^ 2) and RabArea - scylinder >= (4 * pi * rcyl ^ 2)]
	  [
	    set scylinder scylinder + 4 * pi * rcyl ^ 2

	  ]
	; from the cylinder surface, estimate the length and the volume
	  let hcylinder (scylinder - 2 * pi * rcyl ^ 2)  / (2 * pi * rcyl) ; two caps cylinder
	  set vcylinder pi * rcyl ^ 2 * hcylinder ; cylinder volume and area, and RabN are used to split the organelle.
	end */
	  if (scylinder < mincyl) return; //if the cylinder that can be formed is too small, abort splitting
	  //; change the old organelle by substracting the tubule.  Volume, area and Rab-content is changed
	  double vVesicle = vo - vcylinder;
	  double sVesicle = so - scylinder;
	  // if ((vVesicle < 2 * Math.PI * rcyl * rcyl * rcyl)||(sVesicle < mincyl)) return; 
	  this.area = sVesicle;
	  this.volume = vVesicle;

	  Endosome b = new Endosome();
	  b.area = scylinder;
	  b.volume = vcylinder;	  
	  System.out.println("this Volume and Area");	 
	  System.out.println(this.volume);	 
	  System.out.println(this.area);
	  System.out.println("new Volume and Area");	 
	  System.out.println(b.volume);	 
	  System.out.println(b.area);
	}


}


