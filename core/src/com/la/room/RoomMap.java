package com.la.room;

import java.util.HashMap;
import java.util.Map;

public class RoomMap implements IRoomMap {
	private Map<Integer, IRoom> rooms = new HashMap<>();

	@Override
	public void addRoom(IRoom room, int id) {
		rooms.put(id, room);
	}
	@Override
	public void createRoom(int roomID, int xOffset, int yOffset) {
		rooms.get(roomID).createRoom(xOffset, yOffset);
	}
}
