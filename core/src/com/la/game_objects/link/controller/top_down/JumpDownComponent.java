package com.la.game_objects.link.controller.top_down;

public class JumpDownComponent {
	public interface JumpDownComponentDependency {
		boolean isJumping();
		void jump();
		void setDelta(float dx, float dy);
		void onJumpDownEnd();
	}

	private JumpDownComponentDependency dependency;
	private int mode = -1;

	public JumpDownComponent(
			JumpDownComponentDependency dependency) {
		this.dependency = dependency;
	}

	public void jumpDownLeft() {
		if(dependency.isJumping() == false) {
			dependency.jump();
			mode = 0;
		}
	}
	public void jumpDownRight() {
		if(dependency.isJumping() == false) {
			dependency.jump();
			mode = 1;
		}
	}
	public void update() {
		if(mode == 0) {
			dependency.setDelta(-1, 0);
			if(dependency.isJumping() == false) {
				mode = -1;
				dependency.onJumpDownEnd();
			}
		}
		else if(mode == 1) {
			dependency.setDelta( 1, 0);
			if(dependency.isJumping() == false) {
				mode = -1;
				dependency.onJumpDownEnd();
			}
		}
	}

	public boolean isActive() {
		return mode != -1;
	}
}
