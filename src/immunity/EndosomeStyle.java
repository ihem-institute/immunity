package immunity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.media.opengl.GL2;

import org.apache.commons.math3.geometry.euclidean.twod.Line;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.visualizationOGL2D.StyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.render.RenderState;
import saf.v3d.scene.Position;
import saf.v3d.scene.VShape;
import saf.v3d.scene.VSpatial;

//import smodel.Bacteria.State;

public class EndosomeStyle implements StyleOGL2D<Endosome> {

	ShapeFactory2D factory;
	private double String;

	@Override
	public void init(ShapeFactory2D factory) {
		this.factory = factory;

	}

	@Override
	public VSpatial getVSpatial(Endosome object, VSpatial spatial) {
		/*
		 * shape is generated as a relationship between area and volume. For a
		 * sphere the s^3/v^2 is 113. For a cylinder is larger than this. For
		 * the minimum cylinder is 169.6
		 */
		double s = object.getArea();
		double v = object.getVolume();
		double rsphere = Math.pow((v * 3) / (4 * Math.PI), (1 / 3d));
		double svratio = s/v; // ratio surface volume
		double a = rsphere; //initial a from the radius of a sphere of volume v
		double c = a;// initially, c=a
		// calculation from s/v for a cilinder that it is the same than for an ellypsoid
		// s= 2PIa^2+2PIa*2c and v = PIa^2*2c  hence s/v =(1/c)+(2/a)
		for (int i=1; i<3; i++){// just two iterations yield an acceptable a-c ratio for ploting
		a=2/(svratio-1/c);//from s/v ratio
		c= v*3/(4*Math.PI*a*a);//from v ellypsoid
		}
//		System.out.println("area  "+ s+" volume   " + v);
//		System.out.println("a "+a+ " c "+c+" areaE  "+ (2*Math.PI*a*a+2*a*Math.PI*2*c)*.666+" volumeE   " + 4d/3d*Math.PI*a*a*c);
//		PLOT as ellipses with a length/wide ratio depending on the area/volume
//		ratio of the endosome.  It is 1 (sphere) when the area is what you need to
//		cover a sphere with the volume of the endosome
//		double svr = ((s * s * s) / (v * v) / (113.0973355d)); 
//		 svr should be 1 for a sphere
//		SCALE: I MEASURE THAT THE 50 GRID CORRESPOND TO A RECTANGLE OF 750 OF LENGTH
//		IF THE ORGANELLES ARE IN nm, HENCE THE AREA REPRESENT A 750 nm X 750 nm
        VSpatial shape = null;
//		double a1 = 2*rsphere/(1+svr);
		//if (a>10){
        Shape ellypse = new Ellipse2D.Double(-c/2, -a/2, c, a);
        shape = this.factory.createShape(ellypse);
//		}
//		else{
//		shape = this.factory.createRectangle((int) (v/Math.PI/100),5);	
//		}
//		System.out.println("a  "+a1+"svr  "+svr);
/*//       PLOT as a sphere plus a tubule
		Shape sphere = new Ellipse2D.Double(0, 0, rsphere, rsphere);
		double areaTubule = s - 4*Math.PI*rsphere*rsphere;
		//		length of a one-cap cylinder with the extra membrane.
//		h = (area-PI*r2)/2*PI*r.  Where r is the radius of a tubule (10 nm)
		
		double tubLength = (areaTubule-Math.PI*100d)/(2*Math.PI*10);
        Shape tubule = new Rectangle.Double(rsphere-2,rsphere/2-5,tubLength,10);
		System.out.println("SPHERE"+rsphere+"TUBULE"+tubLength);
	    Area area = new Area(sphere);
	    Area a2 = new Area(tubule);
	    area.add(a2);
	    VSpatial spPlusTub = this.factory.createShape(area);*/

//		VSpatial createRectangle = this.factory.createRectangle(5 * svr, 5);

//       Stroke stroke = new BasicStroke((float) 0.2);


		return shape;//createRectangle;
	}

	@Override
	public Color getColor(Endosome object) {
		// color code for contents
		double red = object.getRed();
		double green = object.getGreen();
		double blue = object.getBlue();
		ArrayList<Double> colors = new ArrayList<Double>();
		colors.add(red);
		colors.add(green);
		colors.add(blue);
		// (1 - max (list g r b)) ;
		Double corr = 1 - Collections.max(colors);
		if (Collections.max(colors)>1.1) System.out.println("COLOR FUERA ESCALA "+red+"  "+green+"  "+blue);
//		if the content in the organelle is not represented, then light gray
		if (corr == 1) corr = 0.95;
		return new Color((int) ((red + corr) * 255),
				(int) ((green + corr) * 255), (int) ((blue + corr) * 255));

	}

	@Override
	public int getBorderSize(Endosome object) {
		return 4;
	}

	@Override
	public Color getBorderColor(Endosome object) {
		// color code for rab contents
		double red = object.getEdgeRed();
		double green = object.getEdgeGreen();
		double blue = object.getEdgeBlue();
		ArrayList<Double> colors = new ArrayList<Double>();
		colors.add(red);
		colors.add(green);
		colors.add(blue);

		// (1 - max (list g r b)) ;
		Double corr = 1 - Collections.max(colors);
//		if the rab in the organelle is not represented, then dark gray
		if (corr == 1)corr = 0.1;
		return new Color((int) ((red + corr) * 255),
				(int) ((green + corr) * 255), (int) ((blue + corr) * 255));
	}

	@Override
	public float getRotation(Endosome endosome) {
		// set in a way that object move along its large axis
		return (float) -endosome.heading;
	}

	@Override
	public float getScale(Endosome object) {
		// the size is the radius of a sphere with the volume of the object
		// hence, the newly form endosome with a size 30, has a scale of 3
		return (float) Cell.orgScale;// (float) object.size() / 10f;
	}

	@Override
	public String getLabel(Endosome object) {
		// the label is the number of internal vesicles (Multi Vesicular Body)
		// in the endosome
		if (object.getSolubleContent().containsKey("solubleMarker")
				&& object.getSolubleContent().get("solubleMarker")> 0){
		//String marker = object.getSolubleContent().get("solubleMarker").toString();
		return "M "+object.getMvb();
		}
		return object.getMvb();
	}

	@Override
	public Font getLabelFont(Endosome object) {
		return new Font("sansserif", Font.BOLD, 14);
	}

	@Override
	public float getLabelXOffset(Endosome object) {
		return 0;
	}

	@Override
	public float getLabelYOffset(Endosome object) {
		return 0;
	}

	@Override
	public Position getLabelPosition(Endosome object) {
		return Position.CENTER;
	}

	@Override
	public Color getLabelColor(Endosome object) {
		return new Color(100);
	}

}















