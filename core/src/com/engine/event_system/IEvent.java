package com.engine.event_system;

public interface IEvent {
	void update();
	boolean shouldRemove();
}
