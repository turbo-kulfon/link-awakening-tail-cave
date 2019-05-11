package com.la.game_objects.link;

import com.engine.IInputPort;
import com.engine.aspect.IAspectSystem;
import com.engine.component.carry.IBombComponent;
import com.engine.component.carry.ICarryComponent;
import com.engine.component.shield.ShieldComponent;
import com.engine.component.sword.SwordComponent;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.observer.Observatory;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.enemy_hit.EnemyHitStandard;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.equipment.IEquipmentSystem;
import com.la.factory.IRoomFactory;
import com.la.game_objects.Shadow;
import com.la.observer.LinkFallObserverData;

public class LinkStateManager implements ILinkStateManager {
	private ILinkState states[];
	private int currentMode = -1;

	public LinkStateManager(
			int uniqueID,
			IInputPort inputPort,
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IAspectSystem aspectSystem,
			TextureDrawComponent drawComponent,
			ISpatialComponent spatialComponent,
			Shadow shadow,
			SwordComponent swordComponent,
			ShieldComponent shieldComponent,
			ILinkData linkData,
			EnemyHitStandard enemyHit,
			WallBounce wallBounce,
			ActionButtonControl actionButtonControl,
			Observatory<LinkFallObserverData> linkFallObserver,
			IRoomFactory roomFactory,
			IEquipmentSystem equipmentSystem) {
		states = new ILinkState[2];
		states[0] = new TopDownLinkState(uniqueID, inputPort, gfxSystem, spatialSystem, aspectSystem, drawComponent, spatialComponent, shadow, swordComponent, shieldComponent, linkData, enemyHit, wallBounce, equipmentSystem, actionButtonControl, roomFactory, linkFallObserver);
		states[1] = new PlatformLinkState(uniqueID, inputPort, aspectSystem, drawComponent, spatialComponent, swordComponent, wallBounce, enemyHit, actionButtonControl);
	}

	@Override
	public void change(int newModeID) {
		if(currentMode != newModeID) {
			if(currentMode != -1) {
				states[currentMode].pause();
			}
			currentMode = newModeID;
			states[currentMode].initialize();
		}
	}

	@Override
	public void update() {
		states[currentMode].update();
	}
	@Override
	public void draw() {
		states[currentMode].draw();
	}
}
