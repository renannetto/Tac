package ro7.engine;

import java.awt.Graphics2D;

import cs195n.Vec2f;

public abstract class GameSpace {

	protected Vec2f position;
	protected Vec2f dimensions;

	protected GameSpace(Vec2f position, Vec2f dimensions) {
		this.position = position;
		this.dimensions = dimensions;
	}

	public abstract void draw(Graphics2D g, Vec2f min, Vec2f max);

	public Vec2f getDimensions() {
		return dimensions;
	}

}
