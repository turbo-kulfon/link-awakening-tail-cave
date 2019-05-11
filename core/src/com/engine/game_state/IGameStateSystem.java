package com.engine.game_state;

public interface IGameStateSystem {
	void pushState(IGameState gameState);
	void popState();

	void update();
	void draw();

	void clear();
}
