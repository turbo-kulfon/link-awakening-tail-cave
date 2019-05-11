package com.la;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.engine.sound.ISoundPort;

public class LibGDXSoundPort implements ISoundPort {
	private Map<MusicID, Music> musics = new HashMap<>();
	private Map<SoundID, Sound> sounds = new HashMap<>();
	private Music currentMusic;

	public LibGDXSoundPort() {
		Music music1 = Gdx.audio.newMusic(Gdx.files.internal("snd/mus/LA_Get_SirenInstrument.wav"));
		music1.setLooping(false);
		musics.put(MusicID.GET_INSTRUMENT, music1);
		Music music2 = Gdx.audio.newMusic(Gdx.files.internal("snd/mus/LA_SirensInstrument_FullMoonCello.wav"));
		music2.setLooping(false);
		musics.put(MusicID.FULL_MOON_CELLO, music2);
		Music music3 = Gdx.audio.newMusic(Gdx.files.internal("snd/mus/Tail_Cave-svBnhTI4FCU.mp3"));
		music3.setLooping(true);
		musics.put(MusicID.DUNGEON, music3);
		Music music4 = Gdx.audio.newMusic(Gdx.files.internal("snd/mus/PowerUp-Oc1xTIH8FJ8.ogg"));
		music4.setLooping(true);
		musics.put(MusicID.POWER_UP, music4);
		Music music5 = Gdx.audio.newMusic(Gdx.files.internal("snd/mus/SideScrolling-UqHea7HWvVk.ogg"));
		music5.setLooping(true);
		musics.put(MusicID.UNDERGROUND, music5);
		Music music6 = Gdx.audio.newMusic(Gdx.files.internal("snd/mus/MiniBoss-5rnuVvhyt2E.ogg"));
		music6.setLooping(true);
		musics.put(MusicID.MINI_BOSS, music6);
		Music music7 = Gdx.audio.newMusic(Gdx.files.internal("snd/mus/BossBattle-moV1vVPEa3w.ogg"));
		music7.setLooping(true);
		musics.put(MusicID.DUNGEON_NIGHTMARE, music7);
		Music music8 = Gdx.audio.newMusic(Gdx.files.internal("snd/mus/BossDefeated-mDIWH4vy2iM.ogg"));
		music8.setLooping(true);
		musics.put(MusicID.NIGHTMARE_DEFEATED, music8);
		Music music9 = Gdx.audio.newMusic(Gdx.files.internal("snd/mus/GameOver-zr4wFu8njPw.ogg"));
		music9.setLooping(true);
		musics.put(MusicID.GAME_OVER, music9);

		loadSound(SoundID.KEY_PRESENT, "snd/sfx/LA_Dungeon_Signal.wav");
		loadSound(SoundID.KEY_BOUNCE, "snd/sfx/LA_Key_Bounce.wav");
		loadSound(SoundID.SECRET_SOLVED, "snd/sfx/LA_Secret1.wav");
		loadSound(SoundID.SECRET_ERROR, "snd/sfx/LA_Error.wav");
		loadSound(SoundID.ITEM_TAKE1, "snd/sfx/LA_Get_Item.wav");
		loadSound(SoundID.ITEM_TAKE2, "snd/sfx/LA_Get_Item2.wav");
		loadSound(SoundID.BOMB_EXPLODE, "snd/sfx/LA_Bomb_Explode.wav");
		loadSound(SoundID.HEART_CONTAINER_TAKE, "snd/sfx/LA_Fanfare_HeartContainer.wav");
		loadSound(SoundID.POWER_UP_TAKE, "snd/sfx/LA_Get_PowerUp.wav");
		loadSound(SoundID.SWORD_WALL_COLLISION, "snd/sfx/LA_Sword_Tap.wav");
		loadSound(SoundID.RUPEE_QUANTITY_CHANGE, "snd/sfx/LA_Get_Rupee.wav");
		loadSound(SoundID.SWITCH_PRESSED, "snd/sfx/LA_Dungeon_Switch.wav");
		loadSound(SoundID.DOOR_CLOSE, "snd/sfx/LA_Dungeon_DoorSlam.wav");

		loadSound(SoundID.ENEMY_HIT, "snd/sfx/LA_Enemy_Hit.wav");
		loadSound(SoundID.ENEMY_HIT_POWER_UP, "snd/sfx/LA_Enemy_Hit_Power.wav");
		loadSound(SoundID.ENEMY_DIE, "snd/sfx/LA_Enemy_Die.wav");
		loadSound(SoundID.ENEMY_DIE_POWER_UP, "snd/sfx/LA_Enemy_Die_Power.wav");
		loadSound(SoundID.ENEMY_FALL, "snd/sfx/LA_Enemy_Fall.wav");
		loadSound(SoundID.ENEMY_JUMP, "snd/sfx/LA_Enemy_Jump.wav");
		loadSound(SoundID.BLADE_TRAP_CHARGE, "snd/sfx/LA_BladeTrap.wav");

		loadSound(SoundID.BOSS_HIT, "snd/sfx/LA_Boss_Hit.wav");
		loadSound(SoundID.BOSS_DIE, "snd/sfx/LA_Boss_Die.wav");
		loadSound(SoundID.BOSS_JUMP, "snd/sfx/LA_Boss_Jump.wav");
		loadSound(SoundID.BOSS_DEATH_FLAME, "snd/sfx/LA_Boss_Bursting.wav");
		loadSound(SoundID.BOSS_DEATH_FLAME_FAST, "snd/sfx/LA_Boss_Bursting_Fast1.wav");
		loadSound(SoundID.ROLL, "snd/sfx/LA_RollingBones_Roller.wav");
		loadSound(SoundID.MOLDORM_WALK, "snd/sfx/LA_Moldorm_Speedy.wav");
		loadSound(SoundID.MOLDORM_SEGMENT_EXPLODE1, "snd/sfx/LA_Moldorm_SegmentExplode.wav");
		loadSound(SoundID.MOLDORM_SEGMENT_EXPLODE2, "snd/sfx/LA_Moldorm_SegmentExplode_Full.wav");

		loadSound(SoundID.CHEST_OPEN, "snd/sfx/LA_Chest_Open.wav");
		loadSound(SoundID.CHEST_LESSER_ITEM_FOUND, "snd/sfx/LA_Fanfare_Item.wav");
		loadSound(SoundID.CHEST_MAJOR_ITEM_FOUND, "snd/sfx/LA_Fanfare_Item_Extended.wav");

		loadSound(SoundID.SWORD_SLASH_1, "snd/sfx/LA_Sword_Slash1.wav");
		loadSound(SoundID.SWORD_SLASH_2, "snd/sfx/LA_Sword_Slash2.wav");
		loadSound(SoundID.SWORD_SLASH_3, "snd/sfx/LA_Sword_Slash3.wav");
		loadSound(SoundID.SWORD_SLASH_4, "snd/sfx/LA_Sword_Slash4.wav");
		loadSound(SoundID.SWORD_POWER_UP, "snd/sfx/LA_Sword_Charge.wav");
		loadSound(SoundID.SWORD_SPIN, "snd/sfx/LA_Sword_Spin.wav");
		loadSound(SoundID.SHIELD_UP, "snd/sfx/LA_Shield.wav");
		loadSound(SoundID.LINK_JUMP, "snd/sfx/LA_Link_Jump.wav");
		loadSound(SoundID.LINK_BOUNCE, "snd/sfx/LA_Link_Bounce.wav");
		loadSound(SoundID.LINK_LAND, "snd/sfx/LA_Link_Land.wav");
		loadSound(SoundID.LINK_FALL, "snd/sfx/LA_Link_Fall.wav");
		loadSound(SoundID.LINK_HURT, "snd/sfx/LA_Link_Hurt.wav");
		loadSound(SoundID.LINK_PICK_UP, "snd/sfx/LA_Link_PickUp.wav");
		loadSound(SoundID.LINK_THROW, "snd/sfx/LA_Link_Throw.wav");
		loadSound(SoundID.LINK_DIE, "snd/sfx/LA_Link_Dying.wav");
		loadSound(SoundID.DEFLECT, "snd/sfx/LA_Shield_Deflect.wav");

		loadSound(SoundID.MAGIC_POWDER_SPRINKLE, "snd/sfx/LA_MagicPowder.wav");
		loadSound(SoundID.FIRE_IGNITE, "snd/sfx/LA_MagicPowder_Ignite.wav");

		loadSound(SoundID.ONE_WAY_DOOR, "snd/sfx/LA_Dungeon_OneWayDoor.wav");
		loadSound(SoundID.STAIRS, "snd/sfx/LA_Stairs.wav");
		loadSound(SoundID.FLOOR_CRUMBLE, "snd/sfx/LA_Ground_Crumble.wav");
		loadSound(SoundID.ROCK_DESTROYED, "snd/sfx/LA_Rock_Shatter.wav");

		loadSound(SoundID.EQUIPMENT_MENU_OPEN, "snd/sfx/LA_PauseMenu_Open.wav");
		loadSound(SoundID.EQUIPMENT_MENU_CLOSE, "snd/sfx/LA_PauseMenu_Close.wav");
		loadSound(SoundID.MENU_CURSOR, "snd/sfx/LA_Menu_Cursor.wav");
		loadSound(SoundID.MENU_SELECT, "snd/sfx/LA_Menu_Select.wav");

		loadSound(SoundID.ROCK_PUSH, "snd/sfx/LA_Rock_Push.wav");

		loadSound(SoundID.TEXT_LETTER, "snd/sfx/LA_Text_Letter.wav");
		loadSound(SoundID.TEXT_END, "snd/sfx/LA_Text_Done.wav");

		loadSound(SoundID.TELEPORT_APPEARS, "snd/sfx/LA_Dungeon_Teleport_Appear.wav");
		loadSound(SoundID.TELEPORT_ENTER, "snd/sfx/LA_Dungeon_Teleport.wav");
		loadSound(SoundID.TELEPORT_DUNGEON_OUT, "snd/sfx/LA_Dungeon_TransportOut.wav");
	}

