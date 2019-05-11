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
import com.la.game_objects.enemy.BladeTrap;
import com.la.game_objects.enemy.EnemyDefeatedPrize;
import com.la.game_objects.enemy.EnemyDefeatedPrize.EnemyDefeatedDependency;
import com.la.game_objects.enemy.EnemyPlatformDefeatedPrize;
import com.la.game_objects.enemy.EnemyPlatformDefeatedPrize.EnemyPlatformDefeatedDependency;
import com.la.game_objects.enemy.GelRed;
import com.la.game_objects.enemy.GelRed.GelRedBeetleCallback;
import com.la.game_objects.enemy.Goomba;
import com.la.game_objects.enemy.Goomba.GoombaCallback;
import com.la.game_objects.enemy.HardhatBeetle;
import com.la.game_objects.enemy.HardhatBeetle.HardhatBeetleCallback;
import com.la.game_objects.enemy.Keese;
import com.la.game_objects.enemy.Keese.KeeseCallback;
import com.la.game_objects.enemy.MiniMoldorm;
import com.la.game_objects.enemy.MiniMoldorm.MiniMoldormCallback;
import com.la.game_objects.enemy.Spark;
import com.la.game_objects.enemy.SpikedBeetle;
import com.la.game_objects.enemy.SpikedBeetle.SpikedBeetleCallback;
import com.la.game_objects.enemy.StalfosOrange;
import com.la.game_objects.enemy.StalfosOrange.StalfosOrangeCallback;
import com.la.game_objects.enemy.TestEnemyNxtGen;
import com.la.game_objects.enemy.TestEnemyNxtGen.TestEnemyNxtGenCallback;
import com.la.game_objects.enemy.TestEnemyPlatform;
import com.la.game_objects.enemy.ThreeOfAKind;
import com.la.game_objects.enemy.ThreeOfAKind.ThreeOfAKindCallback;
import com.la.game_objects.enemy.ZolGreen;
import com.la.game_objects.enemy.ZolGreen.ZolGreenCallback;
import com.la.game_objects.enemy.moldorm.Moldorm;
import com.la.game_objects.enemy.moldorm.Moldorm.MoldormCallback;
import com.la.game_objects.enemy.rolling_bones.RolledBone;
import com.la.game_objects.enemy.rolling_bones.RolledBone.RolledBoneCallback;
import com.la.game_objects.enemy.rolling_bones.RollingBones;
import com.la.game_objects.enemy.rolling_bones.RollingBonesSystem;
import com.la.game_objects.enemy.rolling_bones.RollingBones.RollingBonesCallback;
import com.la.game_objects.link.ILinkData;
import com.la.power_up_item_system.PowerUpItemSystem;
import com.la.three_of_a_kind_system.ThreeOfAKindSystem;

public class EnemyFactory implements IEnemyFactory {
	public interface EnemyFactoryDependency {
		void drawText(String text);
	}
	class EnemyDefeatedDependency1 implements EnemyDefeatedDependency {
		@Override
		public void addKill() {
			powerUpItemSystem.enemyDefeated();
		}
		@Override
		public boolean shouldSpawnGuardianAcorn() {
			return powerUpItemSystem.guardianAcornCheck();
		}
		@Override
		public boolean shouldSpawnPieceOfPower() {
			return powerUpItemSystem.pieceOfPowerCheck();
		}
	}
	class EnemyPlatformDefeatedDependency1 implements EnemyPlatformDefeatedDependency {
		@Override
		public void addKill() {
			powerUpItemSystem.enemyDefeated();
		}
		@Override
		public boolean shouldSpawnGuardianAcorn() {
			return powerUpItemSystem.guardianAcornCheck();
		}
		@Override
		public boolean shouldSpawnPieceOfPower() {
			return powerUpItemSystem.pieceOfPowerCheck();
		}
	}

	private IGameObjectSystem gameObjectSystem;
	private GFXSystem gfxSystem;
	private SoundSystem soundSystem;
	private SpatialSystem spatialSystem;
	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;
	private IRoomFactory roomFactory;
	private ILinkData linkData;
	private IDoubleBuffer<IGameObject> roomObjects;
	private TileMapSystem tileMapSystem;
	private PowerUpItemSystem powerUpItemSystem;
	private IRNG rng;
	private EnemyFactoryDependency dependency;

	public EnemyFactory(
			IGameObjectSystem gameObjectSystem,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			IRoomFactory roomFactory,
			ILinkData linkData,
			IDoubleBuffer<IGameObject> roomObjects,
			TileMapSystem tileMapSystem,
			PowerUpItemSystem powerUpItemSystem,
			IRNG rng,
			EnemyFactoryDependency dependency) {
		this.gameObjectSystem = gameObjectSystem;
		this.gfxSystem = gfxSystem;
		this.soundSystem = soundSystem;
		this.spatialSystem = spatialSystem;
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.roomFactory = roomFactory;
		this.linkData = linkData;
		this.roomObjects = roomObjects;
		this.tileMapSystem = tileMapSystem;
		this.powerUpItemSystem = powerUpItemSystem;
		this.rng = rng;
		this.dependency = dependency;
	}

