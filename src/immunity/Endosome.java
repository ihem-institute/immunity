package immunity;

import java.util.ArrayList;

import repast.simphony.query.space.continuous.ContinuousWithin;
import repast.simphony.random.RandomHelper;
import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.continuous.NdPoint;
import repast.simphony.space.grid.Grid;
import repast.simphony.valueLayer.GridValueLayer;


public class Endosome {
	// globals
	public double rcyl = 10.0;
	public double mincyl = 6 * Math.PI * rcyl * rcyl;
	
	private ContinuousSpace<Object> space;
	private Grid<Object> grid;
	float area = 500; // initial value, but should change
	float volume = 1000; // initial value, but should change
	public float speed = 2; // initial value, but should change
	public float heading = 0; // initial value, but should change
	ArrayList<Element> areaElement = new ArrayList<Element>();
	ArrayList<Element> volumeElement = new ArrayList<Element>();
	public class Element {
		float proportion;
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
		   space = sp;
			grid = gr;
			//randomMove (this.speed);
		}
	// moving endosomes.  Should have a random and a MT
	private void randomMove(float speed) {
		// random angle and radius
		double angle = RandomHelper.nextDoubleFromTo(0, 2 * 3.14); 
		//double radius = RandomHelper.nextDoubleFromTo(0, speed);
	
		NdPoint curr = space.getLocation(this);
		System.out.println(curr.getX());
		System.out.println(curr.getY());
		double x = curr.getX() + speed * Math.cos(angle);
		double y = curr.getY() + speed * Math.sin(angle);
		System.out.println(x);
		System.out.println(y);
		moveTo(x, y);
		
		// return speed;
	}
	private void moveTo(double x, double y) {
		space.moveTo(this, x, y);
		grid.moveTo(this, (int)x, (int)y);
	}
	
	// Slit the endosome in two
	public void splitEndosomes(){
		float vo = this.volume;
		float so = this.area;
		if (vo < 2 * Math.PI * rcyl * rcyl * rcyl)return; //if too small do not split. Volume of a cylinder of 2 cylinder radius long (almost a sphere)
		if (so < 2 * mincyl) return; // if the surface is less than two minimus tubules, abort splitting
		if (so * so * so / (vo * vo) <= 36 * Math.PI) return; //if s^3 / v^2 is equal to 36*PI then it is an sphere and cannot form a tubule		
	
	 // procedure to split an organelle which is between a sphere (that cannot be splitted) and a cylinder, that follow other rules.
	  //calculate surface of smallest sphere
	  float vsphere = vo;
	  double deltaS = 2 * mincyl ;// deltaS is set large enough to pass the first round of calculation
	  double scylinder = 2 * Math.PI * rcyl * rcyl ;// the area of the cylinder with two "caps"
	  double vcylinder = 0;
	  float ssphere = 0;
	  int i = 0;
	  while (deltaS >= mincyl){
	  double tempscyl = scylinder;
	  double rsphere = Math.pow((vsphere * 3 / 4 / Math.PI) , (1 / 3)) ;// calculate the radius of the sphere with a given volume
	  ssphere = (float) (4 * Math.PI * rsphere * rsphere) ;//calculate se surface of the sphere with the radius calculated
	  scylinder = so - ssphere ;// calculate the remaining surface that can be used to form a tubule
	  double hcylinder = (scylinder - 2 * Math.PI * rcyl * rcyl)  / (2 * Math.PI * rcyl) ;//with the available surface, calculate the length of a two cap cylinder
	  vcylinder = Math.PI * rcyl * rcyl * hcylinder ;// calculate the volume of the cylincer
	  vsphere = (float) (vo - vcylinder) ;// substract from the initial volume to estimate the remaining volume that is used to estimate a sphere radius
	  deltaS = tempscyl - scylinder ;// calculate the difference of surface between iterations
	  i = i + 1;
	  if (i > 1) System.out.println(i);
	  if (scylinder < mincyl) return;
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
	  if ((vVesicle < 2 * Math.PI * rcyl * rcyl * rcyl)||(sVesicle < mincyl)) return; 
	  this.area = (float) sVesicle;
	  this.volume = (float) vVesicle;
	  
	  Endosome b = new Endosome();
	  b.area = (float) scylinder;
	  b.volume = (float) vcylinder;
		  
	}
		public void fuseEndosomes(){
			//for (Endosome neighbor : (new ContinuousWithin((space), this, 10).query().iterator().hasNext())) {
//}

		}
}


