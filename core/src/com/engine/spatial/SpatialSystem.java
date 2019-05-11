package com.engine.spatial;

import java.util.List;

import com.engine.spatial.core.ISpatialCore;
import com.engine.spatial.core.ISpatialElement;
import com.engine.spatial.core.Move;
import com.engine.spatial.core.Position;
import com.engine.spatial.core.SpatialCore;
import com.engine.spatial.core.SpatialElement;
import com.engine.util.CollisionDetection;

public class SpatialSystem {
	protected ISpatialCore core;

	public SpatialSystem() {
		core = new SpatialCore(new CollisionDetection());
	}

	public ISpatialComponent createStaticComponent(int uniqueID) {
		ISpatialElement element = new SpatialElement(uniqueID, new Position());
		core.addElement(element);
		return new SpatialComponent(element, core);
	}
	public ISpatialComponent createDynamicComponent(int uniqueID) {
		ISpatialElement element = new SpatialElement(uniqueID, new Position(), new Move());
		core.addElement(element);
		return new SpatialComponent(element, core);
	}

	public ISpatialComponent createFreeStaticComponent(int uniqueID) {
		ISpatialElement element = new SpatialElement(uniqueID, new Position());
		return new SpatialComponent(element, core);
	}

	public List<Integer> getCollided(float x, float y, float w, float h) {
		return core.getCollided(x, y, w, h);
	}

	public void update() {
		core.update();
	}
	public void dispose() {
		core.dispose();
	}
}
