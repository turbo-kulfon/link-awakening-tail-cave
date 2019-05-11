package com.la.game_states;

import java.util.ArrayList;
import java.util.List;

import com.engine.IInputPort;
import com.engine.game_state.IGameState;
import com.engine.game_state.IGameStateSystem;
import com.engine.gfx.ColorDrawComponent;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextDrawComponent;
import com.engine.gfx.TextureDrawComponent;
import com.engine.sound.SoundSystem;
import com.la.game_objects.link.ILinkData;

public class TextDisplayState implements IGameState {
	public interface Callback {
		void onEnd();
	}

	enum State {
		LETTER_DISPLAY,
		SCROLL,
		NONE,
	}

	private SoundSystem soundSystem;
	private TextureDrawComponent arrow;
	private ColorDrawComponent colorDrawComponent;
	private TextDrawComponent textDrawComponent;
	private List<String> linesOfText;
	private IInputPort inputPort;
	private ILinkData linkData;
	private boolean blockInput = true;
	private int counter, currentIndex, currentLetter, yOffset;
	private boolean autoSkip;
	private State state = State.LETTER_DISPLAY;

	private IGameStateSystem gameStateSystem;
	private Callback callback;

	public TextDisplayState(
			IGameStateSystem gameStateSystem,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			IInputPort inputPort,
			ILinkData linkData,
			String text,
			Callback callback) {
		this.soundSystem = soundSystem;
		this.gameStateSystem = gameStateSystem;
		this.inputPort = inputPort;
		this.linkData = linkData;
		this.callback = callback;
		linesOfText = new ArrayList<>();
		int currentIndex = 0;
		for(int i = 0; i < text.length(); ++i) {
			if(i+1 >= text.length()) {
				linesOfText.add(text.substring(currentIndex, i+1));
				break;
			}
			else if(text.charAt(i) == '|') {
				linesOfText.add(text.substring(currentIndex, i+1));
				currentIndex = i + 1;
			}
		}

		colorDrawComponent = gfxSystem.createColorDrawComponent(5);
		textDrawComponent = gfxSystem.createTextDrawComponent(5);
		arrow = gfxSystem.createTextureDrawComponent(5);
		arrow.setTexture(118, 69, 8, 7);
		arrow.setSize(8, 7);
		arrow.setVisible(false);
	}

	@Override
	public void onCreate() {
		if(linkData.getCenterY() > 64) {
			yOffset = 0;
		}
		else {
			yOffset = 64;
		}
		colorDrawComponent.setColor(0, 0, 0);
		colorDrawComponent.setCoordinates(8, 8 + yOffset, 144, 40);
		textDrawComponent.setPositionX(16);
		textDrawComponent.setPositionY(16 + yOffset);
		arrow.setPosition(8 + 135, 39 + yOffset);
	}

	@Override
	public void onRemove() {
		colorDrawComponent.remove();
		textDrawComponent.remove();
		if(callback != null) {
			callback.onEnd();
		}
		inputPort.blockAButton();
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
		boolean btn = false;
		if(inputPort.isAButtonPressed() == true) {
			btn = true;
		}
		else {
			blockInput = false;
		}
		if(state == State.LETTER_DISPLAY) {
			counter -= 1;
			if(btn == true) {
				counter -= 3;
			}
			if(counter < 0) {
				counter = 2;
				currentLetter += 1;
				String currText = getCurrentText();
				if(currentLetter + 1 > currText.length()) {
					currentLetter = currText.length();
					state = State.NONE;
					if(autoSkip == false && currentIndex + 2 < linesOfText.size()) {
						soundSystem.sentenceEnd();
					}
				}
				else {
					if(currText.charAt(currentLetter) != ' ') {
						soundSystem.letterDisplay();
					}
				}
				textDrawComponent.setText(currText.substring(0, currentLetter));
			}
		}
		else if(state == State.SCROLL) {
			String txt = getScrollText();
			textDrawComponent.setText(txt);
			counter -= 1;
			textDrawComponent.setPositionY(16 + 8 + yOffset);
			if(counter <= 0) {
				textDrawComponent.setPositionY(16 + yOffset);
				state = State.LETTER_DISPLAY;
				currentIndex += 1;
				currentLetter = txt.length();
				autoSkip = !autoSkip;
			}
		}
		else if(state == State.NONE) {
			counter += 1;
			if(counter > 16) {
				counter = 0;
			}
			if(autoSkip == true && currentIndex + 2 < linesOfText.size()) {
				state = State.SCROLL;
				counter = 10;
			}
			if(btn == true) {
				if(blockInput == false) {
					if(currentIndex + 2 >= linesOfText.size()) {
						gameStateSystem.popState();
					}
					else {
						state = State.SCROLL;
						counter = 10;
					}
				}
			}
		}
		if(btn == true) {
			blockInput = true;
		}
	}

	@Override
	public void pauseDraw() {
	}

	@Override
	public void draw() {
		if(state == State.NONE && currentIndex + 2 < linesOfText.size() && counter < 8 && autoSkip == false) {
			arrow.setVisible(true);
		}
		else {
			arrow.setVisible(false);
		}
	}

	private String getCurrentText() {
		String result = linesOfText.get(currentIndex);
		if(currentIndex + 1 < linesOfText.size()) {
			result += "\n" + linesOfText.get(currentIndex+1);
		}
		return result;
	}
	private String getScrollText() {
		return linesOfText.get(currentIndex+1);
	}
}
