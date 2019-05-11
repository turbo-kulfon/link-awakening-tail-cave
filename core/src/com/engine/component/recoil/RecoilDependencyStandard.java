package com.engine.component.recoil;

import com.engine.spatial.ISpatialComponent;

public class RecoilDependencyStandard implements RecoilDependency {
	private ISpatialComponent spatialComponent;

	public RecoilDependencyStandard(ISpatialComponent spatialComponent) {
		this.spatialComponent = spatialComponent;
	}

	@Override
	public float getCenterX() {
		return spatialComponent.getCenterX();
	}
	@Override
	public float getCenterY() {
		return spatialComponent.getCenterY();
	}

	@Override
	public void update(float dx, float dy) {
		spatialComponent.setDelta(dx, dy);
	}
}
