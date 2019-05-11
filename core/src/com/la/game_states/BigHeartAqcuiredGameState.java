package com.la.game_states;

import com.engine.game_state.IGameState;
import com.engine.game_state.IGameStateSystem;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.sound.SoundSystem;
import com.la.game_objects.link.ILinkData;

public class BigHeartAqcuiredGameState implements IGameState {
	public interface BigHeartAqcuiredGameStateCallback {
		void onEnd();
	}

	private SoundSystem soundSystem;
	private IGameStateSystem gameStateSystem;
	private int duration;
	private BigHeartAqcuiredGameStateCallback callback;
	private TextureDrawComponent bigHeartDrawComponent;
	private ILinkData linkData;

	public BigHeartAqcuiredGameState(
			IGameStateSystem gameStateSystem,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			ILinkData linkData,
			int duration,
			BigHeartAqcuiredGameStateCallback callback
			) {
		this.soundSystem = soundSystem;
		this.gameStateSystem = gameStateSystem;
		this.linkData = linkData;
		this.duration = duration;
		this.callback = callback;

		bigHeartDrawComponent = gfxSystem.createTextureDrawComponent(0);
		bigHeartDrawComponent.setTexture(118, 90, 14, 12);
		bigHeartDrawComponent.setSize(14, 12);
	}

	@Override
	public void onCreate() {
	}
	@Override
	public void onRemove() {
		bigHeartDrawComponent.remove();
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
		duration -= 1;
		if(duration < 0) {
			gameStateSystem.popState();
			callback.onEnd();
			soundSystem.nightmareDefeated();
		}
		linkData.setGetBigItem();
	}

	@Override
	public void pauseDraw() {
	}
	@Override
	public void draw() {
		bigHeartDrawComponent.setPosition(linkData.getCenterX() - 7, linkData.getY() - 18);
	}
}
