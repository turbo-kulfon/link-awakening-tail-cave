package com.la.power_up_item_system;

public class PowerUpItemSystem {
	public interface PowerUpHitCallback {
		void removeGuardianAcorn();
		void removePieceOfPower();
		void onNoPowerUpActive();
	}

	private int acornDefeatedEnemies;
	private int pieceOfPowerDefeatedEnemies;

	private int acornHP, pieceOfPowerHP;

	private PowerUpHitCallback callback;

	public void setCallback(PowerUpHitCallback callback) {
		this.callback = callback;
	}

	public void linkHit() {
		boolean wasActive = false;
		acornDefeatedEnemies = 0;
		if(acornHP > 0) {
			wasActive = true;
			acornHP -= 1;
			if(acornHP <= 0) {
				callback.removeGuardianAcorn();
			}
		}
		if(pieceOfPowerHP > 0) {
			wasActive = true;
			pieceOfPowerHP -= 1;
			if(pieceOfPowerHP <= 0) {
				callback.removePieceOfPower();
			}
		}
		if(wasActive == true && acornHP <= 0 && pieceOfPowerHP <= 0) {
			callback.onNoPowerUpActive();
		}
	}
	public void enemyDefeated() {
		if(acornHP <= 0) {
			acornDefeatedEnemies += 1;
		}
		if(pieceOfPowerHP <= 0) {
			pieceOfPowerDefeatedEnemies += 1;
		}
	}
	public boolean guardianAcornCheck() {
//		if(acornDefeatedEnemies >= 2) {
		if(acornDefeatedEnemies >= 12) {
			acornDefeatedEnemies = 0;
			return true;
		}
		return false;
	}
	public boolean pieceOfPowerCheck() {
//		if(pieceOfPowerDefeatedEnemies >= 2) {
		if(pieceOfPowerDefeatedEnemies >= 40) {
			pieceOfPowerDefeatedEnemies = 0;
			return true;
		}
		return false;
	}

	public void getGuardianAcorn() {
		acornHP = 3;
		acornDefeatedEnemies = 0;
	}
	public void getPieceOfPower() {
		pieceOfPowerHP = 3;
		pieceOfPowerDefeatedEnemies = 0;
	}
}
