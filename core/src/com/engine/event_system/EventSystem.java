package com.engine.event_system;

import java.util.ArrayList;
import java.util.List;

public class EventSystem implements IEventSystem {
	private List<IEvent> events = new ArrayList<>();

	@Override
	public void addEvent(IEvent event) {
		if(events.contains(event) == false) {
			events.add(event);
		}
	}
	@Override
	public void update() {
		for (IEvent event : events) {
			event.update();
		}
		for(int i = 0; i < events.size(); ++i) {
			if(events.get(i).shouldRemove() == true) {
				events.remove(i--);
			}
		}
	}
	@Override
	public void clear() {
		events.clear();
	}
}
