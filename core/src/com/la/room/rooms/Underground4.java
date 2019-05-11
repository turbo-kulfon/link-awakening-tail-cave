package com.la.room.rooms;

import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.transition.IMapLocation;

public class Underground4 implements IRoom {
	private int mapData[][] = new int[][] {
		{100, 100, 100, 100, 100, 101, 101, 120, 101, 101},
		{100, 100, 100, 100, 100, 102, 100, 120, 100, 102},
		{100, 100, 100, 100, 100, 102, 100, 120, 100, 102},
		{100, 114, 115, 114, 100, 101, 101, 121, 101, 101},
		{100, 115, 116, 115, 100, 100, 100, 120, 100, 102},
		{100, 100, 100, 100, 100, 100, 100, 120, 100, 102},
		{100, 100, 100, 100, 117, 100, 117, 122, 117, 102},
		{101, 101, 101, 101, 101, 101, 101, 101, 101, 101},
	};

	private IRoomCreator roomCreator;

	public Underground4(IRoomCreator roomCreator, IMapLocation mapLocation) {
		this.roomCreator = roomCreator;

		mapLocation.addEntry(53, 0, 2, 52, -1, 16, -1, 7*16 + 4, 8, true, false);
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
	}
}
