package com.la.room.respawn;

import com.la.factory.IRoomFactory;

public class DoorKeyController {
	public interface DoorKeyControllerCallback {
		void onOpen();
	}

	private IRoomFactory roomFactory;
	private int x, y, dungeonID;
	private int direction;
	private boolean opened;
	private DoorKeyControllerCallback callback;

	public DoorKeyController(IRoomFactory roomFactory, int x, int y, int dungeonID, int direction, DoorKeyControllerCallback callback) {
		this.roomFactory = roomFactory;
		this.x = x;
		this.y = y;
		this.dungeonID = dungeonID;
		this.direction = direction;
		this.callback = callback;
	}

	public void check(int offsetX, int offsetY) {
		if(opened == false) {
			roomFactory.createKeyDoor(x + offsetX, y + offsetY, dungeonID, direction, ()-> {
				opened = true;
				callback.onOpen();
			});
		}
	}
}
