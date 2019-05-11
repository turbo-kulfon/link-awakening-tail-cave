package com.la.game_objects.link;

import java.util.concurrent.atomic.AtomicBoolean;

import com.engine.IInputPort;
import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.component.carry.BombComponent;
import com.engine.component.carry.CarryComponent;
import com.engine.component.carry.IBombComponent;
import com.engine.component.carry.ICarryComponent;
import com.engine.component.fall.FallComponent;
import com.engine.component.fall.FallComponent.FallDependency;
import com.engine.component.fall.IFallComponent;
import com.engine.component.jump.IJumpComponent;
import com.engine.component.jump.JumpComponent;
import com.engine.component.recoil.IRecoil;
import com.engine.component.recoil.Recoil;
import com.engine.component.recoil.RecoilDependencyStandard;
import com.engine.component.shield.ShieldComponent;
import com.engine.component.shield.ShieldDependencyStandard;
import com.engine.component.sword.SwordComponent;
import com.engine.component.sword.SwordState;
import com.engine.direction.Direction;
import com.engine.direction.LinkDirection;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.observer.Observatory;
import com.engine.spatial.IOutsideViewCheck;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.OutsideViewCheck;
import com.engine.spatial.OutsideViewCheckPosition;
import com.engine.spatial.SpatialSystem;
import com.engine.spatial.core.ResponseCallback;
import com.engine.util.CollisionDetection;
import com.engine.util.ICollisionDetection;
import com.la.aspects.HoleCollision;
import com.la.aspects.LinkCollision;
import com.la.aspects.LinkInteraction;
import com.la.aspects.enemy_hit.EnemyHitStandard;
import com.la.aspects.enemy_hit.EnemyHitStandard.StompJump;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.aspects.wall_bounce.WallBounceCollisionResolve;
import com.la.aspects.wall_bounce.WallBounceStandard;
import com.la.equipment.IEquipmentSystem;
import com.la.factory.IRoomFactory;
import com.la.game_objects.Shadow;
import com.la.observer.LinkFallObserverData;

public class TopDownLinkState implements ILinkState {
	private IInputPort inputPort;
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;
	private IAspectSystem aspectSystem;
	private Shadow shadow;
	private SwordComponent swordComponent;
	private ShieldComponent shieldComponent;
	private ICarryComponent carryComponent;
	private IBombComponent bombComponent;
	private IFallComponent fallComponent;
	private EnemyHitStandard enemyHit;
	private WallBounce wallBounce;
	private ActionButtonControl actionButtonControl;

	private IJumpComponent jumpComponent;
	private IRecoil recoil;
	private LinkDirection linkDirection;
	private ResponseCallback responseCallback;
	private WallBounceCollisionResolve collisionResolve;
	private ICollisionDetection collisionDetection;
	private IOutsideViewCheck outsideViewCheck;
	private float height;
	private boolean fallPositionRestore;

	private int invisibility;

