package com.engine.spatial;

import com.engine.spatial.core.ISpatialCore;
import com.engine.spatial.core.ISpatialElement;
import com.engine.spatial.core.ResponseCallback;

public class SpatialComponent implements ISpatialComponent {
	private ISpatialElement element;
	private ISpatialCore core;

	public SpatialComponent(ISpatialElement element, ISpatialCore core) {
		this.element = element;
		this.core = core;
	}

	@Override
	public void setCoordinates(float x, float y, float w, float h) {
		element.setCoordinates(x, y, w, h);
	}
	@Override
	public void setPosition(float x, float y) {
		element.setPosition(x, y);
	}
	@Override
	public void setSize(float w, float h) {
		element.setSize(w, h);
	}
	@Override
	public void move(float dx, float dy) {
		element.move(dx, dy);
	}

	@Override
	public void setX(float value) {
		element.setX(value);
	}
	@Override
	public void setY(float value) {
		element.setY(value);
	}
	@Override
	public void setW(float value) {
		element.setW(value);
	}
	@Override
	public void setH(float value) {
		element.setH(value);
	}

	@Override
	public float getX() {
		return element.getX();
	}
	@Override
	public float getY() {
		return element.getY();
	}
	@Override
	public float getW() {
		return element.getW();
	}
	@Override
	public float getH() {
		return element.getH();
	}

	@Override
	public float getCenterX() {
		return element.getCenterX();
	}
	@Override
	public float getCenterY() {
		return element.getCenterY();
	}

	@Override
	public void setDelta(float dx, float dy) {
		element.setDelta(dx, dy);
	}
	@Override
	public void setDeltaX(float dx) {
		element.setDeltaX(dx);
	}
	@Override
	public void setDeltaY(float dy) {
		element.setDeltaY(dy);
	}

	@Override
	public void xAxisToZero(float delta) {
		element.xAxisToZero(delta);
	}
	@Override
	public void xAxisToValue(float value, float delta) {
		element.xAxisToValue(value, delta);
	}
	@Override
	public void yAxisToZero(float delta) {
		element.yAxisToZero(delta);
	}
	@Override
	public void yAxisToValue(float value, float delta) {
		element.yAxisToValue(value, delta);
	}

	@Override
	public void stop() {
		element.setDelta(0, 0);
	}

	@Override
	public float getDeltaX() {
		return element.getDeltaX();
	}
	@Override
	public float getDeltaY() {
		return element.getDeltaY();
	}

	@Override
	public void setCollisionResponse(ResponseCallback response) {
		element.setCollisionResponse(response);
	}
	@Override
	public void setCollisionResponseX(ResponseCallback response) {
		element.setCollisionResponseX(response);
	}
	@Override
	public void setCollisionResponseY(ResponseCallback response) {
		element.setCollisionResponseY(response);
	}

	@Override
	public void bounceLeft(float fromX) {
		element.bounceLeft(fromX);
	}
	@Override
	public void bounceRight(float fromX) {
		element.bounceRight(fromX);
	}
	@Override
	public void bounceUp(float fromY) {
		element.bounceUp(fromY);
	}
	@Override
	public void bounceDown(float fromY) {
		element.bounceDown(fromY);
	}

	@Override
	public void setActive(boolean active) {
		element.setActive(active);
	}

	@Override
	public void remove() {
		core.removeElement(element);
	}
}
