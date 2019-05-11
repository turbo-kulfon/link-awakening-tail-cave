package com.la.game_objects.link.controller.common;

import com.engine.direction.Direction;
import com.la.aspects.enemy_hit.EnemyHit;

public class EnemyHitBase extends EnemyHit {
	public interface EnemyHitCallback {
		Result hit(float x, float y, float w, float h, Direction direction, int enemyID, int damage, int shieldProtection);
	}

	private EnemyHitCallback callback;

	public EnemyHitBase(int uniqueID) {
		super(uniqueID);
	}

	public void setCallback(EnemyHitCallback callback) {
		this.callback = callback;
	}
	@Override
	public Result hit(float x, float y, float w, float h, Direction direction, int enemyID, int damage, int shieldProtection) {
		return callback.hit(x, y, w, h, direction, enemyID, damage, shieldProtection);
	}
}
