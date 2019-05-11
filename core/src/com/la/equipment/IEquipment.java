package com.la.equipment;

public interface IEquipment {
	void addKey();
	boolean useKey();

	void addRupee(int quantity);
	void removeRupee(int quantity);
	int getRupees();

	void addHeart(int quantity);
	void removeHeart(int quantity);
	void setHeart(int value);
	int getHeart();

	void addMaxHeart();
	int getMaxHeart();
}
