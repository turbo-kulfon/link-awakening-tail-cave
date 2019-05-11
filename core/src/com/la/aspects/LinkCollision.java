package com.la.aspects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;

public abstract class LinkCollision implements IAspect {
	private int uniqueID;

	public LinkCollision(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	public abstract void collision(float linkX, float linkY, float linkW, float linkH, float linkHeight, boolean blocked);

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.LINK_COLLISION;
	}
}
