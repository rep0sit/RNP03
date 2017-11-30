package gui;

import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


/*
 * 
 */
public class LoginGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2915779788258391995L;
	
	
	private String name = "";
	private String address = "";
	private int port;
	
	
	
	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAddress;
	private JLabel lblNewLabel_1;
	private JTextField txtPort;
	private JLabel lblNewLabel_2;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginGui frame = new LoginGui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginGui() {
		
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		setTitle("Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 280, 370);
		setSize(226, 316);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtName = new JTextField();
		
		txtName.setBounds(22, 57, 164, 20);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Name:");
		lblNewLabel.setBounds(82, 39, 46, 14);
		contentPane.add(lblNewLabel);
		
		txtAddress = new JTextField();
		txtAddress.setBounds(22, 118, 164, 20);
		contentPane.add(txtAddress);
		txtAddress.setColumns(10);
		
		lblNewLabel_1 = new JLabel("IP Address:");
		lblNewLabel_1.setBounds(66, 100, 78, 14);
		contentPane.add(lblNewLabel_1);
		
		txtPort = new JTextField();
		txtPort.setBounds(22, 182, 164, 20);
		contentPane.add(txtPort);
		txtPort.setColumns(10);
		
		lblNewLabel_2 = new JLabel("Port:");
		lblNewLabel_2.setBounds(82, 164, 46, 14);
		contentPane.add(lblNewLabel_2);
		
		JButton btnLogin = new JButton("Login");
		
		btnLogin.setBounds(60, 228, 89, 23);
		contentPane.add(btnLogin);
		
		
		//Enter-Key Adapter for login
		
		KeyAdapter enter = new KeyAdapter(){
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER){
					login();
				}
			}

		};
		
		txtName.requestFocusInWindow();
		
		//Listeners
		
		btnLogin.addActionListener(e -> login());
				
		
		txtName.addKeyListener(enter);
		txtAddress.addKeyListener(enter);
		txtPort.addKeyListener(enter);
		
	}
	
	
	/**
	 * Login Stuff
	 * 
	 * 
	 */
	private void login(){
		
		if(varsCheck()){
			this.dispose();
			initVars();
			new ClientGui(name, address, port);
			
		}
		
		
	}

	private void initVars() {
		name = txtName.getText();
		address = txtAddress.getText();
		port = Integer.parseInt(txtPort.getText());
		
	}

	private boolean varsCheck() {
		boolean portCheck = txtPort.getText().matches("[0-9]+");
		boolean nameCheck = !txtName.getText().isEmpty();
		boolean ipCheck = !txtAddress.getText().isEmpty();
		boolean completeCheck = portCheck && nameCheck && ipCheck;
		
		if(!completeCheck){
			StringBuilder mb = new StringBuilder();
			
			if(!portCheck){
				txtPort.setText("");
				txtPort.requestFocusInWindow();
				mb.append("Enter a numerical Value for Port!\n");
			}
			
			if(!ipCheck){
				txtAddress.setText("");
				txtAddress.requestFocusInWindow();
				mb.append("The field IP Address must not be empty!\n");
			}
			
			if(!nameCheck){
				txtName.setText("");
				txtName.requestFocusInWindow();
				mb.append("The field Name must not be empty!\n");
			}
			
			JOptionPane.showMessageDialog(this, mb.toString());
		}
		
		
		
		
		
		
		return completeCheck;
		
	}

	
}
