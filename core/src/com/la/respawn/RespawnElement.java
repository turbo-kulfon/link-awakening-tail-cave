package com.la.respawn;

public class RespawnElement {
	public enum RespawnType {
		SCREEN_VISIT,
		TRANSITION,
	}
	public interface RespawnElementCallback {
		void onRespawn();
		boolean respawnTypeMatch(RespawnType respawnType);
	}

	private RespawnElementCallback callback;

	public RespawnElement(RespawnElementCallback callback) {
		this.callback = callback;
	}

	public void update(RespawnType respawnType) {
		if(callback.respawnTypeMatch(respawnType) == true) {
			callback.onRespawn();
		}
	}
}
