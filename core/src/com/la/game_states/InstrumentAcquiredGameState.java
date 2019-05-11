package com.la.game_states;

import com.engine.ISystemPort;
import com.engine.game_state.IGameState;
import com.engine.game_state.IGameStateSystem;
import com.engine.gfx.ColorDrawComponent;
import com.engine.gfx.GFXSystem;
import com.engine.sound.SoundSystem;
import com.engine.util.IRNG;
import com.la.factory.IGameStateFactory;
import com.la.game_objects.effect.Note;
import com.la.game_objects.effect.Petal;
import com.la.game_objects.link.ILinkData;
import com.la.game_objects.pickup.instrument.InstrumentDrawComponent;
import com.la.game_objects.pickup.instrument.InstrumentDrawComponent.InstrumentDrawComponentDependency;

public class InstrumentAcquiredGameState implements IGameState {
	private GFXSystem gfxSystem;
	private SoundSystem soundSystem;
	private IGameStateSystem gameStateSystem;
	private IGameStateFactory gameStateFactory;
	private IRNG rng;
	private ISystemPort systemPort;

	private InstrumentDrawComponent instrumentDrawComponent;
	private Note notes[] = new Note[3];
	private Petal petals[] = new Petal[8];
	private ColorDrawComponent background, foreground;
	private ILinkData linkData;

	private int mode = 0, counter;
	private boolean leftNote;

	public InstrumentAcquiredGameState(
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			IGameStateSystem gameStateSystem,
			IGameStateFactory gameStateFactory,
			IRNG rng,
			ISystemPort systemPort,
			ILinkData linkData
		) {
		this.gfxSystem = gfxSystem;
		this.soundSystem = soundSystem;
		this.gameStateSystem = gameStateSystem;
		this.gameStateFactory = gameStateFactory;
		this.systemPort = systemPort;
		this.rng = rng;
		this.linkData = linkData;

		counter = 180;

		background = gfxSystem.createColorDrawComponent(3);
		background.setVisible(false);
		background.setColor(1, 1, 1);
		background.setCoordinates(0, 0, 160, 144);

		foreground = gfxSystem.createColorDrawComponent(5);
		foreground.setVisible(false);
		foreground.setCoordinates(0, 0, 160, 144);
		foreground.setColor(1, 1, 1);

		instrumentDrawComponent = new InstrumentDrawComponent(gfxSystem, 234, 80, 16, 16, 4, new InstrumentDrawComponentDependency() {
			@Override
			public float getX() {
				return linkData.getCenterX() - 8;
			}
			@Override
			public float getY() {
				return linkData.getY() - 20;
			}
		});
		linkData.setDrawLayer(4);

		linkData.setGetBigItem();

		soundSystem.getInstrument();
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onRemove() {
		instrumentDrawComponent.remove();
		for (Note note : notes) {
			if(note != null) {
				note.remove();
			}
		}
		for (Petal petal : petals) {
			petal.remove();
		}
		background.remove();
		foreground.remove();
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
		if(mode == 0) {
			counter -= 1;
			if(counter <= 0) {
				gameStateFactory.createTextState(
					"You've got the|"
					+ "Full Moon Cello!", ()-> {
						counter = 360 + 360;
						mode = 1;
					});
			}
		}
		else if(mode == 1) {
			if(soundSystem.isGetInstrumentPlaying() == false) {
				counter -= 1;
				for(int i = 0; i < 3; ++i) {
					if(notes[i] != null) {
						if(notes[i].update() == true) {
							notes[i] = null;
						}
					}
				}
				if(counter == 720 - 20) {
					soundSystem.fullMoonCello();
				}
				if(counter <= 360 + 360 - 20 && counter > 360 && counter % 25 == 0) {
					createNote();
				}
				if(counter == 360) {
					soundSystem.warpOutDungeon();
					petals[0] = new Petal(linkData.getCenterX(), linkData.getY() - 12, 0, gfxSystem);
					petals[1] = new Petal(linkData.getCenterX(), linkData.getY() - 12, 2, gfxSystem);
					petals[2] = new Petal(linkData.getCenterX(), linkData.getY() - 12, 4, gfxSystem);
					petals[3] = new Petal(linkData.getCenterX(), linkData.getY() - 12, 6, gfxSystem);
				}                                                                  
				if(counter == 360 - 11) {                                                
					petals[4] = new Petal(linkData.getCenterX(), linkData.getY() - 12, 1, gfxSystem);
					petals[5] = new Petal(linkData.getCenterX(), linkData.getY() - 12, 3, gfxSystem);
					petals[6] = new Petal(linkData.getCenterX(), linkData.getY() - 12, 5, gfxSystem);
					petals[7] = new Petal(linkData.getCenterX(), linkData.getY() - 12, 7, gfxSystem);
				}
				if(counter < 180 && counter > 120) {
					background.setVisible(true);
					background.setAlpha(1.0f - ((counter - 120)/60.0f));
				}
				if(counter < 120 && counter > 0) {
					foreground.setVisible(true);
					foreground.setAlpha(1.0f - ((counter)/120.0f));
				}
				if(counter == -60) {
					gameStateFactory.createTextState(
						"Thank you for|"
						+ "playing!", ()-> {
							systemPort.exit();
						});
				}
				for(int i = 0; i < 8; ++i) {
					if(petals[i] != null) {
						petals[i].update();
					}
				}
			}
		}
	}

	@Override
	public void pauseDraw() {
		instrumentDrawComponent.update();
		instrumentDrawComponent.draw();
	}
	@Override
	public void draw() {
		instrumentDrawComponent.update();
		instrumentDrawComponent.draw();
		for(int i = 0; i < 3; ++i) {
			if(notes[i] != null) {
				notes[i].draw();
			}
		}
		for(int i = 0; i < 8; ++i) {
			if(petals[i] != null) {
				petals[i].draw();
			}
		}
	}

	private void createNote() {
		for(int i = 0; i < 3; ++i) {
			if(notes[i] == null) {
				createNote(i);
				leftNote = !leftNote;
				break;
			}
		}
	}
	private void createNote(int index) {
		int dir = rng.getRNG(0, 1);
		int offset = leftNote == true ? -4 : 4;
		Note note = new Note(gfxSystem, dir == 0 ? true : false, linkData.getCenterX() + offset, linkData.getY() - 24, leftNote);
		notes[index] = note;
	}
}
