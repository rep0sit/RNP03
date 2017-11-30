/**
 * 
 */
package gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import mainClasses.ServerThread;

/**
 * @author nelli
 *
 */
public class ServerGui extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4436943265117790657L;
	private JTextArea textArea;
	private ServerThread serverThread;
	
	public ServerGui(){
		setTitle("Server");
		createWindow();
		
		serverThread = new ServerThread(this);
		serverThread.start();
	}
	private void createWindow() {
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Dimension dim = new Dimension(400, 300);
		
		setMinimumSize(dim);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{getSize().width-getSize().width/10, getSize().width/10};
		gridBagLayout.rowHeights = new int[]{getSize().height-getSize().height/10, getSize().height/10};
		
		gridBagLayout.columnWeights = new double[]{0.0, 0.0};
		gridBagLayout.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		GridBagConstraints gbc_textArea_1 = new GridBagConstraints();
		gbc_textArea_1.gridwidth = 2;
		gbc_textArea_1.insets = new Insets(0, 0, 0, 5);
		gbc_textArea_1.fill = GridBagConstraints.BOTH;
		gbc_textArea_1.gridx = 0;
		gbc_textArea_1.gridy = 0;
		getContentPane().add(textArea, gbc_textArea_1);
		Positionings.setWindowPositionMiddle(this, dim);
		
		setVisible(true);
	}
	
	public void writeToConsole(String msg){
		textArea.setCaretPosition(textArea.getDocument().getLength());
		textArea.append(msg);
		textArea.append("\n\r");
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerGui serverFrame = new ServerGui();
		serverFrame.setVisible(true);
	}

}
