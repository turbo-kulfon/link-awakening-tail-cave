package com.engine.spatial;

public interface IOutsideViewCheck {
	int maxX = 160;
	int maxY = 128;

	public interface OutsideViewCheckCallback {
		void outsideLeft();
		void outsideRight();
		void outsideUp();
		void outsideDown();
	}

	void update();
	void setCallback(OutsideViewCheckCallback callback);
}
