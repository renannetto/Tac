package ro7.game.sprites;

import java.awt.Color;
import java.awt.Graphics2D;

import ro7.engine.sprites.FilledRectangle;
import ro7.engine.sprites.FloatingSprite;
import cs195n.Vec2f;

public class HealthBar extends FloatingSprite {

	private Vec2f dimensions;
	private float lifepoints;

	public HealthBar(Vec2f position, Vec2f dimensions) {
		super(position);
		this.dimensions = dimensions;
		this.lifepoints = 100.0f;
	}

	@Override
	public void drawSprite(Graphics2D g) {
		Vec2f lifeDimensions = dimensions.pmult(lifepoints / 100.0f, 1.0f);
		FilledRectangle lifeBar = new FilledRectangle(position,
				lifeDimensions, Color.GREEN, Color.GREEN);
		lifeBar.draw(g);

		float damage = 100 - lifepoints;
		if (damage > 0) {
			FilledRectangle damageBar = new FilledRectangle(position.plus(
					lifeDimensions.x, 0.0f), dimensions.pmult(damage / 100.0f,
					1.0f), Color.RED, Color.RED);
			damageBar.draw(g);
		}
	}

	public void move(Vec2f position) {
		this.position = position;
	}

	public void damage(float damage) {
		this.lifepoints -= damage;
	}

}
