package com.la.game_objects.enemy.rolling_bones;

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
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.engine.util.Coordinate;
import com.engine.util.ICoordinate;
import com.engine.util.ICoordinate.Vector;
import com.engine.util.IRNG;
import com.la.aspects.EnemyTag;
import com.la.aspects.RoomTransition;
import com.la.aspects.SwordHit;
import com.la.aspects.enemy_hit.EnemyHit;
import com.la.aspects.enemy_hit.EnemyHit.Result;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.aspects.wall_bounce.WallBounceStandard;
import com.la.factory.IRoomFactory;
import com.la.game_objects.Shadow;
import com.la.game_objects.Shadow.ShadowDependency;
import com.la.game_objects.enemy.PowerUpSwordHitTrailController;
import com.la.game_objects.enemy.PowerUpSwordHitTrailController.PowerUpSwordHitTrailControllerDependency;
import com.la.game_objects.enemy.RecoilIndicatorController;
import com.la.game_objects.enemy.RecoilIndicatorController.RecoilIndicatorControllerDependency;
import com.la.game_objects.link.controller.top_down.JumpTopDownComponent;
import com.la.game_objects.link.controller.top_down.JumpTopDownComponent.JumpTopDownDependency;
import com.la.game_objects.link.controller.top_down.RecoilTopDownComponent;
import com.la.game_objects.link.controller.top_down.RecoilTopDownComponent.RecoilTopDownDependency;

public class RollingBones implements IGameObject {
	public interface RollingBonesCallback {
		void onPush();
		void onDeath(int x, int y);
		void createPoof(int x, int y);
	}

	private enum RollingBonesState {
		INITIAL_MOVE,
		JUMP,
		TURN_AROUND,
		PUSH
	}

	private SoundSystem soundSystem;
	private TextureDrawComponent drawComponent;
	private Shadow shadow;
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;
	private IRNG rng;

	private RollingBonesState state = RollingBonesState.INITIAL_MOVE;
	private JumpTopDownComponent jumpComponent;
	private PowerUpSwordHitTrailController trailController;
	private RecoilTopDownComponent recoilComponent;
	private DirectionByDelta direction;
	private boolean wallCollision, moveUp, moveLeft = true, lastRecoil;
	private int wallCollisionY;
	private float height, deltaX, deltaY;
	private int counter = 3, hp = 16, invisibility;
	private boolean dead;
	private ICoordinate coordinate;
	private RollingBonesCallback callback;

	private RecoilIndicatorController recoilIndicatorController;

	private boolean remove;
	private int uniqueID;

	public RollingBones(float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			IRoomFactory roomFactory,
			IRNG rng,
			RollingBonesCallback callback) {
		this.soundSystem = soundSystem;
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.rng = rng;
		this.callback = callback;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(449, 66, 20, 24);
		drawComponent.setSize(20, 24);
		drawComponent.setSpriteOffset(-2, -8);

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x - 8, y - 8, 16, 16);
		spatialComponent.setCollisionResponse((collidedID)-> {
			if(dead == false) {
				EnemyHit enemyHit = aspectSystem.getAspect(collidedID, AspectType.ENEMY_HIT);
				if(enemyHit != null) {
					Result result = enemyHit.hit(
							spatialComponent.getX(), spatialComponent.getY(),
							spatialComponent.getW(), spatialComponent.getH(), direction.getDirection(), -1, 4, 0);
					if(result.type == EnemyHit.ResultType.SHIELD) {
						recoilComponent.hit(result.cx, result.cy, 30, 15);
					}
				}
			}
		});

		shadow = new Shadow(gfxSystem, new ShadowDependency() {
			@Override
			public float getY() {
				return spatialComponent.getY();
			}
			@Override
			public float getH() {
				return spatialComponent.getH();
			}
			@Override
			public float getCenterX() {
				return spatialComponent.getCenterX();
			}
		});

