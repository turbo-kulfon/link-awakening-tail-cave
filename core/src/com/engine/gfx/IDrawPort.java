package com.engine.gfx;

public interface IDrawPort {
	void drawColorQuad(
		float x, float y, float w, float h,
		float r, float g, float b, float a);
	void drawTextureQuad(
		float x, float y, float w, float h,
		int texX, int texY, int texW, int texH,
		float alpha, boolean mirrorX);
	void drawTextureQuadInvert(
		float x, float y, float w, float h,
		int texX, int texY, int texW, int texH,
		float r, float g, float b,
		float alpha, boolean mirrorX);
	void drawInstrument(
		float x, float y, float w, float h,
		int texX, int texY, int texW, int texH,
		float r, float g, float b);
	 void drawTextureQuadRotatedIverted(
		float x, float y, float w, float h,
		int texX, int texY, int texW, int texH,
		float r, float g, float b,
		float angle, float originX, float originY,
		float alpha);
	void drawTextureQuadRotated(
		float x, float y, float w, float h,
		int texX, int texY, int texW, int texH,
		float angle, float originX, float originY,
		float alpha);
	void drawText(float x, float y, String text);
	void drawNumber(float x, float y, String text);
}
