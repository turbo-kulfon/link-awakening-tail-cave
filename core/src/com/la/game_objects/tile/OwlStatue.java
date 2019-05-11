package com.la.game_objects.tile;

import com.engine.aspect.IAspectSystem;
import com.engine.direction.Direction;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.engine.util.CollisionDetection;
import com.engine.util.ICollisionDetection;
import com.la.aspects.LinkInteraction;
import com.la.aspects.RoomTransition;
import com.la.aspects.wall_bounce.WallBounceResponseCallback;
import com.la.equipment.IEquipmentSystem;

public class OwlStatue implements IGameObject {
	public interface OwlStatueDependency {
		void drawText(String text);
	}

	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;
	private ICollisionDetection collisionDetection;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private int uniqueID;
	private boolean remove;

	public OwlStatue(int x, int y, int dungeonID, String owlClue,
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			IEquipmentSystem equipmentSystem,
			OwlStatueDependency dependency) {
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;
		collisionDetection = new CollisionDetection();

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexturePosition(360, 16);
		drawComponent.setTextureSize(16, 16);
		drawComponent.setSize(16, 16);

		spatialComponent = spatialSystem.createStaticComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 16, 16);
		spatialComponent.setCollisionResponse(new WallBounceResponseCallback(aspectSystem, spatialComponent));

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		aspectSystem.addAspect(new LinkInteraction(uniqueID) {
			@Override
			public boolean interact(float linkX, float linkY, float linkW, float linkH, Direction direction) {
				if(direction == Direction.UP) {
					if(collisionDetection.collisionDetect(
							linkX, linkY - 1, linkW, linkH,
							spatialComponent.getX(), spatialComponent.getY(),
							spatialComponent.getW(), spatialComponent.getH()) == true) {
						if(equipmentSystem.isStoneBeakPresent(dungeonID) == true) {
							dependency.drawText(owlClue);
						}
						else {
							dependency.drawText(
									  "This owl statue|"
									+ "is trying to say|"
									+ "something, but|"
									+ "you can't|"
									+ "understand it|"
									+ "because it has|"
									+ "no beak.");
						}
						return true;
					}
				}
				return false;
			}
		});
	}

	@Override
	public void update() {
	}
	@Override
	public void draw() {
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
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
