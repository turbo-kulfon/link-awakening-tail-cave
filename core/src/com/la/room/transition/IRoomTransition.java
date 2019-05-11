package com.la.room.transition;

public interface IRoomTransition {
	void gotoRoom(int roomID);

	void goLeft();
	void goRight();
	void goUp();
	void goDown();
}
