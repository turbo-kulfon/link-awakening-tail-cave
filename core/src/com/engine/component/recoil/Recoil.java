package com.engine.component.recoil;

import com.engine.util.Coordinate;
import com.engine.util.ICoordinate;
import com.engine.util.ICoordinate.Vector;

public class Recoil implements IRecoil {
	private Vector delta;
	private int counter;
	private boolean active;
	private ICoordinate coordinate;
	private RecoilDependency dependency;

	public Recoil(RecoilDependency dependency) {
		this.dependency = dependency;
		coordinate = new Coordinate();
	}

	@Override
	public boolean hit(float x, float y, float distance, int frameTime) {
		if(counter <= 0) {
			counter = frameTime;
			delta = coordinate.calculateDelta(
					x, y,
					dependency.getCenterX(), dependency.getCenterY());
			float mod = distance/(float)frameTime;
			delta.x *= mod;
			delta.y *= mod;
			return true;
		}
		return false;
	}

	@Override
	public void update() {
		if(counter > 0) {
			counter -= 1;
			dependency.update(delta.x, delta.y);
			active = true;
		}
		else {
			active = false;
		}
	}
	@Override
	public void stop() {
		counter = 0;
		active = false;
	}

	@Override
	public boolean isActive() {
		return active;
	}
}
