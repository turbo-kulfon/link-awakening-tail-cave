package com.engine.event_system;

public interface IEventSystem {
	void addEvent(IEvent event);

	void update();
	void clear();
}
