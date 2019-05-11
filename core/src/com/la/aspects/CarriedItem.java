package com.la.aspects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;
import com.engine.direction.Direction;

public abstract class CarriedItem implements IAspect {
	public enum ItemType {
		BOMB
	}

	private int uniqueID;
	private ItemType type;

	public CarriedItem(int uniqueID, ItemType type) {
		this.uniqueID = uniqueID;
		this.type = type;
	}

	public abstract void setPosition(float x, float y);
	public abstract void toss(Direction direction);
	public abstract void tossDown();
	public abstract void removeItem();
	public abstract boolean take();
	public abstract boolean isCarried();
	public ItemType getItemType() {
		return type;
	}

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.CARRIED_ITEM;
	}
}
