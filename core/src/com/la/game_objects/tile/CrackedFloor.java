package com.la.game_objects.tile;

import com.engine.aspect.AspectType;
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
import com.engine.util.CollisionDetection;
import com.engine.util.ICollisionDetection;
import com.la.aspects.HoleCollision;
import com.la.aspects.LinkCollision;
import com.la.aspects.RoomTransition;

public class CrackedFloor implements IGameObject {
	private TextureDrawComponent drawComponent;
	private SoundSystem soundSystem;
	private ISpatialComponent spatialComponent;
	private TileMapComponent tileMapComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private int uniqueID;
	private boolean remove;

	private boolean cracked, linkCollision;
	private int counter = 45;

	public CrackedFloor(
		int x, int y,
		GFXSystem gfxSystem,
		SoundSystem soundSystem,
		SpatialSystem spatialSystem,
		IAspectSystem aspectSystem,
		IUniqueIDManager uniqueIDManager,
		TileMapSystem tileMapSystem) {
		this.soundSystem = soundSystem;
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		uniqueID = uniqueIDManager.getUniqueID();
		
		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(312, 0, 16, 16);
		drawComponent.setSize(16, 16);

		spatialComponent = spatialSystem.createStaticComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 16, 16);
		spatialComponent.setCollisionResponse((collidedID)-> {
			if(cracked == true) {
				HoleCollision holeCollision = aspectSystem.getAspect(collidedID, AspectType.HOLE_COLLISION);
				if(holeCollision != null) {
					holeCollision.collision(
						spatialComponent.getX(), spatialComponent.getY(),
						spatialComponent.getW(), spatialComponent.getH(), true);
				}
			}
		});

		tileMapComponent = tileMapSystem.createComponent(spatialComponent.getCenterX(), spatialComponent.getCenterY());
		tileMapComponent.setTileAttribute(TileAttribute.EMPTY);

		ICollisionDetection collisionDetection = new CollisionDetection();
		aspectSystem.addAspect(new LinkCollision(uniqueID) {
			@Override
			public void collision(float linkX, float linkY, float linkW, float linkH, float linkHeight, boolean blocked) {
				if(cracked == false && linkHeight <= 0) {
					if(collisionDetection.collisionDetect(
							spatialComponent.getX(), spatialComponent.getY(),
							spatialComponent.getW(), spatialComponent.getH(),
							linkX + linkW/2.0f, linkY + linkH/2.0f) == true) {
						linkCollision = true;
					}
				}
			}
		});
		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
	}

	@Override
	public void update() {
		if(cracked == false) {
			if(linkCollision == true) {
				counter -= 1;
				if(counter <= 0) {
					cracked = true;
					drawComponent.setTexture(376, 0, 16, 16);
					tileMapComponent.setTileAttribute(TileAttribute.SOLID);
					soundSystem.floorCrumble();
				}
				linkCollision = false;
			}
			else {
				counter = 45;
			}
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
