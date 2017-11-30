package mainClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gui.ServerGui;
import utils.Commands;
import utils.Constants;

public final class ServerThread extends AbstractClientServerThread{
	
	static final String LOBBY = "lobby";
	static final String MUSIC = "music";
	static final String GOSSIP = "gossip";
	
	
	static List<String> rooms = new ArrayList<>(Arrays.asList(LOBBY, MUSIC, GOSSIP));
	static List<String> completeLog = new ArrayList<>();
	
	//private List<String> userNames = new ArrayList<>();
	private List<UserThread> users = new ArrayList<>();
	
	
	
	private ServerSocket serverSocket;
	
	private ServerGui serverGui = null;
	
	
	
	public ServerThread(int port) {
		this.port = port;
		try {
			this.serverSocket = new ServerSocket(port);
			
			/*
			 * if gui != null br von gui
			 */
			br = new BufferedReader(new InputStreamReader(System.in));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public ServerThread(ServerGui gui){
		this(Constants.SERVER_STANDARD_PORT);
		serverGui = gui;
		serverGui.writeToConsole("Server is running on port " + Constants.SERVER_STANDARD_PORT);
	}
	public ServerThread() {
		this(Constants.SERVER_STANDARD_PORT);
	}
	
	
	
	public void disconnect() {
		System.exit(-1);
		closed = true;
		try {
			br.close();
			serverSocket.close();
			serverGui.dispose();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	static synchronized void addToLog(String message) {
		completeLog.add(message);
	}
	static void printLog() {
		System.out.println("COMPLETE LOG: ");
		System.out.println("################################");
		for(String line : completeLog) {
			System.out.println(line);
		}
		System.out.println("################################");
	}
	/**
	 * This method adds a new room and returs true if it doesn't exit yet.
	 * If the room already exists, this method returns false.
	 * @param room the name of the new room
	 * @return true, if the room don't exists already, false else.
	 */
	static synchronized boolean addRoom(String room) {
		boolean contains = rooms.contains(room);
		
		if(!contains) {
			rooms.add(room);
		}
		return !contains;
		
	}
	
	@Override
	public void run() {
		selfMessage("Server running on port ", Integer.toString(port));
		
		
		while(!closed) {
			new ConsoleReader();
			try {
				socket = serverSocket.accept();
				selfMessage("Login Attempt: ", "IP = ", socket.getInetAddress().toString());
				
				if(users.size() >= Constants.MAX_CLIENT_THREADS) {
					pw = new PrintWriter(socket.getOutputStream());
					write(Commands.SERVER_FULL + "Sorry, Server is Full.");
					pw.close();
				}else {
//					new ClientLoginThread(socket, users);
					new ClientLoginThread(socket, users, serverGui);
				}
				
				
				
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	public static void main(String...args) {
		int port;
		ServerThread st = new ServerThread();
		if(args.length == 1) {
			port = Integer.parseInt(args[0]);
			st = new ServerThread(port);
			
		}
		
		st.start();
	}

	class ConsoleReader extends Thread{
		
		ConsoleReader(){
			this.setDaemon(true);
			start();
		}
		
		@Override
		public void run() {
			String command = "";
			while(!closed) {
				try {
					/*
					 * br liest von System.in
					 */
					command = br.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if(command.equals(Commands.STOP) || command.equals(Commands.QUIT)) {
					disconnect();
				}
				
				else if(command.startsWith(Commands.KICK_USER)) {
					String user = command.substring(Commands.KICK_USER.length());
					for(UserThread u : users) {
						if (u.getUserName().equals(user)) {
							u.close();
							users.remove(u);
							break;
							
							//serverGui.writeToConsole("User "+u.getName()+" was kicked.");
						}
						
					}
				}
				else if(command.equals(Commands.LOG)) {
					printLog();
				}
				
				else if(command.equals(Commands.USERS)) {
					StringBuilder sb = new StringBuilder();
					for(UserThread u : users) {
						sb.append("[").append(u).append("]");
					}
					selfMessage(sb.toString());
				}
				
			}
		}
	}
	
}
