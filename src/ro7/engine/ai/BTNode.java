package ro7.engine.ai;

public interface BTNode {
	
	public Status update(float nanoseconds);
	
	public void reset();

}
