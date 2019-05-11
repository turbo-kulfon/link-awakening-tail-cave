package com.la.game_objects.tile;

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
import com.la.aspects.RoomTransition;
import com.la.aspects.SwordHit;
import com.la.aspects.wall_bounce.WallBounceResponseCallback;
import com.la.factory.IRoomFactory;

public class PurpleCrystal implements IGameObject {
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;
	private IRoomFactory roomFactory;

	private boolean remove;
	private int uniqueID;

	public PurpleCrystal(float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IRoomFactory roomFactory,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.roomFactory = roomFactory;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(186, 64, 16, 16);
		drawComponent.setSize(16, 16);

		spatialComponent = spatialSystem.createStaticComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 16, 16);
		spatialComponent.setCollisionResponse(new WallBounceResponseCallback(aspectSystem, spatialComponent));

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		aspectSystem.addAspect(new SwordHit(uniqueID) {
			@Override
			public SwordHitResult hit(float swordX, float swordY, float swordW, float swordH, float ownerCX, float ownerCY, Direction direction, int dmg, boolean powerUped, SwordState swordState, int counter) {
				if(remove == false) {
					setToRemove();
					soundSystem.destroyCrystal();
					return new SwordHitResult(SwordHitResultType.HIT, spatialComponent.getCenterX(), spatialComponent.getCenterY());
				}
				return new SwordHitResult(SwordHitResultType.NONE, spatialComponent.getCenterX(), spatialComponent.getCenterY());
			}
		});
	}

	@Override
	public void update() {
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

		roomFactory.createDebris(spatialComponent.getCenterX() - 2, spatialComponent.getY() + 6, true);
		roomFactory.createDebris(spatialComponent.getCenterX() - 2, spatialComponent.getY() + 14, true);
		roomFactory.createDebris(spatialComponent.getCenterX() + 2, spatialComponent.getY() + 6, false);
		roomFactory.createDebris(spatialComponent.getCenterX() + 2, spatialComponent.getY() + 12, false);
	}
}
