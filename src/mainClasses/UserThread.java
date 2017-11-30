package mainClasses;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import gui.ServerGui;
import utils.Commands;

class UserThread extends AbstractWriteThread {
	private String name;
	private String room;
	private List<UserThread> users;
	private List<String> log = new ArrayList<>();
	private boolean closed = false;
	
	private ServerGui serverGui;
	
	public UserThread(String name, Socket socket, List<UserThread> users) {
		this.socket = socket;
		this.name = name;
		this.users = users;
		room = ServerThread.LOBBY;
		setDaemon(true);
		init();
		start();
	}

	public UserThread(String userName, Socket socket, List<UserThread> users, ServerGui serverGui) {
		this(userName, socket, users);
		this.serverGui = serverGui;
		
	}

	private void init() {
		try {
			pw = new PrintWriter(socket.getOutputStream());
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String getUserName() {
		return name;
	}
	
	public String getRoom() {
		return room;
	}
	
	public void setRoom(String room) {
		this.room = room;
	}
	
	@Override
	public synchronized void write(String message) {
		log.add(message);
		super.write(message);
	}
	
	private void writeList(List<?> list) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for(Object s : list) {
			sb.append(s.toString());
			i++;
			if(i < list.size()) {
//				sb.append("[").append(s).append("]");
				sb.append("\n");
			}
		}
		super.write(sb.toString());
	}
	
	private String getOptionalMessage(String serverCommand, String completeLine) {
		return completeLine.substring(serverCommand.length());
	}
	
	
	@Override
	public String toString() {
		return name;
	}
	
	@Override
	public void run() {
		String inc = "";
		
		boolean whisper = false;
		
		UserThread whisperToUser = null;
		try {
			while(!closed && (inc = br.readLine()) != null) {
				
				
				
				if(inc.equals(Commands.SHOW_COMMANDS)) {
					super.write("USER COMMANDS: ");
					writeList(Commands.USER_COMMANDS);
				}
				
				else if(inc.startsWith(Commands.GOTO_ROOM)) {
					
					String designatedRoom = getOptionalMessage(Commands.GOTO_ROOM, inc);
					if(ServerThread.rooms.contains(designatedRoom)) {
						super.write("You are in the room " + designatedRoom +" now.");
						setRoom(designatedRoom);
						whisper = false;
//						if(serverGui != null){
//							serverGui.writeToConsole("User "+this.name+" switched to room: "+designatedRoom);
//						}
					}
					else {
						super.write("The room " + designatedRoom + " does not exist!");
					}
				}
				
				
				else if(inc.startsWith(Commands.ADD_ROOM)) {
					String roomName = getOptionalMessage(Commands.ADD_ROOM, inc);
					
					if (ServerThread.addRoom(roomName)) {
						super.write("Room " + roomName + " created.");
					}else {
						super.write("Room " + roomName + " already exists.");
					}
				}
				
				else if(inc.startsWith(Commands.WHISPER)) {
					String user = getOptionalMessage(Commands.WHISPER, inc);
					boolean found = false;
					for(UserThread u : users) {
						if(u.getUserName().equals(user)) {
							whisperToUser = u;
							found = true;
							break;
						}
					}
					if(!found) {
						super.write("The user " + user + " does not exist.");
					}else {
						super.write("You are whispering now with user " + user + ".");
						whisper = true;
					}
				}
				else if(inc.equals(Commands.UN_WHISPER)) {
					super.write("You are talking to everyone in the chat now.");
					whisper = false;
				}
				
				else if(inc.equals(Commands.USERS)) {
					super.write("USERS: ");
					writeList(users);
				}
				
				else if(inc.equals(Commands.ROOMS)) {
					super.write("CHAT ROOMS:");
					writeList(ServerThread.rooms);
				}
				else if(inc.equals(Commands.LOG)) {
					super.write("CHAT LOG:");
					super.write("###########################################################");
					writeList(log);
					super.write("###########################################################");
				}
				else if (inc.equals(Commands.QUIT)) {
					// close socket and streams
					super.write("User " + name + " disconnected.");
					users.remove(this);
					String str = "User " + name + " left this room.";
					// write to other users that user "name" left room
					ServerThread.addToLog(str);
					for (UserThread u : users) {
						if (u.getRoom().equals(room)) {

							u.write(str);
						}
					}
					if (serverGui != null) {
						serverGui.writeToConsole("User " + this.name + " left chat.");
					} else {
						System.out.println("User " + this.name + " left chat.");
					}
					close();
				}
				// Write to every user in room
				else if (inc != null && !inc.equals("") && Commands.messageAllowed(inc)) {
					
					String toWrite = "[" + room + "] " + name + ": " + inc;
					
					
					if(!whisper) {
						ServerThread.addToLog(toWrite);
						for(UserThread u : users) {
							if(u.getRoom().equals(room)) {
								
								//log(toWrite);
								u.write(toWrite);
							}
						}
					}
					else {
						toWrite = "[" + room + "] " + name + "(whispering): " + inc;
						ServerThread.addToLog(toWrite);
						whisperToUser.write(toWrite);
						write(toWrite);
					}
				}	
			}
			
		} catch (IOException e) {
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			users.remove(this);
			//e.printStackTrace();
		}
	}

	public void close() {
		try {
			closed = true;
			write(Commands.FORCE_DISCONNECT);
			br.close();
			pw.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
