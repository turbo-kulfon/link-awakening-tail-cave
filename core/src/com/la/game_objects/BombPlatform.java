package com.la.game_objects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.direction.Direction;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.IOutsideViewCheck;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.OutsideViewCheck;
import com.engine.spatial.OutsideViewCheckPosition;
import com.engine.spatial.SpatialSystem;
import com.engine.spatial.IOutsideViewCheck.OutsideViewCheckCallback;
import com.la.aspects.BombHit;
import com.la.aspects.BombTag;
import com.la.aspects.CarriedItem;
import com.la.aspects.RoomTransition;
import com.la.aspects.CarriedItem.ItemType;
import com.la.aspects.wall_bounce.WallBounceStandard;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.game_objects.BombExplosionGFX.BombExplosionDependency;
import com.la.game_objects.link.controller.platform.BounceComponent;
import com.la.game_objects.link.controller.platform.BounceComponent.BounceComponentDependency;
import com.la.game_objects.link.controller.platform.GravityComponent;
import com.la.game_objects.link.controller.platform.GravityComponent.GravityComponentDependency;

public class BombPlatform implements IGameObject {
	private SoundSystem soundSystem;
	private TextureDrawComponent drawComponent;
	private BombExplosionGFX bombExplosion;
	private ISpatialComponent spatialComponent;

	private GravityComponent gravityComponent;
	private BounceComponent bounceComponent;
	private IOutsideViewCheck outsideViewCheck;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private int uniqueID;
	private boolean remove;

	private boolean onGround, carried;
	private int explosionTimer = 90, mode, bounces, bounceBenc = 0;
	private float deltaX;

	public BombPlatform(float x, float y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem) {
		this.soundSystem = soundSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(140, 64, 8, 13);
		drawComponent.setSize(8, 13);
		drawComponent.setSpriteOffsetY(-1);

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x - 4, y - 12, 8, 12);
		spatialComponent.setCollisionResponse((collidedID)-> {
			if(mode == 1) {
				BombHit bombHit = aspectSystem.getAspect(collidedID, AspectType.BOMB_HIT);
				if(bombHit != null) {
					bombHit.hit(spatialComponent.getCenterX(), spatialComponent.getCenterY(), true);
				}
			}
		});

		bombExplosion = new BombExplosionGFX(drawComponent, new BombExplosionDependency() {
			@Override
			public float centerX() {
				return spatialComponent.getCenterX();
			}
			@Override
			public float centerY() {
				return spatialComponent.getCenterY();
			}
			@Override
			public int counter() {
				return explosionTimer;
			}
		});

		outsideViewCheck = new OutsideViewCheck(new OutsideViewCheckPosition(spatialComponent), 
			new OutsideViewCheckCallback() {
			@Override
			public void outsideLeft() {
				if(mode == 0) {
					spatialComponent.setX(0);
					spatialComponent.setDeltaX(0);
				}
			}
			@Override
			public void outsideRight() {
				if(mode == 0) {
					spatialComponent.setX(160 - spatialComponent.getW());
					spatialComponent.setDeltaX(0);
				}
			}
			@Override
			public void outsideUp() {
				if(mode == 0) {
					spatialComponent.setY(0);
					spatialComponent.setDeltaY(0);
				}
			}
			@Override
			public void outsideDown() {
				if(mode == 0) {
					spatialComponent.setY(128 - spatialComponent.getH());
					spatialComponent.setDeltaY(0);
				}
			}
			}
		);

		gravityComponent = new GravityComponent(new GravityComponentDependency() {
			@Override
			public void setDeltaY(float dy) {
				spatialComponent.setDeltaY(dy);
			}
			@Override
			public boolean isOnGround() {
				return onGround;
			}
			@Override
			public boolean isLevitating() {
				return false;
			}
			@Override
			public void decreaseDeltaY(float amount) {
				spatialComponent.setDeltaY(spatialComponent.getDeltaY() - amount);
				if(spatialComponent.getDeltaY() >= 4) {
					spatialComponent.setDeltaY(4);
				}
			}
		});
		bounceComponent = new BounceComponent(new BounceComponentDependency() {
			@Override
			public boolean onGround() {
				return onGround;
			}
			@Override
			public void bounce(float delta) {
				spatialComponent.setDeltaY(-delta);
				soundSystem.bombBounce();
			}
		});

