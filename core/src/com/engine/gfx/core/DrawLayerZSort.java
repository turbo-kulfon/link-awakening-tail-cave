package com.engine.gfx.core;

import java.util.Comparator;

public class DrawLayerZSort extends DrawLayerStandard {
	protected Comparator<IDrawElement> zCompare = new Comparator<IDrawElement>() {
		@Override
		public int compare(IDrawElement o1, IDrawElement o2) {
			if(o1.getZ() < o2.getZ()) {
				return -1;
			}
			else if(o1.getZ() > o2.getZ()) {
				return 1;
			}
			return 0;
		}
	};

	@Override
	public void draw() {
		elements.sort(zCompare);
		super.draw();
	}
}
