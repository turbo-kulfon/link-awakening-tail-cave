package com.la.aspects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;

public abstract class SlowDown implements IAspect {
	private int uniqueID;

	public SlowDown(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	public abstract void slowDown();

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.SLOW_DOWN;
	}
}
