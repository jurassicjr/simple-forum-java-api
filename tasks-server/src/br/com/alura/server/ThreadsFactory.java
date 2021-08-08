package br.com.alura.server;

import java.util.concurrent.ThreadFactory;

public class ThreadsFactory implements ThreadFactory {

	private static int NUMBER = 1;
	@Override
	public Thread newThread(Runnable r) {
		Thread thread =  new Thread(r, "Thread - " + NUMBER);
		
		NUMBER++;
		
		thread.setUncaughtExceptionHandler(new ErrorHandler());
		return thread;
	}

}
