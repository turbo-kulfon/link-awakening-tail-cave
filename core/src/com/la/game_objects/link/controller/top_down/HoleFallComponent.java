package com.la.game_objects.link.controller.top_down;

import com.engine.util.Coordinate;
import com.engine.util.ICoordinate;
import com.engine.util.ICoordinate.Vector;

public class HoleFallComponent {
	public interface HoleFallDependency {
		float getCenterX();
		float getCenterY();

		void setDelta(float dx, float dy);
		void onFall();
		void onFallEnd();
	}

	private Vector delta;
	private ICoordinate coordinate = new Coordinate();
	private int counter;
	private boolean active;

	private HoleFallDependency dependency;

	public HoleFallComponent(HoleFallDependency dependency) {
		this.dependency = dependency;
	}

	public boolean fall(float holeX, float holeY) {
		if(active == false) {
			delta = coordinate.calculateNonNormalizedDelta(
				dependency.getCenterX(), dependency.getCenterY(),
				holeX, holeY);
			counter = 60;
			delta.x /= 60.0f;
			delta.y /= 60.0f;
			active = true;
			dependency.onFall();
			return true;
		}
		return false;
	}
	public void update() {
		if(active == true) {
			counter -= 1;
			if(counter <= 0) {
				active = false;
				dependency.onFallEnd();
			}
			else {
				dependency.setDelta(delta.x, delta.y);
			}
		}
	}
	public int getCounter() {
		return counter;
	}
	public boolean isActive() {
		return active;
	}
}
