package com.la.game_objects.link.controller.platform;

import com.engine.direction.Direction;
import com.la.aspects.enemy_hit.EnemyHit.Result;
import com.la.aspects.enemy_hit.EnemyHit.ResultType;
import com.la.game_objects.link.controller.common.EnemyHitBase.EnemyHitCallback;

public class EnemyHitPlatformCallback implements EnemyHitCallback {
	public interface EnemyHitPlatformDependency {
		float getCenterX();
		float getCenterY();
		float getBottomY();
		float getDeltaY();

		boolean shieldCollisionCheck(float centerX, float centerY, Direction direction);
		boolean isRecoilActive();
		void hitRecoil(float centerX, float centerY);
		void shieldRecoil(float centerX, float centerY);
		void stompedEnemyBounce();

		void doDamage(int damage);

		boolean isInvisible();
		boolean isOnLadder();
	}

	private EnemyHitPlatformDependency dependency;
	private Result result = new Result();

	public EnemyHitPlatformCallback(EnemyHitPlatformDependency dependency) {
		this.dependency = dependency;
	}

	@Override
	public Result hit(float x, float y, float w, float h, Direction direction, int enemyID, int damage, int shieldProtect) {
		if(enemyID == 10 && dependency.isOnLadder() == false) {
			if(dependency.getBottomY() - 1 <= y &&
			   dependency.getDeltaY() > 0) {
				result.type = ResultType.STOMP;
				dependency.stompedEnemyBounce();
				return result;
			}
		}
		if(dependency.isInvisible() == false) {
			float centerX = x + w/2.0f;
			float centerY = y + h/2.0f;
			if(dependency.shieldCollisionCheck(centerX, centerY, direction) == false) {
				if(dependency.isRecoilActive() == false) {
					dependency.doDamage(damage);
					dependency.hitRecoil(centerX, centerY);
					result.type = ResultType.HIT;
					return result;
				}
				else {
					result.type = ResultType.NONE;
					return result;
				}
			}
			else {
				dependency.shieldRecoil(centerX, centerY);
				result.cx = dependency.getCenterX();
				result.cy = dependency.getCenterY();
				result.type = ResultType.SHIELD;
				return result;
			}
		}
		result.type = ResultType.NONE;
		return result;
	}
}
