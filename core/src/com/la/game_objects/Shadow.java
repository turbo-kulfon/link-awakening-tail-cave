package com.la.game_objects;

import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;

public class Shadow {
	public interface ShadowDependency {
		float getCenterX();
		float getY();
		float getH();
	}

	private TextureDrawComponent drawComponent;
	private ShadowDependency dependency;

	public Shadow(
			GFXSystem gfxSystem,
			ShadowDependency dependency) {
		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(133, 71, 7, 4);
		drawComponent.setSize(7, 4);
		drawComponent.setSpriteOffset(-3.5f, -4);
		drawComponent.setAlpha(0.60f);

		this.dependency = dependency;
	}

	public void setVisible(boolean visible) {
		drawComponent.setVisible(visible);
	}
	public void draw() {
		drawComponent.setPosition(
			dependency.getCenterX(),
			dependency.getY() + dependency.getH() - 0.1f);
	}

	public void remove() {
		drawComponent.remove();
	}
}
