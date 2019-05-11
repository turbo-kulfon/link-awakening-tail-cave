package com.la.game_objects.enemy.moldorm;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.component.sword.SwordState;
import com.engine.component.tail.Trail;
import com.engine.direction.Direction;
import com.engine.direction.Direction8;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.sound.SoundSystem;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.engine.util.Coordinate;
import com.engine.util.ICoordinate;
import com.engine.util.ICoordinate.Vector;
import com.engine.util.IRNG;
import com.la.aspects.EnemyTag;
import com.la.aspects.HoleCollision;
import com.la.aspects.RoomTransition;
import com.la.aspects.SwordHit;
import com.la.aspects.enemy_hit.EnemyHit;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.aspects.wall_bounce.WallBounceStandard;
import com.la.factory.IRoomFactory;
import com.la.game_objects.effect.Poof2.Poof2Callback;
import com.la.game_objects.pickup.PickupItemTopDown.PickupItemTopDownCallback;

public class Moldorm implements IGameObject {
	public interface MoldormCallback {
		void onDeath();
		void drawText(String text);
	}

	private enum MoldormState {
		WALK,
		HIT,
		DIE
	}

	private SoundSystem soundSystem;
	private TextureDrawComponent segments[] = new TextureDrawComponent[5];
	private ISpatialComponent segmentsSpatial[] = new ISpatialComponent[5];

	private IAspectSystem aspectSystem;
	private IUniqueIDManager uniqueIDManager;
	private IRoomFactory roomFactory;
	private IRNG rng;
	private ICoordinate coordinate;

	private MoldormState state = MoldormState.WALK;
	private Trail tailTrail;
	private Direction8 direction;
	private int counter, textShow = 16, fastWalk, hp = 8, spikeAnimation;
	private MoldormCallback callback;

	private boolean remove;
	private int uniqueIDs[] = new int[5];

	public Moldorm(int x, int y,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			IUniqueIDManager uniqueIDManager,
			IRoomFactory roomFactory,
			IRNG rng,
			MoldormCallback callback
			) {
		this.soundSystem = soundSystem;
		this.aspectSystem = aspectSystem;
		this.uniqueIDManager = uniqueIDManager;
		this.roomFactory = roomFactory;
		this.rng = rng;
		this.callback = callback;

		coordinate = new Coordinate();

		createSpikeSegment(gfxSystem, spatialSystem, 1);
		createSmallSegment(gfxSystem, spatialSystem, 2);
		createBigSegment(gfxSystem, spatialSystem, 3);
		createBigSegment(gfxSystem, spatialSystem, 4);
		createHeadSegment(gfxSystem, spatialSystem, x, y, 0);

		direction = new Direction8();
		tailTrail = new Trail(55);
	}

