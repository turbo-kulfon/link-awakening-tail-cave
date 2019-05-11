package com.la.room.rooms;

import com.engine.sound.SoundSystem;
import com.la.event.AutoDoorEvent.AutoDoorEventCallback;
import com.la.factory.IEnemyFactory;
import com.la.factory.IEventFactory;
import com.la.factory.IRoomFactory;
import com.la.game_objects.enemy.moldorm.Moldorm.MoldormCallback;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.transition.IMapLocation;
import com.la.room.transition.IMapLocation.MapLocationComponent;
import com.la.room.transition.IMapLocation.MapTransitionCallback;

public class DungeonBossRoom implements IRoom {
	private int mapData[][] = new int[][]{
		{18, 10, 10,  5, 27, 28,  5, 10, 10, 19},
		{11, -1, -1,  1,  1,  1,  1, -1, -1, 12},
		{11, -1,  1,  1,  1,  1,  1,  1, -1, 12},
		{11, -1,  1,  1,  1,  1,  1,  1, -1, 12},
		{11, -1,  1,  1,  1,  1,  1,  1, -1, 12},
		{11, -1,  1,  1,  1,  1,  1,  1, -1, 12},
		{11, -1, -1,  1,  1,  1,  1, -1, -1, 12},
		{20, 13, 13, 13, 29, 30, 13, 13, 13, 21},
	};

	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IEnemyFactory enemyFactory;
	private IEventFactory eventFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private MapLocationComponent mapLocationComponent;

	private boolean bossDefeated;

	public DungeonBossRoom(
			SoundSystem soundSystem,
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEnemyFactory enemyFactory,
			IEventFactory eventFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem) {
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.enemyFactory = enemyFactory;
		this.eventFactory = eventFactory;

		mapLocationComponent = mapLocation.addEntry(17, 6, 3,  -1, -1, 18, 16, 0, 0, false, true, new MapTransitionCallback() {
			@Override
			public void onMapTransitionStart() {
				if(bossDefeated == false) {
					soundSystem.stopMusic();
				}
			}
			@Override
			public void onMapTransitionEnd() {
				if(bossDefeated == false) {
					soundSystem.nightmareCombat();
				}
			}
		});
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(6, 3, 0, false, false, true, false, false, true, true);
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);

		roomFactory.createTeleportHole(1 * 16 + offsetX, 1 * 16 + offsetY, -1, 52);
		roomFactory.createTeleportHole(2 * 16 + offsetX, 1 * 16 + offsetY, 2, 52);
		roomFactory.createTeleportHole(1 * 16 + offsetX, 2 * 16 + offsetY, -1, 52);
		roomFactory.createTeleportHole(1 * 16 + offsetX, 3 * 16 + offsetY, -1, 52);
		roomFactory.createTeleportHole(1 * 16 + offsetX, 4 * 16 + offsetY, -1, 52);
		roomFactory.createTeleportHole(1 * 16 + offsetX, 5 * 16 + offsetY, -1, 52);
		roomFactory.createTeleportHole(1 * 16 + offsetX, 6 * 16 + offsetY, -1, 52);
		roomFactory.createTeleportHole(2 * 16 + offsetX, 6 * 16 + offsetY, 1, 52);

		roomFactory.createTeleportHole(8 * 16 + offsetX, 1 * 16 + offsetY, -1, 52);
		roomFactory.createTeleportHole(7 * 16 + offsetX, 1 * 16 + offsetY, 2, 52);
		roomFactory.createTeleportHole(8 * 16 + offsetX, 2 * 16 + offsetY, -1, 52);
		roomFactory.createTeleportHole(8 * 16 + offsetX, 3 * 16 + offsetY, -1, 52);
		roomFactory.createTeleportHole(8 * 16 + offsetX, 4 * 16 + offsetY, -1, 52);
		roomFactory.createTeleportHole(8 * 16 + offsetX, 5 * 16 + offsetY, -1, 52);
		roomFactory.createTeleportHole(8 * 16 + offsetX, 6 * 16 + offsetY, -1, 52);
		roomFactory.createTeleportHole(7 * 16 + offsetX, 6 * 16 + offsetY, 1, 52);

		roomFactory.createDoor(4 * 16 + 8 + offsetX, offsetY, 2);
		roomFactory.createDoor(4 * 16 + 8 + offsetX, 7 * 16 + offsetY, 3);

		if(bossDefeated == false) {
			enemyFactory.createMoldorm(4 * 16 + 8 + offsetX, 3 * 16 + 8 + offsetY, new MoldormCallback() {
				@Override
				public void onDeath() {
					bossDefeated = true;
					mapLocationComponent.setDeepWalk(false);
					mapDisplayEntryComponent.bossDefeated();
				}
				@Override
				public void drawText(String text) {
					// dupa
				}
			});
		}

		eventFactory.createAutoDoorEvent(new AutoDoorEventCallback() {
			@Override
			public boolean openCondition() {
				return bossDefeated;
			}
			@Override
			public void onOpen() {
				mapDisplayEntryComponent.setUpPath(true);
				mapDisplayEntryComponent.setDownPath(true);
			}
			
			@Override
			public void onClose() {
				mapDisplayEntryComponent.setUpPath(false);
				mapDisplayEntryComponent.setDownPath(false);
			}
		});

		mapDisplayEntryComponent.setVisited();
	}
}
