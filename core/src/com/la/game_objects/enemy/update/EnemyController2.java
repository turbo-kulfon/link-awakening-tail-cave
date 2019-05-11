package com.la.game_objects.enemy.update;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.component.sword.SwordState;
import com.engine.direction.Direction;
import com.engine.direction.DirectionByDelta;
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
import com.la.aspects.HoleCollision;
import com.la.aspects.MagicPowderCollision;
import com.la.aspects.RoomTransition;
import com.la.aspects.SwordHit;
import com.la.aspects.enemy_hit.EnemyHit;
import com.la.aspects.hidden.Hidden;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.aspects.wall_bounce.WallBounceStandard;
import com.la.game_objects.enemy.update.IObjectController.State;
import com.la.game_objects.link.controller.top_down.HoleFallComponent;
import com.la.game_objects.link.controller.top_down.HoleFallComponent.HoleFallDependency;
import com.la.game_objects.link.controller.top_down.JumpTopDownComponent;
import com.la.game_objects.link.controller.top_down.JumpTopDownComponent.JumpTopDownDependency;
import com.la.game_objects.link.controller.top_down.RecoilTopDownComponent;
import com.la.game_objects.link.controller.top_down.RecoilTopDownComponent.RecoilTopDownDependency;

public abstract class EnemyController2 implements IEnemyController {
	private SoundSystem soundSystem;
	private ISpatialComponent spatialComponent;
	private JumpTopDownComponent jumpComponent;
	private RecoilTopDownComponent recoilComponent;
	private HoleFallComponent fallComponent;
	private DirectionByDelta direction;

	private IObjectController objectController;
	private IObjectControllerCallback callback;
	private IOutsideViewCheck outsideViewCheck;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private int uniqueID;
	private boolean remove, dead, flying, wallCollision, collideWithLink = true, holeCollision, lastHoleCollision;
	private int invisibilityFrame, burnCounter, hp;
	private float height;

	public EnemyController2(
			int enemyTypeID,
			float x, float y, float w, float h,
			int healthPoints, boolean tagEnemy, int shieldProtection, int damage,
			boolean holeBounceCollision,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			IObjectControllerCallback callback) {
		this.soundSystem = soundSystem;
		this.hp = healthPoints;
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;
		this.callback = callback;

		uniqueID = uniqueIDManager.getUniqueID();

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, w, h);
		spatialComponent.setCollisionResponse((collidedID)-> {
			if(collideWithLink == true && dead == false && remove == false && height <= 0 && collidedID != uniqueID) {
				EnemyHit enemyHit = aspectSystem.getAspect(collidedID, AspectType.ENEMY_HIT);
				if(enemyHit != null) {
					EnemyHit.Result result = enemyHit.hit(
						spatialComponent.getX(), spatialComponent.getY(),
						spatialComponent.getW(), spatialComponent.getH(),
						direction.getDirection(), enemyTypeID, damage, shieldProtection);
					if(result.type == EnemyHit.ResultType.SHIELD) {
						callback.onLinkShieldCollision(objectController, result.cx, result.cy);
					}
					else if(result.type == EnemyHit.ResultType.HIT) {
						callback.onLinkCollision(collidedID);
					}
				}
			}
		});

