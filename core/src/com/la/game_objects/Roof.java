package com.la.game_objects;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.ColorDrawComponent;
import com.engine.gfx.GFXSystem;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.RoomTransition;

public class Roof implements IGameObject {
	private ColorDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private int uniqueID;
	private boolean remove;

	public Roof(
		int x, int y, float r, float g, float b,
		GFXSystem gfxSystem,
		SpatialSystem spatialSystem,
		IAspectSystem aspectSystem,
		IUniqueIDManager uniqueIDManager) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		uniqueID = uniqueIDManager.getUniqueID();
		
		drawComponent = gfxSystem.createColorDrawComponent(0);
		drawComponent.setColor(r, g, b);
		drawComponent.setSize(16, 16);

		spatialComponent = spatialSystem.createFreeStaticComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 16, 16);

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
	}

	@Override
	public void update() {
	}

	@Override
	public void draw() {
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
	}

	@Override
	public void setToRemove() {
		remove = true;
	}
	@Override
	public boolean shouldRemove() {
		return remove;
	}

	@Override
	public void onRemove() {
		drawComponent.remove();
		spatialComponent.remove();
		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}
}
