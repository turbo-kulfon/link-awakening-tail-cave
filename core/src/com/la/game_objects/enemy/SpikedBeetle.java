package com.la.game_objects.enemy;

import com.engine.aspect.IAspectSystem;
import com.engine.component.sword.SwordState;
import com.engine.direction.Direction;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.SpatialSystem;
import com.engine.util.Coordinate;
import com.engine.util.ICoordinate;
import com.engine.util.ICoordinate.Vector;
import com.engine.util.IRNG;
import com.la.aspects.SwordHit.SwordHitResult;
import com.la.aspects.SwordHit.SwordHitResultType;
import com.la.factory.IRoomFactory;
import com.la.game_objects.FallAnimation;
import com.la.game_objects.Shadow;
import com.la.game_objects.enemy.DefeatSparkController.DefeatSparkControllerDependency;
import com.la.game_objects.enemy.PowerUpSwordHitTrailController.PowerUpSwordHitTrailControllerDependency;
import com.la.game_objects.enemy.RecoilIndicatorController.RecoilIndicatorControllerDependency;
import com.la.game_objects.enemy.update.EnemyController2;
import com.la.game_objects.enemy.update.IEnemyController;
import com.la.game_objects.enemy.update.IObjectController;
import com.la.game_objects.enemy.update.IObjectController.State;
import com.la.game_objects.enemy.update.ObjectControllerCallbackStandard;
import com.la.game_objects.link.ILinkData;

public class SpikedBeetle implements IGameObject {
	public interface SpikedBeetleCallback {
		void onDeath();
	}

	private enum SpikedBeetleState {
		WALK,
		STOP,
		CHARGE,
		FALL_DOWN,
		JUMP_UP
	}

	private TextureDrawComponent drawComponent;
	private Shadow shadow;
	private FallAnimation fallAnimation;
	private IEnemyController enemyController;

	private SpikedBeetleState state = SpikedBeetleState.WALK;
	private PowerUpSwordHitTrailController trailController;
	private RecoilIndicatorController recoilIndicatorController;
	private DefeatSparkController defeatSparkController;

	private int updateCounter, animationCounter;
	private Direction direction;
	private Vector jumpFrom;
	private boolean wallCollision;
	private boolean remove;
	private float speed = 0.5f;

