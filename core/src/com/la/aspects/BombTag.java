package com.la.aspects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;

public class BombTag implements IAspect {
	private int uniqueID;

	public BombTag(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.BOMB_TAG;
	}
}
