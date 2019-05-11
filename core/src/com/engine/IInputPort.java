package com.engine;

public interface IInputPort {
	boolean isLeftButtonPressed();
	boolean isLeftButtonJustPressed();
	boolean isRightButtonPressed();
	boolean isRightButtonJustPressed();
	boolean isUpButtonPressed();
	boolean isUpButtonJustPressed();
	boolean isDownButtonPressed();
	boolean isDownButtonJustPressed();

	boolean isBButtonPressed();
	boolean isBButtonJustPressed();
	boolean isAButtonPressed();
	boolean isAButtonJustPressed();

	boolean isSelectButtonPressed();
	boolean isSelectButtonJustPressed();
	boolean isStartButtonJustPressed();
	boolean isStartButtonPressed();

	void blockBBUtton();
	void blockAButton();
}
