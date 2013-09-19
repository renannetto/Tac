package ro7.game.map;

import ro7.engine.util.Node;
import cs195n.Vec2f;
import cs195n.Vec2i;

public class TacNode extends Node {
	
	private Terrain terrain;

	public TacNode(Terrain terrain) {
		super();
		this.terrain = terrain;
	}
	
	@Override
	public String toString() {
		Vec2i mapPosition = terrain.getMapPosition();
		return "(" + mapPosition.x + ", " + mapPosition.y + ")";
	}

	public Vec2f getPosition() {
		return terrain.getPosition();
	}

	public float distance(TacNode endTacNode) {
		Vec2i startPosition = this.terrain.getMapPosition();
		Vec2i endPosition = endTacNode.terrain.getMapPosition();
		
		int xDistance = startPosition.x-endPosition.x;
		int yDistance = startPosition.y - endPosition.y;
		
		return (float)Math.sqrt(xDistance*xDistance + yDistance*yDistance);
	}

}
