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

public class Spark implements IGameObject {
	private TextureDrawComponent drawComponent;
	private IEnemyController enemyController;
	private boolean remove;

	private int frame;
	private int dir;
	private boolean defaultLeft;

	public Spark(
			float x, float y, boolean defaultLeftArg, int dirArg,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			ILinkData linkData,
			IEnemyDefeatedPrize prize,
			TileMapSystem tileMapSystem) {
		prize.setRupeeDropChance(50);
		prize.setHeartDropChance(25);
		defaultLeft = defaultLeftArg;
		dir = dirArg;

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(426, 0, 16, 16);
		drawComponent.setSize(16, 16);
		drawComponent.setSpriteOffset(-3, -3);

		enemyController = new EnemyController2(-1, x, y, 10, 10, 1, false, -1, 2, true, soundSystem, spatialSystem, uniqueIDManager, aspectSystem, new ObjectControllerCallbackStandard() {
			@Override
			public boolean onDeath(IObjectController controller) {
				prize.createPickup((int)controller.getCenterX(), (int)controller.getCenterY());
				return false;
			}
			@Override
			public SwordHitResult onSwordCollision(IObjectController controller, float swordX, float swordY, float swordW, float swordH, float linkCX, float linkCY, int dmg, boolean powerUped, SwordState state, int swordCounter) {
				return new SwordHitResult(SwordHitResultType.NONE, 0, 0);
			}
			@Override
			public boolean onBombCollision(IObjectController controller, float bombCX, float bombCY, boolean isPlayerOwner) {
				return false;
			}
		}) {
			@Override
			public void update(IObjectController controller) {
				float speed = 1.1f;
				if(defaultLeft == true) {
					if(dir == 0) {
						controller.move(-speed,  0);
						boolean leftCollision = tileMapSystem.collisionCheck(controller.getX(), controller.getY(), 10, 10) || controller.getX() <= 0;
						boolean upCollision   = tileMapSystem.collisionCheck(controller.getX(), controller.getY() - 1, 10, 10) || controller.getY() <= 0;
						if(leftCollision == false && upCollision == false) {
							dir = 2;
						}
						if(leftCollision == true) {
							dir = 3;
						}
					}
					else if(dir == 1) {
						controller.move( speed,  0);
						boolean rightCollision = tileMapSystem.collisionCheck(controller.getX(), controller.getY(), 10, 10) || controller.getX() + 10 >= 160;
						boolean downCollision   = tileMapSystem.collisionCheck(controller.getX(), controller.getY() + 1, 10, 10) || controller.getY() + 10 >= 128;
						if(rightCollision == false && downCollision == false) {
							dir = 3;
						}
						if(rightCollision == true) {
							dir = 2;
						}
					}
					else if(dir == 2) {
						controller.move( 0, -speed);
						boolean upCollision   = tileMapSystem.collisionCheck(controller.getX(), controller.getY(), 10, 10) || controller.getY() <= 0;
						boolean rightCollision = tileMapSystem.collisionCheck(controller.getX() + 1, controller.getY(), 10, 10) || controller.getX() + 10 >= 160;
						if(rightCollision == false && upCollision == false) {
							dir = 1;
						}
						if(upCollision == true) {
							dir = 0;
						}
					}
					else if(dir == 3) {
						controller.move( 0,  speed);
						boolean downCollision   = tileMapSystem.collisionCheck(controller.getX(), controller.getY(), 10, 10) || controller.getY() + 10 >= 128;
						boolean leftCollision = tileMapSystem.collisionCheck(controller.getX() - 1, controller.getY(), 10, 10) || controller.getX() <= 0;
						if(leftCollision == false && downCollision == false) {
							dir = 0;
						}
						if(downCollision == true) {
							dir = 1;
						}
					}
				}
				else {
					if(dir == 0) {
						controller.move(-speed,  0);
						boolean leftCollision = tileMapSystem.collisionCheck(controller.getX(), controller.getY(), 10, 10) || controller.getX() <= 0;
						boolean downCollision   = tileMapSystem.collisionCheck(controller.getX(), controller.getY() + 1, 10, 10) || controller.getY() + 10 >= 128;
						if(leftCollision == false && downCollision == false) {
							dir = 3;
						}
						if(leftCollision == true) {
							dir = 2;
						}
					}
					else if(dir == 1) {
						controller.move( speed,  0);
						boolean rightCollision = tileMapSystem.collisionCheck(controller.getX(), controller.getY(), 10, 10) || controller.getX() + 10 >= 160;
						boolean upCollision   = tileMapSystem.collisionCheck(controller.getX(), controller.getY() - 1, 10, 10) || controller.getY() <= 0;
						if(rightCollision == false && upCollision == false) {
							dir = 2;
						}
						if(rightCollision == true) {
							dir = 3;
						}
					}
					else if(dir == 2) {
						controller.move( 0, -speed);
						boolean upCollision   = tileMapSystem.collisionCheck(controller.getX(), controller.getY(), 10, 10) || controller.getY() <= 0;
						boolean leftCollision = tileMapSystem.collisionCheck(controller.getX() - 1, controller.getY(), 10, 10) || controller.getX() <= 0;
						if(leftCollision == false && upCollision == false) {
							dir = 0;
						}
						if(upCollision == true) {
							dir = 1;
						}
					}
					else if(dir == 3) {
						controller.move( 0,  speed);
						boolean downCollision   = tileMapSystem.collisionCheck(controller.getX(), controller.getY(), 10, 10) || controller.getY() + 10 >= 128;
						boolean rightCollision = tileMapSystem.collisionCheck(controller.getX() + 1, controller.getY(), 10, 10) || controller.getX() + 10 >= 160;
						if(rightCollision == false && downCollision == false) {
							dir = 1;
						}
						if(downCollision == true) {
							dir = 0;
						}
					}
				}
			}
		};
	}

	@Override
	public void update() {
		enemyController.updateController();

		frame += 1;
		if(frame > 6) {
			frame = 0;
		}
	}

	@Override
	public void draw() {
		drawComponent.setPosition(
			enemyController.getController().getX(),
			enemyController.getController().getY());
		if(frame < 3) {
			drawComponent.setInvert(false, 1, 0.69f, 0.19f);
		}
		else {
			drawComponent.setInvert(true, 1, 0.69f, 0.19f);
		}
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
