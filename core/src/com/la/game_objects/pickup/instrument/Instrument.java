package com.la.game_objects.pickup.instrument;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.LinkCollision;
import com.la.aspects.RoomTransition;
import com.la.game_objects.pickup.instrument.InstrumentDrawComponent.InstrumentDrawComponentDependency;

public class Instrument implements IGameObject {
	public interface InstrumentCallback {
		void onTake();
	}

	private InstrumentDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private int uniqueID;
	private boolean remove;

	public Instrument(
			float x, float y,
			int texX, int texY, int texW, int texH,
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			InstrumentCallback callback
			) {
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;

		uniqueID = uniqueIDManager.getUniqueID();

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x - texW/2.0f, y - texH/2.0f, texW, texH);

		drawComponent = new InstrumentDrawComponent(gfxSystem, texX, texY, texW, texH, 1, new InstrumentDrawComponentDependency() {
			@Override
			public float getX() {
				return spatialComponent.getX();
			}
			@Override
			public float getY() {
				return spatialComponent.getY();
			}
		});

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		aspectSystem.addAspect(new LinkCollision(uniqueID) {
			@Override
			public void collision(float linkX, float linkY, float linkW, float linkH, float linkHeight, boolean blocked) {
				if(remove == false && linkHeight <= 0) {
					callback.onTake();
					setToRemove();
				}
			}
		});
	}

	@Override
	public void update() {
		drawComponent.update();
	}
	@Override
	public void draw() {
		drawComponent.draw();
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
		drawComponent.remove();
		spatialComponent.remove();
		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}
}
