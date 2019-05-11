package com.la.game_objects.enemy;

import com.engine.aspect.IAspectSystem;
import com.engine.component.sword.SwordState;
import com.engine.component.tail.TailComponent;
import com.engine.component.tail.TailComponent.TailComponentDependency;
import com.engine.direction.Direction;
import com.engine.direction.Direction8;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.SpatialSystem;
import com.engine.util.IRNG;
import com.la.aspects.SwordHit.SwordHitResult;
import com.la.factory.IRoomFactory;
import com.la.game_objects.FallAnimation;
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

public class MiniMoldorm implements IGameObject {
	public interface MiniMoldormCallback {
		void onDeath();
	}

	private TextureDrawComponent drawComponent;
	private Burn burn;
	private IEnemyController enemyController;
	private TailComponent tailComponent;
	private FallAnimation fallAnimation;

	private PowerUpSwordHitTrailController trailController;
	private RecoilIndicatorController recoilIndicatorController;
	private DefeatSparkController defeatSparkController;

	private Direction8 direction;
	private int counter = 30;

	private boolean remove;

	public MiniMoldorm(float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IRoomFactory roomFactory,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			IRNG rng,
			IEnemyDefeatedPrize enemyDefeatedPrize,
			MiniMoldormCallback callback) {
		enemyDefeatedPrize.setHeartDropChance(25);
		enemyDefeatedPrize.setRupeeDropChance(50);

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(468, 0, 14, 15);
		drawComponent.setSize(14, 15);
		drawComponent.setSpriteOffset(-2, -2);

		enemyController = new EnemyController2(-1, x, y, 10, 11, 4, true, 0, 2, true, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, new ObjectControllerCallbackStandard() {
			@Override
			public boolean onDeath(IObjectController controller) {
				callback.onDeath();
				return defeatSparkController.onDead();
			}
			@Override
			public SwordHitResult onSwordCollision(IObjectController controller, float swordX, float swordY, float swordW, float swordH, float linkCX, float linkCY, int dmg, boolean powerUped, SwordState state, int swordCounter) {
				tailComponent.reset();
				defeatSparkController.setHit(powerUped);
				trailController.setActive(powerUped);
				return super.onSwordCollision(controller, swordX, swordY, swordW, swordH, linkCX, linkCY, dmg, powerUped, state, counter);
			}
			@Override
			public void onWallCollisionFromLeft(IObjectController controller) {
				super.onWallCollisionFromLeft(controller);
				if(controller.getCurrentState() == State.STANDARD) {
					direction.bounce();
				}
			}
			@Override
			public void onWallCollisionFromRight(IObjectController controller) {
				super.onWallCollisionFromRight(controller);
				if(controller.getCurrentState() == State.STANDARD) {
					direction.bounce();
				}
			}
			@Override
			public void onWallCollisionFromUp(IObjectController controller) {
				super.onWallCollisionFromUp(controller);
				if(controller.getCurrentState() == State.STANDARD) {
					direction.bounce();
				}
			}
			@Override
			public void onWallCollisionFromDown(IObjectController controller) {
				super.onWallCollisionFromDown(controller);
				if(controller.getCurrentState() == State.STANDARD) {
					direction.bounce();
				}
			}
			@Override
			public boolean onMagicPowderCollision(IObjectController controller, float linkCX, float linkCY) {
				burn.setActive(true);
				tailComponent.reset();
				return true;
			}
			@Override
			public boolean onBombCollision(IObjectController controller, float bombCX, float bombCY, boolean isPlayerOwner) {
				defeatSparkController.setHit(false);
				trailController.setActive(false);
				tailComponent.reset();
				return super.onBombCollision(controller, bombCX, bombCY, isPlayerOwner);
			}
			@Override
			public void onLinkShieldCollision(IObjectController controller, float linkCX, float linkCY) {
				super.onLinkShieldCollision(controller, linkCX, linkCY);
				tailComponent.reset();
			}
		}) {
			@Override
			public void update(IObjectController controller) {
				float speed = 0.75f;
				counter -= 1;
				if(counter <= 0) {
					counter = rng.getRNG(10, 20);
					int dir = rng.getRNG(0, 10);
					if(dir <= 7) {
						direction.turnLeft();
					}
					else {
						direction.turnRight();
					}
				}

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

				tailComponent.update();
			}
		};

		IObjectController objectController = enemyController.getController();

		tailComponent = new TailComponent(gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, 18, new TailComponentDependency() {
			@Override
			public float getOwnerCenterX() {
				return objectController.getCenterX();
			}
			@Override
			public float getOwnerCenterY() {
				return objectController.getCenterY();
			}
		});
		tailComponent.addSegment(460, 10,  8,  8, 17);
		tailComponent.addSegment(458,  0, 10, 10, 9);

		direction = new Direction8();

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
		burn = new Burn(gfxSystem,  new BurnDependency() {
			IObjectController objectController = enemyController.getController();

			@Override
			public float getCenterX() {
				return objectController.getCenterX();
			}
			@Override
			public float getBottomY() {
				return objectController.getY() + objectController.getH() + 2;
			}
			@Override
			public float getHeight() {
				return objectController.getHeight();
			}
		});
		recoilIndicatorController = new RecoilIndicatorController(drawComponent, new RecoilIndicatorControllerDependency() {
			@Override
			public boolean isHit() {
				return enemyController.getController().getInvisbilityFrame() > 0;
			}
		});
		fallAnimation = new FallAnimation(drawComponent, ()-> {
			return enemyController.getController().getCurrentState() == State.FALLING;
		}, -2, -2);
	}

	@Override
	public void update() {
		enemyController.updateController();
		trailController.update();
		fallAnimation.update();
		recoilIndicatorController.update();
		burn.update();
	}
	@Override
	public void draw() {
		if(direction.getDirection() == Direction.UP) {
			drawComponent.setRotation(0, 7, 7.5f);
		}
		else if(direction.getDirection() == Direction.LEFT_UP) {
			drawComponent.setRotation(315, 7, 7.5f);
		}
		else if(direction.getDirection() == Direction.LEFT) {
			drawComponent.setRotation(270, 7, 7.5f);
		}
		else if(direction.getDirection() == Direction.LEFT_DOWN) {
			drawComponent.setRotation(235, 7, 7.5f);
		}
		else if(direction.getDirection() == Direction.DOWN) {
			drawComponent.setRotation(180, 7, 7.5f);
		}
		else if(direction.getDirection() == Direction.RIGHT_DOWN) {
			drawComponent.setRotation(135, 7, 7.5f);
		}
		else if(direction.getDirection() == Direction.RIGHT) {
			drawComponent.setRotation(90, 7, 7.5f);
		}
		else if(direction.getDirection() == Direction.RIGHT_UP) {
			drawComponent.setRotation(45, 7, 7.5f);
		}

		drawComponent.setPosition(
			enemyController.getController().getX(),
			enemyController.getController().getY());
		recoilIndicatorController.draw();

		fallAnimation.draw();
		tailComponent.draw();
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
		tailComponent.remove();
		burn.remove();
	}
}
