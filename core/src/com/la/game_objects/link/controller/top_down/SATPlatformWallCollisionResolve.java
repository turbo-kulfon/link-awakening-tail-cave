package com.la.game_objects.link.controller.top_down;

import com.engine.spatial.ISpatialComponent;
import com.la.aspects.wall_bounce.WallBounceStandard;

public class SATPlatformWallCollisionResolve extends WallBounceStandard {
	public interface PlatformWallCollisionDependency {
		void onGroundColision();
		void onCeilCollision();
	}

	private PlatformWallCollisionDependency dependency;

	public SATPlatformWallCollisionResolve(
			ISpatialComponent spatialComponent,
			PlatformWallCollisionDependency dependency) {
		super(spatialComponent);
		this.dependency = dependency;
	}

	@Override
	public void onUpSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
		super.onUpSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
		dependency.onCeilCollision();
	}
	@Override
	public void onDownSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
		super.onDownSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
		dependency.onGroundColision();
	}
}
