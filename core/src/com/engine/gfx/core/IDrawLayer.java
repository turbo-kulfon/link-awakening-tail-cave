package com.engine.gfx.core;

public interface IDrawLayer {
	void addElement(IDrawElement drawElement);
	void removeElement(IDrawElement drawElement);

	void draw();

	void clear();
}
