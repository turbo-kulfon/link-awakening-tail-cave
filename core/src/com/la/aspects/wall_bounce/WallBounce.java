package com.la.aspects.wall_bounce;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspect;
import com.engine.spatial.ISpatialComponent;
import com.engine.util.CollisionDetection;
import com.engine.util.ICollisionDetection;
import com.engine.util.ISAT;
import com.engine.util.SAT;

public class WallBounce implements IAspect {
	private int uniqueID;
	private ISpatialComponent spatialComponent;
	private ISAT sat;
	private WallBounceCollisionResolve collisionResolve;
	private ICollisionDetection collisionDetection = new CollisionDetection();

	public WallBounce(
			int uniqueID,
			ISpatialComponent spatialComponent) {
		this.uniqueID = uniqueID;
		this.spatialComponent = spatialComponent;
		sat = new SAT();
		collisionResolve = new WallBounceStandard(spatialComponent);
	}
	public WallBounce(
			int uniqueID,
			ISpatialComponent spatialComponent,
			WallBounceCollisionResolve collisionResolve) {
		this.uniqueID = uniqueID;
		this.spatialComponent = spatialComponent;
		sat = new SAT();
		this.collisionResolve = collisionResolve;
	}

	public int bounce(float wallX, float wallY, float wallW, float wallH, int wallDirection) {
		return satBounce(wallX, wallY, wallW, wallH, wallDirection);
	}
	public int subPartBounce(float wallX, float wallY, float wallW, float wallH) {
		if(collisionDetection.collisionDetect(
				spatialComponent.getX() + spatialComponent.getDeltaX(),
				spatialComponent.getY() + spatialComponent.getDeltaY(),
				spatialComponent.getW(), spatialComponent.getH(),
			wallX, wallY, wallW, wallH) == true) {
			return satBounce(wallX, wallY, wallW, wallH, -1);
		}
		return -1;
	}
	public void setCollisionResolve(WallBounceCollisionResolve collisionResolve) {
		this.collisionResolve = collisionResolve;
	}

	@Override
	public int getID() {
		return uniqueID;
	}
	@Override
	public AspectType getType() {
		return AspectType.WALL_BOUNCE;
	}

	private int satBounce(float wallX, float wallY, float wallW, float wallH, int wallDirection) {
		int result = sat.sat(
				spatialComponent.getX(), spatialComponent.getY(), spatialComponent.getW(), spatialComponent.getH(),
				wallX, wallY, wallW, wallH);
		if(result == 0) {
			collisionResolve.onLeftSideCollision(wallX, wallY, wallW, wallH, wallDirection);
		}
		else if(result == 1) {
			collisionResolve.onRightSideCollision(wallX, wallY, wallW, wallH, wallDirection);
		}
		else if(result == 2) {
			collisionResolve.onUpSideCollision(wallX, wallY, wallW, wallH, wallDirection);
		}
		else if(result == 3) {
			collisionResolve.onDownSideCollision(wallX, wallY, wallW, wallH, wallDirection);
		}
		return result;
	}
}
