package com.engine.spatial.core;

import java.util.ArrayList;
import java.util.List;

import com.engine.util.ICollisionDetection;

public class SpatialCore implements ISpatialCore {
	interface TraverseCallback {
		void callback(ISpatialElement subject, ISpatialElement collided);
	}

	private List<ISpatialElement> elements = new ArrayList<>();
	private ICollisionDetection collisionDetection;

	public SpatialCore(ICollisionDetection collisionDetection) {
		this.collisionDetection = collisionDetection;
	}

	@Override
	public void addElement(ISpatialElement spatialElement) {
		if(elements.contains(spatialElement) == false) {
			elements.add(spatialElement);
		}
	}
	@Override
	public void removeElement(ISpatialElement spatialElement) {
		elements.remove(spatialElement);
	}
	@Override
	public List<Integer> getCollided(float x, float y, float w, float h) {
		List<Integer> result = new ArrayList<>();
		for (ISpatialElement subject : elements) {
			if(subject.isActive() == true && subject.isDynamic() == true) {
				if(collisionDetection.collisionDetect(
					subject.getX(), subject.getY(), subject.getW(), subject.getH(),
					x, y, w, h) == true) {
					result.add(subject.getID());
				}
			}
		}
		return result;
	}

	@Override
	public void update() {
		traverseData((subject, collided)-> {
			if(collisionDetection.collisionDetect(
					subject.getX() + subject.getDeltaX(), subject.getY(), subject.getW(), subject.getH(),
					collided.getX(), collided.getY(), collided.getW(), collided.getH()) == true) {
				subject.responseX(collided.getID());
				collided.responseX(subject.getID());
			}
		});

		traverseData((subject, collided)-> {
			if(collisionDetection.collisionDetect(
					subject.getX(), subject.getY() + subject.getDeltaY(), subject.getW(), subject.getH(),
					collided.getX(), collided.getY(), collided.getW(), collided.getH()) == true) {
				subject.responseX(collided.getID());
				collided.responseX(subject.getID());
			}
		});

		for (ISpatialElement spatialElement : elements) {
			if(spatialElement.isDynamic() == true) {
				spatialElement.move();
			}
		}
	}

	@Override
	public void dispose() {
		elements.clear();
	}

	private void traverseData(TraverseCallback callback) {
		for (ISpatialElement subject : elements) {
			if(subject.isActive() == true && subject.isDynamic() == true) {
				for (ISpatialElement collided : elements) {
					if(collided.isActive() == true) {
						callback.callback(subject, collided);
					}
				}
			}
		}
	}
}
