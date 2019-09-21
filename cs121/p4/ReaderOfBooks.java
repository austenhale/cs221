import java.awt.Dimension;

import javax.swing.JFrame;
/**
 * Driver class for Reader of Books
 * @author austenhale
 *
 */
public class ReaderOfBooks {
	
	
	public static void main(String args[]) {
		
		
		JFrame frame = new JFrame("READER 3000 GET YOUR READ ON");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ReaderOfBooksPanel panel = new ReaderOfBooksPanel();
		frame.getContentPane().add(panel);
		
		
		frame.pack();
		frame.setVisible(true);
	}

}
