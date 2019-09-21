import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Scrollbar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.StyledDocument;
/**
 * Creates panel for the reader. The panel contains a panel for the information of the book,
 * content of the book, and navigation tools. This class also had action listeners
 * and an adjustment listener for the navigation buttons and for the scrollbar for 
 * the content area.
 * @author austenhale
 *
 */
public class ReaderPanel extends JPanel{

	/* Instance Variables */
	private JPanel readerPane;
	private JPanel infoPanel;
	private JPanel contentPanel;
	private JPanel navgPanel;
	private JLabel titleLabel;
	private JLabel authorLabel;
	private JLabel pageLabel;
	private JButton upButton;
	private JButton downButton;
	private JScrollPane contentScrollPane;
	private JTextArea contentArea;
	private Book book;
	private int pageValue;
	private int pageMax;
	private JScrollBar scrollBar;
	private String title;
	private String author;
	private int num;

	private Font font = new Font("Courier", Font.ITALIC | Font.BOLD, 15);
	
	public ReaderPanel(Book book) {

		this.book = book;
		

		this.setLayout(new BorderLayout());

		// mother panel, reader
		readerPane = new JPanel();
		readerPane.setPreferredSize(new Dimension(700, 700));
		readerPane.setLayout(new BoxLayout(readerPane, BoxLayout.PAGE_AXIS));
//		readerPane.setLayout(new BorderLayout());
		TitledBorder libBorder = new TitledBorder("Reader");
		libBorder.setTitleJustification(TitledBorder.LEFT);
		libBorder.setTitlePosition(TitledBorder.TOP);
		libBorder.setTitleFont(font);
		readerPane.setBorder(libBorder);

		



		// information panel
		infoPanel = new JPanel();
		infoPanel.setPreferredSize(new Dimension(300, 43));
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
		TitledBorder infoBorder = new TitledBorder("Information");
		infoBorder.setTitleJustification(TitledBorder.LEFT);
		infoBorder.setTitlePosition(TitledBorder.TOP);
		infoBorder.setTitleFont(font);
		infoPanel.setBorder(infoBorder);
		titleLabel = new JLabel("Title: "   + book.getTitle());
		authorLabel = new JLabel("Author: " + book.getAuthor());
		pageLabel = new JLabel("Page: " + pageValue + "/" + pageMax);
		infoPanel.add(titleLabel);
		infoPanel.add(Box.createRigidArea(new Dimension(80, 80)));
		infoPanel.add(authorLabel);
		infoPanel.add(Box.createHorizontalGlue());
		infoPanel.add(pageLabel);



		// content panel
		contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		TitledBorder contBorder = new TitledBorder("Content");
		contBorder.setTitleJustification(TitledBorder.LEFT);
		contBorder.setTitlePosition(TitledBorder.TOP);
		contBorder.setTitleFont(font);
		contentPanel.setBorder(contBorder);
		contentArea = new JTextArea();
		contentArea.setEditable(false);
		contentArea.setText("No book selected yet.");
		
		
//		contentArea.setSelectionStart(0);
//		contentArea.setSelectionEnd(0);
		contentScrollPane = new JScrollPane(contentArea);
		contentScrollPane.setPreferredSize(new Dimension(650, 500));
//		contentScrollPane.setPreferredSize(new Dimension(100, 50));
		contentScrollPane.getVerticalScrollBar().addAdjustmentListener(new ScrollAdjustmentListener());
		contentScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		contentScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		contentScrollPane.getVerticalScrollBar().setValue(0);

		contentPanel.add(contentScrollPane);



		// navigation panel
		navgPanel = new JPanel();
		//navgPanel.setPreferredSize(new Dimension( 300, 20));
		TitledBorder navgBorder = new TitledBorder("Navigation");
		navgBorder.setTitleJustification(TitledBorder.LEFT);
		navgBorder.setTitlePosition(TitledBorder.TOP);
		navgBorder.setTitleFont(font);
		navgPanel.setBorder(navgBorder);
		upButton = new JButton("Page Up");
		upButton.addActionListener(new upButtonListener());
		downButton = new JButton("Page Down");
		downButton.addActionListener(new downButtonListener());
		navgPanel.add(upButton);
		navgPanel.add(downButton);




		//adds other panels
		readerPane.add(infoPanel, BorderLayout.NORTH);
		readerPane.add(contentPanel, BorderLayout.CENTER);
		readerPane.add(navgPanel, BorderLayout.SOUTH);



		// adds mother panel
		add(readerPane);
	}

