package mainClasses;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;



abstract class AbstractWriteThread extends Thread{
	protected PrintWriter pw;
	protected BufferedReader br;
	protected Socket socket;
	
	
	
	
	/**
	 * writes a String message on outStream.
	 * 
	 * @param message
	 * @throws ThreadNotRunningException
	 */
	public void write(String message) {
		
		pw.write(message + "\n");
		pw.flush();
		
	}
	
	
}
