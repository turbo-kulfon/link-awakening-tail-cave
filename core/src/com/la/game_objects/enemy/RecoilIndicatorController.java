package com.la.game_objects.enemy;

import com.engine.gfx.TextureDrawComponent;

public class RecoilIndicatorController {
	public interface RecoilIndicatorControllerDependency {
		boolean isHit();
	}

	private TextureDrawComponent drawComponent;
	private RecoilIndicatorControllerDependency dependency;
	private int recoilCounter;

	public RecoilIndicatorController(TextureDrawComponent drawComponent, RecoilIndicatorControllerDependency dependency) {
		this.drawComponent = drawComponent;
		this.dependency = dependency;
		recoilCounter = -1;
	}

	public void update() {
		if(dependency.isHit() == true) {
			recoilCounter += 1;
			if(recoilCounter > 4) {
				recoilCounter = 0;
			}
		}
		else {
			recoilCounter = -1;
		}
	}
	public void draw() {
		drawComponent.setInvert(recoilCounter >= 0 && recoilCounter < 2, 0.76f, 0.19f, 0.19f);
	}
}
