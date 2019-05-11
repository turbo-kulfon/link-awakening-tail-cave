package com.la.game_objects.enemy.update;

import com.engine.component.sword.SwordState;
import com.la.aspects.SwordHit.SwordHitResult;

public interface IObjectControllerCallback {
	SwordHitResult onSwordCollision(IObjectController controller,
			float swordX, float swordY, float swordW, float swordH,
			float linkCX, float linkCY,
			int dmg, boolean powerUped,
			SwordState swordState, int swordCounter);
	void onLinkCollision(int uniqueID);
	void onLinkShieldCollision(IObjectController controller, float linkCX, float linkCY);
	boolean onBombCollision(IObjectController controller, float bombCX, float bombCY, boolean isPlayerOwner);
	boolean onMagicPowderCollision(IObjectController controller, float linkCX, float linkCY);
	boolean onDeath(IObjectController controller);

	void onWallCollisionFromLeft(IObjectController controller);
	void onWallCollisionFromRight(IObjectController controller);
	void onWallCollisionFromUp(IObjectController controller);
	void onWallCollisionFromDown(IObjectController controller);

	void onBurn(IObjectController controller);
}
