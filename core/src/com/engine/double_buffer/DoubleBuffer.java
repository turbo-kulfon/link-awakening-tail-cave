package com.engine.double_buffer;

import java.util.ArrayList;
import java.util.List;

public class DoubleBuffer<T> implements IDoubleBuffer<T> {
	private List<T> buffer1 = new ArrayList<>();
	private List<T> buffer2 = new ArrayList<>();
	private List<T> front, back;

	public DoubleBuffer() {
		front = buffer1;
		back = buffer2;
	}

	@Override
	public void add(T data) {
		front.add(data);
	}

	@Override
	public void swap() {
		List<T> tmp = front;
		front = back;
		back = tmp;
	}

	@Override
	public void clearFrontBuffer() {
		front.clear();
	}
	@Override
	public void clearBackBuffer() {
		back.clear();
	}
	@Override
	public void clear() {
		clearFrontBuffer();
		clearBackBuffer();
	}

	@Override
	public void runCommandOnFrontBuffer(DoubleBufferCommand<T> command) {
		for (T data : front) {
			command.execute(data);
		}
	}
	@Override
	public void runCommandOnBackBuffer(DoubleBufferCommand<T> command) {
		for (T data : back) {
			command.execute(data);
		}
	}
}