	@Override
	public IGameObject createTestEnemy(int x, int y, TestEnemyNxtGenCallback callback) {
//		IGameObject enemy = new TestEnemy(x, y, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, new EnemyDefeatedPrize(roomFactory, rng), linkData);
		IGameObject enemy = new TestEnemyNxtGen(x, y, gfxSystem, spatialSystem, uniqueIDManager, aspectSystem, linkData, new EnemyDefeatedPrize(roomFactory, rng, new EnemyDefeatedDependency1()), callback);
		addToSystem(enemy);
		return enemy;
	}
	@Override
	public IGameObject createTestEnemyPlatform(int x, int y) {
		IGameObject enemy = new TestEnemyPlatform(x, y, gfxSystem, spatialSystem, aspectSystem, uniqueIDManager, new EnemyPlatformDefeatedPrize(roomFactory, rng, null), linkData);
		addToSystem(enemy);
		return enemy;
	}

	@Override
	public IGameObject createHardhatBeetle(int x, int y, HardhatBeetleCallback callback) {
		IGameObject enemy = new HardhatBeetle(x, y, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, roomFactory, linkData, new EnemyDefeatedPrize(roomFactory, rng, new EnemyDefeatedDependency1()), callback);
		addToSystem(enemy);
		return enemy;
	}
	@Override
	public IGameObject createZolGreen(int x, int y, ZolGreenCallback callback) {
		IGameObject enemy = new ZolGreen(x, y, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, roomFactory, linkData, new EnemyDefeatedPrize(roomFactory, rng, new EnemyDefeatedDependency1()), callback);
		addToSystem(enemy);
		return enemy;
	}
	@Override
	public IGameObject createGelRed(int x, int y, GelRedBeetleCallback callback) {
		IGameObject enemy = new GelRed(x, y, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, roomFactory, linkData, new EnemyDefeatedPrize(roomFactory, rng, new EnemyDefeatedDependency1()), callback, rng);
		addToSystem(enemy);
		return enemy;
	}
	@Override
	public IGameObject createKeese(int x, int y, KeeseCallback callback) {
		IGameObject enemy = new Keese(x, y, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, linkData, rng, new EnemyDefeatedPrize(roomFactory, rng, new EnemyDefeatedDependency1()), roomFactory, callback);
		addToSystem(enemy);
		return enemy;
	}
	@Override
	public IGameObject createStalfosOrange(int x, int y, StalfosOrangeCallback callback) {
		IGameObject enemy = new StalfosOrange(x, y, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, roomFactory, rng, new EnemyDefeatedPrize(roomFactory, rng, new EnemyDefeatedDependency1()), linkData, callback);
		addToSystem(enemy);
		return enemy;
	}
	@Override
	public IGameObject createSpark(int x, int y, boolean defaultDir, int dir) {
		IGameObject enemy = new Spark(x, y, defaultDir, dir, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, linkData, new EnemyDefeatedPrize(roomFactory, rng, new EnemyDefeatedDependency1()), tileMapSystem);
		addToSystem(enemy);
		return enemy;
	}
	@Override
	public IGameObject createBladeTrap(int x, int y) {
		IGameObject enemy = new BladeTrap(x, y, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, linkData, tileMapSystem);
		addToSystem(enemy);
		return enemy;
	}
	@Override
	public IGameObject createSpikedBeetle(int x, int y, SpikedBeetleCallback callback) {
		IGameObject enemy = new SpikedBeetle(x, y, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, roomFactory, linkData, new EnemyDefeatedPrize(roomFactory, rng, new EnemyDefeatedDependency1()), callback, rng);
		addToSystem(enemy);
		return enemy;
	}
	@Override
	public IGameObject createThreeOfAKind(int x, int y, ThreeOfAKindSystem system, ThreeOfAKindCallback callback) {
		IGameObject enemy = new ThreeOfAKind(x, y, gfxSystem, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, roomFactory, rng, system, new EnemyDefeatedPrize(roomFactory, rng, new EnemyDefeatedDependency1()), callback);
		addToSystem(enemy);
		return enemy;
	}
	@Override
	public IGameObject createMiniMoldorm(int x, int y, MiniMoldormCallback callback) {
		IGameObject enemy = new MiniMoldorm(x, y, gfxSystem, soundSystem, spatialSystem, roomFactory, uniqueIDManager, aspectSystem, rng, new EnemyDefeatedPrize(roomFactory, rng, new EnemyDefeatedDependency1()), callback);
		addToSystem(enemy);
		return enemy;
	}
	@Override
	public IGameObject createGoomba(int x, int y, GoombaCallback callback) {
		IGameObject enemy = new Goomba(x, y, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, roomFactory, new EnemyPlatformDefeatedPrize(roomFactory, rng, new EnemyPlatformDefeatedDependency1()), callback);
		addToSystem(enemy);
		return enemy;
	}
	@Override
	public IGameObject createRollingBones(int x, int y, RollingBonesCallback callback) {
		IGameObject enemy = new RollingBones(x, y, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, roomFactory, rng, callback);
		addToSystem(enemy);
		return enemy;
	}
	@Override
	public IGameObject createRolledBone(int x, int y, RollingBonesSystem system, RolledBoneCallback callback) {
		IGameObject enemy = new RolledBone(x, y, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, system, callback);
		addToSystem(enemy);
		return enemy;
	}

	@Override
	public IGameObject createMoldorm(int x, int y, MoldormCallback callback) {
		IGameObject enemy = new Moldorm(x, y, gfxSystem, soundSystem, spatialSystem, aspectSystem, uniqueIDManager, roomFactory, rng, new MoldormCallback() {
			@Override
			public void onDeath() {
				callback.onDeath();
			}
			@Override
			public void drawText(String text) {
				dependency.drawText(text);
			}
		});
		addToSystem(enemy);
		return enemy;
	}

	private void addToSystem(IGameObject enemy) {
		gameObjectSystem.add(enemy);
		roomObjects.add(enemy);
	}
}
