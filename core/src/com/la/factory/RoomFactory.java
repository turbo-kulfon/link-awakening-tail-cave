package com.la.factory;

import com.engine.aspect.IAspectSystem;
import com.engine.double_buffer.IDoubleBuffer;
import com.engine.game_object.IGameObject;
import com.engine.game_object.IGameObjectSystem;
import com.engine.gfx.GFXSystem;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.SpatialSystem;
import com.engine.tile_map.TileMapSystem;
import com.engine.util.IRNG;
import com.la.equipment.IEquipmentSystem;
import com.la.game_objects.AggressiveGroundTorch;
import com.la.game_objects.BombPlatform;
import com.la.game_objects.BombTopDown;
import com.la.game_objects.Chest;
import com.la.game_objects.Chest.ChestCallback;
import com.la.game_objects.DestructibleWall;
import com.la.game_objects.DestructibleWall.DestructibleWallCallback;
import com.la.game_objects.Door;
import com.la.game_objects.Fairy;
import com.la.game_objects.Fireball;
import com.la.game_objects.FloorButton;
import com.la.game_objects.FloorButton.FloorButtonCallback;
import com.la.game_objects.KeyDoor;
import com.la.game_objects.KeyDoor.KeyDoorCallback;
import com.la.game_objects.KeyLock;
import com.la.game_objects.KeyLock.KeyLockCallback;
import com.la.game_objects.Ladder;
import com.la.game_objects.MagicPowderSprinkle;
import com.la.game_objects.MagicPowderSprinkle.MagicPowderDependency;
import com.la.game_objects.MovingBlock;
import com.la.game_objects.MovingBlock.MovingBlockCallback;
import com.la.game_objects.NightmareDoor;
import com.la.game_objects.NightmareDoor.NightmareDoorCallback;
import com.la.game_objects.OneWayDoor;
import com.la.game_objects.OneWayDoorExit;
import com.la.game_objects.Roof;
import com.la.game_objects.Stairs;
import com.la.game_objects.Stairs.StairsCallback;
import com.la.game_objects.Teleport;
import com.la.game_objects.Teleport.TeleportCallback;
import com.la.game_objects.WalkingStairs;
import com.la.game_objects.Wall;
import com.la.game_objects.Wall.WallCallback;
import com.la.game_objects.effect.Debris;
import com.la.game_objects.effect.EnemyDefeatedBigSpark;
import com.la.game_objects.effect.EnemyDefeatedBigSpark.EnemyDefeatedBigSparkCallback;
import com.la.game_objects.effect.EnemyDefeatedSmallSpark;
import com.la.game_objects.effect.EnemyDefeatedSmallSpark.EnemyDefeatedSmallSparkCallback;
import com.la.game_objects.effect.FireballDefeated;
import com.la.game_objects.effect.Poof;
import com.la.game_objects.effect.Poof2;
import com.la.game_objects.effect.Poof2.Poof2Callback;
import com.la.game_objects.effect.Spark;
import com.la.game_objects.link.ILinkData;
import com.la.game_objects.pickup.PickUpFlying;
import com.la.game_objects.pickup.PickUpFlying.PickUpFlyingCallback;
import com.la.game_objects.pickup.PickupItemPlatform;
import com.la.game_objects.pickup.PickupItemTopDown;
import com.la.game_objects.pickup.PickupItemTopDown.PickupItemTopDownCallback;
import com.la.game_objects.pickup.SmallKeyCallback;
import com.la.game_objects.pickup.instrument.Instrument;
import com.la.game_objects.tile.CrackedFloor;
import com.la.game_objects.tile.Floor;
import com.la.game_objects.tile.GroundTorch;
import com.la.game_objects.tile.Hole;
import com.la.game_objects.tile.HorizontalHalfWallDownSegment;
import com.la.game_objects.tile.HorizontalHalfWallUpSegment;
import com.la.game_objects.tile.LeftDownHalfCorner;
import com.la.game_objects.tile.LeftDownHalfWall;
import com.la.game_objects.tile.LeftUpHalfWall;
import com.la.game_objects.tile.OwlStatue;
import com.la.game_objects.tile.PlatformSectionTorch;
import com.la.game_objects.tile.PurpleCrystal;
import com.la.game_objects.tile.RightDownHalfCorner;
import com.la.game_objects.tile.RightDownHalfWall;
import com.la.game_objects.tile.RightUpHalfWall;
import com.la.game_objects.tile.TeleportHole;
import com.la.game_objects.tile.VerticalHalfWallLeftSegment;
import com.la.game_objects.tile.VerticalHalfWallRightSegment;
import com.la.game_objects.tile.WallTorch;
import com.la.power_up_item_system.PowerUpItemSystem;
import com.la.room.transition.HoleTeleportSystem;

