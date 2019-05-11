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

public class BladeTrapRoom implements IRoom {
	private int mapData[][] = new int[][]{
		{22, 22, 18, 10, 27, 28, 10, 19, 31, 41},
		{22, 18, 14,  0,  0,  0, 90, 12, 43,  0},
		{18, 14, 92,  0,  0,  0, 88, 12, 43,  0},
		{11,  0,  0,  0,  0, 92, 88, 12, 33, 42},
		{11,  0,  0,  0,  0,  0, 88, 15, 10, 10},
		{11,  0,  0,  0,  0,  0, 91, 91, 91, 91},
		{11,  2,  0, 50,  0,  0, 50,  0, 16, 13},
		{11,  0,  0,  0,  0,  0,  0,  0, 12, 22},
	};

	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IEnemyFactory enemyFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private DoorKeyController doorKeyController;
	private EnemyRespawnComponent e1;

	public BladeTrapRoom(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEnemyFactory enemyFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem,
			RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.enemyFactory = enemyFactory;

		mapLocation.addEntry(9, 2, 4, -1, 19, 10, 7, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(2, 4, 0, false, false, false, false, true, false, true);
		e1 = new EnemyRespawnComponent(respawnSystem);

		doorKeyController = new DoorKeyController(roomFactory, 4 * 16 + 8, 0 * 16, 0, 2, ()-> {
			mapDisplayEntryComponent.setUpPath(true);
		});
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		roomFactory.createMovingBlock(2 * 16 + offsetX, 4 * 16 + offsetY, ()->{});
		doorKeyController.check(offsetX, offsetY);
		enemyFactory.createBladeTrap(1 * 16 + offsetX, 3 * 16 + offsetY);

		e1.create((callback)-> {
			enemyFactory.createKeese(5 * 16 + 4 + offsetX, 4 * 16 + 3 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});

		mapDisplayEntryComponent.setVisited();
	}
}
