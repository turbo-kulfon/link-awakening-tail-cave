package com.la.game_objects.link;

import com.engine.component.carry.IBombComponent;
import com.engine.component.jump.IJumpComponent;
import com.engine.component.shield.IShieldComponent;
import com.engine.component.sword.ISwordComponent;
import com.la.equipment.IEquipmentSystem;

public class ActionButtonControl implements IActionButtonControl {
	private IEquipmentSystem equipmentSystem;

	private ISwordComponent swordComponent;
	private IShieldComponent shieldComponent;
	private IJumpComponent jumpComponent;
	private IBombComponent bombComponent;

	public ActionButtonControl(IEquipmentSystem equipmentSystem) {
		this.equipmentSystem = equipmentSystem;
	}

	public void setDependency(
			ISwordComponent swordComponent,
			IShieldComponent shieldComponent,
			IJumpComponent jumpComponent,
			IBombComponent bombComponent) {
		this.swordComponent = swordComponent;
		this.shieldComponent = shieldComponent;
		this.jumpComponent = jumpComponent;
		this.bombComponent = bombComponent;
	}

	@Override
	public void BButtonAction() {
		useItem(equipmentSystem.getBItemID());
	}
	@Override
	public void AButtonAction() {
		useItem(equipmentSystem.getAItemID());
	}

	private void useItem(int itemID) {
		if(itemID == 0) {
			if(swordComponent != null) {
				swordComponent.attack();
			}
		}
//		else if(itemID == 1) {
//			if(shieldComponent != null) {
//				shieldComponent.shieldUp();
//			}
//		}
		else if(itemID == 2) {
			if(jumpComponent != null) {
				jumpComponent.jump(2.2f);
			}
		}
		else if(itemID == 1) {
			if(bombComponent != null) {
				bombComponent.buttonPressed();
			}
		}
	}
}
