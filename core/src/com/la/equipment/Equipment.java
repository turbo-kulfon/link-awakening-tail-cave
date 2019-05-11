package com.la.equipment;

public class Equipment implements IEquipment {
	private int keys, rupees = 78, heart = 12, maxHeart = 12;

	@Override
	public void addKey() {
		keys += 1;
	}
	@Override
	public boolean useKey() {
		if(keys > 0) {
			keys -= 1;
			return true;
		}
		return false;
	}

	@Override
	public void addRupee(int quantity) {
		rupees += quantity;
		if(rupees > 999) {
			rupees = 999;
		}
	}
	@Override
	public void removeRupee(int quantity) {
		rupees -= quantity;
		if(rupees < 0) {
			rupees = 0;
		}
	}
	@Override
	public int getRupees() {
		return rupees;
	}

	@Override
	public void addHeart(int quantity) {
		heart += quantity;
		if(heart > maxHeart) {
			heart = maxHeart;
		}
	}
	@Override
	public void removeHeart(int quantity) {
		heart -= quantity;
		if(heart < 0) {
			heart = 0;
		}
	}
	@Override
	public void setHeart(int value) {
		heart = value;
		if(heart > maxHeart) {
			heart = maxHeart;
		}
	}
 	@Override
	public int getHeart() {
		return heart;
	}

	@Override
	public void addMaxHeart() {
		maxHeart += 4;
	}
	@Override
	public int getMaxHeart() {
		return maxHeart;
	}
}
