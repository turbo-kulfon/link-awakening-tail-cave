package com.la.equipment;

import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;

public class BlinkingCursor {
	private TextureDrawComponent cursor1, cursor2;
	private int blinkCounter;

	public BlinkingCursor(GFXSystem gfxSystem) {
		cursor1 = gfxSystem.createTextureDrawComponent(2);
		cursor1.setTexture(116, 55, 2, 14);
		cursor1.setSize(2, 14);
		cursor2 = gfxSystem.createTextureDrawComponent(2);
		cursor2.setTexture(118, 55, 2, 14);
		cursor2.setSize(2, 14);

		resetBlink();
	}

	public void resetBlink() {
		blinkCounter = 30;
	}
	public void update() {
		blinkCounter -= 1;
		if(blinkCounter < 0) {
			resetBlink();
		}
		if(blinkCounter > 10) {
			cursor1.setVisible(true);
			cursor2.setVisible(true);
		}
		else {
			cursor1.setVisible(false);
			cursor2.setVisible(false);
		}
	}

	public void setPosition(float x, float y) {
		cursor1.setPosition(x, y);
		cursor2.setPosition(x + 26, y);
	}

	public void remove() {
		cursor1.remove();
		cursor2.remove();
	}
}
