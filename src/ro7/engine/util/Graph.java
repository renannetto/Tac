package ro7.engine.util;

import java.util.HashSet;
import java.util.Set;

public class Graph {
	
	protected Set<Node> nodes;
	
	public Graph() {
		nodes = new HashSet<Node>();
	}
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	public void removeNode(Node node) {
		node.delete();
		nodes.remove(node);
	}
	
	public void connect(Node nodeA, Node nodeB, int cost) {
		nodeA.connect(nodeB, cost);
		nodeB.connect(nodeA, cost);
	}
	
	public void disconnect(Node nodeA, Node nodeB) {
		nodeA.disconnect(nodeB);
		nodeB.disconnect(nodeA);
	}

}
