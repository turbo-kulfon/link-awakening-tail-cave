package com.la.game_objects.enemy;

import com.engine.util.IRNG;
import com.la.factory.IRoomFactory;

public class EnemyDefeatedPrize implements IEnemyDefeatedPrize {
	public interface EnemyDefeatedDependency {
		boolean shouldSpawnGuardianAcorn();
		boolean shouldSpawnPieceOfPower();
		void addKill();
	}

	private IRoomFactory roomFactory;
	private IRNG rng;

	private int ruppeChance, heartChance;

	private EnemyDefeatedDependency dependency;

	public EnemyDefeatedPrize(
			IRoomFactory roomFactory,
			IRNG rng,
			EnemyDefeatedDependency dependency) {
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
				roomFactory.createGuardianAcorn(cx, cy);
			}
			else {
				if(dependency.shouldSpawnPieceOfPower() == true) {
					roomFactory.createPieceOfPower(cx, cy);
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
			roomFactory.createRupee(cx, cy);
		}
		else if(random <= ruppeChance + heartChance) {
			roomFactory.createSmallHeart(cx, cy);
		}
	}
}
