package com.engine.observer;

import java.util.LinkedList;
import java.util.List;

public class Observatory<T> {
	private List<IObserver<T>> observers = new LinkedList<>();
	private List<IObserver<T>> toRemove = new LinkedList<>();

	public void addObserver(IObserver<T> observer) {
		if(observers.contains(observer) == false) {
			observers.add(observer);
		}
	}
	public void update(T data) {
		for (IObserver<T> observer : observers) {
			if(observer.shouldRemove() == false) {
				observer.update(data);
			}
			else {
				toRemove.add(observer);
			}
		}

		if(toRemove.size() > 0) {
			for (IObserver<T> remove : toRemove) {
				observers.remove(remove);
			}
			toRemove.clear();
		}
	}
	public void clear() {
		observers.clear();
		toRemove.clear();
	}
}
