package com.la.game_objects.link.controller.common;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.direction.Direction;
import com.engine.direction.IDirection;
import com.engine.spatial.ISpatialComponent;
import com.la.aspects.CarriedItem;
import com.la.aspects.CarriedItem.ItemType;
import com.la.equipment.IEquipmentSystem;
import com.la.game_objects.link.controller.common.CarryComponent.CarryComponentDependency;

public class CarryComponentDependencyStandard implements CarryComponentDependency {
	private ISpatialComponent spatialComponent;
	private IDirection direction;
	private IEquipmentSystem equipmentSystem;
	private IAspectSystem aspectSystem;

	public CarryComponentDependencyStandard(
			ISpatialComponent spatialComponent,
			IEquipmentSystem equipmentSystem,
			IAspectSystem aspectSystem) {
		this.spatialComponent = spatialComponent;
		this.equipmentSystem = equipmentSystem;
		this.aspectSystem = aspectSystem;
	}

	public void setDirectionComponent(IDirection direction) {
		this.direction = direction;
	}

	@Override
	public boolean setCarriedItemPosition(int uniqueID) {
		CarriedItem carriedItem = aspectSystem.getAspect(uniqueID, AspectType.CARRIED_ITEM);
		if(carriedItem != null) {
			carriedItem.setPosition(spatialComponent.getCenterX(), spatialComponent.getCenterY());
			return true;
		}
		return false;
	}
	@Override
	public boolean setTakingItemPosition(int uniqueID, int frame) {
		CarriedItem carriedItem = aspectSystem.getAspect(uniqueID, AspectType.CARRIED_ITEM);
		if(carriedItem != null) {
			if(direction.getDirection() == Direction.LEFT) {
				if(frame >= 7) {
					carriedItem.setPosition(spatialComponent.getX() - 1, spatialComponent.getY() + spatialComponent.getH() + 10);
				}
				else {
					carriedItem.setPosition(spatialComponent.getX() + 1, spatialComponent.getY() + 7);
				}
			}
			else if(direction.getDirection() == Direction.RIGHT) {
				if(frame >= 7) {
					carriedItem.setPosition(spatialComponent.getX() + spatialComponent.getW() + 1, spatialComponent.getY() + spatialComponent.getH() + 10);
				}
				else {
					carriedItem.setPosition(spatialComponent.getX() + spatialComponent.getW() - 1, spatialComponent.getY() + 7);
				}
			}
			else if(direction.getDirection() == Direction.UP) {
				if(frame >= 7) {
					carriedItem.setPosition(spatialComponent.getCenterX(), spatialComponent.getY() + 9);
				}
				else {
					carriedItem.setPosition(spatialComponent.getCenterX(), spatialComponent.getY() + 5);
				}
			}
			else if(direction.getDirection() == Direction.DOWN) {
				if(frame >= 7) {
					carriedItem.setPosition(spatialComponent.getCenterX(), spatialComponent.getY() + spatialComponent.getH() + 18);
				}
				else {
					carriedItem.setPosition(spatialComponent.getCenterX(), spatialComponent.getY() + spatialComponent.getH() + 13);
				}
			}
			return true;
		}
		return false;
	}
	@Override
	public boolean isItemIDActive(int id) {
		return equipmentSystem.isItemIDActive(id);
	}
	@Override
	public ItemType getCarriedItemType(int uniqueID) {
		CarriedItem carriedItem = aspectSystem.getAspect(uniqueID, AspectType.CARRIED_ITEM);
		if(carriedItem != null) {
			return carriedItem.getItemType();
		}
		return null;
	}

	@Override
	public boolean take(int uniqueID) {
		CarriedItem carriedItem = aspectSystem.getAspect(uniqueID, AspectType.CARRIED_ITEM);
		if(carriedItem != null) {
			return carriedItem.take();
		}
		return false;
	}
	@Override
	public void toss(int uniqueID) {
		CarriedItem carriedItem = aspectSystem.getAspect(uniqueID, AspectType.CARRIED_ITEM);
		if(carriedItem != null) {
			carriedItem.toss(direction.getDirection());
		}
	}
	@Override
	public void tossDown(int uniqueID) {
		CarriedItem carriedItem = aspectSystem.getAspect(uniqueID, AspectType.CARRIED_ITEM);
		if(carriedItem != null) {
			carriedItem.tossDown();
		}
	}
	@Override
	public void removeItem(int uniqueID) {
		CarriedItem carriedItem = aspectSystem.getAspect(uniqueID, AspectType.CARRIED_ITEM);
		if(carriedItem != null) {
			carriedItem.removeItem();
		}
	}

	@Override
	public int getBButtonItemID() {
		return equipmentSystem.getBItemID();
	}
	@Override
	public int getAButtonItemID() {
		return equipmentSystem.getAItemID();
	}
}
