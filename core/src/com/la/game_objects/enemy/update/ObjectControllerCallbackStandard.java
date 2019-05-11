package com.la.game_objects.enemy.update;

import com.engine.component.sword.SwordState;
import com.la.aspects.SwordHit.SwordHitResult;
import com.la.aspects.SwordHit.SwordHitResultType;

public abstract class ObjectControllerCallbackStandard implements IObjectControllerCallback {
	@Override
	public SwordHitResult onSwordCollision(IObjectController controller,
			float swordX, float swordY, float swordW, float swordH,
			float linkCX, float linkCY,
			int dmg, boolean powerUped,
			SwordState state, int swordCounter) {
		if(powerUped == false) {
			controller.recoil(linkCX, linkCY, 30, 10);
		}
		else {
			controller.recoil(linkCX, linkCY, 30 * 100, 10 * 100);
		}
		controller.doDmg(dmg);
		return new SwordHitResult(SwordHitResultType.HIT, controller.getCenterX(), controller.getCenterY());
	}
	@Override
	public void onLinkCollision(int uniqueID) {}
	@Override
	public void onLinkShieldCollision(IObjectController controller, float linkCX, float linkCY) {
		controller.recoil(linkCX, linkCY, 30, 10);
	}
	@Override
	public boolean onBombCollision(IObjectController controller, float bombCX, float bombCY, boolean isPlayerOwner) {
		controller.recoil(bombCX, bombCY, 30, 10);
		controller.doDmg(4);
		controller.stop();
		return true;
	}
	@Override
	public boolean onMagicPowderCollision(IObjectController controller, float linkCX, float linkCY) {
		return false;
	}
	@Override
	public void onWallCollisionFromLeft(IObjectController controller) {
	}
	@Override
	public void onWallCollisionFromRight(IObjectController controller) {
	}
	@Override
	public void onWallCollisionFromUp(IObjectController controller) {
	}
	@Override
	public void onWallCollisionFromDown(IObjectController controller) {
	}

	@Override
	public void onBurn(IObjectController controller) {
	}
}
