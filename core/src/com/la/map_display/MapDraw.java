package com.la.map_display;

import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;

public class MapDraw implements IMapDraw {
	private TextureDrawComponent drawComponents[][] = new TextureDrawComponent[8][8];
	private TextureDrawComponent position;
	private int linkX, linkY, counter;

	public MapDraw(GFXSystem gfxSystem, MapData mapData[][], int linkX, int linkY, boolean mapPresent, boolean compassPresent) {
		this.linkX = linkX;
		this.linkY = linkY;
		for(int y = 0; y < 8; ++y) {
			for(int x = 0; x < 8; ++x) {
				int id = getTileID(x, y, mapData, mapPresent, compassPresent);
				TextureDrawComponent drawComponent = gfxSystem.createTextureDrawComponent(2);
				drawComponent.setTexture((id % 11) * 10 + 4, id <= 10 ? 490 : 500, 8, 8);
				drawComponent.setSize(8, 8);
				drawComponents[y][x] = drawComponent;
			}
		}
		position = gfxSystem.createTextureDrawComponent(2);
		position.setTextureSize(8, 8);
		position.setSize(8, 8);
	}

	@Override
	public void draw(float x, float y) {
		counter -= 1;
		if(counter < 0) {
			counter = 10;
		}
		if(counter >= 5) {
			position.setTexturePosition(94, 500);
		}
		else {
			position.setTexturePosition(104, 500);
		}
		position.setPosition(x + linkX * 8, y + linkY * 8);
		for(int yIndex = 0; yIndex < 8; ++yIndex) {
			for(int xIndex = 0; xIndex < 8; ++xIndex) {
				drawComponents[yIndex][xIndex].setPosition(x + xIndex * 8, y + yIndex * 8);
			}
		}
	}
	@Override
	public void clean() {
		for(int y = 0; y < 8; ++y) {
			for(int x = 0; x < 8; ++x) {
				drawComponents[y][x].remove();
			}
		}
		position.remove();
	}

