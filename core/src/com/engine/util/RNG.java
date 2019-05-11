package com.engine.util;

import java.util.Random;

public class RNG implements IRNG {
	private Random random = new Random();

	@Override
	public int getRNG(int min, int max) {
		return random.nextInt((max - min) + 1) + min;
	}
}
