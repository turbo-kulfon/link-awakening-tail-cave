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
import com.la.room.respawn.DestructableWallController;
import com.la.room.respawn.EnemyRespawnComponent;
import com.la.room.transition.IMapLocation;

public class SecretRoomEntrance implements IRoom {
	private int mapData[][] = new int[][]{
		{11,  0,  0,  0,  0,  0,  0,  0, 12, 22},
		{11,  0,  0, 50,  0,  0, 50,  0, 15, 10},
		{11,  0,  0, 50,  0,  0, 50,  0,  0,  0},
		{ 0,  0, 90,  0,  0,  0,  0, 90,  0,  0},
		{11,  0, 91,  0,  0,  0,  0, 91,  0,  0},
		{11, 90,  0,  0,  0,  0,  0,  0,  0,  0},
		{11, 88, 90, 90,  0,  0, 90, 90, 90,  0},
		{20, 13, 13, 13, 29, 30, 13, 13, 13, 13},
	};

	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IEnemyFactory enemyFactory;
	private IEventFactory eventFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;

	private EnemyRespawnComponent e1;
	private ChestController chestController;
	private DestructableWallController destructableWallController;

	public SecretRoomEntrance(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEnemyFactory enemyFactory,
			IEventFactory eventFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem,
			IGameStateFactory gameStateFactory,
			RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.enemyFactory = enemyFactory;
		this.eventFactory = eventFactory;

		e1 = new EnemyRespawnComponent(respawnSystem);

		mapLocation.addEntry(7, 2, 5, 24, 8, 9, 6, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(2, 5, 0, true, false, false, false, true, true, true);

		chestController = new ChestController(8, 2, true, true, roomFactory, respawnSystem, (chestX, chestY)-> {
			gameStateFactory.createChestItemAcquireState(50, 0, (int)chestX + 8, (int)chestY + 8);
			mapDisplayEntryComponent.chestOpened();
		});
		destructableWallController = new DestructableWallController(roomFactory, 0, 3 * 16, 0, ()-> {
			mapDisplayEntryComponent.setLeftPath(true);
		});
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		chestController.check(offsetX, offsetY, true);
		destructableWallController.check(offsetX, offsetY);

		roomFactory.createPurpleCrystal(4 * 16 + offsetX, 6 * 16 + offsetY);
		roomFactory.createPurpleCrystal(5 * 16 + offsetX, 6 * 16 + offsetY);

		e1.create((callback)-> {
			enemyFactory.createMiniMoldorm(4 * 16 + 4 + offsetX, 3 * 16 + 3 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});

		eventFactory.createWhenEnemiesDefeated(()-> {
			chestController.trigger();
		});

		mapDisplayEntryComponent.setVisited();
	}
}
