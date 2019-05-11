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
import com.engine.util.Coordinate;
import com.engine.util.ICollisionDetection;
import com.engine.util.ICoordinate;
import com.engine.util.ICoordinate.Vector;
import com.la.aspects.LinkCollision;
import com.la.aspects.RoomTransition;

public class Teleport implements IGameObject {
	public interface TeleportCallback {
		void onEnter(float cenX, float cenY);
	}

	private SoundSystem soundSystem;
	private TextureDrawComponent drawComponent;
	private TextureDrawComponent orbs[] = new TextureDrawComponent[4];
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;
	private ICoordinate coordinate;

	private TeleportCallback callback;
	private ICollisionDetection collisionDetection;
	private int uniqueID, angle;
	private boolean remove, enter = true, reEnter = true;

	public Teleport(
			int x, int y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			TeleportCallback callback) {
		this.soundSystem = soundSystem;
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.callback = callback;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(186, 80, 10, 10);
		drawComponent.setSize(10, 10);

		for(int i = 0; i < 4; ++i) {
			orbs[i] = gfxSystem.createTextureDrawComponent(0);
			orbs[i].setTexture(182, 81, 4, 4);
			orbs[i].setSize(4, 4);
		}

		spatialComponent = spatialSystem.createStaticComponent(uniqueID);
		spatialComponent.setCoordinates(x - 4, y - 4, 8, 8);

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

		coordinate = new Coordinate();
	}

	@Override
	public void update() {
		if(enter == true) {
			if(reEnter == false) {
				callback.onEnter(
					spatialComponent.getCenterX(),
					spatialComponent.getCenterY());
				soundSystem.teleportEnter();
			}
			enter = false;
		}
		else {
			reEnter = false;
		}
	}
	@Override
	public void draw() {
		angle += 6;
		if(angle >= 360) {
			angle -= 360;
		}
		drawComponent.setRotation(angle, 5, 5);
		drawComponent.setPosition(
			spatialComponent.getCenterX() - 5,
			spatialComponent.getCenterY() - 5);
		Vector delta = coordinate.angleToDelta(360 - angle);
		orbs[0].setPosition(
			spatialComponent.getCenterX() + delta.x * 9 - 2,
			spatialComponent.getCenterY() + delta.y * 9 - 2);
		delta = coordinate.angleToDelta(360 - (angle + 90));
		orbs[1].setPosition(
			spatialComponent.getCenterX() + delta.x * 9 - 2,
			spatialComponent.getCenterY() + delta.y * 9 - 2);
		delta = coordinate.angleToDelta(360 - (angle + 180));
		orbs[2].setPosition(
			spatialComponent.getCenterX() + delta.x * 9 - 2,
			spatialComponent.getCenterY() + delta.y * 9 - 2);
		delta = coordinate.angleToDelta(360 - (angle + 270));
		orbs[3].setPosition(
			spatialComponent.getCenterX() + delta.x * 9 - 2,
			spatialComponent.getCenterY() + delta.y * 9 - 2);
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
		for (TextureDrawComponent orb : orbs) {
			orb.remove();
		}
		spatialComponent.remove();
		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}
}
