package com.weibo.stormUI.dataLoader;

import java.util.List;

public interface DataLoader<T> {
	public List<T> nextData();
}