		jumpComponent = new JumpTopDownComponent(new JumpTopDownDependency() {
			@Override
			public void update(float heightArg, boolean isJumping) {
				height = heightArg;
			}
			@Override
			public void onJump() {
				soundSystem.bossJump();
			}
			@Override
			public void onLand() {
			}
		});
		recoilComponent = new RecoilTopDownComponent(new RecoilTopDownDependency() {
			@Override
			public boolean wallCollision() {
				return wallCollision || wallCollisionY != -1;
			}
			@Override
			public void setDelta(float dx, float dy) {
				spatialComponent.setDelta(dx, dy);
			}
			@Override
			public float getCenterX() {
				return spatialComponent.getCenterX();
			}
			@Override
			public float getCenterY() {
				return spatialComponent.getCenterY();
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

		direction = new DirectionByDelta();

		aspectSystem.addAspect(new WallBounce(uniqueID, spatialComponent, new WallBounceStandard(spatialComponent) {
			@Override
			public void onLeftSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onLeftSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				if(moveLeft == false) {
					wallCollision = true;
				}
				wallCollisionY = 2;
			}
			@Override
			public void onRightSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onRightSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				if(moveLeft == true) {
					wallCollision = true;
				}
				wallCollisionY = 2;
			}
			@Override
			public void onUpSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onUpSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				wallCollisionY = 0;
			}
			@Override
			public void onDownSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onDownSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				wallCollisionY = 1;
			}
		}));
		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		aspectSystem.addAspect(new SwordHit(uniqueID) {
			@Override
			public SwordHitResult hit(
					float swordX, float swordY, float swordW, float swordH, float ownerCX, float ownerCY, Direction ownerDirection,
					int dmg, boolean powerUped, SwordState swordState, int counter) {
				if(dead == false && invisibility <= 0 && recoilComponent.isActive() == false) {
					trailController.setActive(powerUped);
					if(powerUped == true) {
						recoilComponent.hit(ownerCX, ownerCY, 30 * 100, 15 * 100);
					}
					else {
						recoilComponent.hit(ownerCX, ownerCY, 30, 15);
					}
					spatialComponent.stop();
					hp -= dmg;
					if(hp <= 0) {
						dead = true;
						RollingBones.this.counter = 240;
						soundSystem.miniBossDie();
					}
					else {
						soundSystem.bossHit();
					}
					invisibility = 30;
					return new SwordHitResult(SwordHitResultType.HIT, spatialComponent.getCenterX(), spatialComponent.getCenterY());
				}
				return new SwordHitResult(SwordHitResultType.NONE, spatialComponent.getCenterX(), spatialComponent.getCenterY());
			}
		});
		aspectSystem.addAspect(new EnemyTag(uniqueID));

		recoilIndicatorController = new RecoilIndicatorController(drawComponent, new RecoilIndicatorControllerDependency() {
			@Override
			public boolean isHit() {
				return invisibility > 0 || dead == true;
			}
		});
		coordinate = new Coordinate();
	}

	@Override
	public void update() {
		if(dead == false) {
			if(recoilComponent.isActive() == false) {
				if(state == RollingBonesState.INITIAL_MOVE) {
					if(jumpComponent.isJumping() == false) {
						counter -= 1;
						if(counter > 0) {
							jumpComponent.jump(1);
							spatialComponent.setDeltaX(-0.5f);
						}
						else {
							state = RollingBonesState.PUSH;
							counter = 60;
						}
					}
				}
				else if(state == RollingBonesState.JUMP) {
					if(wallCollision == false) {
						if(jumpComponent.isJumping() == false) {
							counter -= 1;
							if(counter <= 0) {
								if(moveLeft == true) {
									deltaX = -0.9f;
									spatialComponent.setDeltaX(-0.90f);
								}
								else {
									deltaX = 0.9f;
									spatialComponent.setDeltaX( 0.90f);
								}
								if(moveUp == true) {
									deltaY = -0.9f;
									spatialComponent.setDeltaY(-0.90f);
								}
								else {
									deltaY = 0.9f;
									spatialComponent.setDeltaY( 0.90f);
								}
								jumpComponent.jump(1.9f);
								moveUp = !moveUp;
								counter = 20;
							}
							else {
								spatialComponent.stop();
							}
						}
					}
					else {
						jumpComponent.zeroDelta();
						spatialComponent.stop();
						state = RollingBonesState.TURN_AROUND;
					}
				}
				else if(state == RollingBonesState.TURN_AROUND) {
					if(jumpComponent.isJumping() == false) {
						state = RollingBonesState.PUSH;
						counter = 60;
						moveLeft = !moveLeft;
					}
				}
				else if(state == RollingBonesState.PUSH) {
					counter -= 1;
					spatialComponent.stop();
					if(counter <= 0) {
						state = RollingBonesState.JUMP;
						int random = rng.getRNG(0, 1);
						if(random == 0) {
							moveUp = true;
						}
						else {
							moveUp = false;
						}
						if(spatialComponent.getCenterY() >= 128 - 2 * 16 - 8) {
							moveUp = true;
						}
						else if(spatialComponent.getCenterY() <= 0 + 2 * 16 + 8) {
							moveUp = false;
						}
					}
					if(counter == 40) {
						callback.onPush();
					}
				}
			}
			jumpComponent.update();
			recoilComponent.update();
			if(recoilComponent.isActive() == false && lastRecoil == true) {
				if((wallCollision == true || wallCollisionY != -1) && state == RollingBonesState.JUMP) {
					spatialComponent.setDeltaX(deltaX);
					if(wallCollisionY == 1) {
						spatialComponent.setDeltaY(0.9f);
						moveUp = false;
					}
					else if(wallCollisionY == 0) {
						spatialComponent.setDeltaY(-0.9f);
						moveUp = true;
					}
				}
			}
			direction.update(spatialComponent.getDeltaX(), spatialComponent.getDeltaY());
			if(invisibility > 0) {
				invisibility -= 1;
			}
			trailController.update();
		}
		else {
			counter -= 1;
			if(counter <= 0) {
				spatialComponent.stop();
				setToRemove();
				callback.onDeath((int)spatialComponent.getCenterX(), (int)(spatialComponent.getCenterY() - height));
			}
			else {
				if(counter <= 120 && counter % 8 == 0) {
					int angle = rng.getRNG(0, 359);
					Vector d = coordinate.angleToDelta(angle);
					float l = rng.getRNG(3, 16);
					callback.createPoof(
						(int)(spatialComponent.getCenterX() + d.x * l),
						(int)(spatialComponent.getCenterY() + d.y * l));
					soundSystem.bossDiesInFlames();
				}
			}
		}
		wallCollision = false;
		wallCollisionY = -1;
		lastRecoil = recoilComponent.isActive();
		recoilIndicatorController.update();
	}
	@Override
	public void draw() {
		if(dead == false) {
			if(moveLeft == true) {
				drawComponent.setFlipX(false);
			}
			else {
				drawComponent.setFlipX(true);
			}
			if(state != RollingBonesState.PUSH) {
				if(jumpComponent.isJumping() == false) {
					drawComponent.setTexture(449, 66, 20, 24);
					drawComponent.setSize(20, 24);
					drawComponent.setSpriteOffset(-2, -8);
				}
				else {
					drawComponent.setTexture(469, 66, 20, 24);
					drawComponent.setSize(20, 24);
					drawComponent.setSpriteOffset(-2, -8);
				}
			}
			else {
				if(counter <= 40 && counter >= 10) {
					drawComponent.setTexture(489, 66, 23, 24);
					drawComponent.setSize(23, 24);
					drawComponent.setSpriteOffset(-3.5f, -8);
				}
				else {
					drawComponent.setTexture(449, 66, 20, 24);
					drawComponent.setSize(20, 24);
					drawComponent.setSpriteOffset(-2, -8);
				}
			}
		}
		drawComponent.setHeight(-height);
		drawComponent.setPosition(
			spatialComponent.getX(),
			spatialComponent.getY());
		recoilIndicatorController.draw();

		shadow.setVisible(height > 0);
		shadow.draw();
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
		shadow.remove();
		spatialComponent.remove();

		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}
}
