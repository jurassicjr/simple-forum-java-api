package br.com.alura.server;

import java.lang.Thread.UncaughtExceptionHandler;

public class ErrorHandler implements UncaughtExceptionHandler {

	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println("Exceção pega na thread " + t.getName() +  "com a seguinte mensagem: " + e.getMessage());
	}

}
