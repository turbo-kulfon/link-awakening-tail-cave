package com.la.room.rooms;

import com.engine.sound.SoundSystem;
import com.la.factory.IEnemyFactory;
import com.la.factory.IGameStateFactory;
import com.la.factory.IRoomFactory;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.respawn.RespawnSystem;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.respawn.ChestController;
import com.la.room.respawn.EnemyRespawnComponent;
import com.la.room.transition.IMapLocation;

public class FloorButtonRoom implements IRoom {
	private int mapData[][] = new int[][] {
		{18, 10, 10, 10, 10, 10, 10, 10, 10, 19},
		{11,  0,  0,  0,  0,  0,  0,  0,  0, 12},
		{11,  0,  0, 90,  0,  0, 90,  0,  0, 15},
		{23,  0,  0, 88,  0,  0, 88,  0,  0,  0},
		{24,  0,  0, 91, 92, 92, 91,  0,  0,  0},
		{11,  0,  0,  0,  0,  0,  0,  0,  0, 16},
		{11,  2,  0,  0,  0,  0,  0,  0,  2, 12},
		{20, 13, 13, 17,  0,  0, 16, 13, 13, 21},
	};

	private SoundSystem soundSystem;
	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IEnemyFactory enemyFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private ChestController chestController;
	private EnemyRespawnComponent e1, e2, e3;

	public FloorButtonRoom(
			SoundSystem soundSystem,
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEnemyFactory enemyFactory,
			IGameStateFactory gameStateFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem,
			RespawnSystem respawnSystem) {
		this.soundSystem = soundSystem;
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.enemyFactory = enemyFactory;

		mapLocation.addEntry(2, 3, 6, 6, 4, -1, 0, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(3, 6, 0, true, true, false, true, true, false, true);

		e1 = new EnemyRespawnComponent(respawnSystem);
		e2 = new EnemyRespawnComponent(respawnSystem);
		e3 = new EnemyRespawnComponent(respawnSystem);

		chestController = new ChestController(8, 2, true, true, roomFactory, respawnSystem, (chestX, chestY)-> {
			gameStateFactory.createChestItemAcquireState(70, 0, (int)chestX + 8, (int)chestY + 8);
			mapDisplayEntryComponent.chestOpened();
			mapDisplayEntryComponent.keyTaken();
		});
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		chestController.check(offsetX, offsetY, false);

		e1.create((callback)-> {
			enemyFactory.createGelRed(2 * 16 + 8 + offsetX, 5 * 16 + 8 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		e2.create((callback)-> {
			enemyFactory.createGelRed(7 * 16 + 8 + offsetX, 5 * 16 + 8 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		e3.create((callback)-> {
			enemyFactory.createHardhatBeetle(4 * 16 + offsetX, 2 * 16 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});

		roomFactory.createFloorButton(5 * 16 + offsetX, 3 * 16 + offsetY, ()-> {
			chestController.trigger();
			soundSystem.secretSolved();
		});

		mapDisplayEntryComponent.setVisited();
	}
}
