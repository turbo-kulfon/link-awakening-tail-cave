package com.la.game_objects.enemy;

import com.engine.aspect.IAspectSystem;
import com.engine.component.sword.SwordState;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.SpatialSystem;
import com.engine.tile_map.TileMapSystem;
import com.la.aspects.SwordHit.SwordHitResult;
import com.la.aspects.SwordHit.SwordHitResultType;
import com.la.game_objects.enemy.update.EnemyController2;
import com.la.game_objects.enemy.update.IEnemyController;
import com.la.game_objects.enemy.update.IObjectController;
import com.la.game_objects.enemy.update.ObjectControllerCallbackStandard;
import com.la.game_objects.link.ILinkData;

public class BladeTrap implements IGameObject {
	private TextureDrawComponent drawComponent;
	private IEnemyController enemyController;

	private boolean remove;

	private int mode;
	private float axisDelta, maxPath;
	private boolean wallCollisionDetected;
	private boolean init = true;
	private float startX, startY;
	private int delayCounter;

	public BladeTrap(float x, float y, 
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			ILinkData linkData,
			TileMapSystem tileMapSystem) {
		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(410, 0, 16, 16);
		drawComponent.setSize(16, 16);

		enemyController = new EnemyController2(10, x, y, 16, 16, 1, false, 0, 4, true, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, new ObjectControllerCallbackStandard() {
			@Override
			public boolean onDeath(IObjectController controller) {return false;}
			@Override
			public void onLinkShieldCollision(IObjectController controller, float linkCX, float linkCY) {}
			@Override
			public SwordHitResult onSwordCollision(IObjectController controller, float swordX, float swordY, float swordW, float swordH, float linkCX, float linkCY, int dmg, boolean powerUped, SwordState state, int swordCounter) {
				return new SwordHitResult(SwordHitResultType.DEFLECT, controller.getCenterX(), controller.getCenterY());
			}
			@Override
			public boolean onBombCollision(IObjectController controller, float bombCX, float bombCY, boolean isPlayerOwner) {
				return false;
			}
			@Override
			public void onWallCollisionFromLeft(IObjectController controller) {
				super.onWallCollisionFromLeft(controller);
				wallCollisionDetected = true;
			}
			@Override
			public void onWallCollisionFromRight(IObjectController controller) {
				super.onWallCollisionFromRight(controller);
				wallCollisionDetected = true;
			}
			@Override
			public void onWallCollisionFromUp(IObjectController controller) {
				super.onWallCollisionFromUp(controller);
				wallCollisionDetected = true;
			}
			@Override
			public void onWallCollisionFromDown(IObjectController controller) {
				super.onWallCollisionFromDown(controller);
				wallCollisionDetected = true;
			}
		}) {
			@Override
			public void update(IObjectController controller) {
				float speed = 2;
				if(mode == 0) {
					controller.stop();
					if(linkData.getCenterY() >= controller.getY() && linkData.getCenterY() <= controller.getY() + 16) {
						if(tileMapSystem.isPathOnXAxisClear(controller.getCenterX(), controller.getCenterY(), linkData.getCenterX()) == true) {
							mode = 1;
							maxPath = 3 * 16;
							if(controller.getCenterX() > linkData.getCenterX()) {
								axisDelta = -speed;
							}
							else {
								axisDelta = speed;
							}
							soundSystem.bladeTrap();
						}
					}
					if(linkData.getCenterX() >= controller.getX() && linkData.getCenterX() <= controller.getX() + 16) {
						if(tileMapSystem.isPathOnYAxisClear(controller.getCenterX(), controller.getCenterY(), linkData.getCenterY()) == true) {
							mode = 2;
							maxPath = 2 * 16;
							if(controller.getCenterY() > linkData.getCenterY()) {
								axisDelta = -speed;
							}
							else {
								axisDelta = speed;
							}
							soundSystem.bladeTrap();
						}
					}
				}
				else if(mode == 1) {
					maxPath -= speed;
					if(wallCollisionDetected == false && maxPath > 0) {
						controller.setMoveDelta(axisDelta, 0);
					}
					else {
						mode = 3;
						controller.setFlying(true);
						delayCounter = 30;
						soundSystem.deflect();
					}
				}
				else if(mode == 2) {
					maxPath -= speed;
					if(wallCollisionDetected == false && maxPath > 0) {
						controller.setMoveDelta(0, axisDelta);
					}
					else {
						mode = 4;
						controller.setFlying(true);
						delayCounter = 30;
						soundSystem.deflect();
					}
				}
				else if(mode == 3) {
					if(delayCounter > 0) {
						controller.stop();
						delayCounter -= 1;
					}
					else {
						controller.setMoveDelta(-axisDelta / 4.0f, 0);
						if(axisDelta > 0) {
							if(controller.getX() <= startX) {
								mode = 0;
								controller.setFlying(false);
								controller.stop();
								controller.setX(startX);
							}
						}
						else {
							if(controller.getX() >= startX) {
								mode = 0;
								controller.setFlying(false);
								controller.stop();
								controller.setX(startX);
							}
						}
					}
				}
				else if(mode == 4) {
					if(delayCounter > 0) {
						controller.stop();
						delayCounter -= 1;
					}
					else {
						controller.setMoveDelta(0, -axisDelta / 4.0f);
						if(axisDelta > 0) {
							if(controller.getY() <= startY) {
								mode = 0;
								controller.setFlying(false);
								controller.stop();
								controller.setY(startY);
							}
						}
						else {
							if(controller.getY() >= startY) {
								mode = 0;
								controller.setFlying(false);
								controller.stop();
								controller.setY(startY);
							}
						}
					}
				}

				wallCollisionDetected = false;
			}
		};
	}

	@Override
	public void update() {
		if(init == true) {
			startX = enemyController.getController().getX();
			startY = enemyController.getController().getY();
			init = false;
		}
		enemyController.updateController();
	}
	@Override
	public void draw() {
		drawComponent.setPosition(
			enemyController.getController().getX(),
			enemyController.getController().getY());
	}

	@Override
	public void setToRemove() {
		remove = true;
	}
	@Override
	public boolean shouldRemove() {
		return enemyController.shouldRemove() || remove == true;
	}
	@Override
	public void onRemove() {
		enemyController.remove();
		drawComponent.remove();
	}
}
