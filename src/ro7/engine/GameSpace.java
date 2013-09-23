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

	/**
	 * Draw the gamespace inside the viewport
	 * @param g Graphics object used to draw
	 * @param min minimum game coordinate that will appear on the viewport
	 * @param max maximum game coordinate that will appear on the viewport
	 * @param viewport the current viewport
	 */
	public abstract void draw(Graphics2D g, Vec2f min, Vec2f max, Viewport viewport);

	public Vec2f getDimensions() {
		return dimensions;
	}

}
