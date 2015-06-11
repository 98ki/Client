package com.shape100.gym.protocol;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	private ExecutorService threadPool;
	private static ThreadPool pool;

	private ThreadPool() {
		threadPool = Executors.newCachedThreadPool();
	}

	public static ThreadPool getInstance() {
		if (pool == null) {
			synchronized (ThreadPool.class) {
				if (pool == null) {
					return pool = new ThreadPool();
				}
			}
		}
		return pool;
	}

	public void execute(Runnable runnable) {
		// Runtime.getRuntime().maxMemory();// 最大内存
		threadPool.execute(runnable);
	}
}