	public SpikedBeetle(float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			IRoomFactory roomFactory,
			ILinkData linkData,
			IEnemyDefeatedPrize prize,
			SpikedBeetleCallback callback,
			IRNG rng) {
		ICoordinate coordinate = new Coordinate();

		prize.setRupeeDropChance(50);
		prize.setHeartDropChance(25);

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(416, 34, 16, 16);
		drawComponent.setSize(16, 16);
		drawComponent.setSpriteOffset(-2, -6);

		enemyController = new EnemyController2(
				-1,
				x, y, 12, 10,
				4,
				true, 0, 2, true,
				soundSystem, spatialSystem, uniqueIDManager, aspectSystem,
				new ObjectControllerCallbackStandard() {
					@Override
					public boolean onDeath(IObjectController controller) {
						callback.onDeath();
						return defeatSparkController.onDead();
					}
					@Override
					public SwordHitResult onSwordCollision(IObjectController controller,
							float swordX, float swordY, float swordW, float swordH,
							float linkCX, float linkCY, int dmg, boolean powerUped,
							SwordState swordState, int swordCounter) {
						if(state == SpikedBeetleState.WALK ||
							state == SpikedBeetleState.STOP ||
						    state == SpikedBeetleState.CHARGE ||
						    state == SpikedBeetleState.JUMP_UP) {
							state = SpikedBeetleState.STOP;
							updateCounter = 10;
							return new SwordHitResult(SwordHitResultType.DEFLECT, controller.getCenterX(), controller.getCenterY());
						}
						defeatSparkController.setHit(powerUped);
						trailController.setActive(powerUped);
						return super.onSwordCollision(controller, swordX, swordY, swordW, swordH, linkCX, linkCY, dmg, powerUped, swordState, swordCounter);
					}
					@Override
					public void onLinkShieldCollision(IObjectController controller, float linkCX, float linkCY) {
						if(state == SpikedBeetleState.WALK ||
							state == SpikedBeetleState.STOP ||
							state == SpikedBeetleState.CHARGE) {
							state = SpikedBeetleState.FALL_DOWN;
							updateCounter = 120;
							jumpFrom = coordinate.calculateDelta(
								linkData.getCenterX(), linkData.getCenterY(),
								controller.getCenterX(), controller.getCenterY()
							);
						}
					}
					@Override
					public void onWallCollisionFromLeft(IObjectController controller) {
						super.onWallCollisionFromLeft(controller);
						wallCollision = true;
					}
					@Override
					public void onWallCollisionFromRight(IObjectController controller) {
						super.onWallCollisionFromRight(controller);
						wallCollision = true;
					}
					@Override
					public void onWallCollisionFromUp(IObjectController controller) {
						super.onWallCollisionFromUp(controller);
						wallCollision = true;
					}
					@Override
					public void onWallCollisionFromDown(IObjectController controller) {
						super.onWallCollisionFromDown(controller);
						wallCollision = true;
					}
					@Override
					public boolean onBombCollision(IObjectController controller, float bombCX, float bombCY, boolean isPlayerOwner) {
						if(state == SpikedBeetleState.FALL_DOWN) {
							defeatSparkController.setHit(false);
							trailController.setActive(false);
							return super.onBombCollision(controller, bombCX, bombCY, isPlayerOwner);							
						}
						return false;
					}
				}) {

				@Override
				public void update(IObjectController controller) {
					if(state == SpikedBeetleState.WALK) {
						speed = 0.5f;
						animationCounter += 1;
						if(animationCounter > 18) {
							animationCounter = 0;
						}
						updateCounter -= 1;
						if(updateCounter < 0) {
							state = SpikedBeetleState.STOP;
							updateCounter = 30;
						}
						else {
							if(direction == Direction.UP) {
								controller.setMoveDelta(0, -speed);
							}
							else if(direction == Direction.LEFT) {
								controller.setMoveDelta(-speed, 0);
							}
							else if(direction == Direction.DOWN) {
								controller.setMoveDelta(0, speed);
							}
							else if(direction == Direction.RIGHT) {
								controller.setMoveDelta(speed, 0);
							}
							
							if(linkData.getCenterY() >= controller.getY() && linkData.getCenterY() <= controller.getY() + 16) {
								if(controller.getCenterX() > linkData.getCenterX()) {
									direction = Direction.LEFT;
								}
								else {
									direction = Direction.RIGHT;
								}
								state = SpikedBeetleState.CHARGE;
								controller.stop();
							}
							if(linkData.getCenterX() >= controller.getX() && linkData.getCenterX() <= controller.getX() + 16) {
								if(controller.getCenterY() > linkData.getCenterY()) {
									direction = Direction.UP;
								}
								else {
									direction = Direction.DOWN;
								}
								state = SpikedBeetleState.CHARGE;
								controller.stop();
							}
						}

					}
					else if(state == SpikedBeetleState.STOP) {
						controller.stop();
						speed = 0.5f;
						updateCounter -= 1;
						if(updateCounter < 0) {
							int newDir = rng.getRNG(0, 3);
							if(newDir == 0) {
								direction = Direction.LEFT;
							}
							else if(newDir == 1) {
								direction = Direction.RIGHT;
							}
							else if(newDir == 2) {
								direction = Direction.UP;
							}
							else if(newDir == 3) {
								direction = Direction.DOWN;
							}
							updateCounter = rng.getRNG(20, 40);
							state = SpikedBeetleState.WALK;
						}
						else {
							if(linkData.getCenterY() >= controller.getY() && linkData.getCenterY() <= controller.getY() + 16) {
								if(controller.getCenterX() > linkData.getCenterX()) {
									direction = Direction.LEFT;
								}
								else {
									direction = Direction.RIGHT;
								}
								state = SpikedBeetleState.CHARGE;
								controller.stop();
							}
							if(linkData.getCenterX() >= controller.getX() && linkData.getCenterX() <= controller.getX() + 16) {
								if(controller.getCenterY() > linkData.getCenterY()) {
									direction = Direction.UP;
								}
								else {
									direction = Direction.DOWN;
								}
								state = SpikedBeetleState.CHARGE;
								controller.stop();
							}
						}
					}
					else if(state == SpikedBeetleState.CHARGE) {
						animationCounter += 2;
						if(animationCounter > 18) {
							animationCounter = 0;
						}

						speed += 0.05f;
						if(speed > 1.75f) {
							speed = 1.75f;
						}

						if(direction == Direction.UP) {
							controller.setMoveDelta(0, -speed);
						}
						else if(direction == Direction.LEFT) {
							controller.setMoveDelta(-speed, 0);
						}
						else if(direction == Direction.DOWN) {
							controller.setMoveDelta(0, speed);
						}
						else if(direction == Direction.RIGHT) {
							controller.setMoveDelta(speed, 0);
						}

						if(wallCollision == true) {
							state = SpikedBeetleState.STOP;
							updateCounter = 60;
						}
					}
					else if(state == SpikedBeetleState.FALL_DOWN) {
						if(jumpFrom != null) {
							controller.setMoveDelta(jumpFrom.x, jumpFrom.y);
							controller.jumpBounce(2.25f);
							jumpFrom = null;
						}
						else {
							if(controller.getCurrentState() != State.JUMPING) {
								animationCounter += 1;
								if(animationCounter > 18) {
									animationCounter = 0;
								}
								controller.stop();
								updateCounter -= 1;
								if(updateCounter < 0) {
									controller.jump(1.5f);
									state = SpikedBeetleState.JUMP_UP;
								}
							}
						}
					}
					else if(state == SpikedBeetleState.JUMP_UP) {
						controller.stop();
						if(controller.getCurrentState() != State.JUMPING) {
							state = SpikedBeetleState.STOP;
							updateCounter = 0;
						}
					}

					wallCollision = false;
				};
			};

		shadow = new Shadow(gfxSystem, new ShadowDependencyEnemy(enemyController.getController()));
		fallAnimation = new FallAnimation(drawComponent, ()-> {
			return enemyController.getController().getCurrentState() == State.FALLING;
		}, 1, 0);
		trailController = new PowerUpSwordHitTrailController(roomFactory, new PowerUpSwordHitTrailControllerDependency() {
			@Override
			public float getCenterX() {
				return enemyController.getController().getCenterX();
			}
			@Override
			public float getCenterY() {
				return enemyController.getController().getCenterY();
			}
			@Override
			public boolean isInRecoilMode() {
				return enemyController.getController().getCurrentState() == State.RECOIL;
			}
		});
		recoilIndicatorController = new RecoilIndicatorController(drawComponent, new RecoilIndicatorControllerDependency() {
			@Override
			public boolean isHit() {
				return enemyController.getController().getInvisbilityFrame() > 0;
			}
		});
		defeatSparkController = new DefeatSparkController(prize, roomFactory, new DefeatSparkControllerDependency() {
			@Override
			public float getCenterX() {
				return enemyController.getController().getCenterX();
			}
			@Override
			public float getCenterY() {
				return enemyController.getController().getCenterY();
			}
		});
	}

