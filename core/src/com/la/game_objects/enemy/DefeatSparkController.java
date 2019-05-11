package com.la.game_objects.enemy;

import com.la.factory.IRoomFactory;

public class DefeatSparkController {
	public interface DefeatSparkControllerDependency {
		float getCenterX();
		float getCenterY();
	}

	private IEnemyDefeatedPrize prize;
	private IRoomFactory roomFactory;
	private DefeatSparkControllerDependency dependency;
	private boolean poweredUpHit;

	public DefeatSparkController(
			IEnemyDefeatedPrize prize,
			IRoomFactory roomFactory,
			DefeatSparkControllerDependency dependency) {
		this.prize = prize;
		this.roomFactory = roomFactory;
		this.dependency = dependency;
	}

	public void setHit(boolean poweredUpHit) {
		this.poweredUpHit = poweredUpHit;
	}

	public boolean onDead() {
		if(poweredUpHit == false) {
			roomFactory.createEnemyDefeatedSmallSpark((int)dependency.getCenterX(), (int)dependency.getCenterY(), ()-> {
				prize.createPickup((int)dependency.getCenterX(), (int)dependency.getCenterY());
			});
		}
		else {
			roomFactory.createEnemyDefeatedBigSpark((int)dependency.getCenterX(), (int)dependency.getCenterY(), ()-> {
				prize.createPickup((int)dependency.getCenterX(), (int)dependency.getCenterY());
			});
		}
		return poweredUpHit;
	}
}
