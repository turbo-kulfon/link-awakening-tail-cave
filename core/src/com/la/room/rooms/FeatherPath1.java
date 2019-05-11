package com.la.room.rooms;

import com.la.factory.IRoomFactory;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.transition.IMapLocation;
import com.la.room.transition.IRoomTransition;

public class FeatherPath1 implements IRoom {
//	private int mapData[][] = new int[][]{
//		{22, 22, 22, 22, 22, 22, 22, 22, 22, 22},
//		{22, 22, 22, 22, 22, 22, 22, 22, 22, 22},
//		{22, 22, 22, 22, 22, 22, 22, 22, 22, 22},
//		{22, 22, 22, 22, 22, 22, 22, 22, 22, 22},
//		{22, 22, 22, 22, 22, 22, 22, 22, 22, 22},
//		{22, 22, 22, 22, 22, 22, 22, 22, 22, 22},
//		{22, 22, 22, 22, 22, 22, 22, 22, 22, 22},
//		{22, 22, 22, 22, 22, 22, 22, 22, 22, 22},
//	};
	private int mapData[][] = new int[][]{
		{22, 22, 22, 11,  0, 12, 22, 22, 22, 22},
		{22, 22, 22, 11,  0, 12, 22, 22, 22, 22},
		{22, 22, 22, 11,  0, 12, 22, 22, 22, 22},
		{22, 18, 10, 14,  0, 15, 10, 19, 22, 22},
		{22, 11,  2,  0,  0,  0,  2, 12, 22, 22},
		{22, 11,  0,  0,  0,  0,  0, 12, 22, 22},
		{22, 11,  2,  0,  0,  0,  2, 12, 22, 22},
		{22, 20, 13, 13, 13, 13, 13, 21, 22, 22},
	};

	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IRoomTransition roomTransition;
	private MapDisplayEntryComponent mapDisplayEntryComponent;

	public FeatherPath1(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IRoomTransition roomTransition,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem) {
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.roomTransition = roomTransition;

		mapLocation.addEntry(21, 0, 6, -1, -1, 22, -1, 4 * 16 + 4, 5 * 16 + 4, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(0, 6, 0, false, false, false, false, false, true, false);
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);

		roomFactory.createStairs(4*16 + offsetX, 5*16 + offsetY, ()-> {
			roomTransition.gotoRoom(50);
		});
		mapDisplayEntryComponent.setVisited();
	}
}
