package com.la.room;

import com.la.factory.IRoomFactory;

public class RoomCreator implements IRoomCreator {
	private IRoomFactory roomFactory;

	public RoomCreator(IRoomFactory roomFactory) {
		this.roomFactory = roomFactory;
	}

	@Override
	public void createRoom(int[][] mapData, int offsetX, int offsetY) {
		for(int y = 0; y < 8; ++y) {
			for(int x = 0; x < 10; ++x) {
				int positionX = x * 16 + offsetX;
				int positionY = y * 16 + offsetY;
				if(mapData[y][x] == 0) {
					roomFactory.createBlueFloor(positionX, positionY);
				}
				else if(mapData[y][x] == 1) {
					roomFactory.createGrayFloor(positionX, positionY);
				}
				else if(mapData[y][x] == 2) {
					roomFactory.createGroundTorch(positionX, positionY);
				}
				else if(mapData[y][x] == 3) {
					roomFactory.createWallTorch(positionX, positionY, 0);
				}
				else if(mapData[y][x] == 4) {
					roomFactory.createWallTorch(positionX, positionY, 1);
				}
				else if(mapData[y][x] == 5) {
					roomFactory.createWallTorch(positionX, positionY, 2);
				}
				else if(mapData[y][x] == 6) {
					roomFactory.createWallTorch(positionX, positionY, 3);
				}
				else if(mapData[y][x] == 7) {
					roomFactory.createGreenFloor(positionX, positionY);
				}
				else if(mapData[y][x] == 8) {
					roomFactory.createWalkingStairs(positionX, positionY);
				}

				else if(mapData[y][x] == 10) {
					roomFactory.createUpWall(positionX, positionY);
				}
				else if(mapData[y][x] == 11) {
					roomFactory.createLeftWall(positionX, positionY);
				}
				else if(mapData[y][x] == 12) {
					roomFactory.createRightWall(positionX, positionY);
				}
				else if(mapData[y][x] == 13) {
					roomFactory.createDownWall(positionX, positionY);
				}

				else if(mapData[y][x] == 14) {
					roomFactory.createLeftUpCorner2Wall(positionX, positionY);
				}
				else if(mapData[y][x] == 15) {
					roomFactory.createRightUpCorner2Wall(positionX, positionY);
				}
				else if(mapData[y][x] == 16) {
					roomFactory.createRightDownCorner2Wall(positionX, positionY);
				}
				else if(mapData[y][x] == 17) {
					roomFactory.createLeftDownCorner2Wall(positionX, positionY);
				}

				else if(mapData[y][x] == 18) {
					roomFactory.createLeftUpCorner1Wall(positionX, positionY);
				}
				else if(mapData[y][x] == 19) {
					roomFactory.createRightUpCorner1Wall(positionX, positionY);
				}
				else if(mapData[y][x] == 20) {
					roomFactory.createLeftDownCorner1Wall(positionX, positionY);
				}
				else if(mapData[y][x] == 21) {
					roomFactory.createRightDownCorner1Wall(positionX, positionY);
				}

				else if(mapData[y][x] == 22) {
					roomFactory.createRoof(positionX, positionY);
				}

				else if(mapData[y][x] == 23) {
					roomFactory.createLeftHorizontalHalfWallUpSegment(positionX, positionY);
				}
				else if(mapData[y][x] == 24) {
					roomFactory.createLeftHorizontalHalfWallDownSegment(positionX, positionY);
				}
				else if(mapData[y][x] == 25) {
					roomFactory.createRightHorizontalHalfWallUpSegment(positionX, positionY);
				}
				else if(mapData[y][x] == 26) {
					roomFactory.createRightHorizontalHalfWallDownSegment(positionX, positionY);
				}
				else if(mapData[y][x] == 27) {
					roomFactory.createUpVerticalHalfWallLeftSegment(positionX, positionY);
				}
				else if(mapData[y][x] == 28) {
					roomFactory.createUpVerticalHalfWallRightSegment(positionX, positionY);
				}
				else if(mapData[y][x] == 29) {
					roomFactory.createDownVerticalHalfWallLeftSegment(positionX, positionY);
				}
				else if(mapData[y][x] == 30) {
					roomFactory.createDownVerticalHalfWallRightSegment(positionX, positionY);
				}

				else if(mapData[y][x] == 31) {
					roomFactory.createLeftUpBarrier(positionX, positionY);
				}
				else if(mapData[y][x] == 32) {
					roomFactory.createRightUpBarrier(positionX, positionY);
				}
				else if(mapData[y][x] == 33) {
					roomFactory.createLeftDownBarrier(positionX, positionY);
				}
				else if(mapData[y][x] == 34) {
					roomFactory.createRightDownBarrier(positionX, positionY);
				}

				else if(mapData[y][x] == 35) {
					roomFactory.createLeftDownCornerBarrier(positionX, positionY);
				}
				else if(mapData[y][x] == 36) {
					roomFactory.createRightDownCornerBarrier(positionX, positionY);
				}

				else if(mapData[y][x] == 41) {
					roomFactory.createBarrierUp(positionX, positionY);
				}
				else if(mapData[y][x] == 42) {
					roomFactory.createBarrierDown(positionX, positionY);
				}
				else if(mapData[y][x] == 43) {
					roomFactory.createBarrierLeft(positionX, positionY);
				}
				else if(mapData[y][x] == 44) {
					roomFactory.createBarrierRight(positionX, positionY);
				}

				else if(mapData[y][x] == 50) {
					roomFactory.createGreenStatue(positionX, positionY);
				}
				else if(mapData[y][x] == 51) {
					roomFactory.createStaticBlock(positionX, positionY);
				}
				else if(mapData[y][x] == 52) {
					roomFactory.createDestroyedWallHorizontal(positionX, positionY);
				}
				else if(mapData[y][x] == 88) {
					roomFactory.createHole(positionX, positionY, -1);
				}
				else if(mapData[y][x] == 89) {
					roomFactory.createCrackedFloor(positionX, positionY);
				}
				else if(mapData[y][x] == 90) {
					roomFactory.createHole(positionX, positionY, 1);
				}
				else if(mapData[y][x] == 91) {
					roomFactory.createHole(positionX, positionY, 2);
				}
				else if(mapData[y][x] == 92) {
					roomFactory.createHole(positionX, positionY, 0);
				}

				else if(mapData[y][x] == 101) {
					roomFactory.createBrickPlatform(positionX, positionY);
				}
				else if(mapData[y][x] == 102) {
					roomFactory.createColumn(positionX, positionY);
				}
				else if(mapData[y][x] == 110) {
					roomFactory.createRockPlatform(positionX, positionY);
				}
				else if(mapData[y][x] == 111) {
					roomFactory.createRockPlatformLeft(positionX, positionY);
				}
				else if(mapData[y][x] == 112) {
					roomFactory.createRockPlatformRight(positionX, positionY);
				}
				else if(mapData[y][x] == 113) {
					roomFactory.createRockPlatformDown(positionX, positionY);
				}
				else if(mapData[y][x] == 114) {
					roomFactory.createBackgroundWall1(positionX, positionY);
				}
				else if(mapData[y][x] == 115) {
					roomFactory.createBackgroundWall2(positionX, positionY);
				}
				else if(mapData[y][x] == 116) {
					roomFactory.createBackgroundTorch(positionX, positionY);
				}
				else if(mapData[y][x] == 117) {
					roomFactory.createPlant(positionX, positionY);
				}
				else if(mapData[y][x] == 118) {
					roomFactory.createSkeletonBackground1(positionX, positionY);
				}
				else if(mapData[y][x] == 119) {
					roomFactory.createSkeletonBackground2(positionX, positionY);
				}
				else if(mapData[y][x] == 120) {
					roomFactory.createLadder(positionX, positionY, 0);
				}
				else if(mapData[y][x] == 121) {
					roomFactory.createLadder(positionX, positionY, 1);
				}
				else if(mapData[y][x] == 122) {
					roomFactory.createLadder(positionX, positionY, -1);
				}
				else if(mapData[y][x] == 123) {
					roomFactory.createChain(positionX, positionY);
				}
			}
		}
	}
}
