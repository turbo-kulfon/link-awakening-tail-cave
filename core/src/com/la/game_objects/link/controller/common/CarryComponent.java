package com.la.game_objects.link.controller.common;

import com.engine.sound.SoundSystem;
import com.la.aspects.CarriedItem.ItemType;

public class CarryComponent {
	public enum CarryState {
		IDLE,
		TAKE,
		CARRY,
		THROW
	}

	public interface CarryComponentDependency {
		boolean setCarriedItemPosition(int uniqueID);
		boolean setTakingItemPosition(int uniqueID, int frame);
		boolean isItemIDActive(int id);
		ItemType getCarriedItemType(int uniqueID);

		boolean take(int uniqueID);
		void toss(int uniqueID);
		void tossDown(int uniqueID);
		void removeItem(int uniqueID);

		int getBButtonItemID();
		int getAButtonItemID();
	}

	private SoundSystem soundSystem;
	private int carriedItemID = -1, counter;
	private CarryState state = CarryState.IDLE;
	private CarryComponentDependency dependency;

	public void setDependency(SoundSystem soundSystem, CarryComponentDependency dependency) {
		this.soundSystem = soundSystem;
		this.dependency = dependency;
	}

	public void updateItemPosition() {
		if(state == CarryState.CARRY) {
			dependency.setCarriedItemPosition(carriedItemID);
		}
	}
	public void update() {
		if(carriedItemID != -1) {
			if(state == CarryState.TAKE) {
				counter -= 1;
				if(counter <= 0) {
					state = CarryState.CARRY;
				}
				else {
					dependency.setTakingItemPosition(carriedItemID, counter);
				}
			}
			else if(state == CarryState.CARRY) {
				if(dependency.setCarriedItemPosition(carriedItemID) == false) {
					carriedItemID = -1;
				}
			}
		}
		else {
			if(state == CarryState.THROW) {
				counter -= 1;
				if(counter <= 0) {
					state = CarryState.IDLE;
				}
			}
		}
	}

	public boolean BButtonPressed() {
		return pressedButton(dependency.getBButtonItemID());
	}
	public boolean AButtonPressed() {
		return pressedButton(dependency.getAButtonItemID());
	}

	public void take(int id) {
		if(dependency.take(id) == true) {
			carriedItemID = id;
			counter = 15;
			state = CarryState.TAKE;
			soundSystem.linkPickUp();
		}
	}
	public void toss() {
		if(state == CarryState.CARRY) {
			dependency.toss(carriedItemID);
			carriedItemID = -1;
			counter = 15;
			state = CarryState.THROW;
			soundSystem.linkThrow();
		}
	}
	public void tossDown() {
		if(carriedItemID != -1) {
			dependency.tossDown(carriedItemID);
			carriedItemID = -1;
			soundSystem.linkThrow();
		}
	}

	public void reset() {
		if(carriedItemID != -1) {
			dependency.removeItem(carriedItemID);
			carriedItemID = -1;
		}
	}

	public boolean isActive() {
		return carriedItemID != -1;
	}
	public boolean canMove() {
		return state != CarryState.TAKE;
	}
	public CarryState getState() {
		return state;
	}
	public int getCounter() {
		return counter;
	}

	private boolean pressedButton(int itemID) {
		if(carriedItemID == -1) {
			return true;
		}
		else {
			ItemType carriedItemType = dependency.getCarriedItemType(carriedItemID);
			if(carriedItemType == ItemType.BOMB) {
				if(dependency.isItemIDActive(3) == false) {
					toss();
					return true;
				}
				else {
					if(itemID == 3) {
						toss();
						return false;
					}
				}
			}
		}
		return false;
	}
}
