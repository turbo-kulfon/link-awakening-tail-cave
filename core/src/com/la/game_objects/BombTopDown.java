package com.la.game_objects;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.component.fall.FallComponent;
import com.engine.component.fall.FallComponent.FallDependency;
import com.engine.component.fall.IFallComponent;
import com.engine.component.floor_bounce.FloorBounce;
import com.engine.direction.Direction;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.IOutsideViewCheck;
import com.engine.spatial.IOutsideViewCheck.OutsideViewCheckCallback;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.OutsideViewCheck;
import com.engine.spatial.OutsideViewCheckPosition;
import com.engine.spatial.SpatialSystem;
import com.engine.util.CollisionDetection;
import com.engine.util.ICollisionDetection;
import com.engine.util.ICoordinate.Vector;
import com.la.aspects.BombHit;
import com.la.aspects.BombTag;
import com.la.aspects.CarriedItem;
import com.la.aspects.CarriedItem.ItemType;
import com.la.aspects.HoleCollision;
import com.la.aspects.RoomTransition;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.game_objects.BombExplosionGFX.BombExplosionDependency;

public class BombTopDown implements IGameObject {
	private SoundSystem soundSystem;
	private TextureDrawComponent drawComponent;
	private BombExplosionGFX bombExplosion;
	private ISpatialComponent spatialComponent;
	private FloorBounce floorBounce;
	private IFallComponent fallComponent;
	private Shadow shadow;
	private FallAnimation fallAnimation;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;
	
	private int uniqueID, bounces = 2, explosionTimer = 90, mode = 0, fallBug;
	private boolean remove, carried;
	private IOutsideViewCheck outsideViewCheck;
	private float height;
	private Vector delta = new Vector(0, 0);

	public BombTopDown(float x, float y, float height,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem) {
		this.soundSystem = soundSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;
		this.height = height;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(140, 64, 8, 13);
		drawComponent.setSize(8, 13);
		drawComponent.setSpriteOffsetY(-6);

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x - 4, y - 7, 8, 7);
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

		float initialDelta = 0;
		if(height > 0) {
			initialDelta = 2.2f;
		}

