package com.la.aspects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;

public abstract class AutoDoor implements IAspect {
	private int uniqueID;

	public AutoDoor(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	public abstract void open();
	public abstract void close();

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.AUTO_DOOR;
	}
}
