package com.engine.aspect;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class AspectSystem implements IAspectSystem {
	protected Map<AspectType, Map<Integer, IAspect>> aspects = new HashMap<>();

	@Override
	public void addAspect(IAspect aspect) {
		Map<Integer, IAspect> aspectMap = getAspectMap(aspect.getType());
		aspectMap.put(aspect.getID(), aspect);
	}
	@Override
	public void removeAspects(int uniqueID) {
		aspects.forEach((type, map)-> {
			map.remove(uniqueID);
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IAspect> T getAspect(int uniqueID, AspectType aspectType) {
		Map<Integer, IAspect> aspectMap = aspects.get(aspectType);
		if(aspectMap != null) {
			return (T) aspectMap.get(uniqueID);
		}
		return null;
	}

	@Override
	public void forEachAspect(AspectType aspectType, BiConsumer<Integer, IAspect> command) {
		Map<Integer, IAspect> aspectMap = aspects.get(aspectType);
		if(aspectMap != null) {
			aspectMap.forEach(command);
		}
	}
	@Override
	public int getAspectSize(AspectType aspectType) {
		Map<Integer, IAspect> aspectMap = aspects.get(aspectType);
		if(aspectMap != null) {
			return aspectMap.size();
		}
		return 0;
	}

	@Override
	public void dispose() {
		aspects.clear();
	}

	protected Map<Integer, IAspect> getAspectMap(AspectType type) {
		Map<Integer, IAspect> result = aspects.get(type);
		if(result == null) {
			result = new HashMap<>();
			aspects.put(type, result);
		}
		return result;
	}
}
