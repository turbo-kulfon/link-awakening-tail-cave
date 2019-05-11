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
import com.la.aspects.HeightObject;
import com.la.aspects.RoomTransition;

public class FloorButton implements IGameObject {
	public interface FloorButtonCallback {
		void onButtonPressed();
	}

	private SoundSystem soundSystem;
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private FloorButtonCallback callback;

	private boolean remove;
	private int uniqueID;
	private int mode;
	private int counter = 30;
	private boolean linkCollision;

	public FloorButton(float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			FloorButtonCallback callback) {
		this.soundSystem = soundSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;
		this.callback = callback;
		
		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(328, 0, 16, 16);
		drawComponent.setSize(16, 16);

		spatialComponent = spatialSystem.createStaticComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 16, 16);
		spatialComponent.setCollisionResponse((collidedID)-> {
			if(mode == 0) {
				HeightObject heightObject = aspectSystem.getAspect(collidedID, AspectType.HEIGHT_OBJECT);
				if(heightObject != null) {
					if(heightObject.collision(
							spatialComponent.getX(), spatialComponent.getY(),
							spatialComponent.getW(), spatialComponent.getH(), 2) == true) {
						heightObject.setHeight(2);
						linkCollision = true;
					}
				}
			}
		});

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
	}

	@Override
	public void update() {
		if(mode == 0) {
			if(linkCollision == true) {
				counter -= 1;
				if(counter <= 0) {
					mode = 1;
					drawComponent.setTexture(328, 16, 16, 16);
					callback.onButtonPressed();
					soundSystem.switchPressed();
				}
				linkCollision = false;
			}
			else {
				counter = 30;
			}
		}
		else if(mode == 1) {
			
		}
	}
	@Override
	public void draw() {
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

		uniqueIDManager.returnID(uniqueID);
		aspectSystem.removeAspects(uniqueID);
	}
}
