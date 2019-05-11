package com.engine.component.gravity;

public class GravityComponent implements IGravityComponent {
	public interface GravityComponentPlatformAdditionalData {
		void setDelta(float value);
		void moveDelta(float delta);
		void calibrateDelta(float value);
		void stopDelta();
	}

	private float gravity;
	private GravityComponentPlatformAdditionalData additionalData;

	public GravityComponent(float gravity, GravityComponentPlatformAdditionalData additionalData) {
		this.gravity = gravity;
		this.additionalData = additionalData;
	}

	@Override
	public void update(boolean isOnGround, boolean isLevitating) {
		if(isLevitating == false) {
			if(isOnGround == false) {
				additionalData.moveDelta(gravity);
				additionalData.calibrateDelta(4);
			}
			else {
				additionalData.setDelta(0.1f);
			}
		}
	}
}
