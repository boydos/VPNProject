package com.sdt.vpnproject.utils;

import java.util.LinkedHashMap;

public class LRUCache<K,V> extends LinkedHashMap<K, V>{

	private static final long serialVersionUID = 208092839092837L;
	private int maxSize;
	private CleanUpCallBack<K, V> callback;
	
	public LRUCache(int maxSize,CleanUpCallBack<K,V> callback) {
		super(maxSize+1, 1, true);
		this.maxSize =maxSize;
		this.callback = callback;
	}
	
	
	@Override
	protected boolean removeEldestEntry(Entry<K, V> eldest) {
		// TODO Auto-generated method stub
		if(size()>maxSize) {
			callback.clean(eldest);
			return true;
		}
		
		return false;
	}


	public interface CleanUpCallBack<K,V> {
		
		public void clean(Entry<K , V> entry);
	}
}
