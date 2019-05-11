package com.la.game_objects.link.controller.common;

public class BombComponent {
	public interface BombComponentDependency {
		boolean isItemCarried();
		boolean isBombAlreadyPlanted();
		void putBomb();
		void takeBomb();
	}

	private BombComponentDependency dependency;

	public BombComponent(BombComponentDependency dependency) {
		this.dependency = dependency;
	}

	public void buttonPressed() {
		if(dependency.isItemCarried() == false) {
			if(dependency.isBombAlreadyPlanted() == false) {
				dependency.putBomb();
			}
			else {
				dependency.takeBomb();
			}
		}
	}
}
