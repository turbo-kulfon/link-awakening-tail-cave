package com.la.room.rooms;

import com.la.factory.IEnemyFactory;
import com.la.respawn.RespawnSystem;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.respawn.EnemyRespawnComponent;
import com.la.room.transition.IMapLocation;

public class Underground1 implements IRoom {
	private int mapData[][] = new int[][] {
		{101, 101, 120, 101, 101, 101, 101, 101, 101, 101},
		{102, 100, 120, 100, 100, 100, 114, 115, 114, 100},
		{102, 100, 120, 100, 100, 100, 115, 116, 115, 100},
		{102, 100, 120, 100, 100, 100, 100, 100, 100, 100},
		{101, 101, 121, 101, 101, 100, 101, 101, 121, 101},
		{110, 111, 120, 100, 100, 100, 100, 100, 120, 102},
		{110, 111, 122, 100, 100, 100, 100, 100, 122, 102},
		{110, 110, 113, 113, 113, 113, 113, 113, 113, 113},
	};

	private IRoomCreator roomCreator;
	private IEnemyFactory enemyFactory;
	private EnemyRespawnComponent e1;

	public Underground1(
			IRoomCreator roomCreator,
			IEnemyFactory enemyFactory,
			IMapLocation mapLocation,
			RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;
		this.enemyFactory = enemyFactory;

		e1 = new EnemyRespawnComponent(respawnSystem);

		mapLocation.addEntry(50, 0, 0, -1, 51, 21, -1, 2*16 + 4, 8, true, false);
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);

		e1.create((callback)-> {
			enemyFactory.createGoomba(4 * 16 + offsetX, 6 * 16 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
	}
}
