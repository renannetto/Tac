package ro7.game.model;

import java.awt.Graphics2D;

import ro7.engine.Viewport;
import ro7.game.sprites.UnitSprite;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class Unit {

	protected final float ATTACK_DAMAGE = 1f;

	protected Vec2f dimensions;
	protected Vec2f position;
	protected float lifepoints;

	private boolean attacking;

	private UnitSprite movingSprite;
	private UnitSprite attackingSprite;

	public Unit(Vec2f dimensions, UnitSprite movingSprite,
			UnitSprite attackingSprite) {
		this.dimensions = dimensions;
		this.position = new Vec2f(0.0f, 0.0f);
		this.lifepoints = 100;

		this.attacking = false;

		this.movingSprite = movingSprite;
		this.attackingSprite = attackingSprite;
	}

	public Unit(Unit unit) {
		this.dimensions = unit.dimensions;
		this.position = unit.position;
		this.lifepoints = unit.lifepoints;

		this.attacking = false;

		this.movingSprite = new UnitSprite(unit.movingSprite);
		this.attackingSprite = new UnitSprite(unit.attackingSprite);
	}

	public void draw(Graphics2D g, Viewport viewport) {
		if (!attacking) {
			movingSprite.draw(g, viewport);
		} else {
			attackingSprite.draw(g, viewport);
		}
	}

	public void updateMoving(long nanosSincePreviousTick) {
		movingSprite.update(nanosSincePreviousTick);
	}

	public void updateAttacking(long nanosSincePreviousTick) {
		attackingSprite.update(nanosSincePreviousTick);
	}

	public void select() {
		movingSprite.select();
	}

	public void unselect() {
		movingSprite.unselect();
	}

	public Vec2i getMapPosition() {
		return new Vec2i(Math.round(position.x / dimensions.y),
				Math.round(position.y / dimensions.x));
	}

	public void move(Vec2f position) {
		this.position = position;
		if (attacking) {
			stopAttack();
		}
		movingSprite.move(position);
		attackingSprite.move(position);
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

	public void attacked(Unit attacker) {
		float damage = attacker.getAttackDamage();
		lifepoints -= damage;
		movingSprite.damage(damage);
		attackingSprite.damage(damage);
	}
	
	protected float getAttackDamage() {
		return ATTACK_DAMAGE;
	}

	public boolean isAlive() {
		return lifepoints > 0;
	}

	public boolean isAlly(Unit unit) {
		return (this.isComputer() && unit.isComputer())
				|| (!this.isComputer() && !unit.isComputer());
	}

	public boolean nextTo(Unit targetUnit) {
		float distance = this.distance(targetUnit);
		return (distance <= targetUnit.dimensions.x || distance <= targetUnit.dimensions.y);
	}

	public void attack(Unit target) {
		attackingSprite.updateDirection(target.position);
		attacking = true;
	}

	public void stopAttack() {
		attacking = false;
	}

}
