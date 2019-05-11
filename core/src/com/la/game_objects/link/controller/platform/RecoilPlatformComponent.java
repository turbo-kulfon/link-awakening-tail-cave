package com.la.game_objects.link.controller.platform;

public class RecoilPlatformComponent {
	public interface RecoilPlatformDependency {
		float getCenterX();
		float getCenterY();

		void setDeltaX(float dx);
		void setDeltaY(float dy);

		boolean isOnGround();
		void unsetOnGround();

		boolean wallCollision();
	}

	private float dx;
	private int counter;
	private boolean active, yUpdate, bounce;

	private RecoilPlatformDependency dependency;

	public RecoilPlatformComponent(RecoilPlatformDependency dependency) {
		this.dependency = dependency;
	}

	public void hit(float centerX, float centerY) {
		if(dependency.getCenterX() <= centerX) {
			dx = -1;
		}
		else {
			dx = 1;
		}
		active = true;
		yUpdate = true;
		bounce = false;
	}
	public void hitNoBounce(float centerX, float centerY, float distance, int time) {
		if(dependency.getCenterX() <= centerX) {
			dx = -1 * (distance/(float)time);
		}
		else {
			dx = 1 * (distance/(float)time);
		}
		counter = time;
		active = true;
		bounce = true;
	}
	public void update() {
		if(active == true) {
			if(bounce == false) {
				dependency.setDeltaX(dx);
				if(yUpdate == true) {
					dependency.setDeltaY(-1);
					dependency.unsetOnGround();
					yUpdate = false;
				}
				if(dependency.isOnGround() == true || dependency.wallCollision() == true) {
					active = false;
				}
			}
			else {
				counter -= 1;
				if(counter > 0 && dependency.wallCollision() == false) {
					dependency.setDeltaX(dx);
				}
				else {
					active = false;
				}
			}
		}
	}
	public void stop() {
		active = false;
	}
	public boolean isActive() {
		return active;
	}
}
