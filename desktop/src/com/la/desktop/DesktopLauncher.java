package com.la.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.la.LAEntryPoint;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 160 * 4;
		config.height = 144 * 4;
//		config.foregroundFPS = 10;
		new LwjglApplication(new LAEntryPoint(), config);
	}
}
