package com.engine.spatial.core;

public class Move implements IMove {
	protected class Axis {
		private float value;

		public void setDelta(float value) {
			this.value = value;
		}

		public void stop() {
			value = 0;
		}
		public void toZero(float delta) {
			toValue(0, delta);
		}
		public void toValue(float toValue, float delta) {
			if(value < toValue) {
				value += delta;
				if(value > toValue) {
					value = toValue;
				}
			}
			else if(value > toValue) {
				value -= delta;
				if(value < toValue) {
					value = toValue;
				}
			}
		}

		public float getDelta() {
			return value;
		}
	}

	protected Axis dx = new Axis(), dy = new Axis();

	@Override
	public void setDelta(float dx, float dy) {
		this.dx.setDelta(dx);
		this.dy.setDelta(dy);
	}
	@Override
	public void setDeltaX(float dx) {
		this.dx.setDelta(dx);
	}
	@Override
	public void setDeltaY(float dy) {
		this.dy.setDelta(dy);
	}

	@Override
	public void stopX() {
		dx.stop();
	}
	@Override
	public void stopY() {
		dy.stop();
	}

	@Override
	public void xAxisToZero(float delta) {
		dx.toZero(delta);
	}
	@Override
	public void yAxisToZero(float delta) {
		dy.toZero(delta);
	}
	@Override
	public void xAxisToValue(float value, float delta) {
		dx.toValue(value, delta);
	}
	@Override
	public void yAxisToValue(float value, float delta) {
		dy.toValue(value, delta);
	}

	@Override
	public float getDeltaX() {
		return dx.getDelta();
	}
	@Override
	public float getDeltaY() {
		return dy.getDelta();
	}
}
