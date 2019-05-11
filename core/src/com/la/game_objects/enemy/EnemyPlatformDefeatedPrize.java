package com.la.game_objects.enemy;

import com.engine.util.IRNG;
import com.la.factory.IRoomFactory;

public class EnemyPlatformDefeatedPrize implements IEnemyDefeatedPrize {
	public interface EnemyPlatformDefeatedDependency {
		boolean shouldSpawnGuardianAcorn();
		boolean shouldSpawnPieceOfPower();
		void addKill();
	}

	private IRoomFactory roomFactory;
	private IRNG rng;

	private int ruppeChance, heartChance;

	private EnemyPlatformDefeatedDependency dependency;

	public EnemyPlatformDefeatedPrize(
			IRoomFactory roomFactory,
			IRNG rng,
			EnemyPlatformDefeatedDependency dependency) {
		this.roomFactory = roomFactory;
		this.rng = rng;
		this.dependency = dependency;
	}

	@Override
	public void setRupeeDropChance(int chance) {
		ruppeChance = chance;
	}
	@Override
	public void setHeartDropChance(int chance) {
		heartChance = chance;
	}
	@Override
	public void createPickup(int cx, int cy) {
		if(dependency != null) {
			dependency.addKill();
			if(dependency.shouldSpawnGuardianAcorn() == true) {
				roomFactory.createGuardianAcornPlatform(cx, cy);
			}
			else {
				if(dependency.shouldSpawnPieceOfPower() == true) {
					roomFactory.createPieceOfPowerPlatform(cx, cy);
				}
				else {
					spawnItem(cx, cy);
				}
			}
		}
		else {
			spawnItem(cx, cy);
		}
	}

	private void spawnItem(int cx, int cy) {
		int random = rng.getRNG(0, 100);
		if(random <= ruppeChance) {
			roomFactory.createRupeePlatform(cx, cy);
		}
		else if(random <= ruppeChance + heartChance) {
			roomFactory.createSmallHeartPlatform(cx, cy);
		}
	}
}
