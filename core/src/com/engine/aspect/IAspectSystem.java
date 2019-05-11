package com.engine.aspect;

import java.util.function.BiConsumer;

public interface IAspectSystem {
	void addAspect(IAspect aspect);
	void removeAspects(int uniqueID);

	<T extends IAspect> T getAspect(int uniqueID, AspectType aspectType);

	void forEachAspect(AspectType aspectType, BiConsumer<Integer, IAspect> command);
	int getAspectSize(AspectType aspectType);

	void dispose();
}
