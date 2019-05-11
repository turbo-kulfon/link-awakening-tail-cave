package com.la.game_objects.link.controller.platform;

public class BounceComponent {
	public interface BounceComponentDependency {
		boolean onGround();
		void bounce(float delta);
	}

	private BounceComponentDependency dependency;
	private float bounceFactor;

	public BounceComponent(BounceComponentDependency dependency) {
		this.dependency = dependency;
	}

	public void reset(float bounceFactor) {
		this.bounceFactor = bounceFactor;
	}
	public void update() {
		if(dependency.onGround() == true) {
			bounceFactor /= 1.5f;
			if(bounceFactor >= 0.5f) {
				dependency.bounce(bounceFactor);
			}
		}
	}
}