	private int getTileID(int x, int y, MapData mapData[][], boolean mapPresent, boolean compassPresent) {
		if(mapData[y][x] == null) {
			return 18;
		}
		else if(mapData[y][x].chestClosed == true && compassPresent == true) {
			return 9;
		}
		else if(mapData[y][x].bossPresent == true && compassPresent == true) {
			return 10;
		}
		else if(mapPresent == false) {
			return 18;
		}
		else if(mapData[y][x].visited == false) {
			return 7;
		}
		else if(
		   leftPathExist(x, y, mapData) == false &&
		   rightPathExist(x, y, mapData) == true &&
		   upPathExist(x, y, mapData) == false &&
		   downPathExist(x, y, mapData) == false) {
			return 0;
		}
		else if(
		   leftPathExist(x, y, mapData) == true &&
		   rightPathExist(x, y, mapData) == false &&
		   upPathExist(x, y, mapData) == false &&
		   downPathExist(x, y, mapData) == false) {
			return 1;
		}
		else if(
		   leftPathExist(x, y, mapData) == false &&
		   rightPathExist(x, y, mapData) == false &&
		   upPathExist(x, y, mapData) == true &&
		   downPathExist(x, y, mapData) == false) {
			return 12;
		}
		else if(
		   leftPathExist(x, y, mapData) == false &&
		   rightPathExist(x, y, mapData) == false &&
		   upPathExist(x, y, mapData) == false &&
		   downPathExist(x, y, mapData) == true) {
			return 11;
		}
		else if(
		   leftPathExist(x, y, mapData) == false &&
		   rightPathExist(x, y, mapData) == true &&
		   upPathExist(x, y, mapData) == false &&
		   downPathExist(x, y, mapData) == true) {
			return 2;
		}
		else if(
		   leftPathExist(x, y, mapData) == true &&
		   rightPathExist(x, y, mapData) == false &&
		   upPathExist(x, y, mapData) == false &&
		   downPathExist(x, y, mapData) == true) {
			return 3;
		}
		else if(
		   leftPathExist(x, y, mapData) == true &&
		   rightPathExist(x, y, mapData) == true &&
		   upPathExist(x, y, mapData) == true &&
		   downPathExist(x, y, mapData) == false) {
			return 5;
		}
		else if(
		   leftPathExist(x, y, mapData) == true &&
		   rightPathExist(x, y, mapData) == true &&
		   upPathExist(x, y, mapData) == false &&
		   downPathExist(x, y, mapData) == true) {
			return 6;
		}
		else if(
				leftPathExist(x, y, mapData) == true &&
		   rightPathExist(x, y, mapData) == true &&
		   upPathExist(x, y, mapData) == false &&
		   downPathExist(x, y, mapData) == false) {
			return 8;
		}
		else if(
		   leftPathExist(x, y, mapData) == false &&
		   rightPathExist(x, y, mapData) == true &&
		   upPathExist(x, y, mapData) == true &&
		   downPathExist(x, y, mapData) == false) {
			return 13;
		}
		else if(
		   leftPathExist(x, y, mapData) == true &&
		   rightPathExist(x, y, mapData) == false &&
		   upPathExist(x, y, mapData) == true &&
		   downPathExist(x, y, mapData) == false) {
			return 14;
		}
		else if(
		   leftPathExist(x, y, mapData) == true &&
		   rightPathExist(x, y, mapData) == true &&
		   upPathExist(x, y, mapData) == true &&
		   downPathExist(x, y, mapData) == true) {
			return 15;
		}
		else if(
		   leftPathExist(x, y, mapData) == false &&
		   rightPathExist(x, y, mapData) == true &&
		   upPathExist(x, y, mapData) == true &&
		   downPathExist(x, y, mapData) == true) {
			return 16;
		}
		else if(
		   leftPathExist(x, y, mapData) == true &&
		   rightPathExist(x, y, mapData) == false &&
		   upPathExist(x, y, mapData) == true &&
		   downPathExist(x, y, mapData) == true) {
			return 17;
		}
		else if(
		   leftPathExist(x, y, mapData) == false &&
		   rightPathExist(x, y, mapData) == false &&
		   upPathExist(x, y, mapData) == true &&
		   downPathExist(x, y, mapData) == true) {
			return 19;
		}
		return 4;
	}

	private boolean leftPathExist(int x, int y, MapData mapData[][]) {
		if(rangeCheck(x - 1, y) == true) {
//			if(nextRoomValid(mapData[y][x-1]) == false) {
//				return false;
//			}
			if(mapData[y][x].leftPath == true) {
				return true;
			}
		}
		return false;
	}
	private boolean rightPathExist(int x, int y, MapData mapData[][]) {
		if(rangeCheck(x + 1, y) == true) {
//			if(nextRoomValid(mapData[y][x+1]) == false) {
//				return false;
//			}
			if(mapData[y][x].rightPath == true) {
				return true;
			}
		}
		return false;
	}
	private boolean upPathExist(int x, int y, MapData mapData[][]) {
		if(rangeCheck(x, y - 1) == true) {
//			if(nextRoomValid(mapData[y-1][x]) == false) {
//				return false;
//			}
			if(mapData[y][x].upPath == true) {
				return true;
			}
		}
		return false;
	}
	private boolean downPathExist(int x, int y, MapData mapData[][]) {
		if(rangeCheck(x, y + 1) == true) {
//			if(nextRoomValid(mapData[y+1][x]) == false) {
//				return false;
//			}
			if(mapData[y][x].downPath == true) {
				return true;
			}
		}
		return false;
	}

	private boolean rangeCheck(int x, int y) {
		return x >= 0 && y >= 0 && x < 8 && y < 8;
	}
	private boolean nextRoomValid(MapData mapData) {
		return mapData != null && mapData.visited == true;
	}
}
