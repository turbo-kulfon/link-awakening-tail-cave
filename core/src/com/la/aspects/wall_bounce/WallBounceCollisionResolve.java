package com.la.aspects.wall_bounce;

public interface WallBounceCollisionResolve {
	void onLeftSideCollision(
		float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection);
	void onRightSideCollision(
		float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection);
	void onUpSideCollision(
		float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection);
	void onDownSideCollision(
		float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection);
}
