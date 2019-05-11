package com.engine.game_object;

public interface IGameObject {
	void update();
	void draw();

	void setToRemove();
	boolean shouldRemove();

	void onRemove();
}
