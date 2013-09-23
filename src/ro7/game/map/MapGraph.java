package ro7.game.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs195n.Vec2i;
import ro7.engine.util.Graph;
import ro7.engine.util.Node;
import ro7.game.model.Terrain;
import ro7.game.model.Unit;

public class MapGraph extends Graph {
	
	private Map<Vec2i, Terrain> terrains;
	private Map<Vec2i, Unit> units;
	
	private Map<Terrain, TacNode> mapNodes;
	
	private TacAStar astar;
	
	public MapGraph(Map<Vec2i, Terrain> terrains, Map<Vec2i, Unit> units) {
		this.terrains = terrains;
		this.units = units;
		this.mapNodes = new HashMap<Terrain, TacNode>();
		
		addNodes();
		addEdges();
		
		this.astar = new TacAStar(this);
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
			connect(mapNodes.get(terrain), mapNodes.get(neighbor), 1);
		}
	}

	private void addNodes() {
		for (Terrain terrain : terrains.values()) {
			Vec2i mapPosition = terrain.getMapPosition();
			if (isPassable(mapPosition)) {
				TacNode node = new TacNode(terrain);
				addNode(node);
				mapNodes.put(terrain, node);
			}
		}
	}

	public void addNode(Vec2i mapPosition) {
		Terrain terrain = terrains.get(mapPosition);
		TacNode node = new TacNode(terrain);
		addNode(node);
		mapNodes.put(terrain, node);

		connectNeighbor(terrain, mapPosition, new Vec2i(-1, 0));
		connectNeighbor(terrain, mapPosition, new Vec2i(0, -1));
		connectNeighbor(terrain, mapPosition, new Vec2i(1, 0));
		connectNeighbor(terrain, mapPosition, new Vec2i(0, 1));
	}

	public void removeNode(Vec2i mapPosition) {
		Terrain terrain = terrains.get(mapPosition);
		TacNode node = mapNodes.get(terrain);
		removeNode(node);
		mapNodes.remove(mapPosition);
	}
	
	private boolean isPassable(Vec2i mapPosition) {
		return terrains.get(mapPosition).isPassable() && !isUnit(mapPosition);
	}
	
	private boolean isUnit(Vec2i mapPosition) {
		return units.get(mapPosition) != null;
	}

	public List<Node> shortestPath(Vec2i startPosition, Vec2i endPosition) {
		TacNode startPoint = mapNodes.get(terrains.get(startPosition));
		TacNode endPoint = mapNodes.get(terrains.get(endPosition));
		return astar.shortestPath(startPoint, endPoint);
	}

}
