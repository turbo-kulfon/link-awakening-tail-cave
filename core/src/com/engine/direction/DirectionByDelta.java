package com.engine.direction;

import com.engine.util.Coordinate;
import com.engine.util.ICoordinate;

public class DirectionByDelta implements IDirection {
	private Direction direction;
	private ICoordinate coordinate = new Coordinate();

	public void update(float dx, float dy) {
		float angle = coordinate.deltaToAngle(dx, dy);
		if(angle >= 45 && angle <= 135) {
			direction = Direction.RIGHT;
		}
		else if(angle > 135 && angle <= 225) {
			direction = Direction.DOWN;
		}
		else if(angle > 225 && angle <= 315) {
			direction = Direction.LEFT;
		}
		else if(angle > 315 || angle < 45) {
			direction = Direction.UP;
		}
	}

	@Override
	public Direction getDirection() {
		return direction;
	}
}
