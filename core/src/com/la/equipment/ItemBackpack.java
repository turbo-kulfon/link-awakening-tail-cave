package com.la.equipment;

public class ItemBackpack implements IItemBackpack {
	private ItemData backpack[] = new ItemData[12];

	private final int BSlot = 10;
	private final int ASlot = 11;

	@Override
	public void addItem(ItemData item) {
		int itemSlot = getSlot(item.itemID);
		if(itemSlot != -1) {
			backpack[itemSlot].quantity += item.quantity;
			return;
		}
		if(backpack[BSlot] == null) {
			backpack[BSlot] = item;
			return;
		}
		if(backpack[ASlot] == null) {
			backpack[ASlot] = item;
			return;
		}
		for(int i = 0; i < 10; ++i) {
			if(backpack[i] == null) {
				backpack[i] = item;
				return;
			}
		}
	}
	@Override
	public boolean removeQuantity(int itemID) {
		for(int i = 0; i < 12; ++i) {
			if(backpack[i] != null) {
				if(backpack[i].itemID == itemID) {
					if(backpack[i].quantity > 0) {
						backpack[i].quantity -= 1;
						return true;
					}
				}
			}
		}
		return false;
	}
	@Override
	public boolean removeQuantityAndItemWhenZero(int itemID) {
		for(int i = 0; i < 12; ++i) {
			if(backpack[i] != null) {
				if(backpack[i].itemID == itemID) {
					if(backpack[i].quantity > 0) {
						backpack[i].quantity -= 1;
						if(backpack[i].quantity == 0) {
							backpack[i].itemID = -1;
						}
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public int getItemID(int slot) {
		ItemData itemData = backpack[slot];
		if(itemData != null) {
			return itemData.itemID;
		}
		return -1;
	}
	@Override
	public int getQuantity(int slot) {
		ItemData itemData = backpack[slot];
		if(itemData != null) {
			return itemData.quantity;
		}
		return 0;
	}

	@Override
	public int getBItemID() {
		return getItemID(BSlot);
	}
	@Override
	public int getBQuantity() {
		return getQuantity(BSlot);
	}
	@Override
	public int getAItemID() {
		return getItemID(ASlot);
	}
	@Override
	public int getAQuantity() {
		return getQuantity(ASlot);
	}

	@Override
	public void BItemSwitch(int slot) {
		switchItems(BSlot, slot);
	}
	@Override
	public void AItemSwitch(int slot) {
		switchItems(ASlot, slot);
	}

	private void switchItems(int src, int dest) {
		if(backpack[src] != null || backpack[dest] != null) {
			ItemData buffer = backpack[src];
			backpack[src] = backpack[dest];
			backpack[dest] = buffer;
		}
	}
	private int getSlot(int itemID) {
		for(int i = 0; i < 12; ++i) {
			if(backpack[i] != null && backpack[i].itemID == itemID) {
				return i;
			}
		}
		return -1;
	}
}