public class RoomFactory implements IRoomFactory {
	private class DefaultWallCallback implements WallCallback {
		@Override
		public void onWallHit(float x, float y) {
			IGameObject object = new Spark((int)x, (int)y - 3, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
			addToSystem(object);
			soundSystem.swordWallCollision();
		}
	}

	private IGameObjectSystem gameObjectSystem;
	private GFXSystem gfxSystem;
	private SoundSystem soundSystem;
	private SpatialSystem spatialSystem;
	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;
	private IEquipmentSystem equipmentSystem;
	private HoleTeleportSystem holeTeleportSystem;
	private TileMapSystem tileMapSystem;
	private ILinkData linkData;
	private IGameStateFactory gameStateFactory;
	private PowerUpItemSystem powerUpItemSystem;
	private IDoubleBuffer<IGameObject> roomObjects;
	private IRNG rng;

	public RoomFactory(
			IGameObjectSystem gameObjectSystem,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			IEquipmentSystem equipmentSystem,
			TileMapSystem tileMapSystem,
			IDoubleBuffer<IGameObject> roomObjects,
			PowerUpItemSystem powerUpItemSystem,
			IRNG rng) {
		this.gameObjectSystem = gameObjectSystem;
		this.gfxSystem = gfxSystem;
		this.soundSystem = soundSystem;
		this.spatialSystem = spatialSystem;
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.equipmentSystem = equipmentSystem;
		this.tileMapSystem = tileMapSystem;
		this.roomObjects = roomObjects;
		this.powerUpItemSystem = powerUpItemSystem;
		this.rng = rng;
	}

	public void setHoleTeleportSystem(
			HoleTeleportSystem holeTeleportSystem,
			ILinkData linkData,
			IGameStateFactory gameStateFactory) {
		this.holeTeleportSystem = holeTeleportSystem;
		this.linkData = linkData;
		this.gameStateFactory = gameStateFactory;
	}

