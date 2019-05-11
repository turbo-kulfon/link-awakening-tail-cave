package com.engine.tile_map;

public class TileMapComponent {
	private TileMap tileMap;
	private TileData currentData;

	public TileMapComponent(float centerX, float centerY, TileMap tileMap) {
		this.tileMap = tileMap;
		currentData = tileMap.getTileData(centerX, centerY);
	}

	public void changePosition(float centerX, float centerY, TileAttribute oldTileAttribute, TileAttribute newTileAttribute) {
		currentData.attribute = oldTileAttribute;
		currentData = tileMap.getTileData(centerX, centerY);
		currentData.attribute = newTileAttribute;
	}
	public void setTileAttribute(TileAttribute attribute) {
		currentData.attribute = attribute;
	}
}
