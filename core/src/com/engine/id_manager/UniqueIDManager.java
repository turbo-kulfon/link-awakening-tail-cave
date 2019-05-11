package com.engine.id_manager;

import java.util.ArrayList;
import java.util.List;

public class UniqueIDManager implements IUniqueIDManager {
	private List<Boolean> ids = new ArrayList<>();

	@Override
	public int getUniqueID() {
		for(int i = 0; i < ids.size(); ++i) {
			if(ids.get(i) == true) {
				ids.set(i, false);
				return i;
			}
		}
		ids.add(new Boolean(false));
		return ids.size()-1;
	}
	@Override
	public void returnID(int uniqueID) {
		ids.set(uniqueID, true);
	}

	@Override
	public void clear() {
		for(int i = 0; i < ids.size(); ++i) {
			ids.set(i, true);
		}
	}
}
