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

public class StalfosOrange implements IGameObject {
	public interface StalfosOrangeCallback {
		void onDeath();
	}
	private enum StalfosState {
		WALK,
		JUMP
	}

	private TextureDrawComponent drawComponent;
	private Shadow shadow;
	private IEnemyController enemyController;

	private DefeatSparkController defeatSparkController;
	private RecoilIndicatorController recoilIndicatorController;
	private PowerUpSwordHitTrailController powerUpSwordHitTrailController;
	private FallAnimation fallAnimation;

	private StalfosState state = StalfosState.WALK;
	private boolean jump;
	private Vector jumpFrom;
	private Direction direction;
	private int updateCounter, animationCounter;
	private boolean wallCollision;

	private boolean remove;

	public StalfosOrange(float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			IRoomFactory roomFactory,
			IRNG rng,
			IEnemyDefeatedPrize enemyDefeatedPrize,
			ILinkData linkData,
			StalfosOrangeCallback callback) {
		ICoordinate coordinate = new Coordinate();

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(482, 0, 15, 16);
		drawComponent.setSize(15, 16);
		drawComponent.setSpriteOffset(-2, -7);
		
		enemyController = new EnemyController2(
				-1,
				x, y, 11, 9,
				4, true, 0, 2, true,
				soundSystem, spatialSystem, uniqueIDManager, aspectSystem, new ObjectControllerCallbackStandard() {
					@Override
					public boolean onDeath(IObjectController controller) {
						callback.onDeath();
						return defeatSparkController.onDead();
					}
					@Override
					public SwordHitResult onSwordCollision(IObjectController controller, float swordX, float swordY, float swordW, float swordH, float linkCX, float linkCY, int dmg, boolean powerUped, SwordState swordState, int swordCounter) {
						if(controller.getHeight() <= 5f) {
							if(controller.getHeight() <= 0) {
								if(powerUped == false) {
									controller.recoil(linkCX, linkCY, 30, 10);
								}
								else {
									controller.recoil(linkCX, linkCY, 30 * 100, 10 * 100);
								}
								controller.stop();
							}
							controller.doDmg(dmg);
							defeatSparkController.setHit(powerUped);
							powerUpSwordHitTrailController.setActive(powerUped);
							return new SwordHitResult(SwordHitResultType.HIT, controller.getCenterX(), controller.getCenterY());
						}
						return new SwordHitResult(SwordHitResultType.NONE, 0, 0);
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
					public boolean onMagicPowderCollision(IObjectController controller, float linkCX, float linkCY) {
						jump = true;
						jumpFrom = coordinate.calculateDelta(
							linkCX, linkCY,
							controller.getCenterX(), controller.getCenterY()
						);
						return false;
					}
					@Override
					public boolean onBombCollision(IObjectController controller, float bombCX, float bombCY, boolean isPlayerOwner) {
						defeatSparkController.setHit(false);
						powerUpSwordHitTrailController.setActive(false);
						return super.onBombCollision(controller, bombCX, bombCY, isPlayerOwner);
					}
				}) {
			
			@Override
			public void update(IObjectController controller) {
				if(state == StalfosState.WALK) {
					if(linkData.getSwordState() == SwordState.CUT &&
							controller.getCurrentState() == State.STANDARD) {
						if(coordinate.calculateDistance(
							controller.getCenterX(), controller.getCenterY(),
							linkData.getSwordCenterX(), linkData.getSwordCenterY()) <= 20) {
							jump = true;
							jumpFrom = coordinate.calculateDelta(
								linkData.getCenterX(), linkData.getCenterY(),
								controller.getCenterX(), controller.getCenterY()
							);
						}
					}
					if(jump == false) {
						float speed = 0.3f;
						updateCounter -= 1;
						animationCounter += 1;
						if(animationCounter >= 16) {
							animationCounter = 0;
						}
						if(updateCounter <= 0) {
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
							updateCounter = rng.getRNG(10, 30);
						}
						if(wallCollision == true) {
							if(direction == Direction.LEFT) {
								direction = Direction.RIGHT;
							}
							else if(direction == Direction.RIGHT) {
								direction = Direction.LEFT;
							}
							else if(direction == Direction.UP) {
								direction = Direction.DOWN;
							}
							else if(direction == Direction.DOWN) {
								direction = Direction.UP;
							}
							wallCollision = false;
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
					}
					else {
						state = StalfosState.JUMP;
						controller.setMoveDelta(jumpFrom.x * 1.5f, jumpFrom.y * 1.5f);
						controller.jump(2.2f);
					}
				}
				else if(state == StalfosState.JUMP) {
					if(controller.getCurrentState() != State.JUMPING) {
						state = StalfosState.WALK;
					}
				}
				jump = false;
			}
		};

		shadow = new Shadow(gfxSystem, new ShadowDependencyEnemy(enemyController.getController()));
		fallAnimation = new FallAnimation(drawComponent, ()-> {
			return enemyController.getController().getCurrentState() == State.FALLING;
		}, -2.5f, -3);
		powerUpSwordHitTrailController = new PowerUpSwordHitTrailController(roomFactory, new PowerUpSwordHitTrailControllerDependency() {
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
		defeatSparkController = new DefeatSparkController(enemyDefeatedPrize, roomFactory, new DefeatSparkControllerDependency() {
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
		powerUpSwordHitTrailController.update();
		recoilIndicatorController.update();
		fallAnimation.update();
	}
	@Override
	public void draw() {
		if(state == StalfosState.WALK) {
			if(animationCounter <= 8) {
				drawComponent.setTexture(482, 0, 15, 16);
				drawComponent.setSize(15, 16);
				drawComponent.setSpriteOffset(-2, -7);
			}
			else {
				drawComponent.setTexture(497, 0, 15, 16);
				drawComponent.setSize(15, 16);
				drawComponent.setSpriteOffset(-2, -7);
			}
		}
		else if(state == StalfosState.JUMP) {
			drawComponent.setTexture(464, 34, 16, 14);
			drawComponent.setSize(16, 14);
			drawComponent.setSpriteOffset(-2.5f, -5);
		}
		recoilIndicatorController.draw();
		drawComponent.setHeight(-enemyController.getController().getHeight());
		drawComponent.setPosition(
				enemyController.getController().getX(),
				enemyController.getController().getY());

		fallAnimation.draw();

		shadow.setVisible(enemyController.getController().getHeight() > 0);
		shadow.draw();
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
