package com.engine.component.jump;

import com.engine.component.gravity.GravityComponent;
import com.engine.component.gravity.IGravityComponent;
import com.engine.component.gravity.GravityComponent.GravityComponentPlatformAdditionalData;

public class JumpPlatformComponent implements IJumpComponent {
	public interface Dependency {
		void setDelta(float value);
		void moveDelta(float delta);
		void calibrateDelta(float value);
		void stopDelta();

		boolean isOnGround();
		boolean isLevitating();
		boolean ceilHit();
		void unsetOnGround();

		void onUpdate(float delta);
	}

	private Dependency dependency;
	private IGravityComponent gravityComponent;

	public JumpPlatformComponent(float gravity, Dependency dependency) {
		this.dependency = dependency;
		gravityComponent = new GravityComponent(gravity, new GravityComponentPlatformAdditionalData() {
			@Override
			public void stopDelta() {
				dependency.stopDelta();
			}
			@Override
			public void setDelta(float value) {
				dependency.setDelta(value);
			}
			@Override
			public void moveDelta(float delta) {
				dependency.moveDelta(delta);
			}
			@Override
			public void calibrateDelta(float value) {
				dependency.calibrateDelta(value);
			}
		});
	}
	@Override public void stop() {}
	@Override public void setHeight(float height) {}
	@Override
	public boolean jump(float deltaStart) {
		if(dependency.isOnGround() == true && dependency.isLevitating() == false) {
			dependency.setDelta(-deltaStart);
			dependency.unsetOnGround();
			return true;
		}
		return false;
	}
	@Override
	public void update() {
		gravityComponent.update(dependency.isOnGround(), dependency.isLevitating());
	}
}
