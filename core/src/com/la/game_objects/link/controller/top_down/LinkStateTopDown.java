package com.la.game_objects.link.controller.top_down;

import java.util.concurrent.atomic.AtomicBoolean;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.component.shield.ShieldComponent;
import com.engine.component.sword.SwordComponent;
import com.engine.component.sword.SwordState;
import com.engine.direction.Direction;
import com.engine.direction.LinkDirection;
import com.engine.observer.Observatory;
import com.engine.sound.SoundSystem;
import com.engine.spatial.IOutsideViewCheck;
import com.engine.spatial.IOutsideViewCheck.OutsideViewCheckCallback;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.engine.spatial.core.ResponseCallback;
import com.engine.util.CollisionDetection;
import com.engine.util.ICollisionDetection;
import com.la.aspects.BlockItemUsage;
import com.la.aspects.HeightObject;
import com.la.aspects.HeightObject.HeightObjectDependency;
import com.la.aspects.HoleCollision.HoleCollisionResult;
import com.la.aspects.LinkCollision;
import com.la.aspects.LinkInteraction;
import com.la.aspects.OneWayDoorAspect;
import com.la.aspects.OneWayDoorAspect.OneWayDoorAspectDependency;
import com.la.aspects.SlowDown;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.aspects.wall_bounce.WallBounceCollisionResolve;
import com.la.aspects.wall_bounce.WallBounceStandard;
import com.la.equipment.IEquipmentSystem;
import com.la.factory.IRoomFactory;
import com.la.game_objects.Shadow;
import com.la.game_objects.link.ILinkData;
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
import com.la.game_objects.link.controller.common.EnemyHitBase.EnemyHitCallback;
import com.la.game_objects.link.controller.common.HoleCollisionBase;
import com.la.game_objects.link.controller.common.HoleCollisionBase.HoleCollisionBaseDependency;
import com.la.game_objects.link.controller.common.LinkOutsideViewCheck;
import com.la.game_objects.link.controller.common.MagicPowderComponent;
import com.la.game_objects.link.controller.common.MagicPowderComponent.MagicPowderComponentDependency;
import com.la.game_objects.link.controller.top_down.EnemyHitTopDownCallback.EnemyHitTopDownDependency;
import com.la.game_objects.link.controller.top_down.HoleFallComponent.HoleFallDependency;
import com.la.game_objects.link.controller.top_down.JumpDownComponent.JumpDownComponentDependency;
import com.la.game_objects.link.controller.top_down.JumpTopDownComponent.JumpTopDownDependency;
import com.la.game_objects.link.controller.top_down.RecoilTopDownComponent.RecoilTopDownDependency;
import com.la.game_objects.link.draw.LinkDrawComponent;
import com.la.game_objects.link.draw.LinkDrawComponent.LinkDrawState;
import com.la.observer.LinkFallObserverData;

public class LinkStateTopDown implements ILinkState {
	private SoundSystem soundSystem;
	private LinkDrawComponent drawComponent;
	private Shadow shadow;
	private ISpatialComponent spatialComponent;
	private SwordComponent swordComponent;
	private ShieldComponent shieldComponent;
	private CarryComponent carryComponent;
	private WallBounce wallBounce;
	private EnemyHitBase enemyHit;
	private IOutsideViewCheck outsideViewCheck;
	private IEquipmentSystem equipmentSystem;

	private ResponseCallback collisionResponseCallback;
	private SwordComponent.SwordComponentDependency swordComponentDependency;
	private ShieldComponent.ShieldComponentDependency shieldComponentDependency;
	private CarryComponentDependencyStandard carryComponentDependency;
	private BombComponent bombComponent;
	private JumpTopDownComponent jumpComponent;
	private JumpDownComponent jumpDownComponent;
	private RecoilTopDownComponent recoilComponent;
	private HoleFallComponent holeFallComponent;
	private LinkDirection direction;
	private ActionButtonController actionButtonController;
	private WallBounceCollisionResolve wallBounceCollisionResolve;
	private EnemyHitCallback enemyHitCallback;
	private OutsideViewCheckCallback outsideViewCheckCallback;
	private HeightObject heightObject;

	private float height, objectHeight;
	private boolean restorePositionOnFall, positionReseted, blockMove, slowMode, blockItemUsage, wallPush, wallCollision;
	private int invisibility, swordActive;

