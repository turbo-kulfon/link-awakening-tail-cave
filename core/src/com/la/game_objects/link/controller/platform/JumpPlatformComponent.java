package com.la.game_objects.link.controller.platform;

public class JumpPlatformComponent {
	public interface JumpPlatformDependency {
		boolean isOnGround();
		void unsetOnGround();

		void setDeltaY(float dy);
		void onJump();
	}

	private JumpPlatformDependency dependency;
	private boolean doJump;
	private float delta;

	public JumpPlatformComponent(JumpPlatformDependency dependency) {
		this.dependency = dependency;
	}

	public void jump(float initialDelta) {
		if(dependency.isOnGround() == true) {
			doJump = true;
			delta = initialDelta;
			dependency.onJump();
		}
	}
	public void forceJump(float initialDelta) {
		doJump = true;
		delta = initialDelta;
	}
	public void update() {
		if(doJump == true) {
			dependency.setDeltaY(-delta);
			dependency.unsetOnGround();
			doJump = false;
		}
	}
	public void stop() {
		doJump = false;
	}
}
