package com.engine.component.tail;

import com.engine.aspect.IAspectSystem;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.engine.util.ICoordinate.Vector;
import com.la.aspects.RoomTransition;

public class TailSegment {
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;
	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;
	private Trail trail;
	private int trailIndex, uniqueID;
	private float w2, h2;

	public TailSegment(
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			int texX, int texY, int w, int h,
			Trail trail, int trailIndex) {
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;
		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(texX, texY, w, h);
		drawComponent.setSize(w, h);
		drawComponent.setVisible(false);

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setW(w);
		spatialComponent.setH(h);

		this.trail = trail;
		this.trailIndex = trailIndex;

		w2 = w/2.0f;
		h2 = h/2.0f;

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
	}

	public void reset() {
		drawComponent.setVisible(false);
	}
	public void setPosition(float cx, float cy) {
		spatialComponent.setPosition(
			cx - w2,
			cy - h2);
	}
	public void update() {
		Vector pos = trail.getPosition(trailIndex);
		if(pos != null) {
			drawComponent.setVisible(true);
			spatialComponent.setPosition(pos.x - w2, pos.y - h2);
		}
		else {
			drawComponent.setVisible(false);
		}
	}
	public void draw() {
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
	}
	public void remove() {
		drawComponent.remove();
		spatialComponent.remove();
		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}
}
