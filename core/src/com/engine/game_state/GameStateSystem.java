package com.engine.game_state;

import java.util.ArrayList;
import java.util.List;

public class GameStateSystem implements IGameStateSystem {
	private List<IGameState> gameStates = new ArrayList<IGameState>();

	@Override
	public void pushState(IGameState gameState) {
		sendOnPause();
		gameStates.add(gameState);
		gameState.onCreate();
	}
	@Override
	public void popState() {
		if(removeLastState() == true) {
			sendOnResume();
		}
	}

	@Override
	public void update() {
		int size = gameStates.size();
		for(int i = 0; i < size - 1; ++i) {
			gameStates.get(i).pauseUpdate();
		}
		IGameState lastState = getLastElement();
		if(lastState != null) {
			lastState.update();
		}
	}
	@Override
	public void draw() {
		int size = gameStates.size();
		for(int i = 0; i < size - 1; ++i) {
			gameStates.get(i).pauseDraw();
		}
		IGameState lastState = getLastElement();
		if(lastState != null) {
			lastState.draw();
		}

//		int size = gameStates.size();
//		if(size > 0) {
//			for(int i = size - 1; i > 0; --i) {
//				gameStates.get(i).pauseDraw();
//			}
//			gameStates.get(0).draw();
//		}
	}
	@Override
	public void clear() {
		for(int i = 0; i < gameStates.size(); ++i) {
			gameStates.get(i).onRemove();
		}
		gameStates.clear();
	}

	private IGameState getLastElement() {
		int size = gameStates.size();
		if(size > 0) {
			return gameStates.get(size-1);
		}
		return null;
 	}
	private boolean removeLastState() {
		IGameState lastState = getLastElement();
		if(lastState != null) {
			gameStates.remove(lastState);
			lastState.onRemove();
			return true;
		}
		return false;
	}
	private void sendOnResume() {
		IGameState lastState = getLastElement();
		if(lastState != null) {
			lastState.onResume();
		}
	}
	private void sendOnPause() {
		IGameState lastState = getLastElement();
		if(lastState != null) {
			lastState.onPause();
		}		
	}
}
