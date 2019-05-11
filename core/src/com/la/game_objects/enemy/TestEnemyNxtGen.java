package com.la.game_objects.enemy;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.SpatialSystem;
import com.engine.util.Coordinate;
import com.engine.util.ICoordinate;
import com.engine.util.ICoordinate.Vector;
import com.la.game_objects.enemy.update.EnemyController2;
import com.la.game_objects.enemy.update.IEnemyController;
import com.la.game_objects.enemy.update.IObjectController;
import com.la.game_objects.enemy.update.ObjectControllerCallbackStandard;
import com.la.game_objects.link.ILinkData;

public class TestEnemyNxtGen implements IGameObject {
	public interface TestEnemyNxtGenCallback {
		void onDeath();
	}

	private TextureDrawComponent drawComponent;
	private IEnemyController enemyController;

	private ICoordinate coordinate;

	private TestEnemyNxtGenCallback callback;
	private boolean remove;

	public TestEnemyNxtGen(
			float x, float y,
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			ILinkData linkData,
			IEnemyDefeatedPrize prize,
			TestEnemyNxtGenCallback callback) {
		coordinate = new Coordinate();

		prize.setRupeeDropChance(50);
		prize.setHeartDropChance(25);

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(480, 26, 16, 16);
		drawComponent.setSize(16, 16);
		drawComponent.setSpriteOffset(-2, -6);

		enemyController = new EnemyController2(-1, x, y, 12, 10, 1, true, 0, 2, true, null, spatialSystem, uniqueIDManager, aspectSystem, new ObjectControllerCallbackStandard() {
			@Override
			public boolean onDeath(IObjectController controller) {
				prize.createPickup((int)controller.getCenterX(), (int)controller.getCenterY());
				callback.onDeath();
				return false;
			}
		}) {
			@Override
			public void update(IObjectController controller) {
				Vector delta = coordinate.calculateDelta(
						controller.getCenterX(), controller.getCenterY(),
						linkData.getCenterX(), linkData.getCenterY());
				controller.setMoveDelta(delta.x * 0.5f, delta.y * 0.5f);
			}
		};
	}

	@Override
	public void update() {
		enemyController.updateController();
	}

	@Override
	public void draw() {
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
