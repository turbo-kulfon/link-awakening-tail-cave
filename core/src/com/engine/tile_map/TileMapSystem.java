package com.engine.tile_map;

import com.engine.util.ICoordinate.Vector;

public class TileMapSystem {
	private TileMap tileMap = new TileMap();

	public TileMapComponent createComponent(float centerX, float centerY) {
		return new TileMapComponent(centerX, centerY, tileMap);
	}

	public void reset() {
		tileMap.reset();
	}
	public boolean isTileSolid(float centerX, float centerY) {
		TileData tileData = tileMap.getTileData(centerX, centerY);
		if(tileData != null) {
			return tileData.attribute == TileAttribute.SOLID;
		}
		return false;
	}
	public boolean isTileSolid(float centerX, float centerY, int xOffset, int yOffset) {
		TileData tileData = tileMap.getTileData(centerX, centerY, xOffset, yOffset);
		if(tileData != null) {
			return tileData.attribute == TileAttribute.SOLID;
		}
		return false;
	}
	public boolean isPathOnXAxisClear(float centerX, float centerY, float targetCenterX) {
		return tileMap.isPathOnXAxisClear(centerX, centerY, targetCenterX);
	}
	public boolean isPathOnYAxisClear(float centerX, float centerY, float targetCenterY) {
		return tileMap.isPathOnYAxisClear(centerX, centerY, targetCenterY);
	}

	public Vector getCoordinates(float centerX, float centerY) {
		int indexX = (int) Math.floor(centerX/16.0f);
		int indexY = (int) Math.floor(centerY/16.0f);
		return new Vector(indexX, indexY);
	}
	public boolean collisionCheck(float x, float y, float w, float h) {
		return tileMap.collisionCheck(x, y, w, h);
	}
}
