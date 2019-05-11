package com.la.game_states;

import com.engine.IInputPort;
import com.engine.game_state.IGameState;
import com.engine.game_state.IGameStateSystem;
import com.engine.gfx.GFXSystem;
import com.engine.sound.SoundSystem;
import com.la.equipment.EquipmentPanel;
import com.la.equipment.IEquipmentSystem;
import com.la.equipment.StatusPanel;
import com.la.map_display.IMapDisplaySystem;

public class EquipmentMenuState implements IGameState {
	private IGameStateSystem gameStateSystem;
	private SoundSystem soundSystem;
	private StatusPanel statusPanel;
	private IInputPort inputPort;

	private int mode;
	private int yOffset;
	private float volume = 100;
	private EquipmentPanel equipmentPanel;
	private IMapDisplaySystem mapDisplaySystem;

	public EquipmentMenuState(
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			IGameStateSystem gameStateSystem,
			StatusPanel statusPanel,
			IEquipmentSystem equipmentSystem,
			IInputPort inputPort,
			IMapDisplaySystem mapDisplaySystem) {
		this.soundSystem = soundSystem;
		this.gameStateSystem = gameStateSystem;
		this.statusPanel = statusPanel;
		this.inputPort = inputPort;
		this.mapDisplaySystem = mapDisplaySystem;

		mode = 0;
		yOffset = statusPanel.getOffsetY();

		equipmentPanel = new EquipmentPanel(inputPort, soundSystem, gfxSystem, equipmentSystem);
		mapDisplaySystem.initializeMapDisplay(0);
	}

	@Override
	public void onCreate() {
	}

	@Override
	public void onRemove() {
		equipmentPanel.remove();
		mapDisplaySystem.cleanMapDisplay();
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
			yOffset -= 4;
			volume -= 1.75f;
			soundSystem.setMusicVolume((int) volume);
			if(yOffset <= 0) {
				yOffset = 0;
				mode = 2;
			}
			statusPanel.setOffsetY(yOffset);
			equipmentPanel.setOffsetY(yOffset);
		}
		else if(mode == 1) {
			yOffset += 4;
			volume += 1.75f;
			soundSystem.setMusicVolume((int) volume);
			if(yOffset >= 128) {
				yOffset = 128;
				gameStateSystem.popState();
				soundSystem.setMusicVolume(100);
			}
			statusPanel.setOffsetY(yOffset);
			equipmentPanel.setOffsetY(yOffset);
		}
		else if(mode == 2) {
			equipmentPanel.update();
			if(inputPort.isSelectButtonPressed() == true) {
				mode = 1;
				soundSystem.menuClose();
			}
		}
	}

	@Override
	public void pauseDraw() {
		equipmentPanel.draw();
		mapDisplaySystem.drawMap(88, yOffset + 64);
	}
	@Override
	public void draw() {
		equipmentPanel.draw();
		mapDisplaySystem.drawMap(88, yOffset + 64);
	}
}
