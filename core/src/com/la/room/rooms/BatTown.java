package com.la.room.rooms;

import com.la.event.EnemiesAutoDoorEvent.EnemiesAutoDoorEventCallback;
import com.la.factory.IEnemyFactory;
import com.la.factory.IEventFactory;
import com.la.factory.IRoomFactory;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.respawn.RespawnSystem;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.respawn.EnemyRespawnComponent;
import com.la.room.transition.IMapLocation;

public class BatTown implements IRoom {
	private int mapData[][] = new int[][]{
		{22, 18, 10, 10, 27, 28, 10, 10, 19, 22},
		{18, 14, 89, 89,  0,  0, 89, 89, 15, 19},
		{11, 89,  0,  0,  0,  0,  0,  0, 89,  4},
		{11, 89,  0, 50,  0,  0, 50,  0,  0, 25},
		{11, 89,  0, 50,  0,  0, 50,  0,  0, 26},
		{11, 89,  0,  0,  0,  0,  0,  0, 89,  4},
		{20, 17, 89, 89,  0,  0, 89, 89, 16, 21},
		{22, 20, 13, 13, 13, 13, 13, 13, 21, 22},
	};

	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IEventFactory eventFactory;
	private IEnemyFactory enemyFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private EnemyRespawnComponent e1, e2, e3, e4;

	public BatTown(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEventFactory eventFactory,
			IEnemyFactory enemyFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem,
			RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.eventFactory = eventFactory;
		this.enemyFactory = enemyFactory;

		e1 = new EnemyRespawnComponent(respawnSystem);
		e2 = new EnemyRespawnComponent(respawnSystem);
		e3 = new EnemyRespawnComponent(respawnSystem);
		e4 = new EnemyRespawnComponent(respawnSystem);

		mapLocation.addEntry(6, 2, 6, -1, 2, 7, -1, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(2, 6, 0, false, false, false, false, true, true, false);
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);

		e1.create((callback)-> {
			enemyFactory.createKeese(4 * 16 + 4 + offsetX, 3 * 16 + 3 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		e2.create((callback)-> {
			enemyFactory.createKeese(5 * 16 + 4 + offsetX, 3 * 16 + 3 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		e3.create((callback)-> {
			enemyFactory.createKeese(4 * 16 + 4 + offsetX, 4 * 16 + 3 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		e4.create((callback)-> {
			enemyFactory.createKeese(5 * 16 + 4 + offsetX, 4 * 16 + 3 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});

		roomFactory.createDoor(9 * 16 + offsetX, 3 * 16 + 8 + offsetY, 1);
		roomFactory.createDoor(4 * 16 + 8 + offsetX, 0 + offsetY, 2);
		eventFactory.createEnemiesAutoDoor(new EnemiesAutoDoorEventCallback() {
			@Override
			public void onOpen() {
				mapDisplayEntryComponent.setUpPath(true);
				mapDisplayEntryComponent.setRightPath(true);
			}
			@Override
			public void onClose() {
				mapDisplayEntryComponent.setUpPath(false);
				mapDisplayEntryComponent.setRightPath(false);
			}
		});

		mapDisplayEntryComponent.setVisited();
	}
}
