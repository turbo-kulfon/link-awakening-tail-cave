package com.la.game_objects.link.controller.top_down;

import com.engine.direction.Direction;
import com.la.aspects.enemy_hit.EnemyHit.Result;
import com.la.aspects.enemy_hit.EnemyHit.ResultType;
import com.la.game_objects.link.controller.common.EnemyHitBase.EnemyHitCallback;

public class EnemyHitTopDownCallback implements EnemyHitCallback {
	public interface EnemyHitTopDownDependency {
		float getCenterX();
		float getCenterY();

		boolean shieldCollisionCheck(float centerX, float centerY, Direction direction);
		boolean isRecoilActive();
		void hitRecoil(float centerX, float centerY);
		void shieldRecoil(float centerX, float centerY);

		boolean isInvisible();
		float getHeight();
		void doDamage(int damage);
	}

	private EnemyHitTopDownDependency dependency;
	private Result result = new Result();

	public EnemyHitTopDownCallback(EnemyHitTopDownDependency dependency) {
		this.dependency = dependency;
	}

	@Override
	public Result hit(float x, float y, float w, float h, Direction direction, int enemyID, int damage, int shieldProtection) {
		if(dependency.getHeight() <= 0) {
			float centerX = x + w/2.0f;
			float centerY = y + h/2.0f;
			if(shieldProtection == -1) {
				getHit(centerX, centerY, damage);
				return result;
			}
			else {
				if(dependency.shieldCollisionCheck(centerX, centerY, direction) == false) {
					getHit(centerX, centerY, damage);
					return result;
				}
				else {
					if(shieldProtection == 2) {
						dependency.shieldRecoil(centerX, dependency.getCenterY());
					}
					else {
						dependency.shieldRecoil(centerX, centerY);
					}
					result.cx = dependency.getCenterX();
					result.cy = dependency.getCenterY();
					result.type = ResultType.SHIELD;
					return result;
				}
			}
		}
		result.type = ResultType.NONE;
		return result;
	}

	private void getHit(float centerX, float centerY, int damage) {
		if(dependency.isRecoilActive() == false && damage != -1 && dependency.isInvisible() == false) {
			if(damage > 0) {
				dependency.doDamage(damage);
				dependency.hitRecoil(centerX, centerY);
			}
			result.type = ResultType.HIT;
		}
		else {
			result.type = ResultType.NONE;
		}
	}
}
