package com.engine.gfx;

import com.engine.gfx.core.IDrawCore;
import com.engine.gfx.core.ITextDrawElement;

public class TextDrawComponent {
	private ITextDrawElement drawElement;
	private IDrawCore drawCore;

	public TextDrawComponent(
			ITextDrawElement drawElement,
			IDrawCore drawCore) {
		this.drawElement = drawElement;
		this.drawCore = drawCore;
	}

	public void setPositionX(float value) {
		drawElement.setX(value);
	}
	public void setPositionY(float value) {
		drawElement.setY(value);
	}
	public void setText(String text) {
		drawElement.setText(text);
	}

	public void remove() {
		drawCore.removeElement(drawElement);
	}
}
