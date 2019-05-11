package com.la;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.engine.IInputPort;

public class LibGDXInputPort implements IInputPort {
	private boolean BBlock, ABlock;

	@Override
	public boolean isLeftButtonPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.LEFT);
	}
	@Override
	public boolean isLeftButtonJustPressed() {
		return Gdx.input.isKeyJustPressed(Input.Keys.LEFT);
	}
	@Override
	public boolean isRightButtonPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.RIGHT);
	}
	@Override
	public boolean isRightButtonJustPressed() {
		return Gdx.input.isKeyJustPressed(Input.Keys.RIGHT);
	}
	@Override
	public boolean isUpButtonPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.UP);
	}
	@Override
	public boolean isUpButtonJustPressed() {
		return Gdx.input.isKeyJustPressed(Input.Keys.UP);
	}
	@Override
	public boolean isDownButtonPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.DOWN);
	}
	@Override
	public boolean isDownButtonJustPressed() {
		return Gdx.input.isKeyJustPressed(Input.Keys.DOWN);
	}

	@Override
	public boolean isBButtonPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.Z) && BBlock == false;
	}
	@Override
	public boolean isBButtonJustPressed() {
		return Gdx.input.isKeyJustPressed(Input.Keys.Z) && BBlock == false;
	}
	@Override
	public boolean isAButtonPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.X) && ABlock == false;
	}
	@Override
	public boolean isAButtonJustPressed() {
		return Gdx.input.isKeyJustPressed(Input.Keys.X) && ABlock == false;
	}

	@Override
	public boolean isSelectButtonPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.Q);
	}
	@Override
	public boolean isSelectButtonJustPressed() {
		return Gdx.input.isKeyJustPressed(Input.Keys.Q);
	}
	@Override
	public boolean isStartButtonPressed() {
		return Gdx.input.isKeyPressed(Input.Keys.W);
	}
	@Override
	public boolean isStartButtonJustPressed() {
		return Gdx.input.isKeyJustPressed(Input.Keys.W);
	}

	@Override
	public void blockBBUtton() {
		BBlock = true;
	}
	@Override
	public void blockAButton() {
		ABlock = true;
	}

	public void update() {
		if(Gdx.input.isKeyPressed(Input.Keys.Z) == false) {
			BBlock = false;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.X) == false) {
			ABlock = false;
		}
	}
}
