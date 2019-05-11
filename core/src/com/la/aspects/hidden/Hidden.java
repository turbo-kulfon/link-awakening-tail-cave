package com.la.aspects.hidden;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;

public class Hidden implements IAspect {
	private int uniqueID;
	private boolean hidden;

	public Hidden(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	public void hide() {
		hidden = true;
	}
	public void unhide() {
		hidden = false;
	}
	public boolean isHidden() {
		return hidden;
	}

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.HIDDEN;
	}
}
