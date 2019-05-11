package com.la.room.rooms;

import com.engine.sound.SoundSystem;
import com.la.factory.IEnemyFactory;
import com.la.factory.IEventFactory;
import com.la.factory.IRoomFactory;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.respawn.RespawnSystem;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.respawn.EnemyRespawnComponent;
import com.la.room.respawn.KeyController;
import com.la.room.transition.IMapLocation;

public class KeyBridge implements IRoom {
	private int mapData[][] = new int[][] {
		{22, 18, 10, 10, 10, 10, 10, 10, 19, 22},
		{18, 14, 91, 91, 91, 91, 91, 91, 15, 19},
		{ 3, 91,  0,  0,  0,  0,  0,  0, 91, 15},
		{23,  0,  1,  0,  0,  0,  0,  0,  0,  0},
		{24,  0,  0,  0,  0,  0,  0,  0,  0,  0},
		{ 3, 90,  0,  0,  0,  0,  0,  0, 90, 16},
		{20, 17, 90, 90, 90, 90, 90, 90, 16, 21},
		{22, 20, 13, 13, 13, 13, 13, 13, 21, 22},
	};

	private IRoomCreator roomCreator;
	private IEventFactory eventFactory;
	private IEnemyFactory enemyFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private SoundSystem soundSystem;

	private KeyController keyController;
	private EnemyRespawnComponent e1, e2;

	public KeyBridge(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEventFactory eventFactory,
			IEnemyFactory enemyFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem,
			SoundSystem soundSystem,
			RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;
		this.eventFactory = eventFactory;
		this.enemyFactory = enemyFactory;
		this.soundSystem = soundSystem;

		mapLocation.addEntry(1, 2, 7, 3, 0, -1, -1, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(2, 7, 0, false, true, false, true, true, false, false);

		e1 = new EnemyRespawnComponent(respawnSystem);
		e2 = new EnemyRespawnComponent(respawnSystem);

		keyController = new KeyController(2, 3, 0, 100, true, roomFactory, ()-> {
			mapDisplayEntryComponent.keyTaken();
		});
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		e1.create((callback)-> {
			enemyFactory.createHardhatBeetle(3 * 16 + offsetX, 2 * 16 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		e2.create((callback)-> {
			enemyFactory.createHardhatBeetle(6 * 16 + offsetX, 2 * 16 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		keyController.check(offsetX, offsetY);
		eventFactory.createWhenEnemiesDefeated(()-> {
			keyController.trigger();
		});
		mapDisplayEntryComponent.setVisited();
	}
}
