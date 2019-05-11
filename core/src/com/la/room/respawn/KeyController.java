package com.la.room.respawn;

import com.la.factory.IRoomFactory;
import com.la.game_objects.pickup.SmallKeyCallback;

public class KeyController {
	public interface KeyControllerCallback {
		void onTaken();
	}

	private IRoomFactory roomFactory;
	private SmallKeyCallback callback;
	private int x, y;
	private int dungeonID;
	private int height;
	private boolean taken = false;
	private boolean hidden = false;
	private boolean triggered = false;

	public KeyController(
			int x, int y, int dungeonID, int height, boolean hidden,
			IRoomFactory roomFactory, KeyControllerCallback callbackArg) {
		this.x = x;
		this.y = y;
		this.dungeonID = dungeonID;
		this.height = height;
		this.hidden = hidden;

		this.roomFactory = roomFactory;
		this.callback = ()-> {
			taken = true;
			callbackArg.onTaken();
		};
	}

	public void check(int offsetX, int offsetY) {
		triggered = false;
		if(taken == false && hidden == false) {
			roomFactory.createSmallKey(x * 16 + 8 + offsetX, y * 16 + 4 + offsetY, dungeonID, height, callback);
			triggered = true;
		}
	}
	public void trigger() {
		if(triggered == false && taken == false) {
			roomFactory.createSmallKey(x * 16 + 8, y * 16 + 4, dungeonID, height, callback);
			triggered = true;
		}
	}
}
