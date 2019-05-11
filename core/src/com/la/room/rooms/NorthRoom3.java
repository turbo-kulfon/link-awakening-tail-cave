package com.la.room.rooms;

import com.la.factory.IEnemyFactory;
import com.la.factory.IEventFactory;
import com.la.factory.IRoomFactory;
import com.la.game_objects.effect.Poof2.Poof2Callback;
import com.la.map_display.IMapDisplaySystem;
import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;
import com.la.respawn.RespawnSystem;
import com.la.room.IRoom;
import com.la.room.IRoomCreator;
import com.la.room.respawn.EnemyRespawnComponent;
import com.la.room.transition.IMapLocation;
import com.la.room.transition.IRoomTransition;

public class NorthRoom3 implements IRoom {
	private int mapData[][] = new int[][]{
		{18, 10, 10, 10, 10, 10, 10, 10, 10, 19},
		{11,  0,  0, 92,  0,  0, 92,  0,  0, 12},
		{11,  0, 92,  0,  0,  0,  0, 92,  0, 25},
		{11,  0,  0,  0,  0,  0,  0,  0,  0, 26},
		{11,  0,  0,  0,  0,  0,  0,  0,  0, 12},
		{11,  2,  0,  0,  0,  0,  0,  0,  2, 12},
		{20, 17,  2,  0,  0,  0,  0,  2, 16, 21},
		{22, 20, 13, 13, 13, 13, 13, 13, 21, 22},
	};

	private IRoomCreator roomCreator;
	private IRoomFactory roomFactory;
	private IEnemyFactory enemyFactory;
	private IEventFactory eventFactory;
	private IRoomTransition roomTransition;
	private MapDisplayEntryComponent mapDisplayEntryComponent;

	private EnemyRespawnComponent e1, e2;
	private boolean showStairs;

	public NorthRoom3(
			IRoomCreator roomCreator,
			IRoomFactory roomFactory,
			IEnemyFactory enemyFactory,
			IEventFactory eventFactory,
			IRoomTransition roomTransition,
			IMapLocation mapLocation,
			IMapDisplaySystem mapDisplaySystem,
			RespawnSystem respawnSystem) {
		this.roomCreator = roomCreator;
		this.roomFactory = roomFactory;
		this.enemyFactory = enemyFactory;
		this.eventFactory = eventFactory;
		this.roomTransition = roomTransition;

		e1 = new EnemyRespawnComponent(respawnSystem);
		e2 = new EnemyRespawnComponent(respawnSystem);

		mapLocation.addEntry(12, 1, 3, -1, 10, -1, -1, 8 * 16 + 4, 20, false, false);
		mapDisplayEntryComponent = mapDisplaySystem.addMapEntry(1, 3, 0, false, false, false, false, true, false, false);
	}

	@Override
	public void createRoom(int offsetX, int offsetY) {
		roomCreator.createRoom(mapData, offsetX, offsetY);
		roomFactory.createOwlStatue(5* 16 + offsetX, 4 + offsetY, 0,
				  "Turn aside the|"
				+ "spined ones with|"
				+ "a shield...");

		e1.create((callback)-> {
			enemyFactory.createSpikedBeetle(3 * 16 + 8 + offsetX, 3 * 16 + 8 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});
		e2.create((callback)-> {
			enemyFactory.createSpikedBeetle(6 * 16 + 8 + offsetX, 5 * 16 + 8 + offsetY, ()-> {
				callback.blockRespawn();
			});
		});

		if(showStairs == false) {
			eventFactory.createWhenEnemiesDefeated(()-> {
				showStairs = true;
				roomFactory.createPoof2(8*16 + 8, 1*16 + 8, new Poof2Callback() {
					@Override
					public void onUpdate(int counter) {
						if(counter == 8) {
							roomFactory.createStairs(8*16, 1*16, ()-> {
								roomTransition.gotoRoom(51);
							});
						}
					}
					@Override
					public void onEnd() {
					}
				});
			});
		}
		else {
			roomFactory.createStairs(8*16 + offsetX, 1*16 + offsetY, ()-> {
				roomTransition.gotoRoom(51);
			});
		}
		mapDisplayEntryComponent.setVisited();
	}
}
