package com.la.game_objects;

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
import com.la.aspects.wall_bounce.WallBounceResponseCallback;
import com.la.equipment.IEquipmentSystem;

public class KeyLock implements IGameObject {
	public interface KeyLockCallback {
		void onLockOpen();
	}

	private SoundSystem soundSystem;
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;
	private IEquipmentSystem equipmentSystem;

	private int uniqueID;
	private boolean remove;

	private int dungeonID;
	private int mode, counter;
	private boolean linkCollision;
	private KeyLockCallback callback;

	public KeyLock(
			int x, int y, int dungeonID,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IAspectSystem  aspectSystem,
			IUniqueIDManager uniqueIDManager,
			IEquipmentSystem equipmentSystem,
			KeyLockCallback callback) {
		this.soundSystem = soundSystem;
		this.dungeonID = dungeonID;
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.equipmentSystem = equipmentSystem;
		this.callback = callback;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(296, 0, 16, 16);
		drawComponent.setSize(16, 16);

		spatialComponent = spatialSystem.createStaticComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 16, 16);
		spatialComponent.setCollisionResponse(new WallBounceResponseCallback(aspectSystem, spatialComponent));

		aspectSystem.addAspect(new LinkCollision(uniqueID) {
			@Override
			public void collision(float linkX, float linkY, float linkW, float linkH, float linkHeight, boolean blocked) {
				if(mode == 0 && linkHeight <= 0) {
					linkCollision = true;
				}
			}
		});
		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
	}

	@Override
	public void update() {
		if(mode == 0) {
			if(linkCollision == true) {
				if(counter > 0) {
					counter -= 1;
				}
				else {
					if(equipmentSystem.useKey(dungeonID) == true) {
						mode = 1;
						counter = 14;
						spatialComponent.setActive(false);
						callback.onLockOpen();
						soundSystem.openChest();
					}
				}
			}
			else {
				counter = 30;
			}
			linkCollision = false;
		}
		else if(mode == 1) {
			counter -= 1;
			if(counter > 7) {
				drawComponent.setTexture(190, 48, 16, 16);
			}
			else {
				drawComponent.setTexture(216, 32, 16, 16);
			}
			if(counter <= 0) {
				counter = 0;
				mode = 2;
			}
		}
	}

	@Override
	public void draw() {
		if(mode == 0 || mode == 1) {
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		}
		else {
			drawComponent.setVisible(false);
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
