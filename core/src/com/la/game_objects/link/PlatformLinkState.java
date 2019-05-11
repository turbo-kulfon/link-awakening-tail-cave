package com.la.game_objects.link;

import com.engine.IInputPort;
import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.component.jump.IJumpComponent;
import com.engine.component.jump.JumpPlatformComponent;
import com.engine.component.recoil.IRecoil;
import com.engine.component.recoil.RecoilPlatform;
import com.engine.component.recoil.RecoilPlatform.RecoilDependency;
import com.engine.component.sword.SwordComponent;
import com.engine.component.sword.SwordState;
import com.engine.direction.Direction;
import com.engine.direction.LinkDirection;
import com.engine.gfx.TextureDrawComponent;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.core.ResponseCallback;
import com.la.aspects.LadderCollision;
import com.la.aspects.LinkCollision;
import com.la.aspects.enemy_hit.EnemyHitStandard;
import com.la.aspects.enemy_hit.EnemyHitStandard.StompJump;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.aspects.wall_bounce.WallBounceCollisionResolve;
import com.la.aspects.wall_bounce.WallBounceStandard;

public class PlatformLinkState implements ILinkState {
	private IInputPort inputPort;
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;
	private SwordComponent swordComponent;
	private WallBounce wallBounce;
	private EnemyHitStandard enemyHit;
	private ActionButtonControl actionButtonControl;

	private LinkDirection linkDirection;
	private IJumpComponent jumpComponent;
	private IRecoil recoilComponent;
	private ResponseCallback responseCallback;
	private WallBounceCollisionResolve collisionResolve;
	private int lastLR, ud;
	private boolean onGround , lastOnGround, ceilHit, onLadder, lastLadder, ladderCollision;

