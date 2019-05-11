package com.la.room.transition;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MapLocation implements IMapLocation {
	class Entry {
		public RoomData roomData;
		public int connectedRooms[];

		public Entry(RoomData roomData, int[] connectedRooms) {
			this.roomData = roomData;
			this.connectedRooms = connectedRooms;
		}
	}

	private Map<Integer, Entry> entries = new HashMap<>();
	private int currentRoomID;

	public MapLocation(int currentRoomID) {
		this.currentRoomID = currentRoomID;
	}

	@Override
	public MapLocationComponent addEntry(
			int roomID,
			int xMap, int yMap,
			int leftRoomID, int rightRoomID, int upRoomID, int downRoomID,
			float linkDefaultX, float linkDefaultY,
			boolean isPlatform, boolean deepWalk) {
		AtomicBoolean deep = new AtomicBoolean(deepWalk);
		entries.put(roomID, new Entry(
			new RoomData() {
				@Override
				public int getX() {
					return xMap;
				}
				@Override
				public int getY() {
					return yMap;
				}
				@Override
				public int getRoomID() {
					return roomID;
				}
				@Override
				public boolean isPlatform() {
					return isPlatform;
				}
				@Override
				public boolean isDeepWalk() {
					return deep.get();
				}
				@Override
				public float getLinkDefaultX() {
					return linkDefaultX;
				}
				@Override
				public float getLinkDefaultY() {
					return linkDefaultY;
				}
				@Override public void onMapTransitionStart() {}
				@Override public void onMapTransitionEnd() {}
			},
			new int[] {
				leftRoomID,
				rightRoomID,
				upRoomID,
				downRoomID
			})
		);
		return new MapLocationComponent() {
			@Override
			public void setDeepWalk(boolean value) {
				deep.set(value);
			}
		};
	}
	@Override
	public MapLocationComponent addEntry(
			int roomID,
			int xMap, int yMap,
			int leftRoomID, int rightRoomID, int upRoomID, int downRoomID,
			float linkDefaultX, float linkDefaultY,
			boolean isPlatform, boolean deepWalk, MapTransitionCallback mapTransitionCallback) {
		AtomicBoolean deep = new AtomicBoolean(deepWalk);
		entries.put(roomID, new Entry(
			new RoomData() {
				@Override
				public int getX() {
					return xMap;
				}
				@Override
				public int getY() {
					return yMap;
				}
				@Override
				public int getRoomID() {
					return roomID;
				}
				@Override
				public boolean isPlatform() {
					return isPlatform;
				}
				@Override
				public boolean isDeepWalk() {
					return deep.get();
				}
				@Override
				public float getLinkDefaultX() {
					return linkDefaultX;
				}
				@Override
				public float getLinkDefaultY() {
					return linkDefaultY;
				}
				@Override
				public void onMapTransitionStart() {
					mapTransitionCallback.onMapTransitionStart();
				}
				@Override
				public void onMapTransitionEnd() {
					mapTransitionCallback.onMapTransitionEnd();
				}
			},
			new int[] {
				leftRoomID,
				rightRoomID,
				upRoomID,
				downRoomID
			})
		);
		return new MapLocationComponent() {
			@Override
			public void setDeepWalk(boolean value) {
				deep.set(value);
			}
		};
	}

	@Override
	public void setPosition(int roomID) {
		currentRoomID = roomID;
	}

	@Override
	public void moveLeft() {
		Entry entry = entries.get(currentRoomID);
		int nextRoom = entry.connectedRooms[0];
		currentRoomID = nextRoom;
	}
	@Override
	public void moveRight() {
		Entry entry = entries.get(currentRoomID);
		int nextRoom = entry.connectedRooms[1];
		currentRoomID = nextRoom;
	}
	@Override
	public void moveUp() {
		Entry entry = entries.get(currentRoomID);
		int nextRoom = entry.connectedRooms[2];
		currentRoomID = nextRoom;
	}
	@Override
	public void moveDown() {
		Entry entry = entries.get(currentRoomID);
		int nextRoom = entry.connectedRooms[3];
		currentRoomID = nextRoom;
	}

	@Override
	public RoomData getCurrentRoomData() {
		return entries.get(currentRoomID).roomData;
	}
}
