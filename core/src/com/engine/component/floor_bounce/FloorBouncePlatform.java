package com.engine.component.floor_bounce;

import com.engine.component.jump.IJumpComponent;
import com.engine.component.jump.JumpPlatformComponent;
import com.engine.component.jump.JumpPlatformComponent.Dependency;

public class FloorBouncePlatform {
	private IJumpComponent jumpComponent;
	private float jumpStartDelta;

	public FloorBouncePlatform() {
		jumpComponent = new JumpPlatformComponent(0.125f, new Dependency() {
			
			@Override
			public void unsetOnGround() {
			}
			
			@Override
			public void stopDelta() {
			}
			
			@Override
			public void setDelta(float value) {
			}
			
			@Override
			public void onUpdate(float delta) {
			}
			
			@Override
			public void moveDelta(float delta) {
			}
			
			@Override
			public boolean isOnGround() {
				return false;
			}
			
			@Override
			public boolean isLevitating() {
				return false;
			}
			
			@Override
			public boolean ceilHit() {
				return false;
			}
			
			@Override
			public void calibrateDelta(float value) {
			}
		});
	}

	public void update() {
		jumpComponent.update();
	}
}
