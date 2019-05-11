package com.la.game_objects.link;

import com.engine.component.recoil.IRecoil;
import com.engine.spatial.IOutsideViewCheck.OutsideViewCheckCallback;
import com.engine.spatial.ISpatialComponent;

public class LinkOutsideCheckView implements OutsideViewCheckCallback {
	private ISpatialComponent spatialComponent;
	private IRecoil recoil;

	public LinkOutsideCheckView(
			ISpatialComponent spatialComponent,
			IRecoil recoil) {
		this.spatialComponent = spatialComponent;
		this.recoil = recoil;
	}

	@Override
	public void outsideLeft() {
		if(recoil.isActive() == true) {
			spatialComponent.setX(0);
			spatialComponent.setDeltaX(0);
		}
	}
	@Override
	public void outsideRight() {
		if(recoil.isActive() == true) {
			spatialComponent.setX(160 - spatialComponent.getW());
			spatialComponent.setDeltaX(0);
		}
	}
	@Override
	public void outsideUp() {
		if(recoil.isActive() == true) {
			spatialComponent.setY(0);
			spatialComponent.setDeltaY(0);
		}
	}
	@Override
	public void outsideDown() {
		if(recoil.isActive() == true) {
			spatialComponent.setY(128 - spatialComponent.getH());
			spatialComponent.setDeltaY(0);
		}
	}
}
