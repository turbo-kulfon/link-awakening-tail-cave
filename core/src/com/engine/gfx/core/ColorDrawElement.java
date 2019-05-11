package com.engine.gfx.core;

import com.engine.gfx.IDrawPort;

public class ColorDrawElement extends DrawElement implements IColorDrawElement {
	protected float r, g, b, a;
	protected IDrawPort drawPort;

	public ColorDrawElement(IDrawPort drawPort) {
		this.drawPort = drawPort;
		setColor(1, 1, 1);
		setAlpha(1);
	}

	@Override
	public void setColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}
	@Override
	public void setAlpha(float alpha) {
		a = alpha;
	}

	@Override
	protected void drawImplementation() {
		drawPort.drawColorQuad(x, y, w, h, r, g, b, a);
	}
}
