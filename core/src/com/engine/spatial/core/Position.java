package com.engine.spatial.core;

public class Position implements IPosition {
	protected float x, y, w, h, w2, h2;

	public Position() {}
	public Position(float x, float y, float w, float h) {
		setCoordinates(x, y, w, h);
	}
	public Position(float w, float h) {
		setSize(w, h);
	}

	@Override
	public void setCoordinates(float x, float y, float w, float h) {
		setPosition(x, y);
		setSize(w, h);
	}
	@Override
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	@Override
	public void setSize(float w, float h) {
		setW(w);
		setH(h);
	}

	@Override
	public void move(float dx, float dy) {
		x += dx;
		y += dy;
	}

	@Override
	public void setX(float value) {
		x = value;
	}
	@Override
	public void setY(float value) {
		y = value;
	}
	@Override
	public void setW(float value) {
		w = value;
		w2 = value/2.0f;
	}
	@Override
	public void setH(float value) {
		h = value;
		h2 = value/2.0f;
	}

	@Override
	public float getX() {
		return x;
	}
	@Override
	public float getY() {
		return y;
	}
	@Override
	public float getW() {
		return w;
	}
	@Override
	public float getH() {
		return h;
	}

	@Override
	public float getCenterX() {
		return x + w2;
	}
	@Override
	public float getCenterY() {
		return y + h2;
	}

	@Override
	public void bounceLeft(float fromX) {
		x = fromX - w;
	}
	@Override
	public void bounceRight(float fromX) {
		x = fromX;
	}
	@Override
	public void bounceUp(float fromY) {
		y = fromY - h;
	}
	@Override
	public void bounceDown(float fromY) {
		y = fromY;
	}
}
