package com.la.game_objects.link.controller.common;

import com.engine.direction.Direction;
import com.la.equipment.IEquipmentSystem;
import com.la.factory.IRoomFactory;
import com.la.game_objects.MagicPowderSprinkle.MagicPowderDependency;

public class MagicPowderComponent {
	public interface MagicPowderComponentDependency {
		float getCenterX();
		float getCenterY();
		float getY();
		float getH();

		Direction getDirection();

		void onSprinkle();
	}

	private IEquipmentSystem equipmentSystem;
	private IRoomFactory roomFactory;
	private MagicPowderDependency magicPowderDependency;
	private MagicPowderComponentDependency dependency;

	public MagicPowderComponent(
			IEquipmentSystem equipmentSystem,
			IRoomFactory roomFactory,
			MagicPowderComponentDependency dependency) {
		this.equipmentSystem = equipmentSystem;
		this.roomFactory = roomFactory;
		this.dependency = dependency;

		magicPowderDependency = new MagicPowderDependency() {
			@Override
			public float getOwnerCenterX() {
				return dependency.getCenterX();
			}
			@Override
			public float getOwnerCenterY() {
				return dependency.getCenterY();
			}
		};
	}

	public void sprinkle() {
		if(equipmentSystem.removeMagicPowder() == true) {
			if(dependency.getDirection() == Direction.LEFT) {
				roomFactory.createMagicPowderSprinkle(dependency.getCenterX() - 15, dependency.getY() + dependency.getH() - 9, magicPowderDependency);
			}
			else if(dependency.getDirection() == Direction.RIGHT) {
				roomFactory.createMagicPowderSprinkle(dependency.getCenterX() + 15, dependency.getY() + dependency.getH() - 9, magicPowderDependency);
			}
			else if(dependency.getDirection() == Direction.UP) {
				roomFactory.createMagicPowderSprinkle(dependency.getCenterX(), dependency.getY() - 14, magicPowderDependency);
			}
			else if(dependency.getDirection() == Direction.DOWN) {
				roomFactory.createMagicPowderSprinkle(dependency.getCenterX(), dependency.getY() + dependency.getH() + 3, magicPowderDependency);
			}
			dependency.onSprinkle();
		}
	}
}
