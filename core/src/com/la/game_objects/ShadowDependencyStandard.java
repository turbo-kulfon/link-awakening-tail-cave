package com.la.game_objects;

import com.engine.spatial.ISpatialComponent;
import com.la.game_objects.Shadow.ShadowDependency;

public class ShadowDependencyStandard implements ShadowDependency {
	private ISpatialComponent spatialComponent;
	
	public ShadowDependencyStandard(ISpatialComponent spatialComponent) {
		this.spatialComponent = spatialComponent;
	}

	@Override
	public float getCenterX() {
		return spatialComponent.getCenterX();
	}
	@Override
	public float getY() {
		return spatialComponent.getY();
	}
	@Override
	public float getH() {
		return spatialComponent.getH();
	}
}
