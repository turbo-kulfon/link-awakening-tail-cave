package com.engine.gfx.core;

public abstract class DrawElement implements IDrawElement {
	protected float x, y, w, h;
	protected int layer;
	protected boolean visible;

	public DrawElement() {
		visible = true;
	}
	public DrawElement(float w, float h, int layer) {
		this.w = w;
		this.h = h;
		this.layer = layer;
		visible = true;
	}
	public DrawElement(float x, float y, float w, float h, int layer) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.layer = layer;
		visible = true;
	}

	public void setCoordinates(float x, float y, float w, float h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	@Override
	public void setX(float x) {
		this.x = x;
	}
	@Override
	public void setY(float y) {
		this.y = y;
	}
	@Override
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}
	@Override
	public void setSize(float w, float h) {
		this.w = w;
		this.h = h;
	}

	@Override
	public void setLayer(int layer) {
		this.layer = layer;
	}
	@Override
	public int getLayer() {
		return layer;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	@Override
	public float getZ() {
		return y + h;
	}

	@Override
	public void draw() {
		if(visible == true) {
			drawImplementation();
		}
	}

	protected abstract void drawImplementation();
}
