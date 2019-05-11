package com.engine.direction;

public class Direction8 implements IDirection {
	private Direction direction = Direction.UP;

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void bounce() {
		if(direction == Direction.UP) {
			direction =  Direction.DOWN;
		}
		else if(direction == Direction.LEFT_UP) {
			direction =  Direction.RIGHT_DOWN;
		}
		else if(direction == Direction.LEFT) {
			direction = Direction.RIGHT;
		}
		else if(direction == Direction.LEFT_DOWN) {
			direction =  Direction.RIGHT_UP;
		}
		else if(direction == Direction.DOWN) {
			direction =  Direction.UP;
		}
		else if(direction == Direction.RIGHT_DOWN) {
			direction =  Direction.LEFT_UP;
		}
		else if(direction == Direction.RIGHT) {
			direction =  Direction.LEFT;
		}
		else if(direction == Direction.RIGHT_UP) {
			direction =  Direction.LEFT_DOWN;
		}
	}

	public void turnLeft() {
		if(direction == Direction.UP) {
			direction =  Direction.LEFT_UP;
		}
		else if(direction == Direction.LEFT_UP) {
			direction =  Direction.LEFT;
		}
		else if(direction == Direction.LEFT) {
			direction = Direction.LEFT_DOWN;
		}
		else if(direction == Direction.LEFT_DOWN) {
			direction =  Direction.DOWN;
		}
		else if(direction == Direction.DOWN) {
			direction =  Direction.RIGHT_DOWN;
		}
		else if(direction == Direction.RIGHT_DOWN) {
			direction =  Direction.RIGHT;
		}
		else if(direction == Direction.RIGHT) {
			direction =  Direction.RIGHT_UP;
		}
		else if(direction == Direction.RIGHT_UP) {
			direction =  Direction.UP;
		}
	}
	public void turnRight() {
		if(direction == Direction.UP) {
			direction =  Direction.RIGHT_UP;
		}
		else if(direction == Direction.RIGHT_UP) {
			direction =  Direction.RIGHT;
		}
		else if(direction == Direction.RIGHT) {
			direction =  Direction.RIGHT_DOWN;
		}
		else if(direction == Direction.RIGHT_DOWN) {
			direction =  Direction.DOWN;
		}
		else if(direction == Direction.DOWN) {
			direction =  Direction.LEFT_DOWN;
		}
		else if(direction == Direction.LEFT_DOWN) {
			direction =  Direction.LEFT;
		}
		else if(direction == Direction.LEFT) {
			direction =  Direction.LEFT_UP;
		}
		else if(direction == Direction.LEFT_UP) {
			direction =  Direction.UP;
		}
	}

	@Override
	public Direction getDirection() {
		return direction;
	}
}
