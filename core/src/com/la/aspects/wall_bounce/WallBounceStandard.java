package com.la.aspects.wall_bounce;

import com.engine.spatial.ISpatialComponent;

public class WallBounceStandard implements WallBounceCollisionResolve {
	private ISpatialComponent spatialComponent;

	public WallBounceStandard(ISpatialComponent spatialComponent) {
		this.spatialComponent = spatialComponent;
	}

	@Override
	public void onLeftSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
		spatialComponent.bounceLeft(collidedX);
		spatialComponent.setDeltaX(0);
	}
	@Override
	public void onRightSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
		spatialComponent.bounceRight(collidedX + collidedW);
		spatialComponent.setDeltaX(0);
	}
	@Override
	public void onUpSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
		spatialComponent.bounceUp(collidedY);
		spatialComponent.setDeltaY(0);
	}
	@Override
	public void onDownSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
		spatialComponent.bounceDown(collidedY + collidedH);
		spatialComponent.setDeltaY(0);
	}
}
