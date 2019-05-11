package com.la.factory;

import com.engine.game_object.IGameObject;
import com.la.game_objects.enemy.GelRed.GelRedBeetleCallback;
import com.la.game_objects.enemy.Goomba.GoombaCallback;
import com.la.game_objects.enemy.HardhatBeetle.HardhatBeetleCallback;
import com.la.game_objects.enemy.Keese.KeeseCallback;
import com.la.game_objects.enemy.MiniMoldorm.MiniMoldormCallback;
import com.la.game_objects.enemy.SpikedBeetle.SpikedBeetleCallback;
import com.la.game_objects.enemy.StalfosOrange.StalfosOrangeCallback;
import com.la.game_objects.enemy.TestEnemyNxtGen.TestEnemyNxtGenCallback;
import com.la.game_objects.enemy.ThreeOfAKind.ThreeOfAKindCallback;
import com.la.game_objects.enemy.ZolGreen.ZolGreenCallback;
import com.la.game_objects.enemy.moldorm.Moldorm.MoldormCallback;
import com.la.game_objects.enemy.rolling_bones.RolledBone.RolledBoneCallback;
import com.la.game_objects.enemy.rolling_bones.RollingBones.RollingBonesCallback;
import com.la.game_objects.enemy.rolling_bones.RollingBonesSystem;
import com.la.three_of_a_kind_system.ThreeOfAKindSystem;

public interface IEnemyFactory {
	IGameObject createTestEnemy(int x, int y, TestEnemyNxtGenCallback callback);
	IGameObject createTestEnemyPlatform(int x, int y);

	IGameObject createHardhatBeetle(int x, int y, HardhatBeetleCallback callback);
	IGameObject createZolGreen(int x, int y, ZolGreenCallback callback);
	IGameObject createGelRed(int x, int y, GelRedBeetleCallback callback);
	IGameObject createKeese(int x, int y, KeeseCallback callback);
	IGameObject createStalfosOrange(int x, int y, StalfosOrangeCallback callback);
	IGameObject createSpark(int x, int y, boolean defaultDir, int dir);
	IGameObject createBladeTrap(int x, int y);
	IGameObject createMiniMoldorm(int x, int y, MiniMoldormCallback callback);
	IGameObject createSpikedBeetle(int x, int y, SpikedBeetleCallback callback);
	IGameObject createThreeOfAKind(int x, int y, ThreeOfAKindSystem system, ThreeOfAKindCallback callback);
	IGameObject createGoomba(int x, int y, GoombaCallback callback);

	IGameObject createRollingBones(int x, int y, RollingBonesCallback callback);
	IGameObject createRolledBone(int x, int y, RollingBonesSystem system, RolledBoneCallback callback);

	IGameObject createMoldorm(int x, int y, MoldormCallback callback);
}
