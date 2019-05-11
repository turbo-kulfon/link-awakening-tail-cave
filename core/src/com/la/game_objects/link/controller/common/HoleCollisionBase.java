package com.la.game_objects.link.controller.common;

import com.la.aspects.HoleCollision;

public class HoleCollisionBase extends HoleCollision {
	public interface HoleCollisionBaseDependency {
		HoleCollisionResult collision(float holeX, float holeY, float holeW, float holeH, boolean restoreLastPosition);
	}

	private HoleCollisionBaseDependency dependency;

	public HoleCollisionBase(int uniqueID) {
		super(uniqueID);
	}

	public void setDependency(HoleCollisionBaseDependency dependency) {
		this.dependency = dependency;
	}

	@Override
	public HoleCollisionResult collision(float holeX, float holeY, float holeW, float holeH, boolean restoreLastPosition) {
		return dependency.collision(holeX, holeY, holeW, holeH, restoreLastPosition);
	}
}
