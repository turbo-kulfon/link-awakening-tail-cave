package com.la.game_objects.enemy.update;

public interface IObjectController {
	public enum State {
		STANDARD,
		JUMPING,
		RECOIL,
		FALLING,
		BURN
	}

	void moveLeft(float speed);
	void moveRight(float speed);
	void moveUp(float speed);
	void moveDown(float speed);
	void stop();
	void setMoveDeltaX(float dx);
	void setMoveDeltaY(float dy);
	void setMoveDelta(float dx, float dy);

	void setX(float x);
	void setY(float y);
	void move(float dx, float dy);

	void recoil(float cx, float cy, float distance, int timeFrame);
	void jump(float initialDelta);
	void jumpBounce(float initialDelta);
	void setSpatialComponentActive(boolean active);
	void setFlying(boolean flying);
	void setHidden(boolean hidden);
	void setCollideWithLink(boolean collide);

	void doDmg(int damage);

	float getX();
	float getY();
	float getW();
	float getH();
	float getCenterX();
	float getCenterY();
	int getInvisbilityFrame();
	float getHeight();

	State getCurrentState();
}
