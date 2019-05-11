package com.la.aspects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;
import com.engine.direction.Direction;

public abstract class LinkInteraction implements IAspect {
	private int uniqueID;

	public LinkInteraction(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	public abstract boolean interact(float linkX, float linkY, float linkW, float linkH, Direction direction);

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.LINK_INTERACT;
	}
}
