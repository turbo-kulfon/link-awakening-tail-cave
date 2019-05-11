package com.la.three_of_a_kind_system;

import java.util.ArrayList;
import java.util.List;

import com.la.three_of_a_kind_system.ThreeOfAKindElement.ThreeOfAKindElementDependency;

public class ThreeOfAKindCore {
	public interface ThreeOfAKindDependency {
		void onSymbolMatch();
		void onSymbolMismatch();
	}

	private List<ThreeOfAKindElement> components = new ArrayList<>();
	private ThreeOfAKindDependency dependency;

	public ThreeOfAKindCore() {}

	public void setDependency(ThreeOfAKindDependency dependency) {
		this.dependency = dependency;
	}

	public ThreeOfAKindElement createComponent(ThreeOfAKindElementDependency dependency) {
		ThreeOfAKindElement component = new ThreeOfAKindElement(this, dependency);
		components.add(component);
		return component;
	}

	public void update() {
		if(shouldCheck() == true) {
			if(components.size() > 0) {
				CardSymbol cardSymbol = components.get(0).getCardSymbol();
				for(int i = 1; i < components.size(); ++i) {
					if(components.get(i).getCardSymbol() != cardSymbol) {
						restore();
						dependency.onSymbolMismatch();
						return;
					}
				}
				kilAll();
				dependency.onSymbolMatch();
			}
		}
	}

	private boolean shouldCheck() {
		for (ThreeOfAKindElement component : components) {
			if(component.isIdle() == false) {
				return false;
			}
		}
		return true;
	}
	private void restore() {
		for (ThreeOfAKindElement component : components) {
			component.restore();
		}
	}
	private void kilAll() {
		for (ThreeOfAKindElement component : components) {
			component.kill();
		}
	}
}
