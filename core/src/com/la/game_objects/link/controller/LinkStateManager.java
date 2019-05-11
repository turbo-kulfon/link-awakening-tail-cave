package com.la.game_objects.link.controller;

import java.util.HashMap;
import java.util.Map;

public class LinkStateManager {
	private Map<StateType, ILinkState> states = new HashMap<>();
	private ILinkState currentState;

	public void addState(ILinkState linkState) {
		states.put(linkState.getType(), linkState);
	}
	public void changeState(StateType type) {
		if(currentState != null) {
			if(currentState.getType() != type) {
				currentState = states.get(type);
				currentState.initialize();
			}
		}
		else {
			currentState = states.get(type);
			currentState.initialize();
		}
	}

	public void leftButtonPressed() {
		currentState.leftButtonPressed();
	}
	public void rightButtonPressed() {
		currentState.rightButtonPressed();
	}
	public void upButtonPressed() {
		currentState.upButtonPressed();
	}
	public void downButtonPressed() {
		currentState.downButtonPressed();
	}

	public void BButtonPressed(boolean justPressed) {
		currentState.BButtonPressed(justPressed);
	}
	public void AButtonPressed(boolean justPressed) {
		currentState.AButtonPressed(justPressed);
	}

	public void update() {
		currentState.update();
	}
	public void draw() {
		currentState.draw();
	}
}
