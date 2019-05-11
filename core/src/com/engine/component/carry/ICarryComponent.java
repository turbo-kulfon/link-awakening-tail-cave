package com.engine.component.carry;

public interface ICarryComponent {
	boolean pressedBButton();
	boolean pressedAButton();
	void update();
	void updatePosition();
	void toss();
	void tossDown();
	void take(int uniqueID);
	void reset();
	boolean isActive();
}
