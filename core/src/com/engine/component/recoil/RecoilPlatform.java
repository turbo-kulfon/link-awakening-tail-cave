package com.engine.component.recoil;

import com.engine.util.ICoordinate.Vector;

public class RecoilPlatform implements IRecoil {
	public interface RecoilDependency {
		float getCenterX();
		float getCenterY();

		void groundUnset();
		boolean isOnGround();

		void update(float dx, float dy);
	}

	private RecoilDependency dependency;

	private int counter;
	private boolean yDeltaUpdate;
	private Vector delta = new Vector(0, 0);

	public RecoilPlatform(RecoilDependency dependency) {
		this.dependency = dependency;
	}

	@Override
	public boolean hit(float x, float y, float distance, int frameTime) {
		if(counter <= 0) {
			if(dependency.getCenterX() <= x) {
				delta.x = -1;
			}
			else {
				delta.x = 1;
			}
			delta.y = -1;
			yDeltaUpdate = true;
			counter = 1000000;
			return true;
		}
		return false;
	}
	@Override
	public void update() {
		if(counter > 0) {
			if(yDeltaUpdate == true) {
				dependency.groundUnset();
			}
			if(dependency.isOnGround() == true) {
				stop();
			}
			else {
				dependency.update(delta.x, yDeltaUpdate == true ? delta.y : 0);
			}
			yDeltaUpdate = false;
		}
	}
	@Override
	public void stop() {
		counter = 0;
	}
	@Override
	public boolean isActive() {
		return counter > 0;
	}
}
