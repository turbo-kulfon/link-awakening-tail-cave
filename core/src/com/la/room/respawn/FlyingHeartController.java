package com.la.room.respawn;

import com.la.factory.IRoomFactory;
import com.la.respawn.RespawnElement.RespawnElementCallback;
import com.la.respawn.RespawnElement.RespawnType;
import com.la.respawn.RespawnSystem;

public class FlyingHeartController {
	private IRoomFactory roomFactory;
	private float x, y;
	private boolean taken;

	public FlyingHeartController(float x, float y, IRoomFactory roomFactory, RespawnSystem respawnSystem) {
		this.x = x;
		this.y = y;
		this.roomFactory = roomFactory;
		respawnSystem.createComponent(new RespawnElementCallback() {
			@Override
			public boolean respawnTypeMatch(RespawnType respawnType) {
				return RespawnType.TRANSITION == respawnType;
			}
			@Override
			public void onRespawn() {
				taken = false;
			}
		});
	}

	public void check(float offsetX, float offsetY) {
		if(taken == false) {
			roomFactory.createFlyingSmallHeart(x + offsetX, y + offsetY, ()-> {
				taken = true;
			});
		}
	}
}
