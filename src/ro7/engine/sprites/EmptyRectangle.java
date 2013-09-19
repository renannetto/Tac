package ro7.engine.sprites;

import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

import cs195n.Vec2f;

public class EmptyRectangle extends Sprite {
	
	private Vec2f dimensions;

	public EmptyRectangle(Vec2f position, Vec2f dimensions) {
		super(position);
		this.dimensions = dimensions;
	}

	@Override
	public void draw(Graphics2D g) {
		Rectangle2D rectangle = new Rectangle2D.Float(position.x, position.y, dimensions.x, dimensions.y);
		g.draw(rectangle);
	}

	public Shape getShape() {
		return new Rectangle2D.Float(position.x, position.y, dimensions.x, dimensions.y);
	}

}
