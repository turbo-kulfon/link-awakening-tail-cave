package com.la.game_objects;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.RoomTransition;
import com.la.aspects.hidden.DetectHidden;
import com.la.aspects.wall_bounce.WallBounceResponseCallback;
import com.la.factory.IRoomFactory;
import com.la.game_objects.tile.torch.TorchAnimationController;
import com.la.game_objects.tile.torch.TorchAnimationController.TorchAnimationCallback;

public class AggressiveGroundTorch implements IGameObject {
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;
	private DetectHidden detectHidden;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;
	private IRoomFactory roomFactory;
	private TorchAnimationController torchAnimationController;

	private int uniqueID;
	private boolean remove;
	private int nextShoot;

	public AggressiveGroundTorch(
		int x, int y, int shootCounter,
		GFXSystem gfxSystem,
		SpatialSystem spatialSystem,
		IAspectSystem aspectSystem,
		IUniqueIDManager uniqueIDManager,
		IRoomFactory roomFactory) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.roomFactory = roomFactory;
		nextShoot = shootCounter;

		uniqueID = uniqueIDManager.getUniqueID();
		
		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(264, 16, 16, 16);
		drawComponent.setSize(16, 16);

		spatialComponent = spatialSystem.createStaticComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 16, 16);
		spatialComponent.setCollisionResponse(new WallBounceResponseCallback(aspectSystem, spatialComponent));

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));

		detectHidden = new DetectHidden(aspectSystem);

		torchAnimationController = new TorchAnimationController(new TorchAnimationCallback() {
			@Override
			public void onFrame0() {
				drawComponent.setTexturePosition(264, 16);
			}
			@Override
			public void onFrame1() {
				drawComponent.setTexturePosition(248, 32);
			}
			@Override
			public void onFrame2() {
				drawComponent.setTexturePosition(248, 16);
			}
			@Override
			public void onFrame3() {
				drawComponent.setTexturePosition(264, 32);
			}
		});
	}

	@Override
	public void update() {
		torchAnimationController.update();
		if(detectHidden.isEnemyUnhidden() == true) {
			nextShoot -= 1;
			if(nextShoot <= 0) {
				roomFactory.createFireball((int)spatialComponent.getCenterX(), (int)spatialComponent.getCenterY());
				nextShoot = 120;
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
