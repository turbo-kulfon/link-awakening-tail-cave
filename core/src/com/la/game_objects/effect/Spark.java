package com.la.game_objects.effect;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.RoomTransition;

public class Spark implements IGameObject {
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private int counter;

	private int uniqueID;
	private boolean remove;

	public Spark(
			float x, float y,
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setSize(16, 16);

		spatialComponent = spatialSystem.createFreeStaticComponent(uniqueID);
		spatialComponent.setPosition(x - 8, y - 8);
		spatialComponent.setSize(16, 16);

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
	}

	@Override
	public void update() {
		counter += 1;
		if(counter >= 10) {
			setToRemove();
		}
	}
	@Override
	public void draw() {
		if(counter <= 4) {
			drawComponent.setTexture(158, 48, 16, 16);
			drawComponent.setSize(16, 16);
		}
		else {
			drawComponent.setTexture(174, 48, 16, 16);
			drawComponent.setSize(16, 16);
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

		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}
}
