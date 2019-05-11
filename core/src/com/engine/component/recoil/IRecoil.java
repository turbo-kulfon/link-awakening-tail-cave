package com.engine.component.recoil;

public interface IRecoil {
	boolean hit(float x, float y, float distance, int frameTime);
	void update();
	void stop();

	boolean isActive();
}
