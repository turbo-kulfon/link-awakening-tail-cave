package com.la.aspects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;
import com.engine.util.CollisionDetection;
import com.engine.util.ICollisionDetection;

public class HeightObject implements IAspect {
	public interface HeightObjectDependency {
		float getCenterX();
		float getCenterY();

		float getHeight();
	}

	private int uniqueID;
	private float height;
	private ICollisionDetection collisionDetection;
	private HeightObjectDependency dependency;

	public HeightObject(int uniqueID, HeightObjectDependency dependency) {
		this.uniqueID = uniqueID;
		collisionDetection = new CollisionDetection();
		this.dependency = dependency;
	}

	public boolean collision(float x, float y, float w, float h, float minHeight) {
		if(dependency.getHeight() > minHeight) {
			return false;
		}
		return collisionDetection.collisionDetect(x, y, w, h, dependency.getCenterX(), dependency.getCenterY());
	}
	public void reset() {
		height = 0;
	}
	public void setHeight(float arg) {
		height = arg;
	}
	public float getHeight() {
		return height;
	}

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.HEIGHT_OBJECT;
	}
}
