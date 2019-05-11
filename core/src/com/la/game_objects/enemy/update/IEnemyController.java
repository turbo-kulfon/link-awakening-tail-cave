package com.la.game_objects.enemy.update;

public interface IEnemyController {
	void update(IObjectController controller);
	void updateController();

	IObjectController getController();

	boolean shouldRemove();

	void remove();
}
