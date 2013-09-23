package ro7.engine.ai;

public interface BTNode {
	
	/**
	 * Update the current node
	 * @param nanoseconds time since last update
	 * @return status of the node
	 */
	public Status update(float nanoseconds);
	
	/**
	 * Reset the current node
	 */
	public void reset();

}
