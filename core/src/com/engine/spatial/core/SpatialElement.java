package com.engine.spatial.core;

public class SpatialElement implements ISpatialElement {
	protected IPosition position;
	protected IMove move;
	protected ResponseCallback responseX, responseY;
	protected int id;
	protected boolean active, dynamic;

	public SpatialElement(int id, IPosition position, IMove move) {
		this.id = id;
		this.position = position;
		this.move = move;
		active = true;
		dynamic = true;
	}
	public SpatialElement(int id, IPosition position) {
		this.id = id;
		this.position = position;
		move = new MoveNULL();
		active = true;
		dynamic = false;
	}

	@Override
	public void setCoordinates(float x, float y, float w, float h) {
		position.setCoordinates(x, y, w, h);
	}
	@Override
	public void setPosition(float x, float y) {
		position.setPosition(x, y);
	}
	@Override
	public void setSize(float w, float h) {
		position.setSize(w, h);
	}
	@Override
	public void move(float dx, float dy) {
		position.move(dx, dy);
	}

	@Override
	public void setX(float value) {
		position.setX(value);
	}
	@Override
	public void setY(float value) {
		position.setY(value);
	}
	@Override
	public void setW(float value) {
		position.setW(value);
	}
	@Override
	public void setH(float value) {
		position.setH(value);
	}

	@Override
	public float getX() {
		return position.getX();
	}
	@Override
	public float getY() {
		return position.getY();
	}
	@Override
	public float getW() {
		return position.getW();
	}
	@Override
	public float getH() {
		return position.getH();
	}

	@Override
	public float getCenterX() {
		return position.getCenterX();
	}
	@Override
	public float getCenterY() {
		return position.getCenterY();
	}

	@Override
	public void setDelta(float dx, float dy) {
		move.setDelta(dx, dy);
	}
	@Override
	public void setDeltaX(float dx) {
		move.setDeltaX(dx);
	}
	@Override
	public void setDeltaY(float dy) {
		move.setDeltaY(dy);
	}

	@Override
	public void xAxisToZero(float delta) {
		move.xAxisToZero(delta);
	}
	@Override
	public void xAxisToValue(float value, float delta) {
		move.xAxisToValue(value, delta);
	}
	@Override
	public void yAxisToZero(float delta) {
		move.yAxisToZero(delta);
	}
	@Override
	public void yAxisToValue(float value, float delta) {
		move.yAxisToValue(value, delta);
	}

	@Override
	public float getDeltaX() {
		return move.getDeltaX();
	}
	@Override
	public float getDeltaY() {
		return move.getDeltaY();
	}

	@Override
	public void move() {
		position.move(move.getDeltaX(), move.getDeltaY());
	}

	@Override
	public void setCollisionResponse(ResponseCallback response) {
		responseX = response;
		responseY = response;
	}
	@Override
	public void setCollisionResponseX(ResponseCallback response) {
		responseX = response;
	}
	@Override
	public void setCollisionResponseY(ResponseCallback response) {
		responseY = response;
	}

	@Override
	public void responseX(int collidedID) {
		if(responseX != null) {
			responseX.response(collidedID);
		}
	}
	@Override
	public void responseY(int collidedID) {
		if(responseY != null) {
			responseY.response(collidedID);
		}
	}

	@Override
	public void bounceLeft(float fromX) {
		position.bounceLeft(fromX);
	}
	@Override
	public void bounceRight(float fromX) {
		position.bounceRight(fromX);
	}
	@Override
	public void bounceUp(float fromY) {
		position.bounceUp(fromY);
	}
	@Override
	public void bounceDown(float fromY) {
		position.bounceDown(fromY);
	}

	@Override
	public boolean isActive() {
		return active;
	}
	@Override
	public void setActive(boolean active) {
		this.active = active;
	}
	@Override
	public boolean isDynamic() {
		return dynamic;
	}

	@Override
	public int getID() {
		return id;
	}
}
