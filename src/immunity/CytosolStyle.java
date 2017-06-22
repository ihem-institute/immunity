package immunity;

import java.awt.Color;
import java.awt.Font;

import javax.media.opengl.GL2;

import repast.simphony.visualizationOGL2D.StyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.render.RenderState;
import saf.v3d.scene.Position;
import saf.v3d.scene.VShape;
import saf.v3d.scene.VSpatial;

public class CytosolStyle implements StyleOGL2D<Cytosol> {

	ShapeFactory2D factory;
	private double String;
	
	@Override
	public void init(ShapeFactory2D factory) {
		this.factory = factory;

	}

	@Override
	public VSpatial getVSpatial(Cytosol object, VSpatial spatial) {
		// 15 measure the size of the grid.  Each point is 50*15X50*15
		VSpatial createRectangle = this.factory.createRectangle(15, 15);
		return createRectangle;

	}

	@Override
	public Color getColor(Cytosol object) {
		// eventually the color will reflect some local cytosol characteristics
		int blue = (int)object.getBlue();
		return new Color(240, 240, blue);
	}

	@Override
	public int getBorderSize(Cytosol object) {
		//if larger than 0, form a nice grid
		return 0;
	}

	@Override
	public Color getBorderColor(Cytosol object) {
		return new Color(100);
	}

	@Override
	public float getRotation(Cytosol object) {
		return 0;
	}

	@Override
	public float getScale(Cytosol object) {	
		return (float) 1;
	}

	@Override
	public String getLabel(Cytosol object) {
		return ""; 
	}
	@Override
	public Font getLabelFont(Cytosol object) {
		return new Font("sansserif", Font.PLAIN, 14);
	}

	@Override
	public float getLabelXOffset(Cytosol object) {
		return 0;
	}

	@Override
	public float getLabelYOffset(Cytosol object) {
		return 0;
	}

	@Override
	public Position getLabelPosition(Cytosol object) {
		return Position.CENTER;
	}

	@Override
	public Color getLabelColor(Cytosol object) {
		return new Color(100);
	}

}
