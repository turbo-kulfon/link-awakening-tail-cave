package com.la.equipment;

public interface IEquipmentSystem {
	void addSword();
	void addShield();
	void addGreifenfeder();
	void addBombs(int quantity);
	void addMagicPowder(int quantity);

	boolean removeBomb();
	boolean removeMagicPowder();

	void addKey(int dungeonID);
	int getKeyNumber(int dungeonID);
	boolean useKey(int dungeonID);

	void addNightmareKey(int dungeonID);
	boolean useNightmareKey(int dungeonID);
	boolean nightmareKeyPresent(int dungeonID);

	void addDungeonMap(int dungeonID);
	boolean isDungeonMapPresent(int dungeonID);
	void addCompass(int dungeonID);
	boolean isCompassPresent(int dungeonID);
	void addStoneBeak(int dungeonID);
	boolean isStoneBeakPresent(int dungeonID);

	void addRupee();
	void addRupee(int quantity);
	void removeRupee(int quantity);
	int getRupees();

	void addHeart();
	void getFairy();
	void fullHeal();
	void removeHeart(int quantity);
	int getHeart();

	void addBigHeart();
	int getBigHeart();

	void addPieceOfPower();
	void removePieceOfPower();
	boolean isPieceOfPowerActive();

	void addGuardianAcorn();
	void removeGuardianAcorn();
	boolean isGuardianAcornActive();

	void BItemSwitch(int slot);
	void AItemSwitch(int slot);

	boolean isItemIDActive(int itemID);

	int getBItemID();
	int getAItemID();
	int getItemID(int slot);
	int getQuantity(int slot);
}
