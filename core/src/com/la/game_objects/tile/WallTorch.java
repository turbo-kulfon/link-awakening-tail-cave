package com.la.game_objects.tile;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.engine.tile_map.TileAttribute;
import com.engine.tile_map.TileMapComponent;
import com.engine.tile_map.TileMapSystem;
import com.la.aspects.RoomTransition;
import com.la.aspects.wall_bounce.WallBounceResponseCallback;
import com.la.game_objects.tile.torch.TorchAnimationController;
import com.la.game_objects.tile.torch.TorchAnimationController.TorchAnimationCallback;

public class WallTorch implements IGameObject {
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private int uniqueID;
	private boolean remove;

	private TorchAnimationController torchAnimationController;

	public WallTorch(
		int x, int y, int direction,
		GFXSystem gfxSystem,
		SpatialSystem spatialSystem,
		IAspectSystem aspectSystem,
		IUniqueIDManager uniqueIDManager,
		TileMapSystem tileMapSystem) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(296, 16, 16, 16);
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

		TileMapComponent tileMapComponent = tileMapSystem.createComponent(spatialComponent.getCenterX(), spatialComponent.getCenterY());
		tileMapComponent.setTileAttribute(TileAttribute.SOLID);

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));

		torchAnimationController = new TorchAnimationController(new TorchAnimationCallback() {
			@Override
			public void onFrame0() {
				drawComponent.setTexturePosition(296, 32);
			}
			@Override
			public void onFrame1() {
				drawComponent.setTexturePosition(296, 16);
			}
			@Override
			public void onFrame2() {
				drawComponent.setTexturePosition(280, 32);
			}
			@Override
			public void onFrame3() {
				drawComponent.setTexturePosition(280, 16);
			}
		});
	}

	@Override
	public void update() {
		torchAnimationController.update();
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
