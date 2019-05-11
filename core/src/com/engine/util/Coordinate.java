package com.engine.util;

public class Coordinate implements ICoordinate {
	@Override
	public Vector calculateDelta(float x1, float y1, float x2, float y2) {
		float dx = x2 - x1;
		float dy = y2 - y1;
		float length = (float) Math.sqrt(dx * dx + dy * dy);
		if(length == 0 || Float.isNaN(length) == true) {
			return new Vector(0, 0);
		}
		return new Vector(dx/length, dy/length);
	}
	@Override
	public Vector calculateNonNormalizedDelta(float x1, float y1, float x2, float y2) {
		float dx = x2 - x1;
		float dy = y2 - y1;
		return new Vector(dx, dy);
	}
	@Override
	public Vector angleToDelta(float angle) {
		float x = (float) Math.cos(Math.toRadians(angle));
		float y = (float) Math.sin(Math.toRadians(angle));
		return new Vector(x, y);
	}
	@Override
	public float deltaToAngle(float dx, float dy) {
		float angle = (float) Math.toDegrees(Math.atan2(dx, dy));
		return -(angle - 180);
	}
	@Override
	public float calculateDistance(float x1, float y1, float x2, float y2) {
		float dx = x2 - x1;
		float dy = y2 - y1;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}
}
