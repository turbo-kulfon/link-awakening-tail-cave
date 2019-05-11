package com.la.aspects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;

public abstract class MagicPowderCollision implements IAspect {
	private int uniqueID;

	public MagicPowderCollision(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	public abstract void collision(float powderX, float powderY, float powderW, float powderH, float ownerCX, float ownerCY);

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.MAGIC_POWDER_HIT;
	}
}