		aspectSystem.addAspect(new BombTag(uniqueID));
		aspectSystem.addAspect(new CarriedItem(uniqueID, ItemType.BOMB) {
			@Override
			public void toss(Direction direction) {
				spatialComponent.setActive(true);
				bounceComponent.reset(2.2f);
				spatialComponent.move(0, 3.5f);
				carried = false;
				if(direction == Direction.LEFT) {
					spatialComponent.setDelta(0, -2.2f);
					deltaX = -1.5f;
				}
				else if(direction == Direction.RIGHT) {
					spatialComponent.setDelta(0, -2.2f);
					deltaX = 1.5f;
				}
				else if(direction == Direction.UP) {
					spatialComponent.setDelta(0, -2.2f);
				}
				else if(direction == Direction.DOWN) {
					spatialComponent.setDelta(0, 1);
				}
				bounces = 5;
			}
			@Override
			public void tossDown() {
				spatialComponent.setActive(true);
				bounceComponent.reset(2.2f);
				carried = false;
				bounces = 5;
			}
			@Override
			public boolean take() {
				if(mode == 0) {
					spatialComponent.setActive(false);
					carried = true;
					explosionTimer = 90;
					return true;
				}
				return false;
			}
			@Override
			public void setPosition(float x, float y) {
				spatialComponent.setPosition(x - 4, y - 14);
			}
			@Override
			public void removeItem() {
				setToRemove();
			}
			@Override
			public boolean isCarried() {
				return carried;
			}
		});
		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		aspectSystem.addAspect(new WallBounce(uniqueID, spatialComponent, new WallBounceStandard(spatialComponent) {
			@Override
			public void onUpSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onUpSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				onGround = true;
			}
			@Override
			public void onLeftSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onLeftSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				deltaX /= 1.25f;
			}
			@Override
			public void onRightSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onRightSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				deltaX /= 1.25f;
			}
		}) {
			@Override
			public int bounce(float wallX, float wallY, float wallW, float wallH, int wallDirection) {
				if(mode == 0) {
					return super.bounce(wallX, wallY, wallW, wallH, wallDirection);
				}
				return -1;
			}
		});
	}

	@Override
	public void update() {
		if(mode == 0) {
			if(bounceBenc == 0) {
				bounceBenc = 1;
			}
			else if(bounceBenc == 1) {
				if(onGround == false) {
					bounceBenc = 2;
				}
				else {
					bounceBenc = 3;
				}
			}
			else if(bounceBenc == 2) {
				bounceComponent.reset(1.1f);
				bounceBenc = 3;
			}
			if(carried == false) {
				gravityComponent.update();
				bounceComponent.update();
			}
			
			if(onGround == true) {
				if(bounces > 0) {
					bounces -= 1;
					deltaX /= 2.0f;
				}
				else {
					deltaX = 0;
					explosionTimer -= 1;
					if(explosionTimer <= 0 && carried == false) {
						mode = 1;
						explosionTimer = 30;
						soundSystem.bombExplode();
						drawComponent.setTexture(184, 16, 32, 32);
						drawComponent.setSize(32, 32);
						drawComponent.setSpriteOffset(0, 0);
						spatialComponent.setCoordinates(spatialComponent.getCenterX() - 16, spatialComponent.getCenterY() - 16, 32, 32);
					}
				}
			}

			spatialComponent.setDeltaX(deltaX);
			onGround = false;
			outsideViewCheck.update();
		}
		else if(mode == 1) {
			spatialComponent.stop();
			explosionTimer -= 1;
			if(explosionTimer <= 0) {
				setToRemove();
			}
		}
	}
	@Override
	public void draw() {
		if(mode == 0) {
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
			if(explosionTimer < 30 && carried == false) {
				drawComponent.setInvert(explosionTimer % 8 < 4, 0.76f, 0.19f, 0.19f);
			}
			else {
				drawComponent.setInvert(false, 0.76f, 0.19f, 0.19f);
			}
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		}
		else if(mode == 1) {
			bombExplosion.draw();
			drawComponent.setInvert(false, 0.76f, 0.19f, 0.19f);
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
		drawComponent.remove();
		spatialComponent.remove();
		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}
}
