package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import mainClasses.ClientThread;

public class ClientGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextArea txtrHistory;
	
	private JPanel contentPane;
	//private String name, address;
	//private int port;
	private JTextField txtMessage;
	
	
	
	//private InetAddress ip;

	private ClientThread client;
	
	private static final int MIN_WIDTH = 400;
	private static final int MIN_HEIGHT = 150;
	
	//private DefaultCaret caret;
	
	/**
	 * Create the frame.
	 */
public ClientGui(String name, String address, int port) {
		setTitle("Client");
		
//		this.name = name;
//		this.address = address;
//		this.port = port;
		
		createWindow();
		
		console("Attempting a connection with " + address + ":" + port + ", user: " + name + " ...");
		
		client = new ClientThread(name, address, port);
		client.setGui(this);
		client.start();
	}
	
	




	private void createWindow(){
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		
		
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent windowEvent){
				
				dispose();
//				client.write(Commands.QUIT);
				client.close();
			}
		});
		//setLocationRelativeTo(null);
		
		Positionings.setWindowSizeAccordingToResolution(this);
		

		Dimension minSize = new Dimension(MIN_WIDTH, MIN_HEIGHT);
		
		setMinimumSize(minSize);
		
		
		int width = getSize().width;
		int height = getSize().height;
		
		int smallPartWidth = width / 10;
		int smallPartHeight = height / 10;
		
		int bigPartWidth = width - smallPartWidth;
		int bigPartHeight = height - smallPartHeight;
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5,5,5,5));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		
		gridBagLayout.columnWidths = new int[]{bigPartWidth, smallPartWidth};
		gridBagLayout.rowHeights = new int[]{bigPartHeight, smallPartHeight};
		
		gridBagLayout.columnWeights = new double[]{1.0, 0.0};
		gridBagLayout.rowWeights = new double[]{1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		txtrHistory = new JTextArea();
		txtrHistory.setLineWrap(true);
		txtrHistory.setEditable(false);
		
		
		JScrollPane scroll = new JScrollPane(txtrHistory);
		
		/*
		 * eventually this doesn'T work on macs.
		 * See console() method for alternative.
		 */
		//caret = (DefaultCaret) txtrHistory.getCaret();
		//caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		GridBagConstraints scrollConstraints = new GridBagConstraints();
		scrollConstraints.insets = new Insets(0, 0, 5, 5);
		scrollConstraints.fill = GridBagConstraints.BOTH;
		scrollConstraints.gridx = 0;
		scrollConstraints.gridy = 0;
		scrollConstraints.gridwidth = 1;
		scrollConstraints.gridheight = 1;
		scrollConstraints.insets = new Insets(0,5,0,0);
		getContentPane().add(scroll, scrollConstraints);
		
		txtMessage = new JTextField();
		
		GridBagConstraints gbc_txtMessage = new GridBagConstraints();
		gbc_txtMessage.insets = new Insets(0, 5, 0, 0);
		gbc_txtMessage.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtMessage.gridx = 0;
		gbc_txtMessage.gridy = 1;
		
		getContentPane().add(txtMessage, gbc_txtMessage);
		txtMessage.setColumns(10);
		
		JButton btnSend = new JButton("Send");
		
		
		
		GridBagConstraints gbc_btnSend = new GridBagConstraints();
		gbc_btnSend.anchor = GridBagConstraints.WEST;
		gbc_btnSend.insets = new Insets(0, 5, 0, 0);
		gbc_btnSend.gridx = 1;
		gbc_btnSend.gridy = 1;
		getContentPane().add(btnSend, gbc_btnSend);
		
		
		
		//Listeners
		btnSend.addActionListener(e -> send());
		
		txtMessage.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					send();
				}
			}
		});
		//The first focus is set on the text message text field.
		setVisible(true);
		txtMessage.requestFocusInWindow();
		
		
	}
	
	//helpers
	
	//sendbutton invoked
	private void send(){
		String message = txtMessage.getText();
		
		if(!message.equals("")){
			//console(name + ": " + txtMessage.getText());
			
			client.write(message);
			
			txtMessage.setText("");
			txtMessage.requestFocusInWindow();
			
			
		}
		
	}
	

	public void console(String message){
		consoleSameLine(message);
		
		txtrHistory.append("\n\r");
	}
	
	private void consoleSameLine(String message){
		txtrHistory.setCaretPosition(txtrHistory.getDocument().getLength());
		txtrHistory.append(message);
		
	}
	
	
//
//	public String getClientName() {
//		return this.name;
//	}
//
//
//
//	public String getAddress() {
//		return this.address;
//	}
//
//
//
//	public int getPort() {
//		return this.port;
//	}
//
//
//
//	public InetAddress getIp() {
//		return this.ip;
//	}
//	
//	

}
