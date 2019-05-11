package com.la.map_display;

import com.la.map_display.IMapDisplaySystem.MapDisplayEntryComponent;

public class MapDisplayEntryComponentStandard implements MapDisplayEntryComponent {
	protected MapData mapData;

	public MapDisplayEntryComponentStandard(
			MapData mapData) {
		this.mapData = mapData;
	}

	@Override
	public void setVisited() {
		mapData.visited = true;
	}
	@Override
	public void chestOpened() {
		mapData.chestClosed = false;
	}
	@Override
	public void keyTaken() {
		mapData.keyPresent = false;
	}
	@Override
	public void bossDefeated() {
		mapData.bossPresent = false;
	}

	@Override
	public void setLeftPath(boolean open) {
		mapData.leftPath = open;
	}
	@Override
	public void setRightPath(boolean open) {
		mapData.rightPath = open;
	}
	@Override
	public void setUpPath(boolean open) {
		mapData.upPath = open;
	}
	@Override
	public void setDownPath(boolean open) {
		mapData.downPath = open;
	}
}
