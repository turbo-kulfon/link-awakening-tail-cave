package com.la.game_states;

import com.engine.game_state.IGameState;
import com.engine.game_state.IGameStateSystem;
import com.la.game_objects.link.ILinkData;

public class TeleportState implements IGameState {
	public interface TeleportStateCallback {
		void onEnd();
	}

	private IGameStateSystem gameStateSystem;
	private ILinkData linkData;
	private int counter = 60;
	private TeleportStateCallback callback;

	public TeleportState(
			IGameStateSystem gameStateSystem,
			ILinkData linkData,
			TeleportStateCallback callback) {
		this.gameStateSystem = gameStateSystem;
		this.linkData = linkData;
		this.callback = callback;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onRemove() {
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
	}

	@Override
	public void pauseUpdate() {
	}
	@Override
	public void update() {
		if(counter > 0) {
			counter -= 1;
			if(counter % 8 == 0) {
				linkData.changeDirectionToRight();
			}
		}
		else {
			gameStateSystem.popState();
			callback.onEnd();
		}
	}

	@Override
	public void pauseDraw() {
	}
	@Override
	public void draw() {
	}
}
