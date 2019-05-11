package com.la.game_objects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.component.sword.SwordState;
import com.engine.direction.Direction;
import com.engine.direction.DirectionByDelta;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.IOutsideViewCheck;
import com.engine.spatial.IOutsideViewCheck.OutsideViewCheckCallback;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.OutsideViewCheck;
import com.engine.spatial.OutsideViewCheckPosition;
import com.engine.spatial.SpatialSystem;
import com.engine.util.Coordinate;
import com.engine.util.ICoordinate.Vector;
import com.la.aspects.RoomTransition;
import com.la.aspects.SwordHit;
import com.la.aspects.enemy_hit.EnemyHit;
import com.la.aspects.enemy_hit.EnemyHit.Result;
import com.la.aspects.enemy_hit.EnemyHit.ResultType;
import com.la.factory.IRoomFactory;
import com.la.game_objects.link.ILinkData;

public class Fireball implements IGameObject {
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private int uniqueID;
	private boolean remove;
	private DirectionByDelta direction;
	private IOutsideViewCheck outsideViewCheck;

	public Fireball(
			float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			IRoomFactory roomFactory,
			ILinkData linkData) {
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(186, 80, 10, 10);
		drawComponent.setSize(10, 10);
		drawComponent.setSpriteOffset(-1, -1);

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x-4, y-4, 8, 8);
		spatialComponent.setCollisionResponse((collidedID)-> {
			EnemyHit enemyHit = aspectSystem.getAspect(collidedID, AspectType.ENEMY_HIT);
			if(enemyHit != null) {
				Result result = enemyHit.hit(
						spatialComponent.getX(), spatialComponent.getY(),
						spatialComponent.getW(), spatialComponent.getH(),
						direction.getDirection(), 11, 2, 0);
				if(result.type == ResultType.HIT ||
				   result.type == ResultType.SHIELD) {
					setToRemove();
					soundSystem.enemyHit();
					roomFactory.createFireballDefeated((int)spatialComponent.getCenterX(), (int)spatialComponent.getCenterY());
				}
			}
		});

		Coordinate coordinate = new Coordinate();
		Vector delta = coordinate.calculateDelta(
			spatialComponent.getCenterX(), spatialComponent.getCenterY(),
			linkData.getCenterX(), linkData.getCenterY());
		spatialComponent.setDelta(delta.x, delta.y);

		aspectSystem.addAspect(new SwordHit(uniqueID) {
			@Override
			public SwordHitResult hit(float swordX, float swordY, float swordW, float swordH, float ownerCX, float ownerCY, Direction direction, int dmg, boolean powerUped, SwordState swordState, int counter) {
				if(remove == false) {
					setToRemove();
					roomFactory.createFireballDefeated((int)spatialComponent.getCenterX(), (int)spatialComponent.getCenterY());
					soundSystem.enemyHit();
					return new SwordHitResult(SwordHitResultType.HIT, spatialComponent.getCenterX(), spatialComponent.getCenterY());
				}
				return new SwordHitResult(SwordHitResultType.NONE, spatialComponent.getCenterX(), spatialComponent.getCenterY());
			}
		});
		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));

		direction = new DirectionByDelta();
		direction.update(spatialComponent.getDeltaX(), spatialComponent.getDeltaY());

		outsideViewCheck = new OutsideViewCheck(new OutsideViewCheckPosition(spatialComponent), 
			new OutsideViewCheckCallback() {
				@Override
				public void outsideLeft() {
					setToRemove();
				}
				@Override
				public void outsideRight() {
					setToRemove();
				}
				@Override
				public void outsideUp() {
					setToRemove();
				}
				@Override
				public void outsideDown() {
					setToRemove();
				}
			}
		);
	}

	@Override
	public void update() {
		outsideViewCheck.update();
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
