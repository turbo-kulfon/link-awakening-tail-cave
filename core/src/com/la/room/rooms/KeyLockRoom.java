package com.la.room.rooms;

import com.la.factory.IEnemyFactory;
import com.la.factory.IRoomFactory;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.respawn.RespawnSystem;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.respawn.EnemyRespawnComponent;
import com.la.room.respawn.KeyLockController;
import com.la.room.transition.IMapLocation;

public class KeyLockRoom implements IRoom {
	private int mapData[][] = new int[][]{
		{41, 41, 41, 32, 18, 10, 10, 10, 19, 22},
		{ 0,  0,  0, 44, 11,  0,  0,  0, 15, 19},
		{ 0,  0,  0, 44, 11,  0,  0,  0,  0, 12},
		{42, 42,  0, 34, 11,  0,  0,  0,  0, 12},
		{18, 10,  8, 10, 14, 90,  0,  0,  0, 12},
		{11,  0,  0,  0,  0, 88,  0,  0,  0, 12},
		{11,  0,  0,  0,  0, 91,  0,  0,  0, 12},
		{20, 13, 13, 13, 13, 13, 17,  0, 16, 21},
	};

	private IRoomCreator roomCreator;
	private IEnemyFactory enemyFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;

	private EnemyRespawnComponent e1;
	private KeyLockController keyLockController;

	public KeyLockRoom(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEnemyFactory enemyFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem,
			RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;
		this.enemyFactory = enemyFactory;

		e1 = new EnemyRespawnComponent(respawnSystem);

		mapLocation.addEntry(20, 4, 4, 19, -1, -1, 5, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(4, 4, 0, false, false, false, true, false, false, true);

		keyLockController = new KeyLockController(roomFactory, 2*16, 5*16, 0, ()-> {
			
		});
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		keyLockController.check(offsetX, offsetY);

		e1.create((callback)-> {
			enemyFactory.createHardhatBeetle(7 * 16 + 4 + offsetX, 2 * 16 + 3 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});

		mapDisplayEntryComponent.setVisited();
	}
}
