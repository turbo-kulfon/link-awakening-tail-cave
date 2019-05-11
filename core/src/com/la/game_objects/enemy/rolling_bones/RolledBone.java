package com.la.game_objects.enemy.rolling_bones;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.component.sword.SwordState;
import com.engine.direction.Direction;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.RoomTransition;
import com.la.aspects.SwordHit;
import com.la.aspects.enemy_hit.EnemyHit;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.aspects.wall_bounce.WallBounceStandard;
import com.la.game_objects.enemy.rolling_bones.RollingBonesSystem.RollingBoneComponent;

public class RolledBone implements IGameObject {
	public interface RolledBoneCallback {
		void onWallCollision();
		void createPoof(float x, float y);
	}

	private enum RolledBoneState {
		IDLE,
		ROLL,
		RECOIL,
	}

	private SoundSystem soundSystem;
	private TextureDrawComponent element;
	private ISpatialComponent spatialComponent;

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;

	private RolledBoneState state = RolledBoneState.IDLE;
	private boolean wallCollision, leftMove = true;
	private int counter = 90, animationCounter, delay = -1;
	private RolledBoneCallback callback;

	private boolean remove;
	private int uniqueID;

	public RolledBone(float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			RollingBonesSystem rollingBonesSystem,
			RolledBoneCallback callback) {
		this.soundSystem = soundSystem;
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.callback = callback;

		rollingBonesSystem.addRollingBoneComponent(new RollingBoneComponent() {
			@Override
			public void push() {
				state = RolledBoneState.ROLL;
			}
			@Override
			public void remove(int delay) {
				RolledBone.this.delay = delay;
			}
		});

		uniqueID = uniqueIDManager.getUniqueID();

		element = gfxSystem.createTextureDrawComponent(0);
		element.setTexture(481, 90, 16, 16);
		element.setSize(16, 16);
		element.setSpriteOffsetX(-2);
		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 12, 16);

		spatialComponent.setCollisionResponse((collidedID)-> {
			EnemyHit enemyHit = aspectSystem.getAspect(collidedID, AspectType.ENEMY_HIT);
			if(enemyHit != null) {
				enemyHit.hit(
					spatialComponent.getX(), spatialComponent.getY(),
					spatialComponent.getW(), spatialComponent.getH(),
					leftMove == true ? Direction.LEFT : Direction.RIGHT, -1, 2, 2);
			}
		});

		aspectSystem.addAspect(new WallBounce(uniqueID, spatialComponent, new WallBounceStandard(spatialComponent) {
			@Override
			public void onLeftSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onLeftSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				wallCollision = true;
			}
			@Override
			public void onRightSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onRightSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				wallCollision = true;
			}
		}));
		aspectSystem.addAspect(new SwordHit(uniqueID) {
			@Override
			public SwordHitResult hit(
					float swordX, float swordY, float swordW, float swordH, float ownerCX, float ownerCY, Direction ownerDirection,
					int dmg, boolean powerUped, SwordState swordState, int counter) {
				if(leftMove == true) {
					if(ownerDirection == Direction.RIGHT) {
						return new SwordHitResult(SwordHitResultType.DEFLECT, spatialComponent.getCenterX(), ownerCY);
					}
				}
				else {
					if(ownerDirection == Direction.LEFT) {
						return new SwordHitResult(SwordHitResultType.DEFLECT, spatialComponent.getCenterX(), ownerCY);
					}
				}
				return new SwordHitResult(SwordHitResultType.NONE, spatialComponent.getCenterX(), ownerCY);
			}
		});
		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
	}

	@Override
	public void update() {
		if(delay == -1) { 
			if(state == RolledBoneState.IDLE) {
				spatialComponent.stop();
			}
			else if(state == RolledBoneState.ROLL) {
				if(wallCollision == false) {
					counter += 1;
					if(counter % 11 == 0) {
						soundSystem.roll();
					}
					if(leftMove == true) {
						spatialComponent.setDeltaX(-1);
					}
					else {
						spatialComponent.setDeltaX( 1);
					}
					animationCounterUpdate(2);
				}
				else {
					spatialComponent.stop();
					state = RolledBoneState.RECOIL;
					counter = 32;
					leftMove = !leftMove;
					callback.onWallCollision();
					soundSystem.floorCrumble();
				}
			}
			else if(state == RolledBoneState.RECOIL) {
				counter -= 1;
				if(counter > 0) {
					if(leftMove == true) {
						spatialComponent.setDeltaX(-0.5f);
					}
					else {
						spatialComponent.setDeltaX( 0.5f);
					}
					animationCounterUpdate(1);
				}
				else {
					state = RolledBoneState.IDLE;
					counter = 90;
				}
			}
		}
		else {
			spatialComponent.stop();
			delay -= 1;
			if(delay <= 0) {
				setToRemove();
				callback.createPoof(spatialComponent.getCenterX(), spatialComponent.getCenterY());
				soundSystem.bossDiesInFlamesFast();
			}
		}
		wallCollision = false;
	}
	@Override
	public void draw() {
		element.setPosition(spatialComponent.getX(), spatialComponent.getY());
		if(animationCounter <= 8) {
			element.setTexturePosition(481, 90);
		}
		else {
			element.setTexturePosition(496, 90);
		}
	}

	@Override
	public void setToRemove() {
		remove = true;
	}
	@Override
	public boolean shouldRemove() {
		return remove;
	}
	@Override
	public void onRemove() {
		element.remove();
		spatialComponent.remove();
		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}

	private void animationCounterUpdate(int delta) {
		animationCounter += delta;
		if(animationCounter > 16) {
			animationCounter = 0;
		}
	}
}
