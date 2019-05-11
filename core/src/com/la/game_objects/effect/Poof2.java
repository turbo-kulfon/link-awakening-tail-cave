package com.la.game_objects.effect;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.RoomTransition;

public class Poof2 implements IGameObject {
	public interface Poof2Callback {
		void onUpdate(int counter);
		void onEnd();
	}

	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private int counter;

	private int uniqueID;
	private boolean remove;
	private Poof2Callback callback;

	public Poof2(
			float x, float y,
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			Poof2Callback callback) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.callback = callback;

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
		if(counter >= 16) {
			callback.onEnd();
			setToRemove();
		}
		else {
			callback.onUpdate(counter);
		}
	}
	@Override
	public void draw() {
		if(counter >= 0 && counter < 8) {
			drawComponent.setTexture(190, 48, 16, 16);
			drawComponent.setPosition(spatialComponent.getCenterX() - 8, spatialComponent.getCenterY() - 8);
		}
		else if(counter >= 8) {
			drawComponent.setTexture(216, 32, 16, 16);
			drawComponent.setSize(16, 16);
			drawComponent.setPosition(spatialComponent.getCenterX() - 8, spatialComponent.getCenterY() - 8);
		}
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
