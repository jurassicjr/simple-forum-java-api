package br.com.alura.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TasksServer {

	private static Socket socket;

	public static void main(String[] args) throws IOException {

		System.out.println("---- Starting the server ----");
		
		try (ServerSocket server = new ServerSocket(11011)) {
			ExecutorService threadPool = Executors.newCachedThreadPool();
			
			while(true) {
				socket = server.accept();			
				System.out.println("--- Accepting new client at port - " + socket.getPort());
				
				TasksDistribution  tasksDistribution = new TasksDistribution(socket);
				threadPool.execute(tasksDistribution);
			}
		}
	}
}