	public PlatformLinkState(
			int uniqueID,
			IInputPort inputPort,
			IAspectSystem aspectSystem,
			TextureDrawComponent drawComponent,
			ISpatialComponent spatialComponent,
			SwordComponent swordComponent,
			WallBounce wallBounce,
			EnemyHitStandard enemyHit,
			ActionButtonControl actionButtonControl) {
		this.inputPort = inputPort;
		this.drawComponent = drawComponent;
		this.spatialComponent = spatialComponent;
		this.swordComponent = swordComponent;
		this.wallBounce = wallBounce;
		this.enemyHit = enemyHit;
		this.actionButtonControl = actionButtonControl;

		jumpComponent = new JumpPlatformComponent(0.125f, new JumpPlatformComponent.Dependency() {
			@Override
			public void onUpdate(float delta) {
				spatialComponent.setDeltaY(delta);
			}
			@Override
			public boolean isOnGround() {
				return onGround;
			}
			@Override
			public boolean ceilHit() {
				return ceilHit;
			}
			@Override
			public void unsetOnGround() {
				onGround = false;
			}
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
			@Override
			public boolean isLevitating() {
				return onLadder;
			}
		});
		recoilComponent = new RecoilPlatform(new RecoilDependency() {
			@Override
			public float getCenterX() {
				return spatialComponent.getCenterX();
			}
			@Override
			public float getCenterY() {
				return spatialComponent.getCenterY();
			}
			@Override
			public void update(float dx, float dy) {
				spatialComponent.setDeltaX(dx);
				if(dy != 0) {
					spatialComponent.setDeltaY(dy);
				}
			}
			@Override
			public boolean isOnGround() {
				return onGround;
			}
			@Override
			public void groundUnset() {
				onGround = false;
			}
		});
		linkDirection = new LinkDirection();
		collisionResolve = new WallBounceStandard(spatialComponent) {
			@Override
			public void onUpSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onUpSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				onGround = true;
			}
			@Override
			public void onDownSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onDownSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				ceilHit = true;
			}
		};
		responseCallback = new ResponseCallback() {
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

		aspectSystem.addAspect(new LadderCollision(uniqueID) {
			@Override
			public void collision(float ladderX, float ladderY, float ladderW, float ladderH, int ladderType) {
				if(spatialComponent.getCenterX() + spatialComponent.getDeltaX() >= ladderX	&&
					spatialComponent.getCenterX() + spatialComponent.getDeltaX() <= ladderX + ladderW &&
					recoilComponent.isActive() == false) {
					if(ladderType == 1) {
						if(spatialComponent.getY() + spatialComponent.getH() <= ladderY) {
							if(onLadder == false) {
								WallBounce wallBounce = aspectSystem.getAspect(uniqueID, AspectType.WALL_BOUNCE);
								if(wallBounce != null) {
									wallBounce.bounce(ladderX, ladderY, ladderW, ladderH, -1);
								}
							}
							if(ud == 1) {
								ladderCollision = true;
								onLadder = true;
							}
						}
						else {
							ladderCollision = true;
						}
					}
					else if(ladderType == -1) {
						if(ud == -1) {
							ladderCollision = true;
						}
						else if(ud == 1) {
							if(lastOnGround == true) {
								ladderCollision = false;
								onLadder = false;
							}
						}
					}
					else {
						ladderCollision = true;
					}
				}
			}
		});
	}

	@Override
	public void initialize() {
		spatialComponent.setActive(true);
		wallBounce.setCollisionResolve(collisionResolve);
		spatialComponent.setH(15);
		spatialComponent.setCollisionResponse(responseCallback);
		ladderCollision = true;
		onLadder = true;
		swordComponent.setDependency(new SwordComponent.SwordComponentDependency() {
			@Override
			public float getX() {
				return spatialComponent.getX();
			}
			@Override
			public float getY() {
				return spatialComponent.getY() + 8;
			}
			@Override
			public Direction getOwnerDirection() {
				return linkDirection.getDirection();
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
			public boolean recoil(float enemyCX, float enemyCY) {
				return false;
			}
			@Override
			public void onUpdate(SwordState swordState, int counter, Direction spinAttakcDirection) {
			}
			@Override
			public boolean isPieceOfPowerActive() {
				return false;
			}
		});

		actionButtonControl.setDependency(swordComponent, null, jumpComponent, null);
		enemyHit.setDependency(recoilComponent, new StompJump() {
			@Override
			public void jump() {
				onGround = true;
				jumpComponent.jump(1);
			}
			@Override
			public boolean isOnLadder() {
				return onLadder;
			}
			@Override
			public float getHeight() {
				return 0;
			}
			@Override
			public boolean invisibility() {
				return false;
			}
			@Override
			public void onHit() {
			}
		});
	}

	@Override
	public void pause() {
	}

	@Override
	public void update() {
		int lr = 0;
		ud = 0;
		if(inputPort.isLeftButtonPressed() == true) {
			lr = -1;
		}
		else if(inputPort.isRightButtonPressed() == true) {
			lr = 1;
		}
		if(inputPort.isUpButtonPressed() == true) {
			ud = -1;
		}
		else if(inputPort.isDownButtonPressed() == true) {
			ud = 1;
		}

		if(inputPort.isStartButtonPressed() == true) {
			recoilComponent.hit(80, 0, 0, 0);
		}

		if(inputPort.isBButtonPressed() == true) {
			actionButtonControl.BButtonAction();
		}
		if(inputPort.isAButtonPressed() == true) {
			actionButtonControl.AButtonAction();
		}

		if(ladderCollision == true) {
			if(ud > 0) {
				onLadder = true;
			}
			else if(ud < 0) {
				onLadder = true;
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
			ladderCollision = false;
		}
		else {
			onLadder = false;
		}

		if(onGround == true || (lastLadder == true && onLadder == true)) {
			if(lr < 0) {
				spatialComponent.setDeltaX(-1);
			}
			else if(lr > 0) {
				spatialComponent.setDeltaX(1);
			}
			else {
				spatialComponent.setDeltaX(0);
			}
		}
		else {
			if(lr < 0) {
				spatialComponent.xAxisToValue(-1, 0.1f);
			}
			else if(lr > 0) {
				spatialComponent.xAxisToValue(1, 0.1f);
			}
			else {
				spatialComponent.xAxisToZero(0.05f);
			}
		}
		if(lastOnGround == true && onGround == false) {
			if(lastLR < 0) {
				spatialComponent.setDeltaX(-1);
			}
			else if(lastLR > 0) {
				spatialComponent.setDeltaX(1);
			}
		}
		recoilComponent.update();
		jumpComponent.update();

		lastLadder = onLadder;
		linkDirection.update(lr, ud);
		lastLR = lr;
		swordComponent.update();

		lastOnGround = onGround;
		onGround = false;
		ceilHit = false;
	}

	@Override
	public void draw() {
		if(linkDirection.getDirection() == Direction.LEFT) {
			drawComponent.setTexture(0, 0, 14, 16);
			drawComponent.setSize(14, 16);
			drawComponent.setSpriteOffset(-2, -1);
		}
		else if(linkDirection.getDirection() == Direction.RIGHT) {
			drawComponent.setTexture(90, 0, 14, 16);
			drawComponent.setSize(14, 16);
			drawComponent.setSpriteOffset(-2, -1);
		}
		else if(linkDirection.getDirection() == Direction.UP) {
			drawComponent.setTexture(53, 0, 12, 16);
			drawComponent.setSize(12, 16);
			drawComponent.setSpriteOffset(-1, -1);
		}
		else if(linkDirection.getDirection() == Direction.DOWN) {
			drawComponent.setTexture(27, 0, 13, 16);
			drawComponent.setSize(13, 16);
			drawComponent.setSpriteOffset(-1.5f, -1);
		}
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		swordComponent.draw();
	}
}
