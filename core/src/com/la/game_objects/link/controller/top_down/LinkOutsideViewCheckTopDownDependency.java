package com.la.game_objects.link.controller.top_down;

import com.engine.spatial.ISpatialComponent;
import com.la.game_objects.link.controller.common.LinkOutsideViewCheck.LinkOutsideViewCheckDependency;

public abstract class LinkOutsideViewCheckTopDownDependency implements LinkOutsideViewCheckDependency {
	private ISpatialComponent spatialComponent;

	public LinkOutsideViewCheckTopDownDependency(
			ISpatialComponent spatialComponent) {
		this.spatialComponent = spatialComponent;
	}

	@Override
	public void bounceLeft() {
		spatialComponent.setX(0);
		spatialComponent.setDeltaX(0);
	}
	@Override
	public void bounceRight() {
		spatialComponent.setX(160 - spatialComponent.getW());
		spatialComponent.setDeltaX(0);
	}
	@Override
	public void bounceUp() {
		spatialComponent.setY(0);
		spatialComponent.setDeltaY(0);
	}
	@Override
	public void bounceDown() {
		spatialComponent.setY(128 - spatialComponent.getH());
		spatialComponent.setDeltaY(0);
	}
}
