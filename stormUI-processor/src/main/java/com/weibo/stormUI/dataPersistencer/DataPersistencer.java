package com.weibo.stormUI.dataPersistencer;

import java.util.List;

public interface DataPersistencer<T> {
	public boolean saveData(List<T> t);
}
