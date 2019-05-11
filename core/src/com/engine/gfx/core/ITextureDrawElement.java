package com.engine.gfx.core;

public interface ITextureDrawElement extends IDrawElement {
	void setTexture(int texX, int texY, int texW, int texH);
	void setTexturePosition(int texX, int texY);
	void setTextureSize(int texW, int texH);

	void setSpriteOffset(float offsetX, float offsetY);
	void setSpriteOffsetX(float offsetX);
	void setSpriteOffsetY(float offsetY);

	void setFlipX(boolean flip);

	void setRotation(float angle, float originX, float originY);

	void setHeight(float height);

	void setAlpha(float alpha);
	void setInvert(boolean invert, float r, float g, float b);
	void setInstrumentMode(boolean active, float r, float g, float b);
}
