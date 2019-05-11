package com.la.game_objects;

import com.engine.aspect.IAspectSystem;
import com.engine.component.sword.SwordState;
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
import com.la.aspects.BombHit;
import com.la.aspects.RoomTransition;
import com.la.aspects.SwordHit;
import com.la.aspects.wall_bounce.WallBounceResponseCallback;

public class DestructibleWall implements IGameObject {
	public interface DestructibleWallCallback {
		void onWallHit(float x, float y);
		void onDestroy();
	}

	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private int uniqueID, wallDirection;
	private boolean remove, destroyed, swordWallHit;
	private float wallHitX, wallHitY;
	private DestructibleWallCallback callback;

	public DestructibleWall(
		int x, int y, int wallDirection,
		boolean isDestroyed,
		GFXSystem gfxSystem,
		SoundSystem soundSystem,
		SpatialSystem spatialSystem,
		IAspectSystem aspectSystem,
		IUniqueIDManager uniqueIDManager,
		TileMapSystem tileMapSystem,
		DestructibleWallCallback callback) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.callback = callback;
		this.wallDirection = wallDirection;
		destroyed = isDestroyed;
		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(0);
		setImage();
		drawComponent.setSize(16, 16);

		spatialComponent = spatialSystem.createStaticComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 16, 16);
		spatialComponent.setCollisionResponse(new WallBounceResponseCallback(aspectSystem, spatialComponent, wallDirection) {
			@Override
			public void response(int collidedID) {
				if(destroyed == false) {
					super.response(collidedID);
				}
			}
		});

		TileMapComponent tileMapComponent = tileMapSystem.createComponent(spatialComponent.getCenterX(), spatialComponent.getCenterY());
		tileMapComponent.setTileAttribute(TileAttribute.SOLID);

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));

		ICollisionDetection collisionDetection = new CollisionDetection();
		aspectSystem.addAspect(new SwordHit(uniqueID) {
			@Override
			public SwordHitResult hit(float swordX, float swordY, float swordW, float swordH, float ownerCX, float ownerCY, Direction direction, int dmg, boolean powerUped, SwordState swordState, int counter) {
				SwordHitResult result = new SwordHitResult(SwordHitResultType.NONE, 0, 0);
				if(destroyed == false && (swordState == SwordState.THRUST_NO_KEY_BLOCK && counter == 10) && 
				   collisionDetection.collisionDetect(
				   spatialComponent.getX(), spatialComponent.getY(), spatialComponent.getW(), spatialComponent.getH(),
				   swordX + swordW/2.0f, swordY + 3 + swordH/2.0f)) {
					result.type = SwordHitResultType.HIT;
					swordWallHit = true;
					wallHitX = swordX + swordW/2.0f;
					wallHitY = swordY + 3 + swordH/2.0f;
				}
				return result;
			}
		});
		aspectSystem.addAspect(new BombHit(uniqueID) {
			@Override
			public void hit(float bombCenterX, float bombCenterY, boolean playerIsOwner) {
				if(destroyed == false) {
					destroyed = true;
					callback.onDestroy();
					setImage();
					soundSystem.secretSolved();
				}
			}
		});
	}

	@Override
	public void update() {
		if(destroyed == false && swordWallHit == true) {
			callback.onWallHit(wallHitX, wallHitY);
			swordWallHit = false;
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

	private void setImage() {
		if(destroyed == false) {
			drawComponent.setTexture(344, 0, 16, 16);
			if(wallDirection == 0) {
				drawComponent.setRotation(270, 8, 8);
			}
			else if(wallDirection == 1) {
				drawComponent.setRotation(90, 8, 8);
			}
			else if(wallDirection == 2) {
				drawComponent.setRotation(90, 8, 8);
			}
			else if(wallDirection == 3) {
				drawComponent.setRotation(180, 8, 8);
			}
		}
		else {
			drawComponent.setTexture(360, 0, 16, 16);
			if(wallDirection == 0) {
				drawComponent.setRotation(0, 8, 8);
			}
			else if(wallDirection == 1) {
				drawComponent.setRotation(0, 8, 8);
			}
			else if(wallDirection == 2) {
				drawComponent.setRotation(90, 8, 8);
			}
			else if(wallDirection == 3) {
				drawComponent.setRotation(90, 8, 8);
			}
		}
	}
}
