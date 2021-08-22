package br.com.alura.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class TasksServer {

	private static Socket socket;
	private ServerSocket server;
	private ExecutorService threadPool;
	private AtomicBoolean isRunning;
	private BlockingQueue<String> commandQueue;

	public TasksServer() throws IOException {
		System.out.println("---- Starting the server ----");
		this.server = new ServerSocket(11011);
		this.threadPool = Executors.newCachedThreadPool(new ThreadsFactory());
		this.isRunning = new AtomicBoolean(true);
		this.commandQueue = new ArrayBlockingQueue<String>(3);
		initializeConsumers();
	}

	private void initializeConsumers() {
		for (int i = 0; i < 2; i++) {
			ConsumerTask consumer = new ConsumerTask(commandQueue);
			this.threadPool.execute(consumer);
		}
	}

	public void run() throws IOException {
		while (this.isRunning.get()) {
			try {
				socket = server.accept();
				System.out.println("--- Accepting new client at port - " + socket.getPort());

				TasksDistribution tasksDistribution = new TasksDistribution(threadPool, commandQueue, socket, this);
				threadPool.execute(tasksDistribution);
			} catch (SocketException e) {
				System.out.println("socket exception, is running? " + this.isRunning);
			}
		}
	}
	
	public void shutdown() throws IOException {
		this.isRunning.set(false);
		server.close();
		threadPool.shutdown();
	}

	public static void main(String[] args) throws IOException {
		TasksServer server = new TasksServer();
		server.run();
		server.shutdown();
	}
}
