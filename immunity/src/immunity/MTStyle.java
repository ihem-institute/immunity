package immunity;

import java.awt.Color;
import java.awt.Font;

import repast.simphony.visualizationOGL2D.StyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.Position;
import saf.v3d.scene.VSpatial;

public class MTStyle implements StyleOGL2D<MTs> {

	ShapeFactory2D factory;
	private double String;
	
	@Override
	public void init(ShapeFactory2D factory) {
		this.factory = factory;

	}

	@Override
	public VSpatial getVSpatial(MTs object, VSpatial spatial) {
		double h =  object.getMtheading();
		double hh = h * Math.PI /180;
		int length = (int)(- 750 / (Math.sin(hh)));
		int diameterMT = (int) (12*Cell.orgScale);
		VSpatial createRectangle = this.factory.createRectangle(diameterMT, length);
		return createRectangle;

	}

	@Override
	public Color getColor(MTs object) {
		//int f = Math.abs( (int)object.getArea() % 256 );
		return new Color(0, 0, 200, 80);
	}

	@Override
	public int getBorderSize(MTs object) {
		return 0;
	}

	@Override
	public Color getBorderColor(MTs object) {
		return new Color(100);
	}

	@Override
	public float getRotation(MTs object) {
		float h = (float) object.getMtheading()+ 90f;
		return h;
	}

	@Override
	public float getScale(MTs object) {
		
		return (float) 1;
	}

	@Override
	public String getLabel(MTs object) {
		return ""; 
	}
	@Override
	public Font getLabelFont(MTs object) {
		return new Font("sansserif", Font.PLAIN, 14);
	}

	@Override
	public float getLabelXOffset(MTs object) {
		return 0;
	}

	@Override
	public float getLabelYOffset(MTs object) {
		return 0;
	}

	@Override
	public Position getLabelPosition(MTs object) {
		return Position.CENTER;
	}

	@Override
	public Color getLabelColor(MTs object) {
		return new Color(100);
	}

}