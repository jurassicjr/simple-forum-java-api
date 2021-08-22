package br.com.alura.server;

import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 * On a real project here is were some heavy process probably would happens.
 */
public class CommandCallWS implements Callable<String>{

	private PrintStream output;

	public CommandCallWS(PrintStream output) {
		this.output = output;
	}

	@Override
	public String call() throws InterruptedException   {
		output.println("WebService sendo chamado!");
		Thread.sleep(15000);
		
		int number = new Random().nextInt(100) + 1;
		
		System.out.println("C2 WebService command finalizado");
		return Integer.toString(number);
	}
	

	

}
