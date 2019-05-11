package com.la.game_objects;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.AutoDoor;
import com.la.aspects.RoomTransition;
import com.la.aspects.wall_bounce.WallBounceResponseCallback;

public class Door implements IGameObject {
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private int uniqueID;
	private boolean remove;

	private int mode, counter;

	public Door(
			int x, int y, int direction,
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IAspectSystem  aspectSystem,
			IUniqueIDManager uniqueIDManager) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(222, 48, 16, 16);
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

		mode = 3;
		spatialComponent.setActive(false);

		aspectSystem.addAspect(new AutoDoor(uniqueID) {
			@Override
			public void open() {
				if(mode == 0 || mode == 2) {
					mode = 1;
					counter = 10;
				}
			}
			@Override
			public void close() {
				if(mode == 1 || mode == 3) {
					mode = 2;
					counter = 10;
					spatialComponent.setActive(true);
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
				counter = 0;
				mode = 3;
				spatialComponent.setActive(false);
			}
		}
		else if(mode == 2) {
			counter -= 1;
			if(counter <= 0) {
				counter = 0;
				mode = 0;
			}
		}
	}

	@Override
	public void draw() {
		if(mode == 0) {
			drawComponent.setTexture(222, 48, 16, 16);
			drawComponent.setSize(16, 16);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
			drawComponent.setVisible(true);
		}
		else if(mode == 1) {
			drawComponent.setTexture(222, 56, 16, 8);
			drawComponent.setSize(16, 8);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
			drawComponent.setVisible(true);
		}
		else if(mode == 2) {
			drawComponent.setTexture(222, 56, 16, 8);
			drawComponent.setSize(16, 8);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
			drawComponent.setVisible(true);
		}
		else if(mode == 3) {
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
