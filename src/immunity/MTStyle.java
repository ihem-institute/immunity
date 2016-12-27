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

public class MTStyle implements StyleOGL2D<MT> {

	ShapeFactory2D factory;
	private double String;
	
	@Override
	public void init(ShapeFactory2D factory) {
		this.factory = factory;

	}

	@Override
	public VSpatial getVSpatial(MT object, VSpatial spatial) {
		double h =  object.getMtheading();
		double hh = h * Math.PI /180;
		int length = (int)(- 750 / (Math.sin(hh)));
		VSpatial createRectangle = this.factory.createRectangle(3, length);
		return createRectangle;

	}

	@Override
	public Color getColor(MT object) {
		//int f = Math.abs( (int)object.getArea() % 256 );
		return new Color(0, 0, 200, 80);
	}

	@Override
	public int getBorderSize(MT object) {
		return 0;
	}

	@Override
	public Color getBorderColor(MT object) {
		return new Color(100);
	}

	@Override
	public float getRotation(MT object) {
		float h = (float) object.getMtheading()+ 90f;
		return h;
	}

	@Override
	public float getScale(MT object) {
		
		return (float) 1;
	}

	@Override
	public String getLabel(MT object) {
		return ""; 
	}
	@Override
	public Font getLabelFont(MT object) {
		return new Font("sansserif", Font.PLAIN, 14);
	}

	@Override
	public float getLabelXOffset(MT object) {
		return 0;
	}

	@Override
	public float getLabelYOffset(MT object) {
		return 0;
	}

	@Override
	public Position getLabelPosition(MT object) {
		return Position.CENTER;
	}

	@Override
	public Color getLabelColor(MT object) {
		return new Color(100);
	}

}