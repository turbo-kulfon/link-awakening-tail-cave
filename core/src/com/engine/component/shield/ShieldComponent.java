package com.engine.component.shield;

import com.engine.direction.Direction;
import com.engine.sound.SoundSystem;

public class ShieldComponent implements IShieldComponent {
	public interface ShieldComponentDependency {
		float getCenterX();
		float getCenterY();
		Direction getDirection();
	}

	private ShieldComponentDependency dependency;

	private SoundSystem soundSystem;
	private boolean buttonPressed, shieldUp;

	public void setDependency(SoundSystem soundSystem, ShieldComponentDependency dependency) {
		this.soundSystem = soundSystem;
		this.dependency = dependency;
	}

	@Override
	public void shieldUp() {
		buttonPressed = true;
	}

	@Override
	public void update() {
		if(buttonPressed == true) {
			if(shieldUp == false) {
				soundSystem.shieldUp();
			}
			shieldUp = true;
		}
		else {
			shieldUp = false;
		}
		buttonPressed = false;
	}

	@Override
	public void stop() {
		shieldUp = false;
	}

	@Override
	public boolean collisionCheck(float targetCenterX, float targetCenterY, Direction targetDirection) {
		if(shieldUp == false) {
			return false;
		}
		if(dependency.getDirection() == Direction.LEFT) {
			if(targetDirection == Direction.RIGHT) {
				return true;
			}
			else {
				if(targetCenterX + 4 <= dependency.getCenterX()) {
					return true;
				}
			}
		}
		else if(dependency.getDirection() == Direction.RIGHT) {
			if(targetDirection == Direction.LEFT) {
				return true;
			}
			else {
				if(targetCenterX - 4 >= dependency.getCenterX()) {
					return true;
				}
			}
		}
		else if(dependency.getDirection() == Direction.UP) {
			if(targetDirection == Direction.DOWN) {
				return true;
			}
			else {
				if(targetCenterY <= dependency.getCenterY()) {
					return true;
				}
			}
		}
		else if(dependency.getDirection() == Direction.DOWN) {
			if(targetDirection == Direction.UP) {
				return true;
			}
			else {
				if(targetCenterY >= dependency.getCenterY()) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean isActive() {
		return shieldUp;
	}
}
