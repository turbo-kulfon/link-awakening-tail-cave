package com.la.game_objects.enemy;

import com.engine.aspect.IAspectSystem;
import com.engine.component.sword.SwordState;
import com.engine.direction.Direction;
import com.engine.direction.Direction8;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.SpatialSystem;
import com.engine.util.Coordinate;
import com.engine.util.ICoordinate;
import com.engine.util.IRNG;
import com.la.aspects.SwordHit.SwordHitResult;
import com.la.aspects.SwordHit.SwordHitResultType;
import com.la.factory.IRoomFactory;
import com.la.game_objects.effect.Burn;
import com.la.game_objects.effect.Burn.BurnDependency;
import com.la.game_objects.enemy.DefeatSparkController.DefeatSparkControllerDependency;
import com.la.game_objects.enemy.PowerUpSwordHitTrailController.PowerUpSwordHitTrailControllerDependency;
import com.la.game_objects.enemy.RecoilIndicatorController.RecoilIndicatorControllerDependency;
import com.la.game_objects.enemy.update.EnemyController2;
import com.la.game_objects.enemy.update.IEnemyController;
import com.la.game_objects.enemy.update.IObjectController;
import com.la.game_objects.enemy.update.IObjectController.State;
import com.la.game_objects.enemy.update.ObjectControllerCallbackStandard;
import com.la.game_objects.link.ILinkData;

public class Keese implements IGameObject {
	public interface KeeseCallback {
		void onDeath();
	}

	private enum KeeseState {
		IDLE,
		FLYING
	}

	private TextureDrawComponent drawComponent;
	private Burn burn;
	private IEnemyController enemyController;

	private PowerUpSwordHitTrailController trailController;
	private RecoilIndicatorController recoilIndicatorController;
	private DefeatSparkController defeatSparkController;

	private KeeseState state = KeeseState.IDLE;
	private Direction8 direction;
	private int animationCounter, counter;
	private boolean leftMove;
	private boolean remove;

