package com.la.game_objects.link.controller.top_down;

import com.engine.util.Coordinate;
import com.engine.util.ICoordinate;
import com.engine.util.ICoordinate.Vector;

public class RecoilTopDownComponent {
	public interface RecoilTopDownDependency {
		float getCenterX();
		float getCenterY();

		void setDelta(float dx, float dy);

		boolean wallCollision();
	}

	private Vector delta;
	private boolean active;
	private int counter;
	private ICoordinate coordinate = new Coordinate();

	private RecoilTopDownDependency dependency;

	public RecoilTopDownComponent(RecoilTopDownDependency dependency) {
		this.dependency = dependency;
	}

	public void hit(float centerX, float centerY, float distance, int time) {
		delta = coordinate.calculateDelta(
				centerX, centerY,
				dependency.getCenterX(), dependency.getCenterY());
		float mod = distance/(float)time;
		delta.x *= mod;
		delta.y *= mod;
		counter = time;
		active = true;
	}
	public void update() {
		if(active == true) {
			counter -= 1;
			if(counter <= 0 || dependency.wallCollision() == true) {
				active = false;
			}
			else {
				dependency.setDelta(delta.x, delta.y);
			}
		}
	}
	public void stop() {
		active = false;
		counter = 0;
	}
	public boolean isActive() {
		return active;
	}
}
