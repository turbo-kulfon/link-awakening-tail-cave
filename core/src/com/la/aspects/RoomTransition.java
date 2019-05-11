package com.la.aspects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;
import com.engine.spatial.ISpatialComponent;

public class RoomTransition implements IAspect {
	private ISpatialComponent spatialComponent;
	private boolean specMove;

	private int uniqueID;

	public RoomTransition(
			int uniqueID,
			boolean specMove,
			ISpatialComponent spatialComponent) {
		this.uniqueID = uniqueID;
		this.specMove = specMove;
		this.spatialComponent = spatialComponent;
	}

	public boolean isSpecialMove() {
		return specMove;
	}
	public void move(float dx, float dy) {
		spatialComponent.move(dx, dy);
	}

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.ROOM_TRANSITION;
	}
}
