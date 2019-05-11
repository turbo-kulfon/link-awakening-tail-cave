package com.la.aspects.wall_bounce;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.core.ResponseCallback;

public class WallBounceResponseCallback implements ResponseCallback {
	private IAspectSystem aspectSystem;
	private ISpatialComponent spatialComponent;
	private int wallDirection = -1;

	public WallBounceResponseCallback(
			IAspectSystem aspectSystem,
			ISpatialComponent spatialComponent) {
		this.aspectSystem = aspectSystem;
		this.spatialComponent = spatialComponent;
	}
	public WallBounceResponseCallback(
			IAspectSystem aspectSystem,
			ISpatialComponent spatialComponent,
			int wallDirection) {
		this.aspectSystem = aspectSystem;
		this.spatialComponent = spatialComponent;
		this.wallDirection = wallDirection;
	}

	@Override
	public void response(int collidedID) {
		WallBounce wallBounce = aspectSystem.getAspect(collidedID, AspectType.WALL_BOUNCE);
		if(wallBounce != null) {
			wallBounce.bounce(
				spatialComponent.getX(), spatialComponent.getY(),
				spatialComponent.getW(), spatialComponent.getH(),
				wallDirection);
		}
	}
}
