package com.la.event;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.event_system.IEvent;
import com.engine.sound.SoundSystem;
import com.la.aspects.AutoDoor;
import com.la.game_objects.link.ILinkData;

public class AutoDoorEvent implements IEvent {
	public interface AutoDoorEventCallback {
		void onOpen();
		void onClose();
		boolean openCondition();
	}

	private SoundSystem soundSystem;
	private IAspectSystem aspectSystem;
	private ILinkData linkData;
	private AutoDoorEventCallback callback;
	private boolean remove, closed;

	public AutoDoorEvent(
			SoundSystem soundSystem,
			IAspectSystem aspectSystem,
			ILinkData linkData,
			AutoDoorEventCallback callback) {
		this.soundSystem = soundSystem;
		this.aspectSystem = aspectSystem;
		this.linkData = linkData;
		this.callback = callback;
	}

	@Override
	public void update() {
		if(remove == false) {
			if(callback.openCondition() == true) {
				aspectSystem.forEachAspect(AspectType.AUTO_DOOR, (id, aspect)-> {
					AutoDoor convert = (AutoDoor) aspect;
					convert.open();
				});
				callback.onOpen();
				if(closed == true) {
					soundSystem.doorOpen();
				}
				remove = true;
			}
			else {
				if(closed == false) {
					if(linkData.getCenterX() > 21 && linkData.getCenterX() < 160-20 &&
					   linkData.getCenterY() > 20 && linkData.getCenterY() < 128-20) {
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
