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
import com.la.factory.IGameStateFactory;

public class NightmareDoor implements IGameObject {
	public interface NightmareDoorCallback {
		void onDoorOpen();
	}

	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private int uniqueID;
	private boolean remove;

	private int mode, counter;

	public NightmareDoor(
			int x, int y, int dungeonID, int direction,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IAspectSystem  aspectSystem,
			IUniqueIDManager uniqueIDManager,
			IGameStateFactory gameStateFactory,
			IEquipmentSystem equipmentSystem,
			NightmareDoorCallback callback) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(344, 16, 16, 16);
		drawComponent.setSize(16, 16);
		if(direction == 0) {
			drawComponent.setRotation(270, 8, 8);
		}
		else if(direction == 1) {
			drawComponent.setRotation(90, 8, 8);
		}
		else if(direction == 3) {
			drawComponent.setRotation(180, 8, 8);
		}

		spatialComponent = spatialSystem.createStaticComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 16, 16);
		spatialComponent.setCollisionResponse(new WallBounceResponseCallback(aspectSystem, spatialComponent));

		aspectSystem.addAspect(new LinkCollision(uniqueID) {
			@Override
			public void collision(float linkX, float linkY, float linkW, float linkH, float linkHeight, boolean blocked) {
				if(mode == 0 && linkHeight <= 0) {
					if(equipmentSystem.useNightmareKey(dungeonID) == true) {
						mode = 1;
						counter = 10;
						callback.onDoorOpen();
						soundSystem.openChest();
					}
					else {
						gameStateFactory.createTextState(
							  "Eh? It's locked!|"
							+ "You can open the|"
							+ "door with the|"
							+ "Nightmare Key.");
					}
				}
			}
		});
		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
	}

	@Override
	public void update() {
		if(mode == 1) {
			drawComponent.setTexture(344, 22, 16, 8);
			drawComponent.setSize(16, 8);
			counter -= 1;
			if(counter <= 0) {
				counter = 0;
				mode = 2;
				spatialComponent.setActive(false);
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
