package com.engine.gfx.core;

import com.engine.gfx.IDrawPort;

public class TextureDrawElement extends DrawElement implements ITextureDrawElement {
	protected enum Mode {
		STANDARD,
		INVERTED,
		INSTRUMENT
	}

	protected int texX, texY, texW, texH;
	protected float alpha;
	protected float offsetX, offsetY, height;
	protected float angle, originX, originY;
	protected float invertR, invertG, invertB;
	protected boolean flipX;
	protected Mode mode = Mode.STANDARD;
	protected IDrawPort drawPort;

	public TextureDrawElement(IDrawPort drawPort) {
		this.drawPort = drawPort;
		alpha = 1;
	}

	@Override
	public void setTexture(int texX, int texY, int texW, int texH) {
		this.texX = texX;
		this.texY = texY;
		this.texW = texW;
		this.texH = texH;
	}
	@Override
	public void setTexturePosition(int texX, int texY) {
		this.texX = texX;
		this.texY = texY;
	}
	@Override
	public void setTextureSize(int texW, int texH) {
		this.texW = texW;
		this.texH = texH;
	}

	@Override
	public void setSpriteOffset(float offsetX, float offsetY) {
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}
	@Override
	public void setSpriteOffsetX(float offsetX) {
		this.offsetX = offsetX;
	}
	@Override
	public void setSpriteOffsetY(float offsetY) {
		this.offsetY = offsetY;
	}

	@Override
	public void setFlipX(boolean flip) {
		flipX = flip;
	}

	@Override
	public void setRotation(float angle, float originX, float originY) {
		this.angle = angle;
		this.originX = originX;
		this.originY = originY;
	}

	@Override
	public void setHeight(float height) {
		this.height = height;
	}

	@Override
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	@Override
	public void setInvert(boolean invert, float r, float g, float b) {
		if(invert == true) {
			mode = Mode.INVERTED;
		}
		else {
			mode = Mode.STANDARD;
		}
		this.invertR = r;
		this.invertG = g;
		this.invertB = b;
	}
	@Override
	public void setInstrumentMode(boolean active, float r, float g, float b) {
		if(active == true) {
			mode = Mode.INSTRUMENT;
		}
		else {
			mode = Mode.STANDARD;
		}
		this.invertR = r;
		this.invertG = g;
		this.invertB = b;
	}

	@Override
	public float getZ() {
		return super.getZ() + offsetY;
	}

	@Override
	protected void drawImplementation() {
		if(angle == 0) {
			if(mode == Mode.STANDARD) {
				drawPort.drawTextureQuad(x + offsetX, y + offsetY + height, w, h, texX, texY, texW, texH, alpha, flipX);
			}
			else if(mode == Mode.INVERTED) {
				drawPort.drawTextureQuadInvert(x + offsetX, y + offsetY + height, w, h, texX, texY, texW, texH, invertR, invertG, invertB, alpha, flipX);
			}
			else if(mode == Mode.INSTRUMENT) {
				drawPort.drawInstrument(x + offsetX, y + offsetY + height, w, h, texX, texY, texW, texH, invertR, invertG, invertB);
			}
		}
		else {
			if(mode == Mode.STANDARD) {
				drawPort.drawTextureQuadRotated(x + offsetX, y + offsetY + height, w, h, texX, texY, texW, texH, angle, originX, originY, alpha);
			}
			else if(mode == Mode.INVERTED) {
				drawPort.drawTextureQuadRotatedIverted(x + offsetX, y + offsetY + height, w, h, texX, texY, texW, texH, invertR, invertG, invertB, angle, originX, originY, alpha);
			}
			else if(mode == Mode.INSTRUMENT) {
				drawPort.drawInstrument(x + offsetX, y + offsetY + height, w, h, texX, texY, texW, texH, invertR, invertG, invertB);
			}
		}
	}
}
