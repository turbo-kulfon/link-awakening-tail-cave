package com.la.equipment;

import com.engine.IInputPort;
import com.engine.gfx.ColorDrawComponent;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.NumberDrawComponent;
import com.engine.gfx.TextureDrawComponent;
import com.engine.sound.SoundSystem;

public class EquipmentPanel {
	private SoundSystem soundSystem;
	private IInputPort inputPort;
	private IEquipmentSystem equipmentSystem;

	private ColorDrawComponent background;
	private TextureDrawComponent horizontalLine, verticalLine;
	private TextureDrawComponent map, compass, beak, nightmareKey, smallKey;
	private NumberDrawComponent smallKeyNumber;
	private BlinkingCursor cursor;
	private ItemIcon itemIcons[] = new ItemIcon[10];

	private int cursorPosition, yOffset;
	private boolean blockX, blockY, BBlock, ABlock;

	public EquipmentPanel(
			IInputPort inputPort,
			SoundSystem soundSystem,
			GFXSystem gfxSystem,
			IEquipmentSystem equipmentSystem) {
		this.soundSystem = soundSystem;
		this.inputPort = inputPort;
		this.equipmentSystem = equipmentSystem;

		background = gfxSystem.createColorDrawComponent(2);
		background.setColor(248.0f/256.0f, 248.0f/256.0f, 168.0f/256.0f);
		background.setSize(160, 128);
		yOffset = 144;
		background.setPosition(0, yOffset);

		horizontalLine = gfxSystem.createTextureDrawComponent(2);
		horizontalLine.setTexture(0, 509, 159, 3);
		horizontalLine.setPosition(0, yOffset);
		horizontalLine.setSize(159, 3);

		verticalLine = gfxSystem.createTextureDrawComponent(2);
		verticalLine.setTexture(0, 389, 3, 119);
		verticalLine.setPosition(0, yOffset);
		verticalLine.setSize(3, 119);

		map = gfxSystem.createTextureDrawComponent(2);
		map.setVisible(false);
		map.setTexture(118, 76, 8, 14);
		map.setSize(8, 14);

		compass = gfxSystem.createTextureDrawComponent(2);
		compass.setVisible(false);
		compass.setTexture(126, 76, 8, 14);
		compass.setSize(8, 14);

		beak = gfxSystem.createTextureDrawComponent(2);
		beak.setVisible(false);
		beak.setTexture(142, 77, 7, 12);
		beak.setSize(7, 12);

		nightmareKey = gfxSystem.createTextureDrawComponent(2);
		nightmareKey.setVisible(false);
		nightmareKey.setTexture(134, 77, 8, 14);
		nightmareKey.setSize(8, 14);

		smallKey = gfxSystem.createTextureDrawComponent(2);
		smallKey.setVisible(false);
		smallKey.setTexture(164, 64, 7, 13);
		smallKey.setSize(7, 13);

		smallKeyNumber = gfxSystem.createNumberDrawComponent(2);

		setDungeonItems();

		cursor = new BlinkingCursor(gfxSystem);
		int row = cursorPosition / 2;
		cursor.setPosition(cursorPosition % 2 == 0 ? 6 : 38, yOffset + 24 + row * 24);

		for(int i = 0; i < 10; ++i) {
			itemIcons[i] = new ItemIcon(gfxSystem, equipmentSystem);
		}
	}

	public void setOffsetY(int yOffset) {
		this.yOffset = yOffset;
	}

	public void update() {
		if(inputPort.isLeftButtonPressed() == true) {
			if(blockX == false) {
				cursorPosition -= 1;
				calibrateCursor();
				blockX = true;
				soundSystem.menuCursor();
			}
		}
		else if(inputPort.isRightButtonPressed() == true) {
			if(blockX == false) {
				cursorPosition += 1;
				calibrateCursor();
				blockX = true;
				soundSystem.menuCursor();
			}
		}
		else {
			blockX = false;
		}
		if(inputPort.isUpButtonPressed() == true) {
			if(blockY == false) {
				cursorPosition -= 2;
				calibrateCursor();
				blockY = true;
				soundSystem.menuCursor();
			}
		}
		else if(inputPort.isDownButtonPressed() == true) {
			if(blockY == false) {
				cursorPosition += 2;
				calibrateCursor();
				blockY = true;
				soundSystem.menuCursor();
			}
		}
		else {
			blockY = false;
		}

		if(inputPort.isBButtonPressed() == true) {
			if(BBlock == false) {
				equipmentSystem.BItemSwitch(cursorPosition);
				BBlock = true;
				soundSystem.menuSelect();
			}
		}
		else {
			BBlock = false;
		}

		if(inputPort.isAButtonPressed() == true) {
			if(ABlock == false) {
				equipmentSystem.AItemSwitch(cursorPosition);
				ABlock = true;
				soundSystem.menuSelect();
			}
		}
		else {
			ABlock = false;
		}

		cursor.update();
	}

	public void draw() {
		background.setPosition(0, yOffset + 16);
		horizontalLine.setPosition(0, yOffset + 18);
		verticalLine.setPosition(67, yOffset + 24);
		cursor.setPosition(cursorPosition % 2 == 0 ? 6 : 38, yOffset + 24 + (cursorPosition / 2) * 24);

		for(int i = 0; i < 10; i++) {
			int itemID = equipmentSystem.getItemID(i);
			if(itemID != -1) {
				itemIcons[i].setItemID(itemID);
				itemIcons[i].setQuantity(equipmentSystem.getQuantity(i));
				itemIcons[i].setPosition(i % 2 == 0 ? 6 : 38, yOffset + 24 + (i / 2) * 24);
				itemIcons[i].setVisibility(true);
				itemIcons[i].draw();
			}
			else {
				itemIcons[i].setVisibility(false);
			}
		}

		setDungeonItems();
	}

	public void remove() {
		background.remove();
		horizontalLine.remove();
		verticalLine.remove();
		cursor.remove();
		for (ItemIcon itemIcon : itemIcons) {
			if(itemIcon != null) {
				itemIcon.remove();
			}
		}
	}

	private void calibrateCursor() {
		if(cursorPosition < 0) {
			cursorPosition += 10;
		}
		if(cursorPosition >= 10) {
			cursorPosition -= 10;
		}
		cursor.resetBlink();
	}

	private void setDungeonItems() {
		if(equipmentSystem.isDungeonMapPresent(0) == true) {
			map.setVisible(true);
			map.setPosition(80, 40 + yOffset);
		}
		if(equipmentSystem.isCompassPresent(0) == true) {
			compass.setVisible(true);
			compass.setPosition(96, 40 + yOffset);
		}
		if(equipmentSystem.isStoneBeakPresent(0) == true) {
			beak.setVisible(true);
			beak.setPosition(112, 40 + yOffset);
		}
		if(equipmentSystem.nightmareKeyPresent(0) == true) {
			nightmareKey.setVisible(true);
			nightmareKey.setPosition(128, 40 + yOffset);
		}
		if(equipmentSystem.getKeyNumber(0) > 0) {
			smallKey.setVisible(true);
			smallKey.setPosition(144, 40 + yOffset);
			smallKeyNumber.setVisible(true);
			smallKeyNumber.setPosition(152, 46 + yOffset);
			smallKeyNumber.setNumber(equipmentSystem.getKeyNumber(0), 1);
		}
		else {
			smallKey.setVisible(false);
			smallKeyNumber.setVisible(true);
		}
	}
}
