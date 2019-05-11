package com.la.game_objects.link.controller.platform;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.component.shield.ShieldComponent;
import com.engine.component.shield.ShieldComponent.ShieldComponentDependency;
import com.engine.component.sword.SwordComponent;
import com.engine.component.sword.SwordComponent.SwordComponentDependency;
import com.engine.component.sword.SwordState;
import com.engine.direction.Direction;
import com.engine.direction.LinkDirectionPlatform;
import com.engine.sound.SoundSystem;
import com.engine.spatial.IOutsideViewCheck;
import com.engine.spatial.IOutsideViewCheck.OutsideViewCheckCallback;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.engine.spatial.core.ResponseCallback;
import com.la.aspects.LinkCollision;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.aspects.wall_bounce.WallBounceCollisionResolve;
import com.la.aspects.wall_bounce.WallBounceStandard;
import com.la.equipment.IEquipmentSystem;
import com.la.factory.IRoomFactory;
import com.la.game_objects.link.controller.ActionButtonController;
import com.la.game_objects.link.controller.ActionButtonController.ActionButtonDependency;
import com.la.game_objects.link.controller.ILinkState;
import com.la.game_objects.link.controller.StateType;
import com.la.game_objects.link.controller.common.BombComponent;
import com.la.game_objects.link.controller.common.BombComponentDependencyStandard;
import com.la.game_objects.link.controller.common.CarryComponent;
import com.la.game_objects.link.controller.common.CarryComponent.CarryState;
import com.la.game_objects.link.controller.common.CarryComponentDependencyStandard;
import com.la.game_objects.link.controller.common.EnemyHitBase;
import com.la.game_objects.link.controller.common.LadderCollisionBase;
import com.la.game_objects.link.controller.common.LadderCollisionBase.LadderCollisionBaseDependency;
import com.la.game_objects.link.controller.common.LinkOutsideViewCheck;
import com.la.game_objects.link.controller.common.MagicPowderComponent;
import com.la.game_objects.link.controller.common.MagicPowderComponent.MagicPowderComponentDependency;
import com.la.game_objects.link.controller.platform.EnemyHitPlatformCallback.EnemyHitPlatformDependency;
import com.la.game_objects.link.controller.platform.GravityComponent.GravityComponentDependency;
import com.la.game_objects.link.controller.platform.JumpPlatformComponent.JumpPlatformDependency;
import com.la.game_objects.link.controller.platform.RecoilPlatformComponent.RecoilPlatformDependency;
import com.la.game_objects.link.controller.top_down.LinkOutsideViewCheckTopDownDependency;
import com.la.game_objects.link.controller.top_down.ShieldTopDownDependency;
import com.la.game_objects.link.controller.top_down.SwordTopDownDependency;
import com.la.game_objects.link.draw.LinkDrawComponent;
import com.la.game_objects.link.draw.LinkDrawComponent.LinkDrawState;

public class LinkStatePlatform implements ILinkState {
	private SoundSystem soundSystem;
	private LinkDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;
	private SwordComponent swordComponent;
	private ShieldComponent shieldComponent;
	private CarryComponent carryComponent;
	private WallBounce wallBounce;
	private EnemyHitBase enemyHit;
	private IOutsideViewCheck outsideViewCheck;
	private IEquipmentSystem equipmentSystem;

	private ActionButtonController actionButtonController;
	private RecoilPlatformComponent recoilComponent;
	private JumpPlatformComponent jumpComponent;
	private GravityComponent gravityComponent;
	private BombComponent bombComponent;
	private LinkDirectionPlatform direction;
	private WallBounceCollisionResolve wallBounceCollisionResolve;
	private ResponseCallback collisionResponseCallback;
	private EnemyHitPlatformCallback enemyHitCallback;
	private SwordComponentDependency swordComponentDependency;
	private ShieldComponentDependency shieldComponentDependency;
	private CarryComponentDependencyStandard carryComponentDependency;
	private OutsideViewCheckCallback outsideViewCheckCallback;

	private int lr, ud, currentLR, currentUD;
	private boolean onGround, lastOnGround, ladderCollisionDetected, onLadder, lastOnLadder, bugLadder, bugLadder2;
	private int invisibility;

	private int swordActive, animationCounter;
	private boolean wallPush;

