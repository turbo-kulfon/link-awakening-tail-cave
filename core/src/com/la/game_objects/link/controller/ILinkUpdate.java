package com.la.game_objects.link.controller;

public interface ILinkUpdate {
	public enum LinkState {
		TOP_DOWN,
		PLATFORM
	}

	void control(ILinkController controller);
	void switchState(LinkState state);

	void leftKeyPressed();
	void rightKeyPressed();
	void upKeyPressed();
	void downKeyPressed();
	void BButtonPressed();
	void AButtonPressed();

	void update();

	void remove();
}
