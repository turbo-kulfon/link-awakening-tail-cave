package com.engine.component.tail;

import java.util.LinkedList;
import java.util.List;

import com.engine.util.ICoordinate.Vector;

public class Trail {
	private List<Vector> trail = new LinkedList<>();
	private int maxSize;

	public Trail(int size) {
		maxSize = size;
	}

	public void reset() {
		trail.clear();
	}
	public void update(float cx, float cy) {
		trail.add(0, new Vector(cx, cy));
		while(trail.size() > maxSize) {
			trail.remove(trail.size() - 1);
		}
	}
	public Vector getPosition(int index) {
		if(index < trail.size()) {
			return trail.get(index);
		}
		return null;
	}
	public boolean isIndexVisible(int index) {
		if(index < trail.size()) {
			return true;
		}
		return false;
	}
}
