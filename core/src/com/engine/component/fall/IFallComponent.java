package com.engine.component.fall;

public interface IFallComponent {
	void fall(float holeX, float holeY);
	void update();
	boolean isFalling();
	void stop();
}
