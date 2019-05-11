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
import com.la.room.transition.IMapLocation;

public class FeatherRoom implements IRoom {
	private int mapData[][] = new int[][] {
		{18, 10, 10, 10, 10, 10, 10, 10, 10, 19},
		{11, 88,  2,  1,  1,  1,  2, 88, 88, 12},
		{11, 88,  0,  1,  1,  1,  0, 88, 88, 12},
		{11, 88,  0,  1,  1,  1,  0, 88, 88, 12},
		{11, 91, 92, 92,  0, 92, 92, 91, 91, 12},
		{11,  0,  0,  0,  0,  0,  0,  0,  0, 12},
		{11,  2,  0,  0,  0,  0,  0,  0,  2, 12},
		{20, 13, 13, 17,  0, 16, 13, 13, 13, 21},
	};

	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IEnemyFactory enemyFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private ChestController chestController;

	public FeatherRoom(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEnemyFactory enemyFactory,
			IMapLocation mapLocation,
			IGameStateFactory gameStateFactory,
			IMapDisplaySystem mapDisplaySystem,
			RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.enemyFactory = enemyFactory;

		mapLocation.addEntry(23, 0, 4, -1, -1, -1, 22, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(0, 4, 0, true, false, false, true, false, false, true);

		chestController = new ChestController(4, 2, false, false, roomFactory, respawnSystem, (chestX, chestY)-> {
			gameStateFactory.createChestItemAcquireState(2, 0, (int)chestX + 8, (int)chestY + 8);
			mapDisplayEntryComponent.chestOpened();
		});
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		chestController.check(offsetX, offsetY, false);

		enemyFactory.createBladeTrap(1 * 16 + offsetX, 5 * 16 + offsetY);
		enemyFactory.createBladeTrap(8 * 16 + offsetX, 5 * 16 + offsetY);

		mapDisplayEntryComponent.setVisited();
	}
}
