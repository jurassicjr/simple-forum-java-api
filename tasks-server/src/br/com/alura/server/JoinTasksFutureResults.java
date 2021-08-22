package br.com.alura.server;

import java.io.PrintStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class JoinTasksFutureResults implements Runnable {

	private Future<String> futureDB;
	private Future<String> futureWS;
	private PrintStream output;

	public JoinTasksFutureResults(Future<String> futureDB, Future<String> futureWS, PrintStream output) {
		this.futureDB = futureDB;
		this.futureWS = futureWS;
		this.output = output;
	}

	@Override
	public void run() {
		try {
			String wsResult = this.futureWS.get(15, TimeUnit.SECONDS);
			String dbresult = this.futureDB.get(15, TimeUnit.SECONDS);
			
			
			output.println("Resultado comando C2: " + wsResult + ", " + dbresult);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			output.println("Timeout: execução do comando c2");
			this.futureDB.cancel(true);
			this.futureWS.cancel(true);
		}
	}

}
