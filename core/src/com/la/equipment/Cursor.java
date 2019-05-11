package com.la.equipment;

import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;

public class Cursor {
	private TextureDrawComponent cursor1, cursor2;
	private TextureDrawComponent buttonLetter;

	public Cursor(GFXSystem gfxSystem, boolean isBButton) {
		cursor1 = gfxSystem.createTextureDrawComponent(2);
		cursor1.setTexture(116, 55, 2, 14);
		cursor1.setSize(2, 14);
		cursor2 = gfxSystem.createTextureDrawComponent(2);
		cursor2.setTexture(118, 55, 2, 14);
		cursor2.setSize(2, 14);
		if(isBButton == true) {
			buttonLetter = gfxSystem.createTextureDrawComponent(2);
			buttonLetter.setTexture(120, 61, 4, 7);
			buttonLetter.setSize(4, 7);
		}
		else {
			buttonLetter = gfxSystem.createTextureDrawComponent(2);
			buttonLetter.setTexture(124, 61, 4, 7);
			buttonLetter.setSize(4, 7);
		}
	}

	public void setPosition(float x, float y) {
		cursor1.setPosition(x, y);
		cursor2.setPosition(x + 26, y);
		buttonLetter.setPosition(x - 5, y);
	}

	public void remove() {
		cursor1.remove();
		cursor2.remove();
		buttonLetter.remove();
	}
}
