package ro7.game.map;

import java.awt.Graphics2D;

import ro7.game.sprites.UnitSprite;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class Unit {

	private boolean computer;
	private boolean selected;
	private float size;
	private Vec2f position;
	
	private UnitSprite sprite;

	public Unit(boolean computer, float size) {
		this.computer = computer;
		this.size = size;
		this.position = new Vec2f(0.0f, 0.0f);
		this.selected = false;
		
		sprite = new UnitSprite(this.position, this.computer, this.size, this.selected);
	}

	public Unit(Unit unit) {
		this.computer = unit.computer;
		this.size = unit.size;
		this.position = unit.position;
		this.selected = unit.selected;
		
		sprite = new UnitSprite(this.position, this.computer, this.size, this.selected);
	}

	public void draw(Graphics2D g) {
		sprite.draw(g);
	}

	public void select() {
		selected = true;
		sprite = new UnitSprite(this.position, this.computer, this.size, this.selected);
	}

	public void unselect() {
		selected = false;
		sprite = new UnitSprite(this.position, this.computer, this.size, this.selected);
	}

	public Vec2i getMapPosition() {
		return new Vec2i(Math.round(position.y/size), Math.round(position.x/size));
	}

	public void move(Vec2f position) {
		this.position = position;
		sprite = new UnitSprite(this.position, this.computer, this.size, this.selected);
	}

	public boolean isComputer() {
		return computer;
	}
	
	@Override
	public String toString() {
		return getMapPosition().toString();
	}

	public Vec2i getTargetMapPosition(Vec2f position) {
		int xCoordinate;
		int yCoordinate;
		if (position.x < this.position.x) {
			yCoordinate = (int)Math.floor(position.x / size);
		} else {
			yCoordinate = (int)Math.ceil(position.x / size);
		}
		if (position.y < this.position.y) {
			xCoordinate = (int)Math.floor(position.y / size);
		} else {
			xCoordinate = (int)Math.ceil(position.y / size);
		}
		return new Vec2i(xCoordinate, yCoordinate);
	}

}
