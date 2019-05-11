package com.engine.sound;

import com.engine.sound.ISoundPort.MusicID;
import com.engine.sound.ISoundPort.SoundID;

public class SoundSystem {
	private ISoundPort soundPort;
	private int swordSlashCounter;
	private boolean isUnderground, bossCombat, nightmareDefeated, powerUpActive;

	public SoundSystem(ISoundPort soundPort) {
		this.soundPort = soundPort;
	}

	public void setMusicVolume(int volume) {
		soundPort.setMusicVolume(volume);
	}
	public void setIsUnderground(boolean value) {
		isUnderground = value;
	}
	public void setPowerUp(boolean value) {
		powerUpActive = value;
	}
	public void playDungeonMusic() {
		if(nightmareDefeated == false) {
			if(bossCombat == true) {
				return;
			}
			if(powerUpActive == false) {
				if(isUnderground == false) {
					soundPort.changeMusic(MusicID.DUNGEON);
				}
				else {
					soundPort.changeMusic(MusicID.UNDERGROUND);
				}
			}
			else {
				soundPort.changeMusic(MusicID.POWER_UP);
			}
		}
		else {
			soundPort.changeMusic(MusicID.NIGHTMARE_DEFEATED);
		}
	}
	public void stopMusic() {
		soundPort.stopMusic();
	}
	public void miniBossMusic() {
		soundPort.changeMusic(MusicID.MINI_BOSS);
		bossCombat = true;
	}
	public void nightmareCombat() {
		soundPort.changeMusic(MusicID.DUNGEON_NIGHTMARE);
		bossCombat = true;
	}
	public void unsetBossCombat() {
		bossCombat = false;
	}
	public void nightmareDefeated() {
		soundPort.changeMusic(MusicID.NIGHTMARE_DEFEATED);
		nightmareDefeated = true;
	}
	public void gameOverScreen() {
		soundPort.changeMusic(MusicID.GAME_OVER);
	}

	public void playKeyPresent() {
		soundPort.playSound(SoundID.KEY_PRESENT);
	}
	public void keyBounce() {
		soundPort.playSound(SoundID.KEY_BOUNCE);
	}
	public void takeKey() {
		soundPort.playSound(SoundID.ITEM_TAKE2);
	}
	public void takePickupItem() {
		soundPort.playSound(SoundID.ITEM_TAKE1);
	}
	public void takeFlyingPickupItem() {
		soundPort.playSound(SoundID.ITEM_TAKE2);
	}
	public void rupeeQuantityChange() {
		soundPort.playSoundNonSimultaneous(SoundID.RUPEE_QUANTITY_CHANGE);
	}
	public void takePowerUp() {
		soundPort.stopMusic();
		soundPort.playSound(SoundID.POWER_UP_TAKE);
	}
	public void takeHeartContainer() {
		soundPort.playSound(SoundID.HEART_CONTAINER_TAKE);
	}
	public void switchPressed() {
		soundPort.playSound(SoundID.SWITCH_PRESSED);
	}
	public void doorOpen() {
		soundPort.playSound(SoundID.CHEST_OPEN);
	}
	public void doorClose() {
		soundPort.playSound(SoundID.DOOR_CLOSE);
	}
	public void bombExplode() {
		soundPort.playSound(SoundID.BOMB_EXPLODE);
	}
	public void bombBounce() {
		soundPort.playSound(SoundID.LINK_BOUNCE);
	}

	public void swordWallCollision() {
		soundPort.playSound(SoundID.SWORD_WALL_COLLISION);
	}
	public void swordBombableWallCollision() {
		soundPort.playSound(SoundID.KEY_BOUNCE);
	}

	public void enemyHit() {
		soundPort.playSound(SoundID.ENEMY_HIT);
	}
	public void enemyHitWithPoweredUpSword() {
		soundPort.playSound(SoundID.ENEMY_HIT_POWER_UP);
	}
	public void enemyDie() {
		soundPort.playSound(SoundID.ENEMY_DIE);
	}
	public void enemyDieWithPoweredUpSword() {
		soundPort.playSound(SoundID.ENEMY_DIE_POWER_UP);
	}
	public void enemyFall() {
		soundPort.playSound(SoundID.ENEMY_FALL);
	}
	public void enemyJump() {
		soundPort.playSound(SoundID.ENEMY_JUMP);
	}
	public void bladeTrap() {
		soundPort.playSound(SoundID.BLADE_TRAP_CHARGE);
	}
	public void enemyStomped() {
		soundPort.playSound(SoundID.SWITCH_PRESSED);
	}

	public void bossHit() {
		soundPort.playSound(SoundID.BOSS_HIT);
	}
	public void miniBossDie() {
		soundPort.playSound(SoundID.BOSS_DIE);
		bossCombat = false;
	}
	public void nightmareDie() {
		soundPort.stopMusic();
		soundPort.playSound(SoundID.BOSS_DIE);
		bossCombat = false;
	}
	public void bossJump() {
		soundPort.playSound(SoundID.BOSS_JUMP);
	}
	public void bossDiesInFlames() {
		soundPort.playSound(SoundID.BOSS_DEATH_FLAME);
	}
	public void bossDiesInFlamesFast() {
		soundPort.playSoundNonSimultaneous(SoundID.BOSS_DEATH_FLAME_FAST);
	}
	public void roll() {
		soundPort.playSoundNonSimultaneous(SoundID.ROLL);
	}
	public void moldormWalk() {
		soundPort.playSound(SoundID.MOLDORM_WALK);
	}
	public void moldormSegmentExplode() {
		soundPort.playSoundNonSimultaneous(SoundID.MOLDORM_SEGMENT_EXPLODE1);
	}
	public void moldormSegmentExplode2() {
		soundPort.playSound(SoundID.MOLDORM_SEGMENT_EXPLODE2);
	}

