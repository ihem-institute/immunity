package immunity;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.context.space.continuous.ContinuousSpaceFactory;
import repast.simphony.context.space.continuous.ContinuousSpaceFactoryFinder;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
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
	// space
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	// Endosomal 
	double area = 4d * Math.PI * 30d * 30d; // initial value, but should change
	double volume = 4d / 3d * Math.PI * 30d * 30d * 30d ; // initial value, but should change
	double size = Math.pow(volume * 3d / 4d / Math.PI, ( 1d / 3d ));
	double speed = 5d/size; // initial value, but should change
	double heading = Math.random() * 360d; // initial value, but should change
	ArrayList<Element> areaElement = new ArrayList<Element>();
	ArrayList<Element> volumeElement = new ArrayList<Element>();
	private List<MT> mts;
	HashMap<String, Float> rabCompatibility = new HashMap<String, Float>();
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
	public	Endosome (ContinuousSpace<Object> sp, Grid<Object> gr, ArrayList<Element> rabs) {
		this.space = sp;
		this.grid = gr;
		this.areaElement = rabs;
		rabCompatibility.put("AA", 1.0f);
		rabCompatibility.put("AB", 0.1f);
		rabCompatibility.put("BB", 1.0f);
		// TODO: agregar todas las combinaciones
	}
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		if (Math.random()< 0.1) changeDirection();
		moveTowards();
		fusion();
		split();
		size();
	}
	public List<MT> associateMt(){
		List<MT> mts = new ArrayList<MT>();
		for (Object obj : grid.getObjects()) {
			if (obj instanceof MT) {
				mts.add((MT) obj);
				}
			}
		  System.out.println(mts);
		return mts;
		
		}

	public double changeDirection(){
		if (Math.random()< 0.9) {
			this.heading = this.heading + (0.5d - Math.random()) * 90d *30 / size();
		return this.heading;
		}
		if(mts == null) {
			mts = associateMt();
		}
		for (MT mt : mts){
			if(distance(this, mt)< this.size){
			this.heading = mt.getMtheading();
			System.out.println("headingMT");  
			System.out.println(this.heading);
			return this.heading;
		}
		}
		return this.heading;
	}
	
	private double distance(Endosome endosome, MT obj) {
		
		// If the line passes through two points P1=(x1,y1) and P2=(x2,y2) then the distance of (x0,y0) from the line is:
		GridPoint pt = grid.getLocation(this);
		double xpt = pt.getX();
		double ypt = pt.getY();
		double ymax = (double)((MT) obj).getYend();
		double ymin = (double)((MT) obj).getYorigin();
		double xmax = (double)((MT) obj).getXend();
		double xmin = (double)((MT) obj).getXorigin();
		double a = Math.abs((ymax-ymin) * xpt - (xmax-xmin)*ypt + xmax*ymin - ymax*xmin);
		double b = Math.sqrt((ymax-ymin)*(ymax-ymin) + (xmax-xmin)*(xmax-xmin));
		double distance = a/b;
		return distance;
	}
	public double size(){
		 double rsphere = Math.pow(this.volume * 3d / 4d / Math.PI, ( 1d / 3d ));
		 double size = rsphere; // cellscale ;calculate size proportional to volume (radius of sphere with this volume)
	return size;
	}
	// TRAIDO DE RABS
	/*private float getCompatibility(String rabX, String rabY) {
		
		try {
			if(rabCompatibility.containsKey(rabX+rabY)) {
				return rabCompatibility.get(rabX+rabY);
			} else {
				return rabCompatibility.get(rabY+rabX);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return 0;
	}
	
	private boolean compatibles(Endosome endosome, Endosome obj) {
		float sum = 0;
		for (Element element : endosome.areaElement) {
			for (Element element2 : obj.areaElement) {
				float comp = getCompatibility(element.type, element2.type) * element.proportion * element2.proportion;
				sum += comp;
			}
		}
		return Math.random() < sum;
	}
	// HASTA ACÁ
	*/
	public void fusion() {
		GridPoint pt = grid.getLocation(this);
		List<Endosome> endosomes_to_delete = new ArrayList<Endosome>();
		for (Object obj : grid.getObjectsAt(pt.getX(), pt.getY())) {
			if (obj instanceof Endosome && obj != this) {
				endosomes_to_delete.add((Endosome) obj);
			}
		}
		 /* System.out.println("this Volume and Area");	 
		  System.out.println(this.volume);	 
		  System.out.println(this.area);*/
		for (Endosome endosome : endosomes_to_delete) {
			this.volume = this.volume + endosome.volume;
			this.area = this.area + endosome.area;
			  /*System.out.println("new Volume and Area");	 
			  System.out.println(this.volume);	 
			  System.out.println(this.area);*/
			Context<Object> context = ContextUtils.getContext(endosome);
			context.remove(endosome);
		}
		  size();
		  this.speed = 5/size();
	}
	
	public void moveTowards() {
		// only move if we are not already in this grid location

			NdPoint myPoint = space.getLocation(this);
			double x = myPoint.getX() + Math.cos(heading * 2d * Math.PI / 360d) * this.speed;
			double y = myPoint.getY() + Math.sin(heading * 2d * Math.PI / 360d) * this.speed;
			if (y >50 || y < 0){
				heading = - heading;
				return;
			}
		space.moveTo(this, x, y);
		grid.moveTo(this, (int)x, (int)y);
	}
	
	// Slit the endosome in two
	public void split(){
		double vo = this.volume;
		double so = this.area;
		if (vo < 2 * Math.PI * Cell.rcyl * Cell.rcyl * Cell.rcyl)return; //if too small do not split. Volume of a cylinder of 2 cylinder radius long (almost a sphere)
		if (so < 2 * Cell.mincyl) return; // if the surface is less than two minimus tubules, abort splitting
		if (so * so * so / (vo * vo) <= 36 * Math.PI) return; //if s^3 / v^2 is equal to 36*PI then it is an sphere and cannot form a tubule		
		if (vo / (so - 2 * Math.PI * Cell.rcyl * Cell.rcyl) == Cell.rcyl / 2){
			System.out.println("tubule");

			return;	
		}
		
		// procedure to split an organelle which is between a sphere (that cannot be splitted) and a cylinder, that follow other rules.
	  //calculate surface of smallest sphere
	  double vsphere = vo;
	  double deltaS = 2 * Cell.mincyl ;// deltaS is set large enough to pass the first round of calculation
	  double scylinder = 2 * Math.PI * Cell.rcyl * Cell.rcyl ;// the area of the cylinder with two "caps"
	  double vcylinder = 0;
	  double ssphere = 0;
	  int i = 0;
	  while (deltaS >= Cell.mincyl){
	  double tempscyl = scylinder;
	  double rsphere = Math.pow((vsphere * 3) / (4 * Math.PI) , (1/3d)) ;// calculate the radius of the sphere with a given volume
	  //System.out.println("vsphere");
	  //System.out.println(vsphere);
	  //System.out.println("rsphere");
	  //System.out.println(rsphere);
	  ssphere = (4 * Math.PI * rsphere * rsphere) ;//calculate the surface of the sphere with the radius calculated
	  scylinder = so - ssphere ;// calculate the remaining surface that can be used to form a tubule
	  double hcylinder = (scylinder - 2 * Math.PI * Cell.rcyl * Cell.rcyl)  / (2 * Math.PI * Cell.rcyl) ;//with the available surface, calculate the length of a two cap cylinder
	  vcylinder = Math.PI * Cell.rcyl * Cell.rcyl * hcylinder ;// calculate the volume of the cylinder
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
	  if (scylinder < Cell.mincyl) return; //if the cylinder that can be formed is too small, abort splitting
	  //; change the old organelle by substracting the tubule.  Volume, area and Rab-content is changed
	  double vVesicle = vo - vcylinder;
	  double sVesicle = so - scylinder;
	  if ((vVesicle < 2 * Math.PI * Cell.rcyl * Cell.rcyl * Cell.rcyl)||(sVesicle < Cell.mincyl)) return; 
	  this.area = sVesicle;
	  this.volume = vVesicle;
	  if (vVesicle < 0) {
		  System.out.println("VESICLE VOLUME");
		  System.out.println(vVesicle);
		  
	  }
	  size();
	  this.speed = 5/size();
//ContinuousSpace<Object> sp, Grid<Object> gr, ArrayList<Element> rabs
	  Endosome b = new Endosome(this.space, this.grid, null);
	  Context<Object> context = ContextUtils.getContext(this);
	  context.add(b) ;

	  b.area = scylinder;
	  b.volume = vcylinder;
	  if (vcylinder < 0) {
		  System.out.println("TUBULE VOLUME");
		  System.out.println(vVesicle);
		  
	  }
	  double rsphere = Math.pow(b.volume * 3d / 4d / Math.PI, ( 1d / 3d ));
	  b.size = rsphere; 
	  b.speed = 5/b.size;
	  b.heading = -this.heading;
	  NdPoint myPoint = space.getLocation(this);
		double x = myPoint.getX();
		double y = myPoint.getY();
	space.moveTo(b, x, y);
	grid.moveTo(b, (int)x, (int)y);
	 // moveTowards();
	  /*System.out.println("this Area3/V2");	 
	  System.out.println(this.area * this.area * this.area / this.volume / this.volume /113);
	  System.out.println("new Area3/V2");	 
	  System.out.println(b.area * b.area * b.area / b.volume / b.volume /113);	 
	  System.out.println(this.volume + " " + b.volume);
	  System.out.println(this.area + " " + b.area);*/
	}

	public double getArea() {
		return area;
	}

	public double getVolume() {
		return volume;
	}

	public double getSpeed() {
		return speed;
	}
	public double getHeading() {
		return heading;
	}
}


