package br.com.alura.server;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class TasksDistribution implements Runnable{

	private Socket socket;

	public TasksDistribution(Socket socket) {
		this.socket = socket;
		
	}

	@Override
	public void run() {
		
		System.out.println("Distributing tasks");
		
		try {
			Scanner clientInput = new Scanner(socket.getInputStream());
			
			while(clientInput.hasNextLine()) {
				String command = clientInput.nextLine();
				System.out.println(command);
			}
			
			clientInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
