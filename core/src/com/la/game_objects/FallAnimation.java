package com.la.game_objects;

import com.engine.gfx.TextureDrawComponent;

public class FallAnimation {
	public interface FallAnimationDependency {
		boolean isFalling();
	}

	private TextureDrawComponent drawComponent;
	private FallAnimationDependency dependency;
	private float offsetX, offsetY;
	private int counter;

	public FallAnimation(
			TextureDrawComponent drawComponent,
			FallAnimationDependency dependency,
			float offsetX, float offsetY) {
		this.drawComponent = drawComponent;
		this.dependency = dependency;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public void update() {
		if(dependency.isFalling() == true) {
			counter += 1;
		}
	}
	public void draw() {
		if(dependency.isFalling() == true) {
			drawComponent.setRotation(0, 0, 0);
			if(counter >= 0 && counter <= 20) {
				drawComponent.setTexture(196, 80, 10, 10);
				drawComponent.setSize(10, 10);
				drawComponent.setSpriteOffset(offsetX, offsetY);
			}
			else if(counter >= 20 && counter <= 40) {
				drawComponent.setTexture(79, 161, 4, 4);
				drawComponent.setSize(4, 4);
				drawComponent.setSpriteOffset(offsetX + 3, offsetY + 3);
			}
			else if(counter >= 40 && counter <= 60) {
				drawComponent.setTexture(79, 165, 4, 4);
				drawComponent.setSize(4, 4);
				drawComponent.setSpriteOffset(offsetX + 3, offsetY + 3);
			}
		}
	}
}
