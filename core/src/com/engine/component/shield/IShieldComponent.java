package com.engine.component.shield;

import com.engine.direction.Direction;

public interface IShieldComponent {
	void shieldUp();
	void update();
	void stop();
	boolean collisionCheck(float targetCenterX, float targetCenterY, Direction targetDirection);

	boolean isActive();
}
