import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;

import javax.swing.JPanel;
/**
 * Creates Top Panel for Reader of Books GUI, adds the library panel
 * and the reader panel to the main frame. Also adds the 
 * actionlistener for the BookButtons.
 * @author austenhale
 *
 */
public class ReaderOfBooksPanel extends JPanel {

	/* Instance Variables */
	private LibraryPanel libPan;
	private ReaderPanel readPan;
	private Library lib;
	private Book book;
	private int intiHeight;
	private int intiWidth;
	private int height;
	private int width;

	/* Constructor */
	public ReaderOfBooksPanel(){
		
		intiHeight = 1020;
		intiWidth = 720;
		
		
		this.setPreferredSize(new Dimension(intiHeight, intiWidth));	
		setLayout(new BorderLayout());
		
		
		//adds Library Panel
		libPan = new LibraryPanel(new BookButtonListener());
		libPan.createBookList();
		libPan.addImportPanel();
		libPan.addImportBooks();
		libPan.addPanels();
		libPan.addLibraryPanel();
		
		libPan.setBackground(Color.BLACK);
		add(libPan, BorderLayout.WEST);
		
		book = new Book("No title", "No author");
		book.setFilename("");
		//adds Reader Panel
		readPan = new ReaderPanel(book);
//		readPan.addInfoPanel();
//		readPan.addContentPanel();
//		readPan.addNavigationPanel();
//		readPan.addPanels();
//		readPan.addReaderPane();
		readPan.setBackground(Color.black);
		add(readPan, BorderLayout.CENTER);
		
	}

	
	private class BookButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			//System.out.println("clicked");
			BookButton pressed = (BookButton)e.getSource();
			
				pressed.setBackground(Color.GREEN);
			
			
			book = pressed.getBook();
			BufferedReader bfr = new BufferedReader(book.getReader());
			//book.setAuthor(pressed.getBook().getAuthor());
			System.out.println("Book: " + book);
			//make readerpanel take book as parameter, make reader panel
			//readPan = new ReaderPanel(book);
			readPan.setTextArea(book.getFilename(), bfr);
			//readPan.setTextArea(book.getText());
			readPan.setTitle(book.getTitle());
			readPan.setAuthor(book.getAuthor());
			readPan.setStart(0);
			
			//add(readPan, BorderLayout.CENTER);
			revalidate();
			
		}
		
	}
	public int GetWidth(int width) {
		this.width=width;
		width = getWidth();
		return width;
		
	}
	public int GetHeight(int height) {
		this.height = height;
		height = getHeight();
		return height;
	}
	
}
