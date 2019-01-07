package immunity;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.media.opengl.GL2;

import org.apache.commons.math3.geometry.euclidean.twod.Line;

import repast.simphony.space.continuous.ContinuousSpace;
import repast.simphony.space.grid.Grid;
import repast.simphony.visualization.visualization2D.layout.CircleLayout2D;
import repast.simphony.visualizationOGL2D.StyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.Position;
import saf.v3d.scene.VSpatial;


public class MoleculeStyle implements StyleOGL2D<MoleculePM> {

	ShapeFactory2D factory;
	private double String;

	@Override
	public void init(ShapeFactory2D factory) {
		this.factory = factory;

	}

	@Override
	public VSpatial getVSpatial(MoleculePM object, VSpatial spatial) {

        VSpatial shape = null;
        float a = 0;
        if (object.getType().equals("lipid") ) {
        	a = 15;
        }
        else {a = 30;}
        

//        CircleLayout2D circle = new CircleLayout2D();
        shape = this.factory.createCircle((float) a, 10);

		return shape;//createRectangle;
	}

	@Override
	public Color getColor(MoleculePM object) {
		// color code for contents
		if (object.type.equals("lipid"))	return new Color (0,0,255);
		else return new Color (0,255,0);
	}

		@Override
	public float getScale(MoleculePM object) {
		// the size is the radius of a sphere with the volume of the object
		// hence, the newly form endosome with a size 30, has a scale of 3
		return (float) Cell.orgScale;// (float) object.size() / 10f;
	}

		@Override
		public int getBorderSize(MoleculePM object) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Color getBorderColor(MoleculePM object) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public float getRotation(MoleculePM object) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public java.lang.String getLabel(MoleculePM object) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Font getLabelFont(MoleculePM object) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public float getLabelXOffset(MoleculePM object) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public float getLabelYOffset(MoleculePM object) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Position getLabelPosition(MoleculePM object) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Color getLabelColor(MoleculePM object) {
			// TODO Auto-generated method stub
			return null;
		}


}

