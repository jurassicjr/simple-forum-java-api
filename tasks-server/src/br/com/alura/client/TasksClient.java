package br.com.alura.client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class TasksClient {

	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {

		Socket socket = new Socket("localhost", 11011);

		System.out.println("---- connection established ----");

		Thread sendCommandThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try (PrintStream output = new PrintStream(socket.getOutputStream())) {

					try (Scanner keyboard = new Scanner(System.in)) {

						System.out.println("--- Pronto para enviar dados ---");

						while (keyboard.hasNextLine()) {
							String line = keyboard.nextLine();

							if (line.trim().equals("")) {
								break;
							}

							output.println(line);
						}
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		Thread receiveCommandThread = new Thread(new Runnable() {

			@Override
			public void run() {
				try (Scanner serverInput = new Scanner(socket.getInputStream())) {
					System.out.println("--- Pronto para receber dados ---");
					while (serverInput.hasNext()) {
						String line = serverInput.nextLine();
						System.out.println(line);
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});

		sendCommandThread.start();
		receiveCommandThread.start();
		
		sendCommandThread.join();
		
		socket.close();
	}

}
