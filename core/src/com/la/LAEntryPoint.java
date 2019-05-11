package com.la;

import com.badlogic.gdx.ApplicationAdapter;
import com.engine.ISystemPort;
import com.engine.game_state.GameStateSystem;
import com.engine.game_state.IGameStateSystem;
import com.engine.gfx.GFXSystem;
import com.la.game_states.MainGameState;

public class LAEntryPoint extends ApplicationAdapter {
	private LibGDXDrawPort drawPort;
	private LibGDXInputPort inputPort;
	private LibGDXSoundPort soundPort;
	private ISystemPort systemPort;
	
	private GFXSystem gfxSystem;
	private IGameStateSystem gameStateSystem;

	@Override
	public void create () {
		drawPort = new LibGDXDrawPort(160, 144);
		soundPort = new LibGDXSoundPort();
		inputPort = new LibGDXInputPort();
		systemPort = new LibGDXSystemPort();
		gfxSystem = new GFXSystem(drawPort);
		gameStateSystem = new GameStateSystem();
		gameStateSystem.pushState(new MainGameState(gfxSystem, systemPort, inputPort, soundPort, gameStateSystem));
	}

	@Override
	public void render () {
		inputPort.update();

		gameStateSystem.update();
		gameStateSystem.draw();

		drawPort.beginDraw();
		gfxSystem.draw();
		drawPort.endDraw();
	}
	
	@Override
	public void dispose () {
		drawPort.dispose();
		soundPort.dispose();
	}
}
