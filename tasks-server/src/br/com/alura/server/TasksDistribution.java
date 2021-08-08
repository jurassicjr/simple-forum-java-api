package br.com.alura.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class TasksDistribution implements Runnable{

	private Socket socket;
	private TasksServer server;
	private ExecutorService threadPool;
	private BlockingQueue<String> commandQueue;

	public TasksDistribution(ExecutorService threadPool, BlockingQueue<String> commandQueue, Socket socket, TasksServer server) {
		this.threadPool = threadPool;
		this.commandQueue = commandQueue;
		this.socket = socket;
		this.server = server;
	}

	@Override
	public void run() {
		
		System.out.println("Distributing tasks");
		
		try {
			Scanner clientInput = new Scanner(socket.getInputStream());
			PrintStream output = new PrintStream(socket.getOutputStream());
			
			while(clientInput.hasNextLine()) {
				String command = clientInput.nextLine();
				
				System.out.println("Executando comando " + command);
				switch (command) {
				case "c1": {
					output.println("Confirmação comando C1");
					CommandC1 c1 = new CommandC1(output);
					threadPool.execute(c1);
					break;
				}
				case "c2": {
					output.println("Confirmação comando C2");
					CommandCallWS callWebService = new CommandCallWS(output);
					CommandAccessDatabase accessDatabase = new CommandAccessDatabase(output);
					Future<String> futureDB = threadPool.submit(accessDatabase);
					Future<String> futureWS = threadPool.submit(callWebService);
					
					this.threadPool.execute(new JoinTasksFutureResults(futureDB, futureWS, output));
					break;
				}
				case "c3": {
					this.commandQueue.put("c3");
					output.println("Comando c3 adicionado na fila");
					break;
				}
				case "shutdown": {
					output.println("Desligando o servidor!");
					server.shutdown();
					break;
				}
				default:
					output.println("Comando não encontrado");
				}
				
				System.out.println(command);
			}
			
			output.close();
			clientInput.close();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
