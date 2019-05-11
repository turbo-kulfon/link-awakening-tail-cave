package com.engine.util;

public interface ICoordinate {
	public class Vector {
		public float x, y;

		public Vector(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

	Vector calculateDelta(float x1, float y1, float x2, float y2);
	Vector calculateNonNormalizedDelta(float x1, float y1, float x2, float y2);
	Vector angleToDelta(float angle);
	float deltaToAngle(float dx, float dy);
	float calculateDistance(float x1, float y1, float x2, float y2);
}
