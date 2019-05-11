package com.la.room.rooms;

import com.la.event.AutoDoorEvent.AutoDoorEventCallback;
import com.la.factory.IEventFactory;
import com.la.factory.IRoomFactory;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.transition.IMapLocation;

public class FullMoonCelloInstrumentRoom implements IRoom {
	private int mapData[][] = new int[][]{
		{18, 10, 19, 15, 10, 10, 14, 18, 10, 19},
		{11,  2, 12, 50,  1,  1, 50, 11,  2, 12},
		{11,  0, 12,  1,  1,  1,  1, 11,  0, 12},
		{11,  0, 12, 50,  1,  1, 50, 11,  0, 12},
		{11,  0, 15, 10,  8,  8, 10, 14,  0, 12},
		{11,  0,  0,  0,  1,  1,  0,  0,  0, 12},
		{11,  2,  0,  0,  1,  1,  0,  0,  2, 12},
		{20, 13, 13, 13, 29, 30, 13, 13, 13, 21},
	};

	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IEventFactory eventFactory;
	private MapDisplayEntryComponent mapDisplayEntryComponent;

	public FullMoonCelloInstrumentRoom(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEventFactory eventFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem) {
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.eventFactory = eventFactory;

		mapLocation.addEntry(18, 6, 2,  -1, -1, -1, 17, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(6, 2, 0, false, false, false, false, false, false, true);
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		roomFactory.createFullMoonCello(5 * 16 + offsetX, 2 * 16 + 8 + offsetY);
		roomFactory.createDoor(4 * 16 + 8 + offsetX, 7 * 16 + offsetY, 3);

		eventFactory.createAutoDoorEvent(new AutoDoorEventCallback() {
			@Override
			public boolean openCondition() {
				return false;
			}
			@Override
			public void onOpen() {
			}
			@Override
			public void onClose() {
				mapDisplayEntryComponent.setDownPath(false);
			}
		});

		mapDisplayEntryComponent.setVisited();
	}
}
