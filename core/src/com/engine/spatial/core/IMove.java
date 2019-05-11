package com.engine.spatial.core;

public interface IMove {
	void setDelta(float dx, float dy);
	void setDeltaX(float dx);
	void setDeltaY(float dy);

	void stopX();
	void stopY();

	void xAxisToZero(float delta);
	void yAxisToZero(float delta);
	void xAxisToValue(float value, float delta);
	void yAxisToValue(float value, float delta);

	float getDeltaX();
	float getDeltaY();
}
