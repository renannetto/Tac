package ro7.engine.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public abstract class AStar {

	protected Graph graph;

	protected AStar(Graph graph) {
		this.graph = graph;
	}

	public List<Node> shortestPath(Node start, Node end) {		
		Map<Node, PathNode> predecessor = new HashMap<Node, PathNode>();
		Set<Node> visited = new HashSet<Node>();
		
		Queue<PathNode> queue = new PriorityQueue<PathNode>();
		queue.add(new PathNode(start, heuristic(start, end)));

		PathNode pathNode = queue.remove();
		visited.add(pathNode.node);
		queue = expandNode(queue, pathNode, predecessor, visited, end);
		while (!pathNode.node.equals(end) && queue.size() > 0) {
			pathNode = queue.remove();	
			visited.add(pathNode.node);
			queue = expandNode(queue, pathNode, predecessor, visited, end);
		}

		if (!pathNode.node.equals(end)) {
			return null;
		}
		return reconstructPath(start, pathNode, predecessor);
	}

	private List<Node> reconstructPath(Node start, PathNode pathNode,
			Map<Node, PathNode> predecessor) {
		List<Node> path = new ArrayList<Node>();
		path.add(0, pathNode.node);

		PathNode pre = predecessor.get(pathNode.node);
		while (!pre.node.equals(start)) {
			path.add(0, pre.node);
			pre = predecessor.get(pre.node);
		}
		path.add(0, start);
		return path;
	}

	private Queue<PathNode> expandNode(Queue<PathNode> queue,
			PathNode pathNode, Map<Node, PathNode> predecessor,
			Set<Node> visited, Node end) {
		Map<Node, Integer> neighbors = pathNode.node.getNeighbors();
		for (Map.Entry<Node, Integer> neighbor : neighbors.entrySet()) {
			Node neighborNode = neighbor.getKey();
			float cost = pathNode.cost + neighbor.getValue() - heuristic(pathNode.node, end);
			PathNode newPathNode = new PathNode(neighborNode, cost
					+ heuristic(neighborNode, end));
			
			if (!visited.contains(newPathNode.node)) {
				queue.add(newPathNode);
			}

			if (newShortestPath(newPathNode, cost, predecessor, end)) {
				predecessor.put(newPathNode.node, pathNode);
			}
		}
		return queue;
	}

	private boolean newShortestPath(PathNode pathNode, float newCost,
			Map<Node, PathNode> predecessor, Node end) {
		PathNode oldPreNode = predecessor.get(pathNode.node);
		if (oldPreNode == null) {
			return true;
		}

		Map<Node, Integer> oldNeighbors = oldPreNode.node.getNeighbors();
		float oldCost = oldPreNode.cost + oldNeighbors.get(pathNode.node) - heuristic(oldPreNode.node, end);
		if (newCost < oldCost) {
			return true;
		}

		return false;
	}

	public abstract float heuristic(Node node, Node end);

	private class PathNode implements Comparable<PathNode> {

		private Node node;
		private float cost;

		public PathNode(Node node, float cost) {
			this.node = node;
			this.cost = cost;
		}

		@Override
		public int compareTo(PathNode o) {
			if (cost < o.cost)
				return -1;
			if (cost > o.cost)
				return 1;
			return 0;
		}

	}

}
