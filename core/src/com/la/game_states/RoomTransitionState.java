package com.la.game_states;

import com.engine.aspect.AspectType;
import com.engine.aspect.IAspectSystem;
import com.engine.game_state.IGameState;
import com.engine.game_state.IGameStateSystem;
import com.la.aspects.RoomTransition;

public class RoomTransitionState implements IGameState {
	public interface CounterEndCallback {
		void onCountdownEnd();
	}

	private int counterLeft, speed = 4;
	private int direction;
	private boolean deepWalk;
	private IAspectSystem aspectSystem;
	private IGameStateSystem gameStateSystem;
	private CounterEndCallback callback;

	public RoomTransitionState(
			int direction, boolean deepWalk,
			IGameStateSystem gameStateSystem,
			IAspectSystem aspectSystem,
			CounterEndCallback callback) {
		this.direction = direction;
		this.deepWalk = deepWalk;
		this.gameStateSystem = gameStateSystem;
		this.aspectSystem = aspectSystem;
		this.callback = callback;
		if(direction == 0 || direction == 1) {
			counterLeft = 160;
		}
		else if(direction == 2 || direction == 3) {
			counterLeft = 128;
		}
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onRemove() {
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onResume() {
	}

	@Override
	public void pauseUpdate() {
	}

	@Override
	public void update() {
		if(counterLeft > 0) {
			counterLeft -= speed;
			if(direction == 0) {
				aspectSystem.forEachAspect(AspectType.ROOM_TRANSITION, (id, aspect)-> {
					RoomTransition roomTransition = (RoomTransition) aspect;
					if(roomTransition.isSpecialMove() == false) {
						roomTransition.move(speed, 0);
					}
					else {
						roomTransition.move(speed - deep()/40.0f, 0);
					}
				});
			}
			else if(direction == 1) {
				aspectSystem.forEachAspect(AspectType.ROOM_TRANSITION, (id, aspect)-> {
					RoomTransition roomTransition = (RoomTransition) aspect;
					if(roomTransition.isSpecialMove() == false) {
						roomTransition.move(-speed, 0);
					}
					else {
						roomTransition.move(-speed + deep()/40.0f, 0);
					}
				});
			}
			else if(direction == 2) {
				aspectSystem.forEachAspect(AspectType.ROOM_TRANSITION, (id, aspect)-> {
					RoomTransition roomTransition = (RoomTransition) aspect;
					if(roomTransition.isSpecialMove() == false) {
						roomTransition.move(0, speed);
					}
					else {
						roomTransition.move(0, speed - deep()/32.0f);
					}
				});
			}
			else if(direction == 3) {
				aspectSystem.forEachAspect(AspectType.ROOM_TRANSITION, (id, aspect)-> {
					RoomTransition roomTransition = (RoomTransition) aspect;
					if(roomTransition.isSpecialMove() == false) {
						roomTransition.move(0, -speed);
					}
					else {
						roomTransition.move(0, -speed + deep()/32.0f);
					}
				});
			}
			if(counterLeft <= 0) {
				gameStateSystem.popState();
				if(callback != null) {
					callback.onCountdownEnd();
				}
			}
		}
	}

	@Override
	public void pauseDraw() {
	}

	@Override
	public void draw() {
	}

	private float deep() {
		if(deepWalk == true) {
			return 26;
		}
		return 16;
	}
}
