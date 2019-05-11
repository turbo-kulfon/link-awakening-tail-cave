package com.la.game_objects.effect;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.RoomTransition;

public class EnemyDefeatedSmallSpark implements IGameObject {
	public interface EnemyDefeatedSmallSparkCallback {
		void onEnd();
	}

	private TextureDrawComponent drawComponent1, drawComponent2;
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private int counter;
	private EnemyDefeatedSmallSparkCallback callback;

	private int uniqueID;
	private boolean remove;

	public EnemyDefeatedSmallSpark(
			float x, float y,
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			EnemyDefeatedSmallSparkCallback callback) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.callback = callback;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent1 = gfxSystem.createTextureDrawComponent(1);
		drawComponent1.setSize(16, 16);
		drawComponent2 = gfxSystem.createTextureDrawComponent(1);
		drawComponent2.setSize(16, 16);

		spatialComponent = spatialSystem.createFreeStaticComponent(uniqueID);
		spatialComponent.setPosition(x - 8, y - 8);
		spatialComponent.setSize(16, 16);

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
	}

	@Override
	public void update() {
		counter += 1;
		if(counter >= 28) {
			setToRemove();
			callback.onEnd();
		}
	}
	@Override
	public void draw() {
		if(counter >= 0 && counter < 7) {
			drawComponent1.setTexture(190, 48, 16, 16);
			drawComponent2.setTexture(190, 48, 16, 16);

			drawComponent1.setInvert(counter <= 3, 1, 0.69f, 0.19f);
			drawComponent2.setInvert(counter <= 3, 1, 0.69f, 0.19f);

			drawComponent1.setPosition(spatialComponent.getCenterX() - 4 - 8, spatialComponent.getCenterY() + 4 - 8);
			drawComponent2.setPosition(spatialComponent.getCenterX() + 4 - 8, spatialComponent.getCenterY() - 4 - 8);
		}
		else if(counter >= 7 && counter < 14) {
			drawComponent1.setTexture(158, 48, 16, 16);
			drawComponent2.setTexture(158, 48, 16, 16);

			drawComponent1.setInvert(counter <= 10, 1, 0.69f, 0.19f);
			drawComponent2.setInvert(counter <= 10, 1, 0.69f, 0.19f);

			drawComponent1.setPosition(spatialComponent.getCenterX() - 6 - 8, spatialComponent.getCenterY() - 6 - 8);
			drawComponent2.setPosition(spatialComponent.getCenterX() + 6 - 8, spatialComponent.getCenterY() + 6 - 8);
		}
		else if(counter >= 14 && counter < 21) {
			drawComponent1.setPosition(spatialComponent.getCenterX() - 8, spatialComponent.getCenterY() - 8);
			drawComponent1.setInvert(counter <= 17, 1, 0.69f, 0.19f);
			drawComponent2.setVisible(false);
		}
		else if(counter >= 21) {
			drawComponent1.setPosition(spatialComponent.getCenterX() - 8, spatialComponent.getCenterY() - 8);
			drawComponent1.setTexture(174, 48, 16, 16);
		}
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
		drawComponent1.remove();
		drawComponent2.remove();
		spatialComponent.remove();

		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}
}
