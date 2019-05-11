package com.la.game_states;

import com.engine.IInputPort;
import com.engine.ISystemPort;
import com.engine.game_state.IGameState;
import com.engine.game_state.IGameStateSystem;
import com.engine.gfx.ColorDrawComponent;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.sound.SoundSystem;
import com.la.factory.IGameStateFactory;
import com.la.game_objects.link.ILinkData;

public class GameOverGameState implements IGameState {
	private enum GameOverState {
		LINK_DIE,
		GAME_OVER_SCREEN
	}

	private SoundSystem soundSystem;
	private IInputPort inputPort;
	private IGameStateSystem gameStateSystem;
	private IGameStateFactory gameStateFactory;
	private ILinkData linkData;
	private ISystemPort systemPort;

	private TextureDrawComponent gameOverScreen;
	private ColorDrawComponent background;

	private GameOverState state = GameOverState.LINK_DIE;
	private int counter = 180;

	public GameOverGameState(
			IInputPort inputPort,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			IGameStateSystem gameStateSystem,
			IGameStateFactory gameStateFactory,
			ILinkData linkData,
			ISystemPort systemPort
			) {
		this.inputPort = inputPort;
		this.soundSystem = soundSystem;
		this.gameStateSystem = gameStateSystem;
		this.gameStateFactory = gameStateFactory;
		this.linkData = linkData;
		this.systemPort = systemPort;

		gameOverScreen = gfxSystem.createTextureDrawComponent(4);
		gameOverScreen.setTexture(4, 345, 160, 144);
		gameOverScreen.setSize(160, 144);
		gameOverScreen.setVisible(false);

		background = gfxSystem.createColorDrawComponent(3);
		background.setColor(1, 1, 1);
		background.setAlpha(0);
		background.setCoordinates(0, 0, 160, 144);

		linkData.setDrawLayer(4);
		linkData.setDying();

		soundSystem.linkDies();
	}

	@Override
	public void onCreate() {
	}
	@Override
	public void onRemove() {
		gameOverScreen.remove();
		background.remove();
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
		if(state == GameOverState.LINK_DIE) {
			counter -= 1;
			if(counter < 0) {
				state = GameOverState.GAME_OVER_SCREEN;
				soundSystem.gameOverScreen();
				linkData.setDrawLayer(1);
//				counter = 60;
				gameOverScreen.setVisible(true);
			}
			else if(counter == 60) {
				linkData.setDead();
			}
			if(counter > 60) {
				if(counter > 120) {
					background.setAlpha(1 - ((counter-120)/60.0f));
				}
				if(counter % 10 == 0) {
					linkData.changeDirectionToRight();
				}
			}
		}
		else if(state == GameOverState.GAME_OVER_SCREEN) {
			if(inputPort.isStartButtonJustPressed() == true) {
				gameStateSystem.clear();
				gameStateFactory.createMainGameState();
			}
		}
	}

	@Override
	public void pauseDraw() {
	}
	@Override
	public void draw() {
	}
}
