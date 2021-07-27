package br.com.alura.client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TasksClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		Socket socket = new Socket("localhost", 11011);
		
		System.out.println("---- connection established ----");
		
		PrintStream output = new PrintStream(socket.getOutputStream());
		output.println("c1");
		
		Scanner keyboard = new Scanner(System.in);
		
		keyboard.nextLine();
		
		output.close();
		keyboard.close();
		socket.close();
	}

}
