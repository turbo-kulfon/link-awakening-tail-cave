package com.engine.spatial;

public class OutsideViewCheck implements IOutsideViewCheck {
	public interface Position {
		float getX();
		float getY();
		float getW();
		float getH();
	}

	protected Position position;
	protected OutsideViewCheckCallback callback;

	public OutsideViewCheck(Position position) {
		this.position = position;
		callback = new OutsideViewCheckCallback() {
			@Override public void outsideUp() {}
			@Override public void outsideRight() {}
			@Override public void outsideLeft() {}
			@Override public void outsideDown() {}
		};
	}
	public OutsideViewCheck(
			Position position,
			OutsideViewCheckCallback callback) {
		this.position = position;
		this.callback = callback;
	}

	@Override
	public void update() {
		if(position.getX() < 0) {
			callback.outsideLeft();
		}
		if(position.getX() + position.getW() > maxX) {
			callback.outsideRight();
		}
		if(position.getY() < 0) {
			callback.outsideUp();
		}
		if(position.getY() + position.getH() > maxY) {
			callback.outsideDown();
		}
	}
	@Override
	public void setCallback(OutsideViewCheckCallback callback) {
		this.callback = callback;
	}
}
