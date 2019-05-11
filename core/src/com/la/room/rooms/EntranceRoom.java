package com.la.room.rooms;

import com.la.factory.IRoomFactory;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.teleport_sys.TeleportSystem;
import com.la.room.transition.IMapLocation;
import com.la.room.transition.IRoomTransition;

public class EntranceRoom implements IRoom {
	private int mapData[][] = new int[][] {
		{18, 10, 10, 14,  0,  0, 15, 10, 10, 19},
		{11,  2,  1,  1,  1,  1,  1,  1,  2, 12},
		{14,  1, 50, 50,  0,  0, 50, 50,  1, 12},
		{ 0,  1, 50,  0,  0,  0,  0, 50,  1, 12},
		{ 0,  1, 50,  0,  0,  0,  0, 50,  1, 12},
		{17,  1,  0,  0,  0,  0,  0,  0,  1, 12},
		{11,  2,  1,  0,  0,  0,  0,  1,  2, 12},
		{20, 13, 13, 13, 13, 13, 13, 13, 13, 21},
	};

	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IRoomTransition roomTransition;
	private TeleportSystem teleportSystem;
	private MapDisplayEntryComponent mapDisplayEntryComponent;

	public EntranceRoom(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IRoomTransition roomTransition,
			IMapLocation mapLocation,
			TeleportSystem teleportSystem,
			IMapDisplaySystem mapDisplaySystem) {
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.teleportSystem = teleportSystem;
		this.roomTransition = roomTransition;

		mapLocation.addEntry(0, 3, 7, 1, -1, 2, -1, 80 - 5, 64 - 3, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(3, 7, 0, false, false, false, true, false, true, false);
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		teleportSystem.createEntranceTeleport(offsetX, offsetY);
		
		mapDisplayEntryComponent.setVisited();
	}
}
