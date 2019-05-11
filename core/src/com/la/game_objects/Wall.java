package com.la.game_objects;

import com.engine.aspect.IAspectSystem;
import com.engine.component.sword.SwordState;
import com.engine.direction.Direction;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.engine.tile_map.TileAttribute;
import com.engine.tile_map.TileMapComponent;
import com.engine.tile_map.TileMapSystem;
import com.engine.util.CollisionDetection;
import com.engine.util.ICollisionDetection;
import com.la.aspects.RoomTransition;
import com.la.aspects.SwordHit;
import com.la.aspects.wall_bounce.WallBounceResponseCallback;

public class Wall implements IGameObject {
	public interface WallCallback {
		void onWallHit(float x, float y);
	}

	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private int uniqueID;
	private boolean remove, swordWallHit;
	private float wallHitX, wallHitY;
	private WallCallback callback;

	public Wall(
		int x, int y, int textureX, int textureY, int wallDirection,
		GFXSystem gfxSystem,
		SpatialSystem spatialSystem,
		IAspectSystem aspectSystem,
		IUniqueIDManager uniqueIDManager,
		TileMapSystem tileMapSystem,
		WallCallback callback) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.callback = callback;
		uniqueID = uniqueIDManager.getUniqueID();

		
		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(textureX, textureY, 16, 16);
		drawComponent.setSize(16, 16);

		spatialComponent = spatialSystem.createStaticComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 16, 16);
		spatialComponent.setCollisionResponse(new WallBounceResponseCallback(aspectSystem, spatialComponent, wallDirection));

		TileMapComponent tileMapComponent = tileMapSystem.createComponent(spatialComponent.getCenterX(), spatialComponent.getCenterY());
		tileMapComponent.setTileAttribute(TileAttribute.SOLID);

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));

		ICollisionDetection collisionDetection = new CollisionDetection();
		aspectSystem.addAspect(new SwordHit(uniqueID) {
			@Override
			public SwordHitResult hit(float swordX, float swordY, float swordW, float swordH, float ownerCX, float ownerCY, Direction direction, int dmg, boolean powerUped, SwordState swordState, int counter) {
				SwordHitResult result = new SwordHitResult(SwordHitResultType.NONE, 0, 0);
				if((swordState == SwordState.THRUST_NO_KEY_BLOCK && counter == 10) && 
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
	}

	@Override
	public void update() {
		if(swordWallHit == true) {
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
}
