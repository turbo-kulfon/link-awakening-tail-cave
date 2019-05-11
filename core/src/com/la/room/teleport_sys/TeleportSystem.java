package com.la.room.teleport_sys;

import com.engine.sound.SoundSystem;
import com.la.factory.IGameStateFactory;
import com.la.factory.IRoomFactory;
import com.la.game_objects.link.ILinkData;
import com.la.room.transition.IRoomTransition;

public class TeleportSystem {
	private SoundSystem soundSystem;
	private IRoomFactory roomFactory;
	private IRoomTransition roomTransition;
	private ILinkData linkData;
	private IGameStateFactory gameStateFactory;
	private int entranceRoomID, miniBossRoomID;
	private boolean active;

	public TeleportSystem(
			SoundSystem soundSystem,
			IRoomFactory roomFactory,
			IRoomTransition roomTransition,
			ILinkData linkData,
			IGameStateFactory gameStateFactory,
			int entranceRoomID, int miniBossRoomID) {
		this.soundSystem = soundSystem;
		this.roomFactory = roomFactory;
		this.roomTransition = roomTransition;
		this.linkData = linkData;
		this.gameStateFactory = gameStateFactory;
		this.entranceRoomID = entranceRoomID;
		this.miniBossRoomID = miniBossRoomID;
	}

	public void activate() {
		active = true;
	}
	public void activateAndCreateMiniBossEntrance() {
		active = true;
		createMiniBossTeleport(0, 0);
		soundSystem.teleportCreated();
	}
	public void createEntranceTeleport(int xOffset, int yOffset) {
		if(active == true) {
			roomFactory.createTeleport(80 + xOffset, 64 + yOffset, (x, y)->{
				linkData.setPosition(x - 5, y - 3);
				gameStateFactory.createTeleportState(()-> {
					roomTransition.gotoRoom(miniBossRoomID);
				});
			});
		}
	}
	public void createMiniBossTeleport(int xOffset, int yOffset) {
		if(active == true) {
			roomFactory.createTeleport(80 + xOffset, 64 + yOffset, (x, y)->{
				linkData.setPosition(x - 5, y - 3);
				gameStateFactory.createTeleportState(()-> {
					roomTransition.gotoRoom(entranceRoomID);
				});
			});
		}
	}
}
