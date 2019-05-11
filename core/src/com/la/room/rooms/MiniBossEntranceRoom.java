package com.la.room.rooms;

import com.la.factory.IEnemyFactory;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.respawn.RespawnSystem;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.respawn.EnemyRespawnComponent;
import com.la.room.transition.IMapLocation;

public class MiniBossEntranceRoom implements IRoom {
	private int mapData[][] = new int[][] {
		{18, 10, 10, 14,  0,  0, 15, 10, 10, 19},
		{11,  0,  0,  0,  0,  0,  0,  0,  0, 12},
		{11,  0,  0,  0,  0,  0,  0, 90, 92,  4},
		{23,  0,  0, 16, 13, 17,  0, 88,  0, 25},
		{24,  0,  0, 15, 10, 14,  0, 88,  0, 26},
		{11,  0,  0,  0,  0,  0,  0, 91, 92,  4},
		{11,  0,  0,  0,  0,  0,  0,  0,  0, 12},
		{20, 13, 13, 13, 13, 13, 13, 13, 13, 21},
	};

	private IRoomCreator roomCreator;
	private IEnemyFactory enemyFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private EnemyRespawnComponent e1;

	public MiniBossEntranceRoom(
			IRoomCreator roomCreator,
			IEnemyFactory enemyFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem,
			RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;
		this.enemyFactory = enemyFactory;

		e1 = new EnemyRespawnComponent(respawnSystem);

		mapLocation.addEntry(13, 5, 5,  5, 15, 14, -1, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(5, 5, 0, false, false, false, true, true, true, false);
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);

		enemyFactory.createSpark(5 * 16 + offsetX, 5 * 16 - 0 + offsetY, false, 1);
		enemyFactory.createSpark(7 * 16 + offsetX, 6 * 16 + 6 + offsetY, true, 1);

		e1.create((callback)-> {
			enemyFactory.createStalfosOrange(3 * 16 + offsetX, 1 * 16 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});

		mapDisplayEntryComponent.setVisited();
	}
}
