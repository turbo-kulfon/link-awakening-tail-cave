package com.la.aspects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;

public abstract class LadderCollision implements IAspect {
	private int uniqueID;

	public LadderCollision(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	public abstract void collision(float ladderX, float ladderY, float ladderW, float ladderH, int ladderType);

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.LADDER_COLLISION;
	}
}
