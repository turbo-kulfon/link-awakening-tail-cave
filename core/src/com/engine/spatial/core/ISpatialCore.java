package com.engine.spatial.core;

import java.util.List;

public interface ISpatialCore {
	void addElement(ISpatialElement spatialElement);
	void removeElement(ISpatialElement spatialElement);
	List<Integer> getCollided(float x, float y, float w, float h);

	void update();

	void dispose();
}
