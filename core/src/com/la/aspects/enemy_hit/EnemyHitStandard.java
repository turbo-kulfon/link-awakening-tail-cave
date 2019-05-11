package com.la.aspects.enemy_hit;

import com.engine.component.recoil.IRecoil;
import com.engine.component.shield.IShieldComponent;
import com.engine.direction.Direction;
import com.engine.spatial.ISpatialComponent;
import com.la.equipment.IEquipmentSystem;

public class EnemyHitStandard extends EnemyHit {
	public interface StompJump {
		void jump();
		boolean isOnLadder();
		float getHeight();
		boolean invisibility();
		void onHit();
	}

	private ISpatialComponent spatialComponent;
	private IEquipmentSystem equipmentSystem;
	private IRecoil recoil;
	private StompJump stompJump;
	private IShieldComponent shieldComponent;
	private Result result = new Result();

	public EnemyHitStandard(
			int uniqueID,
			ISpatialComponent spatialComponent,
			IEquipmentSystem equipmentSystem,
			IShieldComponent shieldComponent) {
		super(uniqueID);
		this.spatialComponent = spatialComponent;
		this.equipmentSystem = equipmentSystem;
		this.shieldComponent = shieldComponent;
	}

	@Override
	public Result hit(float x, float y, float w, float h, Direction direction, int enemyID, int damage, int shieldProtection) {
		if(stompJump.getHeight() <= 0 && stompJump.invisibility() == false) {
			if(enemyID == 10 && stompJump.isOnLadder() == false) {
				if(spatialComponent.getY() + spatialComponent.getH() - 1 <= y &&
						spatialComponent.getDeltaY() > 0) {
					result.type = ResultType.STOMP;
					stompJump.jump();
					return result;
				}
			}
			float centerX = x + w/2.0f;
			float centerY = y + h/2.0f;
			if(shieldComponent.collisionCheck(centerX, centerY, direction) == false) {
				if(recoil.isActive() == false) {
					equipmentSystem.removeHeart(damage);
					recoil.hit(centerX, centerY, 20, 20);
					result.type = ResultType.HIT;
					stompJump.onHit();
					return result;
				}
				else {
					result.type = ResultType.NONE;
					return result;
				}
			}
			else {
				recoil.hit(centerX, centerY, 16, 16);
				result.cx = spatialComponent.getCenterX();
				result.cy = spatialComponent.getCenterY();
				result.type = ResultType.SHIELD;
				return result;
			}
		}
		result.type = ResultType.NONE;
		return result;
	}

	public void setDependency(
			IRecoil recoil,
			StompJump stompJump) {
		this.recoil = recoil;
		this.stompJump = stompJump;
	}
}
