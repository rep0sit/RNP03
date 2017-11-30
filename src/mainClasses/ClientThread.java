package mainClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import gui.ClientGui;
import utils.Commands;
import utils.Constants;

/**
 * 
 * @author Nelli Welker, Etienne Onasch
 *
 */
public final class ClientThread extends AbstractClientServerThread {

	//private String serverIp;
	private String name;
	private InetAddress inetAddress;
	
	private ClientGui gui = null;
	

	// state
	//private boolean loggedIn = false;
	
	
	private int timeout = Constants.TIMEOUT_5SEC;
	
	public ClientThread(String name, String serverIp, int port) {
		this.name = name;
		//this.serverIp = serverIp;
		this.port = port;
		try {
			this.inetAddress = InetAddress.getByName(serverIp);
		} catch (UnknownHostException e) {
			selfMessage("serverIp couldn't be resolved into a valid internet address.");
			e.printStackTrace();
		}

	}
	
	private void init() {

		try {
			
			socket = new Socket();
			socket.connect(new InetSocketAddress(inetAddress, port), timeout);
			
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(socket.getOutputStream());
		} 
		catch (SocketTimeoutException e) {

			e.printStackTrace();
		}
		
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		selfMessage("ClientThread initialized.");
	}
	
	
	public void setGui(ClientGui gui) {
		this.gui = gui;
	}
	

	/**
	 * Writes a server response to the user invoking the message(String message)
	 * method.
	 * 
	 * @param command
	 *            a command from server to client defined in Commands.
	 * @param interpretation
	 *            the interpretation of the command.
	 * @param completeLine
	 *            the complete line the server has sended over the stream. (command
	 *            + optional message)
	 */
	private void selfMessageResponse(String command, String interpretation, String completeLine) {
		StringBuilder sb = new StringBuilder();
		sb.append("Server command: ").append(command);
		if (!interpretation.equals("")) {
			sb.append("(Interpretation = ").append(interpretation).append(")");
		}
		if (completeLine.length() > command.length()) {
			sb.append("[optional server message: ").append(completeLine.substring(command.length())).append("]");
		}
		selfMessage(sb.toString());
	}

	/**
	 * Writes a server response to the user invoking the message(String message)
	 * method.
	 * 
	 * @param command
	 *            a command from server to client defined in Commands.
	 * @param completeLine
	 *            the complete line the server has sended over the stream. (command
	 *            + optional message)
	 */
	private void selfMessageResponse(String command, String completeLine) {
		selfMessageResponse(command, "", completeLine);
	}

	
	@Override
	protected void selfMessage(String message) {
		if(gui != null) {
			gui.console(message);
		}
		super.selfMessage(message);
	}
	
	
	@Override
	public void run() {
		init();
		String currentLine;
		boolean invalidName = false;
		System.out.println("ClientThread is running now.");
		try {
			while (!closed && (currentLine = br.readLine()) != null) {
				// server commands
				if (currentLine.equals(Commands.GIVE_USERNAME)) {
					write(name);
				}
				else if(currentLine.startsWith(Commands.FORCE_DISCONNECT)) {
					//selfMessage("You were kicked from server.");
					selfMessageResponse(Commands.FORCE_DISCONNECT, "kicked from server", currentLine);
					close();
					gui.dispose();
					break;
				}

				// negative server responses
				else if (currentLine.startsWith(Commands.USERNAME_TAKEN)) {
					selfMessageResponse(Commands.USERNAME_TAKEN, "username already taken", currentLine);
					invalidName = true;
				} 
				else if (currentLine.startsWith(Commands.INVALID_USERNAME)) {
					selfMessageResponse(Commands.INVALID_USERNAME, "invalid username", currentLine);
					invalidName = true;
				} 
				else if (currentLine.startsWith(Commands.SERVER_FULL)) {
					selfMessageResponse(Commands.SERVER_FULL, "server already full", currentLine);
					close();
					gui.dispose();
					break;
				}

				// positive server responses
				else if (currentLine.startsWith(Commands.LOGGED_IN)) {
					//loggedIn = true;
					selfMessageResponse(Commands.LOGGED_IN, "you are logged in now", currentLine);
				}

				else if (currentLine.startsWith(Commands.VALID_USERNAME)) {
					selfMessageResponse(Commands.VALID_USERNAME,"username was valid", currentLine);
					invalidName = false;
				} 
				else if (currentLine.startsWith(Commands.IN_ROOM)) {
					selfMessageResponse(Commands.IN_ROOM, currentLine);
				}

				else {
					selfMessage(currentLine);
				}

				if (invalidName) {
					selfMessage("--> Enter new username.");
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			
			System.out.println("Server does not respond anymore!");
		}
	}
	
	public static void main(String...args) throws IOException {
		if(args.length != 3) {
			System.out.println("3 arguments needed! <username>, <server ip> and <server port>.");
		}else {
			String username = args[0];
			String ip = args[1];
			String port = args[2];
			
			ClientThread ct = new ClientThread(username, ip, Integer.parseInt(port));
			ct.start();
			
		}
		
	}

	
}
