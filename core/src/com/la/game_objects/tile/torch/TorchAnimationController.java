package com.la.game_objects.tile.torch;

public class TorchAnimationController {
	public interface TorchAnimationCallback {
		void onFrame0();
		void onFrame1();
		void onFrame2();
		void onFrame3();
	}

	private int animationCounter;
	private boolean forward;
	private TorchAnimationCallback callback;

	public TorchAnimationController(TorchAnimationCallback callback) {
		this.callback = callback;
	}

	public void update() {
		if(forward == false) {
			animationCounter -= 1;
			if(animationCounter <= 0) {
				animationCounter = 0;
				forward = true;
			}
		}
		else {
			animationCounter += 1;
			if(animationCounter >= 48) {
				animationCounter = (int) (12*3);
				forward = false;
			}
		}
		if(animationCounter >= 0 && animationCounter < 12*1) {
			callback.onFrame0();
		}
		else if(animationCounter >= 12*1 && animationCounter < 12*2) {
			callback.onFrame1();
		}
		else if(animationCounter >= 12*2 && animationCounter < 12*3) {
			callback.onFrame2();
		}
		else if(animationCounter >= 12*3 && animationCounter < 48) {
			callback.onFrame3();
		}
	}
}
