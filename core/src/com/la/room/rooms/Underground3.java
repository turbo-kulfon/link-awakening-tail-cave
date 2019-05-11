package com.la.room.rooms;

import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.transition.IMapLocation;

public class Underground3 implements IRoom {
	private int mapData[][] = new int[][] {
		{102, 100, 123, 100, 100, 123, 100, 100, 123, 100},
		{102, 100, 118, 100, 100, 123, 100, 100, 118, 100},
		{102, 100, 119, 100, 100, 118, 100, 100, 119, 100},
		{101, 101, 114, 115, 114, 119, 114, 115, 114, 100},
		{102, 100, 115, 116, 115, 100, 115, 116, 115, 100},
		{102, 100, 100, 100, 100, 100, 100, 100, 100, 100},
		{102, 117, 117, 100, 117, 100, 100, 100, 100, 100},
		{101, 101, 101, 101, 101, 101, 101, 101, 101, 101},
	};

	private IRoomCreator roomCreator;

	public Underground3(IRoomCreator roomCreator, IMapLocation mapLocation) {
		this.roomCreator = roomCreator;

		mapLocation.addEntry(52, 0, 2, -1, 53, -1, -1, 4*16, 8, true, false);
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
	}
}
