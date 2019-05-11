package com.la.game_objects.link;

import com.engine.IInputPort;
import com.engine.aspect.IAspectSystem;
import com.engine.component.carry.CarryComponent;
import com.engine.component.carry.ICarryComponent;
import com.engine.component.shield.ShieldComponent;
import com.engine.component.sword.SwordComponent;
import com.engine.component.sword.SwordState;
import com.engine.game_object.IGameObject;
import com.engine.gfx.GFXSystem;
import com.engine.gfx.TextureDrawComponent;
import com.engine.id_manager.IUniqueIDManager;
import com.engine.observer.Observatory;
import com.engine.spatial.ISpatialComponent;
import com.engine.spatial.SpatialSystem;
import com.la.aspects.RoomTransition;
import com.la.aspects.enemy_hit.EnemyHitStandard;
import com.la.aspects.wall_bounce.WallBounce;
import com.la.equipment.IEquipmentSystem;
import com.la.factory.IRoomFactory;
import com.la.game_objects.Shadow;
import com.la.game_objects.ShadowDependencyStandard;
import com.la.observer.LinkFallObserverData;

public class Link implements IGameObject, ILinkData {
	private TextureDrawComponent drawComponent;
	private ISpatialComponent spatialComponent;

	private Shadow shadow;
	private SwordComponent swordComponent;
	private ShieldComponent shieldComponent;

	private IUniqueIDManager uniqueIDManager;
	private IAspectSystem aspectSystem;

	private ILinkStateManager linkStateManager;

	private int uniqueID;
	private boolean remove;

	public Link(int x, int y,
			IInputPort inputPort,
			GFXSystem gfxSystem,
			SpatialSystem spatialSystem,
			IUniqueIDManager uniqueIDManager,
			IAspectSystem aspectSystem,
			IEquipmentSystem equipmentSystem,
			ActionButtonControl actionButtonControl,
			Observatory<LinkFallObserverData> linkFallObserver,
			IRoomFactory roomFactory) {
		this.uniqueIDManager = uniqueIDManager;
		this.aspectSystem = aspectSystem;

		uniqueID = uniqueIDManager.getUniqueID();

		drawComponent = gfxSystem.createTextureDrawComponent(1);
		drawComponent.setTexture(0, 0, 14, 16);
		drawComponent.setSize(14, 16);
		drawComponent.setSpriteOffset(-2, -9);

		spatialComponent = spatialSystem.createDynamicComponent(uniqueID);
		spatialComponent.setCoordinates(x, y, 10, 7);

		shadow = new Shadow(gfxSystem, new ShadowDependencyStandard(spatialComponent));
		swordComponent = new SwordComponent(gfxSystem, spatialSystem, null, uniqueIDManager, aspectSystem, roomFactory);
		shieldComponent = new ShieldComponent();
		
		WallBounce wallBounce = new WallBounce(uniqueID, spatialComponent);

		aspectSystem.addAspect(wallBounce);
		aspectSystem.addAspect(new RoomTransition(uniqueID, true, spatialComponent));
		EnemyHitStandard enemyHit = new EnemyHitStandard(uniqueID, spatialComponent, equipmentSystem, shieldComponent);
		aspectSystem.addAspect(enemyHit);

		linkStateManager = new LinkStateManager(uniqueID, inputPort, gfxSystem, spatialSystem, aspectSystem, drawComponent, spatialComponent, shadow, swordComponent, shieldComponent, this, enemyHit, wallBounce, actionButtonControl, linkFallObserver, roomFactory, equipmentSystem);
		linkStateManager.change(0);
	}

	@Override
	public void update() {
		linkStateManager.update();
	}
	@Override
	public void draw() {
		linkStateManager.draw();
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
		drawComponent.remove();
		spatialComponent.remove();
		aspectSystem.removeAspects(uniqueID);
		shadow.remove();
		swordComponent.remove();
		uniqueIDManager.returnID(uniqueID);
	}

	private float lastX, lastY;

	@Override
	public void setPosition(float x, float y) {
		spatialComponent.setPosition(x, y);
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
		return 0;
	}
	@Override
	public float getSwordCenterY() {
		return 0;
	}
	@Override
	public SwordState getSwordState() {
		return null;
	}
	@Override
	public void changeDirectionToRight() {
	}
	@Override
	public void changeMoveState(int newStateID) {
		linkStateManager.change(newStateID);
	}
	@Override
	public void setDrawLayer(int layer) {
	}
	@Override
	public void setGetBigItem() {
	}
	@Override
	public void setGetPowerUpItemAnimation() {
	}
	@Override
	public void setDying() {
	}
	@Override
	public void setDead() {
	}
}
