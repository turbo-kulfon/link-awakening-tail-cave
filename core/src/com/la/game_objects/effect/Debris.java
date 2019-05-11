package com.la.game_objects.effect;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.RoomTransition;

public class Debris implements IGameObject {
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private int counter;
	private float height, deltaY, grav;
	private boolean left;

	private int uniqueID;
	private boolean remove;

	public Debris(float x, float y, boolean left,
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;

		this.left = left;
		counter = 20;
		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(178, 64, 8, 8);
		drawComponent.setSize(8, 8);
//		drawComponent.setVisible(false);

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x-4, y-4, 8, 8);

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));

		deltaY = -1;
		grav = 0.09f;
	}

	@Override
	public void update() {
		counter -= 1;
		if(counter > 0) {
			if(left == true) {
				spatialComponent.setDeltaX(-0.25f);
			}
			else {
				spatialComponent.setDeltaX(0.25f);
			}
			deltaY += grav;
			spatialComponent.setDeltaY(deltaY);
		}
		else {
			setToRemove();
		}
	}

	@Override
	public void draw() {
		if(counter > 5) {
			drawComponent.setAlpha(255);
		}
		else {
			float result = ((float)counter/5.0f);
			drawComponent.setAlpha(result);
		}
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		drawComponent.setHeight(-height);
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
