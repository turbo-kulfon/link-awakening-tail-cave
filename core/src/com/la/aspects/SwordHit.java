package com.la.aspects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;
import com.engine.component.sword.SwordState;
import com.engine.direction.Direction;

public abstract class SwordHit implements IAspect {
	public enum SwordHitResultType {
		NONE,
		HIT,
		DEFLECT
	}
	public static class SwordHitResult {
		public SwordHitResultType type;
		public float enemyCX, enemyCY;

		public SwordHitResult(SwordHitResultType type, float enemyCX, float enemyCY) {
			this.type = type;
			this.enemyCX = enemyCX;
			this.enemyCY = enemyCY;
		}
	}

	private int uniqueID;

	public SwordHit(int uniqueID) {
		this.uniqueID = uniqueID;
	}

	public abstract SwordHitResult hit(
		float swordX, float swordY, float swordW, float swordH,
		float ownerCX, float ownerCY, Direction ownerDirection, int dmg, boolean powerUped,
		SwordState swordState, int counter);

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.SWORD_HIT;
	}
}
