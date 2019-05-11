package com.la.equipment;

import com.engine.gfx.ColorDrawComponent;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.NumberDrawComponent;
import com.engine.gfx.TextureDrawComponent;
import com.engine.sound.SoundSystem;

public class StatusPanel {
	public interface StatusPanelCallback {
		void onDead();
	}

	private GFXSystem gfxSystem;
	private SoundSystem soundSystem;
	private IEquipmentSystem equipmentSystem;

	private ColorDrawComponent background;
	private TextureDrawComponent rupeeSymbol;
	private NumberDrawComponent rupeeQuantity;
	private TextureDrawComponent hearts[];
	private ItemIcon itemIcons[] = new ItemIcon[2];

	private Cursor itemCursor1, itemCursor2;

	private int yOffset;
	private int currentRupees, currentHP, maxHearts, counterRupee, counterHP;
	private StatusPanelCallback callback;

	public StatusPanel(
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			IEquipmentSystem equipmentSystem,
			StatusPanelCallback callback) {
		this.gfxSystem = gfxSystem;
		this.soundSystem = soundSystem;
		this.equipmentSystem = equipmentSystem;
		this.callback = callback;
		currentHP = equipmentSystem.getHeart();
		currentRupees = equipmentSystem.getRupees();

		yOffset = 128;

		background = gfxSystem.createColorDrawComponent(2);
		background.setColor(248.0f/256.0f, 248.0f/256.0f, 168.0f/256.0f);
		background.setSize(160, 16);

		rupeeSymbol = gfxSystem.createTextureDrawComponent(2);
		rupeeSymbol.setTexture(126, 69, 7, 7);
		rupeeSymbol.setSize(7, 7);

		rupeeQuantity = gfxSystem.createNumberDrawComponent(2);

		hearts = new TextureDrawComponent[14];

		itemCursor1 = new Cursor(gfxSystem, true);
		itemCursor2 = new Cursor(gfxSystem, false);

		for(int i = 0; i < 2; ++i) {
			itemIcons[i] = new ItemIcon(gfxSystem, equipmentSystem);
		}
	}

	public void update() {
		if(currentRupees != equipmentSystem.getRupees()) {
			counterRupee -= 1;
			if(counterRupee <= 0) {
				if(currentRupees < equipmentSystem.getRupees()) {
					currentRupees += 1;
				}
				else if(currentRupees > equipmentSystem.getRupees()) {
					currentRupees -= 1;
				}
				counterRupee = 3;
				soundSystem.rupeeQuantityChange();
			}
		}

		int target = equipmentSystem.getHeart();
		if(currentHP != target) {
			counterHP -= 1;
			if(counterHP <= 0) {
				if(currentHP < target) {
					currentHP += 1;
				}
				else if(currentHP > target) {
					currentHP -= 1;
				}
				counterHP = 5;
				if(currentHP <= 0) {
					callback.onDead();
				}
			}
		}
		int t = (int) (equipmentSystem.getBigHeart()/4.0f);
		if(maxHearts != t) {
			for(int i = 0; i < 14 && i < t; ++i) {
				if(hearts[i] == null) {
					int row = i % 7;
					TextureDrawComponent heartDrawComponent = gfxSystem.createTextureDrawComponent(2);
					if(i <= 6) {
						heartDrawComponent.setPosition(105 + row * 8, yOffset + 1);
					}
					else {
						heartDrawComponent.setPosition(105 + row * 8, yOffset + 9);
					}
					heartDrawComponent.setSize(7, 7);
					hearts[i] = heartDrawComponent;
				}
			}
		}
	}

	public void draw() {
		background.setPosition(0, yOffset);
		rupeeSymbol.setPosition(82, yOffset+1);
		rupeeQuantity.setPosition(81, yOffset + 9);
		rupeeQuantity.setNumber(currentRupees, 3);

		for(int i = 0; i < 14; ++i) {
			if(hearts[i] != null) {
				float rowY = i < 7 ? 1:9;
				if(isFullHeart((i+1) * 4) == true) {
					TextureDrawComponent drawComponent = hearts[i];
					drawComponent.setY(yOffset + rowY);
					drawComponent.setTexture(104, 48, 7, 7);
				}
				else {
					if(isHalfHeart((i+1) * 4) == false) {
						TextureDrawComponent drawComponent = hearts[i];
						drawComponent.setY(yOffset + rowY);
						drawComponent.setTexture(118, 48, 7, 7);
					}
					else {
						TextureDrawComponent drawComponent = hearts[i];
						drawComponent.setY(yOffset + rowY);
						drawComponent.setTexture(111, 48, 7, 7);
					}
				}
			}
		}

		itemCursor1.setPosition(6, yOffset + 1);
		itemCursor2.setPosition(46, yOffset + 1);

		for(int i = 0; i < 2; i++) {
			int itemID = equipmentSystem.getItemID(i+10);
			if(itemID != -1) {
				itemIcons[i].setItemID(itemID);
				itemIcons[i].setQuantity(equipmentSystem.getQuantity(i+10));
				itemIcons[i].setPosition(i % 2 == 0 ? 6 : 46, yOffset + 1);
				itemIcons[i].setVisibility(true);
				itemIcons[i].draw();
			}
			else {
				itemIcons[i].setVisibility(false);
			}
		}
	}

	public void setOffsetY(int yOffset) {
		this.yOffset = yOffset;
	}
	public int getOffsetY() {
		return yOffset;
	}

	public void remove() {
		background.remove();
		rupeeSymbol.remove();
		rupeeQuantity.remove();
		for(int i = 0; i < 14; ++i) {
			if(hearts[i] != null) {
				hearts[i].remove();
			}
		}
		itemCursor1.remove();
		itemCursor2.remove();
	}

	private boolean isFullHeart(int checkedHP) {
		return currentHP >= checkedHP;
	}
	private boolean isHalfHeart(int checkedHP) {
		int result = checkedHP - currentHP;
		return result == 1 || result == 2;
	}
}
