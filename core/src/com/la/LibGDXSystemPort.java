package com.la;

import com.badlogic.gdx.Gdx;
import com.engine.ISystemPort;

public class LibGDXSystemPort implements ISystemPort {
	@Override
	public void exit() {
		Gdx.app.exit();
	}
}
