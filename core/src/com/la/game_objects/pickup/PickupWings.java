package com.la.game_objects.pickup;

import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;

public class PickupWings {
	private TextureDrawComponent wing1, wing2;
	private int counter;

	public PickupWings(GFXSystem gfxSystem) {
		wing1 = gfxSystem.createTextureDrawComponent(1);
		wing1.setTexture(178, 72, 8, 9);
		wing1.setSize(8, 9);

		wing2 = gfxSystem.createTextureDrawComponent(1);
		wing2.setTexture(178, 72, 8, 9);
		wing2.setSize(8, 9);
	}

	public void update() {
		if(counter > 16) {
			counter = 0;
		}
		else {
			counter += 1;
		}
	}
	public void draw(float cenX, float cenY, float yOffset) {
		if(counter <= 8) {
			wing1.setPosition(cenX - 12, cenY - 12);
			wing1.setRotation(0, 4, 4.5f);
			wing1.setFlipX(false);
			wing1.setHeight(-yOffset);

			wing2.setPosition(cenX + 4f, cenY - 11.5f);
			wing2.setRotation(0, 4, 4.5f);
			wing2.setFlipX(true);
			wing2.setHeight(-yOffset);
		}
		else {
			wing1.setPosition(cenX - 12, cenY - 6);
			wing1.setRotation(90, 4, 4.5f);
			wing1.setFlipX(true);
			wing1.setHeight(-yOffset);

			wing2.setPosition(cenX + 4f, cenY - 5.5f);
			wing2.setRotation(-180, 4, 4.5f);
			wing2.setFlipX(true);
			wing2.setHeight(-yOffset);
		}
	}

	public void remove() {
		wing1.remove();
		wing2.remove();
	}
}
