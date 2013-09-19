package ro7.game.map;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ro7.engine.GameSpace;
import ro7.engine.util.Graph;
import ro7.engine.util.Node;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class GameMap extends GameSpace {

	private final int SQUARE_SIZE;
	private final int MOVE_STEPS = 8;

	private Vec2i matrixDimensions;

	private Map<Vec2i, Terrain> terrains;
	private Map<Vec2i, Unit> units;

	private Unit selected;

	private Graph mapGraph;
	private Map<Terrain, TacNode> nodes;

	private TacAStar astar;

	private Map<Unit, List<Vec2f>> moving;

	protected GameMap(Vec2f position, Vec2f dimensions, Vec2i matrixDimensions) {
		super(position, dimensions);

		this.matrixDimensions = matrixDimensions;

		SQUARE_SIZE = (int) (dimensions.x / matrixDimensions.x);

		terrains = new HashMap<Vec2i, Terrain>();
		units = new HashMap<Vec2i, Unit>();

		nodes = new HashMap<Terrain, TacNode>();

		moving = new HashMap<Unit, List<Vec2f>>();
	}

	@Override
	public void draw(Graphics2D g, Vec2f min, Vec2f max) {
		Vec2i minMapPosition = new Vec2i(Math.max(
				(int) Math.floor(min.y / SQUARE_SIZE), 0), Math.max(
				(int) Math.floor(min.x / SQUARE_SIZE), 0));
		Vec2i maxMapPosition = new Vec2i(Math.min(
				(int) Math.ceil(max.y / SQUARE_SIZE), getHeight() - 1),
				Math.min((int) Math.ceil(max.x / SQUARE_SIZE), getWidth() - 1));

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
					unit.draw(g);
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

	public void buildGraph() {
		mapGraph = new Graph();

		addNodes();

		addEdges();

		astar = new TacAStar(mapGraph);
	}

	private void addEdges() {
		for (Terrain terrain : terrains.values()) {
			Vec2i mapPosition = terrain.getMapPosition();
			if (isPassable(mapPosition)) {

				connectNeighbor(terrain, mapPosition, new Vec2i(-1, 0));
				connectNeighbor(terrain, mapPosition, new Vec2i(0, -1));
				connectNeighbor(terrain, mapPosition, new Vec2i(1, 0));
				connectNeighbor(terrain, mapPosition, new Vec2i(0, 1));
			}
		}
	}

	private void connectNeighbor(Terrain terrain, Vec2i mapPosition,
			Vec2i neighborPosition) {
		Vec2i newPosition = mapPosition.plus(neighborPosition);
		Terrain neighbor = terrains.get(newPosition);
		if (neighbor != null && isPassable(newPosition)) {
			mapGraph.connect(nodes.get(terrain), nodes.get(neighbor), 1);
		}
	}

	private void addNodes() {
		for (Terrain terrain : terrains.values()) {
			Vec2i mapPosition = terrain.getMapPosition();
			if (isPassable(mapPosition)) {
				TacNode node = new TacNode(terrain);
				mapGraph.addNode(node);
				nodes.put(terrain, node);
			}
		}
	}

	private void addNode(Vec2i mapPosition) {
		Terrain terrain = terrains.get(mapPosition);
		TacNode node = new TacNode(terrain);
		mapGraph.addNode(node);
		nodes.put(terrain, node);

		connectNeighbor(terrain, mapPosition, new Vec2i(-1, 0));
		connectNeighbor(terrain, mapPosition, new Vec2i(0, -1));
		connectNeighbor(terrain, mapPosition, new Vec2i(1, 0));
		connectNeighbor(terrain, mapPosition, new Vec2i(0, 1));
	}

	private void removeNode(Vec2i mapPosition) {
		Terrain terrain = terrains.get(mapPosition);
		TacNode node = nodes.get(terrain);
		mapGraph.removeNode(node);
		nodes.remove(mapPosition);
	}

	public void clicked(Vec2f gamePosition) {
		Vec2i mapPosition = new Vec2i((int) (gamePosition.y / SQUARE_SIZE),
				(int) (gamePosition.x / SQUARE_SIZE));

		if (isPlayerUnit(mapPosition)) {
			Unit unit = units.get(mapPosition);
			unit.select();
			if (selected != null && !selected.equals(unit)) {
				selected.unselect();
			}
			selected = unit;
		} else {
			if (selected != null && isPassable(mapPosition)) {
				getPathTo(mapPosition);
			}
		}
	}

	private void getPathTo(Vec2i mapPosition) {
		Vec2i oldPosition = selected.getMapPosition();

		addNode(oldPosition);

		TacNode startPoint = nodes.get(terrains.get(oldPosition));
		TacNode endPoint = nodes.get(terrains.get(mapPosition));
		List<Node> path = astar.shortestPath(startPoint, endPoint);

		removeNode(oldPosition);

		if (path != null) {
			List<Vec2f> movingPath = new ArrayList<Vec2f>();
			Vec2f previousPoint = startPoint.getPosition();
			path.remove(0);
			for (Node node : path) {
				TacNode tacNode = (TacNode) node;
				Vec2f currentPoint = tacNode.getPosition();
				movingPath = interpolate(previousPoint, currentPoint,
						movingPath);
				previousPoint = currentPoint;
			}
			moving.put(selected, movingPath);
			selected.unselect();
			selected = null;
		}
	}

	private List<Vec2f> interpolate(Vec2f previousPoint, Vec2f currentPoint,
			List<Vec2f> path) {
		for (int i = 1; i <= MOVE_STEPS; i++) {
			path.add(previousPoint.lerpTo(currentPoint, (float) i / MOVE_STEPS));
		}
		return path;
	}

	private boolean isPassable(Vec2i mapPosition) {
		return terrains.get(mapPosition).isPassable() && !isUnit(mapPosition);
	}

	private boolean isUnit(Vec2i mapPosition) {
		return units.get(mapPosition) != null;
	}

	private boolean isPlayerUnit(Vec2i mapPosition) {
		Unit unit = units.get(mapPosition);
		return unit != null && !unit.isComputer();
	}

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
			addNode(oldMapPosition);
			removeNode(newMapPosition);
		}
		return true;
	}

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

}
