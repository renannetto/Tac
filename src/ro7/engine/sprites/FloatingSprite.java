package ro7.engine.sprites;

import java.awt.Graphics2D;

import ro7.engine.Viewport;
import cs195n.Vec2f;

/**
 * @author ro7
 * Sprite that will be drawn above a viewport
 */
public abstract class FloatingSprite {

	protected Vec2f position;

	protected FloatingSprite(Vec2f position) {
		this.position = position;
	}

	/**
	 * Go back to screen coordinates, draw the sprite
	 * and go to game coordinates again
	 * @param g Graphics object used to draw
	 * @param viewport current viewport
	 */
	public void draw(Graphics2D g, Viewport viewport) {
		viewport.undoTransform(g);
		
		Vec2f screenPosition = viewport.gameToScreen(position);
		Vec2f translateVector = screenPosition.minus(position);
		
		g.translate(translateVector.x, translateVector.y);
		drawSprite(g);
		g.translate(-translateVector.x, -translateVector.y);
		
		viewport.doTransform(g);
	}
	
	public abstract void drawSprite(Graphics2D g);

}
