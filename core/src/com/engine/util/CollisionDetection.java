package com.engine.util;

public class CollisionDetection implements ICollisionDetection {
	@Override
	public boolean collisionDetect(float x1, float y1, float w1, float h1, float x2, float y2, float w2, float h2) {
		if(x1 + w1 <= x2 || x1 >= x2 + w2) {
			return false;
		}
		if(y1 + h1 <= y2 || y1 >= y2 + h2) {
			return false;
		}
		return true;
	}
	@Override
	public boolean collisionDetect(float x1, float y1, float w1, float h1, float pointX, float pointY) {
		return collisionDetect(x1, y1, w1, h1, pointX, pointY, 1, 1);
	}
}
