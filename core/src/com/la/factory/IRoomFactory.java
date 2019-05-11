package com.la.factory;

import com.engine.game_object.IGameObject;
import com.la.game_objects.Chest.ChestCallback;
import com.la.game_objects.DestructibleWall.DestructibleWallCallback;
import com.la.game_objects.FloorButton.FloorButtonCallback;
import com.la.game_objects.KeyDoor.KeyDoorCallback;
import com.la.game_objects.KeyLock.KeyLockCallback;
import com.la.game_objects.MagicPowderSprinkle.MagicPowderDependency;
import com.la.game_objects.MovingBlock.MovingBlockCallback;
import com.la.game_objects.NightmareDoor.NightmareDoorCallback;
import com.la.game_objects.Stairs.StairsCallback;
import com.la.game_objects.Teleport.TeleportCallback;
import com.la.game_objects.effect.EnemyDefeatedBigSpark.EnemyDefeatedBigSparkCallback;
import com.la.game_objects.effect.EnemyDefeatedSmallSpark.EnemyDefeatedSmallSparkCallback;
import com.la.game_objects.effect.Poof2.Poof2Callback;
import com.la.game_objects.pickup.PickUpFlying.PickUpFlyingCallback;
import com.la.game_objects.pickup.PickupItemTopDown.PickupItemTopDownCallback;
import com.la.game_objects.pickup.SmallKeyCallback;

public interface IRoomFactory {
	IGameObject createBlueFloor(int x, int y);
	IGameObject createGrayFloor(int x, int y);
	IGameObject createGreenFloor(int x, int y);
	IGameObject createRoof(int x, int y);
	IGameObject createWalkingStairs(int x, int y);
	IGameObject createGreenStatue(int x, int y);
	IGameObject createStaticBlock(int x, int y);
	IGameObject createDestructableWall(int x, int y, int direction, DestructibleWallCallback callback);
	IGameObject createDestroyedWallHorizontal(int x, int y);
	IGameObject createGroundTorch(int x, int y);
	IGameObject createWallTorch(int x, int y, int direction);
	IGameObject createHole(int x, int y, int borderType);
	IGameObject createCrackedFloor(int x, int y);
	IGameObject createTeleportHole(int x, int y, int borderType, int roomID);
	IGameObject createAggressiveGroundTorch(int x, int y, int nextShoot);

	IGameObject createLeftWall(int x, int y);
	IGameObject createRightWall(int x, int y);
	IGameObject createUpWall(int x, int y);
	IGameObject createDownWall(int x, int y);

	IGameObject createLeftUpCorner1Wall(int x, int y);
	IGameObject createRightUpCorner1Wall(int x, int y);
	IGameObject createLeftDownCorner1Wall(int x, int y);
	IGameObject createRightDownCorner1Wall(int x, int y);

	IGameObject createLeftUpCorner2Wall(int x, int y);
	IGameObject createRightUpCorner2Wall(int x, int y);
	IGameObject createLeftDownCorner2Wall(int x, int y);
	IGameObject createRightDownCorner2Wall(int x, int y);

	IGameObject createLeftHorizontalHalfWallUpSegment(int x, int y);
	IGameObject createLeftHorizontalHalfWallDownSegment(int x, int y);
	IGameObject createRightHorizontalHalfWallUpSegment(int x, int y);
	IGameObject createRightHorizontalHalfWallDownSegment(int x, int y);

	IGameObject createUpVerticalHalfWallLeftSegment(int x, int y);
	IGameObject createUpVerticalHalfWallRightSegment(int x, int y);
	IGameObject createDownVerticalHalfWallLeftSegment(int x, int y);
	IGameObject createDownVerticalHalfWallRightSegment(int x, int y);

	IGameObject createLeftUpBarrier(int x, int y);
	IGameObject createRightUpBarrier(int x, int y);
	IGameObject createLeftDownBarrier(int x, int y);
	IGameObject createRightDownBarrier(int x, int y);

