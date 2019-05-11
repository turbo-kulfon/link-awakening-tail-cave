package com.engine.direction;

public class LinkDirectionPlatform implements IDirection {
	enum Mode {
		NONE, X_AXIS, Y_AXIS
	}

	private Direction direction = Direction.DOWN;

	public void update(int xAxis, int yAxis) {
		if(xAxis == 0 && yAxis == 0) {
			return;
		}
		if(yAxis < 0) {
			direction = Direction.UP;
		}
		else if(yAxis > 0) {
			direction = Direction.DOWN;
		}
		if(xAxis < 0) {
			direction = Direction.LEFT;
		}
		else if(xAxis > 0) {
			direction = Direction.RIGHT;
		}
	}

	@Override
	public Direction getDirection() {
		return direction;
	}
}
