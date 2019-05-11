package com.la.room.respawn;

import com.la.factory.IRoomFactory;

public class KeyLockController {
	public interface KeyLockControllerCallback {
		void onOpen();
	}

	private IRoomFactory roomFactory;
	private int x, y, dungeonID;
	private boolean opened;
	private KeyLockControllerCallback callback;

	public KeyLockController(IRoomFactory roomFactory, int x, int y, int dungeonID, KeyLockControllerCallback callback) {
		this.roomFactory = roomFactory;
		this.x = x;
		this.y = y;
		this.dungeonID = dungeonID;
		this.callback = callback;
	}

	public void check(int offsetX, int offsetY) {
		if(opened == false) {
			roomFactory.createKeyLock(x + offsetX, y + offsetY, dungeonID, ()-> {
				opened = true;
				callback.onOpen();
			});
		}
	}
}
