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

public class EndosomeStyle implements StyleOGL2D<Endosome> {

	ShapeFactory2D factory;
	private double String;

	@Override
	public void init(ShapeFactory2D factory) {
		this.factory = factory;

	}
	
	@Override
	public VSpatial getVSpatial(Endosome object, VSpatial spatial) {
		double s = object.getArea();
		double v = object.getVolume();
		int svr =(int) ((s * s * s) / (v * v)/ (113d));
		VSpatial createRectangle = this.factory.createRectangle(5* svr, 5);
		return createRectangle;
	}

	@Override
	public Color getColor(Endosome object) {
		int f = Math.abs( (int)object.getArea() % 256 );
		return new Color(f, 0, 0);
	}

	@Override
	public int getBorderSize(Endosome object) {
		return 0;
	}

	@Override
	public Color getBorderColor(Endosome object) {
		return new Color(100);
	}

	@Override
	public float getRotation(Endosome object) {
		return (float) -object.getHeading();
	}

	@Override
	public float getScale(Endosome object) {
		
		return (float) object.size() / 10f;
	}

	@Override
	public String getLabel(Endosome object) {
		return "radius " + Math.abs((int)object.size());
	}
	@Override
	public Font getLabelFont(Endosome object) {
		return new Font("sansserif", Font.PLAIN, 14);
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
