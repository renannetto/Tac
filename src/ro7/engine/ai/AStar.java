package ro7.engine.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import ro7.engine.util.Graph;
import ro7.engine.util.Node;

public abstract class AStar {

	protected Graph graph;

	protected AStar(Graph graph) {
		this.graph = graph;
	}

	public List<Node> shortestPath(Node start, Node end) {
		Map<Node, Node> predecessor = new HashMap<Node, Node>();
		Set<Node> visited = new HashSet<Node>();

		Map<Node, Float> nodesCost = new HashMap<Node, Float>();

		Queue<PathNode> queue = new PriorityQueue<PathNode>();
		queue.add(new PathNode(start, heuristic(start, end)));

		PathNode pathNode = queue.remove();
		visited.add(pathNode.node);
		queue = expandNode(queue, pathNode, predecessor, visited, nodesCost,
				end);
		while (!pathNode.node.equals(end) && queue.size() > 0) {
			pathNode = queue.remove();
			visited.add(pathNode.node);
			queue = expandNode(queue, pathNode, predecessor, visited,
					nodesCost, end);
		}

		if (!pathNode.node.equals(end)) {
			return null;
		}
		return reconstructPath(start, pathNode, predecessor);
	}

	private List<Node> reconstructPath(Node start, PathNode pathNode,
			Map<Node, Node> predecessor) {
		List<Node> path = new ArrayList<Node>();
		path.add(0, pathNode.node);

		Node pre = predecessor.get(pathNode.node);
		while (!pre.equals(start)) {
			path.add(0, pre);
			pre = predecessor.get(pre);
		}
		path.add(0, start);
		return path;
	}

	private Queue<PathNode> expandNode(Queue<PathNode> queue,
			PathNode pathNode, Map<Node, Node> predecessor,
			Set<Node> visited, Map<Node, Float> nodesCost, Node end) {
		Map<Node, Integer> neighbors = pathNode.node.getNeighbors();
		for (Map.Entry<Node, Integer> neighbor : neighbors.entrySet()) {
			Node neighborNode = neighbor.getKey();
			float cost = pathNode.cost + neighbor.getValue()
					- heuristic(pathNode.node, end);
			PathNode newPathNode = new PathNode(neighborNode, cost
					+ heuristic(neighborNode, end));

			if (!visited.contains(newPathNode.node)) {
				Float nodeCost = nodesCost.get(newPathNode.node);
				if (nodeCost == null) {
					queue.add(newPathNode);
					predecessor.put(newPathNode.node, pathNode.node);
					nodesCost.put(newPathNode.node, cost);
				} else {
					if (cost <= nodeCost) {
						queue.remove(newPathNode);
						queue.add(newPathNode);
						nodesCost.put(newPathNode.node, cost);
						predecessor.put(newPathNode.node, pathNode.node);
					}
				}
			}
		}
		return queue;
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

		@Override
		public boolean equals(Object obj) {
			if (!obj.getClass().equals(this.getClass())) {
				return false;
			}
			PathNode other = (PathNode) obj;
			return this.node.equals(other.node);
		}

	}

}
