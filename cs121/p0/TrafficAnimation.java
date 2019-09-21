import java.awt.BasicStroke;
import java.awt.geom.Line2D;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * CS 121 Project 1: Traffic Animation
 *
 * This project creates an animation where a snowman is being carried across the screen upon a catapault, while the red snowman is helpless (and armless) to help.
 *
 * @author BSU CS 121 Instructors
 * @author austen hale
 */
@SuppressWarnings("serial")
public class TrafficAnimation extends JPanel
{
	// This is where you declare constants and variables that need to keep their
	// values between calls	to paintComponent(). Any other variables should be
	// declared locally, in the method where they are used.

	/**
	 * A constant to regulate the frequency of Timer events.
	 * Note: 100ms is 10 frames per second - you should not need
	 * a faster refresh rate than this
	 */
	private final int DELAY = 100; //milliseconds

	/**
	 * The anchor coordinate for drawing / animating. All of your vehicle's
	 * coordinates should be relative to this offset value.
	 */
	private int xOffset = 0;

	/**
	 * The number of pixels added to xOffset each time paintComponent() is called.
	 */
	private int stepSize = 15;

	private final Color BACKGROUND_COLOR = Color.black;

	/* This method draws on the panel's Graphics context.
	 * This is where the majority of your work will be.
	 *
	 * (non-Javadoc)
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g)
	{
		// Get the current width and height of the window.
		int width = getWidth(); // panel width
		int height = getHeight(); // panel height

		// Fill the graphics page with the background color.
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0, width, height);
		
		

		// Calculate the new xOffset position of the moving object.
		xOffset  = (xOffset + stepSize) % width;

		// TODO: Use width, height, and xOffset to draw your scalable objects
		// at their new positions on the screen
		
		//Colors
		Color redWheel = new Color(236, 16, 16);
		Color grass = new Color(38,127,1);
		Color greenerGrass = new Color(108, 175, 7);
		Color baseVehicle = new Color(119, 32, 0);
		Color brownLever = new Color(79, 53, 51);
		Color purpleEnd = new Color(148,10, 173);
		Color snowBlue = new Color(131,222,234);
		Color brownStick = new Color(109,55,4);
		Color blackEye = new Color (0,0,0);
		
		
		//Panel Sections
		g.setColor(greenerGrass);
		g.fillRect(0, 0, width, height);
		g.setColor(grass);
		g.fillRect(0, 0+(height/2), width, height/2);
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, 0+(height/3), width, height/2);
		
		// Object base
		int baseHeight = height / 4;
		int baseWidth = width/2;
		int squareY = height / 2 - baseHeight / 2;
		
		g.setColor(baseVehicle);
		g.fillRect(xOffset, squareY, baseWidth, baseHeight);
		
		//Wheels
		int baseWheelHeight = height/8;
		int baseWheelWidth = width/8;
		int wheelY = height/2 - baseWheelHeight / 2;
		g.setColor(redWheel);
		g.fillOval(xOffset, wheelY  + (height/6), baseWheelWidth, baseWheelHeight);
		g.fillOval(xOffset + (width /6), wheelY + (height/6), baseWheelWidth, baseWheelHeight);
		g.fillOval(xOffset + (width/3), wheelY + (height/6), baseWheelWidth, baseWheelHeight);
		 
		//Snowman 2 in background
		g.setColor(redWheel);
		g.fillOval((width/10)+ (width/27), squareY - (height/13)- 30, baseWheelWidth - (width/45), baseWheelHeight - (height/45)); //Bottom circle
		g.fillOval((width/10)+ (width/20), squareY - (height/7) - 30, baseWheelWidth - (width/25), baseWheelHeight - (height/25)); //Middle Circle
		g.fillOval( (width/10)+ (width/15), squareY - (height/6)- (height/35)- 30, baseWheelWidth - (width/15), baseWheelHeight - (height/15)); //Head
		g.setColor(blackEye);
		g.fillOval((width/10)+ (width/10), squareY - (height/6)- (height/50) - 30, baseWheelWidth - (width/9), baseWheelHeight - (height/9));//Right eye
		g.fillOval( (width/10)+ (width/13), squareY - (height/6)- (height/50)- 30, baseWheelWidth - (width/9), baseWheelHeight - (height/9));//Left Eye
		
		// Lever
		g.setColor(brownLever);
		Graphics2D lineThickness = (Graphics2D) g;
		lineThickness.setStroke(new BasicStroke(10));
		lineThickness.drawLine(xOffset - (width/8), squareY, xOffset + (width/3) + (width/8), wheelY + (height/8));
		
		//Lever End
		g.setColor(purpleEnd);
		g.fillOval(xOffset - (width/4), squareY - (height/11), baseWheelWidth + (width/15), baseWheelHeight + (height/15));
		
		
		
		//Snowman on Catapault
		g.setColor(snowBlue);
		g.fillOval(xOffset - (width/4)+ (width/27), squareY - (height/13), baseWheelWidth - (width/45), baseWheelHeight - (height/45)); //Bottom circle
		g.fillOval(xOffset - (width/4)+ (width/20), squareY - (height/7), baseWheelWidth - (width/25), baseWheelHeight - (height/25)); //Middle Circle
		lineThickness.setStroke(new BasicStroke(5));
		g.setColor(brownStick);
		lineThickness.drawLine(xOffset - (width/4) + (width/45), squareY - (height/10) - (height/90), xOffset - (width/4) + (width/6), squareY - (height/10) - (height/90)); //Stick arms
		g.setColor(snowBlue);
		lineThickness.drawLine(xOffset - (width/4) + (width/18), squareY - (height/10) - (height/90), xOffset - (width/4) + (width/8), squareY - (height/10) - (height/90)); //Make middle line blue while across snowman
		g.setColor(snowBlue);
		g.fillOval(xOffset - (width/4)+ (width/15), squareY - (height/6)- (height/35), baseWheelWidth - (width/15), baseWheelHeight - (height/15)); //Head
		g.setColor(blackEye);
		g.fillOval(xOffset - (width/4)+ (width/10), squareY - (height/6)- (height/50), baseWheelWidth - (width/9), baseWheelHeight - (height/9));//Right eye
		g.fillOval(xOffset - (width/4)+ (width/13), squareY - (height/6)- (height/50), baseWheelWidth - (width/9), baseWheelHeight - (height/9));//Left Eye
		
		
		
		//Text
		String text1 = new String("Make it Snow!");
		g.setFont(new Font("Arial", Font.BOLD, 32));
		FontMetrics metricSnow = g.getFontMetrics();
		int xFontSnow = (width - metricSnow.stringWidth(text1)) / 2;
		int yFontSnow = (height + metricSnow.getHeight()) / 8;
		g.setColor(snowBlue);
		g.drawString(text1, xFontSnow, yFontSnow);
		
		// Put your code above this line. This makes the drawing smoother.
		Toolkit.getDefaultToolkit().sync();
	}


	//==============================================================
	// You don't need to modify anything beyond this point.
	//==============================================================


	/**
	 * Starting point for this program. Your code will not go in the
	 * main method for this program. It will go in the paintComponent
	 * method above.
	 *
	 * DO NOT MODIFY this method!
	 *
	 * @param args unused
	 */
	public static void main (String[] args)
	{
		// DO NOT MODIFY THIS CODE.
		JFrame frame = new JFrame ("Traffic Animation");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new TrafficAnimation());
		frame.pack();
		frame.setVisible(true);
	}

	/**
	 * Constructor for the display panel initializes necessary variables.
	 * Only called once, when the program first begins. This method also
	 * sets up a Timer that will call paint() with frequency specified by
	 * the DELAY constant.
	 */
	public TrafficAnimation()
	{
		// Do not initialize larger than 800x600. I won't be able to
		// grade your project if you do.
		int initWidth = 600;
		int initHeight = 400;
		setPreferredSize(new Dimension(initWidth, initHeight));
		this.setDoubleBuffered(true);

		//Start the animation - DO NOT REMOVE
		startAnimation();
	}

	/**
	 * Create an animation thread that runs periodically.
	 * DO NOT MODIFY this method!
	 */
	private void startAnimation()
	{
		ActionListener timerListener = new TimerListener();
		Timer timer = new Timer(DELAY, timerListener);
		timer.start();
	}

	/**
	 * Repaints the graphics panel every time the timer fires.
	 * DO NOT MODIFY this class!
	 */
	private class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) {
			repaint();
		}
	}
}