package com.la.factory;

import com.engine.event_system.IEvent;
import com.la.event.AutoDoorEvent.AutoDoorEventCallback;
import com.la.event.DropKeyWhenEnemiesDefeated;
import com.la.event.EnemiesAutoDoorEvent.EnemiesAutoDoorEventCallback;
import com.la.event.WhenEnemiesDefeatedEvent.EnemiesDefeatedCallback;
import com.la.game_objects.Chest.ChestCallback;

public interface IEventFactory {
	IEvent createWhenEnemiesDefeated(EnemiesDefeatedCallback callback);
	IEvent createDropKeyWhenEnemiesDefeated(int keyDropX, int keyDropY, int dungeonID, DropKeyWhenEnemiesDefeated.Dependency dependency);
	IEvent createShowChestWhenEnemiesDefeated(int chestX, int chestY, ChestCallback callback);
	IEvent createAutoDoorEvent(AutoDoorEventCallback callback);
	IEvent createEnemiesAutoDoor(EnemiesAutoDoorEventCallback callback);
}
