package ro7.engine.ai;

import static ro7.engine.ai.Status.FAILURE;
import static ro7.engine.ai.Status.SUCCESS;

public abstract class Condition implements BTNode {

	/* (non-Javadoc)
	 * @see ro7.engine.ai.BTNode#update(float)
	 * Returns SUCCESS when the condition is true
	 */
	@Override
	public Status update(float nanoseconds) {
		if (checkCondition(nanoseconds)) {
			return SUCCESS;
		} else {
			return FAILURE;
		}
	}
	
	public abstract boolean checkCondition(float nanoseconds);

}
