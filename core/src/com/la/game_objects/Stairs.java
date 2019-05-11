package com.la.game_objects;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.engine.util.CollisionDetection;
import com.engine.util.ICollisionDetection;
import com.la.aspects.LinkCollision;
import com.la.aspects.RoomTransition;

public class Stairs implements IGameObject {
	public interface StairsCallback {
		void onEnter();
	}

	private SoundSystem soundSystem;
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private StairsCallback callback;
	private ICollisionDetection collisionDetection;
	private int uniqueID;
	private boolean remove, enter = true, reEnter = true;

	public Stairs(
			int x, int y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			StairsCallback callback) {
		this.soundSystem = soundSystem;
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.callback = callback;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(312, 32, 16, 16);
		drawComponent.setSize(16, 16);

		spatialComponent = spatialSystem.createStaticComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 16, 16);

		collisionDetection = new CollisionDetection();

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		aspectSystem.addAspect(new LinkCollision(uniqueID) {
			@Override
			public void collision(float linkX, float linkY, float linkW, float linkH, float linkHeight, boolean blocked) {
				if(linkHeight <= 0 && collisionDetection.collisionDetect(
					spatialComponent.getX(), spatialComponent.getY(),
					spatialComponent.getW(), spatialComponent.getH(),
					linkX + linkW/2.0f, linkY + linkH/2.0f)) {
					enter = true;
					if(blocked == true) {
						reEnter = true;
					}
				}
			}
		});
	}

	@Override
	public void update() {
		if(enter == true) {
			if(reEnter == false) {
				callback.onEnter();
				soundSystem.enterStairs();
			}
			enter = false;
		}
		else {
			reEnter = false;
		}
	}
	@Override
	public void draw() {
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
