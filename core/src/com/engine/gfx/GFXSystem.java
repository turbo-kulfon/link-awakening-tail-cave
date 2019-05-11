package com.engine.gfx;

import com.engine.gfx.core.ColorDrawElement;
import com.engine.gfx.core.DrawCore;
import com.engine.gfx.core.IColorDrawElement;
import com.engine.gfx.core.IDrawCore;
import com.engine.gfx.core.IDrawElement;
import com.engine.gfx.core.IDrawLayer;
import com.engine.gfx.core.INumberDrawElement;
import com.engine.gfx.core.ITextDrawElement;
import com.engine.gfx.core.ITextureDrawElement;
import com.engine.gfx.core.NumberDrawElement;
import com.engine.gfx.core.TextDrawElement;
import com.engine.gfx.core.TextureDrawElement;

public class GFXSystem {
	protected IDrawPort drawPort;
	protected IDrawCore drawCore;

	public GFXSystem(IDrawPort drawPort) {
		this.drawPort = drawPort;
		drawCore = new DrawCore();
	}

	public ColorDrawComponent createColorDrawComponent(int layer) {
		IColorDrawElement drawElement = new ColorDrawElement(drawPort);
		addToSystem(drawElement, layer);
		return new ColorDrawComponent(drawCore, drawElement);
	}
	public TextureDrawComponent createTextureDrawComponent(int layer) {
		ITextureDrawElement drawElement = new TextureDrawElement(drawPort);
		addToSystem(drawElement, layer);
		return new TextureDrawComponent(drawCore, drawElement);
	}
	public TextDrawComponent createTextDrawComponent(int layer) {
		ITextDrawElement drawElement = new TextDrawElement(drawPort);
		addToSystem(drawElement, layer);
		return new TextDrawComponent(drawElement, drawCore);
	}
	public NumberDrawComponent createNumberDrawComponent(int layer) {
		INumberDrawElement drawElement = new NumberDrawElement(drawPort);
		addToSystem(drawElement, layer);
		return new NumberDrawComponent(drawElement, drawCore);
	}

	public void addLayer(IDrawLayer drawLayer) {
		drawCore.addLayer(drawLayer);
	}
	public void draw() {
		drawCore.draw();
	}
	public void dispose() {
		drawCore.clear();
	}

	private void addToSystem(IDrawElement drawElement, int layer) {
		drawElement.setLayer(layer);
		drawCore.addElement(drawElement);
	}
}
