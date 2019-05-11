package com.la.game_objects.enemy;

import com.engine.aspect.IAspectSystem;
import com.engine.component.sword.SwordState;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.SpatialSystem;
import com.engine.util.Coordinate;
import com.engine.util.ICoordinate;
import com.engine.util.ICoordinate.Vector;
import com.la.aspects.SwordHit.SwordHitResult;
import com.la.factory.IRoomFactory;
import com.la.game_objects.Shadow;
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

public class ZolGreen  implements IGameObject {
	public interface ZolGreenCallback {
		void onDeath();
	}

	private enum ZolGreenState {
		HIDDEN,
		UNCOVERING,
		IDLE,
		JUMP,
		COVERING
	}

	private TextureDrawComponent drawComponent;
	private Burn burn;
	private Shadow shadow;
	private IEnemyController enemyController;

	private PowerUpSwordHitTrailController trailController;
	private RecoilIndicatorController recoilIndicatorController;
	private DefeatSparkController defeatSparkController;

	private ZolGreenState state = ZolGreenState.HIDDEN;
	private int counter, jumpCounter;
	private boolean remove;

	public ZolGreen(
			float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			IRoomFactory roomFactory,
			ILinkData linkData,
			IEnemyDefeatedPrize prize,
			ZolGreenCallback callback) {
		ICoordinate coordinate = new Coordinate();

		prize.setRupeeDropChance(50);
		prize.setHeartDropChance(25);

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(480, 26, 16, 16);
		drawComponent.setSize(16, 16);

		enemyController = new EnemyController2(-1, x, y, 10, 10, 1, true, 0, 2, false, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, new ObjectControllerCallbackStandard() {
			@Override
			public boolean onDeath(IObjectController controller) {
				callback.onDeath();
				return defeatSparkController.onDead();
			}
			@Override
			public SwordHitResult onSwordCollision(IObjectController controller, float swordX, float swordY, float swordW, float swordH, float linkCX, float linkCY, int dmg, boolean powerUped, SwordState state, int swordCounter) {
				defeatSparkController.setHit(powerUped);
				trailController.setActive(powerUped);
				return super.onSwordCollision(controller, swordX, swordY, swordW, swordH, linkCX, linkCY, dmg, powerUped, state, counter);
			}
			@Override
			public void onLinkShieldCollision(IObjectController controller, float linkCX, float linkCY) {}
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
				if(state == ZolGreenState.HIDDEN) {
					controller.setHidden(true);
					if(counter > 0) {
						counter -= 1;
					}
					else {
						if(coordinate.calculateDistance(
								controller.getCenterX(), controller.getCenterY(),
								linkData.getCenterX(), linkData.getCenterY()) <= 32) {
							state = ZolGreenState.UNCOVERING;
							counter = 60;
							controller.setHidden(false);
						}
					}
				}
				else if(state == ZolGreenState.UNCOVERING) {
					if(counter > 0) {
						if(controller.getCurrentState() != State.JUMPING) {
							counter -= 1;
						}
						if(counter == 30) {
							controller.jump(1.5f);
							counter -= 1;
						}						
					}
					else {
						state = ZolGreenState.IDLE;
						counter = 30;
						jumpCounter = 0;
					}
				}
				else if(state == ZolGreenState.IDLE) {
					counter -= 1;
					if(counter <= 0) {
						if(jumpCounter < 3) {
							Vector delta = coordinate.calculateDelta(
									controller.getCenterX(), controller.getCenterY(),
									linkData.getCenterX(), linkData.getCenterY());
							controller.setMoveDelta(delta.x * 0.5f, delta.y * 0.5f);
							controller.jump(2.2f);
							state = ZolGreenState.JUMP;
							jumpCounter += 1;
						}
						else {
							state = ZolGreenState.COVERING;
							controller.stop();
							counter = 60;
						}
					}
				}
				else if(state == ZolGreenState.JUMP) {
					if(controller.getCurrentState() != State.JUMPING) {
						state = ZolGreenState.IDLE;
						controller.stop();
						counter = 30;
					}
				}
				else if(state == ZolGreenState.COVERING) {
					counter -= 1;
					if(counter <= 0) {
						state = ZolGreenState.HIDDEN;
						counter = 60;
					}
				}
				if(state == ZolGreenState.IDLE || state == ZolGreenState.JUMP || (state == ZolGreenState.UNCOVERING && counter <= 30)) {
					controller.setSpatialComponentActive(true);
				}
				else {
					controller.setSpatialComponentActive(false);
				}
			}
		};

		shadow = new Shadow(gfxSystem, new ShadowDependencyEnemy(enemyController.getController()));
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
		if(state == ZolGreenState.HIDDEN) {
			drawComponent.setVisible(false);
		}
		else if(state == ZolGreenState.UNCOVERING) {
			drawComponent.setVisible(true);
			if(counter <= 60 && counter > 45) {
				smallSprite();
			}
			else if(counter <= 45 && counter > 31) {
				jumpSprite();
			}
			else if(counter <= 31 && counter >= 30) {
				bigSprite(0);
			}
			else if(counter < 30) {
				bigSprite(counter % 2);
			}
		}
		else if(state == ZolGreenState.IDLE) {
			bigSprite(0);
		}
		else if(state == ZolGreenState.JUMP) {
			jumpSprite();
		}
		else if(state == ZolGreenState.COVERING) {
			if(counter <= 60 && counter > 40) {
				bigSprite(0);
			}
			else if(counter <= 40 && counter > 20) {
				jumpSprite();
			}
			else if(counter <= 20) {
				smallSprite();
			}
		}
		drawComponent.setHeight(-enemyController.getController().getHeight());
		drawComponent.setPosition(
			enemyController.getController().getX(),
			enemyController.getController().getY());

		recoilIndicatorController.draw();

		shadow.setVisible(enemyController.getController().getHeight() > 0);
		shadow.draw();

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
		shadow.remove();
		burn.remove();
	}

	private void jumpSprite() {
		drawComponent.setTexture(490, 42, 10, 16);
		drawComponent.setSize(10, 16);
		drawComponent.setSpriteOffset(-0, -6);
	}
	private void smallSprite() {
		drawComponent.setTexture(483, 51, 7, 7);
		drawComponent.setSize(7, 7);
		drawComponent.setSpriteOffset(1.5f, 3);
	}
	private void bigSprite(int shakeOffset) {
		drawComponent.setTexture(500, 47, 12, 11);
		drawComponent.setSize(12, 11);
		drawComponent.setSpriteOffset(-1 + shakeOffset, -1);
	}
}
