package com.la.map_display;

public interface IMapDisplaySystem {
	public interface MapDisplayEntryComponent {
		void setVisited();
		void chestOpened();
		void keyTaken();
		void bossDefeated();

		void setLeftPath(boolean open);
		void setRightPath(boolean open);
		void setUpPath(boolean open);
		void setDownPath(boolean open);
	}

	MapDisplayEntryComponent addMapEntry(
		int xMap, int yMap, int dungeonID, boolean chest, boolean key, boolean boss,
		boolean leftPath, boolean rightPath, boolean upPath, boolean downPath);

	void setLinkMapPosition(int x, int y);

	void keyCheck();
	void initializeMapDisplay(int dungeonID);
	void cleanMapDisplay();
	void drawMap(float x, float y);
}