	public void secretSolved() {
		soundPort.playSound(SoundID.SECRET_SOLVED);
	}
	public void secretError() {
		soundPort.playSound(SoundID.SECRET_ERROR);
	}

	public void openChest() {
		soundPort.playSound(SoundID.CHEST_OPEN);
	}
	public void chestLesserItemFound() {
		soundPort.playSound(SoundID.CHEST_LESSER_ITEM_FOUND);
	}
	public void chestMajorItemFound() {
		soundPort.playSound(SoundID.CHEST_MAJOR_ITEM_FOUND);
	}

	public void playSwordSlash() {
		if(swordSlashCounter == 0) {
			soundPort.playSound(SoundID.SWORD_SLASH_1);
		}
		else if(swordSlashCounter == 1) {
			soundPort.playSound(SoundID.SWORD_SLASH_2);
		}
		else if(swordSlashCounter == 2) {
			soundPort.playSound(SoundID.SWORD_SLASH_3);
		}
		else if(swordSlashCounter == 3) {
			soundPort.playSound(SoundID.SWORD_SLASH_4);
		}
		swordSlashCounter += 1;
		if(swordSlashCounter > 3) {
			swordSlashCounter = 0;
		}
	}
	public void swordPoweredUp() {
		soundPort.playSound(SoundID.SWORD_POWER_UP);
	}
	public void swordSpin() {
		soundPort.playSound(SoundID.SWORD_SPIN);
	}
	public void shieldUp() {
		soundPort.playSound(SoundID.SHIELD_UP);
	}
	public void linkJump() {
		soundPort.playSound(SoundID.LINK_JUMP);
	}
	public void linkBounce() {
		soundPort.playSound(SoundID.LINK_BOUNCE);
	}
	public void linkLand() {
		soundPort.playSound(SoundID.LINK_LAND);
	}
	public void linkFall() {
		soundPort.playSound(SoundID.LINK_FALL);
	}
	public void linkHurt() {
		soundPort.playSound(SoundID.LINK_HURT);
	}
	public void linkPickUp() {
		soundPort.playSound(SoundID.LINK_PICK_UP);
	}
	public void linkThrow() {
		soundPort.playSound(SoundID.LINK_THROW);
	}
	public void linkDies() {
		soundPort.stopMusic();
		soundPort.playSound(SoundID.LINK_DIE);
	}

	public void deflect() {
		soundPort.playSound(SoundID.DEFLECT);
	}

	public void rockPush() {
		soundPort.playSound(SoundID.ROCK_PUSH);
	}

	public void oneWayDoor() {
		soundPort.playSound(SoundID.ONE_WAY_DOOR);
	}
	public void enterStairs() {
		soundPort.playSound(SoundID.STAIRS);
	}
	public void floorCrumble() {
		soundPort.playSoundNonSimultaneous(SoundID.FLOOR_CRUMBLE);
	}
	public void destroyCrystal() {
		soundPort.playSound(SoundID.ROCK_DESTROYED);
	}

	public void menuOpen() {
		soundPort.playSound(SoundID.EQUIPMENT_MENU_OPEN);
	}
	public void menuClose() {
		soundPort.playSound(SoundID.EQUIPMENT_MENU_CLOSE);
	}
	public void menuCursor() {
		soundPort.playSound(SoundID.MENU_CURSOR);
	}
	public void menuSelect() {
		soundPort.playSoundNonSimultaneous(SoundID.MENU_SELECT);
	}
	public void letterDisplay() {
		soundPort.playSoundNonSimultaneous(SoundID.TEXT_LETTER);
	}
	public void sentenceEnd() {
		soundPort.playSound(SoundID.TEXT_END);
	}

	public void magicPowderSprinkle() {
		soundPort.playSound(SoundID.MAGIC_POWDER_SPRINKLE);
	}
	public void fireIgnite() {
		soundPort.playSound(SoundID.FIRE_IGNITE);
	}

	public void teleportCreated() {
		soundPort.playSound(SoundID.TELEPORT_APPEARS);
	}
	public void teleportEnter() {
		soundPort.playSound(SoundID.TELEPORT_ENTER);
	}

	public void getInstrument() {
		soundPort.changeMusic(MusicID.GET_INSTRUMENT);
	}
	public boolean isGetInstrumentPlaying() {
		return soundPort.isMusicPlaying(MusicID.GET_INSTRUMENT);
	}
	public void fullMoonCello() {
		soundPort.changeMusic(MusicID.FULL_MOON_CELLO);
	}
	public void warpOutDungeon() {
		soundPort.playSound(SoundID.TELEPORT_DUNGEON_OUT);
	}

	public void stopAllSound() {
		soundPort.stopMusic();
		soundPort.stopAllSound();
	}
}
