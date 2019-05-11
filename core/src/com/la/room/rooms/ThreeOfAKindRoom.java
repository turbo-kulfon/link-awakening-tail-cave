package com.la.room.rooms;

import com.engine.sound.SoundSystem;
import com.la.factory.IEnemyFactory;
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
import com.la.three_of_a_kind_system.ThreeOfAKindCore.ThreeOfAKindDependency;
import com.la.three_of_a_kind_system.ThreeOfAKindSystem;

public class ThreeOfAKindRoom implements IRoom {
	private int mapData[][] = new int[][]{
		{22, 18, 10, 10, 10, 10, 10, 10, 19, 22},
		{18, 14,  7,  7,  7,  7,  7,  7, 15, 19},
		{11,  2,  7,  7,  7,  7,  7,  7,  7, 12},
		{11,  7,  7,  7,  7,  7,  7,  7,  7, 12},
		{11,  7,  7,  7,  7,  7,  7,  7,  7, 12},
		{11,  7,  7,  7,  7,  7,  7,  7,  7, 12},
		{20, 17,  7,  7,  7,  7,  7,  7, 16, 21},
		{22, 20, 13, 17,  0,  0, 16, 13, 21, 22},
	};

	private SoundSystem soundSystem;
	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IEnemyFactory enemyFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private ChestController chestController;
	private EnemyRespawnComponent e1, e2, e3;

	public ThreeOfAKindRoom(
			SoundSystem soundSystem,
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEnemyFactory enemyFactory,
			IMapLocation mapLocation,
			IGameStateFactory gameStateFactory,
			IMapDisplaySystem mapDisplaySystem,
			RespawnSystem respawnSystem) {
		this.soundSystem = soundSystem;
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.enemyFactory = enemyFactory;

		mapLocation.addEntry(14, 5, 4, -1, -1, -1, 13, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(5, 4, 0, true, false, false, false, false, false, true);

		e1 = new EnemyRespawnComponent(respawnSystem);
		e2 = new EnemyRespawnComponent(respawnSystem);
		e3 = new EnemyRespawnComponent(respawnSystem);

		chestController = new ChestController(8, 2, true, true, roomFactory, respawnSystem, (chestX, chestY)-> {
			gameStateFactory.createChestItemAcquireState(120, 0, (int)chestX + 8, (int)chestY + 8);
			mapDisplayEntryComponent.chestOpened();
		});
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		chestController.check(offsetX, offsetY, true);

		roomFactory.createOwlStatue(5* 16 + offsetX, 4 + offsetY, 0,
				  "Turn aside the|"
				+ "spined ones with|"
				+ "a shield...");

		ThreeOfAKindSystem threeOfAKindSystem = new ThreeOfAKindSystem(soundSystem);
		threeOfAKindSystem.setDependency(new ThreeOfAKindDependency() {
			@Override
			public void onSymbolMismatch() {
				
			}
			@Override
			public void onSymbolMatch() {
				chestController.trigger();
			}
		});

		e1.create((callback)-> {
			enemyFactory.createThreeOfAKind(2 * 16 + offsetX, 2 * 16 + offsetY, threeOfAKindSystem, ()-> {
				callback.blockRespawn();
			});
		});
		e2.create((callback)-> {
			enemyFactory.createThreeOfAKind(7 * 16 + offsetX, 2 * 16 + offsetY, threeOfAKindSystem, ()-> {
				callback.blockRespawn();
			});
		});
		e3.create((callback)-> {
			enemyFactory.createThreeOfAKind(4 * 16 + offsetX, 3 * 16 + offsetY, threeOfAKindSystem, ()-> {
				callback.blockRespawn();
			});
		});

		mapDisplayEntryComponent.setVisited();
	}
}
