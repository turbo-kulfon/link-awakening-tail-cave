package com.la.aspects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;
import com.engine.util.CollisionDetection;
import com.engine.util.ICollisionDetection;

public class OneWayDoorAspect implements IAspect {
	public interface OneWayDoorAspectDependency {
		float getCenterX();
		float getCenterY();

		float getHeight();

		void setDrawComponentVisible(boolean visible);
		void setSpatialComponent(boolean active);
		void moveOnYAxis(float value);

		void onEnter();
		void onExit();
	}

	private OneWayDoorAspectDependency dependency;
	private ICollisionDetection collisionDetection = new CollisionDetection();
	private int uniqueID;

	public OneWayDoorAspect(int uniqueID, OneWayDoorAspectDependency dependency) {
		this.uniqueID = uniqueID;
		this.dependency = dependency;
	}

	public boolean collision(float x, float y, float w, float h) {
		if(dependency.getHeight() > 0) {
			return false;
		}
		return collisionDetection.collisionDetect(x, y, w, h, dependency.getCenterX(), dependency.getCenterY());
	}
	public void enterDoor() {
		dependency.setDrawComponentVisible(false);
		dependency.setSpatialComponent(false);
		dependency.onEnter();
	}
	public void exitDoor() {
		dependency.setDrawComponentVisible(true);
		dependency.setSpatialComponent(true);
		dependency.moveOnYAxis(-19);
		dependency.onExit();
	}

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.ONE_WAY_DOOR;
	}
}
