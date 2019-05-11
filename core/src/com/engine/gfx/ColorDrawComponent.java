package com.engine.gfx;

import com.engine.gfx.core.IColorDrawElement;
import com.engine.gfx.core.IDrawCore;

public class ColorDrawComponent {
	private IDrawCore drawCore;
	private IColorDrawElement drawElement;

	public ColorDrawComponent(
			IDrawCore drawCore,
			IColorDrawElement drawElement) {
		this.drawCore = drawCore;
		this.drawElement = drawElement;
	}

	public void setCoordinates(float x, float y, float w, float h) {
		drawElement.setCoordinates(x, y, w, h);
	}
	public void setPosition(float x, float y) {
		drawElement.setPosition(x, y);
	}
	public void setSize(float w, float h) {
		drawElement.setSize(w, h);
	}

	public void setColor(float r, float g, float b) {
		drawElement.setColor(r, g, b);
	}
	public void setAlpha(float alpha) {
		drawElement.setAlpha(alpha);
	}

	public void setVisible(boolean visible) {
		drawElement.setVisible(visible);
	}
	public void changeLayer(int newLayer) {
		drawCore.changeLayer(drawElement, newLayer);
	}

	public void remove() {
		drawCore.removeElement(drawElement);	
	}
}
