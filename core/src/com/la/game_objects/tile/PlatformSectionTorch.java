package com.la.game_objects.tile;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.RoomTransition;

public class PlatformSectionTorch implements IGameObject {
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private int animationCounter = 100;

	private int uniqueID;
	private boolean remove;

	public PlatformSectionTorch(int x, int y,
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem) {
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTextureSize(16, 16);
		drawComponent.setSize(16, 16);

		spatialComponent = spatialSystem.createFreeStaticComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 16, 16);

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
	}

	@Override
	public void update() {
		if(animationCounter > 0) {
			animationCounter -= 1;
		}
		else {
			animationCounter = 100;
		}
	}
	@Override
	public void draw() {
		if(animationCounter <= 100 && animationCounter > 75) {
			drawComponent.setTexturePosition(480, 464);
		}
		else if(animationCounter <= 75 && animationCounter > 50) {
			drawComponent.setTexturePosition(464, 464);
		}
		else if(animationCounter <= 50 && animationCounter > 25) {
			drawComponent.setTexturePosition(448, 464);
		}
		else if(animationCounter <= 25 && animationCounter >= 0) {
			drawComponent.setTexturePosition(432, 464);
		}
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
		uniqueIDManager.returnID(uniqueID);
		aspectSystem.removeAspects(uniqueID);
	}
}