	IGameObject createLeftDownCornerBarrier(int x, int y);
	IGameObject createRightDownCornerBarrier(int x, int y);

	IGameObject createBarrierLeft(int x, int y);
	IGameObject createBarrierRight(int x, int y);
	IGameObject createBarrierUp(int x, int y);
	IGameObject createBarrierDown(int x, int y);

	IGameObject createBrickPlatform(int x, int y);
	IGameObject createColumn(int x, int y);
	IGameObject createRockPlatform(int x, int y);
	IGameObject createRockPlatformLeft(int x, int y);
	IGameObject createRockPlatformRight(int x, int y);
	IGameObject createRockPlatformDown(int x, int y);
	IGameObject createLadder(int x, int y, int ladderType);
	IGameObject createBackgroundWall1(int x, int y);
	IGameObject createBackgroundWall2(int x, int y);
	IGameObject createPlant(int x, int y);
	IGameObject createBackgroundTorch(int x, int y);
	IGameObject createChain(int x, int y);
	IGameObject createSkeletonBackground1(int x, int y);
	IGameObject createSkeletonBackground2(int x, int y);

	IGameObject createStairs(int x, int y, StairsCallback callback);
	IGameObject createTeleport(int x, int y, TeleportCallback callback);
	IGameObject createDoor(int x, int y, int direction);
	IGameObject createKeyDoor(int x, int y, int dungeonID, int direction, KeyDoorCallback callback);
	IGameObject createNightmareDoor(int x, int y, int dungeonID, int direction, NightmareDoorCallback callback);
	IGameObject createKeyLock(int x, int y, int dungeonID, KeyLockCallback callback);
	IGameObject createChest(int x, int y, boolean opened, boolean spawned, ChestCallback callback);
	IGameObject createOpenedChest(int x, int y);
	IGameObject createFloorButton(int x, int y, FloorButtonCallback callback);
	IGameObject createMovingBlock(int x, int y, MovingBlockCallback callback);
	IGameObject createOwlStatue(int x, int y, int dungeonID, String owlClue);

	IGameObject createOneWayDoorEntrance(int x, int y);
	IGameObject createOneWayDoorExit(int x, int y);
	IGameObject createPurpleCrystal(int x, int y);

	IGameObject createRupee(int x, int y);
	IGameObject createSmallHeart(int x, int y);
	IGameObject createSmallKey(int x, int y, int dungeonID, int height, SmallKeyCallback callback);
	IGameObject createFlyingSmallHeart(float x, float y, PickUpFlyingCallback callback);
	IGameObject createGuardianAcorn(float x, float y);
	IGameObject createPieceOfPower(float x, float y);
	IGameObject createFairy(float x, float y);
	IGameObject createBigHeart(float x, float y, PickupItemTopDownCallback callback);

	IGameObject createRupeePlatform(int x, int y);
	IGameObject createSmallHeartPlatform(int x, int y);
	IGameObject createFireball(int x, int y);
	IGameObject createBomb(int x, int y, float height);
	IGameObject createBombPlatform(int x, int y);
	IGameObject createGuardianAcornPlatform(float x, float y);
	IGameObject createPieceOfPowerPlatform(float x, float y);

	IGameObject createFullMoonCello(float x, float y);

	IGameObject createMagicPowderSprinkle(float x, float y, MagicPowderDependency dependency);

	IGameObject createSpark(float x, float y);
	IGameObject createPoof(int x, int y);
	IGameObject createPoof2(int x, int y, Poof2Callback callback);
	IGameObject createDebris(float x, float y, boolean left);
	IGameObject createFireballDefeated(int x, int y);
	IGameObject createEnemyDefeatedSmallSpark(int x, int y, EnemyDefeatedSmallSparkCallback callback);
	IGameObject createEnemyDefeatedBigSpark(int x, int y, EnemyDefeatedBigSparkCallback callback);
}
