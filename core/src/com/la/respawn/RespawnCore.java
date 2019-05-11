package com.la.respawn;

import java.util.ArrayList;
import java.util.List;

import com.la.respawn.RespawnElement.RespawnType;

public class RespawnCore {
	private List<Integer> visitedUniqueRooms = new ArrayList<>();
	private List<RespawnElement> respawnElements = new ArrayList<>();

	public void addElement(RespawnElement element) {
		if(respawnElements.contains(element) == false) {
			respawnElements.add(element);
		}
	}
	public void removeElement(RespawnElement element) {
		respawnElements.remove(element);
	}

	public void update(int xMap, int yMap, int roomID, boolean transition) {
		if(transition == true) {
			visitedUniqueRooms.clear();
			respawn(RespawnType.TRANSITION);
		}
		else {
			if(roomOnList(roomID) == false) {
				visitedUniqueRooms.add(roomID);
				if(visitedUniqueRooms.size() >= 8) {
					respawn(RespawnType.SCREEN_VISIT);
				}
			}
		}
	}

	private void respawn(RespawnType respawnType) {
		visitedUniqueRooms.clear();
		for (RespawnElement respawnElement : respawnElements) {
			respawnElement.update(respawnType);
		}
	}
	private boolean roomOnList(int roomID) {
		for (Integer id : visitedUniqueRooms) {
			if(id.equals(roomID) == true) {
				return true;
			}
		}
		return false;
	}
}
