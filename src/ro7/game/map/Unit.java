package ro7.game.map;

import java.awt.Graphics2D;

import ro7.engine.sprites.SpriteSheet;
import ro7.game.sprites.UnitSprite;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class Unit {
	
	private final float ATTACK_DAMAGE = 5.0f;

	protected Vec2f dimensions;
	protected Vec2f position;
	protected float lifepoints;

	private UnitSprite sprite;

	public Unit(Vec2f dimensions, SpriteSheet sheet,
			Vec2i sheetPosition) {
		this.dimensions = dimensions;
		this.position = new Vec2f(0.0f, 0.0f);
		this.lifepoints = 100;

		sprite = new UnitSprite(this.position, this.dimensions, false, sheet,
				sheetPosition);
	}

	public Unit(Unit unit) {
		this.dimensions = unit.dimensions;
		this.position = unit.position;
		this.lifepoints = unit.lifepoints;

		sprite = new UnitSprite(unit.sprite);
	}

	public void draw(Graphics2D g) {
		sprite.draw(g);
	}

	public void select() {
		sprite.select();
	}

	public void unselect() {
		sprite.unselect();
	}

	public Vec2i getMapPosition() {
		return new Vec2i(Math.round(position.x / dimensions.y),
				Math.round(position.y / dimensions.x));
	}

	public void move(Vec2f position) {
		this.position = position;
		sprite.setPosition(position);
	}

	public boolean isComputer() {
		return (this instanceof ComputerUnit);
	}

	@Override
	public String toString() {
		return getMapPosition().toString();
	}

	public Vec2i getTargetMapPosition(Vec2f position) {
		int xCoordinate;
		int yCoordinate;
		if (position.x < this.position.x) {
			xCoordinate = (int) Math.floor(position.x / dimensions.x);
		} else {
			xCoordinate = (int) Math.ceil(position.x / dimensions.x);
		}
		if (position.y < this.position.y) {
			yCoordinate = (int) Math.floor(position.y / dimensions.y);
		} else {
			yCoordinate = (int) Math.ceil(position.y / dimensions.y);
		}
		return new Vec2i(xCoordinate, yCoordinate);
	}

	public float distance(Unit unit) {
		return position.dist(unit.position);
	}
	
	public void attacked() {
		lifepoints -= ATTACK_DAMAGE;
	}

	public boolean isAlive() {
		return lifepoints > 0;
	}

}
