import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;



/**
 * Creates a panel for the library. Contains panels that holds the list of books
 * and a panel that holds the textfield to specify the filepath for books, and an
 * import books button.
 * 
 * @author austenhale
 *
 */




public class LibraryPanel extends JPanel{

	/* Instance Variables */

	private Library lib;
	private JTextField bookInput;
	private JButton loadBooks;
	private JPanel bookList;
	private JPanel libList;
	private JPanel importPanel;
	private JScrollPane bookScroll;
	private JButton testButton;
	private JPanel bookButtons;
	private BookButton bookButton;
	private ReaderOfBooksPanel bPanel;
	private ActionListener action;
	private JButton removeBooks;
	private Font font = new Font("Courier", Font.ITALIC | Font.BOLD, 15);
	/* Constructor */
	public LibraryPanel(ActionListener action) {

		this.action = action;
		this.setLayout(new BorderLayout());
		

		//Overall panel, holds all others
		libList = new JPanel();
		libList.setPreferredSize(new Dimension(300, 700));
		libList.setLayout(new BoxLayout(libList, BoxLayout.PAGE_AXIS));
		TitledBorder libBorder = new TitledBorder("Library");
		libBorder.setTitleJustification(TitledBorder.LEFT);
		libBorder.setTitlePosition(TitledBorder.TOP);
		libBorder.setTitleFont(font);
		libList.setBorder(libBorder);
		lib = new Library();

	}
	public void createBookList() {
		bookList = new JPanel();
		//bookList.setLayout(new BoxLayout(bookList, BoxLayout.Y_AXIS));
		//		for (int i = 0; i< bookTestTotal; i++) {
		//			testButton = new JButton("test " + i);
		//			
		//			testButton.setAlignmentX(CENTER_ALIGNMENT);
		//			testButton.setPreferredSize(new Dimension(200, 20));
		//			bookList.add(testButton);
		//			//bookList.add(Box.createRigidArea(new Dimension(0,5)));
		//		}

		TitledBorder bookBorder = new TitledBorder("Books");
		bookBorder.setTitleJustification(TitledBorder.LEFT);
		bookBorder.setTitlePosition(TitledBorder.TOP);
		bookBorder.setTitleFont(font);
		bookList.setBorder(bookBorder);
		bookList.setLayout(new BoxLayout(bookList, BoxLayout.Y_AXIS));
//		GridLayout bookLayout = new GridLayout(0, 1);
//		bookList.setLayout(bookLayout);
		
		//bookList.setPreferredSize(new Dimension(150, 550));
		//bookList.setPreferredSize(new Dimension(50, 55));
		bookScroll = new JScrollPane(bookList);
		
		bookScroll.setPreferredSize(new Dimension(150, 550));
		bookScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		bookScroll.getVerticalScrollBar().setUnitIncrement(15);
		//add(bookScroll, BorderLayout.CENTER);
	}

	public void addImportPanel() {
		//import books panel
		importPanel = new JPanel();
		//importPanel.setPreferredSize(new Dimension(10, 20));
		
		TitledBorder impBorder = new TitledBorder("Import Books");
		impBorder.setTitleJustification(TitledBorder.LEFT);
		impBorder.setTitlePosition(TitledBorder.TOP);
		impBorder.setTitleFont(font);
		importPanel.setBorder(impBorder);
		bookInput = new JTextField("etext/booklist-full.csv");
		loadBooks = new JButton("Load Books");
		removeBooks = new JButton("Remove ALL Books");
		removeBooks.setBackground(Color.RED);
		removeBooks.setForeground(Color.WHITE);
		removeBooks.setToolTipText("Removes all books from the book list, "
				+ "and the library. Does not clear currently selected book information.");
		
		importPanel.add(bookInput);
		importPanel.add(loadBooks);
		importPanel.add(removeBooks);

	}




	


	public void addImportBooks() {
		//import books into library
		
		lib = new Library();
		//lib.loadLibraryFromCSV("etext/booklist.csv");

		bookInput.addActionListener(new BookTextFieldListener());
		loadBooks.addActionListener(new BookTextFieldListener());
		removeBooks.addActionListener(new RemoveBooksListener());
		
		
	}
	public void addPanels() {
		//adds children panels
		libList.add(bookScroll, BorderLayout.CENTER);
		libList.add(importPanel, BorderLayout.SOUTH);
	}


	public void addLibraryPanel() {
		//adds mother panel
		add(libList);
	}

	


	private class BookTextFieldListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//System.out.println("Clicked!");
			
			bookList.removeAll();
			lib.removeBooks();
			
			revalidate();
			lib.loadLibraryFromCSV(bookInput.getText());
			//System.out.println("Library: " + lib);
			for (int i = 0; i< lib.getBooks().size(); i++) {

				Book book = lib.getBook(i);
			
			
				bookButton = new BookButton(book);
				bookButton.addActionListener(action);
				bookButton.setAlignmentX(CENTER_ALIGNMENT);
				//bookButton.setPreferredSize(new Dimension (bookList.getWidth(), bookList.getHeight()));
				//bookButton.getPreferredSize();
				//bookButton.setSize(200, 100);
				//bookButton.setBackground(Color.BLUE);
				bookButton.setPreferredSize(new Dimension(210, 60));
				bookButton.setMinimumSize(new Dimension(210, 60));
				bookButton.setMaximumSize(new Dimension(210, 60));
				bookList.add(bookButton);
				bookList.add(Box.createRigidArea(new Dimension(0,7)));
				
				revalidate();
				
			}
			System.out.println("Library: " + lib);
			
			
		}
		
	}
	private class RemoveBooksListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			bookList.removeAll();
			lib.removeBooks();
			revalidate();
			System.out.println("New library: " + lib);			
		}
		
	}



}






