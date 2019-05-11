package com.engine.game_state;

public interface IGameState {
	void onCreate();
	void onRemove();
	void onPause();
	void onResume();

	void pauseUpdate();
	void update();
	void pauseDraw();
	void draw();
}
