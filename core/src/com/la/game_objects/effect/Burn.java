package com.la.game_objects.effect;

import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;

public class Burn {
	public interface BurnDependency {
		float getCenterX();
		float getBottomY();

		float getHeight();
	}

	private TextureDrawComponent drawComponent;

	private int counter, frame;
	private boolean active;
	private BurnDependency dependency;

	public Burn(
			GFXSystem gfxSystem,
			BurnDependency dependency) {
		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setVisible(false);
		drawComponent.setTextureSize(14, 16);
		drawComponent.setSize(14, 16);

		this.dependency = dependency;
	}

	public void setActive(boolean active) {
		this.active = active;
		drawComponent.setVisible(active);
	}
	public void update() {
		if(active == true) {
			counter += 1;
			if(counter % 20 < 10) {
				frame = 0;
			}
			else {
				frame = 1;
			}
		}
	}
	public void draw() {
		if(active == true) {
			if(frame == 0) {
				drawComponent.setTexturePosition(178, 90);
			}
			else {
				drawComponent.setTexturePosition(192, 90);
			}

			drawComponent.setPosition(dependency.getCenterX() - 7, dependency.getBottomY() - 15.9f);
			drawComponent.setHeight(-dependency.getHeight());
		}
	}
	public void remove() {
		drawComponent.remove();
	}
}
