package com.la.room.rooms;

import com.la.factory.IGameStateFactory;
import com.la.factory.IRoomFactory;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.respawn.RespawnSystem;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.respawn.ChestController;
import com.la.room.transition.IMapLocation;

public class NightmareKeyRoom implements IRoom {
	private int mapData[][] = new int[][]{
		{41,  2,  2, 41, 41, 41,  2,  2, 41, 41},
		{ 0,  0,  0,  0,  0,  0,  0,  0,  0,  0},
		{ 0, 36, 42, 42, 42, 42, 42, 35,  0,  0},
		{42, 34, 18, 10, 10, 10, 19, 33, 42, 42},
		{10, 10, 14, 88, 91, 88, 15, 10, 10, 19},
		{91, 91, 91, 91,  0, 91, 91, 91, 91, 12},
		{13, 17,  0,  0,  0,  0,  0,  0,  0, 12},
		{22, 11,  0,  0,  0,  0,  0,  0,  0, 12},
	};

	private IRoomCreator roomCreator;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private ChestController chestController;

	public NightmareKeyRoom(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IMapLocation mapLocation,
			IGameStateFactory gameStateFactory,
			IMapDisplaySystem mapDisplaySystem,
			RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;

		mapLocation.addEntry(19, 3, 4, 9, 20, -1, 8, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(3, 4, 0, true, true, false, true, true, false, true);

		chestController = new ChestController(4, 1, false, false, roomFactory, respawnSystem, (chestX, chestY)-> {
			gameStateFactory.createChestItemAcquireState(80, 0, (int)chestX + 8, (int)chestY + 8);
			mapDisplayEntryComponent.chestOpened();
			mapDisplayEntryComponent.keyTaken();
		});
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		chestController.check(offsetX, offsetY, false);

		mapDisplayEntryComponent.setVisited();
	}
}
