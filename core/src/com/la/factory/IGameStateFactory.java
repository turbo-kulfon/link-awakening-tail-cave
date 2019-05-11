package com.la.factory;

import com.engine.game_state.IGameState;
import com.la.game_states.FlashGameState.FlashStateCallback;
import com.la.game_states.RoomTransitionState.CounterEndCallback;
import com.la.game_states.TeleportState.TeleportStateCallback;
import com.la.game_states.TextDisplayState;
import com.la.game_states.BigHeartAqcuiredGameState.BigHeartAqcuiredGameStateCallback;

public interface IGameStateFactory {
	IGameState createMainGameState();
	IGameState createRoomTransitionState(int direction, boolean deepWalk, CounterEndCallback callback);
	IGameState createFlashState(FlashStateCallback callback);
	IGameState createTextState(String text);
	IGameState createTextState(String text, TextDisplayState.Callback callback);
	IGameState createChestItemAcquireState(int itemID, int quantity, int posX, int posY);
	IGameState createPowerUpAcquireState(int itemID, float posX, float posY);
	IGameState createTeleportState(TeleportStateCallback callback);
	IGameState createBigHeartAqcuiredGameState(BigHeartAqcuiredGameStateCallback callback);
	IGameState createInstrumentAcquiredGameState();
	IGameState createGameOverGameState();
}
