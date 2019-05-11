package com.la.event;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.event_system.IEvent;
import com.la.factory.IRoomFactory;
import com.la.game_objects.Chest.ChestCallback;

public class ShowChestWhenEnemiesDefeated implements IEvent {
	private int chestX, chestY;
	private IAspectSystem aspectSystem;
	private IRoomFactory roomFactory;
	private ChestCallback callback;

	private boolean chestShow;

	public ShowChestWhenEnemiesDefeated(
			int chestX, int chestY,
			IAspectSystem aspectSystem,
			IRoomFactory roomFactory,
			ChestCallback callback) {
		this.chestX = chestX;
		this.chestY = chestY;
		this.aspectSystem = aspectSystem;
		this.roomFactory = roomFactory;
		this.callback = callback;
	}

	@Override
	public void update() {
		if(chestShow == false) {
			if(aspectSystem.getAspectSize(AspectType.ENEMY_TAG) == 0) {
				roomFactory.createChest(chestX, chestY, false, true, callback);
				chestShow = true;
			}
		}
	}

	@Override
	public boolean shouldRemove() {
		return chestShow == true;
	}
}
