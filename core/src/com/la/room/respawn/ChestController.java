package com.la.room.respawn;

import com.la.factory.IRoomFactory;
import com.la.game_objects.Chest.ChestCallback;
import com.la.respawn.RespawnComponent;
import com.la.respawn.RespawnElement.RespawnElementCallback;
import com.la.respawn.RespawnElement.RespawnType;
import com.la.respawn.RespawnSystem;

public class ChestController {
	private IRoomFactory roomFactory;
	private ChestCallback chestCallback;
	private int x, y;
	private boolean opened = false;
	private boolean hidden = false;
	private boolean triggered = false;
	private boolean respawn;
	private RespawnComponent respawnComponent;

	public ChestController(
			int x, int y, boolean isHidden, boolean respawn,
			IRoomFactory roomFactory, RespawnSystem respawnSystem, ChestCallback chestCallback) {
		this.x = x;
		this.y = y;
		hidden = isHidden;
		this.roomFactory = roomFactory;
		this.chestCallback = (chestX, chestY)-> {
			opened = true;
			chestCallback.onOpen(chestX, chestY);
		};

		this.respawn = respawn;

		if(respawn == true) {
			respawnComponent = respawnSystem.createComponent(new RespawnElementCallback() {
				@Override
				public boolean respawnTypeMatch(RespawnType respawnType) {
					return respawnType == RespawnType.TRANSITION;
				}
				@Override
				public void onRespawn() {
					hidden = true;
				}
			});
		}
	}

	public void check(int offsetX, int offsetY, boolean hide) {
		triggered = false;
		if(hide == true) {
			hidden = true;
		}
		if(opened == true) {
			roomFactory.createChest(x * 16 + offsetX, y * 16 + offsetY, opened, false, (chestX, chestY)-> {});
			triggered = true;
			if(respawnComponent != null) {
				respawnComponent.remove();
			}
		}
		else {
			if(hidden == false) {
				roomFactory.createChest(x * 16 + offsetX, y * 16 + offsetY, opened, respawn, chestCallback);
				triggered = true;
			}
		}
	}
	public void trigger() {
		if(triggered == false && opened == false) {
			hidden = false;
			roomFactory.createChest(x * 16, y * 16, opened, true, chestCallback);
			triggered = true;
		}
	}
}
