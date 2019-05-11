package com.engine.util;

public interface ISAT {
	

//	void setCollisionResolve(SATCollisionResolve collisionResolve);
	int sat(
		float x1, float y1, float w1, float h1,
		float x2, float y2, float w2, float h2);
}
