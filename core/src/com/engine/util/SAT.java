package com.engine.util;

public class SAT implements ISAT {
	public SAT() {}

	@Override
	public int sat(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {
		float halfWidth1 = w1/2.0f;
		float halfHeight1 = h1/2.0f;
		float halfWidth2 = w2/2.0f;
		float halfHeight2 = h2/2.0f;
		float center1X = x1 + halfWidth1;
		float center1Y = y1 + halfHeight1;
		float center2X = x2 + halfWidth2;
		float center2Y = y2 + halfHeight2;
		float xDiff = center1X - center2X;
		float yDiff = center1Y - center2Y;
		float xOverlap = halfWidth1 + halfWidth2 - Math.abs(xDiff);
		float yOverlap = halfHeight1 + halfHeight2 - Math.abs(yDiff);
		if(xOverlap <= yOverlap) {
			if(xDiff <= 0) {
//				collisionResolve.onLeftSideCollision(x2, y2, w2, h2, xOverlap);
				return 0;
			}
			else {
//				collisionResolve.onRightSideCollision(x2, y2, w2, h2, xOverlap);
				return 1;
			}
		}
		else {
			if(yDiff <= 0) {
//				collisionResolve.onUpSideCollision(x2, y2, w2, h2, yOverlap);
				return 2;
			}
			else {
//				collisionResolve.onDownSideCollision(x2, y2, w2, h2, yOverlap);
				return 3;
			}
		}
	}
}
