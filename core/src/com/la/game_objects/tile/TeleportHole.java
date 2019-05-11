package com.la.game_objects.tile;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.HoleCollision;
import com.la.aspects.HoleCollision.HoleCollisionResult;
import com.la.aspects.RoomTransition;
import com.la.room.transition.HoleTeleportSystem;

public class TeleportHole implements IGameObject {
	private TextureDrawComponent drawComponent;
	private TextureDrawComponent borders[] = new TextureDrawComponent[2];
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private int uniqueID;
	private boolean remove;

	public TeleportHole(
		int x, int y, int bordersType, int roomID,
		GFXSystem gfxSystem,
		SpatialSystem spatialSystem,
		IAspectSystem aspectSystem,
		IUniqueIDManager uniqueIDManager,
		HoleTeleportSystem holeTeleportSystem) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		uniqueID = uniqueIDManager.getUniqueID();
		
		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(328, 32, 16, 16);
		drawComponent.setSize(16, 16);

		if(bordersType == 0) {
			TextureDrawComponent border1 = gfxSystem.createTextureDrawComponent(0);
			border1.setTexture(376, 0, 16, 8);
			border1.setSize(16, 8);
			borders[0] = border1;
			TextureDrawComponent border2 = gfxSystem.createTextureDrawComponent(0);
			border2.setTexture(376, 8, 16, 8);
			border2.setSize(16, 8);
			borders[1] = border2;
		}
		else if(bordersType == 1) {
			TextureDrawComponent border = gfxSystem.createTextureDrawComponent(0);
			border.setTexture(376, 0, 16, 8);
			border.setSize(16, 8);
			borders[0] = border;
		}
		else if(bordersType == 2) {
			TextureDrawComponent border = gfxSystem.createTextureDrawComponent(0);
			border.setTexture(376, 8, 16, 8);
			border.setSize(16, 8);
			borders[1] = border;
		}

		spatialComponent = spatialSystem.createStaticComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 16, 16);
		spatialComponent.setCollisionResponse((collidedID)-> {
			HoleCollision holeCollision = aspectSystem.getAspect(collidedID, AspectType.HOLE_COLLISION);
			if(holeCollision != null) {
				if(holeCollision.collision(
					spatialComponent.getX(), spatialComponent.getY(),
					spatialComponent.getW(), spatialComponent.getH(), false) == HoleCollisionResult.LINK) {
					holeTeleportSystem.initialize(roomID);
				}
			}
		});

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
	}

	@Override
	public void update() {
	}

	@Override
	public void draw() {
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		if(borders[0] != null) {
			borders[0].setPosition(spatialComponent.getX(), spatialComponent.getY());
		}
		if(borders[1] != null) {
			borders[1].setPosition(spatialComponent.getX(), spatialComponent.getY() + 8);
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
		for (TextureDrawComponent border : borders) {
			if(border != null) {
				border.remove();
			}
		}
		spatialComponent.remove();
		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}
}
