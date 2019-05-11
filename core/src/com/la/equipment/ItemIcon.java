package com.la.equipment;

import com.engine.gfx.GFXSystem;
import com.engine.gfx.NumberDrawComponent;
import com.engine.gfx.TextureDrawComponent;

public class ItemIcon {
	private TextureDrawComponent drawComponent, itemLevel;
	private NumberDrawComponent numberDrawComponent;
	private IEquipmentSystem equipmentSystem;

	private int itemID, quantity, counter;
	private float x, y;

	public ItemIcon(
			GFXSystem gxfSystem,
			IEquipmentSystem equipmentSystem) {
		this.equipmentSystem = equipmentSystem;
		drawComponent = gxfSystem.createTextureDrawComponent(2);
		itemLevel = gxfSystem.createTextureDrawComponent(2);
		itemLevel.setTexture(104, 55, 11, 6);
		itemLevel.setSize(11, 6);
		itemLevel.setVisible(false);
		numberDrawComponent = gxfSystem.createNumberDrawComponent(2);
	}

	public void setItemID(int itemID) {
		this.itemID = itemID;
		drawComponent.setInvert(false, 1, 0.69f, 0.19f);
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	public void setVisibility(boolean visible) {
		drawComponent.setVisible(visible);
		if(visible == false) {
			numberDrawComponent.setVisible(false);
			itemLevel.setVisible(false);
		}
	}

	public void draw() {
		if(itemID == 0) {
			if(equipmentSystem.isPieceOfPowerActive() == true) {
				counter += 1;
				if(counter > 8) {
					counter = 0;
				}
			}
			else {
				counter = 0;
			}
			if(counter >= 4) {
				drawComponent.setInvert(true, 1, 0.69f, 0.19f);
			}
			else {
				drawComponent.setInvert(false, 1, 0.69f, 0.19f);
			}
			drawComponent.setTexture(133, 48, 8, 16);
			drawComponent.setPosition(x + 2, y - 1);
			drawComponent.setSize(8, 16);
			numberDrawComponent.setVisible(false);
			itemLevel.setVisible(true);
			itemLevel.setPosition(x + 12, y + 8);
		}
		else if(itemID == 1) {
			drawComponent.setTexture(125, 48, 8, 10);
			drawComponent.setPosition(x + 2, y + 2);
			drawComponent.setSize(8, 10);
			numberDrawComponent.setVisible(false);
			itemLevel.setVisible(true);
			itemLevel.setPosition(x + 12, y + 8);
		}
		else if(itemID == 2) {
			drawComponent.setTexture(141, 48, 8, 16);
			drawComponent.setPosition(x + 2, y - 1);
			drawComponent.setSize(8, 16);
			itemLevel.setVisible(false);
			numberDrawComponent.setVisible(false);
		}
		else if(itemID == 3) {
			drawComponent.setTexture(156, 64, 8, 13);
			drawComponent.setPosition(x + 2, y - 1);
			drawComponent.setSize(8, 13);
			itemLevel.setVisible(false);
			numberDrawComponent.setVisible(true);
			numberDrawComponent.setPosition(x + 11, y + 5);
			numberDrawComponent.setNumber(quantity, 2);
		}
		else if(itemID == 4) {
			drawComponent.setTexture(149, 48, 9, 16);
			drawComponent.setPosition(x + 3, y - 2);
			drawComponent.setSize(9, 16);
			itemLevel.setVisible(false);
			numberDrawComponent.setVisible(true);
			numberDrawComponent.setPosition(x + 11, y + 5);
			numberDrawComponent.setNumber(quantity, 2);
		}
	}

	public void remove() {
		drawComponent.remove();
		itemLevel.remove();
		numberDrawComponent.remove();
	}
}
