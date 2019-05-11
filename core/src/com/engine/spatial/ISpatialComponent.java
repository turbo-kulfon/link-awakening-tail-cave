package com.engine.spatial;

import com.engine.spatial.core.ResponseCallback;

public interface ISpatialComponent {
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

	void setDelta(float dx, float dy);
	void setDeltaX(float dx);
	void setDeltaY(float dy);

	void xAxisToZero(float delta);
	void yAxisToZero(float delta);
	void xAxisToValue(float value, float delta);
	void yAxisToValue(float value, float delta);

	void stop();

	float getDeltaX();
	float getDeltaY();

	void setCollisionResponse(ResponseCallback response);
	void setCollisionResponseX(ResponseCallback response);
	void setCollisionResponseY(ResponseCallback response);

	void bounceLeft(float fromX);
	void bounceRight(float fromX);
	void bounceUp(float fromY);
	void bounceDown(float fromY);

	void setActive(boolean active);

	void remove();
}
