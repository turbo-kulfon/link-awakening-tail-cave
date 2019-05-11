package com.la.event;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.event_system.IEvent;
import com.la.factory.IRoomFactory;

public class DropKeyWhenEnemiesDefeated implements IEvent {
	public interface Dependency {
		void keyTaken();
	}

	private int keyDropX, keyDropY, dungeonID;
	private IAspectSystem aspectSystem;
	private IRoomFactory roomFactory;
	private Dependency dependency;

	private boolean isKeyTaken, keyDropped;

	public DropKeyWhenEnemiesDefeated(
			int keyDropX, int keyDropY, int dungeonID,
			IAspectSystem aspectSystem,
			IRoomFactory roomFactory,
			Dependency dependency) {
		this.keyDropX = keyDropX;
		this.keyDropY = keyDropY;
		this.aspectSystem = aspectSystem;
		this.roomFactory = roomFactory;
		this.dependency = dependency;
	}

	@Override
	public void update() {
		if(isKeyTaken == false && keyDropped == false) {
			if(aspectSystem.getAspectSize(AspectType.ENEMY_TAG) == 0) {
				keyDropped = true;
				roomFactory.createSmallKey(keyDropX, keyDropY, dungeonID, 100, ()-> {
					dependency.keyTaken();
					isKeyTaken = true;
				});
			}
		}
	}
	@Override
	public boolean shouldRemove() {
		return isKeyTaken == true;
	}
}