		recoilComponent = new RecoilTopDownComponent(new RecoilTopDownDependency() {
			@Override
			public float getCenterX() {
				return spatialComponent.getCenterX();
			}
			@Override
			public float getCenterY() {
				return spatialComponent.getCenterY();
			}

			@Override
			public void setDelta(float dx, float dy) {
				spatialComponent.setDelta(dx, dy);
			}

			@Override
			public boolean wallCollision() {
				return wallCollision;
			}
		});
		jumpComponent = new JumpTopDownComponent(new JumpTopDownDependency() {
			@Override
			public void update(float heightArg, boolean isJumping) {
				height = heightArg;
			}
			@Override
			public void onJump() {
			}
			@Override
			public void onLand() {
			}
		});
		fallComponent = new HoleFallComponent(new HoleFallDependency() {
			@Override
			public float getCenterX() {
				return spatialComponent.getCenterX();
			}
			@Override
			public float getCenterY() {
				return spatialComponent.getCenterY();
			}

			@Override
			public void setDelta(float dx, float dy) {
				spatialComponent.setDelta(dx, dy);
			}
			@Override
			public void onFall() {
				jumpComponent.stop();
				recoilComponent.stop();
				spatialComponent.setActive(false);
				soundSystem.enemyFall();
			}
			@Override
			public void onFallEnd() {
				remove = true;
			}
		});

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		WallBounce wallBounce = new WallBounce(uniqueID, spatialComponent, new WallBounceStandard(spatialComponent) {
			@Override
			public void onLeftSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onLeftSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				callback.onWallCollisionFromLeft(objectController);
				wallCollision = true;
			}
			@Override
			public void onRightSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onRightSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				callback.onWallCollisionFromRight(objectController);
				wallCollision = true;
			}
			@Override
			public void onUpSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onUpSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				callback.onWallCollisionFromUp(objectController);
				wallCollision = true;
			}
			@Override
			public void onDownSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onDownSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				callback.onWallCollisionFromDown(objectController);
				wallCollision = true;
			}
		}) {
			@Override
			public int bounce(float wallX, float wallY, float wallW, float wallH, int wallDirection) {
				if(flying == true) {
					return -1;
				}
				return super.bounce(wallX, wallY, wallW, wallH, wallDirection);
			}
		};
		aspectSystem.addAspect(wallBounce);
		if(tagEnemy == true) {
			aspectSystem.addAspect(new EnemyTag(uniqueID));
		}
		Hidden hidden = new Hidden(uniqueID);
		aspectSystem.addAspect(hidden);
		aspectSystem.addAspect(new SwordHit(uniqueID) {
			@Override
			public SwordHitResult hit(float swordX, float swordY, float swordW, float swordH, float ownerCX, float ownerCY, Direction ownerDirection, int dmg, boolean powerUped, SwordState swordState, int counter) {
				if(remove == false && burnCounter <= 0 && invisibilityFrame <= 0 && recoilComponent.isActive() == false && fallComponent.isActive() == false) {
					SwordHitResult result = callback.onSwordCollision(objectController, swordX, swordY, swordW, swordH, ownerCX, ownerCY, dmg, powerUped, swordState, counter);
					if(result.type == SwordHitResultType.HIT) {
						invisibilityFrame = 10;
						if(powerUped == false) {
							soundSystem.enemyHit();
						}
						else {
							soundSystem.enemyHitWithPoweredUpSword();
						}
					}
					return result;
				}
				return new SwordHitResult(SwordHitResultType.NONE, 0, 0);
			}
		});
		aspectSystem.addAspect(new HoleCollision(uniqueID) {
			@Override
			public HoleCollisionResult collision(float holeX, float holeY, float holeW, float holeH, boolean restoreLastPosition) {
				holeCollision = true;
				if(flying == false && fallComponent.isActive() == false && height <= 0) {
//				if(flying == false && fallComponent.isActive() == false && height <= 3) {
					if(holeBounceCollision == true) {
						if(recoilComponent.isActive() == true || (getController().getCurrentState() == State.JUMPING && height <= 0) || lastHoleCollision == true) {
//						if(recoilComponent.isActive() == true && height <= 1) {
							fallComponent.fall(holeX + holeW/2.0f, holeY + holeH/2.0f);
							return HoleCollisionResult.OTHER;
						}
						else {
							wallBounce.bounce(holeX, holeY, holeW, holeH, -1);
							holeCollision = false;
						}
					}
					else {
						fallComponent.fall(holeX + holeW/2.0f, holeY + holeH/2.0f);
						return HoleCollisionResult.OTHER;
					}
				}
				return HoleCollisionResult.NONE;
			}
		});
		aspectSystem.addAspect(new BombHit(uniqueID) {
			@Override
			public void hit(float bombCenterX, float bombCenterY, boolean playerIsOwner) {
				if(remove == false && burnCounter <= 0 && invisibilityFrame <= 0 && recoilComponent.isActive() == false && fallComponent.isActive() == false) {
					if(callback.onBombCollision(objectController, bombCenterX, bombCenterY, playerIsOwner) == true) {
						invisibilityFrame = 10;
					}
				}
			}
		});
		aspectSystem.addAspect(new MagicPowderCollision(uniqueID) {
			@Override
			public void collision(float powderX, float powderY, float powderW, float powderH, float ownerCX, float ownerCY) {
				if(remove == false && burnCounter <= 0 && recoilComponent.isActive() == false && fallComponent.isActive() == false) {
					if(callback.onMagicPowderCollision(objectController, ownerCX, ownerCY) == true) {
						burnCounter = 60;
						dead = true;
						spatialComponent.stop();
						soundSystem.fireIgnite();
					}
				}
			}
		});

		direction = new DirectionByDelta();

		objectController = new ObjectControllerStandard2(soundSystem, spatialComponent, jumpComponent, recoilComponent) {
			@Override
			public void doDmg(int damage) {
				hp -= damage;
				if(hp <= 0) {
					hp = 0;
					dead = true;
				}
			}
			@Override
			public State getCurrentState() {
				if(height > 0) {
					return State.JUMPING;
				}
				if(fallComponent.isActive() == true) {
					return State.FALLING;
				}
				if(recoilComponent.isActive() == true) {
					return State.RECOIL;
				}
				if(burnCounter > 0) {
					return State.BURN;
				}
				return State.STANDARD;
			}
			@Override
			public void setFlying(boolean flyingArg) {
				flying = flyingArg;
			}
			@Override
			public void setCollideWithLink(boolean collide) {
				collideWithLink = collide;
			}
			@Override
			public void setHidden(boolean hiddenArg) {
				if(hiddenArg == false) {
					hidden.unhide();
				}
				else {
					hidden.hide();
				}
			}
			@Override
			public float getHeight() {
				return height;
			}
			@Override
			public int getInvisbilityFrame() {
				return invisibilityFrame;
			}
		};

		outsideViewCheck = new OutsideViewCheck(new OutsideViewCheckPosition(spatialComponent), 
			new OutsideViewCheckCallback() {
				@Override
				public void outsideLeft() {
					spatialComponent.setX(0);
					spatialComponent.setDeltaX(0);
					callback.onWallCollisionFromRight(objectController);
					wallCollision = true;
				}
				@Override
				public void outsideRight() {
					spatialComponent.setX(160 - spatialComponent.getW());
					spatialComponent.setDeltaX(0);
					callback.onWallCollisionFromLeft(objectController);
					wallCollision = true;
				}
				@Override
				public void outsideUp() {
					spatialComponent.setY(0);
					spatialComponent.setDeltaY(0);
					callback.onWallCollisionFromDown(objectController);
					wallCollision = true;
				}
				@Override
				public void outsideDown() {
					spatialComponent.setY(128 - spatialComponent.getH());
					spatialComponent.setDeltaY(0);
					callback.onWallCollisionFromUp(objectController);
					wallCollision = true;
				}
			}
		);
	}

	@Override
	public abstract void update(IObjectController controller);
	@Override
	public void updateController() {
		if(recoilComponent.isActive() == false && fallComponent.isActive() == false) {
			if(dead == false) {
				update(objectController);
			}
			direction.update(spatialComponent.getDeltaX(), spatialComponent.getDeltaY());
		}

		jumpComponent.update();
		fallComponent.update();
		recoilComponent.update();
		if(burnCounter > 0) {
			burnCounter -= 1;
			callback.onBurn(objectController);
		}

		if(recoilComponent.isActive() == false && fallComponent.isActive() == false && remove == false && burnCounter <= 0) {
			if(dead == true) {
				remove = true;
				if(callback.onDeath(objectController) == false) {
					soundSystem.enemyDie();
				}
				else {
					soundSystem.enemyDieWithPoweredUpSword();
				}
			}
		}

		wallCollision = false;
		outsideViewCheck.update();

		if(invisibilityFrame > 0) {
			invisibilityFrame -= 1;
		}

		lastHoleCollision = holeCollision;
		holeCollision = false;
	}

	@Override
	public IObjectController getController() {
		return objectController;
	}

	@Override
	public boolean shouldRemove() {
		return remove;
	}

	@Override
	public void remove() {
		spatialComponent.remove();
		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}
}
