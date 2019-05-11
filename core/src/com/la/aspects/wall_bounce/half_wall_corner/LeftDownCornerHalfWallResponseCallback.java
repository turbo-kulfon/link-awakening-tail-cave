package com.la.aspects.wall_bounce.half_wall_corner;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.core.ResponseCallback;
import com.la.aspects.wall_bounce.WallBounce;

public class LeftDownCornerHalfWallResponseCallback implements ResponseCallback {
	private IAspectSystem aspectSystem;
	private ISpatialComponent spatialComponent;

	public LeftDownCornerHalfWallResponseCallback(
			ISpatialComponent spatialComponent,
		IAspectSystem aspectSystem) {
		this.spatialComponent = spatialComponent;
		this.aspectSystem = aspectSystem;
	}

	@Override
	public void response(int collidedID) {
		WallBounce wallBounce = aspectSystem.getAspect(collidedID, AspectType.WALL_BOUNCE);
		if(wallBounce != null) {
			wallBounce.subPartBounce(
				spatialComponent.getX(), spatialComponent.getY() + 8,
				16, 8);
			wallBounce.subPartBounce(
				spatialComponent.getX(), spatialComponent.getY(),
				8, 16);
		}
	}
}
