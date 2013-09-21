package ro7.engine.ai;

import static ro7.engine.ai.Status.FAILURE;
import static ro7.engine.ai.Status.RUNNING;
import static ro7.engine.ai.Status.SUCCESS;

public class Selector extends Composite {

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
