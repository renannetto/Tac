package ro7.game.model;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro7.engine.GameSpace;
import ro7.engine.Viewport;
import ro7.engine.util.Node;
import ro7.game.map.MapGraph;
import ro7.game.map.TacNode;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class GameMap extends GameSpace {

	private final int SQUARE_SIZE;
	private final int MOVE_STEPS = 30;
	private final int ALONE_REGION = 2;

	private Vec2i matrixDimensions;

	private Map<Vec2i, Terrain> terrains;
	private Map<Vec2i, Unit> units;

	private Unit selected;

	private MapGraph mapGraph;

	private Map<Unit, List<Vec2f>> moving;

	private Map<Unit, Unit> attacking;

	public GameMap(Vec2f position, Vec2f dimensions, Vec2i matrixDimensions) {
		super(position, dimensions);

		this.matrixDimensions = matrixDimensions;

		SQUARE_SIZE = (int) (dimensions.x / matrixDimensions.x);

		terrains = new HashMap<Vec2i, Terrain>();
		units = new HashMap<Vec2i, Unit>();

		moving = new HashMap<Unit, List<Vec2f>>();

		attacking = new HashMap<Unit, Unit>();
	}

	public void buildGraph() {
		mapGraph = new MapGraph(terrains, units);
	}

	@Override
	public void draw(Graphics2D g, Vec2f min, Vec2f max, Viewport viewport) {
		Vec2i minMapPosition = new Vec2i(Math.max(
				(int) Math.floor(min.x / SQUARE_SIZE), 0), Math.max(
				(int) Math.floor(min.y / SQUARE_SIZE), 0));
		Vec2i maxMapPosition = new Vec2i(Math.min(
				(int) Math.ceil(max.x / SQUARE_SIZE), getWidth() - 1),
				Math.min((int) Math.ceil(max.y / SQUARE_SIZE), getHeight() - 1));

		for (int i = minMapPosition.x; i <= maxMapPosition.x; i++) {
			for (int j = minMapPosition.y; j <= maxMapPosition.y; j++) {
				Vec2i mapPosition = new Vec2i(i, j);
				Terrain terrain = terrains.get(mapPosition);
				terrain.draw(g);
			}
		}

		for (int i = minMapPosition.x; i <= maxMapPosition.x; i++) {
			for (int j = minMapPosition.y; j <= maxMapPosition.y; j++) {
				Vec2i mapPosition = new Vec2i(i, j);
				Unit unit = units.get(mapPosition);
				if (unit != null) {
					unit.draw(g, viewport);
				}
			}
		}
	}

	public void addTerrain(Terrain terrain, Vec2i position) {
		terrains.put(position, terrain);
	}

	public void addUnit(Unit unit, Vec2i position) {
		units.put(position, unit);
	}

	public int getWidth() {
		return matrixDimensions.x;
	}

	public int getHeight() {
		return matrixDimensions.y;
	}

	/**
	 * If the clicked position is a player unit, select it.
	 * If it is a terrain, move the selected unit to there.
	 * @param gamePosition clicked position
	 */
	public void clicked(Vec2f gamePosition) {
		Vec2i mapPosition = new Vec2i((int) (gamePosition.x / SQUARE_SIZE),
				(int) (gamePosition.y / SQUARE_SIZE));

		if (isPlayerUnit(mapPosition)) {
			Unit unit = units.get(mapPosition);
			unit.select();
			if (selected != null && !selected.equals(unit)) {
				selected.unselect();
			}
			selected = unit;
		} else {
			if (selected != null && isPassable(mapPosition)) {
				getPathTo(selected, mapPosition);
			}
		}
	}

	/**
	 * If the clicked position is a computer unit, attack it
	 * @param gamePosition clicked position
	 */
	public void rightClicked(Vec2f gamePosition) {
		Vec2i mapPosition = new Vec2i((int) (gamePosition.x / SQUARE_SIZE),
				(int) (gamePosition.y / SQUARE_SIZE));

		if (isComputerUnit(mapPosition)) {
			if (selected != null) {
				if (!selected.nextTo(units.get(mapPosition))) {
					moveToUnit(selected, mapPosition);
				}
				attack(selected, units.get(mapPosition));
			}
		}
	}

	/**
	 * Get shortest path from unit to map position
	 * @param unit unit to move
	 * @param mapPosition destination map position
	 * @return list of nodes with shortest path, or null
	 * if the path does not exist
	 */
	private List<Node> getPathTo(Unit unit, Vec2i mapPosition) {
		Vec2i oldPosition = unit.getMapPosition();

		mapGraph.addNode(oldPosition);

		List<Node> path = mapGraph.shortestPath(oldPosition, mapPosition);

		mapGraph.removeNode(oldPosition);

		if (path != null) {
			List<Vec2f> movingPath = new ArrayList<Vec2f>();
			Vec2f previousPoint = mapToGameCoordinates(oldPosition);
			path.remove(0);
			for (Node node : path) {
				TacNode tacNode = (TacNode) node;
				Vec2f currentPoint = tacNode.getPosition();
				movingPath = interpolate(previousPoint, currentPoint,
						movingPath);
				previousPoint = currentPoint;
			}
			moving.put(unit, movingPath);
		}

		return path;
	}

	private Vec2f mapToGameCoordinates(Vec2i oldPosition) {
		return new Vec2f(oldPosition.x * SQUARE_SIZE, oldPosition.y
				* SQUARE_SIZE);
	}

	/**
	 * Interpolate two points on the map and add to the current path
	 * @param previousPoint first point to be interpolated
	 * @param currentPoint second point to be interpolated
	 * @param path current path
	 * @return the new path with the interpolation
	 */
	private List<Vec2f> interpolate(Vec2f previousPoint, Vec2f currentPoint,
			List<Vec2f> path) {
		for (int i = 1; i <= MOVE_STEPS; i++) {
			path.add(previousPoint.lerpTo(currentPoint, (float) i / MOVE_STEPS));
		}
		return path;
	}

	private boolean isPassable(Vec2i mapPosition) {
		Terrain terrain = terrains.get(mapPosition);
		return terrain != null && terrain.isPassable() && !isUnit(mapPosition);
	}

	private boolean isUnit(Vec2i mapPosition) {
		return units.get(mapPosition) != null;
	}

	private boolean isPlayerUnit(Vec2i mapPosition) {
		Unit unit = units.get(mapPosition);
		return unit != null && !unit.isComputer();
	}

	private boolean isComputerUnit(Vec2i mapPosition) {
		return isUnit(mapPosition) && !isPlayerUnit(mapPosition);
	}

	/**
	 * Move unit to position
	 * @param unit unit to be moved
	 * @param position position to be moved
	 * @return true if it was able to move, false otherwise
	 */
	public boolean moveUnit(Unit unit, Vec2f position) {
		Vec2i oldMapPosition = unit.getMapPosition();
		Vec2i nextMapPosition = unit.getTargetMapPosition(position);

		if (isUnit(nextMapPosition) && !oldMapPosition.equals(nextMapPosition)) {
			return false;
		}

		unit.move(position);

		Vec2i newMapPosition = unit.getMapPosition();

		if (!oldMapPosition.equals(newMapPosition)) {
			units.remove(oldMapPosition);
			units.put(newMapPosition, unit);
			mapGraph.addNode(oldMapPosition);
			mapGraph.removeNode(newMapPosition);
		}
		return true;
	}

	/**
	 * Move all units to their next position on their paths
	 */
	public void moveUnits() {
		List<Unit> toRemove = new ArrayList<Unit>();

		for (Map.Entry<Unit, List<Vec2f>> movingUnits : moving.entrySet()) {
			Unit unit = movingUnits.getKey();
			List<Vec2f> path = movingUnits.getValue();
			if (moveUnit(unit, path.get(0))) {
				path.remove(0);
				if (path.size() == 0) {
					toRemove.add(unit);
				}
			}
		}

		for (Unit unit : toRemove) {
			moving.remove(unit);
		}
	}

	public Unit getClosestAlly(Unit unit) {
		float minDistance = Float.MAX_VALUE;
		Unit closest = null;

		for (Unit neighbor : units.values()) {
			if (neighbor.isAlly(unit)) {
				float distance = unit.distance(neighbor);
				if (distance < minDistance) {
					minDistance = distance;
					closest = neighbor;
				}
			}
		}

		return closest;
	}

	/**
	 * Get closest alone enemy
	 * @param unit unit looking for the enemy
	 * @return closest alone enemy to unit, or null if there is no one
	 */
	public Unit getAloneEnemy(Unit unit) {
		float minDistance = Float.MAX_VALUE;
		Unit closest = null;

		for (Unit neighbor : units.values()) {
			if (!neighbor.isAlly(unit) && isAlone(neighbor)) {
				float distance = unit.distance(neighbor);
				if (distance < minDistance) {
					minDistance = distance;
					closest = neighbor;
				}
			}
		}

		return closest;
	}

	public boolean isAlone(Unit unit) {
		Vec2i mapPosition = unit.getMapPosition();
		for (int i = mapPosition.x - ALONE_REGION; i <= mapPosition.x
				+ ALONE_REGION; i++) {
			for (int j = mapPosition.y - ALONE_REGION; j <= mapPosition.y
					+ ALONE_REGION; j++) {
				if (i != mapPosition.x || j != mapPosition.y) {
					Unit neighbor = units.get(new Vec2i(i, j));
					if (neighbor != null && neighbor.isAlly(unit)) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * Try to move next to another unit
	 * @param unit unit to move
	 * @param mapPosition map position of the target unit
	 * @return true if it is possible to move, false otherwise
	 */
	public boolean moveToUnit(Unit unit, Vec2i mapPosition) {
		List<Node> path = null;

		Vec2i left = new Vec2i(mapPosition.x - 1, mapPosition.y);
		if (isPassable(left)) {
			path = getPathTo(unit, left);
			if (path != null) {
				return true;
			}
		}

		Vec2i right = new Vec2i(mapPosition.x + 1, mapPosition.y);
		if (isPassable(right)) {
			path = getPathTo(unit, right);
			if (path != null) {
				return true;
			}
		}

		Vec2i up = new Vec2i(mapPosition.x, mapPosition.y - 1);
		if (isPassable(up)) {
			path = getPathTo(unit, up);
			if (path != null) {
				return true;
			}
		}

		Vec2i down = new Vec2i(mapPosition.x, mapPosition.y + 1);
		if (isPassable(down)) {
			path = getPathTo(unit, down);
			if (path != null) {
				return true;
			}
		}

		return false;
	}

	public Unit getClosestEnemy(Unit unit) {
		float minDistance = Float.MAX_VALUE;
		Unit closest = null;

		for (Unit neighbor : units.values()) {
			if (!neighbor.isAlly(unit)) {
				float distance = unit.distance(neighbor);
				if (distance < minDistance) {
					minDistance = distance;
					closest = neighbor;
				}
			}
		}

		return closest;
	}

	/**
	 * Update AI, and units animations
	 * @param nanoseconds
	 */
	public void update(long nanoseconds) {
		for (Unit unit : units.values()) {
			if (unit.isComputer()) {
				((ComputerUnit) unit).update(nanoseconds);
			}
		}
		
		for (Unit unit : moving.keySet()) {
			unit.updateMoving(nanoseconds);
		}
		
		for (Unit unit: attacking.keySet()) {
			unit.updateAttacking(nanoseconds);
		}
	}

	/**
	 * Look for dead units and remove them from the game
	 */
	public void checkAliveUnits() {
		List<Vec2i> deadUnits = new ArrayList<Vec2i>();

		for (Map.Entry<Vec2i, Unit> entryUnits : units.entrySet()) {
			Unit unit = entryUnits.getValue();
			Vec2i mapPosition = entryUnits.getKey();
			if (!unit.isAlive()) {
				mapGraph.addNode(mapPosition);
				moving.remove(unit);
				attacking.remove(unit);
				if (selected != null && selected.equals(unit)) {
					selected = null;
				}
				deadUnits.add(mapPosition);
			}
		}

		for (Vec2i mapPosition : deadUnits) {
			units.remove(mapPosition);
		}
	}

	public boolean win() {
		for (Unit unit : units.values()) {
			if (unit.isComputer()) {
				return false;
			}
		}
		return true;
	}

	public boolean lose() {
		for (Unit unit : units.values()) {
			if (!unit.isComputer()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Make the attacking units do their action if are next to their
	 * target
	 */
	public void attackUnits() {
		for (Map.Entry<Unit, Unit> entryUnits : attacking.entrySet()) {
			Unit attacker = entryUnits.getKey();
			Unit target = entryUnits.getValue();

			if (attacker.nextTo(target)) {
				attacker.attack(target);
				target.attacked(attacker);
				if (!target.isAlive()) {
					attacker.stopAttack();
				}
			} else {
				attacker.stopAttack();
			}
		}
	}

	public void attack(Unit attacker, Unit target) {
		if (!attacker.isAlly(target)) {
			attacking.put(attacker, target);
		}
	}

}
