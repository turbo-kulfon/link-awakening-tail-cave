package com.engine.id_manager;

public interface IUniqueIDManager {
	int getUniqueID();
	void returnID(int uniqueID);

	void clear();
}