	@Override
	public void changeMusic(MusicID id) {
		Music music = musics.get(id);
		if(music != null) {
			stopMusic();
			music.play();
			currentMusic = music;
		}
	}
	@Override
	public void stopMusic() {
		musics.forEach((id, music)-> {
			music.stop();
		});
		currentMusic = null;
	}
	@Override
	public boolean isMusicPlaying(MusicID id) {
		Music music = musics.get(id);
		if(music != null) {
			return music.isPlaying();
		}
		return false;
	}
	@Override
	public void setMusicVolume(int volume) {
		if(currentMusic != null && currentMusic.isPlaying() == true) {
			currentMusic.setVolume((float)(volume/100.0f));
		}
	}

	@Override
	public void playSound(SoundID id) {
		Sound sound = sounds.get(id);
		if(sound != null) {
			sound.play();
		}
	}
	@Override
	public void playSoundNonSimultaneous(SoundID id) {
		Sound sound = sounds.get(id);
		if(sound != null) {
			sound.stop();
			sound.play();
		}
	}

	@Override
	public void stopAllSound() {
		sounds.forEach((id, sound)-> {
			sound.stop();
		});
	}

	public void dispose() {
		musics.forEach((id, music)-> {
			music.dispose();
		});
		sounds.forEach((id, sound)-> {
			sound.dispose();
		});
	}

	private void loadSound(SoundID id, String filePath) {
		Sound keyPresentSignal = Gdx.audio.newSound(Gdx.files.internal(filePath));
		sounds.put(id, keyPresentSignal);
	}
}
