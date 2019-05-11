package com.engine.gfx.core;

public interface IDrawCore {
	void addElement(IDrawElement drawElement);
	void removeElement(IDrawElement drawElement);

	void addLayer(IDrawLayer drawLayer);
	void changeLayer(IDrawElement drawElement, int newLayer);

	void draw();

	void clear();
}
