package com.la.game_objects.link.controller;

public class ActionButtonController {
	public interface ActionButtonDependency {
		void swordSwing();
		void shieldUp();
		void jump();
		void plantBomb();
		void sprinkMagicPowder();

		int getBButtonItemID();
		int getAButtonItemID();

		boolean BButtonCarryItemTossed();
		boolean AButtonCarryItemTossed();

		boolean interactionCheck();
	}

	private ActionButtonDependency dependency;

	public ActionButtonController(ActionButtonDependency dependency) {
		this.dependency = dependency;
	}

	public void BButtonPressed(boolean justPressed) {
		boolean itemTossed = true;
		if(justPressed == true) {
			itemTossed = dependency.BButtonCarryItemTossed();
		}
		if(itemTossed == true) {
			action(dependency.getBButtonItemID(), justPressed);
		}
	}
	public void AButtonPressed(boolean justPressed) {
		boolean itemTossed = true;
		if(justPressed == true) {
			itemTossed = dependency.AButtonCarryItemTossed();
		}
		if(itemTossed == true) {
			if(dependency.interactionCheck() == false) {
				action(dependency.getAButtonItemID(), justPressed);
			}
		}
	}

	private void action(int itemID, boolean justPressed) {
		if(itemID == 0) {
			dependency.swordSwing();
		}
		else if(itemID == 1) {
			dependency.shieldUp();
		}
		else if(itemID == 2) {
			if(justPressed == true) {
				dependency.jump();
			}
		}
		else if(itemID == 3) {
			if(justPressed == true) {
				dependency.plantBomb();
			}
		}
		else if(itemID == 4) {
			if(justPressed == true) {
				dependency.sprinkMagicPowder();
			}
		}
	}
}