	public TopDownLinkState(
			int uniqueID,
			IInputPort inputPort,
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			TextureDrawComponent drawComponent,
			ISpatialComponent spatialComponent,
			Shadow shadow,
			SwordComponent swordComponent,
			ShieldComponent shieldComponent,
			ILinkData linkData,
			EnemyHitStandard enemyHit,
			WallBounce wallBounce,
			IEquipmentSystem equipmentSystem,
			ActionButtonControl actionButtonControl,
			IRoomFactory roomFactory,
			Observatory<LinkFallObserverData> linkFallObserver) {
		this.inputPort = inputPort;
		this.drawComponent = drawComponent;
		this.spatialComponent = spatialComponent;
		this.aspectSystem = aspectSystem;
		this.shadow = shadow;
		this.swordComponent = swordComponent;
		this.shieldComponent = shieldComponent;
		this.enemyHit = enemyHit;
		this.wallBounce = wallBounce;
		this.actionButtonControl = actionButtonControl;

		collisionDetection = new CollisionDetection();
		jumpComponent = new JumpComponent(0.125f, 2.2f, (height, isJumping)-> {
			this.height = height;
		});
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
				spatialComponent.setActive(false);
			}
			@Override
			public void onFallEnd() {
				if(fallPositionRestore == true) {
					spatialComponent.setActive(true);
					linkData.restoreLastPosition();
				}
				linkFallObserver.update(null);
			}
		});
		linkDirection = new LinkDirection();
		collisionResolve = new WallBounceStandard(spatialComponent);
		responseCallback = new ResponseCallback() {
			@Override
			public void response(int collidedID) {
				if(height <= 0) {
					LinkCollision linkCollision = aspectSystem.getAspect(collidedID, AspectType.LINK_COLLISION);
					if(linkCollision != null) {
						linkCollision.collision(
							spatialComponent.getX(), spatialComponent.getY(),
							spatialComponent.getW(), spatialComponent.getH(), 0, false);
					}
				}
			}
		};
		recoil = new Recoil(new RecoilDependencyStandard(spatialComponent));

		aspectSystem.addAspect(new HoleCollision(uniqueID) {
			@Override
			public HoleCollisionResult collision(float holeX, float holeY, float holeW, float holeH, boolean restoreLastPosition) {
				if(height <= 0 && fallComponent.isFalling() == false && collisionDetection.collisionDetect(holeX, holeY, holeW, holeH, spatialComponent.getCenterX(), spatialComponent.getCenterY()) == true) {
					fallComponent.fall(holeX + holeW/2.0f, holeY + holeH/2.0f);
					fallPositionRestore = restoreLastPosition;
					carryComponent.tossDown();
					return HoleCollisionResult.LINK;
				}
				return HoleCollisionResult.NONE;
			}
		});

		carryComponent = new CarryComponent(spatialComponent, equipmentSystem, aspectSystem, linkDirection);
		bombComponent = new BombComponent(carryComponent, spatialComponent, aspectSystem, spatialSystem, roomFactory);

		outsideViewCheck = new OutsideViewCheck(
				new OutsideViewCheckPosition(spatialComponent),
				new LinkOutsideCheckView(spatialComponent, recoil));
	}
	@Override
	public void initialize() {
		spatialComponent.setActive(true);
		wallBounce.setCollisionResolve(collisionResolve);
		spatialComponent.setH(7);
		spatialComponent.setCollisionResponse(responseCallback);
		jumpComponent.stop();
		fallComponent.stop();
		swordComponent.setDependency(new SwordComponent.SwordComponentDependency() {
			@Override
			public float getX() {
				return spatialComponent.getX();
			}
			@Override
			public float getY() {
				return spatialComponent.getY() - height;
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
		shieldComponent.setDependency(null, new ShieldDependencyStandard(spatialComponent, linkDirection));
		enemyHit.setDependency(recoil, new StompJump() {
			@Override
			public void jump() {
				
			}
			@Override
			public boolean isOnLadder() {
				return false;
			}
			@Override
			public float getHeight() {
				return height;
			}
			@Override
			public boolean invisibility() {
				return invisibility > 0;
			}
			@Override
			public void onHit() {
				invisibility = 60;
			}
		});

		actionButtonControl.setDependency(swordComponent, shieldComponent, jumpComponent, bombComponent);
	}
	@Override
	public void pause() {
		shadow.setVisible(false);
	}

	@Override
	public void update() {
		int lr = 0, ud = 0;
		spatialComponent.stop();
		if(inputPort.isLeftButtonPressed() == true) {
			spatialComponent.setDeltaX(-1);
			lr = -1;
		}
		else if(inputPort.isRightButtonPressed() == true) {
			spatialComponent.setDeltaX( 1);
			lr = 1;
		}
		if(inputPort.isUpButtonPressed() == true) {
			spatialComponent.setDeltaY(-1);
			ud = -1;
		}
		else if(inputPort.isDownButtonPressed() == true) {
			spatialComponent.setDeltaY( 1);
			ud = 1;
		}
		
		if(fallComponent.isFalling() == false) {
			if(inputPort.isBButtonPressed() == true) {
				if(carryComponent.pressedBButton() == true) {
					actionButtonControl.BButtonAction();
				}
			}
			if(inputPort.isAButtonPressed() == true) {
				if(carryComponent.pressedAButton() == true) {
					AtomicBoolean interacted = new AtomicBoolean();
					aspectSystem.forEachAspect(AspectType.LINK_INTERACT, (id, aspect)-> {
						LinkInteraction convert = (LinkInteraction) aspect;
						if(convert.interact(
								spatialComponent.getX(), spatialComponent.getY(),
								spatialComponent.getW(), spatialComponent.getH(),
								linkDirection.getDirection()) == true) {
							interacted.set(true);
						}
					});
					if(interacted.get() == false) {
						actionButtonControl.AButtonAction();
					}
				}
			}
		}
		
		linkDirection.update(lr, ud);
		recoil.update();
		jumpComponent.update();
		swordComponent.update();
		shieldComponent.update();
		fallComponent.update();
		carryComponent.update();
		bombComponent.update();

		outsideViewCheck.update();

		if(invisibility > 0) {
			invisibility -= 1;
		}
	}

	@Override
	public void draw() {
		if(linkDirection.getDirection() == Direction.LEFT) {
			drawComponent.setTexture(0, 0, 14, 16);
			drawComponent.setSize(14, 16);
			drawComponent.setSpriteOffset(-2, -9);
		}
		else if(linkDirection.getDirection() == Direction.RIGHT) {
			drawComponent.setTexture(90, 0, 14, 16);
			drawComponent.setSize(14, 16);
			drawComponent.setSpriteOffset(-2, -9);
		}
		else if(linkDirection.getDirection() == Direction.UP) {
			drawComponent.setTexture(53, 0, 12, 16);
			drawComponent.setSize(12, 16);
			drawComponent.setSpriteOffset(-1, -9);
		}
		else if(linkDirection.getDirection() == Direction.DOWN) {
			drawComponent.setTexture(27, 0, 13, 16);
			drawComponent.setSize(13, 16);
			drawComponent.setSpriteOffset(-1.5f, -9);
		}
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		drawComponent.setHeight(-height);

		shadow.setVisible(height > 0);
		shadow.draw();
		swordComponent.draw();
	}
}
