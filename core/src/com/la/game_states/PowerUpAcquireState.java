package com.la.game_states;

import com.engine.game_state.IGameState;
import com.engine.game_state.IGameStateSystem;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.sound.SoundSystem;
import com.la.equipment.IEquipmentSystem;
import com.la.factory.IGameStateFactory;
import com.la.game_objects.effect.PowerUpItemAcquire;
import com.la.game_objects.link.ILinkData;

public class PowerUpAcquireState implements IGameState {
	public interface PowerUpAcquireStateDependency {
		boolean isPieceOfPowerPresent();
	}

	private SoundSystem soundSystem;
	private TextureDrawComponent itemDrawComponent;
	private PowerUpItemAcquire powerUpAcquire;
	private int itemID, counter, musicCounter = 120;
	private PowerUpAcquireStateDependency dependency;

	public PowerUpAcquireState(float x, float y,
			SoundSystem soundSystem,
			IGameStateSystem gameStateSystem,
			IGameStateFactory gameStateFactory,
			IEquipmentSystem equipmentSystem,
			GFXSystem gfxSystem,
			ILinkData linkData,
			int itemID,
			PowerUpAcquireStateDependency dependency) {
		this.soundSystem = soundSystem;
		this.itemID = itemID;
		this.dependency = dependency;

		powerUpAcquire = new PowerUpItemAcquire(x, y, gfxSystem, ()-> {
//			gameStateSystem.popState();
			if(itemID == 0) {
				gameStateFactory.createTextState(
					"You've got a|"
				  + "Guardian Acorn!|"
				  + "It will reduce|"
				  + "the damage you|"
				  + "take by half!", ()-> {
						itemDrawComponent.remove();
						gameStateSystem.popState();
					});
			}
			else if(itemID == 1) {
				gameStateFactory.createTextState(
					  "You got a Piece|"
					+ "of Power! You|"
					+ "can feel the|"
					+ "energy flowing|"
					+ "through you!", ()-> {
						itemDrawComponent.remove();
						gameStateSystem.popState();
					});
			}
		});
		itemDrawComponent = gfxSystem.createTextureDrawComponent(0);
		if(itemID == 0) {
			itemDrawComponent.setTexture(149, 77, 8, 15);
			itemDrawComponent.setSize(8, 15);
			itemDrawComponent.setPosition(x - 4, y - 7.5f);
		}
		else if(itemID == 1) {
			itemDrawComponent.setTexture(85, 32, 6, 15);
			itemDrawComponent.setSize(6, 15);
			itemDrawComponent.setPosition(x - 3, y - 7.5f);
		}
		linkData.setGetPowerUpItemAnimation();
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onRemove() {
		
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
	}

	@Override
	public void pauseUpdate() {
		musicCounter -= 1;
		if(musicCounter == 0) {
			soundSystem.setPowerUp(true);
			soundSystem.playDungeonMusic();
		}
		if(itemID == 1 && dependency.isPieceOfPowerPresent() == true) {
			counter += 1;
			if(counter > 8) {
				counter = 0;
			}
		}
		else {
			counter = 0;
		}
	}

	@Override
	public void update() {
		powerUpAcquire.update();
		if(itemID == 1 && dependency.isPieceOfPowerPresent() == true) {
			counter += 1;
			if(counter > 8) {
				counter = 0;
			}
		}
		else {
			counter = 0;
		}
	}

	@Override
	public void pauseDraw() {
		if(itemID == 1) {
			if(counter >= 4) {
				itemDrawComponent.setInvert(true, 1, 0.69f, 0.19f);
			}
			else {
				itemDrawComponent.setInvert(false, 1, 0.69f, 0.19f);
			}
		}
	}

	@Override
	public void draw() {
		if(itemID == 1) {
			if(counter >= 4) {
				itemDrawComponent.setInvert(true, 1, 0.69f, 0.19f);
			}
			else {
				itemDrawComponent.setInvert(false, 1, 0.69f, 0.19f);
			}
		}
		powerUpAcquire.draw();
	}
}
