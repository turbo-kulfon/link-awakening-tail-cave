package com.la.game_objects.link.controller;

public interface ILinkController {
	void moveLeft();
	void moveRight();
	void moveUp();
	void moveDown();

	void jump(float startDelta);
}
