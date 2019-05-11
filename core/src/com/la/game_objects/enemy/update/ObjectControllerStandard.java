package com.la.game_objects.enemy.update;

import com.engine.component.jump.IJumpComponent;
import com.engine.component.recoil.IRecoil;
import com.engine.spatial.ISpatialComponent;

public abstract class ObjectControllerStandard implements IObjectController {
	private ISpatialComponent spatialComponent;
	private IJumpComponent jumpComponent;
	private IRecoil recoil;

	public ObjectControllerStandard(
			ISpatialComponent spatialComponent,
			IJumpComponent jumpComponent,
			IRecoil recoil) {
		this.spatialComponent = spatialComponent;
		this.jumpComponent = jumpComponent;
		this.recoil = recoil;
	}

	@Override
	public void moveLeft(float speed) {
		spatialComponent.setDeltaX(-speed);
	}
	@Override
	public void moveRight(float speed) {
		spatialComponent.setDeltaX(speed);
	}
	@Override
	public void moveUp(float speed) {
		spatialComponent.setDeltaY(-speed);
	}
	@Override
	public void moveDown(float speed) {
		spatialComponent.setDeltaY(speed);
	}
	@Override
	public void stop() {
		spatialComponent.setDelta(0, 0);
	}
	@Override
	public void setMoveDeltaX(float dx) {
		spatialComponent.setDeltaX(dx);
	}
	@Override
	public void setMoveDeltaY(float dy) {
		spatialComponent.setDeltaY(dy);
	}
	@Override
	public void setMoveDelta(float dx, float dy) {
		spatialComponent.setDelta(dx, dy);
	}

	@Override
	public void setX(float x) {
		spatialComponent.setX(x);
	}
	@Override
	public void setY(float y) {
		spatialComponent.setY(y);
	}
	@Override
	public void move(float dx, float dy) {
		spatialComponent.move(dx, dy);
	}

	@Override
	public void recoil(float cx, float cy, float distance, int timeFrame) {
		recoil.hit(cx, cy, distance, timeFrame);
	}
	@Override
	public void jump(float initialDelta) {
		jumpComponent.jump(initialDelta);
	}

	@Override
	public float getX() {
		return spatialComponent.getX();
	}
	@Override
	public float getY() {
		return spatialComponent.getY();
	}
	@Override
	public float getW() {
		return spatialComponent.getW();
	}
	@Override
	public float getH() {
		return spatialComponent.getH();
	}
	@Override
	public float getCenterX() {
		return spatialComponent.getCenterX();
	}
	@Override
	public float getCenterY() {
		return spatialComponent.getCenterY();
	}
}
