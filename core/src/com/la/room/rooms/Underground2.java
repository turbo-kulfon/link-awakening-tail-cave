package com.la.room.rooms;

import com.la.factory.IEnemyFactory;
import com.la.respawn.RespawnSystem;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.respawn.EnemyRespawnComponent;
import com.la.room.transition.IMapLocation;

public class Underground2 implements IRoom {
	private int mapData[][] = new int[][] {
		{101, 101, 101, 101, 101, 101, 101, 120, 101, 101},
		{100, 114, 115, 114, 100, 100, 100, 120, 100, 102},
		{100, 115, 116, 115, 100, 100, 100, 120, 100, 102},
		{100, 100, 100, 100, 100, 100, 101, 121, 101, 101},
		{101, 121, 100, 101, 101, 100, 100, 120, 112, 110},
		{102, 120, 100, 100, 100, 100, 100, 120, 112, 110},
		{102, 122, 100, 100, 100, 100, 100, 122, 112, 110},
		{113, 113, 113, 113, 113, 113, 113, 113, 110, 110},
	};

	private IRoomCreator roomCreator;
	private IEnemyFactory enemyFactory;

	private EnemyRespawnComponent e1, e2;

	public Underground2(
			IRoomCreator roomCreator,
			IEnemyFactory enemyFactory,
			IMapLocation mapLocation,
			 RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;
		this.enemyFactory = enemyFactory;

		e1 = new EnemyRespawnComponent(respawnSystem);
		e2 = new EnemyRespawnComponent(respawnSystem);

		mapLocation.addEntry(51, 0, 1, 50, -1, 12, -1, 7*16 + 4, 8, true, false);
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		e1.create((callback)-> {
			enemyFactory.createGoomba(2 * 16 + offsetX, 6 * 16 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		e2.create((callback)-> {
			enemyFactory.createGoomba(6 * 16 + offsetX, 6 * 16 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
	}
}
