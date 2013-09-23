package ro7.engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;

import ro7.engine.sprites.EmptyRectangle;
import cs195n.Vec2f;

public class Viewport {

	private Vec2f position;
	private Vec2f dimensions;
	private GameSpace gameSpace;
	private Vec2f scale;
	private Vec2f gamePosition;

	public Viewport(Vec2f position, Vec2f dimensions, GameSpace gameSpace) {
		this.position = position;
		this.dimensions = dimensions;
		this.gameSpace = gameSpace;
		this.scale = dimensions.pdiv(gameSpace.getDimensions());
		this.gamePosition = new Vec2f(0.0f, 0.0f);
	}

	public Viewport(Vec2f position, Vec2f dimensions, GameSpace gameSpace,
			Vec2f scale, Vec2f gamePosition) {
		this.position = position;
		this.dimensions = dimensions;
		this.gameSpace = gameSpace;
		this.scale = scale;
		this.gamePosition = gamePosition;
	}

	/**
	 * Transform the world to game coordinates
	 * @param g Graphics object used to draw
	 */
	public void doTransform(Graphics2D g) {
		g.translate(position.x, position.y);
		g.scale(scale.x, scale.y);
		g.translate(-gamePosition.x, -gamePosition.y);
	}

	/**
	 * Transform the world to screen coordinates
	 * @param g Graphics object used to draw
	 */
	public void undoTransform(Graphics2D g) {
		g.translate(gamePosition.x, gamePosition.y);
		g.scale(1.0f / scale.x, 1.0f / scale.y);
		g.translate(-position.x, -position.y);
	}

	/**
	 * Calculate the game coordinates of a screen point
	 * @param point screen coordinates point
	 * @return game coordinates of the point
	 */
	public Vec2f screenToGame(Vec2f point) {
		return point.minus(position).pdiv(scale).plus(gamePosition);
	}
	
	/**
	 * Calculate the screen coordinates of a game point
	 * @param point game coordinates point
	 * @return screen coordinates of the point
	 */
	public Vec2f gameToScreen(Vec2f point) {
		return point.minus(gamePosition).pmult(scale).plus(position);
	}

	/**
	 * Increase the scale of the viewport
	 * @param factor quantity to increase the scale
	 */
	public void zoomIn(float factor) {
		Vec2f viewportDimensions = getGameDimensions();

		scale = scale.smult(factor);

		Vec2f translateVector = viewportDimensions.smult(factor - 1).sdiv(2.0f);
		translate(translateVector);
	}

	/**
	 * Decrease the scale of the viewport
	 * @param factor quantity to decrease the scale
	 */
	public void zoomOut(float factor) {
		Vec2f viewportDimensions = getGameDimensions();

		scale = scale.sdiv(factor);

		Vec2f translateVector = viewportDimensions.smult(-(factor - 1)).sdiv(
				2.0f);
		translate(translateVector);
	}
	
	/**
	 * Get viewport dimensions on game coordinates
	 * @return viewport dimensions on game coordinates
	 */
	private Vec2f getGameDimensions() {
		Vec2f min = screenToGame(position);
		Vec2f max = screenToGame(dimensions);
		
		return max.minus(min);
	}

	/**
	 * Move the viewport on the game
	 * @param direction vector to move the viewport
	 */
	public void translate(Vec2f direction) {
		gamePosition = gamePosition.plus(direction);
	}

	/**
	 * Set the clipping area, apply the transformation to game
	 * coordinates, draw the GameSpace content and restore 
	 * the Graphics state to the previous one
	 * @param g Graphics object used to draw
	 */
	public void draw(Graphics2D g) {
		EmptyRectangle viewport = new EmptyRectangle(position, dimensions,
				Color.BLACK);
		viewport.draw(g);

		Shape clip = g.getClip();
		g.setClip(viewport.getShape());
		doTransform(g);

		Vec2f min = screenToGame(position);
		Vec2f max = screenToGame(dimensions);

		gameSpace.draw(g, min, max, this);
		undoTransform(g);
		g.setClip(clip);
	}

	public Vec2f getGamePosition() {
		return gamePosition;
	}

	public Vec2f getScale() {
		return scale;
	}

}
