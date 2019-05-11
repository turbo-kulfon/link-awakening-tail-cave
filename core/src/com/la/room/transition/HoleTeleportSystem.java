package com.la.room.transition;

public class HoleTeleportSystem {
	private IRoomTransition roomTransition;

	private int targetRoomID = -1, posX, posY;

	public HoleTeleportSystem(IRoomTransition roomTransition) {
		this.roomTransition = roomTransition;
	}

	public void initialize(int roomID) {
		targetRoomID = roomID;
	}
	public void teleport() {
		if(targetRoomID != -1) {
			roomTransition.gotoRoom(targetRoomID);
			targetRoomID = -1;
		}
	}
}
