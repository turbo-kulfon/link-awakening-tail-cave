package com.la.game_objects.link.controller.common;

import com.la.aspects.LadderCollision;

public class LadderCollisionBase extends LadderCollision {
	public interface LadderCollisionBaseDependency {
		void collision(float ladderX, float ladderY, float ladderW, float ladderH, int ladderType);
	}

	private LadderCollisionBaseDependency dependency;

	public LadderCollisionBase(int uniqueID) {
		super(uniqueID);
	}

	public void setDependency(LadderCollisionBaseDependency dependency) {
		this.dependency = dependency;
	}

	@Override
	public void collision(float ladderX, float ladderY, float ladderW, float ladderH, int ladderType) {
		dependency.collision(ladderX, ladderY, ladderW, ladderH, ladderType);
	}
}
