package ro7.game.model;

import static ro7.engine.ai.Status.FAILURE;
import static ro7.engine.ai.Status.RUNNING;
import static ro7.engine.ai.Status.SUCCESS;
import ro7.engine.ai.Action;
import ro7.engine.ai.BTNode;
import ro7.engine.ai.Composite;
import ro7.engine.ai.Condition;
import ro7.engine.ai.Selector;
import ro7.engine.ai.Sequence;
import ro7.engine.ai.Status;
import ro7.game.sprites.UnitSprite;
import cs195n.Vec2f;

public class ComputerUnit extends Unit {
	
	protected final float ATTACK_DAMAGE = 0.5f;

	private Composite root;
	private Unit target;
	
	private float elapsedTime = 0;

	public ComputerUnit(Vec2f dimensions, UnitSprite movingSprite, UnitSprite attackingSprite, GameMap map) {
		super(dimensions, movingSprite, attackingSprite);
		buildBehaviorTree(map);
	}

	public ComputerUnit(ComputerUnit unit, GameMap map) {
		super(unit);
		buildBehaviorTree(map);
	}

	private void buildBehaviorTree(GameMap map) {
		root = new Selector();
		Composite defense = new Sequence();
		Composite attack = new Selector();
		Composite attackSingle = new Sequence();

		BTNode health = new HealthNode();
		BTNode aloneNode = new AloneNode(map);
		BTNode regroup = new RegroupNode(map);
		BTNode aloneEnemy = new AloneEnemyNode(map);
		BTNode attackAlone = new AttackAloneNode(map);
		BTNode attackAny = new AttackAnyNode(map);

		defense.addChild(health);
		defense.addChild(aloneNode);
		defense.addChild(regroup);

		attackSingle.addChild(aloneEnemy);
		attackSingle.addChild(attackAlone);

		attack.addChild(attackSingle);
		attack.addChild(attackAny);

		root.addChild(defense);
		root.addChild(attack);
	}

	public void update(float nanoseconds) {
		elapsedTime += nanoseconds / 1000000000.0f;
		if (elapsedTime >= 0.1) {
			elapsedTime = 0;
			root.update(nanoseconds);
		}
	}
	
	protected float getAttackDamage() {
		return ATTACK_DAMAGE;
	}

	/**
	 * @author ro7
	 * Check if the unit is below 50% health
	 */
	public class HealthNode extends Condition {

		@Override
		public boolean checkCondition(float nanoseconds) {
			return lifepoints < 50;
		}

		@Override
		public void reset() {
			// TODO Auto-generated method stub
			
		}

	}

	/**
	 * @author ro7
	 * Check if the unit is alone
	 */
	public class AloneNode extends Condition {

		private GameMap map;

		public AloneNode(GameMap map) {
			this.map = map;
		}

		@Override
		public void reset() {
			// TODO Auto-generated method stub

		}

		@Override
		public boolean checkCondition(float nanoseconds) {
			return map.isAlone(ComputerUnit.this);
		}

	}

	/**
	 * @author ro7
	 * Try to move closer to an ally
	 */
	public class RegroupNode extends Action {

		private GameMap map;
		private Status status;

		public RegroupNode(GameMap map) {
			this.map = map;
			this.status = FAILURE;
		}

		@Override
		public Status act(float nanoseconds) {
			Unit ally = map.getClosestAlly(ComputerUnit.this);

			if (ally == null) {
				status = FAILURE;
				return FAILURE;
			}
			if (ComputerUnit.this.nextTo(ally)) {
				status = SUCCESS;
				return SUCCESS;
			}
			if (status != RUNNING) {
				if (map.moveToUnit(ComputerUnit.this, ally.getMapPosition())) {
					status = RUNNING;
					return RUNNING;
				} else {
					status = FAILURE;
					return FAILURE;
				}
			}
			return status;
		}

		@Override
		public void reset() {
			
		}

	}

	/**
	 * @author ro7
	 * Look for the closest alone enemy
	 */
	public class AloneEnemyNode extends Action {

		private GameMap map;

		public AloneEnemyNode(GameMap map) {
			this.map = map;
		}

		@Override
		public Status act(float nanoseconds) {
			Unit enemy = map.getAloneEnemy(ComputerUnit.this);

			if (enemy == null) {
				return FAILURE;
			}
			target = enemy;
			return SUCCESS;
		}

		@Override
		public void reset() {
			// TODO Auto-generated method stub

		}

	}

	/**
	 * @author ro7
	 * Attack closest alone enemy
	 */
	public class AttackAloneNode extends Action {

		private GameMap map;
		private Status status;
		private Vec2f oldPosition;

		public AttackAloneNode(GameMap map) {
			this.map = map;
			this.status = FAILURE;
			this.oldPosition = ComputerUnit.this.position;
		}

		@Override
		public Status act(float nanoseconds) {
			if (target.isAlive() && ComputerUnit.this.nextTo(target)) {
				map.attack(ComputerUnit.this, target);
				status = SUCCESS;
				return SUCCESS;
			}
			if (!target.isAlive()) {
				return FAILURE;
			}
			if (status != RUNNING) {
				if (map.moveToUnit(ComputerUnit.this, target.getMapPosition())) {
					status = RUNNING;
					return RUNNING;
				} else {
					status = FAILURE;
					return FAILURE;
				}
			} else if (oldPosition.equals(ComputerUnit.this.position)) {
				status = FAILURE;
				return FAILURE;
			}
			oldPosition = ComputerUnit.this.position;
			return status;
		}

		@Override
		public void reset() {
			
		}

	}

	/**
	 * @author ro7
	 * Attack closest enemy
	 */
	public class AttackAnyNode extends Action {

		private GameMap map;
		private Status status;
		private Vec2f oldPosition;

		public AttackAnyNode(GameMap map) {
			this.map = map;
			this.status = FAILURE;
			this.oldPosition = ComputerUnit.this.position;
		}

		@Override
		public Status act(float nanoseconds) {
			Unit enemy = map.getClosestEnemy(ComputerUnit.this);
			if (enemy == null) {
				status = FAILURE;
				return FAILURE;
			}

			if (ComputerUnit.this.nextTo(enemy)) {
				map.attack(ComputerUnit.this, enemy);
				status = SUCCESS;
				return SUCCESS;
			}

			if (status != RUNNING) {
				if (map.moveToUnit(ComputerUnit.this, enemy.getMapPosition())) {
					status = RUNNING;
					return RUNNING;
				} else {
					status = FAILURE;
					return FAILURE;
				}
			} else if (oldPosition.equals(ComputerUnit.this.position)) {
				status = FAILURE;
				return FAILURE;
			}
			oldPosition = ComputerUnit.this.position;
			return status;
		}

		@Override
		public void reset() {
			
		}

	}

}
