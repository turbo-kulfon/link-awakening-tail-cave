package com.engine.component.carry;

import java.util.List;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.BombTag;
import com.la.factory.IRoomFactory;

public class BombComponent implements IBombComponent {
	private ICarryComponent carryComponent;
	private ISpatialComponent spatialComponent;
	private IAspectSystem aspectSystem;
	private SpatialSystem spatialSystem;
	private IRoomFactory roomFactory;

	private boolean buttonPressed, pressBlock;

	public BombComponent(
			ICarryComponent carryComponent,
			ISpatialComponent spatialComponent,
			IAspectSystem aspectSystem,
			SpatialSystem spatialSystem,
			IRoomFactory roomFactory) {
		this.carryComponent = carryComponent;
		this.spatialComponent = spatialComponent;
		this.aspectSystem = aspectSystem;
		this.spatialSystem = spatialSystem;
		this.roomFactory = roomFactory;
	}

	@Override
	public void buttonPressed() {
		buttonPressed = true;
	}

	@Override
	public void update() {
		if(buttonPressed == true) {
			if(pressBlock == false) {
				if(carryComponent.isActive() == false) {
					int bombSize = aspectSystem.getAspectSize(AspectType.BOMB_TAG);
					if(bombSize == 0) {
						roomFactory.createBomb((int)spatialComponent.getCenterX(), (int)spatialComponent.getY() + (int)spatialComponent.getH(), bombSize);
					}
					else {
						List<Integer> collisions = spatialSystem.getCollided(
							spatialComponent.getX(), spatialComponent.getY(),
							spatialComponent.getW(), spatialComponent.getH());
						for (Integer id : collisions) {
							BombTag bombTag = aspectSystem.getAspect(id, AspectType.BOMB_TAG);
							if(bombTag != null) {
								carryComponent.take(id);
								break;
							}
						}
					}
				}
				pressBlock = true;
			}
			buttonPressed = false;
		}
		else {
			pressBlock = false;
		}
	}
}
