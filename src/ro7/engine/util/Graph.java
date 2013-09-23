package ro7.engine.util;

import java.util.HashSet;
import java.util.Set;

public class Graph {
	
	protected Set<Node> nodes;
	
	public Graph() {
		nodes = new HashSet<Node>();
	}
	
	/**
	 * Add a new node to the graph
	 * @param node node to be added
	 */
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	/**
	 * Remove a node from the graph
	 * @param node node to be removed
	 */
	public void removeNode(Node node) {
		node.delete();
		nodes.remove(node);
	}
	
	/**
	 * Connect to nodes on the graph
	 * @param nodeA first node to be connected
	 * @param nodeB second node to be connected
	 * @param cost cost of the edge between nodeA and nodeB
	 */
	public void connect(Node nodeA, Node nodeB, int cost) {
		nodeA.connect(nodeB, cost);
		nodeB.connect(nodeA, cost);
	}
	
	/**
	 * Disconnect to nodes on the graph
	 * @param nodeA first node to be disconnected
	 * @param nodeB second node to be disconnected
	 */
	public void disconnect(Node nodeA, Node nodeB) {
		nodeA.disconnect(nodeB);
		nodeB.disconnect(nodeA);
	}

}
