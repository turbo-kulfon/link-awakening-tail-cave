package com.engine.component.sword;

public interface ISwordComponent {
	void attack();
	void thrust();
	void update();
	void stop();

	void draw();

	boolean allowMove();
	boolean allowChangeDirection();

	float getSwordCenterX();
	float getSwordCenterY();
	SwordState getSwordState();

	void remove();
}
