package com.la.game_objects.enemy;

import com.la.game_objects.Shadow.ShadowDependency;
import com.la.game_objects.enemy.update.IObjectController;

public class ShadowDependencyEnemy implements ShadowDependency {
	private IObjectController objectController;

	public ShadowDependencyEnemy(IObjectController objectController) {
		this.objectController = objectController;
	}

	@Override
	public float getCenterX() {
		return objectController.getCenterX();
	}
	@Override
	public float getY() {
		return objectController.getY();
	}
	@Override
	public float getH() {
		return objectController.getH();
	}
}