	@Override
	public void update() {
		if(textShow > 0) {
			textShow -= 1;
		}
		else if(textShow == 0) {
			textShow = -1;
			callback.drawText(
				  "BUZZZZZ! BUZZZZ!|"
				+ "   OUTZZZIDER!");
		}
		if(state == MoldormState.WALK) {
			float speed = 1.0f;
			if(fastWalk > 0 || hp <= 4) {
				fastWalk -= 1;
				speed = 1.5f;
			}
			counter -= 1;
			if(counter <= 0) {
				int newDirection = rng.getRNG(0, 100);
				if(newDirection <= 75) {
					direction.turnLeft();
				}
				else {
					direction.turnRight();
				}
				counter = 20;
			}

			if(direction.getDirection() == Direction.UP) {
				segmentsSpatial[0].setDelta(0, -speed);
			}
			else if(direction.getDirection() == Direction.LEFT_UP) {
				segmentsSpatial[0].setDelta(-speed, -speed);
			}
			else if(direction.getDirection() == Direction.LEFT) {
				segmentsSpatial[0].setDelta(-speed, 0);
			}
			else if(direction.getDirection() == Direction.LEFT_DOWN) {
				segmentsSpatial[0].setDelta(-speed, speed);
			}
			else if(direction.getDirection() == Direction.DOWN) {
				segmentsSpatial[0].setDelta(0, speed);
			}
			else if(direction.getDirection() == Direction.RIGHT_DOWN) {
				segmentsSpatial[0].setDelta(speed, speed);
			}
			else if(direction.getDirection() == Direction.RIGHT) {
				segmentsSpatial[0].setDelta(speed, 0);
			}
			else if(direction.getDirection() == Direction.RIGHT_UP) {
				segmentsSpatial[0].setDelta(speed, -speed);
			}
			tailTrail.update(
					segmentsSpatial[0].getCenterX(),
					segmentsSpatial[0].getCenterY());
			if(speed > 1 && counter % 2 == 1) {
				tailTrail.update(
						segmentsSpatial[0].getCenterX(),
						segmentsSpatial[0].getCenterY());
			}
			if(speed > 1) {
				if(counter % 8 == 0) {
					soundSystem.moldormWalk();
				}
			}
			else {
				if(counter % 10 == 0) {
					soundSystem.moldormWalk();
				}
			}
		}
		else if(state == MoldormState.HIT) {
			tailTrail.update(
					segmentsSpatial[0].getCenterX(),
					segmentsSpatial[0].getCenterY());
			segmentsSpatial[0].stop();
			counter -= 1;
			if(counter <= 0) {
				state = MoldormState.WALK;
				fastWalk = 180;
				tailTrail.reset();
			}
		}
		else if(state == MoldormState.DIE) {
			segmentsSpatial[0].stop();
			counter -= 1;
			if(counter <= 0) {
				setToRemove();
				soundSystem.moldormSegmentExplode2();
				roomFactory.createBigHeart(segmentsSpatial[0].getCenterX(), segmentsSpatial[0].getCenterY(), new PickupItemTopDownCallback() {
					@Override
					public boolean onItemTake() {
						callback.onDeath();
						return true;
					}
					@Override
					public void onBounce() {
					}
				});
			}
			else if(counter == 180) {
				destroySegment(1);
			}
			else if(counter == 150) {
				destroySegment(2);
			}
			else if(counter == 120) {
				destroySegment(3);
			}
			else if(counter == 90) {
				destroySegment(4);
			}
			if(counter > 0 && counter <= 60 && counter % 4 == 0) {
				Vector v = coordinate.angleToDelta(counter * 6);
				soundSystem.moldormSegmentExplode();
				roomFactory.createPoof2((int)(segmentsSpatial[0].getCenterX() + v.x * 8), (int)(segmentsSpatial[0].getCenterY() + v.y * 8), new Poof2Callback() {
					@Override public void onUpdate(int counter) {}
					@Override public void onEnd() {}
				});
			}
		}

		spikeAnimation += 1;
		if(fastWalk > 0) {
			spikeAnimation += 1;
		}
		if(spikeAnimation > 24) {
			spikeAnimation = 0;
		}

//		if(fastWalk > 0) {
//			updateSegment(4,  9 - 3, 6, 6);
//			updateSegment(3, 17 - 6, 6, 6);
//			updateSegment(2, 26 - 9, 5, 5);
//			updateSegment(1, 33 - 11, 5, 5);
//		}
//		else {
//			updateSegment(4,  6, 6, 6);
//			updateSegment(3, 11, 6, 6);
//			updateSegment(2, 17, 5, 5);
//			updateSegment(1, 22, 5, 5);

			updateSegment(4,  9, 6, 6);
			updateSegment(3, 17, 6, 6);
			updateSegment(2, 26, 5, 5);
			updateSegment(1, 33, 5, 5);
//		}
	}
	@Override
	public void draw() {
		if(direction.getDirection() == Direction.UP) {
			segments[0].setRotation(0, 12, 14);
		}
		else if(direction.getDirection() == Direction.LEFT_UP) {
			segments[0].setRotation(315, 12, 14);
		}
		else if(direction.getDirection() == Direction.LEFT) {
			segments[0].setRotation(270, 12, 14);
		}
		else if(direction.getDirection() == Direction.LEFT_DOWN) {
			segments[0].setRotation(235, 12, 14);
		}
		else if(direction.getDirection() == Direction.DOWN) {
			segments[0].setRotation(180, 12, 14);
		}
		else if(direction.getDirection() == Direction.RIGHT_DOWN) {
			segments[0].setRotation(135, 12, 14);
		}
		else if(direction.getDirection() == Direction.RIGHT) {
			segments[0].setRotation(90, 12, 14);
		}
		else if(direction.getDirection() == Direction.RIGHT_UP) {
			segments[0].setRotation(45, 12, 14);
		}
		for(int i  = 0; i < 5; i++) {
			segments[i].setPosition(
				segmentsSpatial[i].getX(),
				segmentsSpatial[i].getY());
		}
		for (TextureDrawComponent drawComponent : segments) {
			if(state == MoldormState.HIT || state == MoldormState.DIE) {
				if(counter % 10 <= 4) {
					drawComponent.setInvert(true, 1, 0.69f, 0.19f);
				}
				else {
					drawComponent.setInvert(false, 1, 0.69f, 0.19f);
				}
			}
			else {
				drawComponent.setInvert(false, 1, 0.69f, 0.19f);
			}
		}
		if(spikeAnimation >= 0 && spikeAnimation < 6) {
			segments[1].setTexture(465, 118, 16, 16);
			segments[1].setSize(16, 16);
			segments[1].setSpriteOffset(-3, -3);
		}
		else if(spikeAnimation >= 6 && spikeAnimation < 12) {
			segments[1].setTexture(465, 118, 16, 16);
			segments[1].setSize(16, 16);
			segments[1].setSpriteOffset(-3, -3);
			segments[1].setInvert(true, 1, 0.69f, 0.19f);
		}
		else if(spikeAnimation >= 12 && spikeAnimation < 18) {
			segments[1].setTexture(451, 120, 14, 14);
			segments[1].setSize(14, 14);
			segments[1].setSpriteOffset(-2, -2);
		}
		else if(spikeAnimation >= 18 && spikeAnimation < 24) {
			segments[1].setTexture(451, 120, 14, 14);
			segments[1].setSize(14, 14);
			segments[1].setSpriteOffset(-2, -2);
			segments[1].setInvert(true, 1, 0.69f, 0.19f);
		}
		if(state == MoldormState.DIE) {
			segments[1].setInvert(false, 1, 0.69f, 0.19f);
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
		for (TextureDrawComponent segment : segments) {
			segment.remove();
		}
		for (ISpatialComponent spatialComponent : segmentsSpatial) {
			spatialComponent.remove();
		}
		for(Integer id : uniqueIDs) {
			aspectSystem.removeAspects(id);
			uniqueIDManager.returnID(id);
		}
	}

	private void createHeadSegment(GFXSystem gfxSystem, SpatialSystem spatialSystem, int x, int y, int index) {
		int uniqueID = uniqueIDManager.getUniqueID();

		TextureDrawComponent drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(457, 90, 24, 28);
		drawComponent.setSize(24, 28);
		drawComponent.setSpriteOffset(-4, -6);

		ISpatialComponent spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setPosition(x - 12, y - 14);
		spatialComponent.setSize(16, 16);
		spatialComponent.setCollisionResponse((collidedID)-> {
			if(state != MoldormState.DIE) {
				EnemyHit enemyHit = aspectSystem.getAspect(collidedID, AspectType.ENEMY_HIT);
				if(enemyHit != null) {
					enemyHit.hit(
							spatialComponent.getX(), spatialComponent.getY(),
							spatialComponent.getW(), spatialComponent.getH(),
							direction.getDirection(), -1, 4, 1);
				}
			}
		});

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		aspectSystem.addAspect(new SwordHit(uniqueID) {
			@Override
			public SwordHitResult hit(float swordX, float swordY, float swordW, float swordH, float ownerCX, float ownerCY,
					Direction ownerDirection, int dmg, boolean powerUped, SwordState swordState, int counter) {
				if(state != MoldormState.DIE) {
					return new SwordHitResult(SwordHitResultType.DEFLECT, spatialComponent.getCenterX(), spatialComponent.getCenterY());
				}
				return new SwordHitResult(SwordHitResultType.NONE, spatialComponent.getCenterX(), spatialComponent.getCenterY());
			}
		});
		WallBounce wallBounce = new WallBounce(uniqueID, spatialComponent, new WallBounceStandard(spatialComponent) {
			@Override
			public void onLeftSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onLeftSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				direction.bounce();
			}
			@Override
			public void onRightSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onRightSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				direction.bounce();
			}
			@Override
			public void onUpSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onUpSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				direction.bounce();
			}
			@Override
			public void onDownSideCollision(float collidedX, float collidedY, float collidedW, float collidedH, int wallDirection) {
				super.onDownSideCollision(collidedX, collidedY, collidedW, collidedH, wallDirection);
				direction.bounce();
			}
		});
		aspectSystem.addAspect(wallBounce);
		aspectSystem.addAspect(new HoleCollision(uniqueID) {
			@Override
			public HoleCollisionResult collision(float holeX, float holeY, float holeW, float holeH, boolean restoreLastPosition) {
				wallBounce.bounce(holeX, holeY, holeW, holeH, -1);
				return HoleCollisionResult.NONE;
			}
		});
		aspectSystem.addAspect(new EnemyTag(uniqueID));

		uniqueIDs[index] = uniqueID;
		segments[index] = drawComponent;
		segmentsSpatial[index] = spatialComponent;
	}
	private void createSmallSegment(GFXSystem gfxSystem, SpatialSystem spatialSystem, int index) {
		int uniqueID = uniqueIDManager.getUniqueID();

		TextureDrawComponent drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(443, 106, 14, 14);
		drawComponent.setSize(14, 14);
		drawComponent.setSpriteOffset(-2, -2);
		drawComponent.setVisible(false);

		ISpatialComponent spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setSize(10, 10);

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));

		uniqueIDs[index] = uniqueID;
		segments[index] = drawComponent;
		segmentsSpatial[index] = spatialComponent;
	}
	private void createBigSegment(GFXSystem gfxSystem, SpatialSystem spatialSystem, int index) {
		int uniqueID = uniqueIDManager.getUniqueID();

		TextureDrawComponent drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(441, 90, 16, 16);
		drawComponent.setSize(16, 16);
		drawComponent.setSpriteOffset(-2, -2);
		drawComponent.setVisible(false);

		ISpatialComponent spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setSize(12, 12);

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));

		uniqueIDs[index] = uniqueID;
		segments[index] = drawComponent;
		segmentsSpatial[index] = spatialComponent;
	}
	private void createSpikeSegment(GFXSystem gfxSystem, SpatialSystem spatialSystem, int index) {
		int uniqueID = uniqueIDManager.getUniqueID();

		TextureDrawComponent drawComponent = gfxSystem.createTextureDrawComponent(0);
		drawComponent.setTexture(451, 120, 14, 14);
		drawComponent.setSize(14, 14);
		drawComponent.setSpriteOffset(-2, -2);
		drawComponent.setVisible(false);

		ISpatialComponent spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setSize(10, 10);

		aspectSystem.addAspect(new RoomTransition(uniqueID, false, spatialComponent));
		aspectSystem.addAspect(new SwordHit(uniqueID) {
			@Override
			public SwordHitResult hit(float swordX, float swordY, float swordW, float swordH, float ownerCX, float ownerCY,
					Direction ownerDirection, int dmg, boolean powerUped, SwordState swordState, int swordCounter) {
				if(state == MoldormState.WALK && tailTrail.isIndexVisible(33)) {
					hp -= dmg;
					if(hp > 0) {
						state = MoldormState.HIT;
						counter = 60;
						soundSystem.bossHit();
					}
					else {
						state = MoldormState.DIE;
						counter = 300;
						soundSystem.nightmareDie();
					}
					return new SwordHitResult(SwordHitResultType.HIT, spatialComponent.getCenterX(), spatialComponent.getCenterY());
				}
				return new SwordHitResult(SwordHitResultType.NONE, spatialComponent.getCenterX(), spatialComponent.getCenterY());
			}
		});

		uniqueIDs[index] = uniqueID;
		segments[index] = drawComponent;
		segmentsSpatial[index] = spatialComponent;
	}
	private void updateSegment(int index, int trailIndex, int offsetX, int offsetY) {
		Vector v = tailTrail.getPosition(trailIndex);
		if(v != null) {
			if(state != MoldormState.DIE) {
				segments[index].setVisible(true);
			}
			segmentsSpatial[index].setPosition(v.x - offsetX, v.y - offsetY);
		}
		else {
			segmentsSpatial[index].setPosition(
				segmentsSpatial[index].getCenterX() - offsetX,
				segmentsSpatial[index].getCenterY() - offsetY);
		}
	}
	private void destroySegment(int index) {
		segments[index].setVisible(false);
		soundSystem.moldormSegmentExplode();
		roomFactory.createPoof2(
				(int)segmentsSpatial[index].getCenterX(),
				(int)segmentsSpatial[index].getCenterY(),
				new Poof2Callback() {
			@Override
			public void onUpdate(int counter) {
			}
			@Override
			public void onEnd() {
			}
		});
	}
}
