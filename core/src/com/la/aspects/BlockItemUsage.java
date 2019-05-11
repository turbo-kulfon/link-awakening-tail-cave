package com.la.aspects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;

public abstract class BlockItemUsage implements IAspect {
	private int uniqueID;

	public BlockItemUsage(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	public abstract void block();

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.BLOCK_ITEM_USAGE;
	}
}
