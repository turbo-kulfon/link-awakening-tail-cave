package com.engine.gfx.core;

import java.util.ArrayList;
import java.util.List;

public class DrawCore implements IDrawCore {
	private List<IDrawLayer> drawLayers = new ArrayList<>();

	@Override
	public void addElement(IDrawElement drawElement) {
		drawLayers.get(drawElement.getLayer()).addElement(drawElement);
	}
	@Override
	public void removeElement(IDrawElement drawElement) {
		int layerID = drawElement.getLayer();
		if(layerID < drawLayers.size()) {
			drawLayers.get(drawElement.getLayer()).removeElement(drawElement);
		}
	}

	@Override
	public void addLayer(IDrawLayer drawLayer) {
		if(drawLayers.contains(drawLayer) == false) {
			drawLayers.add(drawLayer);
		}
	}

	@Override
	public void changeLayer(IDrawElement drawElement, int newLayer) {
		removeElement(drawElement);
		drawElement.setLayer(newLayer);
		addElement(drawElement);
	}

	@Override
	public void draw() {
		for (IDrawLayer drawLayer : drawLayers) {
			drawLayer.draw();
		}
	}

	@Override
	public void clear() {
		drawLayers.clear();
	}
}
