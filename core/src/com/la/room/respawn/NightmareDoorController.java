package com.la.room.respawn;

import com.la.factory.IRoomFactory;

public class NightmareDoorController {
	public interface NightmareDoorControllerCallback {
		void onOpen();
	}

	private IRoomFactory roomFactory;
	private int x, y, dungeonID;
	private int direction;
	private boolean opened;
	private NightmareDoorControllerCallback callback;

	public NightmareDoorController(IRoomFactory roomFactory, int x, int y, int dungeonID, int direction, NightmareDoorControllerCallback callback) {
		this.roomFactory = roomFactory;
		this.x = x;
		this.y = y;
		this.dungeonID = dungeonID;
		this.direction = direction;
		this.callback = callback;
	}

	public void check(int offsetX, int offsetY) {
		if(opened == false) {
			roomFactory.createNightmareDoor(x + offsetX, y + offsetY, dungeonID, direction, ()-> {
				opened = true;
				callback.onOpen();
			});
		}
	}
}
