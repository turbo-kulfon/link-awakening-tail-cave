package com.la.game_objects.enemy;

import com.engine.aspect.IAspectSystem;
import com.engine.component.sword.SwordState;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.SpatialSystem;
import com.engine.util.IRNG;
import com.la.aspects.SwordHit.SwordHitResult;
import com.la.aspects.SwordHit.SwordHitResultType;
import com.la.factory.IRoomFactory;
import com.la.game_objects.enemy.DefeatSparkController.DefeatSparkControllerDependency;
import com.la.game_objects.enemy.RecoilIndicatorController.RecoilIndicatorControllerDependency;
import com.la.game_objects.enemy.update.EnemyController2;
import com.la.game_objects.enemy.update.IEnemyController;
import com.la.game_objects.enemy.update.IObjectController;
import com.la.game_objects.enemy.update.ObjectControllerCallbackStandard;
import com.la.three_of_a_kind_system.CardSymbol;
import com.la.three_of_a_kind_system.ThreeOfAKindComponent;
import com.la.three_of_a_kind_system.ThreeOfAKindElement.ThreeOfAKindElementDependency;
import com.la.three_of_a_kind_system.ThreeOfAKindSystem;

public class ThreeOfAKind implements IGameObject {
	public interface ThreeOfAKindCallback {
		void onDeath();
	}
	private enum MoveState {
		MOVE,
		STOP
	}

	private TextureDrawComponent drawComponent;
	private IEnemyController enemyController;
	private DefeatSparkController defeatSparkController;
	private RecoilIndicatorController recoilIndicatorController;
	private ThreeOfAKindComponent threeOfAKindComponent;

	private MoveState moveState = MoveState.STOP;
	private int dir;
	private int counter = 30, animationCounter, invisibility;
	private boolean remove;

	public ThreeOfAKind(float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			IRoomFactory roomFactory,
			IRNG rng,
			ThreeOfAKindSystem threeOfAKindSystem,
			IEnemyDefeatedPrize prize,
			ThreeOfAKindCallback callback) {
		prize.setRupeeDropChance(50);
		prize.setHeartDropChance(25);

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(448, 18, 16, 16);
		drawComponent.setSize(16, 16);

		enemyController = new EnemyController2(-1, x, y, 16, 16, 1, true, 0, 2, true, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, new ObjectControllerCallbackStandard() {
			@Override
			public boolean onDeath(IObjectController controller) {
				callback.onDeath();
				return defeatSparkController.onDead();
			}
			@Override
			public SwordHitResult onSwordCollision(IObjectController controller, float swordX, float swordY, float swordW, float swordH, float linkCX, float linkCY, int dmg, boolean powerUped, SwordState state, int swordCounter) {
				threeOfAKindComponent.hit();
				if(invisibility <= 0) {
					if(powerUped == false) {
						soundSystem.enemyHit();
					}
					else {
						soundSystem.enemyHitWithPoweredUpSword();
					}
					invisibility = 10;
				}
				return new SwordHitResult(SwordHitResultType.NONE, 0, 0);
			}
			@Override
			public void onWallCollisionFromLeft(IObjectController controller) {
				super.onWallCollisionFromLeft(controller);
				dir = 0;
			}
			@Override
			public void onWallCollisionFromRight(IObjectController controller) {
				super.onWallCollisionFromRight(controller);
				dir = 1;
			}
			@Override
			public void onWallCollisionFromUp(IObjectController controller) {
				super.onWallCollisionFromUp(controller);
				dir = 2;
			}
			@Override
			public void onWallCollisionFromDown(IObjectController controller) {
				super.onWallCollisionFromDown(controller);
				dir = 3;
			}
			@Override
			public boolean onBombCollision(IObjectController controller, float bombCX, float bombCY, boolean isPlayerOwner) {
				if(invisibility <= 0) {
					threeOfAKindComponent.hit();
					soundSystem.enemyHit();
					invisibility = 10;
				}
				return false;
			}
		}) {
			@Override
			public void update(IObjectController controller) {
				threeOfAKindComponent.update();
				invisibility -= 1;
				if(invisibility < 0) {
					invisibility = 0;
				}
			}
		};
		IObjectController controller = enemyController.getController();

		threeOfAKindComponent = threeOfAKindSystem.createComponent(new ThreeOfAKindElementDependency() {
			float speed = 1f;

			@Override
			public void move() {
				if(moveState == MoveState.MOVE) {
					if(animationCounter <= 16) {
						animationCounter += 1;
					}
					else {
						animationCounter = 0;
					}
					counter -= 1;
					if(counter <= 0) {
						moveState = MoveState.STOP;
						counter = 45;
					}
					if(dir == 0) {
						controller.setMoveDelta(-speed, 0);
					}
					else if(dir == 1) {
						controller.setMoveDelta( speed, 0);
					}
					else if(dir == 2) {
						controller.setMoveDelta(0, -speed);
					}
					else if(dir == 3) {
						controller.setMoveDelta(0,  speed);
					}
				}
				else if(moveState == MoveState.STOP) {
					controller.stop();
					counter -= 1;
					if(counter <= 0) {
						dir = rng.getRNG(0, 3);
						counter = rng.getRNG(30, 45);
						moveState = MoveState.MOVE;
					}
				}
			}
			@Override
			public void onRestore() {
				moveState = MoveState.STOP;
				counter = 45;
				controller.setCollideWithLink(true);
			}
			@Override
			public void stopMove() {
				controller.stop();
				controller.setCollideWithLink(false);
			}
			@Override
			public void onSymbolChange(CardSymbol symbol) {
				if(symbol == CardSymbol.HEART) {
					drawComponent.setTexture(448, 18, 16, 16);
				}
				else if(symbol == CardSymbol.DIAMOND) {
					drawComponent.setTexture(416, 18, 16, 16);
				}
				else if(symbol == CardSymbol.SPADE) {
					drawComponent.setTexture(432, 18, 16, 16);
				}
				else if(symbol == CardSymbol.CLUB) {
					drawComponent.setTexture(464, 18, 16, 16);
				}
			}
			@Override
			public void kill() {
				controller.doDmg(1);
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
		recoilIndicatorController = new RecoilIndicatorController(drawComponent, new RecoilIndicatorControllerDependency() {
			@Override
			public boolean isHit() {
				return invisibility > 0;
			}
		});
	}

	@Override
	public void update() {
		enemyController.updateController();
		recoilIndicatorController.update();
	}

	@Override
	public void draw() {
		if(animationCounter <= 8) {
			drawComponent.setFlipX(false);
		}
		else {
			drawComponent.setFlipX(true);
		}
		recoilIndicatorController.draw();
		drawComponent.setPosition(
			enemyController.getController().getX(),
			enemyController.getController().getY());
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
	}
}
