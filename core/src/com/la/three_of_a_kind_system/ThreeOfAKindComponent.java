package com.la.three_of_a_kind_system;

public class ThreeOfAKindComponent {
	private ThreeOfAKindElement element;

	public ThreeOfAKindComponent(ThreeOfAKindElement element) {
		this.element = element;
	}

	public void hit() {
		element.hit();
	}
	public void update() {
		element.update();
	}
	public CardSymbol getCardSymbol() {
		return element.getCardSymbol();
	}
}
