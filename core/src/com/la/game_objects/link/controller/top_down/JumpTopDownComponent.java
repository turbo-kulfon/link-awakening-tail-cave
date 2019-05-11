package com.la.game_objects.link.controller.top_down;

public class JumpTopDownComponent {
	public interface JumpTopDownDependency {
		void update(float height, boolean isJumping);
		void onJump();
		void onLand();
	}

	private float delta, gravity, height;
	private boolean active;

	private JumpTopDownDependency dependency;

	public JumpTopDownComponent(JumpTopDownDependency dependency) {
		this.dependency = dependency;
		gravity = 0.125f;
	}
	public JumpTopDownComponent(JumpTopDownDependency dependency, float initialHeight) {
		this.dependency = dependency;
		gravity = 0.125f;
		height = initialHeight;
		if(height > 0) {
			active = true;
		}
	}

	public boolean jump(float initialDelta) {
		if(height <= 0) {
			delta = initialDelta;
			active = true;
			dependency.onJump();
			return true;
		}
		return false;
	}
	public void update() {
		if(active == true) {
			delta -= gravity;
			height += delta;
			if(height <= 0) {
				height = 0;
				active = false;
				dependency.onLand();
			}
		}
		dependency.update(height, active);
	}
	public void zeroDelta() {
		delta = 0;
	}
	public void stop() {
		height = 0;
		active = false;
	}
	public boolean isJumping() {
		return active;
	}
}
