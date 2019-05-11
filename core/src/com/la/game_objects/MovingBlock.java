package com.la.game_objects;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.engine.tile_map.TileAttribute;
import com.engine.tile_map.TileMapComponent;
import com.engine.tile_map.TileMapSystem;
import com.la.aspects.LinkCollision;
import com.la.aspects.RoomTransition;
import com.la.aspects.wall_bounce.WallBounceResponseCallback;

public class MovingBlock implements IGameObject {
	public interface MovingBlockCallback {
		void onMoveEnd();
	}

	private SoundSystem soundSystem;
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;
	private TileMapComponent tileMapComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;
	private MovingBlockCallback callback;

	private int uniqueID;
	private boolean remove;

	private boolean linkCollision;
	private int mode;
	private int dir, counter = 30;
	private float path, speed = 0.5f;

	public MovingBlock(float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			TileMapSystem tileMapSystem,
			MovingBlockCallback callback) {
		this.soundSystem = soundSystem;
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.callback = callback;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(312, 16, 16, 16);
		drawComponent.setSize(16, 16);

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 16, 16);
		spatialComponent.setCollisionResponse(new WallBounceResponseCallback(aspectSystem, spatialComponent));

		aspectSystem.addAspect(new LinkCollision(uniqueID) {
			@Override
			public void collision(float linkX, float linkY, float linkW, float linkH, float linkHeight, boolean blocked) {
				if(mode == 0 && linkHeight <= 0) {
					linkCollision = true;
					float dx = spatialComponent.getCenterX() - (linkX + linkW/2.0f);
					float dy = spatialComponent.getCenterY() - (linkY + linkH/2.0f);
					if(Math.abs(dx) > Math.abs(dy)) {
						if(dx >= 0) {
							dir = 1;
						}
						else {
							dir = 0;
						}
					}
					else {
						if(dy >= 0) {
							dir = 3;
						}
						else {
							dir = 2;
						}
					}
				}
			}
		});
		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));

		tileMapComponent = tileMapSystem.createComponent(spatialComponent.getCenterX(), spatialComponent.getCenterY());
		tileMapComponent.setTileAttribute(TileAttribute.SOLID);
	}

	@Override
	public void update() {
		if(mode == 0) {
			if(linkCollision == true) {
				counter -= 1;
				if(counter <= 0) {
					mode = 1;
					path = 16;
					soundSystem.rockPush();
				}
				linkCollision = false;
			}
			else {
				counter = 30;
			}
		}
		else if(mode == 1) {
			path -= speed;
			if(path >= 0) {
				if(dir == 0) {
					spatialComponent.setDelta(-speed,  0);
				}
				else if(dir == 1) {
					spatialComponent.setDelta( speed,  0);
				}
				else if(dir == 2) {
					spatialComponent.setDelta( 0, -speed);
				}
				else if(dir == 3) {
					spatialComponent.setDelta( 0,  speed);
				}
			}
			else {
				mode = 2;
				spatialComponent.stop();
				tileMapComponent.changePosition(
					spatialComponent.getCenterX(),
					spatialComponent.getCenterY(),
					TileAttribute.EMPTY,
					TileAttribute.SOLID);
				callback.onMoveEnd();
			}
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

		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}
}
