package com.la.event;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.event_system.IEvent;
import com.engine.sound.SoundSystem;

public class WhenEnemiesDefeatedEvent implements IEvent {
	public interface EnemiesDefeatedCallback {
		void onEnemiesDefeated();
	}

	private SoundSystem soundSystem;
	private IAspectSystem aspectSystem;
	private EnemiesDefeatedCallback callback;

	private boolean remove;

	public WhenEnemiesDefeatedEvent(
			SoundSystem soundSystem,
			IAspectSystem aspectSystem,
			EnemiesDefeatedCallback callback) {
		this.soundSystem = soundSystem;
		this.aspectSystem = aspectSystem;
		this.callback = callback;
	}

	@Override
	public void update() {
		if(remove == false) {
			if(aspectSystem.getAspectSize(AspectType.ENEMY_TAG) == 0) {
				remove = true;
				callback.onEnemiesDefeated();
				soundSystem.secretSolved();
			}
		}
	}

	@Override
	public boolean shouldRemove() {
		return remove == true;
	}
}