	public Keese(float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			ILinkData linkData,
			IRNG rng,
			IEnemyDefeatedPrize prize,
			IRoomFactory roomFactory,
			KeeseCallback callback) {
		ICoordinate coordinate = new Coordinate();

		prize.setHeartDropChance(25);
		prize.setRupeeDropChance(50);

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(504, 16, 8, 10);
		drawComponent.setSize(8, 10);
		drawComponent.setSpriteOffset(0, 0);

		enemyController = new EnemyController2(
				-1,
				x, y, 8, 10,
				1,
				true, 0, 2, true,
				soundSystem, spatialSystem, uniqueIDManager, aspectSystem,
				new ObjectControllerCallbackStandard() {
					@Override
					public boolean onDeath(IObjectController controller) {
						boolean poweredUp = defeatSparkController.onDead();
						callback.onDeath();
						return poweredUp;
					}
					@Override
					public SwordHitResult onSwordCollision(IObjectController controller, float swordX, float swordY, float swordW, float swordH, float linkCX, float linkCY, int dmg, boolean powerUped, SwordState state, int swordCounter) {
						if(powerUped == false) {
							controller.recoil(linkCX, linkCY, 30, 10);
						}
						else {
							controller.recoil(linkCX, linkCY, 30 * 100, 10 * 100);
						}
						controller.stop();
						controller.doDmg(dmg);
						defeatSparkController.setHit(powerUped);
						trailController.setActive(powerUped);
						return new SwordHitResult(SwordHitResultType.HIT, controller.getCenterX(), controller.getCenterY());
					}
					@Override
					public boolean onMagicPowderCollision(IObjectController controller, float linkCX, float linkCY) {
						burn.setActive(true);
						return true;
					}
					@Override
					public boolean onBombCollision(IObjectController controller, float bombCX, float bombCY, boolean isPlayerOwner) {
						defeatSparkController.setHit(false);
						trailController.setActive(false);
						return super.onBombCollision(controller, bombCX, bombCY, isPlayerOwner);
					}
				}) {
			
			@Override
			public void update(IObjectController controller) {
				if(state == KeeseState.IDLE) {
					controller.stop();
					counter -= 1;
					if(counter < 0) {
						counter = 0;
						if(coordinate.calculateDistance(
								controller.getCenterX(), controller.getCenterY(),
								linkData.getCenterX(), linkData.getCenterY()) <= 32) {
							state = KeeseState.FLYING;
							counter = rng.getRNG(60, 90);
							float dx = controller.getCenterX() - linkData.getCenterX();
							float dy = controller.getCenterY() - linkData.getCenterY();
							if(Math.abs(dx) > Math.abs(dy)) {
								if(dx >= 0) {
									direction.setDirection(Direction.LEFT);
									if(dy >= 0) {
										leftMove = false;
									}
									else {
										leftMove = true;
									}
								}
								else {
									direction.setDirection(Direction.RIGHT);
									if(dy >= 0) {
										leftMove = true;
									}
									else {
										leftMove = false;
									}
								}
							}
							else {
								if(dy >= 0) {
									direction.setDirection(Direction.UP);
									if(dx >= 0) {
										leftMove = true;
									}
									else {
										leftMove = false;
									}
								}
								else {
									direction.setDirection(Direction.DOWN);
									if(dx >= 0) {
										leftMove = false;
									}
									else {
										leftMove = true;
									}
								}
							}
						}
					}
				}
				else if(state == KeeseState.FLYING) {
					animationCounter += 1;
					if(animationCounter > 14) {
						animationCounter = 0;
					}
					counter -= 1;
					if(counter > 0) {
						if(counter % 25 == 0) {
							if(leftMove == true) {
								direction.turnLeft();
							}
							else {
								direction.turnRight();
							}
						}
						float speed = 0.75f;
						if(direction.getDirection() == Direction.UP) {
							controller.setMoveDelta(0, -speed);
						}
						else if(direction.getDirection() == Direction.LEFT_UP) {
							controller.setMoveDelta(-speed, -speed);
						}
						else if(direction.getDirection() == Direction.LEFT) {
							controller.setMoveDelta(-speed, 0);
						}
						else if(direction.getDirection() == Direction.LEFT_DOWN) {
							controller.setMoveDelta(-speed, speed);
						}
						else if(direction.getDirection() == Direction.DOWN) {
							controller.setMoveDelta(0, speed);
						}
						else if(direction.getDirection() == Direction.RIGHT_DOWN) {
							controller.setMoveDelta(speed, speed);
						}
						else if(direction.getDirection() == Direction.RIGHT) {
							controller.setMoveDelta(speed, 0);
						}
						else if(direction.getDirection() == Direction.RIGHT_UP) {
							controller.setMoveDelta(speed, -speed);
						}
					}
					else {
						state = KeeseState.IDLE;
						counter = 30;
					}
				}
			}
		};
		enemyController.getController().setFlying(true);

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

		direction = new Direction8();

		burn = new Burn(gfxSystem,  new BurnDependency() {
			IObjectController objectController = enemyController.getController();

			@Override
			public float getCenterX() {
				return objectController.getCenterX();
			}
			@Override
			public float getBottomY() {
				return objectController.getY() + objectController.getH();
			}
			@Override
			public float getHeight() {
				return objectController.getHeight();
			}
		});
	}

	@Override
	public void update() {
		enemyController.updateController();
		trailController.update();
		recoilIndicatorController.update();
		burn.update();
	}
	@Override
	public void draw() {
		if(state == KeeseState.IDLE) {
			drawComponent.setTexture(504, 16, 8, 10);
			drawComponent.setSize(8, 10);
			drawComponent.setSpriteOffset(0, 0);
		}
		else if(state == KeeseState.FLYING) {
			if(animationCounter <= 7) {
				drawComponent.setTexture(488, 16, 16, 10);
				drawComponent.setSize(16, 10);
				drawComponent.setSpriteOffset(-4, 0);
			}
			else {
				drawComponent.setTexture(504, 16, 8, 10);
				drawComponent.setSize(8, 10);
				drawComponent.setSpriteOffset(0, 0);
			}
		}

		drawComponent.setPosition(
			enemyController.getController().getX(),
			enemyController.getController().getY());

		recoilIndicatorController.draw();
		burn.draw();
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
		burn.remove();
	}
}