	@Override
	public void update() {
		enemyController.updateController();
		trailController.update();
		recoilIndicatorController.update();
		fallAnimation.update();
	}
	@Override
	public void draw() {
		if(state == SpikedBeetleState.WALK || state == SpikedBeetleState.CHARGE) {
			if(animationCounter <= 8) {
				drawComponent.setTexture(416, 34, 16, 16);
				drawComponent.setSize(16, 16);
				drawComponent.setSpriteOffsetY(-6);
			}
			else {
				drawComponent.setTexture(432, 34, 16, 16);
				drawComponent.setSize(16, 16);
				drawComponent.setSpriteOffsetY(-6);
			}
		}
		else if(state == SpikedBeetleState.FALL_DOWN) {
			if(animationCounter <= 8) {
				drawComponent.setTexture(416, 50, 16, 15);
				drawComponent.setSize(16, 15);
				drawComponent.setSpriteOffsetY(-5);
			}
			else {
				drawComponent.setTexture(432, 50, 16, 15);
				drawComponent.setSize(16, 15);
				drawComponent.setSpriteOffsetY(-5);
			}
		}
		else if(state == SpikedBeetleState.JUMP_UP) {
			drawComponent.setTexture(416, 34, 16, 16);
			drawComponent.setSize(16, 16);
			drawComponent.setSpriteOffsetY(-6);
		}

		recoilIndicatorController.draw();
		drawComponent.setHeight(-enemyController.getController().getHeight());
		drawComponent.setPosition(
			enemyController.getController().getX(),
			enemyController.getController().getY());

		shadow.setVisible(enemyController.getController().getHeight() > 0 && enemyController.getController().getCurrentState() != State.FALLING);
		shadow.draw();

		fallAnimation.draw();
	}

	@Override
	public void setToRemove() {
		remove = true;
	}
	@Override
	public boolean shouldRemove() {
		return enemyController.shouldRemove() || remove == true;
	}
	@Override
	public void onRemove() {
		enemyController.remove();
		drawComponent.remove();
		shadow.remove();
	}
}
