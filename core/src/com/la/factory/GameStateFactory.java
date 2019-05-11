package com.la.factory;

import com.engine.IInputPort;
import com.engine.ISystemPort;
import com.engine.aspect.IAspectSystem;
import com.engine.game_state.IGameState;
import com.engine.game_state.IGameStateSystem;
import com.engine.gfx.GFXSystem;
import com.engine.sound.ISoundPort;
import com.engine.sound.SoundSystem;
import com.engine.util.IRNG;
import com.la.equipment.IEquipmentSystem;
import com.la.game_objects.link.ILinkData;
import com.la.game_states.BigHeartAqcuiredGameState;
import com.la.game_states.BigHeartAqcuiredGameState.BigHeartAqcuiredGameStateCallback;
import com.la.game_states.ChestItemAcquireState;
import com.la.game_states.FlashGameState;
import com.la.game_states.FlashGameState.FlashStateCallback;
import com.la.game_states.GameOverGameState;
import com.la.game_states.InstrumentAcquiredGameState;
import com.la.game_states.MainGameState;
import com.la.game_states.PowerUpAcquireState;
import com.la.game_states.RoomTransitionState;
import com.la.game_states.RoomTransitionState.CounterEndCallback;
import com.la.game_states.TeleportState;
import com.la.game_states.TeleportState.TeleportStateCallback;
import com.la.game_states.TextDisplayState;
import com.la.game_states.TextDisplayState.Callback;

public class GameStateFactory implements IGameStateFactory {
	private IGameStateSystem gameStateSystem;
	private GFXSystem gfxSystem;
	private SoundSystem soundSystem;
	private IInputPort inputPort;
	private ISoundPort soundPort;
	private ISystemPort systemPort;
	private ILinkData linkData;
	private IAspectSystem aspectSystem;
	private IEquipmentSystem equipmentSystem;
	private IRNG rng;
	private IRoomFactory roomFactory;

	public GameStateFactory(
			IGameStateSystem gameStateSystem,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			ISoundPort soundPort,
			IInputPort inputPort,
			ISystemPort systemPort,
			ILinkData linkData,
			IAspectSystem aspectSystem,
			IEquipmentSystem equipmentSystem,
			IRNG rng,
			IRoomFactory roomFactory) {
		this.gameStateSystem = gameStateSystem;
		this.gfxSystem = gfxSystem;
		this.soundSystem = soundSystem;
		this.inputPort = inputPort;
		this.soundPort = soundPort;
		this.systemPort = systemPort;
		this.linkData = linkData;
		this.aspectSystem = aspectSystem;
		this.equipmentSystem = equipmentSystem;
		this.rng = rng;
		this.roomFactory = roomFactory;
	}

	@Override
	public IGameState createMainGameState() {
		IGameState gameState = new MainGameState(gfxSystem, systemPort, inputPort, soundPort, gameStateSystem);
		gameStateSystem.pushState(gameState);
		return gameState;
	}
	@Override
	public IGameState createRoomTransitionState(int direction, boolean deepWalk, CounterEndCallback callback) {
		IGameState gameState = new RoomTransitionState(direction, deepWalk, gameStateSystem, aspectSystem, callback);
		gameStateSystem.pushState(gameState);
		return gameState;
	}
	@Override
	public IGameState createFlashState(FlashStateCallback callback) {
		IGameState gameState = new FlashGameState(gameStateSystem, gfxSystem, callback);
		gameStateSystem.pushState(gameState);
		return gameState;
	}
	@Override
	public IGameState createTextState(String text) {
		IGameState gameState = new TextDisplayState(gameStateSystem, gfxSystem, soundSystem, inputPort, linkData, text, null);
		gameStateSystem.pushState(gameState);
		return gameState;
	}
	@Override
	public IGameState createTextState(String text, Callback callback) {
		IGameState gameState = new TextDisplayState(gameStateSystem, gfxSystem, soundSystem, inputPort, linkData, text, callback);
		gameStateSystem.pushState(gameState);
		return gameState;
	}
	@Override
	public IGameState createChestItemAcquireState(int itemID, int quantity, int posX, int posY) {
		IGameState gameState = new ChestItemAcquireState(itemID, quantity, posX, posY, gfxSystem, soundSystem, gameStateSystem, this, equipmentSystem);
		gameStateSystem.pushState(gameState);
		return gameState;
	}
	@Override
	public IGameState createPowerUpAcquireState(int itemID, float posX, float posY) {
		IGameState gameState = new PowerUpAcquireState(posX, posY, soundSystem, gameStateSystem, this, equipmentSystem, gfxSystem, linkData, itemID, ()->{
			return equipmentSystem.isPieceOfPowerActive();
		});
		gameStateSystem.pushState(gameState);
		return gameState;
	}
	@Override
	public IGameState createTeleportState(TeleportStateCallback callback) {
		IGameState gameState = new TeleportState(gameStateSystem, linkData, callback);
		gameStateSystem.pushState(gameState);
		return gameState;
	}
	@Override
	public IGameState createBigHeartAqcuiredGameState(BigHeartAqcuiredGameStateCallback callback) {
		IGameState gameState = new BigHeartAqcuiredGameState(gameStateSystem, gfxSystem, soundSystem, linkData, 120, callback);
		gameStateSystem.pushState(gameState);
		return gameState;
	}
	@Override
	public IGameState createInstrumentAcquiredGameState() {
		IGameState gameState = new InstrumentAcquiredGameState(gfxSystem, soundSystem, gameStateSystem, this, rng, systemPort, linkData);
		gameStateSystem.pushState(gameState);
		return gameState;
	}
	@Override
	public IGameState createGameOverGameState() {
		IGameState gameState = new GameOverGameState(inputPort, gfxSystem, soundSystem, gameStateSystem, this, linkData, systemPort);
		gameStateSystem.pushState(gameState);
		return gameState;
	}
}
