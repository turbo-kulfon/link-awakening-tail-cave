package com.la.respawn;

public class RespawnComponent {
	private RespawnCore respawnCore;
	private RespawnElement respawnElement;

	public RespawnComponent(
			RespawnCore respawnCore,
			RespawnElement respawnElement) {
		this.respawnCore = respawnCore;
		this.respawnElement = respawnElement;
	}

	public void remove() {
		respawnCore.removeElement(respawnElement);
	}
}
