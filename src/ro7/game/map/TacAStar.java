package ro7.game.map;

import ro7.engine.ai.AStar;
import ro7.engine.util.Graph;
import ro7.engine.util.Node;

public class TacAStar extends AStar {

	protected TacAStar(Graph graph) {
		super(graph);
	}

	@Override
	public float heuristic(Node node, Node end) {
		TacNode tacNode = (TacNode) node;
		TacNode endTacNode = (TacNode) end;
		
		return tacNode.distance(endTacNode);
	}

}
