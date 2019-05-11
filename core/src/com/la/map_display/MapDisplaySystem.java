package com.la.map_display;

import com.engine.gfx.GFXSystem;
import com.engine.sound.SoundSystem;
import com.la.equipment.IEquipmentSystem;

public class MapDisplaySystem implements IMapDisplaySystem {
	private GFXSystem gfxSystem;
	private SoundSystem soundSystem;
	private IEquipmentSystem equipmentSystem;
	private MapData mapData[][] = new MapData[8][8];

	private IMapDraw mapDraw;
	private int linkX, linkY;

	public MapDisplaySystem(
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			IEquipmentSystem equipmentSystem) {
		this.gfxSystem = gfxSystem;
		this.soundSystem = soundSystem;
		this.equipmentSystem = equipmentSystem;
	}

	@Override
	public MapDisplayEntryComponent addMapEntry(
			int xMap, int yMap, int dungeonID, boolean chest, boolean key, boolean boss,
			boolean leftPath, boolean rightPath, boolean upPath, boolean downPath) {
		MapData mapDataEntry = new MapData(dungeonID, leftPath, rightPath, upPath, downPath, chest, key, boss);
		mapData[yMap][xMap] = mapDataEntry;
		return new MapDisplayEntryComponentStandard(mapDataEntry);
	}

	@Override
	public void setLinkMapPosition(int x, int y) {
		linkX = x;
		linkY = y;
	}

	@Override
	public void keyCheck() {
		if(equipmentSystem.isCompassPresent(mapData[linkY][linkX].dungeonID) == true &&
		   mapData[linkY][linkX].keyPresent == true) {
			soundSystem.playKeyPresent();
		}
	}
	@Override
	public void initializeMapDisplay(int dungeonID) {
		mapDraw = new MapDraw(
				gfxSystem, mapData,
				linkX, linkY,
				equipmentSystem.isDungeonMapPresent(dungeonID),
				equipmentSystem.isCompassPresent(dungeonID));
	}
	@Override
	public void cleanMapDisplay() {
		mapDraw.clean();
	}
	@Override
	public void drawMap(float x, float y) {
		mapDraw.draw(x, y);
	}
}
