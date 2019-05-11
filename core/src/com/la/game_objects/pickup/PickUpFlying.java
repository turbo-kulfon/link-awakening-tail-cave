package com.la.game_objects.pickup;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.LinkCollision;
import com.la.aspects.RoomTransition;
import com.la.equipment.IEquipmentSystem;
import com.la.game_objects.Shadow;
import com.la.game_objects.ShadowDependencyStandard;

public class PickUpFlying implements IGameObject {
	public interface PickUpFlyingCallback {
		void onTaken();
	}

	private TextureDrawComponent pickUp;
	private PickupWings pickupWings;
	private Shadow shadow;
	private ISpatialComponent spatialComponent;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private float height, delta;
	private boolean moveUp = false;
	private boolean remove;
	private int uniqueID;

	public PickUpFlying(float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			IEquipmentSystem equipmentSystem,
			PickUpFlyingCallback callback) {
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;

		uniqueID = uniqueIDManager.getUniqueID();

		pickUp = gfxSystem.createTextureDrawComponent(1);
		pickUp.setTexture(133, 64, 7, 7);
		pickUp.setSize(7, 7);


		pickupWings = new PickupWings(gfxSystem);

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 7, 7);

		shadow = new Shadow(gfxSystem, new ShadowDependencyStandard(spatialComponent));
		height = 16;

		aspectSystem.addAspect(new LinkCollision(uniqueID) {
			@Override
			public void collision(float linkX, float linkY, float linkW, float linkH, float linkHeight, boolean blocked) {
				if(remove == false && linkHeight + 3 >= height) {
					equipmentSystem.fullHeal();
					callback.onTaken();
					setToRemove();
					soundSystem.takeFlyingPickupItem();
				}
			}
		});
		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
	}

	@Override
	public void update() {
		if(moveUp == true) {
			height += delta;
			delta -= 0.005f;
			if(delta < -0.10f) {
				delta = -0.10f;
				moveUp = false;
			}
		}
		else {
			height += delta;
			delta += 0.005f;
			if(delta > 0.10f) {
				delta = 0.10f;
				moveUp = true;
			}
		}
		pickupWings.update();
	}
	@Override
	public void draw() {
		pickUp.setPosition(spatialComponent.getX(), spatialComponent.getY());
		pickUp.setHeight(-height);
		pickupWings.draw(spatialComponent.getCenterX(), spatialComponent.getCenterY(), height);

		shadow.draw();
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
		pickUp.remove();
		shadow.remove();
		pickupWings.remove();
		spatialComponent.remove();
		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}
}
