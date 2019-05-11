package com.la.room.transition;

public interface IMapLocation {
	public interface MapLocationComponent {
		void setDeepWalk(boolean value);
	}
	public interface MapTransitionCallback {
		void onMapTransitionStart();
		void onMapTransitionEnd();
	}

	public interface RoomData {
		int getX();
		int getY();
		int getRoomID();
		boolean isPlatform();
		boolean isDeepWalk();

		float getLinkDefaultX();
		float getLinkDefaultY();

		void onMapTransitionStart();
		void onMapTransitionEnd();
	}

	MapLocationComponent addEntry(
		int roomID,
		int xMap, int yMap,
		int leftRoomID, int rightRoomID, int upRoomID, int downRoomID,
		float linkDefaultX, float linkDefaultY,
		boolean isPlatform, boolean deepWalk);
	MapLocationComponent addEntry(
		int roomID,
		int xMap, int yMap,
		int leftRoomID, int rightRoomID, int upRoomID, int downRoomID,
		float linkDefaultX, float linkDefaultY,
		boolean isPlatform, boolean deepWalk, MapTransitionCallback mapTransitionCallback);

	void setPosition(int roomID);

	void moveLeft();
	void moveRight();
	void moveDown();
	void moveUp();

	RoomData getCurrentRoomData();
}
