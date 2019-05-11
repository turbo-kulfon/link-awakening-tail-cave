package com.la.game_objects.link.controller.top_down;

import com.engine.component.shield.ShieldComponent.ShieldComponentDependency;
import com.engine.direction.Direction;
import com.engine.direction.IDirection;
import com.engine.spatial.ISpatialComponent;

public class ShieldTopDownDependency implements ShieldComponentDependency {
	private ISpatialComponent spatialComponent;
	private IDirection direction;

	public ShieldTopDownDependency(
			ISpatialComponent spatialComponent,
			IDirection direction) {
		this.spatialComponent = spatialComponent;
		this.direction = direction;
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
	public Direction getDirection() {
		return direction.getDirection();
	}
}
