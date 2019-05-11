package com.la.respawn;

import com.la.respawn.RespawnElement.RespawnElementCallback;

public class RespawnSystem {
	private RespawnCore respawnCore = new RespawnCore();

	public RespawnComponent createComponent(RespawnElementCallback callback) {
		RespawnElement element = new RespawnElement(callback);
		respawnCore.addElement(element);
		return new RespawnComponent(respawnCore, element);
	}
	public void update(int xMap, int yMap, int roomID, boolean transition) {
		respawnCore.update(xMap, yMap, roomID, transition);
	}
}
