package com.la.game_objects.enemy.rolling_bones;

import java.util.LinkedList;
import java.util.List;

import com.la.factory.IEnemyFactory;
import com.la.factory.IRoomFactory;
import com.la.game_objects.effect.Poof2.Poof2Callback;
import com.la.game_objects.enemy.rolling_bones.RolledBone.RolledBoneCallback;
import com.la.game_objects.enemy.rolling_bones.RollingBones.RollingBonesCallback;
import com.la.room.teleport_sys.TeleportSystem;
import com.la.room.transition.IMapLocation.MapLocationComponent;

public class RollingBonesSystem {
	public interface RollingBonesSystemCallback {
		void onDefeated();
	}
	public interface RollingBoneComponent {
		void push();
		void remove(int delay);
	}

	private IEnemyFactory enemyFactory;
	private RollingBonesCallback rollingBonesCallback;
	private RolledBoneCallback rolledBoneCallback;
	private List<RollingBoneComponent> rollingBoneComponents = new LinkedList<>();
	private boolean defeated;

	public RollingBonesSystem(
			IEnemyFactory enemyFactory,
			IRoomFactory roomFactory,
			TeleportSystem teleportSystem,
			MapLocationComponent mapLocationComponent,
			RollingBonesSystemCallback callback) {
		this.enemyFactory = enemyFactory;
		rolledBoneCallback = new RolledBoneCallback() {
			@Override
			public void onWallCollision() {
			}
			@Override
			public void createPoof(float x, float y) {
				roomFactory.createPoof2((int)x, (int)y, new Poof2Callback() {
					@Override
					public void onUpdate(int counter) {
					}
					@Override
					public void onEnd() {
					}
				});
			}
		};
		rollingBonesCallback = new RollingBonesCallback() {
			@Override
			public void onPush() {
				for (RollingBoneComponent rollingBoneComponent : rollingBoneComponents) {
					rollingBoneComponent.push();
				}
			}
			@Override
			public void onDeath(int x, int y) {
				int delay = 0;
				for (RollingBoneComponent rollingBoneComponent : rollingBoneComponents) {
					rollingBoneComponent.remove(delay);
					delay += 6;
				}
				teleportSystem.activateAndCreateMiniBossEntrance();
				defeated = true;
				callback.onDefeated();
				roomFactory.createFairy(x, y);
				mapLocationComponent.setDeepWalk(false);
			}
			@Override
			public void createPoof(int x, int y) {
				roomFactory.createPoof2((int)x, (int)y, new Poof2Callback() {
					@Override
					public void onUpdate(int counter) {
					}
					@Override
					public void onEnd() {
					}
				});
			}
		};
	}

	public void addRollingBoneComponent(RollingBoneComponent component) {
		rollingBoneComponents.add(component);
	}
	public void createMiniBoss(int offsetX, int offsetY) {
		if(defeated == false) {
			enemyFactory.createRollingBones(8 * 16 + 8 + offsetX, 3 * 16 + offsetY, rollingBonesCallback);
			enemyFactory.createRolledBone(6 * 16 + offsetX, 1 * 16 + offsetY, this, rolledBoneCallback);
			enemyFactory.createRolledBone(6 * 16 + offsetX, 2 * 16 + offsetY, this, rolledBoneCallback);
			enemyFactory.createRolledBone(6 * 16 + offsetX, 3 * 16 + offsetY, this, rolledBoneCallback);
			enemyFactory.createRolledBone(6 * 16 + offsetX, 4 * 16 + offsetY, this, rolledBoneCallback);
			enemyFactory.createRolledBone(6 * 16 + offsetX, 5 * 16 + offsetY, this, rolledBoneCallback);
			enemyFactory.createRolledBone(6 * 16 + offsetX, 6 * 16 + offsetY, this, rolledBoneCallback);
		}
	}
	public boolean isDefeated() {
		return defeated;
	}
}
