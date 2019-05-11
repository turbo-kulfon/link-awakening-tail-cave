package com.la.room.rooms;

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

public class CenterRoom implements IRoom {
	private int mapData[][] = new int[][]{
		{22, 11,  0,  0,  0,  0,  0,  0,  0, 12},
		{10, 14,  0,  1,  1,  1,  0,  0,  0, 15},
		{ 0,  0,  0,  1,  1,  1,  0, 51,  0,  0},
		{ 0,  0,  0,  1,  1,  1,  0,  0,  0, 51},
		{ 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
		{ 0,  0,  0,  0, 51,  0,  0, 51,  0,  0},
		{ 0,  0,  0,  0,  0,  0,  0,  0,  0, 16},
		{13, 13, 13, 13, 13, 13, 13, 13, 13, 21},
	};

	private IRoomCreator roomCreator;
	private IEnemyFactory enemyFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private ChestController chestController;
	private EnemyRespawnComponent e1;

	public CenterRoom(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEnemyFactory enemyFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem,
			IGameStateFactory gameStateFactory,
			RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;
		this.enemyFactory = enemyFactory;

		mapLocation.addEntry(8, 3, 5, 7, 5, 19, -1, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(3, 5, 0, true, true, false, true, true, true, false);

		e1 = new EnemyRespawnComponent(respawnSystem);

		chestController = new ChestController(4, 2, false, false, roomFactory, respawnSystem, (chestX, chestY)-> {
			gameStateFactory.createChestItemAcquireState(70, 0, (int)chestX + 8, (int)chestY + 8);
			mapDisplayEntryComponent.chestOpened();
			mapDisplayEntryComponent.keyTaken();
		});
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);

		chestController.check(offsetX, offsetY, false);

		enemyFactory.createSpark(4 * 16 + 4 + offsetX, 2 * 16 + 15 + offsetY, true, 1);
		enemyFactory.createSpark(7 * 16 + 4 + offsetX, 5 * 16 - 8 + offsetY, false, 1);

		e1.create((callback)-> {
			enemyFactory.createMiniMoldorm(5 * 16 + 4 + offsetX, 3 * 16 + 3 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});

		mapDisplayEntryComponent.setVisited();
	}
}
