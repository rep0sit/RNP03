package gui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
/**
 * 
 * @author Nelli Welker, Etienne Onasch
 *
 */
final class Positionings {
	
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private static double width = screenSize.getWidth();
	private static double height = screenSize.getHeight();
	
	/*
	 * a ratio of 3 means that the size of the window will be 1/3 of your resolution
	 */
	
	private static final int RATIO = 2;
	private static final int HEIGHT_RATIO = RATIO;
	private static final int WIDTH_RATIO = RATIO;
	
	private Positionings(){}
	
	
	/**
	 * Method to place the position of a window exactly in the middle of the screen,
	 * regardless of its resolution.
	 */
	public static void setWindowPositionMiddle(JFrame jf, int windowWidth, int windowHeight) {
		
		
	
	
		int halfWidth = (int) width / 2;
		int halfHeight = (int) height / 2;
		
		int halfWindowWidth = windowWidth / 2;
		int halfWindowHeight = windowHeight / 2;
		
		//Window is loaded always in the middle of the screen.
		jf.setLocation(halfWidth - halfWindowWidth, halfHeight - halfWindowHeight);
		
		//System.out.println(screenSize);
		
	}


	public static void setWindowPositionMiddle(JFrame jf, Dimension size) {
		setWindowPositionMiddle(jf, size.width, size.height);
	}
	
	
	public static void setWindowSizeAccordingToResolution(JFrame jf) {
		int frameHeight = (int) height / HEIGHT_RATIO;
		int frameWidth = (int) width / WIDTH_RATIO;
		jf.setSize(frameWidth, frameHeight);
		
		setWindowPositionMiddle(jf, frameWidth, frameHeight);
		
	}
	
	
	
}
