package com.engine.tile_map;

import com.engine.util.CollisionDetection;
import com.engine.util.ICollisionDetection;

public class TileMap {
	private TileData map[][];
	private ICollisionDetection collisionDetection = new CollisionDetection();

	public TileMap() {
		map = new TileData[8][10];
		for(int y = 0; y < 8; ++y) {
			for(int x = 0; x < 10; ++x) {
				map[y][x] = new TileData();
			}
		}
	}

	public void reset() {
		for(int y = 0; y < 8; ++y) {
			for(int x = 0; x < 10; ++x) {
				map[y][x].attribute = TileAttribute.EMPTY;
			}
		}
	}

	public boolean isPathOnXAxisClear(float centerX, float centerY, float targetCenterX) {
		boolean leftDir = false;
		if(targetCenterX < centerX) {
			leftDir = true;
		}
		int targetIndexX = (int) Math.floor(targetCenterX/16.0f);
		int indexX = (int) Math.floor(centerX/16.0f);
		int indexY = (int) Math.floor(centerY/16.0f);
		if(leftDir == false) {
			for(int x = indexX + 1; x <= targetIndexX; ++x) {
				if(map[indexY][x].attribute == TileAttribute.SOLID) {
					return false;
				}
			}
		}
		else {
			for(int x = indexX - 1; x >= targetIndexX; --x) {
				if(map[indexY][x].attribute == TileAttribute.SOLID) {
					return false;
				}
			}
		}
		return true;
	}
	public boolean isPathOnYAxisClear(float centerX, float centerY, float targetCenterY) {
		boolean upDir = false;
		if(targetCenterY < centerY) {
			upDir = true;
		}
		int targetIndexY = (int) Math.floor(targetCenterY/16.0f);
		int indexX = (int) Math.floor(centerX/16.0f);
		int indexY = (int) Math.floor(centerY/16.0f);
		if(upDir == false) {
			for(int y = indexY + 1; y <= targetIndexY; ++y) {
				if(map[y][indexX].attribute == TileAttribute.SOLID) {
					return false;
				}
			}
		}
		else {
			for(int y = indexY - 1; y >= targetIndexY; --y) {
				if(map[y][indexX].attribute == TileAttribute.SOLID) {
					return false;
				}
			}
		}
		return true;
	}
	public TileData getTileData(float centerX, float centerY) {
		while(centerX < 0) {
			centerX += 160;
		}
		while(centerY < 0) {
			centerY += 128;
		}
		while(centerX > 160) {
			centerX -= 160;
		}
		while(centerY > 128) {
			centerY -= 128;
		}
		int indexX = (int) Math.floor(centerX/16.0f);
		int indexY = (int) Math.floor(centerY/16.0f);
		return map[indexY][indexX];
	}
	public TileData getTileData(float centerX, float centerY, int offsetX, int offsetY) {
		int indexX = (int) Math.floor(centerX/16.0f);
		int indexY = (int) Math.floor(centerY/16.0f);
		int ix = indexX + offsetX;
		int iy = indexY + offsetY;
		if(ix >= 0 && iy >= 0 && ix < 10 && iy < 8) {
			return map[indexY][indexX];
		}
		return null;
	}
	public TileData getTileDataByIndex(int xIndex, int yIndex) {
		return map[yIndex][xIndex];
	}
	public boolean collisionCheck(float x, float y, float w, float h) {
		int indexX = (int) Math.floor((x + w/2.0f)/16.0f);
		int indexY = (int) Math.floor((y + h/2.0f)/16.0f);
		for(int yIter = indexY - 1; yIter <= indexY + 1; ++yIter) {
			for(int xIter = indexX - 1; xIter <= indexX + 1; ++xIter) {
				if(xIter >= 0 && yIter >= 0 && xIter < 10 && yIter < 8) {
					if(map[yIter][xIter].attribute == TileAttribute.SOLID) {
						if(collisionDetection.collisionDetect(x, y, w, h, xIter * 16, yIter * 16, 16, 16) == true) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public void draw() {
		for(int y = 0; y < 8; ++y) {
			String txt = "";
			for(int x = 0; x < 10; ++x) {
				TileAttribute attribute = map[y][x].attribute;
				if(attribute == TileAttribute.EMPTY) {
					txt += ".";
				}
				else if(attribute == TileAttribute.SOLID) {
					txt += "#";
				}
			}
			System.out.println(txt);
		}
	}
}