	private class upButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			int blockIncrement = contentScrollPane.getVerticalScrollBar().getBlockIncrement(-1);
			System.out.println("Block increment: " + blockIncrement);

			int scrollSpot = contentScrollPane.getVerticalScrollBar().getValue() - blockIncrement;
			contentScrollPane.getVerticalScrollBar().setValue(scrollSpot);
			
			
		}

	}

	private class downButtonListener implements ActionListener {

		
		@Override
		public void actionPerformed(ActionEvent e) {
			int blockIncrement = contentScrollPane.getVerticalScrollBar().getBlockIncrement(1);
			System.out.println("Block increment: " + blockIncrement);

			int scrollSpot = contentScrollPane.getVerticalScrollBar().getValue() + blockIncrement;
			contentScrollPane.getVerticalScrollBar().setValue(scrollSpot);
//			blockIncrement = blockIncrement + blockIncrement;
//			System.out.println("Block increment: " + blockIncrement);
		}

	
	}
	private class ScrollAdjustmentListener implements AdjustmentListener{

		@Override
		public void adjustmentValueChanged(AdjustmentEvent arg0) {

			//credit JeroenWamerdam of StackOverflow
			int extent = contentScrollPane.getVerticalScrollBar()
					.getModel().getExtent();
			
			if (contentScrollPane.getVerticalScrollBar().getValue()+extent
					== contentScrollPane.getVerticalScrollBar().getMaximum()) {
				
				upButton.setEnabled(true);
				downButton.setEnabled(false);
				//System.out.println("Up should be enabled, down disabled");
				//System.out.println("Value: " + contentPane.getVerticalScrollBar().getValue());
				
			}
			if (contentScrollPane.getVerticalScrollBar().getValue() == 
					contentScrollPane.getVerticalScrollBar().getMinimum()) {
			
				upButton.setEnabled(false);
				downButton.setEnabled(true);
				//System.out.println("Down should be enabled, up disabled");
//				System.out.println("Value: " + contentPane.getVerticalScrollBar().getValue());
//				System.out.println("Minimum " + contentPane.getVerticalScrollBar().getMinimum());
//				System.out.println("Maximum " + contentPane.getVerticalScrollBar().getMaximum());
			}
			else if ((contentScrollPane.getVerticalScrollBar().getValue()+extent != contentScrollPane.getVerticalScrollBar().getMaximum()) && 
					contentScrollPane.getVerticalScrollBar().getValue() != contentScrollPane.getVerticalScrollBar().getMinimum()){
				upButton.setEnabled(true);
				downButton.setEnabled(true);
				//System.out.println("Entering else statement.");
				//System.out.println("Value: " + contentPane.getVerticalScrollBar().getValue());
			}
			int blockIncrement = contentScrollPane.getVerticalScrollBar().getBlockIncrement(1);

			pageValue = contentScrollPane.getVerticalScrollBar().getValue()/blockIncrement +1;
			pageMax = (contentScrollPane.getVerticalScrollBar().getMaximum() )/blockIncrement;
			System.out.println("Page Value: " + pageValue);
			System.out.println("Page max: " + pageMax);
			pageLabel.setText("Page: " + pageValue + "/" + pageMax);
		} 
		
	}
	public void setTextArea(String text, BufferedReader bfr) {
				//contentArea.setText(text);
			try {
				contentArea.read( bfr, text);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public void setTitle(String text) {
		title = text;
		titleLabel.setText("Title:  " + title);
	}
	public void setAuthor(String text) {
		author = text;
		authorLabel.setText("Author: " + author);
	}
	public void setStart(int num) {
		contentArea.setSelectionStart(num);
		contentArea.setSelectionEnd(num);
		contentScrollPane.getVerticalScrollBar().setValue(num);
	}
	
}






