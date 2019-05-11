package com.la.game_objects.enemy;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.component.fall.FallComponent;
import com.engine.component.fall.FallComponent.FallDependency;
import com.engine.component.fall.IFallComponent;
import com.engine.component.recoil.IRecoil;
import com.engine.component.recoil.Recoil;
import com.engine.component.recoil.RecoilDependencyStandard;
import com.engine.component.sword.SwordState;
import com.engine.direction.Direction;
import com.engine.direction.DirectionByDelta;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.engine.util.CollisionDetection;
import com.engine.util.Coordinate;
import com.engine.util.ICollisionDetection;
import com.engine.util.ICoordinate;
import com.engine.util.ICoordinate.Vector;
import com.la.aspects.EnemyTag;
import com.la.aspects.HoleCollision;
import com.la.aspects.RoomTransition;
import com.la.aspects.SwordHit;
import com.la.aspects.enemy_hit.EnemyHit;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.game_objects.link.ILinkData;

public class TestEnemy implements IGameObject {
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;
	private IRecoil recoilComponent;
	private IFallComponent fallComponent;
	private ICoordinate coordinate;
	private DirectionByDelta direction;
	private IEnemyDefeatedPrize prize;
	private ICollisionDetection collisionDetection;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;
	private ILinkData linkData;

	private int uniqueID;
	private boolean remove, dead;

	public TestEnemy(int x, int y,
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			IEnemyDefeatedPrize prize,
			ILinkData linkData) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.prize = prize;
		this.linkData = linkData;

		prize.setRupeeDropChance(50);
		prize.setHeartDropChance(25);

		collisionDetection = new CollisionDetection();

		uniqueID = uniqueIDManager.getUniqueID();

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 12, 10);
		spatialComponent.setCollisionResponse((collidedID)-> {
			if(dead == false) {
				EnemyHit enemyHit = aspectSystem.getAspect(collidedID, AspectType.ENEMY_HIT);
				if(enemyHit != null) {
					EnemyHit.Result result = enemyHit.hit(
							spatialComponent.getX(), spatialComponent.getY(),
							spatialComponent.getW(), spatialComponent.getH(),
							direction.getDirection(), 0, 2, 0);
					if(result.type == EnemyHit.ResultType.SHIELD) {
						recoilComponent.hit(result.cx, result.cy, 30, 15);
					}
				}
			}
		});

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(480, 26, 16, 16);
		drawComponent.setSize(16, 16);
		drawComponent.setSpriteOffset(-2, -6);

		coordinate = new Coordinate();

		recoilComponent = new Recoil(new RecoilDependencyStandard(spatialComponent));
		fallComponent = new FallComponent(new FallDependency() {
			@Override
			public float getCenterX() {
				return spatialComponent.getCenterX();
			}
			@Override
			public float getCenterY() {
				return spatialComponent.getCenterY();
			}
			@Override
			public void update(float dx, float dy, int counter) {
				spatialComponent.setDelta(dx, dy);
			}
			
			@Override
			public void onFall() {
				recoilComponent.stop();
			}
			@Override
			public void onFallEnd() {
				setToRemove();
			}
		});

		aspectSystem.addAspect(new EnemyTag(uniqueID));
		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		WallBounce wallBounce = new WallBounce(uniqueID, spatialComponent);
		aspectSystem.addAspect(wallBounce);
		aspectSystem.addAspect(new SwordHit(uniqueID) {
			@Override
			public SwordHitResult hit(float swordX, float swordY, float swordW, float swordH, float ownerCX, float ownerCY, Direction ownerDirection, int dmg, boolean powerUped, SwordState swordState, int counter) {
				if(dead == false && recoilComponent.isActive() == false && fallComponent.isFalling() == false) {
					recoilComponent.hit(ownerCX, ownerCY, 30, 10);
//					dead = true;
					return new SwordHitResult(SwordHitResultType.HIT, spatialComponent.getCenterX(), spatialComponent.getCenterY());
				}
				return new SwordHitResult(SwordHitResultType.NONE, 0, 0);
			}
		});
		aspectSystem.addAspect(new HoleCollision(uniqueID) {
			@Override
			public HoleCollisionResult collision(float holeX, float holeY, float holeW, float holeH, boolean restoreLastPosition) {
				if(fallComponent.isFalling() == false) {
					if(recoilComponent.isActive() == true) {
						fallComponent.fall(holeX + holeW/2.0f, holeY + holeH/2.0f);
						return HoleCollisionResult.OTHER;
					}
					else {
						wallBounce.bounce(holeX, holeY, holeW, holeH, -1);
					}
				}
				return HoleCollisionResult.NONE;
			}
		});
		direction = new DirectionByDelta();
	}

	@Override
	public void update() {
		if(recoilComponent.isActive() == false && fallComponent.isFalling() == false) {
			if(dead == false) {
				Vector delta = coordinate.calculateDelta(
						spatialComponent.getCenterX(), spatialComponent.getCenterY(),
						linkData.getCenterX(), linkData.getCenterY());
				spatialComponent.setDelta(delta.x * 0.5f, delta.y * 0.5f);
			}
			else {
				setToRemove();
				prize.createPickup((int)spatialComponent.getCenterX(), (int)spatialComponent.getCenterY());
			}
		}
		recoilComponent.update();
		fallComponent.update();
		direction.update(spatialComponent.getDeltaX(), spatialComponent.getDeltaY());
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