	@Override
	public IGameObject createBlueFloor(int x, int y) {
		IGameObject object = new Floor(x, y, 120, 16, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createGrayFloor(int x, int y) {
		IGameObject object = new Floor(x, y, 152, 32, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createGreenFloor(int x, int y) {
		IGameObject object = new Floor(x, y, 302, 64, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createRoof(int x, int y) {
		IGameObject object = new Roof(x, y, 251.0f/255.0f, 48.0f/255.0f, 112.0f/255.0f, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createWalkingStairs(int x, int y) {
		IGameObject object = new WalkingStairs(x, y, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createGreenStatue(int x, int y) {
		IGameObject object = new Wall(x, y, 184, 0, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createStaticBlock(int x, int y) {
		IGameObject object = new Wall(x, y, 312, 16, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createDestructableWall(int x, int y, int direction, DestructibleWallCallback callback) {
		IGameObject object = new DestructibleWall(x, y, direction, false, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DestructibleWallCallback() {
			@Override
			public void onWallHit(float x, float y) {
				IGameObject object = new Spark((int)x, (int)y - 3, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
				addToSystem(object);
				soundSystem.swordBombableWallCollision();
				callback.onWallHit(x, y);
			}
			@Override
			public void onDestroy() {
				callback.onDestroy();
			}
		});
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createDestroyedWallHorizontal(int x, int y) {
		IGameObject object = new DestructibleWall(x, y, 0, true, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DestructibleWallCallback() {
			@Override public void onWallHit(float x, float y) {}
			@Override public void onDestroy() {}
		});
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createGroundTorch(int x, int y) {
		IGameObject object = new GroundTorch(x, y, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createWallTorch(int x, int y, int direction) {
		IGameObject object = new WallTorch(x, y, direction, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createHole(int x, int y, int borderType) {
		IGameObject object = new Hole(x, y, borderType, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createCrackedFloor(int x, int y) {
		IGameObject object = new CrackedFloor(x, y, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createTeleportHole(int x, int y, int borderType, int roomID) {
		IGameObject object = new TeleportHole(x, y, borderType, roomID, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, holeTeleportSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createAggressiveGroundTorch(int x, int y, int nextShoot) {
		IGameObject object = new AggressiveGroundTorch(x, y, nextShoot, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, this);
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createLeftWall(int x, int y) {
		IGameObject object = new Wall(x, y, 104, 16, 0, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createRightWall(int x, int y) {
		IGameObject object = new Wall(x, y, 136, 16, 1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createUpWall(int x, int y) {
		IGameObject object = new Wall(x, y, 120, 0, 2, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createDownWall(int x, int y) {
		IGameObject object = new Wall(x, y, 120, 32, 3, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createLeftUpCorner1Wall(int x, int y) {
		IGameObject object = new Wall(x, y, 104, 0, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createRightUpCorner1Wall(int x, int y) {
		IGameObject object = new Wall(x, y, 136, 0, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createLeftDownCorner1Wall(int x, int y) {
		IGameObject object = new Wall(x, y, 104, 32, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createRightDownCorner1Wall(int x, int y) {
		IGameObject object = new Wall(x, y, 136, 32, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createLeftUpCorner2Wall(int x, int y) {
		IGameObject object = new Wall(x, y, 168, 16, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createRightUpCorner2Wall(int x, int y) {
		IGameObject object = new Wall(x, y, 152, 16, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createLeftDownCorner2Wall(int x, int y) {
		IGameObject object = new Wall(x, y, 168, 0, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createRightDownCorner2Wall(int x, int y) {
		IGameObject object = new Wall(x, y, 152, 0, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createLeftHorizontalHalfWallUpSegment(int x, int y) {
		IGameObject object = new HorizontalHalfWallUpSegment(x, y, 232, 0, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createLeftHorizontalHalfWallDownSegment(int x, int y) {
		IGameObject object = new HorizontalHalfWallDownSegment(x, y, 248, 0, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createRightHorizontalHalfWallUpSegment(int x, int y) {
		IGameObject object = new HorizontalHalfWallUpSegment(x, y, 200, 0, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createRightHorizontalHalfWallDownSegment(int x, int y) {
		IGameObject object = new HorizontalHalfWallDownSegment(x, y, 216, 0, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createUpVerticalHalfWallLeftSegment(int x, int y) {
		IGameObject object = new VerticalHalfWallLeftSegment(x, y, 238, 48, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createUpVerticalHalfWallRightSegment(int x, int y) {
		IGameObject object = new VerticalHalfWallRightSegment(x, y, 254, 48, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createDownVerticalHalfWallLeftSegment(int x, int y) {
		IGameObject object = new VerticalHalfWallLeftSegment(x, y, 270, 48, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createDownVerticalHalfWallRightSegment(int x, int y) {
		IGameObject object = new VerticalHalfWallRightSegment(x, y, 286, 48, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createLeftUpBarrier(int x, int y) {
		IGameObject object = new LeftUpHalfWall(x, y, 334, 48, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createLeftDownBarrier(int x, int y) {
		IGameObject object = new LeftDownHalfWall(x, y, 334, 64, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createRightUpBarrier(int x, int y) {
		IGameObject object = new RightUpHalfWall(x, y, 350, 48, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createRightDownBarrier(int x, int y) {
		IGameObject object = new RightDownHalfWall(x, y, 350, 64, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createLeftDownCornerBarrier(int x, int y) {
		IGameObject object = new LeftDownHalfCorner(x, y, 366, 64, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createRightDownCornerBarrier(int x, int y) {
		IGameObject object = new RightDownHalfCorner(x, y, 366, 48, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createBarrierLeft(int x, int y) {
		IGameObject object = new VerticalHalfWallLeftSegment(x, y, 360, 32, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createBarrierRight(int x, int y) {
		IGameObject object = new VerticalHalfWallRightSegment(x, y, 344, 32, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createBarrierUp(int x, int y) {
		IGameObject object = new HorizontalHalfWallUpSegment(x, y, 302, 48, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createBarrierDown(int x, int y) {
		IGameObject object = new HorizontalHalfWallDownSegment(x, y, 318, 48, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem);
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createBrickPlatform(int x, int y) {
		IGameObject object = new Wall(x, y, 432, 432, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createColumn(int x, int y) {
		IGameObject object = new Wall(x, y, 448, 432, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createRockPlatform(int x, int y) {
		IGameObject object = new Wall(x, y, 480, 432, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createRockPlatformLeft(int x, int y) {
		IGameObject object = new Wall(x, y, 496, 432, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createRockPlatformRight(int x, int y) {
		IGameObject object = new Wall(x, y, 464, 432, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createRockPlatformDown(int x, int y) {
		IGameObject object = new Wall(x, y, 464, 448, -1, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, new DefaultWallCallback());
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createLadder(int x, int y, int ladderType) {
		IGameObject object = new Ladder(x, y, ladderType, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createBackgroundWall1(int x, int y) {
		IGameObject object = new Floor(x, y, 432, 480, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createBackgroundWall2(int x, int y) {
		IGameObject object = new Floor(x, y, 448, 480, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createPlant(int x, int y) {
		IGameObject object = new Floor(x, y, 464, 480, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createBackgroundTorch(int x, int y) {
		IGameObject object = new PlatformSectionTorch(x, y, gfxSystem, spatialSystem, uniqueIDManager, aspectSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createChain(int x, int y) {
		IGameObject object = new Floor(x, y, 496, 448, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createSkeletonBackground1(int x, int y) {
		IGameObject object = new Floor(x, y, 432, 448, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createSkeletonBackground2(int x, int y) {
		IGameObject object = new Floor(x, y, 448, 448, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createStairs(int x, int y, StairsCallback callback) {
		IGameObject object = new Stairs(x, y, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, callback);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createTeleport(int x, int y, TeleportCallback callback) {
		IGameObject object = new Teleport(x, y, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, callback);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createDoor(int x, int y, int direction) {
		IGameObject object = new Door(x, y, direction, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createKeyDoor(int x, int y, int dungeonID, int direction, KeyDoorCallback callback) {
		IGameObject object = new KeyDoor(x, y, dungeonID, direction, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, equipmentSystem, callback);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createNightmareDoor(int x, int y, int dungeonID, int direction, NightmareDoorCallback callback) {
		IGameObject object = new NightmareDoor(x, y, dungeonID, direction, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, gameStateFactory, equipmentSystem, callback);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createKeyLock(int x, int y, int dungeonID, KeyLockCallback callback) {
		IGameObject object = new KeyLock(x, y, dungeonID, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, equipmentSystem, callback);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createChest(int x, int y, boolean opened, boolean spawned, ChestCallback callback) {
		IGameObject object = new Chest(x, y, opened, spawned, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, linkData, callback, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createOpenedChest(int x, int y) {
		IGameObject object = new Chest(x, y, true, false, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, null, null, tileMapSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createFloorButton(int x, int y, FloorButtonCallback callback) {
		IGameObject object = new FloorButton(x, y, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, callback);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createMovingBlock(int x, int y, MovingBlockCallback callback) {
		IGameObject object = new MovingBlock(x, y, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, tileMapSystem, callback);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createOwlStatue(int x, int y, int dungeonID, String owlClue) {
		IGameObject object = new OwlStatue(x, y, dungeonID, owlClue, gfxSystem, spatialSystem, uniqueIDManager, aspectSystem, equipmentSystem, (text)-> {
			gameStateFactory.createTextState(text);
		});
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createOneWayDoorEntrance(int x, int y) {
		IGameObject object = new OneWayDoor(x, y, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createOneWayDoorExit(int x, int y) {
		IGameObject object = new OneWayDoorExit(x, y, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createPurpleCrystal(int x, int y) {
		IGameObject object = new PurpleCrystal(x, y, gfxSystem, soundSystem, spatialSystem, this, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createRupee(int x, int y) {
		IGameObject object = new PickupItemTopDown(x, y, 7, 14, 171, 64, 171, 64, 0, true, true, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, new PickupItemTopDownCallback() {
			@Override
			public boolean onItemTake() {
				equipmentSystem.addRupee();
				return true;
			}
			@Override public void onBounce() {}
		});
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createSmallHeart(int x, int y) {
		IGameObject object = new PickupItemTopDown(x, y, 7, 7, 133, 64, 133, 64, 0, true, true, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, new PickupItemTopDownCallback() {
			@Override
			public boolean onItemTake() {
				equipmentSystem.addHeart();
				soundSystem.takePickupItem();
				return true;
			}
			@Override public void onBounce() {}
		});
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createSmallKey(int x, int y, int dungeonID, int height, SmallKeyCallback callback) {
		IGameObject object = new PickupItemTopDown(x, y, 7, 13, 164, 64, 164, 64, height, false, false, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, new PickupItemTopDownCallback() {
			@Override
			public boolean onItemTake() {
				equipmentSystem.addKey(dungeonID);
				callback.onTake();
				soundSystem.takeKey();
				return true;
			}
			@Override
			public void onBounce() {
				soundSystem.keyBounce();
			}
		});
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createFlyingSmallHeart(float x, float y, PickUpFlyingCallback callback) {
		IGameObject object = new PickUpFlying(x, y, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, equipmentSystem, callback);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createGuardianAcorn(float x, float y) {
		IGameObject object = new PickupItemTopDown(x, y, 8, 15, 149, 77, 149, 77, 0, false, true, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, new PickupItemTopDownCallback() {
			@Override
			public boolean onItemTake() {
				gameStateFactory.createPowerUpAcquireState(0, linkData.getX() + 1, linkData.getY() - 14);
				equipmentSystem.addGuardianAcorn();
				powerUpItemSystem.getGuardianAcorn();
				soundSystem.takePowerUp();
				return true;
			}
			@Override
			public void onBounce() {}
		});
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createPieceOfPower(float x, float y) {
		IGameObject object = new PickupItemTopDown(x, y, 12, 12, 157, 78, 157, 90, 0, false, true, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, new PickupItemTopDownCallback() {
			@Override
			public boolean onItemTake() {
				gameStateFactory.createPowerUpAcquireState(1, linkData.getX() + 1, linkData.getY() - 15);
				equipmentSystem.addPieceOfPower();
				powerUpItemSystem.getPieceOfPower();
				soundSystem.takePowerUp();
				return true;
			}
			@Override
			public void onBounce() {
			}
		});
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createFairy(float x, float y) {
		IGameObject object = new Fairy(x, y, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, equipmentSystem, rng);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createBigHeart(float x, float y, PickupItemTopDownCallback callback) {
		IGameObject object = new PickupItemTopDown(x, y, 14, 12, 118, 90, 118, 90, 0, false, false, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, new PickupItemTopDownCallback() {
			@Override
			public boolean onItemTake() {
				soundSystem.takeHeartContainer();
				gameStateFactory.createBigHeartAqcuiredGameState(()-> {
					equipmentSystem.addBigHeart();
					callback.onItemTake();
				});
				return true;
			}
			@Override
			public void onBounce() {}
		});
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createRupeePlatform(int x, int y) {
		IGameObject object = new PickupItemPlatform(x, y, 7, 14, 171, 64, 171, 64, true, true, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, ()-> {
			equipmentSystem.addRupee();
			return true;
		});
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createSmallHeartPlatform(int x, int y) {
		IGameObject object = new PickupItemPlatform(x, y, 7, 7, 133, 64, 133, 64, true, true, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, ()-> {
			equipmentSystem.addHeart();
			soundSystem.takePickupItem();
			return true;
		});
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createFireball(int x, int y) {
		IGameObject object = new Fireball(x, y, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, this, linkData);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createBomb(int x, int y, float height) {
		IGameObject object = new BombTopDown(x, y, height, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createBombPlatform(int x, int y) {
		IGameObject object = new BombPlatform(x, y, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createGuardianAcornPlatform(float x, float y) {
		IGameObject object = new PickupItemPlatform(x, y, 8, 15, 149, 77, 149, 77, false, true, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, ()-> {
			gameStateFactory.createPowerUpAcquireState(0, linkData.getX() + 1, linkData.getY() - 8);
			equipmentSystem.addGuardianAcorn();
			powerUpItemSystem.getGuardianAcorn();
			soundSystem.takePowerUp();
			return true;
		});
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createPieceOfPowerPlatform(float x, float y) {
		IGameObject object = new PickupItemPlatform(x, y, 12, 12, 157, 78, 157, 90, false, true, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, ()-> {
			gameStateFactory.createPowerUpAcquireState(1, linkData.getX() + 1, linkData.getY() - 8);
			equipmentSystem.addPieceOfPower();
			powerUpItemSystem.getPieceOfPower();
			soundSystem.takePowerUp();
			return true;
		});
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createFullMoonCello(float x, float y) {
		IGameObject object = new Instrument(x, y, 234, 80, 16, 16, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, ()-> {
			gameStateFactory.createInstrumentAcquiredGameState();
		});
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createMagicPowderSprinkle(float x, float y, MagicPowderDependency dependency) {
		IGameObject object = new MagicPowderSprinkle(x, y, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, dependency);
		addToSystem(object);
		return object;
	}

	@Override
	public IGameObject createSpark(float x, float y) {
		IGameObject object = new Spark((int)x, (int)y - 3, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createPoof(int x, int y) {
		IGameObject object = new Poof(x, y, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createPoof2(int x, int y, Poof2Callback callback) {
		IGameObject object = new Poof2(x, y, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, callback);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createDebris(float x, float y, boolean left) {
		IGameObject object = new Debris(x, y, left, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createFireballDefeated(int x, int y) {
		IGameObject object = new FireballDefeated(x, y, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createEnemyDefeatedSmallSpark(int x, int y, EnemyDefeatedSmallSparkCallback callback) {
		IGameObject object = new EnemyDefeatedSmallSpark(x, y, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, callback);
		addToSystem(object);
		return object;
	}
	@Override
	public IGameObject createEnemyDefeatedBigSpark(int x, int y, EnemyDefeatedBigSparkCallback callback) {
		IGameObject object = new EnemyDefeatedBigSpark(x, y, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, callback);
		addToSystem(object);
		return object;
	}

	private void addToSystem(IGameObject object) {
		gameObjectSystem.add(object);
		roomObjects.add(object);
	}
}
