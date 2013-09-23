package ro7.engine.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Node {
	
	private Map<Node, Integer> neighbors;
	
	public Node() {
		neighbors = new HashMap<Node, Integer>();
	}
	
	/**
	 * Connect to another node
	 * @param node node to be connected
	 * @param cost cost of the edge
	 */
	public void connect(Node node, int cost) {
		neighbors.put(node, cost);
	}
	
	/**
	 * Disconnect from another node
	 * @param node node to be disconnected
	 */
	public void disconnect(Node node) {
		neighbors.remove(node);
	}
	
	/**
	 * Disconnect this node from all its neighbors
	 */
	public void delete() {
		Set<Node> neighborNodes = neighbors.keySet();
		for (Node node : neighborNodes) {
			node.disconnect(this);
		}
	}
	
	public Map<Node, Integer> getNeighbors() {
		return neighbors;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!obj.getClass().equals(this.getClass()))
			return false;
		
		Node other = (Node) obj;
		
		for (Map.Entry<Node, Integer> entrySet : neighbors.entrySet()) {
			Node node = entrySet.getKey();
			Integer cost = entrySet.getValue();
			
			Integer otherCost = other.neighbors.get(node);
			if (otherCost == null || !otherCost.equals(cost)) {
				return false;
			}
		}
		return true;
	}

}
