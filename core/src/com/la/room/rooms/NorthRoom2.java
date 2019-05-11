package com.la.room.rooms;

import com.la.factory.IEnemyFactory;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.respawn.RespawnSystem;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.respawn.EnemyRespawnComponent;
import com.la.room.transition.IMapLocation;

public class NorthRoom2 implements IRoom {
	private int mapData[][] = new int[][]{
		{10, 10, 10, 10, 10, 10, 10, 10, 10, 19},
		{ 0,  0,  0,  0,  0,  0,  0,  0,  2, 12},
		{ 1,  1,  1, 51,  1,  1, 51,  1,  0, 12},
		{ 1,  1, 51,  1,  1, 51,  1,  1,  0, 12},
		{ 0,  0,  0,  0, 51,  0,  1, 51,  0, 12},
		{41, 41, 41, 51,  0,  0, 51,  1,  0, 12},
		{ 1,  1,  1,  1,  1,  1,  1,  1,  2, 12},
		{13, 13, 13, 13, 13, 13, 13, 13, 13, 21},
	};

	private IRoomCreator roomCreator;
	private IEnemyFactory enemyFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private EnemyRespawnComponent e1, e2;

	public NorthRoom2(
			IRoomCreator roomCreator,
			IEnemyFactory enemyFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem,
			RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;
		this.enemyFactory = enemyFactory;

		e1 = new EnemyRespawnComponent(respawnSystem);
		e2 = new EnemyRespawnComponent(respawnSystem);

		mapLocation.addEntry(11, 3, 3, 10, -1, -1, -1, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(3, 3, 0, false, false, false, true, false, false, false);
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);

		enemyFactory.createSpark(3 * 16 + offsetX, 2 * 16 + 15 + offsetY, true, 1);
		enemyFactory.createSpark(7 * 16 + offsetX, 4 * 16 + 15 + offsetY, false, 0);

		e1.create((callback)-> {
			enemyFactory.createGelRed(4 * 16 + 8 + offsetX, 1 * 16 + 8 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		e2.create((callback)-> {
			enemyFactory.createGelRed(3 * 16 + 8 + offsetX, 3 * 16 + 8 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});

		mapDisplayEntryComponent.setVisited();
	}
}
