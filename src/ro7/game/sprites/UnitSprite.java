package ro7.game.sprites;

import java.awt.Color;
import java.awt.Graphics2D;

import cs195n.Vec2f;
import ro7.engine.sprites.Circle;
import ro7.engine.sprites.Sprite;

public class UnitSprite extends Sprite {
	
	private boolean computer;
	private float size;
	private boolean selected;

	public UnitSprite(Vec2f position, boolean computer, float size, boolean selected) {
		super(position);
		this.computer = computer;
		this.size = size;
		this.selected = selected;
	}

	@Override
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

}
