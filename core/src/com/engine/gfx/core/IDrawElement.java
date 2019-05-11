package com.engine.gfx.core;

public interface IDrawElement {
	void setCoordinates(float x, float y, float w, float h);
	void setX(float x);
	void setY(float y);
	void setPosition(float x, float y);
	void setSize(float w, float h);

	void setLayer(int layer);
	int getLayer();

	void setVisible(boolean visible);

	float getZ();

	void draw();
}
