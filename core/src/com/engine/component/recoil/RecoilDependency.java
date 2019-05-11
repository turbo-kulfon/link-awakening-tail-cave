package com.engine.component.recoil;

public interface RecoilDependency {
	float getCenterX();
	float getCenterY();

	void update(float dx, float dy);
}
