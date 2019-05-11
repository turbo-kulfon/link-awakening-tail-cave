package com.engine.component.fall;

import com.engine.util.Coordinate;
import com.engine.util.ICoordinate;
import com.engine.util.ICoordinate.Vector;

public class FallComponent implements IFallComponent {
	public interface FallDependency {
		float getCenterX();
		float getCenterY();

		void update(float dx, float dy, int counter);

		void onFall();
		void onFallEnd();
	}

	private Vector fallDelta;
	private boolean falling;
	private int counter;
	private ICoordinate coordinate;
	private FallDependency dependency;

	public FallComponent(FallDependency dependency) {
		this.dependency = dependency;
		coordinate = new Coordinate();
	}

	@Override
	public void fall(float holeX, float holeY) {
		if(falling == false) {
			fallDelta = coordinate.calculateNonNormalizedDelta(dependency.getCenterX(), dependency.getCenterY(), holeX, holeY);
			counter = 45;
			fallDelta.x /= 45.0f;
			fallDelta.y /= 45.0f;
			dependency.onFall();
		}
	}

	@Override
	public void update() {
		if(counter > 0) {
			counter -= 1;
			if(counter <= 0) {
				dependency.onFallEnd();
				falling = false;
			}
			else {
				dependency.update(fallDelta.x, fallDelta.y, counter);
				falling = true;
			}
		}
	}
	@Override
	public void stop() {
		falling = false;
		counter = 0;
	}
	@Override
	public boolean isFalling() {
		return falling;
	}
}
