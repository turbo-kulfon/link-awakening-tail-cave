package com.engine.spatial.core;

public class MoveNULL implements IMove {
	@Override public void setDelta(float dx, float dy) {}
	@Override public void setDeltaX(float dx) {}
	@Override public void setDeltaY(float dy) {}
	@Override public void stopX() {}
	@Override public void stopY() {}
	@Override public void xAxisToZero(float delta) {}
	@Override public void yAxisToZero(float delta) {}
	@Override public void xAxisToValue(float value, float delta) {}
	@Override public void yAxisToValue(float value, float delta) {}
	@Override public float getDeltaX() {return 0;}
	@Override public float getDeltaY() {return 0;}
}
