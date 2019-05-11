package com.la.three_of_a_kind_system;

public class ThreeOfAKindElement {
	public interface ThreeOfAKindElementDependency {
		void move();
		void stopMove();
		void onSymbolChange(CardSymbol symbol);
		void onRestore();
		void kill();
	}

	private enum State {
		NONE,
		HIT,
		IDLE
	}

	private CardSymbol cardSymbol = CardSymbol.HEART;
	private State state = State.NONE;
	private int counter = 20;
	private ThreeOfAKindCore core;
	private ThreeOfAKindElementDependency dependency;

	public ThreeOfAKindElement(
			ThreeOfAKindCore core,
			ThreeOfAKindElementDependency dependency) {
		this.core = core;
		this.dependency = dependency;
	}

	public void hit() {
		if(state == State.NONE) {
			state = State.HIT;
			counter = 45;
		}
	}
	public void update() {
		if(state == State.NONE) {
			counter -= 1;
			if(counter <= 0) {
				nextSymbol();
				dependency.onSymbolChange(cardSymbol);
				counter = 20;
			}
			dependency.move();
		}
		else if(state == State.HIT) {
			counter -= 1;
			if(counter <= 0) {
				state = State.IDLE;
				core.update();
			}
			dependency.stopMove();
		}
		else if(state == State.IDLE) {
			dependency.stopMove();
		}
	}
	public void restore() {
		state = State.NONE;
		counter = 20;
		dependency.onRestore();
	}
	public void kill() {
		dependency.kill();
	}
	public CardSymbol getCardSymbol() {
		return cardSymbol;
	}
	public boolean isIdle() {
		return state == State.IDLE;
	}

	private void nextSymbol() {
		if(cardSymbol == CardSymbol.HEART) {
			cardSymbol = CardSymbol.DIAMOND;
		}
		else if(cardSymbol == CardSymbol.DIAMOND) {
			cardSymbol = CardSymbol.SPADE;
		}
		else if(cardSymbol == CardSymbol.SPADE) {
			cardSymbol = CardSymbol.CLUB;
		}
		else if(cardSymbol == CardSymbol.CLUB) {
			cardSymbol = CardSymbol.HEART;
		}
	}
}
