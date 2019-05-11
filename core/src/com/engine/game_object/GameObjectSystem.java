package com.engine.game_object;

import java.util.ArrayList;
import java.util.List;

public class GameObjectSystem implements IGameObjectSystem {
	private List<IGameObject> gameObjects = new ArrayList<IGameObject>();
	private List<IGameObject> newGameObjects = new ArrayList<IGameObject>();

	@Override
	public void add(IGameObject gameObject) {
		if(newGameObjects.contains(gameObject) == false) {
			newGameObjects.add(gameObject);
		}
	}
	@Override
	public void remove(IGameObject gameObject) {
		gameObjects.remove(gameObject);
	}

	@Override
	public void update() {
		for (IGameObject gameObject : gameObjects) {
			if(gameObject.shouldRemove() == false) {
				gameObject.update();
			}
		}
	}
	@Override
	public void draw() {
		for (IGameObject gameObject : gameObjects) {
			if(gameObject.shouldRemove() == false) {
				gameObject.draw();
			}
		}
	}
	@Override
	public void clean() {
		for(int i = 0; i < gameObjects.size(); ++i) {
			if(gameObjects.get(i).shouldRemove() == true) {
				gameObjects.get(i).onRemove();
				gameObjects.remove(i--);
			}
		}
		if(newGameObjects.size() > 0) {
			gameObjects.addAll(newGameObjects);
			newGameObjects.clear();
		}
	}
}
