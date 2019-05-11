package com.engine.component.jump;

public class JumpComponent implements IJumpComponent {
	public interface Dependency {
		void onJumpUpdate(float height, boolean isJumping);
	}

	protected float gravity;
	protected float delta, height;
	protected boolean jump;
	protected Dependency dependency;

	public JumpComponent(
			float gravity,
			Dependency dependency) {
		this.gravity = gravity;
		this.dependency = dependency;
	}
	public JumpComponent(
			float gravity,
			float height,
			Dependency dependency) {
		this.gravity = gravity;
		this.height = height;
		this.dependency = dependency;
		jump = true;
	}

	@Override
	public boolean jump(float deltaStart) {
		if(jump == false) {
			delta = deltaStart;
			jump = true;
			return true;
		}
		return false;
	}
	@Override
	public void setHeight(float height) {
		this.height = height;
	}
	@Override
	public void stop() {
		jump = false;
		height = 0;
		dependency.onJumpUpdate(height, jump);
	}
	@Override
	public void update() {
		if(jump == true) {
			delta -= gravity;
			height += delta;
			if(height <= 0) {
				jump = false;
				height = 0;
			}
		}
		dependency.onJumpUpdate(height, jump);
	}
}
