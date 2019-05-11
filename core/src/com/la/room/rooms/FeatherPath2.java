package com.la.room.rooms;

import com.la.factory.IRoomFactory;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.respawn.RespawnSystem;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.respawn.FlyingHeartController;
import com.la.room.transition.IMapLocation;

public class FeatherPath2 implements IRoom {
	private int mapData[][] = new int[][]{
		{22, 22, 22, 11,  0, 12, 22, 22, 22, 22},
		{22, 22, 22, 11,  0, 12, 22, 22, 22, 22},
		{22, 22, 22, 11,  0, 12, 22, 22, 22, 22},
		{22, 22, 22, 11,  0, 12, 22, 22, 22, 22},
		{22, 22, 22, 11,  0, 12, 22, 22, 22, 22},
		{22, 22, 22, 11,  0, 12, 22, 22, 22, 22},
		{22, 22, 22, 11,  0, 12, 22, 22, 22, 22},
		{22, 22, 22, 11,  0, 12, 22, 22, 22, 22},
	};

	private IRoomCreator roomCreator;
	private MapDisplayEntryComponent mapDisplayEntryComponent;
	private FlyingHeartController flyingHeartController;

	public FeatherPath2(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem,
			RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;
	
		mapLocation.addEntry(22, 0, 5, -1, -1, 23, 21, 0, 0, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(0, 5, 0, false, false, false, false, false, true, true);

		flyingHeartController = new FlyingHeartController(4 * 16 + 4, 4 * 16 + 8, roomFactory, respawnSystem);
	}
	
	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		flyingHeartController.check(offsetX, offsetY);
	
		mapDisplayEntryComponent.setVisited();
	}
}
