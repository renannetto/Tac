package ro7.game.map;

import java.awt.Color;
import java.awt.Graphics2D;

import ro7.engine.sprites.Circle;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class Unit {

	private boolean computer;
	private boolean selected;
	private float size;
	private Vec2f position;

	public Unit(boolean computer, float size) {
		this.computer = computer;
		this.size = size;
		this.position = new Vec2f(0.0f, 0.0f);
		this.selected = false;
	}

	public Unit(Unit unit) {
		this.computer = unit.computer;
		this.size = unit.size;
		this.position = unit.position;
		this.selected = unit.selected;
	}

	public void setPosition(Vec2f newPosition) {
		this.position = newPosition;
	}

	public void draw(Graphics2D g) {
		Color borderColor;
		if (selected) {
			borderColor = Color.BLUE;
		} else {
			borderColor = Color.WHITE;
		}

		Color fillColor;
		if (computer) {
			fillColor = Color.RED;
		} else {
			fillColor = Color.GREEN;
		}
		Circle unit = new Circle(position, size, borderColor, fillColor);
		unit.draw(g);
	}

	public void select() {
		selected = true;
	}

	public void unselect() {
		selected = false;
	}

	public Vec2i getMapPosition() {
		return new Vec2i(Math.round(position.y/size), Math.round(position.x/size));
	}

	public void move(Vec2f position) {
		this.position = position;
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
	
	public boolean inside(Vec2f min, Vec2f max) {
		return (position.x > min.x && position.y > min.y && position.x < max.x && position.y < max.y);
	}

}
