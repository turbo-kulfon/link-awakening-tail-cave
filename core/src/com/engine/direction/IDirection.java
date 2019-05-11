package com.engine.direction;

public interface IDirection {
	Direction getDirection();

	public static Direction getLeftDirection(Direction direction) {
		if(direction == Direction.LEFT) {
			return Direction.DOWN;
		}
		else if(direction == Direction.RIGHT) {
			return Direction.UP;
		}
		else if(direction == Direction.UP) {
			return Direction.LEFT;
		}
		else if(direction == Direction.DOWN) {
			return Direction.RIGHT;
		}
		return direction;
	}
	public static Direction getRightDirection(Direction direction) {
		if(direction == Direction.LEFT) {
			return Direction.UP;
		}
		else if(direction == Direction.RIGHT) {
			return Direction.DOWN;
		}
		else if(direction == Direction.UP) {
			return Direction.RIGHT;
		}
		else if(direction == Direction.DOWN) {
			return Direction.LEFT;
		}
		return direction;
	}
}
