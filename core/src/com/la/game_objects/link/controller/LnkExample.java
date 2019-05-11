package com.la.game_objects.link.controller;

import com.engine.IInputPort;
import com.engine.aspect.IAspectSystem;
import com.engine.component.shield.ShieldComponent;
import com.engine.component.sword.SwordComponent;
import com.engine.component.sword.SwordState;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.observer.Observatory;
import com.engine.sound.SoundSystem;
import com.engine.spatial.IOutsideViewCheck;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.OutsideViewCheck;
import com.engine.spatial.OutsideViewCheckPosition;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.RoomTransition;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.equipment.IEquipmentSystem;
import com.la.factory.IRoomFactory;
import com.la.game_objects.Shadow;
import com.la.game_objects.ShadowDependencyStandard;
import com.la.game_objects.link.ILinkData;
import com.la.game_objects.link.controller.common.CarryComponent;
import com.la.game_objects.link.controller.common.EnemyHitBase;
import com.la.game_objects.link.controller.common.HoleCollisionBase;
import com.la.game_objects.link.controller.common.LadderCollisionBase;
import com.la.game_objects.link.controller.platform.LinkStatePlatform;
import com.la.game_objects.link.controller.top_down.LinkStateTopDown;
import com.la.game_objects.link.draw.LinkDrawComponent;
import com.la.game_objects.link.draw.LinkDrawComponent.LinkDrawState;
import com.la.observer.LinkFallObserverData;

public class LnkExample implements IGameObject, ILinkData {
	private IInputPort inputPort;
	private LinkDrawComponent linkDrawComponent;
	private Shadow shadow;
	private ISpatialComponent spatialComponent;
	private SwordComponent swordComponent;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private LinkStateManager stateManager;
	private IOutsideViewCheck outsideViewCheck;

	private int uniqueID;
	private boolean remove;

	public LnkExample(
			float x, float y,
			IInputPort inputPort,
			GFXSystem gfxSystem,
			SoundSystem soundSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			IEquipmentSystem equipmentSystem,
			IRoomFactory roomFactory,
			Observatory<LinkFallObserverData> linkFallObserver) {
		this.inputPort = inputPort;
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;

		uniqueID = uniqueIDManager.getUniqueID();

		linkDrawComponent = new LinkDrawComponent(gfxSystem);
		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setPosition(x, y);
		shadow = new Shadow(gfxSystem, new ShadowDependencyStandard(spatialComponent));

		swordComponent = new SwordComponent(gfxSystem, spatialSystem, soundSystem, uniqueIDManager, aspectSystem, roomFactory);
		ShieldComponent shieldComponent = new ShieldComponent();
		CarryComponent carryComponent = new CarryComponent();

		WallBounce wallBounce = new WallBounce(uniqueID, spatialComponent);
		aspectSystem.addAspect(wallBounce);
		EnemyHitBase enemyHit = new EnemyHitBase(uniqueID);
		aspectSystem.addAspect(enemyHit);
		aspectSystem.addAspect(new RoomTransition(uniqueID, true, spatialComponent));
		HoleCollisionBase holeCollision = new HoleCollisionBase(uniqueID);
		aspectSystem.addAspect(holeCollision);
		LadderCollisionBase ladderCollision = new LadderCollisionBase(uniqueID);
		aspectSystem.addAspect(ladderCollision);

		outsideViewCheck = new OutsideViewCheck(new OutsideViewCheckPosition(spatialComponent));

		stateManager = new LinkStateManager();
		stateManager.addState(new LinkStateTopDown(
				uniqueID, soundSystem, spatialSystem, roomFactory, linkDrawComponent, shadow, spatialComponent,
				aspectSystem, swordComponent, shieldComponent, carryComponent, wallBounce,
				enemyHit, holeCollision, outsideViewCheck, equipmentSystem, linkFallObserver, this));
		stateManager.addState(new LinkStatePlatform(
				soundSystem, spatialSystem, linkDrawComponent, spatialComponent,
				swordComponent, shieldComponent, carryComponent, wallBounce, enemyHit,
				outsideViewCheck, ladderCollision, equipmentSystem, aspectSystem, roomFactory));
		stateManager.changeState(StateType.TOP_DOWN);

		setLastPosition();
	}

