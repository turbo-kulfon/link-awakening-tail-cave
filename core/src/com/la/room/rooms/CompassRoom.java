package com.la.room.rooms;

import com.la.event.EnemiesAutoDoorEvent.EnemiesAutoDoorEventCallback;
import com.la.factory.IEnemyFactory;
import com.la.factory.IEventFactory;
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

public class CompassRoom implements IRoom {
	private int mapData[][] = new int[][] {
		{18, 10, 10, 10, 10, 10, 19, 22, 22, 22},
		{11,  0,  0,  0,  0,  0, 12, 22, 22, 22},
		{11,  0, 50,  1, 50,  0, 15, 10, 10, 19},
		{11,  0,  1,  1,  1,  0,  0,  0,  0, 25},
		{11,  0,  1,  1,  1,  0,  0,  0,  0, 26},
		{11,  0,  0,  0,  0,  0, 16, 13, 13, 21},
		{11,  2,  0,  0,  0,  2, 12, 22, 22, 22},
		{20, 13, 13, 13, 13, 13, 21, 22, 22, 22},
	};

	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IEventFactory eventFactory;
	private IEnemyFactory enemyFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private EnemyRespawnComponent enemy1, enemy2, enemy3, enemy4;
	private ChestController chestController;

	public CompassRoom(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEventFactory eventFactory,
			IEnemyFactory enemyFactory,
			IGameStateFactory gameStateFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem,
			RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.eventFactory = eventFactory;
		this.enemyFactory = enemyFactory;

		mapLocation.addEntry(3, 1, 7, -1, 1, -1, -1, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(1, 7, 0, true, false, false, false, true, false, false);

		enemy1 = new EnemyRespawnComponent(respawnSystem);
		enemy2 = new EnemyRespawnComponent(respawnSystem);
		enemy3 = new EnemyRespawnComponent(respawnSystem);
		enemy4 = new EnemyRespawnComponent(respawnSystem);

		chestController = new ChestController(3, 3, false, false, roomFactory, respawnSystem, (chestX, chestY)-> {
			gameStateFactory.createChestItemAcquireState(110, 0, (int)chestX + 8, (int)chestY + 8);
			mapDisplayEntryComponent.chestOpened();
		});
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);

		enemy1.create((callback)-> {
			enemyFactory.createZolGreen(2 * 16 + offsetX, 3 * 16 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		enemy2.create((callback)-> {
			enemyFactory.createZolGreen(4 * 16 + offsetX, 3 * 16 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		enemy3.create((callback)-> {
			enemyFactory.createZolGreen(2 * 16 + offsetX, 4 * 16 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		enemy4.create((callback)-> {
			enemyFactory.createZolGreen(4 * 16 + offsetX, 4 * 16 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});

		chestController.check(offsetX, offsetY, false);
		roomFactory.createDoor(9 * 16 + offsetX, 3 * 16 + 8 + offsetY, 1);
		roomFactory.createAggressiveGroundTorch(1 * 16 + offsetX, 1 * 16 + offsetY, 60);
		roomFactory.createAggressiveGroundTorch(5 * 16 + offsetX, 1 * 16 + offsetY, 120);
		eventFactory.createEnemiesAutoDoor(new EnemiesAutoDoorEventCallback() {
			@Override
			public void onOpen() {
				mapDisplayEntryComponent.setRightPath(true);
			}
			@Override
			public void onClose() {
				mapDisplayEntryComponent.setRightPath(false);
			}
		});
		mapDisplayEntryComponent.setVisited();
	}
}
