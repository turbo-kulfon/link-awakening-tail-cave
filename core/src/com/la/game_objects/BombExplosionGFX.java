package com.la.game_objects;

import com.engine.gfx.TextureDrawComponent;

public class BombExplosionGFX {
	public interface BombExplosionDependency {
		float centerX();
		float centerY();
		int counter();
	}

	private TextureDrawComponent drawComponent;
	private BombExplosionDependency dependency;

	public BombExplosionGFX(TextureDrawComponent drawComponent, BombExplosionDependency dependency) {
		this.drawComponent = drawComponent;
		this.dependency = dependency;
	}

	public void draw() {
		if(dependency.counter() <= 38 && dependency.counter() > 29) {
			drawComponent.setTexture(190, 48, 16, 16);
			drawComponent.setSize(16, 16);
			drawComponent.setSpriteOffset(0, 0);
			drawComponent.setPosition(dependency.centerX() - 8, dependency.centerY() - 8);
		}
		else if(dependency.counter() <= 29 && dependency.counter() > 20) {
			drawComponent.setTexture(206, 80, 28, 24);
			drawComponent.setSize(28, 24);
			drawComponent.setSpriteOffset(0, 0);
			drawComponent.setPosition(dependency.centerX() - 14, dependency.centerY() - 12);
		}
		else if(dependency.counter() <= 20 && dependency.counter() > 11) {
			drawComponent.setTexture(184, 16, 32, 32);
			drawComponent.setSize(32, 32);
			drawComponent.setSpriteOffset(0, 0);
			drawComponent.setPosition(dependency.centerX() - 16, dependency.centerY() - 16);
		}
		else if(dependency.counter() <= 11 && dependency.counter() >= 0) {
			drawComponent.setTexture(216, 16, 32, 32);
			drawComponent.setSize(32, 32);
			drawComponent.setSpriteOffset(0, 0);
			drawComponent.setPosition(dependency.centerX() - 16, dependency.centerY() - 16);
		}
	}
}
