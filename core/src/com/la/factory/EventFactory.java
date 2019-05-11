package com.la.factory;

import com.engine.aspect.IAspectSystem;
import com.engine.event_system.IEvent;
import com.engine.event_system.IEventSystem;
import com.engine.sound.SoundSystem;
import com.la.event.DropKeyWhenEnemiesDefeated;
import com.la.event.ShowChestWhenEnemiesDefeated;
import com.la.event.WhenEnemiesDefeatedEvent;
import com.la.event.AutoDoorEvent;
import com.la.event.AutoDoorEvent.AutoDoorEventCallback;
import com.la.event.DropKeyWhenEnemiesDefeated.Dependency;
import com.la.event.EnemiesAutoDoorEvent.EnemiesAutoDoorEventCallback;
import com.la.event.WhenEnemiesDefeatedEvent.EnemiesDefeatedCallback;
import com.la.event.EnemiesAutoDoorEvent;
import com.la.game_objects.Chest.ChestCallback;
import com.la.game_objects.link.ILinkData;

public class EventFactory implements IEventFactory {
	private SoundSystem soundSystem;
	private IEventSystem eventSystem;
	private IAspectSystem aspectSystem;
	private IRoomFactory roomFactory;
	private ILinkData linkData;

	public EventFactory(
			SoundSystem soundSystem,
			IEventSystem eventSystem,
			IAspectSystem aspectSystem,
			IRoomFactory roomFactory,
			ILinkData linkData) {
		this.soundSystem = soundSystem;
		this.eventSystem = eventSystem;
		this.aspectSystem = aspectSystem;
		this.roomFactory = roomFactory;
		this.linkData = linkData;
	}

	@Override
	public IEvent createWhenEnemiesDefeated(EnemiesDefeatedCallback callback) {
		IEvent event = new WhenEnemiesDefeatedEvent(soundSystem, aspectSystem, callback);
		addToSystem(event);
		return event;
	}
	@Override
	public IEvent createDropKeyWhenEnemiesDefeated(int keyDropX, int keyDropY, int dungeonID, Dependency dependency) {
		IEvent event = new DropKeyWhenEnemiesDefeated(keyDropX, keyDropY, dungeonID, aspectSystem, roomFactory, dependency);
		addToSystem(event);
		return event;
	}
	@Override
	public IEvent createShowChestWhenEnemiesDefeated(int chestX, int chestY, ChestCallback callback) {
		IEvent event = new ShowChestWhenEnemiesDefeated(chestX, chestY, aspectSystem, roomFactory, callback);
		addToSystem(event);
		return event;
	}
	@Override
	public IEvent createAutoDoorEvent(AutoDoorEventCallback callback) {
		IEvent event = new AutoDoorEvent(soundSystem, aspectSystem, linkData, callback);
		addToSystem(event);
		return event;
	}
	@Override
	public IEvent createEnemiesAutoDoor(EnemiesAutoDoorEventCallback callback) {
		IEvent event = new EnemiesAutoDoorEvent(soundSystem, aspectSystem, linkData, callback);
		addToSystem(event);
		return event;
	}

	private void addToSystem(IEvent event) {
		eventSystem.addEvent(event);
	}
}
