package com.la.aspects.hidden;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;

public class DetectHidden {
	private IAspectSystem aspectSystem;
	private boolean result;

	public DetectHidden(IAspectSystem aspectSystem) {
		this.aspectSystem = aspectSystem;
	}

	public boolean isEnemyUnhidden() {
		result = false;
		aspectSystem.forEachAspect(AspectType.ENEMY_TAG, (id, aspect)-> {
			Hidden hidden = aspectSystem.getAspect(id, AspectType.HIDDEN);
			if(hidden != null) {
				if(hidden.isHidden() == false) {
					result = true;
				}
			}
			else {
				result = true;
			}
		});
		return result;
	}
}
