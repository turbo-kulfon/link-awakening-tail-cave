package com.la.game_objects.enemy;

import com.engine.aspect.AspectType;
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
import com.engine.util.IRNG;
import com.la.aspects.BlockItemUsage;
import com.la.aspects.SlowDown;
import com.la.aspects.SwordHit.SwordHitResult;
import com.la.aspects.SwordHit.SwordHitResultType;
import com.la.factory.IRoomFactory;
import com.la.game_objects.FallAnimation;
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

public class GelRed implements IGameObject {
	public interface GelRedBeetleCallback {
		void onDeath();
	}

	private enum GelRedState {
		WALK,
		JUMP_PREPARE,
		JUMP,
		GLUE
	}

	private TextureDrawComponent drawComponent;
	private Shadow shadow;
	private IEnemyController enemyController;
	private RecoilIndicatorController recoilIndicatorController;
	private DefeatSparkController defeatSparkController;
	private PowerUpSwordHitTrailController trailController;
	private FallAnimation fallAnimation;

	private GelRedState state = GelRedState.WALK;

	private Burn burn;
	private int animationCounter, gluedUniqueID;
	private boolean remove, jumpOff;

	public GelRed(
			float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			IRoomFactory roomFactory,
			ILinkData linkData,
			IEnemyDefeatedPrize prize,
			GelRedBeetleCallback callback, 
			IRNG rng) {
		prize.setRupeeDropChance(50);
		prize.setHeartDropChance(25);

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(498, 58, 7, 7);
		drawComponent.setSize(7, 7);

		ICoordinate coordinate = new Coordinate();
		enemyController = new EnemyController2(
				-1,
				x, y, 7, 7,
				1,
				true, 0, 0, false,
				soundSystem, spatialSystem, uniqueIDManager, aspectSystem,
				new ObjectControllerCallbackStandard() {
			@Override
			public boolean onDeath(IObjectController controller) {
				boolean poweredUp = defeatSparkController.onDead();
				callback.onDeath();
				return poweredUp;
			}
			@Override
			public void onLinkCollision(int uniqueID) {
				if(jumpOff == false && state != GelRedState.GLUE) {
					state = GelRedState.GLUE;
					animationCounter = 60;
					gluedUniqueID = uniqueID;
				}
			}
			@Override
			public void onLinkShieldCollision(IObjectController controller, float linkCX, float linkCY) {}
			@Override
			public SwordHitResult onSwordCollision(IObjectController controller, float swordX, float swordY, float swordW, float swordH, float linkCX, float linkCY, int dmg, boolean powerUped, SwordState state, int swordCounter) {
				if(powerUped == false) {
					controller.recoil(linkCX, linkCY, 30, 10);
				}
				defeatSparkController.setHit(powerUped);
				trailController.setActive(powerUped);
				
				controller.stop();
				controller.doDmg(dmg);
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
			@Override
			public void onBurn(IObjectController controller) {
				
			}
		}) {
			@Override
			public void update(IObjectController controller) {
				if(state == GelRedState.WALK) {
					jumpOff = false;
					animationCounter += 1;
					controller.stop();
					if(animationCounter >= 16) {
						animationCounter = 0;
						Vector delta = coordinate.calculateDelta(
								controller.getCenterX(), controller.getCenterY(),
								linkData.getCenterX(), linkData.getCenterY());
						controller.setMoveDelta(delta.x * 1f, delta.y * 1f);
						int value = rng.getRNG(0, 100);
						if(value <= 5) {
							state = GelRedState.JUMP_PREPARE;
							animationCounter = 60;
						}
					}
//					if(coordinate.calculateDistance(
//							controller.getCenterX(), controller.getCenterY(),
//							linkData.getCenterX(), linkData.getCenterY()) <= 32) {
//						
//					}
				}
				else if(state == GelRedState.JUMP_PREPARE) {
					controller.stop();
					animationCounter -= 1;
					if(animationCounter <= 0) {
						Vector delta = coordinate.calculateDelta(
								controller.getCenterX(), controller.getCenterY(),
								linkData.getCenterX(), linkData.getCenterY());
						controller.setMoveDelta(delta.x * 1f, delta.y * 1f);
						controller.jump(1.6f);
						state = GelRedState.JUMP;
					}
				}
				else if(state == GelRedState.JUMP) {
					if(controller.getCurrentState() != State.JUMPING) {
						state = GelRedState.WALK;
						animationCounter = 0;
					}
				}
				else if(state == GelRedState.GLUE) {
					animationCounter -= 1;
					if(animationCounter <= 0) {
						Vector delta = coordinate.calculateDelta(
								controller.getCenterX(), controller.getCenterY(),
								controller.getCenterX() - 10, controller.getCenterY() - 10);
						controller.setMoveDelta(delta.x * 1f, delta.y * 1f);
						controller.jump(1.6f);
						state = GelRedState.JUMP;
						jumpOff = true;
						controller.setSpatialComponentActive(true);
					}
					else {
						controller.setSpatialComponentActive(false);
						controller.stop();
						controller.setX(linkData.getX() + 5 - ((animationCounter % 30) / 5));
						controller.setY(linkData.getY() + ((animationCounter % 30) / 5));

						SlowDown slowDown = aspectSystem.getAspect(gluedUniqueID, AspectType.SLOW_DOWN);
						if(slowDown != null) {
							slowDown.slowDown();
						}
						BlockItemUsage blockItemUsage = aspectSystem.getAspect(gluedUniqueID, AspectType.BLOCK_ITEM_USAGE);
						if(blockItemUsage != null) {
							blockItemUsage.block();
						}
					}
				}
			}
		};

		shadow = new Shadow(gfxSystem, new ShadowDependencyEnemy(enemyController.getController()));
		fallAnimation = new FallAnimation(drawComponent, ()-> {
			return enemyController.getController().getCurrentState() == State.FALLING;
		}, 1.5f, 1.5f);
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
		burn = new Burn(gfxSystem, new BurnDependency() {
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
	}

	@Override
	public void update() {
		enemyController.updateController();
		fallAnimation.update();
		trailController.update();
		recoilIndicatorController.update();
		burn.update();
	}

	@Override
	public void draw() {
		if(state == GelRedState.WALK) {
			if(animationCounter <= 8) {
				drawComponent.setTexture(498, 58, 7, 7);
			}
			else {
				drawComponent.setTexture(505, 58, 7, 7);
			}
			drawComponent.setSpriteOffsetX(0);
		}
		else if(state == GelRedState.JUMP_PREPARE) {
			drawComponent.setTexture(498, 58, 7, 7);
			drawComponent.setSpriteOffsetX(animationCounter % 3 == 0 ? 0 : 1);
		}
		recoilIndicatorController.draw();
		drawComponent.setHeight(-enemyController.getController().getHeight());
		drawComponent.setPosition(
			enemyController.getController().getX(),
			enemyController.getController().getY());

		fallAnimation.draw();

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
}
