package com.engine.spatial.core;

public interface IPosition {
	void setCoordinates(float x, float y, float w, float h);
	void setPosition(float x, float y);
	void setSize(float w, float h);
	void move(float dx, float dy);

	void setX(float value);
	void setY(float value);
	void setW(float value);
	void setH(float value);

	float getX();
	float getY();
	float getW();
	float getH();

	float getCenterX();
	float getCenterY();

	void bounceLeft(float fromX);
	void bounceRight(float fromX);
	void bounceUp(float fromY);
	void bounceDown(float fromY);
}
