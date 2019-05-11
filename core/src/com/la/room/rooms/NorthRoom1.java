package com.la.room.rooms;

import com.engine.sound.SoundSystem;
import com.la.event.AutoDoorEvent.AutoDoorEventCallback;
import com.la.factory.IEnemyFactory;
import com.la.factory.IEventFactory;
import com.la.factory.IRoomFactory;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.transition.IMapLocation;

public class NorthRoom1 implements IRoom {
	private int mapData[][] = new int[][]{
		{18, 10, 10, 10, 10, 10, 10, 10, 10, 10},
		{ 3,  2,  0,  0,  0,  0,  0,  0,  0,  0},
		{23,  1,  1,  1, 51,  1, 51,  1,  1,  1},
		{24,  1,  1,  1,  1, 51,  1, 51,  1,  1},
		{ 3,  0,  0,  0,  0,  0,  0,  0,  0,  0},
		{11, 41, 92, 92, 41, 41, 41, 41, 41, 41},
		{11,  2,  0,  0,  1,  1,  1,  1,  1,  1},
		{20, 13, 13,  6, 29, 30,  6, 13, 13, 13},
	};

	private SoundSystem soundSystem;
	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IEnemyFactory enemyFactory;
	private IEventFactory eventFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;

	private boolean openDoor;

	public NorthRoom1(
			SoundSystem soundSystem,
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEnemyFactory enemyFactory,
			IEventFactory eventFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem) {
		this.soundSystem = soundSystem;
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.eventFactory = eventFactory;
		this.enemyFactory = enemyFactory;

		mapLocation.addEntry(10, 2, 3, 12, 11, -1, 9, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(2, 3, 0, false, false, false, true, true, false, true);
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		openDoor = false;
		roomCreator.createRoom(mapData, offsetX, offsetY);
		roomFactory.createDoor(0 + offsetX, 2 * 16 + 8 + offsetY, 0);
		roomFactory.createMovingBlock(2 * 16 + offsetX, 3 * 16 + offsetY, ()-> {
			openDoor = true;
			soundSystem.secretSolved();
		});
		roomFactory.createOwlStatue(3 * 16 + offsetX, 4 + offsetY, 0,
			  "If there is a|"
			+ "door that you|"
			+ "can't open, move|"
			+ "a stone block.");

		enemyFactory.createSpark(7 * 16 + offsetX, 3 * 16 + 15 + offsetY, true, 1);

		eventFactory.createAutoDoorEvent(new AutoDoorEventCallback() {
			@Override
			public void onOpen() {
				mapDisplayEntryComponent.setLeftPath(true);
			}
			@Override
			public void onClose() {
				mapDisplayEntryComponent.setLeftPath(false);
			}
			@Override
			public boolean openCondition() {
				return openDoor;
			}
		});

		mapDisplayEntryComponent.setVisited();
	}
}
