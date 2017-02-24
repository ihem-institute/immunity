package immunity;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
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
		int svr = (int) ((s * s * s) / (v * v) / (113d)); // should be 1 for a												// sphere
		VSpatial createRectangle = this.factory.createRectangle(5 * svr, 5);
		return createRectangle;
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
		return new Color((int) ((red + corr) * 255),
				(int) ((green + corr) * 255), (int) ((blue + corr) * 255));
	}

	@Override
	public float getRotation(Endosome object) {
		// set in a way that object move along its large axis
		return (float) -object.getHeading();
	}

	@Override
	public float getScale(Endosome object) {
		// the size is the radius of a sphere with the volume of the object
		// hence, the newly form endosome with a size 30, has a scale of 3
		return (float) object.size() / 10f;
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















