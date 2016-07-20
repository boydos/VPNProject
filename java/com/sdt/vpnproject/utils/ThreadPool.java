package com.sdt.vpnproject.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadPool {
	private static ExecutorService service = Executors.newFixedThreadPool(10);
	
	public static Future<?> runThread(Runnable runnable) {
		if(runnable==null) return null;
		return service.submit(runnable);
	}
}
