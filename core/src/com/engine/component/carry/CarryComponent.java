package com.engine.component.carry;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.direction.IDirection;
import com.engine.spatial.ISpatialComponent;
import com.la.aspects.CarriedItem;
import com.la.aspects.CarriedItem.ItemType;
import com.la.equipment.IEquipmentSystem;

public class CarryComponent implements ICarryComponent {
	private ISpatialComponent spatialComponent;
	private IEquipmentSystem equipmentSystem;
	private IAspectSystem aspectSystem;
	private IDirection direction;
	private int carriedUniqueID = -1;
	private boolean blockKurwaButton, buttonPressed;

	public CarryComponent(
			ISpatialComponent spatialComponent,
			IEquipmentSystem equipmentSystem,
			IAspectSystem aspectSystem,
			IDirection direction) {
		this.spatialComponent = spatialComponent;
		this.equipmentSystem = equipmentSystem;
		this.aspectSystem = aspectSystem;
		this.direction = direction;
	}

	@Override
	public boolean pressedBButton() {
		return pressedButton(equipmentSystem.getBItemID());
	}
	@Override
	public boolean pressedAButton() {
		return pressedButton(equipmentSystem.getAItemID());
	}
	@Override
	public void updatePosition() {
		CarriedItem carriedItem = aspectSystem.getAspect(carriedUniqueID, AspectType.CARRIED_ITEM);
		if(carriedItem != null) {
			carriedItem.setPosition(spatialComponent.getCenterX(), spatialComponent.getY());
		}
	}
	@Override
	public void update() {
		if(carriedUniqueID != -1) {
			CarriedItem carriedItem = aspectSystem.getAspect(carriedUniqueID, AspectType.CARRIED_ITEM);
			if(carriedItem != null) {
				carriedItem.setPosition(spatialComponent.getCenterX(), spatialComponent.getY());
			}
			else {
				carriedUniqueID = -1;
			}
		}
		if(buttonPressed == false) {
			blockKurwaButton = false;
		}
		buttonPressed = false;
	}
	private boolean pressedButton(int itemID) {
		if(carriedUniqueID == -1) {
			return true;
		}
		else {
			ItemType carriedItemType = getCarriedItemType();
			if(carriedItemType == ItemType.BOMB) {
				if(equipmentSystem.isItemIDActive(1) == false) {
					toss();
					return true;
				}
				else {
					buttonPressed = true;
					if(itemID == 1 && blockKurwaButton == false) {
						toss();
						return false;
					}
				}
			}
		}
		return false;
	}

	@Override
	public void toss() {
		if(carriedUniqueID != -1) {
			CarriedItem carriedItem = aspectSystem.getAspect(carriedUniqueID, AspectType.CARRIED_ITEM);
			carriedItem.toss(direction.getDirection());
			carriedUniqueID = -1;
		}
	}
	@Override
	public void tossDown() {
		if(carriedUniqueID != -1) {
			CarriedItem carriedItem = aspectSystem.getAspect(carriedUniqueID, AspectType.CARRIED_ITEM);
			carriedItem.tossDown();
			carriedUniqueID = -1;
		}
	}

	@Override
	public void take(int uniqueID) {
		CarriedItem carriedItem = aspectSystem.getAspect(uniqueID, AspectType.CARRIED_ITEM);
		if(carriedItem.take() == true) {
			carriedUniqueID = uniqueID;
			carriedItem.take();
			blockKurwaButton = true;
		}
	}

	@Override
	public void reset() {
		if(carriedUniqueID != -1) {
			CarriedItem carriedItem = aspectSystem.getAspect(carriedUniqueID, AspectType.CARRIED_ITEM);
			carriedItem.removeItem();
			carriedUniqueID = -1;
		}
	}

	@Override
	public boolean isActive() {
		return carriedUniqueID != -1;
	}

	private ItemType getCarriedItemType() {
		CarriedItem carriedItem = aspectSystem.getAspect(carriedUniqueID, AspectType.CARRIED_ITEM);
		return carriedItem.getItemType();
	}
}