	private int lr, ud, lastLR, lastUD, bPressed, aPressed, animationCounter;

	public LinkStateTopDown(
			int uniqueID,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IRoomFactory roomFactory,
			LinkDrawComponent drawComponent,
			Shadow shadow,
			ISpatialComponent spatialComponent,
			IAspectSystem aspectSystem,
			SwordComponent swordComponent,
			ShieldComponent shieldComponent,
			CarryComponent carryComponent,
			WallBounce wallBounce,
			EnemyHitBase enemyHit,
			HoleCollisionBase holeCollisionBase,
			IOutsideViewCheck outsideViewCheck,
			IEquipmentSystem equipmentSystem,
			Observatory<LinkFallObserverData> linkFallObserver,
			ILinkData linkData) {
		this.soundSystem = soundSystem;
		this.drawComponent = drawComponent;
		this.shadow = shadow;
		this.spatialComponent = spatialComponent;
		this.swordComponent = swordComponent;
		this.shieldComponent = shieldComponent;
		this.carryComponent = carryComponent;
		this.wallBounce = wallBounce;
		this.enemyHit = enemyHit;
		this.outsideViewCheck = outsideViewCheck;
		this.equipmentSystem = equipmentSystem;

		collisionResponseCallback = new ResponseCallback() {
			@Override
			public void response(int collidedID) {
				LinkCollision linkCollision = aspectSystem.getAspect(collidedID, AspectType.LINK_COLLISION);
				if(linkCollision != null) {
					linkCollision.collision(
						spatialComponent.getX(), spatialComponent.getY(),
						spatialComponent.getW(), spatialComponent.getH(), height, positionReseted == true);
				}
			}
		};

		jumpComponent = new JumpTopDownComponent(new JumpTopDownDependency() {
			@Override
			public void update(float heightArg, boolean isJumping) {
				height = heightArg;
			}
			@Override
			public void onJump() {
				animationCounter = 0;
				soundSystem.linkJump();
			}
			@Override
			public void onLand() {
				soundSystem.linkLand();
			}
		});
		jumpDownComponent = new JumpDownComponent(new JumpDownComponentDependency() {
			@Override
			public void setDelta(float dx, float dy) {
				spatialComponent.setDelta(dx, dy);
			}
			@Override
			public void jump() {
				jumpComponent.jump(1.8f);
				spatialComponent.setActive(false);
			}
			@Override
			public boolean isJumping() {
				return jumpComponent.isJumping();
			}
			@Override
			public void onJumpDownEnd() {
				spatialComponent.setActive(true);
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
			AtomicBoolean interacted = new AtomicBoolean();

			@Override
			public void swordSwing() {
				if(blockItemUsage == false) {
					swordComponent.attack();
				}
			}
			@Override
			public void shieldUp() {
				if(blockItemUsage == false) {
					shieldComponent.shieldUp();
				}
			}
			@Override
			public void jump() {
				if(blockItemUsage == false) {
					jumpComponent.jump(2.2f);
				}
			}
			@Override
			public void plantBomb() {
				if(blockItemUsage == false) {
					bombComponent.buttonPressed();
				}
			}
			@Override
			public void sprinkMagicPowder() {
				if(blockItemUsage == false) {
					magicPowderComponent.sprinkle();
				}
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
				interacted.set(false);
				aspectSystem.forEachAspect(AspectType.LINK_INTERACT, (id, aspect)-> {
					LinkInteraction convert = (LinkInteraction) aspect;
					if(convert.interact(
							spatialComponent.getX(), spatialComponent.getY(),
							spatialComponent.getW(), spatialComponent.getH(),
							direction.getDirection()) == true) {
						interacted.set(true);
					}
				});
				return interacted.get();
			}
		});

		carryComponentDependency = new CarryComponentDependencyStandard(spatialComponent, equipmentSystem, aspectSystem);

		direction = new LinkDirection();
		carryComponentDependency.setDirectionComponent(direction);
		wallBounceCollisionResolve = new WallBounceStandard(spatialComponent) {
			@Override
			public void onLeftSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onLeftSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				if(direction.getDirection() == Direction.RIGHT && wallDirection == 0) {
					jumpDownComponent.jumpDownRight();
				}
				else {
					if(carryComponent.isActive() == false) {
						wallPush = true;
					}
					if(direction.getDirection() == Direction.RIGHT && lastUD == 0) {
						swordComponent.thrust();
					}
				}
				wallCollision = true;
			}
			@Override
			public void onRightSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onRightSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				if(direction.getDirection() == Direction.LEFT && wallDirection == 1) {
					jumpDownComponent.jumpDownLeft();
				}
				else {
					if(carryComponent.isActive() == false) {
						wallPush = true;
					}
					if(direction.getDirection() == Direction.LEFT && lastUD == 0) {
						swordComponent.thrust();
					}
				}
				wallCollision = true;
			}
			@Override
			public void onUpSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onUpSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				if(carryComponent.isActive() == false) {
					wallPush = true;
				}
				if(direction.getDirection() == Direction.DOWN && lastLR == 0) {
					swordComponent.thrust();
				}
				wallCollision = true;
			}
			@Override
			public void onDownSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onDownSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				if(carryComponent.isActive() == false) {
					wallPush = true;
				}
				if(direction.getDirection() == Direction.UP && lastLR == 0) {
					swordComponent.thrust();
				}
				wallCollision = true;
			}
		};
		enemyHitCallback = new EnemyHitTopDownCallback(new EnemyHitTopDownDependency() {
			@Override
			public float getCenterX() {
				return spatialComponent.getCenterX();
			}
			@Override
			public float getCenterY() {
				return spatialComponent.getCenterY();
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
			public void hitRecoil(float centerX, float centerY) {
				spatialComponent.stop();
				wallCollision = false;
				recoilComponent.hit(centerX, centerY, 16, 16);
				invisibility = 60;
			}
			@Override
			public void shieldRecoil(float centerX, float centerY) {
//				spatialComponent.stop();
				if(recoilComponent.isActive() == false) {
					soundSystem.linkBounce();
				}
				wallCollision = false;
				recoilComponent.hit(centerX, centerY, 20, 20);
			}

			@Override
			public boolean isInvisible() {
				return invisibility > 0;
			}
			@Override
			public float getHeight() {
				return height;
			}
			@Override
			public void doDamage(int damage) {
				equipmentSystem.removeHeart(damage);
				soundSystem.linkHurt();
			}
		});

		holeFallComponent = new HoleFallComponent(new HoleFallDependency() {
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
			public void onFall() {}
			@Override
			public void onFallEnd() {
				if(restorePositionOnFall == true) {
					spatialComponent.setActive(true);
					linkData.restoreLastPosition();
					equipmentSystem.removeHeart(2);
					positionReseted = true;
					invisibility = 60;
				}
				else {
					drawComponent.setVisible(false);
				}
				linkFallObserver.update(null);
			}
		});

		swordComponentDependency = new SwordTopDownDependency(spatialComponent, direction) {
			@Override
			public float getY() {
				return super.getY() - height + 1;
			}
			@Override
			public float getCenterY() {
				return super.getCenterY() - height;
			}
			@Override
			public boolean recoil(float enemyCX, float enemyCY) {
				if(recoilComponent.isActive() == false) {
					recoilComponent.hit(enemyCX, enemyCY, 30, 15);
					return true;
				}
				return false;
			}
			@Override
			public void onUpdate(SwordState swordState, int counter, Direction spinAttakcDirection) {
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
		bombComponent = new BombComponent(new BombComponentDependencyStandard(spatialComponent, carryComponent, spatialSystem, aspectSystem) {
			@Override
			public void putBomb() {
				if(equipmentSystem.removeBomb() == true) {
					if(direction.getDirection() == Direction.LEFT) {
						roomFactory.createBomb(
							(int)spatialComponent.getCenterX() - 8,
							(int)spatialComponent.getCenterY() + 4,
							height);
					}
					else if(direction.getDirection() == Direction.RIGHT) {
						roomFactory.createBomb(
							(int)spatialComponent.getCenterX() + 8,
							(int)spatialComponent.getCenterY() + 4,
							height);
					}
					else if(direction.getDirection() == Direction.UP) {
						roomFactory.createBomb(
							(int)spatialComponent.getCenterX(),
							(int)spatialComponent.getCenterY() - 2,
							height);
					}
					else if(direction.getDirection() == Direction.DOWN) {
						roomFactory.createBomb(
							(int)spatialComponent.getCenterX(),
							(int)spatialComponent.getCenterY() + 8,
							height);
					}
				}
			}
		});

		ICollisionDetection collisionDetection = new CollisionDetection();

		holeCollisionBase.setDependency(new HoleCollisionBaseDependency() {
			@Override
			public HoleCollisionResult collision(float holeX, float holeY, float holeW, float holeH, boolean restoreLastPosition) {
				if(height <= 0 && holeFallComponent.isActive() == false && collisionDetection.collisionDetect(holeX, holeY, holeW, holeH, spatialComponent.getCenterX(), spatialComponent.getCenterY()) == true) {
					holeFallComponent.fall(holeX + holeW/2.0f, holeY + holeH/2.0f);
					soundSystem.linkFall();
					restorePositionOnFall = restoreLastPosition;
					spatialComponent.setActive(false);
					carryComponent.tossDown();
					swordComponent.stop();
					return HoleCollisionResult.LINK;
				}
				return HoleCollisionResult.NONE;
			}
		});

		heightObject = new HeightObject(uniqueID, new HeightObjectDependency() {
			@Override
			public float getCenterX() {
				return spatialComponent.getCenterX();
			}
			@Override
			public float getCenterY() {
				return spatialComponent.getCenterY();
			}
			@Override
			public float getHeight() {
				return height;
			}
		});
		aspectSystem.addAspect(heightObject);

		aspectSystem.addAspect(new OneWayDoorAspect(uniqueID, new OneWayDoorAspectDependency() {
			@Override
			public void setDrawComponentVisible(boolean visible) {
				drawComponent.setVisible(visible);
			}
			@Override
			public void setSpatialComponent(boolean active) {
				spatialComponent.setActive(active);
			}

			@Override
			public void moveOnYAxis(float value) {
				spatialComponent.move(0, value);
			}
			
			@Override
			public float getHeight() {
				return height;
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
			public void onEnter() {
				blockMove = true;
				swordComponent.stop();
				spatialComponent.stop();
			}
			@Override
			public void onExit() {
				blockMove = false;
			}
		}));

		aspectSystem.addAspect(new SlowDown(uniqueID) {
			@Override
			public void slowDown() {
				slowMode = true;
			}
		});

		aspectSystem.addAspect(new BlockItemUsage(uniqueID) {
			@Override
			public void block() {
				blockItemUsage = true;
			}
		});

		outsideViewCheckCallback = 
			new LinkOutsideViewCheck(
				new LinkOutsideViewCheckTopDownDependency(
						spatialComponent) {
					@Override
					public boolean isRecoil() {
						return recoilComponent.isActive();
					}
				}) {
			@Override
			public void outsideLeft() {
				super.outsideLeft();
				wallCollision = true;
			}
			@Override
			public void outsideRight() {
				super.outsideRight();
				wallCollision = true;
			}
			@Override
			public void outsideUp() {
				super.outsideUp();
				wallCollision = true;
			}
			@Override
			public void outsideDown() {
				super.outsideDown();
				wallCollision = true;
			}
		};
	}

	@Override
	public void initialize() {
		drawComponent.setVisible(true);
		spatialComponent.setSize(10, 8);
		spatialComponent.setCollisionResponse(collisionResponseCallback);
		wallBounce.setCollisionResolve(wallBounceCollisionResolve);
		enemyHit.setCallback(enemyHitCallback);
		swordComponent.setDependency(swordComponentDependency);
		shieldComponent.setDependency(soundSystem, shieldComponentDependency);
		outsideViewCheck.setCallback(outsideViewCheckCallback);
		carryComponent.setDependency(soundSystem, carryComponentDependency);
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
		if(justPressed == false) {
			bPressed = 1;
		}
		else {
			bPressed = 2;
		}
	}
	@Override
	public void AButtonPressed(boolean justPressed) {
		if(justPressed == false) {
			aPressed = 1;
		}
		else {
			aPressed = 2;
		}
	}

	@Override
	public void update() {
//		spatialComponent.stop();
		float speed = speed(), delta = 0.05f;
		if(recoilComponent.isActive() == false && blockMove == false &&
		  (swordComponent.allowMove() == true || jumpComponent.isJumping() == true) &&
		  carryComponent.canMove() == true) {
			if(lr == -1) {
				if(jumpComponent.isJumping() == false) {
					spatialComponent.setDeltaX(-speed);
				}
				else {
					spatialComponent.xAxisToValue(-1, delta);
				}
			}
			else if(lr == 1) {
				if(jumpComponent.isJumping() == false) {
					spatialComponent.setDeltaX( speed);
				}
				else {
					spatialComponent.xAxisToValue( 1, delta);
				}
			}
			else {
				if(jumpComponent.isJumping() == false) {
					spatialComponent.setDeltaX(0);
				}
				else {
					spatialComponent.xAxisToZero(delta);
				}
			}
			if(ud == -1) {
				if(jumpComponent.isJumping() == false) {
					spatialComponent.setDeltaY(-speed);
				}
				else {
					spatialComponent.yAxisToValue(-1, delta);
				}
			}
			else if(ud == 1) {
				if(jumpComponent.isJumping() == false) {
					spatialComponent.setDeltaY( speed);
				}
				else {
					spatialComponent.yAxisToValue( 1, delta);
				}
			}
			else {
				if(jumpComponent.isJumping() == false) {
					spatialComponent.setDeltaY(0);
				}
				else {
					spatialComponent.yAxisToZero(delta);
				}
			}

			if(swordComponent.allowChangeDirection() == true && jumpComponent.isJumping() == false) {
				direction.update(lr, ud);
			}
		}
		if(swordComponent.allowMove() == false && jumpComponent.isJumping() == false) {
			spatialComponent.stop();
		}

		if(holeFallComponent.isActive() == false && blockMove == false) {
			if(bPressed > 0) {
				actionButtonController.BButtonPressed(bPressed == 2);
			}
			if(aPressed > 0) {
				actionButtonController.AButtonPressed(aPressed == 2);
			}
		}

		swordComponent.update();
		shieldComponent.update();
		jumpComponent.update();
		jumpDownComponent.update();
		recoilComponent.update();
		holeFallComponent.update();
		carryComponent.update();

		if(swordActive == -1) {
			if(jumpComponent.isJumping() == false) {
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
					animationCounter += 1;
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
				if(wallPush == true && shieldComponent.isActive() == false) {
					drawComponent.setDrawState(LinkDrawState.PUSH);
				}
			}
			else {
				animationCounter += 1;
				if(animationCounter >= 30) {
					animationCounter = 29;
				}
				drawComponent.setDrawState(LinkDrawState.JUMP);
			}
			drawComponent.setDirection(direction.getDirection());
			drawComponent.setFrameCounter(animationCounter);
		}
		else if(swordActive == 1) {
			if(jumpComponent.isJumping() == false) {
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
		if(holeFallComponent.isActive() == true) {
			drawComponent.setDrawState(LinkDrawState.FALL);
			drawComponent.setFrameCounter(holeFallComponent.getCounter());
		}

		lastLR = lr;
		lastUD = ud;
		lr = 0;
		ud = 0;
		bPressed = 0;
		aPressed = 0;

		if(invisibility > 0) {
			invisibility -= 1;
		}

		objectHeight = heightObject.getHeight();

		heightObject.reset();

		if(slowMode == true) {
			if(jumpComponent.isJumping() == false) {
				spatialComponent.setDeltaX(spatialComponent.getDeltaX()/2.0f);
				spatialComponent.setDeltaY(spatialComponent.getDeltaY()/2.0f);
			}
			slowMode = false;
		}
		blockItemUsage = false;
		wallPush = false;
		wallCollision = false;
		if(spatialComponent.getDeltaX() != 0 || spatialComponent.getDeltaY() != 0) {
			positionReseted = false;
		}
		drawComponent.setHit(invisibility > 0);
		drawComponent.update();
	}

	@Override
	public void draw() {
		drawComponent.draw();
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		if(height >= objectHeight) {
			drawComponent.setHeight(- height);
		}
		else {
			drawComponent.setHeight(- height - objectHeight);
		}

		shadow.setVisible(height > 0);
		shadow.draw();

		swordComponent.draw();

		carryComponent.updateItemPosition();
	}

	@Override
	public StateType getType() {
		return StateType.TOP_DOWN;
	}

	private float speed() {
		if(equipmentSystem.isPieceOfPowerActive() == true && slowMode == false) {
			return 1.25f;
		}
		return 0.9f;
	}
}
