package com.la.room.rooms;

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

public class MapRoom implements IRoom {
	private int mapData[][] = new int[][]{
		{18, 10, 10, 10, 27, 28, 10, 10, 10, 19},
		{11, 88,  0,  2,  1,  1,  2,  0,  0, 12},
		{14, 91,  0,  1,  1,  1,  1,  0,  0, 12},
		{ 0,  0,  0,  1,  1,  1,  1,  0,  0, 12},
		{ 0,  0,  0,  1,  1,  1,  1,  0,  0, 12},
		{17, 90,  0, 50,  0,  0, 50,  0,  0, 12},
		{11, 88,  0,  0,  0,  0,  0,  0,  0, 12},
		{20, 13, 13, 13, 13, 13, 13, 13, 13, 21},
	};

	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IEventFactory eventFactory;
	private IEnemyFactory enemyFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;

	private EnemyRespawnComponent e1, e2, e3, e4;
	private ChestController chestController;

	public MapRoom(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEnemyFactory enemyFactory,
			IEventFactory eventFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem,
			RespawnSystem respawnSystem,
			IGameStateFactory gameStateFactory) {
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.eventFactory = eventFactory;
		this.enemyFactory = enemyFactory;

		e1 = new EnemyRespawnComponent(respawnSystem);
		e2 = new EnemyRespawnComponent(respawnSystem);
		e3 = new EnemyRespawnComponent(respawnSystem);
		e4 = new EnemyRespawnComponent(respawnSystem);

		mapLocation.addEntry(4, 4, 6, 2, -1, 5, -1, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(4, 6, 0, true, false, false, true, false, true, false);

		chestController = new ChestController(8, 2, true, true, roomFactory, respawnSystem, (chestX, chestY)-> {
			gameStateFactory.createChestItemAcquireState(100, 0, (int)chestX + 8, (int)chestY + 8);
			mapDisplayEntryComponent.chestOpened();
		});
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		roomFactory.createOneWayDoorEntrance(4 * 16 + 8 + offsetX, offsetY);
		chestController.check(offsetX, offsetY, true);

		e1.create((callback)-> {
			enemyFactory.createStalfosOrange(3 * 16 + offsetX, 4 * 16 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		e2.create((callback)-> {
			enemyFactory.createStalfosOrange(6 * 16 + offsetX, 4 * 16 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		e3.create((callback)-> {
			enemyFactory.createKeese(4 * 16 + 4 + offsetX, 5 * 16 + 3 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		e4.create((callback)-> {
			enemyFactory.createKeese(5 * 16 + 4 + offsetX, 5 * 16 + 3 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});

		eventFactory.createWhenEnemiesDefeated(()-> {
			chestController.trigger();
		});

		mapDisplayEntryComponent.setVisited();
	}
}
