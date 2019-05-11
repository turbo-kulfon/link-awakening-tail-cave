package com.engine.direction;

public class LinkDirection implements IDirection {
	enum Mode {
		NONE, X_AXIS, Y_AXIS
	}

	private Mode mode = Mode.NONE;
	private Direction direction = Direction.DOWN;

	public void update(int xAxis, int yAxis) {
		if(xAxis == 0 && yAxis == 0) {
			mode = Mode.NONE;
			return;
		}
		if(mode == Mode.NONE) {
			if(xAxis < 0) {
				direction = Direction.LEFT;
				mode = Mode.X_AXIS;
			}
			else if(xAxis > 0) {
				direction = Direction.RIGHT;
				mode = Mode.X_AXIS;
			}
			if(yAxis < 0) {
				direction = Direction.UP;
				mode = Mode.Y_AXIS;
			}
			else if(yAxis > 0) {
				direction = Direction.DOWN;
				mode = Mode.Y_AXIS;
			}
		}
		else if(mode == Mode.X_AXIS) {
			if(xAxis < 0) {
				direction = Direction.LEFT;
			}
			else if(xAxis > 0) {
				direction = Direction.RIGHT;
			}
			else if(xAxis == 0) {
				mode = Mode.NONE;
			}
		}
		else if(mode == Mode.Y_AXIS) {
			if(yAxis < 0) {
				direction = Direction.UP;
			}
			else if(yAxis > 0) {
				direction = Direction.DOWN;
			}
			else if(yAxis == 0) {
				mode = Mode.NONE;
			}
		}
	}

	@Override
	public Direction getDirection() {
		return direction;
	}
}
