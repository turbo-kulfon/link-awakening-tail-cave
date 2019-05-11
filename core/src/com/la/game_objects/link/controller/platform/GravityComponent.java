package com.la.game_objects.link.controller.platform;

public class GravityComponent {
	public interface GravityComponentDependency {
		void decreaseDeltaY(float amount);
		void setDeltaY(float dy);
		boolean isOnGround();
		boolean isLevitating();
	}

	private GravityComponentDependency dependency;

	public GravityComponent(GravityComponentDependency dependency) {
		this.dependency = dependency;
	}

	public void update() {
		if(dependency.isLevitating() == false) {
			if(dependency.isOnGround() == false) {
				dependency.decreaseDeltaY(-0.125f);
			}
			else {
				dependency.setDeltaY(1.0f);
			}
		}
	}
}
