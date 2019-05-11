package com.la.game_objects.link;

import com.engine.component.sword.SwordState;
import com.engine.direction.Direction;

public interface ILinkData {
	void setPosition(float x, float y);
	void setLastPosition();
	void restoreLastPosition();

	float getX();
	float getY();
	float getW();
	float getH();

	float getCenterX();
	float getCenterY();

	float getSwordCenterX();
	float getSwordCenterY();
	SwordState getSwordState();

	void changeMoveState(int newStateID);
	void changeDirectionToRight();

	void setDrawLayer(int layer);
	void setGetBigItem();
	void setGetPowerUpItemAnimation();
	void setDying();
	void setDead();
}
