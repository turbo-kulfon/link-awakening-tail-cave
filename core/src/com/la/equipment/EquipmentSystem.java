package com.la.equipment;

import com.la.equipment.IItemBackpack.ItemData;
import com.la.power_up_item_system.PowerUpItemSystem;

public class EquipmentSystem implements IEquipmentSystem {
	public interface EquipmentSystemCallback {
		void onDead();
	}

	private IItemBackpack itemBackpack = new ItemBackpack();
	private IEquipment equipment = new Equipment();

	private int keys[] = new int[8];
	private boolean nightmareKeys[] = new boolean[8];
	private boolean maps[] = new boolean[8];
	private boolean compass[] = new boolean[8];
	private boolean stoneBeak[] = new boolean[8];

	private boolean guardianAcorn, pieceOfPower;

	private PowerUpItemSystem powerUpItemSystem;
	private EquipmentSystemCallback callback;

	public EquipmentSystem(PowerUpItemSystem powerUpItemSystem, EquipmentSystemCallback callback) {
		this.powerUpItemSystem = powerUpItemSystem;
		this.callback = callback;
	}

	@Override
	public void addSword() {
		itemBackpack.addItem(new ItemData(0, 0));
	}
	@Override
	public void addShield() {
		itemBackpack.addItem(new ItemData(1, 0));
	}
	@Override
	public void addGreifenfeder() {
		itemBackpack.addItem(new ItemData(2, 0));
	}
	@Override
	public void addBombs(int quantity) {
		itemBackpack.addItem(new ItemData(3, quantity));
	}
	@Override
	public void addMagicPowder(int quantity) {
		itemBackpack.addItem(new ItemData(4, quantity));
	}

	@Override
	public boolean removeBomb() {
		return itemBackpack.removeQuantity(3);
	}
	@Override
	public boolean removeMagicPowder() {
		return itemBackpack.removeQuantityAndItemWhenZero(4);
	}

	@Override
	public void addKey(int dungeonID) {
		keys[dungeonID] += 1;
	}
	@Override
	public int getKeyNumber(int dungeonID) {
		return keys[dungeonID];
	}
	@Override
	public boolean useKey(int dungeonID) {
		if(keys[dungeonID] > 0) {
			keys[dungeonID] -= 1;
			return true;
		}
		return false;
	}

	@Override
	public void addNightmareKey(int dungeonID) {
		nightmareKeys[dungeonID] = true;
	}
	@Override
	public boolean useNightmareKey(int dungeonID) {
		if(nightmareKeys[dungeonID] == true) {
			nightmareKeys[dungeonID] = false;
			return true;
		}
		return false;
	}
	@Override
	public boolean nightmareKeyPresent(int dungeonID) {
		return nightmareKeys[dungeonID];
	}

	@Override
	public void addDungeonMap(int dungeonID) {
		maps[dungeonID] = true;
	}
	@Override
	public boolean isDungeonMapPresent(int dungeonID) {
		return maps[dungeonID];
	}
	@Override
	public void addCompass(int dungeonID) {
		compass[dungeonID] = true;
	}
	@Override
	public boolean isCompassPresent(int dungeonID) {
		return compass[dungeonID];
	}
	@Override
	public void addStoneBeak(int dungeonID) {
		stoneBeak[dungeonID] = true;
	}
	@Override
	public boolean isStoneBeakPresent(int dungeonID) {
		return stoneBeak[dungeonID];
	}

	@Override
	public void addRupee() {
		equipment.addRupee(1);
	}
	@Override
	public void addRupee(int quantity) {
		equipment.addRupee(quantity);
	}
	@Override
	public void removeRupee(int quantity) {
		equipment.removeRupee(quantity);
	}
	@Override
	public int getRupees() {
		return equipment.getRupees();
	}

	@Override
	public void addHeart() {
		equipment.addHeart(4);
	}
	@Override
	public void getFairy() {
		equipment.addHeart(6*4);
	}
	@Override
	public void fullHeal() {
		equipment.setHeart(equipment.getMaxHeart());
	}
	@Override
	public void removeHeart(int quantity) {
		if(guardianAcorn == true) {
			if(quantity > 1) {
				quantity = (int) Math.ceil((float)quantity/2.0f);
			}
		}
		equipment.removeHeart(quantity);
		powerUpItemSystem.linkHit();
		if(equipment.getHeart() <= 0) {
			callback.onDead();
		}
	}
	@Override
	public int getHeart() {
		return equipment.getHeart();
	}

	@Override
	public void addBigHeart() {
		equipment.addMaxHeart();
		fullHeal();
	}
	@Override
	public int getBigHeart() {
		return equipment.getMaxHeart();
	}

	@Override
	public void addGuardianAcorn() {
		guardianAcorn = true;
	}
	@Override
	public void removeGuardianAcorn() {
		guardianAcorn = false;
	}
	@Override
	public boolean isGuardianAcornActive() {
		return guardianAcorn;
	}

	@Override
	public void addPieceOfPower() {
		pieceOfPower = true;
	}
	@Override
	public void removePieceOfPower() {
		pieceOfPower = false;
	}
	@Override
	public boolean isPieceOfPowerActive() {
		return pieceOfPower;
	}

	@Override
	public void BItemSwitch(int slot) {
		itemBackpack.BItemSwitch(slot);
	}
	@Override
	public void AItemSwitch(int slot) {
		itemBackpack.AItemSwitch(slot);
	}

	@Override
	public boolean isItemIDActive(int itemID) {
		if(itemBackpack.getBItemID() == itemID || 
		   itemBackpack.getAItemID() == itemID) {
			return true;
		}
		return false;
	}

	@Override
	public int getBItemID() {
		return itemBackpack.getBItemID();
	}
	@Override
	public int getAItemID() {
		return itemBackpack.getAItemID();
	}
	@Override
	public int getItemID(int slot) {
		return itemBackpack.getItemID(slot);
	}
	@Override
	public int getQuantity(int slot) {
		return itemBackpack.getQuantity(slot);
	}
}
