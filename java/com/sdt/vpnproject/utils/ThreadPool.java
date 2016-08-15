package com.sdt.vpnproject.utils;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	public static final int THREAD_POOL_MAX_SIZE=10;
	
	private static ExecutorService service = Executors.newFixedThreadPool(THREAD_POOL_MAX_SIZE);
	
	public static void init() {
		service = Executors.newFixedThreadPool(THREAD_POOL_MAX_SIZE);
	}
	
	public static void runThread(Runnable runnable) {
		if(runnable==null) return ;
		if(service.isShutdown()) {
			init();
		}
		service.submit(runnable);
	}
	
	public static void shutdown() {
		service.shutdown();
	}
	public static List<Runnable> shutDownNow() {
		return service.shutdownNow();
	}
}
