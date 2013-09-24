package ro7.game.sprites;

import static ro7.game.sprites.Direction.DOWN;
import static ro7.game.sprites.Direction.LEFT;
import static ro7.game.sprites.Direction.RIGHT;
import static ro7.game.sprites.Direction.UP;

import java.awt.Color;
import java.awt.Graphics2D;

import ro7.engine.Viewport;
import ro7.engine.sprites.AnimatedSprite;
import ro7.engine.sprites.EmptyRectangle;
import ro7.engine.sprites.SpriteSheet;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class UnitSprite extends AnimatedSprite {

	private final int TIME_TO_MOVE = 300;
	private final Vec2f HEALTH_BAR_DIMENSIONS = new Vec2f(20.0f, 2.0f);

	private Vec2f dimensions;
	private boolean selected;

	protected Direction direction;
	
	private HealthBar healthBar;

	public UnitSprite(Vec2f position, Vec2f dimensions, boolean selected,
			SpriteSheet sheet, Vec2i sheetPosition, int frames) {
		super(position, sheet, sheetPosition, frames, 300);
		this.dimensions = dimensions;
		this.selected = selected;

		this.direction = DOWN;
		
		this.healthBar = new HealthBar(position, HEALTH_BAR_DIMENSIONS);
	}

	public UnitSprite(UnitSprite sprite) {
		super(sprite.position, sprite.sheet, sprite.sheetPosition,
				sprite.frames, sprite.TIME_TO_MOVE);
		this.dimensions = sprite.dimensions;
		this.selected = sprite.selected;
		this.direction = sprite.direction;
		
		this.healthBar = new HealthBar(position, HEALTH_BAR_DIMENSIONS);
	}

	public void draw(Graphics2D g, Viewport viewport) {
		super.draw(g);
		if (selected) {
			EmptyRectangle selRectangle = new EmptyRectangle(position,
					dimensions, Color.BLUE);
			selRectangle.draw(g);
		}
		healthBar.draw(g, viewport);
	}

	public void move(Vec2f position) {
		updateDirection(position);

		this.position = position;
		
		healthBar.move(position);
	}
	
	public void updateDirection(Vec2f position) {
		if (position.x > this.position.x) {
			direction = RIGHT;
		} else if (position.x < this.position.x) {
			direction = LEFT;
		} else if (position.y > this.position.y) {
			direction = DOWN;
		} else {
			direction = UP;
		}
	}

	public void select() {
		selected = true;
	}
	
	@Override
	public void updateSheetPosition() {
		int row = 0;
		switch (direction) {
		case UP:
			row = 0;
			break;
		case RIGHT:
			row = 1;
			break;
		case DOWN:
			row = 2;
			break;
		case LEFT:
			row = 3;
			break;
		}
		
		int column = currentFrame;
		sheetPosition = new Vec2i(column, row);
	}

	public void unselect() {
		selected = false;
	}

	public void damage(float damage) {
		healthBar.damage(damage);
	}

}
