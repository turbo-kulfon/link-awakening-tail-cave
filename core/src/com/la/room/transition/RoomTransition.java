package com.la.room.transition;

import com.engine.double_buffer.IDoubleBuffer;
import com.engine.event_system.IEventSystem;
import com.engine.game_object.IGameObject;
import com.engine.sound.SoundSystem;
import com.engine.tile_map.TileMapSystem;
import com.la.factory.IGameStateFactory;
import com.la.game_objects.link.ILinkData;
import com.la.game_states.FlashGameState.FlashStateCallback;
import com.la.game_states.RoomTransitionState.CounterEndCallback;
import com.la.map_display.IMapDisplaySystem;
import com.la.respawn.RespawnSystem;
import com.la.room.IRoomMap;

public class RoomTransition implements IRoomTransition {
	private SoundSystem soundSystem;
	private IRoomMap roomMap;
	private IDoubleBuffer<IGameObject> roomObjects;
	private IMapLocation mapLocation;
	private IGameStateFactory gameStateFactory;
	private IEventSystem eventSystem;
	private CounterEndCallback callback;
	private FlashStateCallback flashStateCallback;
	private IMapDisplaySystem mapDisplaySystem;
	private TileMapSystem tileMapSystem;
	private RespawnSystem respawnSystem;
	private ILinkData linkData;

	public RoomTransition(
			SoundSystem soundSystem,
			IRoomMap roomMap,
			IDoubleBuffer<IGameObject> roomObjects,
			IMapLocation mapLocation,
			IGameStateFactory gameStateFactory,
			IEventSystem eventSystem,
			IMapDisplaySystem mapDisplaySystem,
			TileMapSystem tileMapSystem,
			RespawnSystem respawnSystem,
			ILinkData linkData) {
		this.soundSystem = soundSystem;
		this.roomMap = roomMap;
		this.roomObjects = roomObjects;
		this.mapLocation = mapLocation;
		this.gameStateFactory = gameStateFactory;
		this.eventSystem = eventSystem;
		this.mapDisplaySystem = mapDisplaySystem;
		this.tileMapSystem = tileMapSystem;
		this.respawnSystem = respawnSystem;
		this.linkData = linkData;

		callback = new CounterEndCallback() {
			@Override
			public void onCountdownEnd() {
				clear();
				mapDisplaySystem.keyCheck();
				mapLocation.getCurrentRoomData().onMapTransitionEnd();
			}
		};
		flashStateCallback = new FlashStateCallback() {
			@Override
			public void onTransition() {
				eventSystem.clear();
				tileMapSystem.reset();
				roomMap.createRoom(mapLocation.getCurrentRoomData().getRoomID(), 0, 0);
				linkData.setPosition(mapLocation.getCurrentRoomData().getLinkDefaultX(), mapLocation.getCurrentRoomData().getLinkDefaultY());
				clear();
				moveStateCheck();
				soundSystem.stopMusic();
			}
			@Override
			public void onEnd() {
				mapDisplaySystem.keyCheck();
				soundSystem.playDungeonMusic();
			}
		};
	}

	@Override
	public void gotoRoom(int roomID) {
		boolean currentPlatform = mapLocation.getCurrentRoomData().isPlatform();
		mapLocation.setPosition(roomID);
		boolean nextPlatform = mapLocation.getCurrentRoomData().isPlatform();
		roomObjects.swap();
		createFlashState(currentPlatform, nextPlatform);
//		gameStateFactory.createFlashState(flashStateCallback);
	}
	@Override
	public void goLeft() {
		boolean currentPlatform = mapLocation.getCurrentRoomData().isPlatform();
		mapLocation.moveLeft();
		boolean nextPlatform = mapLocation.getCurrentRoomData().isPlatform();
		roomObjects.swap();
		stateCreate(currentPlatform, nextPlatform, 0, -160, 0);
	}
	@Override
	public void goRight() {
		boolean currentPlatform = mapLocation.getCurrentRoomData().isPlatform();
		mapLocation.moveRight();
		boolean nextPlatform = mapLocation.getCurrentRoomData().isPlatform();
		roomObjects.swap();
		stateCreate(currentPlatform, nextPlatform, 1, 160, 0);
	}
	@Override
	public void goUp() {
		boolean currentPlatform = mapLocation.getCurrentRoomData().isPlatform();
		mapLocation.moveUp();
		boolean nextPlatform = mapLocation.getCurrentRoomData().isPlatform();
		roomObjects.swap();
		stateCreate(currentPlatform, nextPlatform, 2, 0, -128);
	}
	@Override
	public void goDown() {
		boolean currentPlatform = mapLocation.getCurrentRoomData().isPlatform();
		mapLocation.moveDown();
		boolean nextPlatform = mapLocation.getCurrentRoomData().isPlatform();
		roomObjects.swap();
		stateCreate(currentPlatform, nextPlatform, 3, 0, 128);
	}

	private void stateCreate(boolean prevPlatform, boolean nextPlatform, int direction, int xOffset, int yOffset) {
		if(prevPlatform == nextPlatform) {
			respawnSystem.update(
				mapLocation.getCurrentRoomData().getX(),
				mapLocation.getCurrentRoomData().getY(),
				mapLocation.getCurrentRoomData().getRoomID(),
				false);
			eventSystem.clear();
			tileMapSystem.reset();
			roomMap.createRoom(mapLocation.getCurrentRoomData().getRoomID(), xOffset, yOffset);
			mapLocation.getCurrentRoomData().onMapTransitionStart();
			gameStateFactory.createRoomTransitionState(direction, mapLocation.getCurrentRoomData().isDeepWalk(), callback);
		}
		else {
			createFlashState(prevPlatform, nextPlatform);
//			gameStateFactory.createFlashState(flashStateCallback);
		}
	}
	private void createFlashState(boolean prevPlatform, boolean nextPlatform) {
		respawnSystem.update(
				mapLocation.getCurrentRoomData().getX(),
				mapLocation.getCurrentRoomData().getY(),
				mapLocation.getCurrentRoomData().getRoomID(),
				true);
		soundSystem.setIsUnderground(nextPlatform == true);
		soundSystem.unsetBossCombat();
		gameStateFactory.createFlashState(flashStateCallback);
		if(prevPlatform == true && nextPlatform == false) {
			soundSystem.enterStairs();
		}
	}
	private void clear() {
		roomObjects.runCommandOnBackBuffer((gameObject)-> {
			gameObject.setToRemove();
		});
		roomObjects.clearBackBuffer();
		linkData.setLastPosition();
		if(mapLocation.getCurrentRoomData().isPlatform() == false) {
			mapDisplaySystem.setLinkMapPosition(mapLocation.getCurrentRoomData().getX(), mapLocation.getCurrentRoomData().getY());
		}
	}
	private void moveStateCheck() {
		if(mapLocation.getCurrentRoomData().isPlatform() == false) {
			linkData.changeMoveState(0);
		}
		else {
			linkData.changeMoveState(1);
		}
	}
}
