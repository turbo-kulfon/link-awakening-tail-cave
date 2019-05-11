package com.la.room.respawn;

import com.la.factory.IRoomFactory;
import com.la.game_objects.DestructibleWall.DestructibleWallCallback;

public class DestructableWallController {
	public interface DestructableWallControllerCallback {
		void onDestroy();
	}

	private IRoomFactory roomFactory;
	private int x, y, direction;

	boolean destroyed = false;

	private DestructableWallControllerCallback callback;

	public DestructableWallController(
			IRoomFactory roomFactory,
			int x, int y, int direction,
			DestructableWallControllerCallback callback) {
		this.roomFactory = roomFactory;
		this.x = x;
		this.y = y;
		this.direction = direction;
		this.callback = callback;
	}

	public void check(int offsetX, int offsetY) {
		if(destroyed == true) {
			roomFactory.createDestroyedWallHorizontal(x + offsetX, y + offsetY);
		}
		else {
			roomFactory.createDestructableWall(x + offsetX, y + offsetY, direction, new DestructibleWallCallback() {
				@Override
				public void onWallHit(float x, float y) {
				}
				@Override
				public void onDestroy() {
					destroyed = true;
					callback.onDestroy();
				}
			});
		}
	}
}
