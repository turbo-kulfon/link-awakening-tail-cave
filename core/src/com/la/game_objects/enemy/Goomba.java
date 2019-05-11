package com.la.game_objects.enemy;

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
import com.la.aspects.BombHit;
import com.la.aspects.EnemyTag;
import com.la.aspects.MagicPowderCollision;
import com.la.aspects.RoomTransition;
import com.la.aspects.SwordHit;
import com.la.aspects.enemy_hit.EnemyHit;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.aspects.wall_bounce.WallBounceStandard;
import com.la.factory.IRoomFactory;
import com.la.game_objects.effect.Burn;
import com.la.game_objects.effect.Burn.BurnDependency;
import com.la.game_objects.enemy.DefeatSparkController.DefeatSparkControllerDependency;
import com.la.game_objects.enemy.PowerUpSwordHitTrailController.PowerUpSwordHitTrailControllerDependency;
import com.la.game_objects.enemy.RecoilIndicatorController.RecoilIndicatorControllerDependency;
import com.la.game_objects.link.controller.platform.GravityComponent;
import com.la.game_objects.link.controller.platform.GravityComponent.GravityComponentDependency;
import com.la.game_objects.link.controller.platform.RecoilPlatformComponent;
import com.la.game_objects.link.controller.platform.RecoilPlatformComponent.RecoilPlatformDependency;

public class Goomba implements IGameObject {
	public interface GoombaCallback {
		void onDeath();
	}
	private enum GoombaState {
		WALK,
		STOMPED
	}

	private SoundSystem soundSystem;
	private TextureDrawComponent drawComponent;
	private Burn burn;
	private ISpatialComponent spatialComponent;
	private DirectionByDelta direction;

	private RecoilPlatformComponent recoilComponent;
	private GravityComponent gravityComponent;
	private PowerUpSwordHitTrailController trailController;
	private DefeatSparkController defeatSparkController;
	private RecoilIndicatorController recoilIndicatorController;
	private IOutsideViewCheck outsideViewCheck;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private GoombaState state = GoombaState.WALK;
	private int animationCounter, deadCounter = 25;
	private boolean moveLeft, onGround;

	private int uniqueID;
	private int invisibility, burning;
	private boolean remove, dead, wallCollision;
	private GoombaCallback callback;

	public Goomba(float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			IRoomFactory roomFactory,
			IEnemyDefeatedPrize prize,
			GoombaCallback callback) {
		this.soundSystem = soundSystem;
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.callback = callback;

		prize.setRupeeDropChance(50);
		prize.setHeartDropChance(0);

		uniqueID = uniqueIDManager.getUniqueID();
		drawComponent = gfxSystem.createTextureDrawComponent(1);

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 12, 15);
		spatialComponent.setCollisionResponse((collidedID)-> {
			if(dead == false && state == GoombaState.WALK) {
				EnemyHit enemyHit = aspectSystem.getAspect(collidedID, AspectType.ENEMY_HIT);
				if(enemyHit != null) {
					EnemyHit.Result result = enemyHit.hit(
							spatialComponent.getX(), spatialComponent.getY(),
							spatialComponent.getW(), spatialComponent.getH(),
							direction.getDirection(), 10, 2, 0);
					if(result.type == EnemyHit.ResultType.SHIELD) {
						recoilComponent.hit(result.cx, result.cy);
					}
					else if(result.type == EnemyHit.ResultType.STOMP) {
						prize.setRupeeDropChance(0);
						prize.setHeartDropChance(100);
						state = GoombaState.STOMPED;
						soundSystem.enemyStomped();
					}
				}
			}
		});

		recoilComponent = new RecoilPlatformComponent(new RecoilPlatformDependency() {
			@Override
			public void unsetOnGround() {
				onGround = false;
			}
			@Override
			public boolean isOnGround() {
				return onGround;
			}
			
			@Override
			public void setDeltaX(float dx) {
				spatialComponent.setDeltaX(dx);
			}
			@Override
			public void setDeltaY(float dy) {
				spatialComponent.setDeltaY(dy);
			}

			@Override
			public float getCenterX() {
				return spatialComponent.getCenterX();
			}
			@Override
			public float getCenterY() {
				return spatialComponent.getCenterY();
			}

			@Override
			public boolean wallCollision() {
				return wallCollision;
			}
		});
		gravityComponent = new GravityComponent(new GravityComponentDependency() {
			@Override
			public void setDeltaY(float dy) {
				spatialComponent.setDeltaY(dy);
			}
			@Override
			public boolean isOnGround() {
				return onGround;
			}
			@Override
			public boolean isLevitating() {
				return false;
			}
			@Override
			public void decreaseDeltaY(float amount) {
				spatialComponent.setDeltaY(spatialComponent.getDeltaY() - amount);
				if(spatialComponent.getDeltaY() >= 4) {
					spatialComponent.setDeltaY(4);
				}
			}
		});
		
		trailController = new PowerUpSwordHitTrailController(roomFactory, new PowerUpSwordHitTrailControllerDependency() {
			@Override
			public float getCenterX() {
				return spatialComponent.getCenterX();
			}
			@Override
			public float getCenterY() {
				return spatialComponent.getCenterY();
			}
			@Override
			public boolean isInRecoilMode() {
				return recoilComponent.isActive();
			}
		});
		defeatSparkController = new DefeatSparkController(prize, roomFactory, new DefeatSparkControllerDependency() {
			@Override
			public float getCenterX() {
				return spatialComponent.getCenterX();
			}
			@Override
			public float getCenterY() {
				return spatialComponent.getY() + spatialComponent.getH();
			}
		});

