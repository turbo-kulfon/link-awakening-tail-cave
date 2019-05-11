package com.engine.double_buffer;

public interface IDoubleBuffer<T> {
	public interface DoubleBufferCommand<T> {
		void execute(T data);
	}

	void add(T data);
	void swap();

	void clearFrontBuffer();
	void clearBackBuffer();
	void clear();

	void runCommandOnFrontBuffer(DoubleBufferCommand<T> command);
	void runCommandOnBackBuffer(DoubleBufferCommand<T> command);
}
