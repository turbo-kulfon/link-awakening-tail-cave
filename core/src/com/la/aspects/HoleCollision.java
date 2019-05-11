package com.la.aspects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;

public abstract class HoleCollision implements IAspect {
	public enum HoleCollisionResult {
		NONE,
		LINK,
		OTHER
	}

	private int uniqueID;

	public HoleCollision(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	public abstract HoleCollisionResult collision(float holeX, float holeY, float holeW, float holeH, boolean restoreLastPosition);

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.HOLE_COLLISION;
	}
}
