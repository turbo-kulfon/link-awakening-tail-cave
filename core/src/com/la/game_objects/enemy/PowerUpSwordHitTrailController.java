package com.la.game_objects.enemy;

import com.la.factory.IRoomFactory;

public class PowerUpSwordHitTrailController {
	public interface PowerUpSwordHitTrailControllerDependency {
		float getCenterX();
		float getCenterY();

		boolean isInRecoilMode();
	}

	private IRoomFactory roomFactory;
	private PowerUpSwordHitTrailControllerDependency dependency;

	private int counter;
	private boolean active;

	public PowerUpSwordHitTrailController(
			IRoomFactory roomFactory,
			PowerUpSwordHitTrailControllerDependency dependency) {
		this.roomFactory = roomFactory;
		this.dependency = dependency;
	}

	public void setActive(boolean active) {
		this.active = active;
		if(active == true) {
			roomFactory.createPoof(
					(int)dependency.getCenterX(),
					(int)dependency.getCenterY());
		}
	}
	public void update() {
		if(active == true && dependency.isInRecoilMode() == true) {
			counter += 1;
			if(counter >= 6) {
				counter = 0;
				roomFactory.createPoof(
					(int)dependency.getCenterX(),
					(int)dependency.getCenterY());
			}
		}
	}
}
