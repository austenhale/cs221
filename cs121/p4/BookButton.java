import java.awt.Dimension;

import javax.swing.JButton;

/**
 * Book Button class, creates a book button and sets the text on the Button
 * @author austenhale
 *
 */
public class BookButton extends JButton{

	private Book book;
	
	public BookButton(Book book) {
		
	
		this.book = book;
		
		if (book.getTitle().length() > 20) {
			setText(book.getTitle().substring(0, 19) + "...");
		}
		else {
			setText(book.getTitle());
		}
		setToolTipText(book.getTitle().toString());
		//setPreferredSize(new Dimension(10, 5));

	}
	//add get book method
	public Book getBook() {
		return book;
	}
}
