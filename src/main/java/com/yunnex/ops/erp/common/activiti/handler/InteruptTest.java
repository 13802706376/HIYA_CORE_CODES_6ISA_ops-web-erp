package com.yunnex.ops.erp.common.activiti.handler;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class InteruptTest {
	public static void main(String[] args) throws InterruptedException {
		ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 10, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
		executor.execute(new Task("0"));
		executor.execute(new Task("1"));
		Thread.sleep(1);
		executor.shutdown();
		System.out.println("executor has been shutdown");
	}

	static class Task implements Runnable {
		String name;

		public Task(String name) {
			this.name = name;
		}

		@Override
		public void run() {
			for (int i = 1; i <= 100 && !Thread.interrupted(); i++) {
				Thread.yield();
				System.out.println("task " + name + " is running, round " + i);
			}
		}
	}
}
