package com.engine.gfx.core;

import com.engine.gfx.IDrawPort;

public class NumberDrawElement extends DrawElement implements INumberDrawElement {
	private IDrawPort drawPort;
	private String number;

	public NumberDrawElement(IDrawPort drawPort) {
		this.drawPort = drawPort;
		number = "";
	}

	@Override
	public void setNumber(int number, int digits) {
		this.number = "";
		if(digits == 3) {
			if(number < 100) {
				this.number += "0";
			}
			if(number < 10) {
				this.number += "0";
			}
			this.number += Integer.toString(number);
		}
		else if(digits == 2) {
			if(number < 10) {
				this.number += "0";
			}
			this.number += Integer.toString(number);
		}
		else if(digits == 1) {
			this.number += Integer.toString(number);
		}
	}
	@Override
	protected void drawImplementation() {
		drawPort.drawNumber(x, y, number);
	}
}
