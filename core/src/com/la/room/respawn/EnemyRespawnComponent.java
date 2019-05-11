package com.la.room.respawn;

import com.la.respawn.RespawnComponent;
import com.la.respawn.RespawnElement.RespawnElementCallback;
import com.la.respawn.RespawnElement.RespawnType;
import com.la.respawn.RespawnSystem;

public class EnemyRespawnComponent {
	public class EnemyRespawnSet {
		public void blockRespawn() {
			respawn = false;
		}
	}
	public interface EnemyCreate {
		void create(EnemyRespawnSet respawnSet);
	}

	private RespawnComponent respawnComponent;
	private boolean respawn = true;
	private EnemyRespawnSet set;

	public EnemyRespawnComponent(RespawnSystem respawnSystem) {
		respawnComponent = respawnSystem.createComponent(new RespawnElementCallback() {
			@Override
			public boolean respawnTypeMatch(RespawnType respawnType) {
				return respawnType == RespawnType.SCREEN_VISIT || respawnType == RespawnType.TRANSITION;
			}
			@Override
			public void onRespawn() {
				respawn = true;
			}
		});
		set = new EnemyRespawnSet();
	}

	public void create(EnemyCreate enemyCreate) {
		if(respawn == true) {
			enemyCreate.create(set);
		}
	}
	public void remove() {
		respawnComponent.remove();
	}
}
