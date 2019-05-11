package com.engine.gfx.core;

import com.engine.gfx.IDrawPort;

public class TextDrawElement extends DrawElement implements ITextDrawElement {
	private String text;
	private IDrawPort drawPort;

	public TextDrawElement(IDrawPort drawPort) {
		this.drawPort = drawPort;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	protected void drawImplementation() {
		if(text != null) {
			drawPort.drawText(x, y, text);
		}
	}
}
