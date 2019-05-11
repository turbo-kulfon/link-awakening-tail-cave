package com.la.aspects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;

public abstract class BombHit implements IAspect {
	private int uniqueID;

	public BombHit(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	public abstract void hit(float bombCenterX, float bombCenterY, boolean playerIsOwner);

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.BOMB_HIT;
	}
}
