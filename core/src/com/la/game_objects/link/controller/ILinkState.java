package com.la.game_objects.link.controller;

public interface ILinkState {
	void initialize();

	void leftButtonPressed();
	void rightButtonPressed();
	void upButtonPressed();
	void downButtonPressed();

	void BButtonPressed(boolean justPressed);
	void AButtonPressed(boolean justPressed);

	void update();
	void draw();

	StateType getType();
}
