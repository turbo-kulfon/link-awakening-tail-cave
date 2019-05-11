package com.engine.gfx;

import com.engine.gfx.core.IDrawCore;
import com.engine.gfx.core.ITextureDrawElement;

public class TextureDrawComponent {
	private IDrawCore drawCore;
	private ITextureDrawElement drawElement;

	public TextureDrawComponent(
			IDrawCore drawCore,
			ITextureDrawElement drawElement) {
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

	public void setY(float value) {
		drawElement.setY(value);
	}

	public void setTexture(int x, int y, int w, int h) {
		drawElement.setTexture(x, y, w, h);
	}
	public void setTexturePosition(int x, int y) {
		drawElement.setTexturePosition(x, y);
	}
	public void setTextureSize(int w, int h) {
		drawElement.setTextureSize(w, h);
	}

	public void setSpriteOffset(float offsetX, float offsetY) {
		drawElement.setSpriteOffset(offsetX, offsetY);
	}
	public void setSpriteOffsetX(float offsetX) {
		drawElement.setSpriteOffsetX(offsetX);
	}
	public void setSpriteOffsetY(float offsetY) {
		drawElement.setSpriteOffsetY(offsetY);
	}

	public void setFlipX(boolean flip) {
		drawElement.setFlipX(flip);
	}

	public void setRotation(float angle, float originX, float originY) {
		drawElement.setRotation(angle, originX, originY);
	}

	public void setHeight(float height) {
		drawElement.setHeight(height);
	}

	public void setAlpha(float alpha) {
		drawElement.setAlpha(alpha);
	}

	public void setInvert(boolean invert, float r, float g, float b) {
		drawElement.setInvert(invert, r, g, b);
	}
	public void setInstrumentMode(boolean active, float r, float g, float b) {
		drawElement.setInstrumentMode(active, r, g, b);
	}

	public void setLayer(int layer) {
		drawCore.changeLayer(drawElement, layer);
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
