package ro7.game.sprites;

import java.awt.Color;
import java.awt.Graphics2D;

import ro7.engine.sprites.EmptyRectangle;
import ro7.engine.sprites.ImageSprite;
import ro7.engine.sprites.SpriteSheet;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class UnitSprite extends ImageSprite {

	private Vec2f dimensions;
	private boolean selected;

	public UnitSprite(Vec2f position, Vec2f dimensions, boolean selected,
			SpriteSheet sheet, Vec2i sheetPosition) {
		super(position, sheet, sheetPosition);
		this.dimensions = dimensions;
		this.selected = selected;
	}

	public UnitSprite(UnitSprite sprite) {
		super(sprite.position, sprite.sheet, sprite.sheetPosition);
		this.dimensions = sprite.dimensions;
		this.selected = sprite.selected;
	}

	@Override
	public void draw(Graphics2D g) {
		super.draw(g);
		if (selected) {
			EmptyRectangle selRectangle = new EmptyRectangle(position,
					dimensions, Color.BLUE);
			selRectangle.draw(g);
		}
	}

	public void setPosition(Vec2f position) {
		this.position = position;
	}

	public void select() {
		selected = true;
	}

	public void unselect() {
		selected = false;
	}

}