	@Override
	public void update() {
		if(inputPort.isLeftButtonPressed()) {
			stateManager.leftButtonPressed();
		}
		else if(inputPort.isRightButtonPressed()) {
			stateManager.rightButtonPressed();
		}
		if(inputPort.isUpButtonPressed()) {
			stateManager.upButtonPressed();
		}
		else if(inputPort.isDownButtonPressed()) {
			stateManager.downButtonPressed();
		}
		if(inputPort.isBButtonPressed() == true) {
			stateManager.BButtonPressed(inputPort.isBButtonJustPressed());
		}
		if(inputPort.isAButtonPressed() == true) {
			stateManager.AButtonPressed(inputPort.isAButtonJustPressed());
		}

		stateManager.update();

		outsideViewCheck.update();
	}
	@Override
	public void draw() {
		stateManager.draw();
	}

	@Override
	public void setToRemove() {
		remove = true;
	}
	@Override
	public boolean shouldRemove() {
		return remove;
	}
	@Override
	public void onRemove() {
		linkDrawComponent.remove();
		shadow.remove();
		spatialComponent.remove();
		aspectSystem.removeAspects(uniqueID);
		uniqueIDManager.returnID(uniqueID);
	}

	private float lastX, lastY;

	@Override
	public void setPosition(float x, float y) {
		spatialComponent.setPosition(x, y);
		swordComponent.stop();
	}
	@Override
	public void setLastPosition() {
		lastX = spatialComponent.getX();
		lastY = spatialComponent.getY();
	}
	@Override
	public void restoreLastPosition() {
		spatialComponent.setPosition(lastX, lastY);
	}
	@Override
	public float getX() {
		return spatialComponent.getX();
	}
	@Override
	public float getY() {
		return spatialComponent.getY();
	}
	@Override
	public float getW() {
		return spatialComponent.getW();
	}
	@Override
	public float getH() {
		return spatialComponent.getH();
	}
	@Override
	public float getCenterX() {
		return spatialComponent.getCenterX();
	}
	@Override
	public float getCenterY() {
		return spatialComponent.getCenterY();
	}
	@Override
	public float getSwordCenterX() {
		return swordComponent.getSwordCenterX();
	}
	@Override
	public float getSwordCenterY() {
		return swordComponent.getSwordCenterY();
	}
	@Override
	public SwordState getSwordState() {
		return swordComponent.getSwordState();
	}
	@Override
	public void changeDirectionToRight() {
		linkDrawComponent.turnRight();
	}
	@Override
	public void changeMoveState(int newStateID) {
		if(newStateID == 0) {
			stateManager.changeState(StateType.TOP_DOWN);
			linkDrawComponent.switchToTopDownMode();
		}
		else if(newStateID == 1) {
			stateManager.changeState(StateType.PLATFORM);
			linkDrawComponent.switchToPlatformMode();
		}
	}
	@Override
	public void setDrawLayer(int layer) {
		linkDrawComponent.setLayer(layer);
	}
	@Override
	public void setGetBigItem() {
		linkDrawComponent.setDrawState(LinkDrawState.BIG_ITEM_GET);
		swordComponent.stop();
	}
	@Override
	public void setGetPowerUpItemAnimation() {
		linkDrawComponent.setDrawState(LinkDrawState.POWER_UP_ITEM_GET);
		swordComponent.stop();
	}
	@Override
	public void setDying() {
		linkDrawComponent.setDrawState(LinkDrawState.IDLE);
	}
	@Override
	public void setDead() {
		linkDrawComponent.setDrawState(LinkDrawState.DEAD);
	}
}
