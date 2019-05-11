package com.la.room.rooms;

import com.la.factory.IEnemyFactory;
import com.la.factory.IRoomFactory;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.respawn.RespawnSystem;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.respawn.DoorKeyController;
import com.la.room.respawn.EnemyRespawnComponent;
import com.la.room.transition.IMapLocation;

public class OneWayExitRoom implements IRoom {
	private int mapData[][] = new int[][]{
		{18, 10, 10, 10, 10, 10, 14,  0, 15, 19},
		{14,  0,  0,  0,  0,  0,  0,  0,  0, 12},
		{ 0,  0,  0, 51, 51, 51, 51, 51, 51, 12},
		{51, 51, 51, 51,  0,  0,  0,  0,  0, 25},
		{ 0,  0,  0,  0,  0,  0,  0,  0,  0, 26},
		{ 0,  0,  0,  0,  0,  0,  0,  0,  0, 12},
		{17,  0,  0,  0,  0,  0,  0,  0,  0, 12},
		{20, 13, 13, 13, 29, 30, 13, 13, 13, 21},
	};

	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IEnemyFactory enemyFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private DoorKeyController doorKeyController;
	private EnemyRespawnComponent e1, e2, e3;

	public OneWayExitRoom(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEnemyFactory enemyFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem,
			RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.enemyFactory = enemyFactory;

		e1 = new EnemyRespawnComponent(respawnSystem);
		e2 = new EnemyRespawnComponent(respawnSystem);
		e3 = new EnemyRespawnComponent(respawnSystem);

		mapLocation.addEntry(5, 4, 5, 8, 13, 20, 4, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(4, 5, 0, false, false, false, true, false, true, false);
		doorKeyController = new DoorKeyController(roomFactory, 9 * 16, 3 * 16 + 8, 0, 1, ()-> {
			mapDisplayEntryComponent.setRightPath(true);
		});
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		roomFactory.createOneWayDoorExit(4 * 16 + 8 + offsetX, 7 * 16 + offsetY);
		doorKeyController.check(offsetX, offsetY);

		e1.create((callback)-> {
			enemyFactory.createKeese(4 * 16 + 4 + offsetX, 3 * 16 + 3 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		e2.create((callback)-> {
			enemyFactory.createKeese(5 * 16 + 4 + offsetX, 2 * 16 + 3 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		e3.create((callback)-> {
			enemyFactory.createStalfosOrange(7 * 16 + 4 + offsetX, 5 * 16 + 3 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});

		mapDisplayEntryComponent.setVisited();
	}
}
