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

public class ScaleStyle implements StyleOGL2D<Scale> {

	ShapeFactory2D factory;
	private double String;
	
	@Override
	public void init(ShapeFactory2D factory) {
		this.factory = factory;

	}

	@Override
	public VSpatial getVSpatial(Scale object, VSpatial spatial) {
		// 15 measure the size of the grid.  Each point is 50*15X50*15
		double scale = Cell.orgScale;
		int scale500nm = (int) (750d*500d/1500d*scale);
		VSpatial createRectangle = this.factory.createRectangle(scale500nm, 15);
		return createRectangle;

	}

	@Override
	public Color getColor(Scale object) {

		return new Color(0, 0, 255);
	}

	@Override
	public int getBorderSize(Scale object) {
		//if larger than 0, form a nice grid
		return 0;
	}

	@Override
	public Color getBorderColor(Scale object) {
		return new Color(100);
	}

	@Override
	public float getRotation(Scale object) {
		return 0;
	}

	@Override
	public float getScale(Scale object) {	
		return (float) 1;
	}

	@Override
	public String getLabel(Scale object) {
		String time = "500 nm   " + Scale.getTimeString();
		
		
		return time; 
	}
	@Override
	public Font getLabelFont(Scale object) {
		return new Font("sansserif", Font.BOLD, 14);
	}

	@Override
	public float getLabelXOffset(Scale object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getLabelYOffset(Scale object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Position getLabelPosition(Scale object) {
		// TODO Auto-generated method stub
		return Position.CENTER;
	}

	@Override
	public Color getLabelColor(Scale object) {
		// TODO Auto-generated method stub
		return new Color(255,255,255);
	}

}
