package com.la.room.rooms;

import com.engine.sound.SoundSystem;
import com.la.event.EnemiesAutoDoorEvent.EnemiesAutoDoorEventCallback;
import com.la.factory.IEnemyFactory;
import com.la.factory.IEventFactory;
import com.la.factory.IRoomFactory;
import com.la.game_objects.enemy.rolling_bones.RollingBonesSystem;
import com.la.game_objects.enemy.rolling_bones.RollingBonesSystem.RollingBonesSystemCallback;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.teleport_sys.TeleportSystem;
import com.la.room.transition.IMapLocation;
import com.la.room.transition.IMapLocation.MapLocationComponent;
import com.la.room.transition.IMapLocation.MapTransitionCallback;

public class MiniBossRoom implements IRoom {
	private int mapData[][] = new int[][] {
		{18, 10, 10,  5, 27, 28,  5, 10, 10, 19},
		{11,  1,  1,  1,  1,  1,  1,  1,  1, 12},
		{ 3,  1,  1,  1,  1,  1,  1,  1,  1, 12},
		{23,  1,  1,  1,  1,  1,  1,  1,  1, 12},
		{24,  1,  1,  1,  1,  1,  1,  1,  1, 12},
		{ 3,  1,  1,  1,  1,  1,  1,  1,  1, 12},
		{11,  1,  1,  1,  1,  1,  1,  1,  1, 12},
		{20, 13, 13, 13, 13, 13, 13, 13, 13, 21},
	};

	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IEnemyFactory enemyFactory;
	private IEventFactory eventFactory;
	private TeleportSystem teleportSystem;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private MapLocationComponent mapLocationComponent;
	private RollingBonesSystem rollingBonesSystem;
	private boolean miniBossDefeated;

	public MiniBossRoom(
			SoundSystem soundSystem,
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEnemyFactory enemyFactory,
			IEventFactory eventFactory,
			TeleportSystem teleportSystem,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem) {
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.enemyFactory = enemyFactory;
		this.eventFactory = eventFactory;
		this.teleportSystem = teleportSystem;

		mapLocationComponent= mapLocation.addEntry(15, 6, 5, 13, -1, 16, -1, 80 - 5, 64 - 3, false, true, new MapTransitionCallback() {
			@Override
			public void onMapTransitionStart() {
				if(miniBossDefeated == false) {
					soundSystem.stopMusic();
				}
			}
			@Override
			public void onMapTransitionEnd() {
				if(miniBossDefeated == false) {
					soundSystem.miniBossMusic();
				}
			}
		});
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(6, 5, 0, false, false, false, true, false, true, false);
		rollingBonesSystem = new RollingBonesSystem(enemyFactory, roomFactory, teleportSystem, mapLocationComponent, new RollingBonesSystemCallback() {
			@Override
			public void onDefeated() {
				miniBossDefeated = true;
				soundSystem.playDungeonMusic();
			}
		});
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		roomFactory.createDoor(0 + offsetX, 3 * 16 + 8 + offsetY, 0);
		roomFactory.createDoor(4 * 16 + 8 + offsetX, 0 + offsetY, 2);
		teleportSystem.createMiniBossTeleport(offsetX, offsetY);
		rollingBonesSystem.createMiniBoss(offsetX, offsetY);

		eventFactory.createEnemiesAutoDoor(new EnemiesAutoDoorEventCallback() {
			@Override
			public void onOpen() {
				mapDisplayEntryComponent.setLeftPath(true);
				mapDisplayEntryComponent.setUpPath(true);
			}
			@Override
			public void onClose() {
				mapDisplayEntryComponent.setLeftPath(false);
				mapDisplayEntryComponent.setUpPath(false);
			}
		});

		mapDisplayEntryComponent.setVisited();
	}
}
