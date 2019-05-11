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
import com.la.aspects.SwordHit.SwordHitResultType;
import com.la.factory.IRoomFactory;
import com.la.game_objects.FallAnimation;
import com.la.game_objects.enemy.DefeatSparkController.DefeatSparkControllerDependency;
import com.la.game_objects.enemy.PowerUpSwordHitTrailController.PowerUpSwordHitTrailControllerDependency;
import com.la.game_objects.enemy.update.EnemyController2;
import com.la.game_objects.enemy.update.IEnemyController;
import com.la.game_objects.enemy.update.IObjectController;
import com.la.game_objects.enemy.update.ObjectControllerCallbackStandard;
import com.la.game_objects.enemy.update.IObjectController.State;
import com.la.game_objects.link.ILinkData;

public class HardhatBeetle implements IGameObject {
	public interface HardhatBeetleCallback {
		void onDeath();
	}

	private TextureDrawComponent drawComponent;
	private FallAnimation fallAnimation;
	private IEnemyController enemyController;
	private PowerUpSwordHitTrailController trailController;
	private DefeatSparkController defeatSparkController;

	private int animationCounter;
	private boolean remove;

	public HardhatBeetle(
			float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			IRoomFactory roomFactory,
			ILinkData linkData,
			IEnemyDefeatedPrize prize,
			HardhatBeetleCallback callback) {
		prize.setRupeeDropChance(50);
		prize.setHeartDropChance(25);

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(480, 26, 16, 16);
		drawComponent.setSize(16, 16);
		drawComponent.setSpriteOffset(-2, -6);

		ICoordinate coordinate = new Coordinate();
		enemyController = new EnemyController2(
				-1,
				x, y, 12, 10,
				1,
				true, 0, 4, true,
				soundSystem, spatialSystem, uniqueIDManager, aspectSystem,
				new ObjectControllerCallbackStandard() {
			@Override
			public boolean onDeath(IObjectController controller) {
				callback.onDeath();
				return defeatSparkController.onDead();
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
				trailController.setActive(powerUped);
				return new SwordHitResult(SwordHitResultType.HIT, controller.getCenterX(), controller.getCenterY());
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
				Vector delta = coordinate.calculateDelta(
						controller.getCenterX(), controller.getCenterY(),
						linkData.getCenterX(), linkData.getCenterY());
				controller.setMoveDelta(delta.x * 0.5f, delta.y * 0.5f);
				animationCounter += 1;
				if(animationCounter >= 15) {
					animationCounter = 0;
				}
			}
		};

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
		fallAnimation.update();
		trailController.update();
	}

	@Override
	public void draw() {
		if(animationCounter <= 7) {
			drawComponent.setTexture(480, 26, 16, 16);
		}
		else {
			drawComponent.setTexture(496, 26, 16, 16);
		}
		drawComponent.setPosition(
			enemyController.getController().getX(),
			enemyController.getController().getY());

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
	}
}
