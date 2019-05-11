package com.la.game_objects.effect;

import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;

public class PowerUpItemAcquire {
	public interface PowerUpItemAcquireCallback {
		void onEnd();
	}

	private TextureDrawComponent sparks[] = new TextureDrawComponent[4];
	private float x, y;
	private int mode, counter = 24;

	private PowerUpItemAcquireCallback callback;

	public PowerUpItemAcquire(float x, float y,
			GFXSystem gfxSystem,
			PowerUpItemAcquireCallback callback) {
		this.x = x;
		this.y = y;
		this.callback = callback;

		sparks[0] = gfxSystem.createTextureDrawComponent(0);
		sparks[1] = gfxSystem.createTextureDrawComponent(0);
		sparks[2] = gfxSystem.createTextureDrawComponent(0);
		sparks[3] = gfxSystem.createTextureDrawComponent(0);

		setDrawComponent(sparks[0]);
		setDrawComponent(sparks[1]);
		setDrawComponent(sparks[2]);
		setDrawComponent(sparks[3]);
	}

	public void update() {
		if(counter > 0) {
			counter -= 1;
		}
		else {
			if(mode == 0) {
				mode = 1;
				sparks[1].setVisible(false);
				sparks[2].setVisible(false);
				sparks[3].setVisible(false);
				counter = 8;
			}
			else if(mode == 1) {
				mode = 2;
			}
			else if(mode == 2) {
				remove();
				callback.onEnd();
			}
		}
	}
	public void draw() {
		if(mode == 0) {
			sparks[0].setPosition(x - 8 - counter, y - 8 - counter);
			sparks[1].setPosition(x - 8 + counter, y - 8 - counter);
			sparks[2].setPosition(x - 8 - counter, y - 8 + counter);
			sparks[3].setPosition(x - 8 + counter, y - 8 + counter);
		}
		else if(mode == 1) {
			sparks[0].setTexturePosition(174, 48);
			sparks[0].setPosition(x - 8, y - 8);
		}
	}

	private void remove() {
		for (TextureDrawComponent spark : sparks) {
			spark.remove();
		}
	}
	private void setDrawComponent(TextureDrawComponent drawComponent) {
		drawComponent.setTexture(158, 48, 16, 16);
		drawComponent.setSize(16, 16);
	}
}
