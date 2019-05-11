package com.engine.component.floor_bounce;

import com.engine.component.jump.IJumpComponent;
import com.engine.component.jump.JumpComponent;

public class FloorBounce {
	public interface Dependency {
		void onUpdate(float height);
	}

	private IJumpComponent jumpComponent;
	private float jumpStartDelta, h;
	private Dependency dependency;

	public FloorBounce(float startHeight, Dependency dependency) {
		this.dependency = dependency;
		jumpStartDelta = 4f;
		jumpComponent = new JumpComponent(0.125f, startHeight, (height, isJumping)-> {
			h = height;
			if(isJumping == false && height <= 0) {
				jumpStartDelta /= 2.0f;
				jumpComponent.jump(jumpStartDelta);
			}
		});
	}
	public FloorBounce(float startHeight, float jumpStartDelta, Dependency dependency) {
		this.dependency = dependency;
		this.jumpStartDelta = jumpStartDelta;
		jumpComponent = new JumpComponent(0.125f, startHeight, (height, isJumping)-> {
			h = height;
			if(isJumping == false && height <= 0) {
				h = 0;
				this.jumpStartDelta /= 2.0f;
				jumpComponent.jump(this.jumpStartDelta);
			}
		});
	}
	public void reset(float height, float jumpStartDelta) {
		this.jumpStartDelta = jumpStartDelta;
		jumpComponent.stop();
		jumpComponent.jump(jumpStartDelta);
		jumpComponent.setHeight(height);
	}
	public void update() {
		jumpComponent.update();
		dependency.onUpdate(h);
	}
}