	public LinkStatePlatform(
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			LinkDrawComponent drawComponent,
			ISpatialComponent spatialComponent,
			SwordComponent swordComponent,
			ShieldComponent shieldComponent,
			CarryComponent carryComponent,
			WallBounce wallBounce,
			EnemyHitBase enemyHit,
			IOutsideViewCheck outsideViewCheck,
			LadderCollisionBase ladderCollision,
			IEquipmentSystem equipmentSystem,
			IAspectSystem aspectSystem,
			IRoomFactory roomFactory) {
		this.soundSystem = soundSystem;
		this.drawComponent = drawComponent;
		this.spatialComponent = spatialComponent;
		this.swordComponent = swordComponent;
		this.shieldComponent = shieldComponent;
		this.carryComponent = carryComponent;
		this.wallBounce = wallBounce;
		this.enemyHit = enemyHit;
		this.outsideViewCheck = outsideViewCheck;
		this.equipmentSystem = equipmentSystem;

		MagicPowderComponent magicPowderComponent = new MagicPowderComponent(equipmentSystem, roomFactory, new MagicPowderComponentDependency() {
			@Override
			public void onSprinkle() {
				soundSystem.magicPowderSprinkle();
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
			public float getY() {
				return spatialComponent.getY();
			}
			@Override
			public float getH() {
				return spatialComponent.getH();
			}

			@Override
			public Direction getDirection() {
				return direction.getDirection();
			}
		});

		actionButtonController = new ActionButtonController(new ActionButtonDependency() {
			@Override
			public void swordSwing() {
				swordComponent.attack();
			}
			@Override
			public void shieldUp() {
				shieldComponent.shieldUp();
			}
			@Override
			public void jump() {
				jumpComponent.jump(2.2f);
			}
			@Override
			public void plantBomb() {
				bombComponent.buttonPressed();
			}
			@Override
			public void sprinkMagicPowder() {
				magicPowderComponent.sprinkle();
			}
			@Override
			public int getBButtonItemID() {
				return equipmentSystem.getBItemID();
			}
			@Override
			public int getAButtonItemID() {
				return equipmentSystem.getAItemID();
			}
			@Override
			public boolean BButtonCarryItemTossed() {
				return carryComponent.BButtonPressed();
			}
			@Override
			public boolean AButtonCarryItemTossed() {
				return carryComponent.AButtonPressed();
			}

			@Override
			public boolean interactionCheck() {
				return false;
			}
		});

		jumpComponent = new JumpPlatformComponent(new JumpPlatformDependency() {
			@Override
			public void unsetOnGround() {
				onGround = false;
				animationCounter = 0;
			}
			@Override
			public void setDeltaY(float dy) {
				spatialComponent.setDeltaY(dy);
			}
			@Override
			public boolean isOnGround() {
				return onGround;
			}
			@Override
			public void onJump() {
				soundSystem.linkJump();
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
				return onLadder;
			}
			@Override
			public void decreaseDeltaY(float amount) {
				spatialComponent.setDeltaY(spatialComponent.getDeltaY() - amount);
				if(spatialComponent.getDeltaY() >= 4) {
					spatialComponent.setDeltaY(4);
				}
			}
		});

		collisionResponseCallback = new ResponseCallback() {
			@Override
			public void response(int collidedID) {
				LinkCollision linkCollision = aspectSystem.getAspect(collidedID, AspectType.LINK_COLLISION);
				if(linkCollision != null) {
					linkCollision.collision(
						spatialComponent.getX(), spatialComponent.getY(),
						spatialComponent.getW(), spatialComponent.getH(), 0, false);
				}
			}
		};
		wallBounceCollisionResolve = new WallBounceStandard(spatialComponent) {
			@Override
			public void onUpSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onUpSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				onGround = true;
			}
			@Override
			public void onLeftSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onLeftSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				if(carryComponent.isActive() == false) {
					wallPush = true;
				}
				if(direction.getDirection() == Direction.RIGHT && currentUD == 0) {
					swordComponent.thrust();
				}
			}
			@Override
			public void onRightSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onRightSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				if(carryComponent.isActive() == false) {
					wallPush = true;
				}
				if(direction.getDirection() == Direction.LEFT && currentUD == 0) {
					swordComponent.thrust();
				}
			}
		};
		bombComponent = new BombComponent(new BombComponentDependencyStandard(spatialComponent, carryComponent, spatialSystem, aspectSystem) {
			@Override
			public void putBomb() {
				if(equipmentSystem.removeBomb() == true) {
					if(direction.getDirection() == Direction.LEFT) {
						roomFactory.createBombPlatform(
							(int)spatialComponent.getCenterX() - 8,
							(int)spatialComponent.getCenterY() + 4);
					}
					else if(direction.getDirection() == Direction.RIGHT) {
						roomFactory.createBombPlatform(
							(int)spatialComponent.getCenterX() + 8,
							(int)spatialComponent.getCenterY() + 4);
					}
					else if(direction.getDirection() == Direction.UP) {
						roomFactory.createBombPlatform(
							(int)spatialComponent.getCenterX(),
							(int)spatialComponent.getCenterY() - 2);
					}
					else if(direction.getDirection() == Direction.DOWN) {
						roomFactory.createBombPlatform(
							(int)spatialComponent.getCenterX(),
							(int)spatialComponent.getCenterY() + 8);
					}
				}
			}
		});

		ladderCollision.setDependency(new LadderCollisionBaseDependency() {
			@Override
			public void collision(float ladderX, float ladderY, float ladderW, float ladderH, int ladderType) {
				if(spatialComponent.getCenterX() + spatialComponent.getDeltaX() >= ladderX	&&
					spatialComponent.getCenterX() + spatialComponent.getDeltaX() <= ladderX + ladderW &&
					recoilComponent.isActive() == false) {
					if(ladderType == 1) {
						if(spatialComponent.getY() + spatialComponent.getH() <= ladderY) {
							if(onLadder == false) {
								if(wallBounce != null) {
									wallBounce.bounce(ladderX, ladderY, ladderW, ladderH, -1);
								}
							}
							bugLadder = true;
							ladderCollisionDetected = true;
							if(currentUD == 1) {
								onLadder = true;
							}
							else if(currentUD == -1) {
//								ladderCollisionDetected = false;
//								onLadder = false;
							}
						}
						else {
							ladderCollisionDetected = true;
						}
					}
					else if(ladderType == -1) {
						bugLadder2 = true;
						if(currentUD == -1) {
							ladderCollisionDetected = true;
						}
						else if(currentUD == 1) {
							if(lastOnGround == true) {
								ladderCollisionDetected = false;
								onLadder = false;
							}
						}
					}
					else {
						ladderCollisionDetected = true;
						bugLadder2 = true;
					}
				}
			}
		});
		enemyHitCallback = new EnemyHitPlatformCallback(new EnemyHitPlatformDependency() {
			@Override
			public void stompedEnemyBounce() {
				jumpComponent.forceJump(1);
				spatialComponent.setDeltaY(0);
			}

			@Override
			public void shieldRecoil(float centerX, float centerY) {
				recoilComponent.hitNoBounce(centerX, centerY, 16, 16);
			}
			@Override
			public boolean shieldCollisionCheck(float centerX, float centerY, Direction direction) {
				return shieldComponent.collisionCheck(centerX, centerY, direction);
			}

			@Override
			public boolean isRecoilActive() {
				return recoilComponent.isActive();
			}
			@Override
			public boolean isOnLadder() {
				return onLadder;
			}
			@Override
			public boolean isInvisible() {
				return invisibility > 0;
			}
			
			@Override
			public void hitRecoil(float centerX, float centerY) {
				recoilComponent.hit(centerX, centerY);
				invisibility = 60;
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
			public float getDeltaY() {
				return spatialComponent.getDeltaY();
			}
			@Override
			public float getBottomY() {
				return spatialComponent.getY() + spatialComponent.getH();
			}

			@Override
			public void doDamage(int damage) {
				equipmentSystem.removeHeart(damage);
				soundSystem.linkHurt();
			}
		});

		recoilComponent = new RecoilPlatformComponent(new RecoilPlatformDependency() {
			@Override
			public void setDeltaX(float dx) {
				spatialComponent.setDeltaX(dx);
			}
			@Override
			public void setDeltaY(float dy) {
				spatialComponent.setDeltaY(dy);
			}
			@Override
			public boolean isOnGround() {
				return onGround;
			}
			@Override
			public void unsetOnGround() {
				onGround = false;
				onLadder = false;
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
				return wallPush;
			}
		});

//		direction = new LinkDirectionPlatform();
		direction = new LinkDirectionPlatform();
		swordComponentDependency = new SwordTopDownDependency(spatialComponent, direction) {
			@Override
			public float getY() {
				return super.getY() + 8;
			}
			@Override
			public boolean recoil(float enemyCX, float enemyCY) {
				if(recoilComponent.isActive() == false) {
					recoilComponent.hit(enemyCX, enemyCY);
					return true;
				}
				return false;
			}
			@Override
			public void onUpdate(SwordState swordState, int counter, Direction spinAttakcDirection) {
//				swordActive = -1;
//				if(swordState == SwordState.CUT) {
//					drawComponent.setDrawState(LinkDrawState.SWORD_ATTACK);
//					drawComponent.setFrameCounter(counter);
//					drawComponent.setDirection(direction.getDirection());
//					swordActive = 0;
//				}
//				else if(swordState == SwordState.HOLD || swordState == SwordState.POWER_UP) {
//					swordActive = 1;
//				}
//				else if(swordState == SwordState.SPIN) {
//					drawComponent.setDrawState(LinkDrawState.SWORD_ATTACK);
//					drawComponent.setFrameCounter(5);
//					drawComponent.setDirection(spinAttakcDirection);
//					swordActive = 0;
//				}
				swordActive = -1;
				if(swordState == SwordState.CUT) {
					drawComponent.setDrawState(LinkDrawState.SWORD_ATTACK);
					drawComponent.setFrameCounter(counter);
					drawComponent.setDirection(getOwnerDirection());
					swordActive = 0;
				}
				else if(swordState == SwordState.HOLD || swordState == SwordState.POWER_UP) {
					swordActive = 1;
				}
				else if(swordState == SwordState.THRUST_KEY_BLOCK || swordState == SwordState.THRUST_NO_KEY_BLOCK) {
					if(counter >= 7) {
						drawComponent.setDrawState(LinkDrawState.SWORD_ATTACK);
						drawComponent.setFrameCounter(3);
					}
					else {
						drawComponent.setDrawState(LinkDrawState.WALK);
						drawComponent.setFrameCounter(8);
					}
					drawComponent.setDirection(getOwnerDirection());
					swordActive = 0;
				}
				else if(swordState == SwordState.SPIN) {
					drawComponent.setDrawState(LinkDrawState.SWORD_ATTACK);
					drawComponent.setFrameCounter(5);
					drawComponent.setDirection(spinAttakcDirection);
					swordActive = 0;
				}
			}
			@Override
			public boolean isPieceOfPowerActive() {
				return equipmentSystem.isPieceOfPowerActive();
			}
		};
		shieldComponentDependency = new ShieldTopDownDependency(spatialComponent, direction);

		carryComponentDependency = new CarryComponentDependencyStandard(spatialComponent, equipmentSystem, aspectSystem);
		carryComponentDependency.setDirectionComponent(direction);

		outsideViewCheckCallback = 
			new LinkOutsideViewCheck(
				new LinkOutsideViewCheckTopDownDependency(
						spatialComponent) {
					@Override
					public boolean isRecoil() {
						return recoilComponent.isActive();
					}
				});
	}

	@Override
	public void initialize() {
		drawComponent.setHeight(0);
		drawComponent.setVisible(true);
		ladderCollisionDetected = true;
		onLadder = true;
		spatialComponent.setActive(true);
		spatialComponent.setSize(10, 15);
		spatialComponent.setCollisionResponse(collisionResponseCallback);
		wallBounce.setCollisionResolve(wallBounceCollisionResolve);
		enemyHit.setCallback(enemyHitCallback);
		swordComponent.setDependency(swordComponentDependency);
		shieldComponent.setDependency(soundSystem, shieldComponentDependency);
		carryComponent.setDependency(soundSystem, carryComponentDependency);
		outsideViewCheck.setCallback(outsideViewCheckCallback);
		invisibility = 0;
	}

	@Override
	public void leftButtonPressed() {
		lr = -1;
	}
	@Override
	public void rightButtonPressed() {
		lr = 1;
	}
	@Override
	public void upButtonPressed() {
		ud = -1;
	}
	@Override
	public void downButtonPressed() {
		ud = 1;
	}
	@Override
	public void BButtonPressed(boolean justPressed) {
		actionButtonController.BButtonPressed(justPressed);
	}
	@Override
	public void AButtonPressed(boolean justPressed) {
		actionButtonController.AButtonPressed(justPressed);
	}

	@Override
	public void update() {
//		float delta = 0.05f;
//		if(lr == -1) {
//			if(onGround == true) {
//				spatialComponent.setDeltaX(-1);
//			}
//			else {
//				spatialComponent.xAxisToValue(-1, delta);
//			}
//		}
//		else if(lr == 1) {
//			if(onGround == true) {
//				spatialComponent.setDeltaX(1);
//			}
//			else {
//				spatialComponent.xAxisToValue( 1, delta);
//			}
//		}
//		else {
//			if(onGround == true) {
//				spatialComponent.setDeltaX(0);
//			}
//			else {
//				spatialComponent.xAxisToZero(delta);
//			}
//		}
//
//		if(onGround == true && swordComponent.allowMove() == false) {
//			spatialComponent.setDeltaX(0);
//		}

		if(ladderCollisionDetected == true) {
			if(ud > 0) {
				onLadder = true;
			}
			else if(ud < 0) {
				if(bugLadder == false || bugLadder2 == true) {
					onLadder = true;
				}
			}
			if(onLadder == true) {
				if(ud < 0) {
					spatialComponent.setDeltaY(-1);
				}
				else if(ud > 0) {
					spatialComponent.setDeltaY(1);
				}
				else {
					spatialComponent.setDeltaY(0);
				}
			}
			ladderCollisionDetected = false;
		}
		else {
			onLadder = false;
		}
		bugLadder = false;
		bugLadder2 = false;

		if(onGround == true || (lastOnLadder == true && onLadder == true)) {
			if(lr < 0) {
				spatialComponent.setDeltaX(-speed());
			}
			else if(lr > 0) {
				spatialComponent.setDeltaX(speed());
			}
			else {
				spatialComponent.setDeltaX(0);
			}
		}
		else {
			if(lr < 0) {
				spatialComponent.xAxisToValue(-speed(), 0.1f);
			}
			else if(lr > 0) {
				spatialComponent.xAxisToValue(speed(), 0.1f);
			}
			else {
				spatialComponent.xAxisToZero(0.05f);
			}
		}
		if(lastOnGround == true && onGround == false) {
			if(currentLR < 0) {
				spatialComponent.setDeltaX(-speed());
			}
			else if(currentLR > 0) {
				spatialComponent.setDeltaX(speed());
			}
		}

		if(swordComponent.allowMove() == false) {
			if(onGround == true) {
				spatialComponent.setDeltaX(0);
			}
			if(onLadder == true) {
				spatialComponent.stop();
			}
		}

		swordComponent.update();
		shieldComponent.update();
		carryComponent.update();
		jumpComponent.update();
		recoilComponent.update();
		gravityComponent.update();

		if(onGround == false && lastOnGround == true) {
			animationCounter = 0;
		}
		if(lastOnGround == false && onGround == true && onLadder == false) {
			soundSystem.linkLand();
		}
		if(swordComponent.allowChangeDirection() == true) {
			direction.update(lr, ud);
		}

		if(swordActive == -1) {
			if(onGround == true || onLadder == true) {
				if(lr == 0 && ud == 0) {
					animationCounter = 0;
					if(carryComponent.isActive() == false) {
						if(equipmentSystem.isItemIDActive(1) == false) {
							drawComponent.setDrawState(LinkDrawState.IDLE);
						}
						else {
							if(shieldComponent.isActive() == false) {
								drawComponent.setDrawState(LinkDrawState.SHIELD_DOWN_IDLE);
							}
							else {
								drawComponent.setDrawState(LinkDrawState.SHIELD_UP_IDLE);
							}
						}
					}
					else {
						drawComponent.setDrawState(LinkDrawState.ITEM_HOLD);
					}
				}
				else {
					if(lr != 0 || onLadder == true) {
						animationCounter += 1;
					}
					if(carryComponent.isActive() == false) {
						if(equipmentSystem.isItemIDActive(1) == false) {
							drawComponent.setDrawState(LinkDrawState.WALK);
						}
						else {
							if(shieldComponent.isActive() == false) {
								drawComponent.setDrawState(LinkDrawState.SHIELD_DOWN_WALK);
							}
							else {
								drawComponent.setDrawState(LinkDrawState.SHIELD_UP_WALK);
							}
						}
					}
					else {
						drawComponent.setDrawState(LinkDrawState.ITEM_HOLD);
					}
				}
				if(wallPush == true) {
					drawComponent.setDrawState(LinkDrawState.PUSH);
				}
			}
			else {
				animationCounter += 1;
				if(animationCounter >= 30) {
					animationCounter = 29;
				}
				if(recoilComponent.isActive() == false) {
					drawComponent.setDrawState(LinkDrawState.JUMP);
				}
			}
			drawComponent.setDirection(direction.getDirection());
			drawComponent.setFrameCounter(animationCounter);

			if(onLadder == true) {
				if(equipmentSystem.isItemIDActive(1) == false) {
					drawComponent.setDrawState(LinkDrawState.WALK);
				}
				else {
					if(shieldComponent.isActive() == false) {
						drawComponent.setDrawState(LinkDrawState.SHIELD_DOWN_WALK);
					}
					else {
						drawComponent.setDrawState(LinkDrawState.SHIELD_UP_WALK);
					}
				}
				drawComponent.setDirection(Direction.UP);
			}
		}
		else if(swordActive == 1) {
			if(onGround == true || onLadder == true) {
				if(lr == 0 && ud == 0) {
					animationCounter = 0;
					if(equipmentSystem.isItemIDActive(1) == false) {
						drawComponent.setDrawState(LinkDrawState.IDLE);
					}
					else {
						if(shieldComponent.isActive() == false) {
							drawComponent.setDrawState(LinkDrawState.SHIELD_DOWN_IDLE);
						}
						else {
							drawComponent.setDrawState(LinkDrawState.SHIELD_UP_IDLE);
						}
					}
				}
				else {
					animationCounter += 1;
					if(equipmentSystem.isItemIDActive(1) == false) {
						drawComponent.setDrawState(LinkDrawState.WALK);
					}
					else {
						if(shieldComponent.isActive() == false) {
							drawComponent.setDrawState(LinkDrawState.SHIELD_DOWN_WALK);
						}
						else {
							drawComponent.setDrawState(LinkDrawState.SHIELD_UP_WALK);
						}
					}
				}
				drawComponent.setFrameCounter(animationCounter);
			}
			else {
				animationCounter += 1;
				if(animationCounter >= 30) {
					animationCounter = 29;
				}
				drawComponent.setDrawState(LinkDrawState.WALK);
				drawComponent.setFrameCounter(8);
			}
			drawComponent.setDirection(direction.getDirection());
		}
		if(carryComponent.getState() == CarryState.TAKE) {
			drawComponent.setDrawState(LinkDrawState.PULL);
			drawComponent.setFrameCounter(10);
			drawComponent.setDirection(direction.getDirection());
		}
		else if(carryComponent.getState() == CarryState.THROW) {
			drawComponent.setDrawState(LinkDrawState.SWORD_ATTACK);
			drawComponent.setFrameCounter(5);
			drawComponent.setDirection(direction.getDirection());
		}

		currentLR = lr;
		currentUD = ud;
		lastOnGround = onGround;
		lastOnLadder = onLadder;

		lr = 0;
		ud = 0;
		onGround = false;
		wallPush = false;

		if(invisibility > 0) {
			invisibility -= 1;
		}
		drawComponent.setHit(invisibility > 0);
		drawComponent.update();
	}

	@Override
	public void draw() {
		drawComponent.draw();
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
//		if(direction.getDirection() == Direction.LEFT) {
//			drawComponent.setTexture(0, 0, 14, 16);
//			drawComponent.setSize(14, 16);
//			drawComponent.setSpriteOffset(-2, -1);
//		}
//		else if(direction.getDirection() == Direction.RIGHT) {
//			drawComponent.setTexture(90, 0, 14, 16);
//			drawComponent.setSize(14, 16);
//			drawComponent.setSpriteOffset(-2, -1);
//		}
//		else if(direction.getDirection() == Direction.UP) {
//			drawComponent.setTexture(53, 0, 12, 16);
//			drawComponent.setSize(12, 16);
//			drawComponent.setSpriteOffset(-1, -1);
//		}
//		else if(direction.getDirection() == Direction.DOWN) {
//			drawComponent.setTexture(27, 0, 13, 16);
//			drawComponent.setSize(13, 16);
//			drawComponent.setSpriteOffset(-1.5f, -1);
//		}
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());

		swordComponent.draw();
		carryComponent.updateItemPosition();
	}

	@Override
	public StateType getType() {
		return StateType.PLATFORM;
	}

	private float speed() {
		if(equipmentSystem.isPieceOfPowerActive() == true) {
			return 1.25f;
		}
		return 0.9f;
	}
}
