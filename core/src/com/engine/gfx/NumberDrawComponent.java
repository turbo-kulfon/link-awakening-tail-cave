package com.engine.gfx;

import com.engine.gfx.core.IDrawCore;
import com.engine.gfx.core.INumberDrawElement;

public class NumberDrawComponent {
	private INumberDrawElement drawElement;
	private IDrawCore drawCore;

	public NumberDrawComponent(
			INumberDrawElement drawElement,
			IDrawCore drawCore) {
		this.drawElement = drawElement;
		this.drawCore = drawCore;
	}

	public void setPosition(float x, float y) {
		drawElement.setPosition(x, y);
	}
	public void setNumber(int number, int digits) {
		drawElement.setNumber(number, digits);
	}
	public void setVisible(boolean visible) {
		drawElement.setVisible(visible);
	}

	public void remove() {
		drawCore.removeElement(drawElement);
	}
}
