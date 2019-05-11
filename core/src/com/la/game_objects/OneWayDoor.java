package com.la.game_objects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.OneWayDoorAspect;
import com.la.aspects.RoomTransition;

public class OneWayDoor implements IGameObject {
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private boolean remove;
	private int uniqueID;

	private int mode;
	private int counter = 80;
	private OneWayDoorAspect aspect;

	public OneWayDoor(float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem) {
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(206, 63, 16, 16);
		drawComponent.setSize(16, 16);

		spatialComponent = spatialSystem.createStaticComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 16, 16);
		spatialComponent.setCollisionResponse((collidedID)-> {
			if(mode == 0) {
				OneWayDoorAspect oneWayDoorAspect = aspectSystem.getAspect(collidedID, AspectType.ONE_WAY_DOOR);
				if(oneWayDoorAspect != null) {
					if(oneWayDoorAspect.collision(
						spatialComponent.getX(), spatialComponent.getY(),
						spatialComponent.getW(), spatialComponent.getH()) == true) {
						soundSystem.oneWayDoor();
						mode = 1;
						oneWayDoorAspect.enterDoor();
						aspect = oneWayDoorAspect;
					}
				}
			}
		});

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
	}

	@Override
	public void update() {
		if(mode == 1) {
			counter -= 1;
			if(counter <= 0) {
				aspect.exitDoor();
				mode = 2;
			}
		}
	}
	@Override
	public void draw() {
		if(mode == 1) {
			if(counter <= 80 && counter >= 70) {
				drawComponent.setTexturePosition(222, 64);
			}
			else if(counter <= 70 && counter >= 60) {
				drawComponent.setTexturePosition(238, 64);
			}
			else if(counter <= 60 && counter >= 50) {
				drawComponent.setTexturePosition(254, 64);
			}
			else if(counter <= 50 && counter >= 30) {
				drawComponent.setTexturePosition(286, 64);
			}
			else if(counter <= 30 && counter >= 20) {
				drawComponent.setTexturePosition(270, 64);
			}
			else if(counter <= 20 && counter >= 10) {
				drawComponent.setTexturePosition(254, 64);
			}
			else if(counter < 10) {
				drawComponent.setTexturePosition(206, 64);
			}
		}
		drawComponent.setPosition(
				spatialComponent.getX(),
				spatialComponent.getY());
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
