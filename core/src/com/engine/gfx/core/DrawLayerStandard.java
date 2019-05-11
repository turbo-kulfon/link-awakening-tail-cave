package com.engine.gfx.core;

import java.util.ArrayList;
import java.util.List;

public class DrawLayerStandard implements IDrawLayer {
	protected List<IDrawElement> elements = new ArrayList<>();

	@Override
	public void addElement(IDrawElement drawElement) {
		if(elements.contains(drawElement) == false) {
			elements.add(drawElement);
		}
	}
	@Override
	public void removeElement(IDrawElement drawElement) {
		elements.remove(drawElement);
	}

	@Override
	public void draw() {
		for (IDrawElement drawElement : elements) {
			drawElement.draw();
		}
	}

	@Override
	public void clear() {
		elements.clear();
	}
}
