package com.engine.component.tail;

import java.util.ArrayList;
import java.util.List;

import com.engine.aspect.IAspectSystem;
import com.engine.gfx.GFXSystem;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.SpatialSystem;

public class TailComponent {
	public interface TailComponentDependency {
		float getOwnerCenterX();
		float getOwnerCenterY();
	}

	private GFXSystem gfxSystem;
	private SpatialSystem spatialSystem;
	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private List<TailSegment> segments = new ArrayList<>();
	private Trail trail;
	private TailComponentDependency dependency;

	public TailComponent(
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			int trailMaxSize,
			TailComponentDependency dependency) {
		this.gfxSystem = gfxSystem;
		this.spatialSystem = spatialSystem;
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.dependency = dependency;

		trail = new Trail(trailMaxSize);
	}
	public void addSegment(int texX, int texY, int w, int h, int trailIndex) {
		TailSegment segment = new TailSegment(gfxSystem, spatialSystem, uniqueIDManager, aspectSystem, texX, texY, w, h, trail, trailIndex);
		segments.add(segment);
	}
	public void update() {
		trail.update(dependency.getOwnerCenterX(), dependency.getOwnerCenterY());
		for (TailSegment tailSegment : segments) {
			tailSegment.update();
		}
	}
	public void draw() {
		for (TailSegment tailSegment : segments) {
			tailSegment.draw();
		}
	}
	public void reset() {
		trail.reset();
		for (TailSegment tailSegment : segments) {
			tailSegment.reset();
		}
	}
	public void remove() {
		for (TailSegment tailSegment : segments) {
			tailSegment.remove();
		}
	}
}
