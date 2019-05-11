package com.la.game_states;

import com.engine.game_state.IGameState;
import com.engine.game_state.IGameStateSystem;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.sound.SoundSystem;
import com.la.equipment.IEquipmentSystem;
import com.la.factory.IGameStateFactory;

public class ChestItemAcquireState implements IGameState {
	private IGameStateSystem gameStateSystem;
	private SoundSystem soundSystem;
	private IGameStateFactory gameStateFactory;
	private IEquipmentSystem equipmentSystem;

	private TextureDrawComponent itemDrawComponent;

	private int itemID, quantity;
	private float posX, posY;
	private String text;
	private int counter;
	private int fanfareMode;

	public ChestItemAcquireState(int itemID, int quantity, int posX, int posY,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			IGameStateSystem gameStateSystem,
			IGameStateFactory gameStateFactory,
			IEquipmentSystem equipmentSystem) {
		this.itemID = itemID;
		this.quantity = quantity;
		this.soundSystem = soundSystem;
		this.gameStateSystem = gameStateSystem;
		this.gameStateFactory = gameStateFactory;
		this.equipmentSystem = equipmentSystem;
		itemDrawComponent = gfxSystem.createTextureDrawComponent(2);

		if(itemID == 2) {
			this.posX = posX - 4;
			this.posY = posY - 16;
			itemDrawComponent.setTexture(141, 48, 8, 16);
			itemDrawComponent.setSize(8, 16);
			itemDrawComponent.setPosition(this.posX, this.posY);
			itemDrawComponent.setVisible(false);

			text = "You've got the|Roc's Feather!|" + 
					"It feels like|your body is a|" + 
					"lot lighter!|";

			fanfareMode = 2;
		}
		else if(itemID == 50) {
			this.posX = posX - 3.5f;
			this.posY = posY - 14;
			itemDrawComponent.setTexture(171, 64, 7, 14);
			itemDrawComponent.setSize(7, 14);
			itemDrawComponent.setPosition(this.posX, this.posY);
			itemDrawComponent.setVisible(false);

			this.quantity = 20;

			text = "   You got " + 20 + "|" +
				   "     Rupees!|" + 
				   "      JOY!";

			fanfareMode = 1;
		}
		else if(itemID == 70) {
			this.posX = posX - 3.5f;
			this.posY = posY - 13;
			itemDrawComponent.setTexture(164, 64, 7, 13);
			itemDrawComponent.setSize(7, 13);
			itemDrawComponent.setPosition(this.posX, this.posY);
			itemDrawComponent.setVisible(false);

			text = "You got a Small|" +
				   "Key! You can|" + 
				   "open a locked|" +
				   "door.";

			fanfareMode = 1;
		}
		else if(itemID == 80) {
			this.posX = posX - 4f;
			this.posY = posY - 14;
			itemDrawComponent.setTexture(134, 77, 8, 14);
			itemDrawComponent.setSize(8, 14);
			itemDrawComponent.setPosition(this.posX, this.posY);
			itemDrawComponent.setVisible(false);

			text =    "You've got the|"
					+ "Nightmare's Key!|"
					+ "Now you can open|"
					+ "the door to the|"
					+ "Nightmare's|"
					+ "Lair!";

			fanfareMode = 1;
		}
		else if(itemID == 100) {
			this.posX = posX - 4;
			this.posY = posY - 14;
			itemDrawComponent.setTexture(118, 76, 8, 14);
			itemDrawComponent.setSize(8, 14);
			itemDrawComponent.setPosition(this.posX, this.posY);
			itemDrawComponent.setVisible(false);

			text =    "At last, you got|"
					+ "a Map! Press|"
					+ "the START Button|"
					+ "to look at it!";

			fanfareMode = 1;
		}
		else if(itemID == 110) {
			this.posX = posX - 4;
			this.posY = posY - 14;
			itemDrawComponent.setTexture(126, 76, 8, 14);
			itemDrawComponent.setSize(8, 14);
			itemDrawComponent.setPosition(this.posX, this.posY);
			itemDrawComponent.setVisible(false);

			text =    "You've got the|"
					+ "Compass! Now,|"
					+ "you can see|"
					+ "where the chests|"
					+ "and Nightmares|"
					+ "are hidden! This|"
					+ "Compass has a|"
					+ "new feature-- a|"
					+ "tone will tell|"
					+ "you if a key is|"
					+ "hidden in a room|"
					+ "when you enter!";

			fanfareMode = 1;
		}
		else if(itemID == 120) {
			this.posX = posX - 3.5f;
			this.posY = posY - 12;
			itemDrawComponent.setTexture(142, 77, 7, 12);
			itemDrawComponent.setSize(7, 12);
			itemDrawComponent.setPosition(this.posX, this.posY);
			itemDrawComponent.setVisible(false);

			text =    "You found a|"
					+ "stone beak!|"
					+ "Let's find the|"
					+ "owl statue that|"
					+ "belongs to it.";

			fanfareMode = 1;
		}

		counter = 120;
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
	}

	@Override
	public void update() {
		counter -= 1;
		if(counter <= 80) {
			if(fanfareMode == 1) {
				soundSystem.chestLesserItemFound();
				fanfareMode = 0;
			}
			else if(fanfareMode == 2) {
				soundSystem.chestMajorItemFound();
				fanfareMode = 0;
			}
			itemDrawComponent.setVisible(true);
			if(counter > 30) {
				posY -= 32.0f/400.0f;
			}
		}
		if(counter <= 0) {
			gameStateSystem.popState();
			gameStateFactory.createTextState(
				text, ()-> {
					itemDrawComponent.remove();
				});
			if(itemID == 2) {
				equipmentSystem.addGreifenfeder();
			}
			else if(itemID == 50) {
				equipmentSystem.addRupee(quantity);
			}
			else if(itemID == 70) {
				equipmentSystem.addKey(0);
			}
			else if(itemID == 80) {
				equipmentSystem.addNightmareKey(0);
			}
			else if(itemID == 100) {
				equipmentSystem.addDungeonMap(0);
			}
			else if(itemID == 110) {
				equipmentSystem.addCompass(0);
			}
			else if(itemID == 120) {
				equipmentSystem.addStoneBeak(0);
			}
		}
	}

	@Override
	public void pauseDraw() {
	}
	@Override
	public void draw() {
		itemDrawComponent.setPosition(this.posX, this.posY);
	}
}
