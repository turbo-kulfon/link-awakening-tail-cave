package com.la.game_objects.link.controller.top_down;

import com.engine.component.sword.SwordComponent.SwordComponentDependency;
import com.engine.direction.Direction;
import com.engine.direction.IDirection;
import com.engine.spatial.ISpatialComponent;

public abstract class SwordTopDownDependency implements SwordComponentDependency {
	private ISpatialComponent spatialComponent;
	private IDirection direction;

	public SwordTopDownDependency(
			ISpatialComponent spatialComponent,
			IDirection direction) {
		this.spatialComponent = spatialComponent;
		this.direction = direction;
	}

	@Override
	public float getX() {
		return spatialComponent.getX();
	}
	@Override
	public float getY() {
		return spatialComponent.getY();
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
	public Direction getOwnerDirection() {
		return direction.getDirection();
	}
}
