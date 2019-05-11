package com.engine.component.jump;

public interface IJumpComponent {
	boolean jump(float deltaStart);
	void setHeight(float height);
	void stop();
	void update();
}
