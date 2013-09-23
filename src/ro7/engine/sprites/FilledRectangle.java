package ro7.engine.sprites;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

import cs195n.Vec2f;

/**
 * @author ro7
 * Sprite that represents a colored rectangle
 */
public class FilledRectangle extends Sprite {
	
	private Vec2f dimensions;
	private Color borderColor;
	private Color fillColor;

	public FilledRectangle(Vec2f position, Vec2f dimensions, Color borderColor, Color fillColor) {
		super(position);
		this.dimensions = dimensions;
		this.borderColor = borderColor;
		this.fillColor = fillColor;
	}

	@Override
	public void draw(Graphics2D g) {
		Rectangle2D rectangle = new Rectangle2D.Float(position.x, position.y, dimensions.x, dimensions.y);
		
		Stroke oldStroke = g.getStroke();
		g.setColor(borderColor);
		g.setStroke(new BasicStroke(3.0f));
		g.draw(rectangle);
		g.setStroke(oldStroke);
		
		g.setColor(fillColor);
		g.fill(rectangle);
	}

}
