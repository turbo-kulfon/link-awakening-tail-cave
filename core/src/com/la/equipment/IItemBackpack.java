package com.la.equipment;

public interface IItemBackpack {
	public class ItemData {
		public int itemID, quantity;

		public ItemData(int itemID, int quantity) {
			this.itemID = itemID;
			this.quantity = quantity;
		}
	}

	void addItem(ItemData item);
	boolean removeQuantity(int itemID);
	boolean removeQuantityAndItemWhenZero(int itemID);

	int getItemID(int slot);
	int getQuantity(int slot);

	int getBItemID();
	int getBQuantity();
	int getAItemID();
	int getAQuantity();

	void BItemSwitch(int slot);
	void AItemSwitch(int slot);
}
