package ro7.engine.ai;

import static ro7.engine.ai.Status.FAILURE;
import static ro7.engine.ai.Status.RUNNING;
import static ro7.engine.ai.Status.SUCCESS;

public class Selector extends Composite {

	/* (non-Javadoc)
	 * @see ro7.engine.ai.Composite#update(float)
	 * Update each child until one of them does not fail.
	 * If a new node starts running, resets the previous running node.
	 * Returns fail only if all children fail.
	 */
	@Override
	public Status update(float nanoseconds) {
		for (BTNode child : children) {
			Status status = child.update(nanoseconds);
			if (status == RUNNING) {
				if (runningNode != null && !runningNode.equals(child)) {
					runningNode.reset();
				}
				runningNode = child;
				return RUNNING;
			}
			if (status == SUCCESS) {
				return SUCCESS;
			}
		}
		return FAILURE;
	}

}
