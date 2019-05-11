package com.la.aspects.enemy_hit;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;
import com.engine.direction.Direction;

public abstract class EnemyHit implements IAspect {
	public enum ResultType {
		HIT,
		SHIELD,
		NONE,
		STOMP
	}
	public static class Result {
		public ResultType type;
		public float cx, cy;
	}

	private int uniqueID;

	public EnemyHit(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	@Override
	public int getID() {
		return uniqueID;
	}

	public abstract Result hit(
		float x, float y, float w, float h,
		Direction direction, int enemyID, int damage, int shieldProtection);

	@Override
	public AspectType getType() {
		return AspectType.ENEMY_HIT;
	}
}
