package ro7.engine.sprites;

import java.awt.Graphics2D;

import cs195n.Vec2f;
import cs195n.Vec2i;

/**
 * @author ro7
 * Sprite that represents an Image
 */
public class ImageSprite extends Sprite {
	
	protected SpriteSheet sheet;
	protected Vec2i sheetPosition; 

	protected ImageSprite(Vec2f position, SpriteSheet sheet, Vec2i sheetPosition) {
		super(position);
		this.sheet = sheet;
		this.sheetPosition = sheetPosition;
	}

	@Override
	public void draw(Graphics2D g) {
		sheet.draw(g, sheetPosition, position);
	}

}
