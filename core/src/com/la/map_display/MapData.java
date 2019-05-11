package com.la.map_display;

public class MapData {
	public int dungeonID;
	public boolean leftPath, rightPath, upPath, downPath;
	public boolean chestClosed, keyPresent, bossPresent, visited;

	public MapData(int dungeonID,
			boolean leftPath, boolean rightPath, boolean upPath, boolean downPath,
			boolean chestClosed, boolean keyTaken, boolean boss) {
		this.dungeonID = dungeonID;
		this.leftPath = leftPath;
		this.rightPath = rightPath;
		this.upPath = upPath;
		this.downPath = downPath;
		this.chestClosed = chestClosed;
		this.keyPresent = keyTaken;
		this.bossPresent = boss;
		visited = false;
	}
}
