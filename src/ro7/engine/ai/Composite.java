package ro7.engine.ai;

import java.util.ArrayList;
import java.util.List;

public abstract class Composite implements BTNode {
	
	protected BTNode runningNode;
	protected List<BTNode> children;
	
	protected Composite() {
		children = new ArrayList<BTNode>();
	}
	
	public abstract Status update(float nanoseconds);
	
	public void reset() {
		runningNode.reset();
	}
	
	public void addChild(BTNode child) {
		children.add(child);
	}

}
