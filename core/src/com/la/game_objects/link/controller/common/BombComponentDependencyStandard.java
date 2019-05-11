package com.la.game_objects.link.controller.common;

import java.util.List;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.BombTag;
import com.la.game_objects.link.controller.common.BombComponent.BombComponentDependency;

public abstract class BombComponentDependencyStandard implements BombComponentDependency {
	private ISpatialComponent spatialComponent;
	private CarryComponent carryComponent;
	private SpatialSystem spatialSystem;
	private IAspectSystem aspectSystem;

	public BombComponentDependencyStandard(
			ISpatialComponent spatialComponent,
			CarryComponent carryComponent,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem) {
		this.spatialComponent = spatialComponent;
		this.carryComponent = carryComponent;
		this.spatialSystem = spatialSystem;
		this.aspectSystem = aspectSystem;
	}

	@Override
	public boolean isItemCarried() {
		return carryComponent.isActive();
	}

	@Override
	public boolean isBombAlreadyPlanted() {
		int bombSize = aspectSystem.getAspectSize(AspectType.BOMB_TAG);
		if(bombSize == 0) {
			return false;
		}
		return true;
	}

	public abstract void putBomb();

	@Override
	public void takeBomb() {
		List<Integer> collisions = spatialSystem.getCollided(
			spatialComponent.getX(), spatialComponent.getY(),
			spatialComponent.getW(), spatialComponent.getH());
		for (Integer id : collisions) {
			BombTag bombTag = aspectSystem.getAspect(id, AspectType.BOMB_TAG);
			if(bombTag != null) {
				carryComponent.take(id);
				break;
			}
		}
	}
}
