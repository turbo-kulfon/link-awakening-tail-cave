package com.la.game_objects.effect;

import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.util.Coordinate;
import com.engine.util.ICoordinate;
import com.engine.util.ICoordinate.Vector;

public class Petal {
	private TextureDrawComponent drawComponent;
	private float cenX, cenY;
	private float x, y;
	private int counter;
	private int dir;
	private ICoordinate coordinate;

	public Petal(float cx, float cy, int dir,
			GFXSystem gfxSystem) {
		this.cenX = cx;
		this.cenY = cy;
		this.dir = dir;

		counter = 45;

		drawComponent = gfxSystem.createTextureDrawComponent(4);

		coordinate = new Coordinate();
	}

	public void update() {
		counter -= 2;
		if(counter <= 0) {
			counter = 45;
		}
		Vector delta = coordinate.angleToDelta(dir * 45 - 90);
		x = cenX + delta.x * counter - 8;
		y = cenY + delta.y * counter - 4;
	}
	public void draw() {
		int changeFrame = 15;
		if(dir == 0) {
			if(counter > changeFrame) {
				drawComponent.setRotation(90, 8, 4);
				drawComponent.setTexture(260, 80, 16, 8);
				drawComponent.setSize(16, 8);
			}
			else {
				drawComponent.setRotation(90, 8, 3);
				drawComponent.setTexture(260, 88, 16, 6);
				drawComponent.setSize(16, 6);
			}
		}
		else if(dir == 1) {
			if(counter > changeFrame) {
				drawComponent.setRotation(45 - 90, 8, 4);
				drawComponent.setTexture(260, 80, 16, 8);
				drawComponent.setSize(16, 8);
			}
			else {
				drawComponent.setRotation(45 - 90, 8, 3);
				drawComponent.setTexture(260, 88, 16, 6);
				drawComponent.setSize(16, 6);
			}
		}
		else if(dir == 2) {
			if(counter > changeFrame) {
				drawComponent.setRotation(0, 8, 4);
				drawComponent.setTexture(260, 80, 16, 8);
				drawComponent.setSize(16, 8);
			}
			else {
				drawComponent.setRotation(0, 8, 3);
				drawComponent.setTexture(260, 88, 16, 6);
				drawComponent.setSize(16, 6);
			}
		}
		else if(dir == 3) {
			if(counter > changeFrame) {
				drawComponent.setRotation(135 - 90, 8, 4);
				drawComponent.setTexture(260, 80, 16, 8);
				drawComponent.setSize(16, 8);
			}
			else {
				drawComponent.setRotation(135 - 90, 8, 3);
				drawComponent.setTexture(260, 88, 16, 6);
				drawComponent.setSize(16, 6);
			}
		}
		else if(dir == 4) {
			if(counter > changeFrame) {
				drawComponent.setRotation(90, 8, 4);
				drawComponent.setTexture(260, 80, 16, 8);
				drawComponent.setSize(16, 8);
			}
			else {
				drawComponent.setRotation(90, 8, 3);
				drawComponent.setTexture(260, 88, 16, 6);
				drawComponent.setSize(16, 6);
			}
		}
		else if(dir == 5) {
			if(counter > changeFrame) {
				drawComponent.setRotation(225 - 90, 8, 4);
				drawComponent.setTexture(260, 80, 16, 8);
				drawComponent.setSize(16, 8);
			}
			else {
				drawComponent.setRotation(225 - 90, 8, 3);
				drawComponent.setTexture(260, 88, 16, 6);
				drawComponent.setSize(16, 6);
			}
		}
		else if(dir == 6) {
			if(counter > changeFrame) {
				drawComponent.setRotation(0, 8, 4);
				drawComponent.setTexture(260, 80, 16, 8);
				drawComponent.setSize(16, 8);
			}
			else {
				drawComponent.setRotation(0, 8, 3);
				drawComponent.setTexture(260, 88, 16, 6);
				drawComponent.setSize(16, 6);
			}
		}
		else if(dir == 7) {
			if(counter > changeFrame) {
				drawComponent.setRotation(315 - 90, 8, 4);
				drawComponent.setTexture(260, 80, 16, 8);
				drawComponent.setSize(16, 8);
			}
			else {
				drawComponent.setRotation(315 - 90, 8, 3);
				drawComponent.setTexture(260, 88, 16, 6);
				drawComponent.setSize(16, 6);
			}
		}
		if(counter < changeFrame) {
			if(counter > 5) {
				float alpha = (counter - 5)/10.0f;
				drawComponent.setAlpha(alpha);
			}
			else {
				drawComponent.setAlpha(0);
			}
		}
		else {
			drawComponent.setAlpha(1);
		}
		drawComponent.setPosition(x, y);
	}
	public void remove() {
		drawComponent.remove();
	}
}
