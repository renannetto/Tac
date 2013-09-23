package ro7.engine.sprites;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import cs195n.Vec2f;

/**
 * @author ro7
 * Sprite that represents a transparent rectangle
 */
public class EmptyRectangle extends Sprite {
	
	private Vec2f dimensions;
	private Color color;

	public EmptyRectangle(Vec2f position, Vec2f dimensions, Color color) {
		super(position);
		this.dimensions = dimensions;
		this.color = color;
	}

	@Override
	public void draw(Graphics2D g) {
		Rectangle2D rectangle = new Rectangle2D.Float(position.x, position.y, dimensions.x, dimensions.y);
		g.setColor(color);
		g.setStroke(new BasicStroke(3.0f));
		g.draw(rectangle);
	}

	public Shape getShape() {
		return new Rectangle2D.Float(position.x, position.y, dimensions.x, dimensions.y);
	}

}
