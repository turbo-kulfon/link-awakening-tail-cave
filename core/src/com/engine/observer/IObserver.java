package com.engine.observer;

public interface IObserver<T> {
	void update(T data);

	boolean shouldRemove();
}
