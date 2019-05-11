package com.la.game_states;

import com.engine.game_state.IGameState;
import com.engine.game_state.IGameStateSystem;
import com.engine.gfx.ColorDrawComponent;
import com.engine.gfx.GFXSystem;

public class FlashGameState implements IGameState {
	public interface FlashStateCallback {
		void onTransition();
		void onEnd();
	}

	private IGameStateSystem gameStateSystem;
	private ColorDrawComponent drawComponent;
	private FlashStateCallback callback;

	private int counter = 0;
	private int speed = 16;
	private int mode = 0;

	public FlashGameState(
			IGameStateSystem gameStateSystem,
			GFXSystem gfxSystem,
			FlashStateCallback callback) {
		this.gameStateSystem = gameStateSystem;

		drawComponent = gfxSystem.createColorDrawComponent(3);
		drawComponent.setCoordinates(0, 0, 160, 144);
		drawComponent.setColor(1, 1, 1);
		drawComponent.setAlpha(0);

		this.callback = callback;
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onRemove() {
		drawComponent.remove();
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
		if(mode == 0) {
			counter += speed;
			if(counter >= 255) {
				mode = 1;
				counter = 30;
				callback.onTransition();
			}
		}
		else if(mode == 1) {
			counter -= 1;
			if(counter <= 0) {
				counter = 255;
				mode = 2;
			}
		}
		else if(mode == 2) {
			counter -= speed;
			if(counter <= 0) {
				counter = 0;
				gameStateSystem.popState();
				callback.onEnd();
			}
		}
	}

	@Override public void pauseDraw() {}
	@Override
	public void draw() {
		if(mode == 0 || mode == 2) {
			drawComponent.setAlpha(counter/255.0f);
		}
		else {
			drawComponent.setAlpha(1);
		}
	}
}
