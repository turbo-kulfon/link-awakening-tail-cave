package com.la.game_objects.enemy;

public interface IEnemyDefeatedPrize {
	void setRupeeDropChance(int chance);
	void setHeartDropChance(int chance);
	void createPickup(int cx, int cy);
}