		aspectSystem.addAspect(new EnemyTag(uniqueID));
		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		aspectSystem.addAspect(new WallBounce(uniqueID, spatialComponent, new WallBounceStandard(spatialComponent) {
			@Override
			public void onLeftSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onLeftSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				moveLeft = !moveLeft;
				wallCollision = true;
			}
			@Override
			public void onRightSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onRightSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				moveLeft = !moveLeft;
				wallCollision = true;
			}
			@Override
			public void onUpSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onUpSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				onGround = true;
			}
		}));
		aspectSystem.addAspect(new SwordHit(uniqueID) {
			@Override
			public SwordHitResult hit(float swordX, float swordY, float swordW, float swordH, float ownerCX, float ownerCY, Direction ownerDirection, int dmg, boolean powerUped, SwordState swordState, int counter) {
				if(dead == false && invisibility <= 0 && recoilComponent.isActive() == false) {
					if(powerUped == false) {
						recoilComponent.hitNoBounce(ownerCX, ownerCY, 30, 15);
						soundSystem.enemyHit();
					}
					else {
						recoilComponent.hitNoBounce(ownerCX, ownerCY, 30 * 100, 15 * 100);
						soundSystem.enemyDieWithPoweredUpSword();
					}
					dead = true;
					trailController.setActive(powerUped);
					defeatSparkController.setHit(powerUped);
					invisibility = 10;
					return new SwordHitResult(SwordHitResultType.HIT, spatialComponent.getCenterX(), spatialComponent.getCenterY());
				}
				return new SwordHitResult(SwordHitResultType.NONE, 0, 0);
			}
		});
		aspectSystem.addAspect(new BombHit(uniqueID) {
			@Override
			public void hit(float bombCenterX, float bombCenterY, boolean playerIsOwner) {
				if(playerIsOwner == true && dead == false && invisibility <= 0) {
					recoilComponent.hitNoBounce(bombCenterX, bombCenterY, 30, 15);
					trailController.setActive(false);
					defeatSparkController.setHit(false);
					dead = true;
					invisibility = 10;
				}
			}
		});
		aspectSystem.addAspect(new MagicPowderCollision(uniqueID) {
			@Override
			public void collision(float powderX, float powderY, float powderW, float powderH, float ownerCX, float ownerCY) {
				if(burning <= 0 && dead == false) {
					burn.setActive(true);
					burning = 30;
					dead = true;
					soundSystem.fireIgnite();
				}
			}
		});

		direction = new DirectionByDelta();

		outsideViewCheck = new OutsideViewCheck(new OutsideViewCheckPosition(spatialComponent), 
				new OutsideViewCheckCallback() {
					@Override
					public void outsideLeft() {
						spatialComponent.setX(0);
						spatialComponent.setDeltaX(0);
						wallCollision = true;
					}
					@Override
					public void outsideRight() {
						spatialComponent.setX(160 - spatialComponent.getW());
						spatialComponent.setDeltaX(0);
						wallCollision = true;
					}
					@Override
					public void outsideUp() {
						
					}
					@Override
					public void outsideDown() {
						
					}
				}
			);
		burn = new Burn(gfxSystem, new BurnDependency() {
			@Override
			public float getCenterX() {
				return spatialComponent.getCenterX();
			}
			@Override
			public float getBottomY() {
				return spatialComponent.getY() + spatialComponent.getH() + 2;
			}
			@Override
			public float getHeight() {
				return 0;
			}
		});
		recoilIndicatorController = new RecoilIndicatorController(drawComponent, new RecoilIndicatorControllerDependency() {
			@Override
			public boolean isHit() {
				return invisibility > 0;
			}
		});
	}

	@Override
	public void update() {
		if(burning <= 0) {
			animationCounter += 1;
			if(animationCounter > 16) {
				animationCounter = 0;
			}
			if(invisibility > 0) {
				invisibility -= 1;
			}
			
			if(state == GoombaState.WALK) {
				if(moveLeft == true) {
					spatialComponent.setDeltaX(-0.5f);
				}
				else {
					spatialComponent.setDeltaX( 0.5f);
				}
			}
			else if(state == GoombaState.STOMPED) {
				spatialComponent.setDeltaX(0);
				if(deadCounter > 0) {
					deadCounter -= 1;
				}
				else {
					dead = true;
				}
			}
		}
		else {
			burning -= 1;
			spatialComponent.setDeltaX(0);
		}

		recoilComponent.update();
		gravityComponent.update();
		trailController.update();
		recoilIndicatorController.update();

		direction.update(spatialComponent.getDeltaX(), spatialComponent.getDeltaY());

		onGround = false;
		if(recoilComponent.isActive() == false && remove == false && burning <= 0) {
			if(dead == true) {
				callback.onDeath();
				setToRemove();
				if(defeatSparkController.onDead() == false) {
					soundSystem.enemyDie();
				}
				else {
					soundSystem.enemyDieWithPoweredUpSword();
				}
			}
		}
		outsideViewCheck.update();
		wallCollision = false;

		burn.update();
	}
	@Override
	public void draw() {
		if(state == GoombaState.WALK) {
			if(animationCounter <= 8) {
				drawComponent.setTexture(448, 34, 14, 16);
				drawComponent.setSize(14, 16);
				drawComponent.setSpriteOffset(-1, 1);
			}
			else {
				drawComponent.setTexture(448, 50, 14, 16);
				drawComponent.setSize(14, 16);
				drawComponent.setSpriteOffset(-1, 1);
			}
		}
		else if(state == GoombaState.STOMPED) {
			drawComponent.setTexture(462, 58, 16, 8);
			drawComponent.setSize(16, 8);
			drawComponent.setSpriteOffset(-2, 8);
		}

		recoilIndicatorController.draw();
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		burn.draw();
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
		burn.remove();
	}
}
