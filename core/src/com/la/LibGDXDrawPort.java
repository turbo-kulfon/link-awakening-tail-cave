package com.la;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.engine.gfx.IDrawPort;

public class LibGDXDrawPort implements IDrawPort {
	private SpriteBatch batch;
	private Texture texture, fontTexture, numberTexture;
	private ShaderProgram solidColor, invertColor, instrumentColor;

	public LibGDXDrawPort(int w, int h) {
		OrthographicCamera camera = new OrthographicCamera();
		camera.setToOrtho(true, w, h);
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		texture = new Texture("gfx/tex.png");
		fontTexture = new Texture("gfx/lozfont.png");
		numberTexture = new Texture("gfx/loznumbers.png");

		solidColor = new ShaderProgram(Gdx.files.internal("shader/solidColor.vs"), Gdx.files.internal("shader/solidColor.fs"));
		invertColor = new ShaderProgram(Gdx.files.internal("shader/invertColor.vs"), Gdx.files.internal("shader/invertColor.fs"));
		instrumentColor = new ShaderProgram(Gdx.files.internal("shader/instrument_color.vs"), Gdx.files.internal("shader/instrument_color.fs"));
	}

	public void beginDraw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
	}
	public void endDraw() {
		batch.end();
	}

	@Override
	public void drawColorQuad(
			float x, float y, float w, float h,
			float r, float g, float b, float a) {
		batch.setColor(r, g, b, a);
		batch.draw(texture, x, y, w, h, 120, 50, 1, 1, false, false);
	}
	@Override
	public void drawTextureQuad(
			float x, float y, float w, float h,
			int texX, int texY, int texW, int texH,
			float alpha, boolean mirrorX) {
		batch.setColor(1, 1, 1, alpha);
		batch.draw(texture, x, y, w, h, texX, texY, texW, texH, mirrorX, true);
	}
	@Override
	public void drawTextureQuadInvert(
			float x, float y, float w, float h,
			int texX, int texY, int texW, int texH,
			float r, float g, float b,
			float alpha, boolean mirrorX) {
		batch.setShader(invertColor);
		batch.setColor(r, g, b, alpha);
		batch.draw(texture, x, y, w, h, texX, texY, texW, texH, mirrorX, true);
		batch.setShader(null);
	}
	@Override
	public void drawInstrument(float x, float y, float w, float h,
			int texX, int texY, int texW, int texH,
			float r, float g, float b) {
		batch.setShader(instrumentColor);
		batch.setColor(r, g, b, 1);
		batch.draw(texture, x, y, w, h, texX, texY, texW, texH, false, true);
		batch.setShader(null);
	}
	@Override
	public void drawTextureQuadRotated(
			float x, float y, float w, float h,
			int texX, int texY, int texW, int texH,
			float angle, float originX, float originY,
			float alpha) {
		batch.setColor(1, 1, 1, alpha);
		batch.draw(texture, x, y, originX, originY, w, h, 1, 1, angle, texX, texY, texW, texH, false, true);
	}
	@Override
	public void drawTextureQuadRotatedIverted(
			float x, float y, float w, float h,
			int texX, int texY, int texW, int texH,
			float r, float g, float b,
			float angle, float originX, float originY,
			float alpha) {
		batch.setShader(invertColor);
		batch.setColor(r, g, b, alpha);
		batch.draw(texture, x, y, originX, originY, w, h, 1, 1, angle, texX, texY, texW, texH, false, true);
		batch.setShader(null);
	}
	@Override
	public void drawText(float positionX, float positionY, String text) {
		int xMod = 0;
		int yMod = 0;
		batch.setShader(solidColor);
		batch.setColor(248.0f/255.0f, 248.0f/255.0f, 168.0f/255.0f, 1);
		for(int i = 0; i < text.length(); ++i) {
			char code = text.charAt(i);
			if(code != '|') {
				if(code >= 'A' && code <= 'Z') {
					int letter = code - 'A';
					int x = letter * 8;
					batch.draw(fontTexture, positionX + xMod, positionY + yMod, 8, 8, x, 0, 8, 8, false, true);
				}
				else if(code >= 'a' && code <= 'z') {
					int letter = code - 'a';
					int x = letter * 8;
					batch.draw(fontTexture, positionX + xMod, positionY + yMod, 8, 8, x, 8, 8, 8, false, true);
				}
				else if(code >= '0' && code <= '9') {
					int number = code - '0';
					int x = number * 8;
					batch.draw(fontTexture, positionX + xMod, positionY + yMod, 8, 8, x, 16, 8, 8, false, true);
				}
				// apostrof
				else if(code == '\'') {
					batch.draw(fontTexture, positionX + xMod, positionY + yMod, 8, 8, 80, 16, 8, 8, false, true);
				}
				else if(code == ',') {
					batch.draw(fontTexture, positionX + xMod, positionY + yMod + 5, 8, 8, 80, 16, 8, 8, false, true);
				}
				else if(code == '.') {
					batch.draw(fontTexture, positionX + xMod, positionY + yMod, 8, 8, 112, 16, 8, 8, false, true);
				}
				else if(code == '!') {
					batch.draw(fontTexture, positionX + xMod, positionY + yMod, 8, 8, 96, 16, 8, 8, false, true);
				}
				else if(code == '?') {
					batch.draw(fontTexture, positionX + xMod, positionY + yMod, 8, 8, 88, 16, 8, 8, false, true);
				}
				else if(code == '-') {
					batch.draw(fontTexture, positionX + xMod, positionY + yMod, 8, 8, 104, 16, 8, 8, false, true);
				}
				xMod += 8;
			}
			else  {
				xMod = -8;
				yMod += 16;
			}
		}
		batch.setShader(null);
		batch.setColor(1, 1, 1, 1);
	}
	@Override
	public void drawNumber(float positionX, float positionY, String text) {
		int xMod = 0;
		batch.setColor(1, 1, 1, 1);
		for(int i = 0; i < text.length(); ++i) {
			char code = text.charAt(i);
			if(code >= '0' && code <= '9') {
				int number = code - '0';
				int x = number * 8;
				batch.draw(numberTexture, positionX + xMod, positionY, 8, 8, x, 0, 8, 8, false, true);
			}
			xMod += 8;
		}
	}

	public void dispose() {
		batch.dispose();
		texture.dispose();
		fontTexture.dispose();

		solidColor.dispose();
		invertColor.dispose();
		instrumentColor.dispose();
	}
}
