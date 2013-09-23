package ro7.engine.ai;

import static ro7.engine.ai.Status.FAILURE;
import static ro7.engine.ai.Status.RUNNING;
import static ro7.engine.ai.Status.SUCCESS;

import java.util.List;

public class Sequence extends Composite {

	/* (non-Javadoc)
	 * @see ro7.engine.ai.Composite#update(float)
	 * Update the children until one of them fail.
	 * Returns SUCCESS only if all children succeed.
	 */
	@Override
	public Status update(float nanoseconds) {
		List<BTNode> subList = children;
		if (runningNode != null) {
			subList = children.subList(children.indexOf(runningNode),
					children.size());
		}
		for (BTNode child : subList) {
			Status status = child.update(nanoseconds);
			if (status == RUNNING) {
				runningNode = child;
				return RUNNING;
			}
			if (status == FAILURE) {
				return FAILURE;
			}
		}

		return SUCCESS;
	}

}
