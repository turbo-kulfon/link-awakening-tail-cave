package com.la.game_objects.effect;

import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;

public class Note {
	private TextureDrawComponent drawComponent;
	private float x, y;
	private int counter = 60;
	private boolean left;

	public Note(GFXSystem gfxSystem, boolean bigNote, float x, float y, boolean left) {
		this.x = x;
		this.y = y;
		this.left = left;
		if(bigNote == true) {
			drawComponent = gfxSystem.createTextureDrawComponent(4);
			drawComponent.setTexture(250, 80, 10, 12);
			drawComponent.setSize(10, 12);
			this.x -= 5;
		}
		else {
			drawComponent = gfxSystem.createTextureDrawComponent(4);
			drawComponent.setTexture(250, 80, 6, 12);
			drawComponent.setSize(6, 12);
			this.x -= 3;
		}
	}

	public boolean update() {
		counter -= 1;
		if(counter > 0) {
			float speed = 0.20f;
			if(left == true) {
				x -= speed;
				y -= speed;
			}
			else {
				x += speed;
				y -= speed;
			}
		}
		else {
			drawComponent.remove();
			return true;
		}
		return false;
	}
	public void draw() {
		drawComponent.setPosition(x, y);
		if(counter <= 20) {
			float alpha = counter/20.0f;
			drawComponent.setAlpha(alpha);
		}
	}
	public void remove() {
		drawComponent.remove();
	}
}
