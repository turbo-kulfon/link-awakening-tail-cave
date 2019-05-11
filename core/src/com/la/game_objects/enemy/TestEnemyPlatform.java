package com.la.game_objects.enemy;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.component.gravity.GravityComponent;
import com.engine.component.gravity.GravityComponent.GravityComponentPlatformAdditionalData;
import com.engine.component.gravity.IGravityComponent;
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
import com.engine.util.Coordinate;
import com.engine.util.ICoordinate;
import com.la.aspects.BombHit;
import com.la.aspects.EnemyTag;
import com.la.aspects.RoomTransition;
import com.la.aspects.SwordHit;
import com.la.aspects.enemy_hit.EnemyHit;
import com.la.aspects.wall_bounce.WallBounceStandard;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.game_objects.link.ILinkData;

public class TestEnemyPlatform implements IGameObject {
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;
	private IRecoil recoilComponent;
	private IGravityComponent gravityComponent;
	private ICoordinate coordinate;
	private DirectionByDelta direction;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;
	private IEnemyDefeatedPrize prize;
	private ILinkData linkData;

	private int uniqueID, invisibility;
	private boolean remove, dead, moveLeft = true, isOnGround;

	public TestEnemyPlatform(int x, int y,
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
		prize.setHeartDropChance(0);

		uniqueID = uniqueIDManager.getUniqueID();

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 14, 15);
		spatialComponent.setCollisionResponse((collidedID)-> {
			if(dead == false) {
				EnemyHit enemyHit = aspectSystem.getAspect(collidedID, AspectType.ENEMY_HIT);
				if(enemyHit != null) {
					EnemyHit.Result result = enemyHit.hit(
							spatialComponent.getX(), spatialComponent.getY(),
							spatialComponent.getW(), spatialComponent.getH(),
							direction.getDirection(), 10, 2, 0);
					if(result.type == EnemyHit.ResultType.SHIELD) {
						recoilComponent.hit(result.cx, result.cy, 30, 15);
					}
					else if(result.type == EnemyHit.ResultType.STOMP) {
						prize.setRupeeDropChance(0);
						prize.setHeartDropChance(100);
						dead = true;
					}
				}
			}
		});

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(480, 26, 16, 16);
		drawComponent.setSize(16, 16);
		drawComponent.setSpriteOffset(-1, -1);

		coordinate = new Coordinate();

		recoilComponent = new Recoil(new RecoilDependencyStandard(spatialComponent));
		gravityComponent = new GravityComponent(0.125f, new GravityComponentPlatformAdditionalData() {
			@Override
			public void setDelta(float value) {
				spatialComponent.setDeltaY(value);
			}
			@Override
			public void moveDelta(float delta) {
				spatialComponent.setDeltaY(spatialComponent.getDeltaY() + delta);
			}
			@Override
			public void calibrateDelta(float value) {
				if(spatialComponent.getDeltaY() > value) {
					spatialComponent.setDeltaY(value);
				}
			}
			@Override
			public void stopDelta() {
				spatialComponent.setDeltaY(0);
			}
		});

		aspectSystem.addAspect(new EnemyTag(uniqueID));
		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		aspectSystem.addAspect(new WallBounce(uniqueID, spatialComponent, new WallBounceStandard(spatialComponent) {
			@Override
			public void onLeftSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onLeftSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				moveLeft = !moveLeft;
			}
			@Override
			public void onRightSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onRightSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				moveLeft = !moveLeft;
			}
			@Override
			public void onUpSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onUpSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				isOnGround = true;
			}
		}));
		aspectSystem.addAspect(new SwordHit(uniqueID) {
			@Override
			public SwordHitResult hit(float swordX, float swordY, float swordW, float swordH, float ownerCX, float ownerCY, Direction ownerDirection, int dmg, boolean powerUped, SwordState swordState, int counter) {
				if(dead == false && invisibility <= 0 && recoilComponent.isActive() == false) {
					recoilComponent.hit(ownerCX, ownerCY, 30, 15);
					dead = true;
					return new SwordHitResult(SwordHitResultType.HIT, spatialComponent.getCenterX(), spatialComponent.getCenterY());
				}
				return new SwordHitResult(SwordHitResultType.NONE, 0, 0);
			}
		});
		aspectSystem.addAspect(new BombHit(uniqueID) {
			@Override
			public void hit(float bombCenterX, float bombCenterY, boolean playerIsOwner) {
				if(playerIsOwner == true) {
					recoilComponent.hit(bombCenterX, bombCenterY, 30, 15);
					dead = true;
				}
			}
		});
		direction = new DirectionByDelta();
	}

	@Override
	public void update() {
		if(recoilComponent.isActive() == false) {
			if(dead == false) {
				if(moveLeft == false) {
					spatialComponent.setDeltaX(0.5f);
				}
				else {
					spatialComponent.setDeltaX(-0.5f);
				}
			}
		}
		recoilComponent.update();
		gravityComponent.update(isOnGround, false);
		direction.update(spatialComponent.getDeltaX(), spatialComponent.getDeltaY());

		isOnGround = false;

		if(recoilComponent.isActive() == false && remove == false) {
			if(dead == true) {
				setToRemove();
				prize.createPickup((int)spatialComponent.getCenterX(), (int)spatialComponent.getY() + (int)spatialComponent.getH());
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
