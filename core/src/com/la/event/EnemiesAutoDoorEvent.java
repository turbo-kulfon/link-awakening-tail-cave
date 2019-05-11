package com.la.event;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.event_system.IEvent;
import com.engine.sound.SoundSystem;
import com.la.aspects.AutoDoor;
import com.la.game_objects.link.ILinkData;

public class EnemiesAutoDoorEvent implements IEvent {
	public interface EnemiesAutoDoorEventCallback {
		void onOpen();
		void onClose();
	}

	private SoundSystem soundSystem;
	private IAspectSystem aspectSystem;
	private ILinkData linkData;
	private EnemiesAutoDoorEventCallback callback;
	private boolean remove, closed;

	public EnemiesAutoDoorEvent(
			SoundSystem soundSystem,
			IAspectSystem aspectSystem,
			ILinkData linkData,
			EnemiesAutoDoorEventCallback callback) {
		this.soundSystem = soundSystem;
		this.aspectSystem = aspectSystem;
		this.linkData = linkData;
		this.callback = callback;
	}

	@Override
	public void update() {
		if(remove == false) {
			if(aspectSystem.getAspectSize(AspectType.ENEMY_TAG) == 0) {
				aspectSystem.forEachAspect(AspectType.AUTO_DOOR, (id, aspect)-> {
					AutoDoor convert = (AutoDoor) aspect;
					convert.open();
				});
				callback.onOpen();
				if(closed == true) {
					soundSystem.doorOpen();
				}
				soundSystem.secretSolved();
				remove = true;
			}
			else {
				if(closed == false) {
					if(linkData.getCenterX() > 21 && linkData.getCenterX() < 160-21 &&
					   linkData.getCenterY() > 21 && linkData.getCenterY() < 128-21) {
						aspectSystem.forEachAspect(AspectType.AUTO_DOOR, (id, aspect)-> {
							AutoDoor convert = (AutoDoor) aspect;
							convert.close();
						});
						closed = true;
						callback.onClose();
						linkData.setLastPosition();
						soundSystem.doorClose();
					}
				}
			}
		}
	}
	@Override
	public boolean shouldRemove() {
		return remove;
	}
}
