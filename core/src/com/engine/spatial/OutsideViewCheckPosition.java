package com.engine.spatial;

import com.engine.spatial.OutsideViewCheck.Position;

public class OutsideViewCheckPosition implements Position {
	private ISpatialComponent spatialComponent;

	public OutsideViewCheckPosition(ISpatialComponent spatialComponent) {
		this.spatialComponent = spatialComponent;
	}

	@Override
	public float getX() {
		return spatialComponent.getX() + spatialComponent.getDeltaX();
	}
	@Override
	public float getY() {
		return spatialComponent.getY() + spatialComponent.getDeltaY();
	}
	@Override
	public float getW() {
		return spatialComponent.getW();
	}
	@Override
	public float getH() {
		return spatialComponent.getH();
	}
}
