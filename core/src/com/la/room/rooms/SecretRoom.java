package com.la.room.rooms;

import com.la.factory.IRoomFactory;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.transition.IMapLocation;

public class SecretRoom implements IRoom {
	private int mapData[][] = new int[][]{
		{22, 22, 18, 10, 10, 10, 10, 10, 10, 19},
		{22, 18, 14, 88, 88, 88, 88, 88, 88, 12},
		{22, 11, 88, 88,  1,  1,  1, 88, 88, 12},
		{22, 11, 88,  1,  1,  1,  1,  1,  1, 52},
		{22, 11, 88,  1,  1,  1,  1,  1,  1, 12},
		{22, 11, 88, 88,  1,  1,  1, 88, 88, 12},
		{22, 20, 17, 88, 88, 88, 88, 88, 88, 12},
		{22, 22, 20, 13, 13, 13, 13, 13, 13, 21},
	};

	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;

	public SecretRoom(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem) {
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;

		mapLocation.addEntry(24, 1, 5, -1, 7, -1, -1, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(1, 5, 0, false, false, false, false, true, false, false);
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);

		mapDisplayEntryComponent.setVisited();
	}
}
