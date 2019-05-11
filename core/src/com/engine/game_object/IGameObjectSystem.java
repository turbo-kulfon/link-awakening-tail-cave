package com.engine.game_object;

public interface IGameObjectSystem {
	void add(IGameObject gameObject);
	void remove(IGameObject gameObject);

	void update();
	void draw();
	void clean();
}