		floorBounce = new FloorBounce(height, initialDelta, (heightArg)-> {
			this.height = heightArg;
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

		shadow = new Shadow(gfxSystem, new ShadowDependencyStandard(spatialComponent));
		shadow.setVisible(true);

		fallComponent = new FallComponent(new FallDependency() {
			
			@Override
			public void update(float dx, float dy, int counter) {
				spatialComponent.setDelta(dx, dy);
			}
			
			@Override
			public void onFallEnd() {
				setToRemove();
			}
			
			@Override
			public void onFall() {
				if(remove == false) {
					soundSystem.enemyFall();
				}
			}
			
			@Override
			public float getCenterX() {
				return spatialComponent.getCenterX();
			}
			@Override
			public float getCenterY() {
				return spatialComponent.getCenterY();
			}
		});

		aspectSystem.addAspect(new BombTag(uniqueID));
		aspectSystem.addAspect(new CarriedItem(uniqueID, ItemType.BOMB) {
			@Override
			public void toss(Direction direction) {
				spatialComponent.setActive(true);
				BombTopDown.this.height = 12;
				floorBounce.reset(BombTopDown.this.height, 4);
				spatialComponent.move(0, 3.5f);
				carried = false;
				if(direction == Direction.LEFT) {
					delta.x = -1;
					delta.y = 0;
				}
				else if(direction == Direction.RIGHT) {
					delta.x = 1;
					delta.y = 0;
				}
				else if(direction == Direction.UP) {
					delta.x = 0;
					delta.y = -1;
				}
				else if(direction == Direction.DOWN) {
					delta.x = 0;
					delta.y = 1;
				}
				bounces = 0;
			}
			@Override
			public void tossDown() {
				spatialComponent.setActive(true);
				BombTopDown.this.height = 12;
				floorBounce.reset(BombTopDown.this.height, 0);
				spatialComponent.move(0, 3.5f);
				carried = false;
			}
			@Override
			public boolean take() {
				if(BombTopDown.this.height <= 4 && mode == 0) {
					spatialComponent.setActive(false);
					carried = true;
					explosionTimer = 90;
					BombTopDown.this.height = 0;
					floorBounce.reset(BombTopDown.this.height, 0);
					return true;
				}
				return false;
			}
			@Override
			public void setPosition(float x, float y) {
				spatialComponent.setPosition(x - 4, y - 7);
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
		aspectSystem.addAspect(new WallBounce(uniqueID, spatialComponent) {
			@Override
			public int bounce(float wallX, float wallY, float wallW, float wallH, int wallDirection) {
				if(mode == 0) {
					return super.bounce(wallX, wallY, wallW, wallH, wallDirection);
				}
				return -1;
			}
		});
		ICollisionDetection collisionDetection = new CollisionDetection();
		aspectSystem.addAspect(new HoleCollision(uniqueID) {
			@Override
			public HoleCollisionResult collision(float holeX, float holeY, float holeW, float holeH, boolean restoreLastPosition) {
				if(mode == 0 && BombTopDown.this.height <= 0 && fallComponent.isFalling() == false &&
				   collisionDetection.collisionDetect(holeX, holeY, holeW, holeH, spatialComponent.getCenterX(), spatialComponent.getCenterY()) == true) {
					fallComponent.fall(holeX + holeW/2.0f, holeY + holeH/2.0f);
					return HoleCollisionResult.OTHER;
				}
				return HoleCollisionResult.NONE;
			}
		});
		fallAnimation = new FallAnimation(drawComponent, ()-> {
			return fallComponent.isFalling();
		}, -1.0f, -1.5f);
	}

	@Override
	public void update() {
		if(mode == 0) {
			if(fallComponent.isFalling() == false) {
				fallBug -= 1;
				if(fallBug == 0) {
					soundSystem.bombBounce();
				}
				else if(fallBug < 0) {
					fallBug = 0;
				}
				floorBounce.update();
				if(height <= 0) {
					if(bounces < 4) {
						if(bounces > 0 && bounces < 3) {
							soundSystem.bombBounce();
						}
						if(bounces == 0) {
							fallBug = 2;
						}
						bounces += 1;
						delta.x /= 1.25f;
						delta.y /= 1.25f;
					}
					else {
						delta.x = 0;
						delta.y = 0;
					}
				}
				if(bounces >= 4 && carried == false) {
					explosionTimer -= 1;
					if(explosionTimer <= 0) {
						mode = 1;
						explosionTimer = 38;
						soundSystem.bombExplode();
//						drawComponent.setTexture(184, 16, 32, 32);
//						drawComponent.setSize(32, 32);
//						drawComponent.setSpriteOffset(0, 0);
						spatialComponent.setCoordinates(spatialComponent.getCenterX() - 16, spatialComponent.getCenterY() - 18, 32, 32);
					}
				}
				spatialComponent.setDelta(delta.x, delta.y);
			}
			
			fallComponent.update();
			outsideViewCheck.update();
		}
		else if(mode == 1) {
			explosionTimer -= 1;
			if(explosionTimer <= 0) {
				setToRemove();
			}
		}
		fallAnimation.update();
	}

	@Override
	public void draw() {
		if(mode == 0) {
			if(carried == false) {
				drawComponent.setSpriteOffsetY(-6);
			}
			else {
				drawComponent.setSpriteOffsetY(-16);
			}
			drawComponent.setHeight(-height);
			drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
			if(explosionTimer < 30 && carried == false) {
				drawComponent.setInvert(explosionTimer % 8 < 4, 0.76f, 0.19f, 0.19f);
			}
			else {
				drawComponent.setInvert(false, 0.76f, 0.19f, 0.19f);
			}
		}
		else if(mode == 1) {
			bombExplosion.draw();
			drawComponent.setInvert(false, 0.76f, 0.19f, 0.19f);
		}

		fallAnimation.draw();
		shadow.setVisible(height > 0 && fallComponent.isFalling() == false);
		shadow.draw();
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
		shadow.remove();
		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}

}
