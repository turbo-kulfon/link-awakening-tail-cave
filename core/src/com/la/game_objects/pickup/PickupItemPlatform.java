package com.la.game_objects.pickup;

import com.engine.aspect.IAspectSystem;
import com.engine.component.sword.SwordState;
import com.engine.direction.Direction;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.LinkCollision;
import com.la.aspects.RoomTransition;
import com.la.aspects.SwordHit;
import com.la.aspects.wall_bounce.WallBounceStandard;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.game_objects.link.controller.platform.BounceComponent;
import com.la.game_objects.link.controller.platform.BounceComponent.BounceComponentDependency;
import com.la.game_objects.link.controller.platform.GravityComponent;
import com.la.game_objects.link.controller.platform.GravityComponent.GravityComponentDependency;
import com.la.game_objects.link.controller.platform.JumpPlatformComponent;
import com.la.game_objects.link.controller.platform.JumpPlatformComponent.JumpPlatformDependency;

public class PickupItemPlatform implements IGameObject {
	public interface PickupItemPlatformCallback {
		boolean onItemTake();
	}

	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private GravityComponent gravityComponent;
	private JumpPlatformComponent jumpComponent;
	private BounceComponent bounceComponent;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private int uniqueID;
	private boolean remove;
	private boolean onGround;
	private int animationCounter, disappearTimer;
	private int tx1, ty1, tx2, ty2;

	public PickupItemPlatform(
			float x, float y, float w, float h, int texX, int texY, int texX2, int texY2, boolean disappear,
			boolean swordCollision,
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			PickupItemPlatformCallback callback) {
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;

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

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x - w/2.0f, y - h, w, h);

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
		jumpComponent = new JumpPlatformComponent(new JumpPlatformDependency() {
			@Override
			public void unsetOnGround() {
				onGround = false;
			}
			@Override
			public void setDeltaY(float dy) {
				spatialComponent.setDeltaY(dy);
			}
			@Override
			public boolean isOnGround() {
				return onGround;
			}
			@Override
			public void onJump() {
			}
		});
		bounceComponent = new BounceComponent(new BounceComponentDependency() {
			@Override
			public boolean onGround() {
				return onGround;
			}
			@Override
			public void bounce(float delta) {
				jumpComponent.jump(delta);
			}
		});
		bounceComponent.reset(3);

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		aspectSystem.addAspect(new LinkCollision(uniqueID) {
			@Override
			public void collision(float linkX, float linkY, float linkW, float linkH, float linkHeight, boolean blocked) {
				if(remove == false) {
					if(callback.onItemTake() == true) {
						setToRemove();
					}
				}
			}
		});
		aspectSystem.addAspect(new WallBounce(uniqueID, spatialComponent, new WallBounceStandard(spatialComponent) {
			@Override
			public void onUpSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onUpSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				onGround = true;
			}
		}));
		if(swordCollision == true) {
			aspectSystem.addAspect(new SwordHit(uniqueID) {
				@Override
				public SwordHitResult hit(float swordX, float swordY, float swordW, float swordH, float ownerCX, float ownerCY, Direction direction, int dmg, boolean powerUped, SwordState swordState, int counter) {
					if(remove == false) {
						if(callback.onItemTake() == true) {
							setToRemove();
						}
					}
					return new SwordHitResult(SwordHitResultType.NONE, 0, 0);
				}
			});
		}

		draw();
	}

	@Override
	public void update() {
		gravityComponent.update();
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

		onGround = false;
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

		drawComponent.setPosition(spatialComponent.getX(), spatialComponent.getY());
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
