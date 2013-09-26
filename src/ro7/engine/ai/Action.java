package ro7.engine.ai;

public abstract class Action implements BTNode {
	
	/* (non-Javadoc)
	 * @see ro7.engine.ai.BTNode#update(float)
	 * Run the node action
	 */
	@Override
	public Status update(float nanoseconds) {
		return act(nanoseconds);
	}
	
	public abstract Status act(float nanoseconds);

}
