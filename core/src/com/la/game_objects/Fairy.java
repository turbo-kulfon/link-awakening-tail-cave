package com.la.game_objects;

import com.engine.aspect.IAspectSystem;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.IOutsideViewCheck;
import com.engine.spatial.IOutsideViewCheck.OutsideViewCheckCallback;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.OutsideViewCheck;
import com.engine.spatial.OutsideViewCheckPosition;
import com.engine.spatial.SpatialSystem;
import com.engine.util.CollisionDetection;
import com.engine.util.Coordinate;
import com.engine.util.ICollisionDetection;
import com.engine.util.ICoordinate;
import com.engine.util.ICoordinate.Vector;
import com.engine.util.IRNG;
import com.la.aspects.LinkCollision;
import com.la.aspects.RoomTransition;
import com.la.equipment.IEquipmentSystem;
import com.la.game_objects.Shadow.ShadowDependency;

public class Fairy implements IGameObject {
	private TextureDrawComponent drawComponent;
	private Shadow shadow;
	private ISpatialComponent spatialComponent;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;
	
	private IRNG rng;
	private ICoordinate coordinate;
	private IOutsideViewCheck outsideViewCheck;

	private int uniqueID;
	private boolean remove;
	private int angle, counter, disappear = 360;
	private float speed, height;
	private boolean flyUp = true;

	public Fairy(float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			IEquipmentSystem equipmentSystem,
			IRNG rng) {
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;
		this.rng = rng; 

		uniqueID = uniqueIDManager.getUniqueID();

		coordinate = new Coordinate();

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(132, 91, 8, 13);
		drawComponent.setSize(8, 13);

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x - 4, y - 7, 8, 13);
		shadow = new Shadow(gfxSystem, new ShadowDependency() {
			@Override
			public float getY() {
				return spatialComponent.getY();
			}
			@Override
			public float getH() {
				return spatialComponent.getH();
			}
			@Override
			public float getCenterX() {
				return spatialComponent.getCenterX();
			}
		});

		outsideViewCheck = new OutsideViewCheck(new OutsideViewCheckPosition(spatialComponent), 
			new OutsideViewCheckCallback() {
				@Override
				public void outsideLeft() {
					spatialComponent.setX(0);
					spatialComponent.setDeltaX(0);
				}
				@Override
				public void outsideRight() {
					spatialComponent.setX(160 - spatialComponent.getW());
					spatialComponent.setDeltaX(0);
				}
				@Override
				public void outsideUp() {
					spatialComponent.setY(0);
					spatialComponent.setDeltaY(0);
				}
				@Override
				public void outsideDown() {
					spatialComponent.setY(128 - spatialComponent.getH());
					spatialComponent.setDeltaY(0);
				}
			}
		);

		ICollisionDetection collisionDetection = new CollisionDetection();

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		aspectSystem.addAspect(new LinkCollision(uniqueID) {
			@Override
			public void collision(float linkX, float linkY, float linkW, float linkH, float linkHeight, boolean blocked) {
				if(remove == false && disappear <= 330 && collisionDetection.collisionDetect(
						linkX, linkY, linkW, linkH,
						spatialComponent.getX(), spatialComponent.getY() - height/2.0f,
						spatialComponent.getW(), spatialComponent.getH()) == true) {
					equipmentSystem.getFairy();
					setToRemove();
					soundSystem.takeKey();
				}
			}
		});
	}

	@Override
	public void update() {
		if(flyUp == true) {
			if(height < 11) {
				height += 0.3f;
			}
			else if(height < 14) {
				height += 0.1f;
			}
			else {
				flyUp = false;
			}
		}
		else {
			if(height > 9) {
				height -= 0.1f;
			}
			else {
				flyUp = true;
			}
		}
		if(counter > 0) {
			counter -= 1;
			Vector d = coordinate.angleToDelta(angle);
			spatialComponent.setDelta(d.x * speed, d.y * speed);
		}
		else {
			angle = rng.getRNG(0, 359);
			speed = rng.getRNG(3, 6)/10.0f;
			counter = rng.getRNG(30, 60);
		}
		disappear -= 1;
		if(disappear <= 0) {
			setToRemove();
		}

		outsideViewCheck.update();
	}
	@Override
	public void draw() {
		if(disappear <= 60) {
			drawComponent.setVisible(disappear % 8 < 4);
		}
		if(spatialComponent.getDeltaX() > 0) {
			drawComponent.setFlipX(false);
		}
		else {
			drawComponent.setFlipX(true);
		}
		drawComponent.setHeight(-height);
		drawComponent.setPosition(
			spatialComponent.getX(),
			spatialComponent.getY());

		shadow.setVisible(true);
		shadow.draw();
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
		shadow.remove();
		spatialComponent.remove();
		uniqueIDManager.returnID(uniqueID);
		aspectSystem.removeAspects(uniqueID);
	}
}
