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

public class PlasmaMembraneStyle implements StyleOGL2D<PlasmaMembrane> {

	ShapeFactory2D factory;
	private double String;
	
	@Override
	public void init(ShapeFactory2D factory) {
		this.factory = factory;

	}

	@Override
	public VSpatial getVSpatial(PlasmaMembrane object, VSpatial spatial) {
		// 15 measure the size of the grid.  Each point is 50*15X50*15
		
		VSpatial createRectangle = this.factory.createRectangle(750, (int) (20d*Cell.orgScale));
		return createRectangle;

	}

	@Override
	public Color getColor(PlasmaMembrane object) {
		// eventually the color will reflect some local PlasmaMembrane characteristics
		int red = (int)object.getPmcolor();
		return new Color(255, 255-red, 255-red);
	}

	@Override
	public int getBorderSize(PlasmaMembrane object) {
		//if larger than 0, form a nice grid
		return 10;
	}

	@Override
	public Color getBorderColor(PlasmaMembrane object) {
		return new Color(100);
	}

	@Override
	public float getRotation(PlasmaMembrane object) {
		return 0;
	}

	@Override
	public float getScale(PlasmaMembrane object) {	
		return (float) 1;
	}

	@Override
	public String getLabel(PlasmaMembrane object) {
		return ""; 
	}
	@Override
	public Font getLabelFont(PlasmaMembrane object) {
		return new Font("sansserif", Font.PLAIN, 14);
	}

	@Override
	public float getLabelXOffset(PlasmaMembrane object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getLabelYOffset(PlasmaMembrane object) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Position getLabelPosition(PlasmaMembrane object) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getLabelColor(PlasmaMembrane object) {
		// TODO Auto-generated method stub
		return null;
	}

}
