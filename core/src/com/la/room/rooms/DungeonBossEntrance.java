package com.la.room.rooms;

import com.la.factory.IEnemyFactory;
import com.la.factory.IRoomFactory;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.respawn.NightmareDoorController;
import com.la.room.transition.IMapLocation;
import com.la.room.transition.IRoomTransition;

public class DungeonBossEntrance implements IRoom {
	private int mapData[][] = new int[][]{
		{18, 10, 10,  5, 27, 28,  5, 10, 10, 19},
		{11,  0,  0,  0,  0,  0,  0,  0,  0, 12},
		{11,  0,  0,  0,  0,  0,  0,  0,  0, 12},
		{11,  0,  0,  2,  0,  0,  2,  0,  0, 12},
		{11,  0,  0, 50, 50, 50, 50,  0,  0, 12},
		{11,  0,  0,  0,  0,  0,  0,  0,  0, 12},
		{11,  0,  0,  0,  0,  0,  0,  0,  0, 12},
		{20, 13, 13, 13, 29, 30, 13, 13, 13, 21},
	};

	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IEnemyFactory enemyFactory;
	private IRoomTransition roomTransition;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private NightmareDoorController nightmareDoorController;

	public DungeonBossEntrance(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEnemyFactory enemyFactory,
			IRoomTransition roomTransition,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem) {
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.enemyFactory = enemyFactory;
		this.roomTransition = roomTransition;

		mapLocation.addEntry(16, 6, 4,  -1, -1, 17, 15, 5*16 + 4, 3*16 + 4, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(6, 4, 0, false, false, false, false, false, false, true);

		nightmareDoorController = new NightmareDoorController(roomFactory, 4 * 16 + 8, 0, 0, 2, ()-> {
			mapDisplayEntryComponent.setUpPath(true);
		});
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		nightmareDoorController.check(offsetX, offsetY);

		enemyFactory.createBladeTrap(1 * 16 + offsetX, 1 * 16 + offsetY);
		enemyFactory.createBladeTrap(8 * 16 + offsetX, 1 * 16 + offsetY);
		enemyFactory.createBladeTrap(1 * 16 + offsetX, 6 * 16 + offsetY);
		enemyFactory.createBladeTrap(8 * 16 + offsetX, 6 * 16 + offsetY);

		roomFactory.createStairs(5 * 16 + offsetX, 3 * 16 + offsetY, ()-> {
			roomTransition.gotoRoom(53);
		});
		mapDisplayEntryComponent.setVisited();
	}
}
