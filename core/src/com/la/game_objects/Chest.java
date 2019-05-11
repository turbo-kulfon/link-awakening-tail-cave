package com.la.game_objects;

import com.engine.aspect.IAspectSystem;
import com.engine.direction.Direction;
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
import com.engine.util.CollisionDetection;
import com.engine.util.ICollisionDetection;
import com.la.aspects.LinkInteraction;
import com.la.aspects.RoomTransition;
import com.la.aspects.wall_bounce.WallBounceResponseCallback;
import com.la.game_objects.link.ILinkData;

public class Chest implements IGameObject {
	public interface ChestCallback {
		void onOpen(float chestX, float chestY);
	}

	private TextureDrawComponent drawComponent, poof;
	private ISpatialComponent spatialComponent;
	private ICollisionDetection collisionDetection;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private boolean opened, spawned;
	private int spawnAnimation;

	private int uniqueID;
	private boolean remove;

	public Chest(int x, int y, boolean opened, boolean spawned,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			ILinkData linkData,
			ChestCallback callback,
			TileMapSystem tileMapSystem) {
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;
		this.opened = opened;
		this.spawned = spawned;
		spawnAnimation = 15;
		collisionDetection = new CollisionDetection();

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexturePosition(264, 0);
		drawComponent.setTextureSize(16, 16);
		drawComponent.setSize(16, 16);
		if(spawned == true) {
			drawComponent.setVisible(false);
		}
		poof = gfxSystem.createTextureDrawComponent(0);
		poof.setTextureSize(16, 16);
		poof.setSize(16, 16);
		poof.setVisible(false);

		spatialComponent = spatialSystem.createStaticComponent(uniqueID);
		int finalY = y;
		while(collisionDetection.collisionDetect(
			linkData.getX(), linkData.getY(), linkData.getW(), linkData.getH(),
			x, finalY, 16, 16) == true) {
			finalY += 8;
		}
		spatialComponent.setCoordinates(x, finalY, 16, 16);
		spatialComponent.setCollisionResponse(new WallBounceResponseCallback(aspectSystem, spatialComponent));

		TileMapComponent tileMapComponent = tileMapSystem.createComponent(spatialComponent.getCenterX(), spatialComponent.getCenterY());
		tileMapComponent.setTileAttribute(TileAttribute.SOLID);


		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		if(opened == false) {
			aspectSystem.addAspect(new LinkInteraction(uniqueID) {
				@Override
				public boolean interact(float linkX, float linkY, float linkW, float linkH, Direction direction) {
					if(Chest.this.opened == false && direction == Direction.UP) {
						if(collisionDetection.collisionDetect(
								linkX, linkY - 1, linkW, linkH,
								spatialComponent.getX(), spatialComponent.getY(),
								spatialComponent.getW(), spatialComponent.getH()) == true) {
							Chest.this.opened = true;
							callback.onOpen(spatialComponent.getX(), spatialComponent.getY());
							soundSystem.openChest();
							return true;
						}
					}
					return false;
				}
			});
		}
	}

	@Override
	public void update() {
		if(spawned == true) {
			poof.setVisible(true);
			spawnAnimation -= 1;
			if(spawnAnimation <= 0) {
				spawned = false;
				poof.setVisible(false);
			}
		}
	}
	@Override
	public void draw() {
		if(opened == false) {
			drawComponent.setTexturePosition(264, 0);
		}
		else {
			drawComponent.setTexturePosition(280, 0);
		}
		if(spawned == true) {
			if(spawnAnimation >= 7) {
				poof.setTexturePosition(190, 48);
			}
			else {
				poof.setTexturePosition(216, 32);
			}
			if(spawnAnimation == 4) {
				drawComponent.setVisible(true);
			}
		}
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		poof.setPosition(spatialComponent.getX(), spatialComponent.getY());
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
