package com.la.game_objects.pickup;

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
import com.la.aspects.HoleCollision;
import com.la.aspects.LinkCollision;
import com.la.aspects.RoomTransition;
import com.la.aspects.SwordHit;
import com.la.game_objects.FallAnimation;
import com.la.game_objects.Shadow;
import com.la.game_objects.ShadowDependencyStandard;
import com.la.game_objects.link.controller.platform.BounceComponent;
import com.la.game_objects.link.controller.platform.BounceComponent.BounceComponentDependency;
import com.la.game_objects.link.controller.top_down.HoleFallComponent;
import com.la.game_objects.link.controller.top_down.HoleFallComponent.HoleFallDependency;
import com.la.game_objects.link.controller.top_down.JumpTopDownComponent;
import com.la.game_objects.link.controller.top_down.JumpTopDownComponent.JumpTopDownDependency;

public class PickupItemTopDown implements IGameObject {
	public interface PickupItemTopDownCallback {
		boolean onItemTake();
		void onBounce();
	}

	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;
	private Shadow shadow;
	private FallAnimation fallAnimation;

	private JumpTopDownComponent jumpComponent;
	private BounceComponent bounceComponent;
	private HoleFallComponent holeFallComponent;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private int uniqueID;
	private boolean remove;
	private float height;
	private int animationCounter, disappearTimer;
	private int tx1, ty1, tx2, ty2;

	public PickupItemTopDown(
			float x, float y, float w, float h, int texX, int texY, int texX2, int texY2, float initialHeight, boolean disappear,
			boolean swordCollision,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			PickupItemTopDownCallback callback) {
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;
		this.height = initialHeight;

		this.tx1 = texX;
		this.ty1 = texY;
		this.tx2 = texX2;
		this.ty2 = texY2;

		if(disappear == true) {
			disappearTimer = 360;
		}
		else {
			disappearTimer = -1;
		}

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(texX, texY, (int)w, (int)h);
		drawComponent.setSize(w, h);
//		drawComponent.setVisible(false);

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x - w/2.0f, y - h/2.0f, w, h);

		shadow = new Shadow(gfxSystem, new ShadowDependencyStandard(spatialComponent));
		shadow.setVisible(true);

		jumpComponent = new JumpTopDownComponent(new JumpTopDownDependency() {
			@Override
			public void update(float heightArg, boolean isJumping) {
				height = heightArg;
			}
			@Override
			public void onJump() {
			}
			@Override
			public void onLand() {
			}
		}, initialHeight);
		bounceComponent = new BounceComponent(new BounceComponentDependency() {
			@Override
			public boolean onGround() {
				return height <= 0;
			}
			@Override
			public void bounce(float delta) {
				jumpComponent.jump(delta);
				callback.onBounce();
			}
		});
		bounceComponent.reset(3);
		holeFallComponent = new HoleFallComponent(new HoleFallDependency() {
			@Override
			public float getCenterX() {
				return spatialComponent.getCenterX();
			}
			@Override
			public float getCenterY() {
				return spatialComponent.getCenterY();
			}
			@Override
			public void setDelta(float dx, float dy) {
				spatialComponent.setDelta(dx, dy);
			}
			@Override
			public void onFall() {
				soundSystem.enemyFall();
			}
			@Override
			public void onFallEnd() {
				setToRemove();
			}
		});

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		aspectSystem.addAspect(new LinkCollision(uniqueID) {
			@Override
			public void collision(float linkX, float linkY, float linkW, float linkH, float linkHeight, boolean blocked) {
				if(height <= 0 && linkHeight <= 0 && remove == false && holeFallComponent.isActive() == false) {
					if(callback.onItemTake() == true) {
						setToRemove();
					}
				}
			}
		});
		aspectSystem.addAspect(new HoleCollision(uniqueID) {
			@Override
			public HoleCollisionResult collision(float holeX, float holeY, float holeW, float holeH, boolean restoreLastPosition) {
				if(remove == false && height <= 0 && holeFallComponent.isActive() == false) {
					holeFallComponent.fall(holeX + holeW/2.0f, holeY + holeH/2.0f);
					spatialComponent.setActive(false);
					return HoleCollisionResult.OTHER;
				}
				return HoleCollisionResult.NONE;
			}
		});
		if(swordCollision == true) {
			aspectSystem.addAspect(new SwordHit(uniqueID) {
				@Override
				public SwordHitResult hit(float swordX, float swordY, float swordW, float swordH, float ownerCX, float ownerCY, Direction direction, int dmg, boolean powerUped, SwordState swordState, int counter) {
					if(remove == false && holeFallComponent.isActive() == false) {
						if(callback.onItemTake() == true) {
							setToRemove();
						}
					}
					return new SwordHitResult(SwordHitResultType.NONE, 0, 0);
				}
			});
		}

		float wMod = w - 10;
		if(wMod != 0) {
			wMod /= 2.0f;
		}
		float hMod = h - 10;
		if(hMod != 0) {
			hMod /= 2.0f;
		}
		fallAnimation = new FallAnimation(drawComponent, ()-> {
			return holeFallComponent.isActive();
		}, wMod, hMod);

		draw();
	}

	@Override
	public void update() {
		if(holeFallComponent.isActive() == false) {
			jumpComponent.update();
			bounceComponent.update();
			if(disappearTimer > 0) {
				disappearTimer -= 1;
			}
			else if(disappearTimer == 0) {
				setToRemove();
			}
			animationCounter += 1;
			if(animationCounter >= 14) {
				animationCounter = 0;
			}
		}

		holeFallComponent.update();
		fallAnimation.update();
	}
	@Override
	public void draw() {
		if(disappearTimer <= 30) {
			drawComponent.setVisible(disappearTimer % 8 < 4);
		}

		if(animationCounter < 7) {
			drawComponent.setTexturePosition(tx1, ty1);
		}
		else {
			drawComponent.setTexturePosition(tx2, ty2);
		}

		if(holeFallComponent.isActive() == true) {
			drawComponent.setVisible(true);
		}
		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
		drawComponent.setHeight(-height);

		fallAnimation.draw();

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
		shadow.remove();
		spatialComponent.remove();
		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}
}
