package br.com.alura.server;

import java.util.concurrent.BlockingQueue;

public class ConsumerTask implements Runnable{
	
	
	private BlockingQueue<String> commandsQueue;

	public ConsumerTask(BlockingQueue<String> commandsQueue) {
		this.commandsQueue = commandsQueue;
	
	}

	@Override
	public void run() {
		
		String command = null;
		try {
			
			while((command = commandsQueue.take()) != null) {
				System.out.println("consumindo o comando " + command);				
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
