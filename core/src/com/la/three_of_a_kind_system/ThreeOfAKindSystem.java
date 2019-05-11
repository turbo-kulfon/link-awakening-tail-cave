package com.la.three_of_a_kind_system;

import com.engine.sound.SoundSystem;
import com.la.three_of_a_kind_system.ThreeOfAKindCore.ThreeOfAKindDependency;
import com.la.three_of_a_kind_system.ThreeOfAKindElement.ThreeOfAKindElementDependency;

public class ThreeOfAKindSystem {
	private SoundSystem soundSystem;
	private ThreeOfAKindCore core;

	public ThreeOfAKindSystem(SoundSystem soundSystem) {
		this.soundSystem = soundSystem;
		core = new ThreeOfAKindCore();
	}

	public void setDependency(ThreeOfAKindDependency dependency) {
		core.setDependency(new ThreeOfAKindDependency() {
			@Override
			public void onSymbolMatch() {
				dependency.onSymbolMatch();
				soundSystem.secretSolved();
			}
			@Override
			public void onSymbolMismatch() {
				dependency.onSymbolMismatch();
				soundSystem.secretError();
			}
		});
	}
	public ThreeOfAKindComponent createComponent(ThreeOfAKindElementDependency dependency) {
		ThreeOfAKindElement element = core.createComponent(dependency);
		return new ThreeOfAKindComponent(element);
	}
}
