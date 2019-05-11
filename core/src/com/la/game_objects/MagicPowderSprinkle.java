package com.la.game_objects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.engine.util.ICoordinate.Vector;
import com.la.aspects.MagicPowderCollision;

public class MagicPowderSprinkle implements IGameObject {
	public interface MagicPowderDependency {
		float getOwnerCenterX();
		float getOwnerCenterY();
	}

	private TextureDrawComponent sprinkles[] = new TextureDrawComponent[3];
	private Vector sprinklePosition[] = new Vector[3];
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private boolean remove;
	private int uniqueID;

	private int counter = 15;

	public MagicPowderSprinkle(float x, float y,
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			MagicPowderDependency dependency) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;

		uniqueID = uniqueIDManager.getUniqueID();

		sprinkles[0] = gfxSystem.createTextureDrawComponent(1);
		sprinkles[0].setTexture(114, 76, 4, 4);
		sprinkles[0].setSize(4, 4);
		sprinkles[1] = gfxSystem.createTextureDrawComponent(1);
		sprinkles[1].setTexture(114, 76, 4, 4);
		sprinkles[1].setSize(4, 4);
		sprinkles[2] = gfxSystem.createTextureDrawComponent(1);
		sprinkles[2].setTexture(114, 76, 4, 4);
		sprinkles[2].setSize(4, 4);

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x - 6, y - 3.5f, 12, 7);
		spatialComponent.setCollisionResponse((collidedID)-> {
			MagicPowderCollision magicPowderCollision = aspectSystem.getAspect(collidedID, AspectType.MAGIC_POWDER_HIT);
			if(magicPowderCollision != null) {
				magicPowderCollision.collision(
					spatialComponent.getX(), spatialComponent.getY(),
					spatialComponent.getW(), spatialComponent.getH(),
					dependency.getOwnerCenterX(), dependency.getOwnerCenterY());
			}
		});

		sprinklePosition[0] = new Vector(spatialComponent.getCenterX()-2, spatialComponent.getY() + 2);
		sprinklePosition[1] = new Vector(spatialComponent.getCenterX(), spatialComponent.getY());
		sprinklePosition[2] = new Vector(spatialComponent.getCenterX()+1, spatialComponent.getY() + 1);
	}

	@Override
	public void update() {
		if(counter > 0) {
			counter -= 1;
			sprinklePosition[0].x -= 0.1f;
			sprinklePosition[0].y += 0.5f;

			sprinklePosition[1].x += 0.2f;
			sprinklePosition[1].y += 0.5f;

			sprinklePosition[2].x += 0.4f;
			sprinklePosition[2].y += 0.5f;
		}
		else {
			setToRemove();
		}
	}
	@Override
	public void draw() {
		for(int i = 0; i < 3; ++i) {
			if(counter < 5) {
				sprinkles[i].setAlpha((float)counter/5.0f);
			}
			sprinkles[i].setPosition(
				sprinklePosition[i].x - 2,
				sprinklePosition[i].y - 2);
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
		for (TextureDrawComponent sprinkle : sprinkles) {
			sprinkle.remove();
		}
		spatialComponent.remove();
		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}
}
