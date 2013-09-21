package ro7.game.sprites;

import ro7.engine.sprites.ImageSprite;
import ro7.engine.sprites.SpriteSheet;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class TerrainSprite extends ImageSprite {

	public TerrainSprite(Vec2f position, SpriteSheet sheet, Vec2i sheetPosition) {
		super(position, sheet, sheetPosition);
	}

	public TerrainSprite(TerrainSprite sprite) {
		super(sprite.position, sprite.sheet, sprite.sheetPosition);
	}
	
	public void setPosition(Vec2f position) {
		this.position = position;
	}

}
