package com.la.game_objects.tile;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.RoomTransition;

public class Floor implements IGameObject {
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private int uniqueID;
	private boolean remove;

	public Floor(
		int x, int y, int textureX, int textureY,
		GFXSystem gfxSystem,
		SpatialSystem spatialSystem,
		IAspectSystem aspectSystem,
		IUniqueIDManager uniqueIDManager) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		uniqueID = uniqueIDManager.getUniqueID();
		
		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(textureX, textureY, 16, 16);
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
