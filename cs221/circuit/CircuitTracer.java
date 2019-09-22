import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Search for shortest paths between start and end points on a circuit board
 * as read from an input file using either a stack or queue as the underlying
 * search state storage structure and displaying output to the console or to
 * a GUI according to options specified via command-line arguments.
 * 
 * @author mvail, austenhale
 */
public class CircuitTracer extends JPanel{

	private Storage<TraceState> stateStore = null; //storage variable for all the states while searching for a best path
	private ArrayList<TraceState> bestPaths; //ArrayList that takes in all the best paths found from the search algorithm
	private JButton[][] boardGrid;
	private JButton[][] copyBoard;
	private JPanel solutionPanel;
	private JButton solutions;
	private static JFrame frame;
	/** launch the program
	 * @param args three required arguments:
	 *  first arg: -s for stack or -q for queue
	 *  second arg: -c for console output or -g for GUI output
	 *  third arg: input file name 
	 */
	public static void main(String[] args) {
		if (args.length != 3) {
			printUsage();
			System.exit(1);
		}
		try {
			if (args[1].equals("-c")) {
				new CircuitTracer(args); //create this with args
			}
			else if (args[1].equals("-g")){
				frame = new JFrame("Circuit Tracer");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.getContentPane().add(new CircuitTracer(args));
				frame.pack();
				frame.setVisible(true);

			}
			else {
				printUsage();
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/** Print instructions for running CircuitTracer from the command line. */
	private static void printUsage() {
		System.out.println("Invalid file format. Correct format is : java CircuitTracer [-s | -q]  [-c | -g] [filename]"); 
	}

	/** 
	 * Set up the CircuitBoard and all other components based on command
	 * line arguments.
	 * 
	 * @param args command line arguments passed through from main()
	 * @throws FileNotFoundException 
	 */
	private CircuitTracer(String[] args){


		if (args[0].equals("-s") || args[0].equals("-q")) {
			if (args[0].equals("-s")) {
				stateStore = new Storage<TraceState>(Storage.DataStructure.stack);
			}
			if (args[0].equals("-q")) {
				stateStore = new Storage<TraceState>(Storage.DataStructure.queue);
			}

		}
		else {
			printUsage();
			System.exit(0);
		}

		String inputFileName = args[2];

		bestPaths = searchPaths(inputFileName);
		if (args[1].equals("-c") || args[1].equals("-g")) {
			if (args[1].equals("-c")) { //console code, isn't it simpler

				for (int i = 0; i < bestPaths.size(); i++) {
					System.out.println(bestPaths.get(i).toString());
				}
				System.exit(0);
			}
			else if (args[1].equals("-g")) { //GUI code

				CircuitBoard guiBoard = null;
				try {
					guiBoard = new CircuitBoard(inputFileName);

				} catch (FileNotFoundException e) {

					e.printStackTrace();
				} catch (InvalidFileFormatException e) {

					e.printStackTrace();
				}
				/* Creates a gui board with the same characters as the read in file */
				JButton[][] boardGrid = new JButton[guiBoard.numRows()][guiBoard.numCols()];
				for (int row = 0; row < guiBoard.numRows(); row++) {
					for (int col = 0; col < guiBoard.numCols(); col++) {
						String text = Character.toString(guiBoard.charAt(row, col)); //converts the char at this point to a string
						boardGrid[row][col] = new JButton(text); //that string becomes the text on the button
						boardGrid[row][col].setPreferredSize(new Dimension(128, 128));
						boardGrid[row][col].setFont(new Font("Serif", Font.PLAIN, 48));
					}
				}

				this.setLayout(new BorderLayout());

				/* Creates a panel that is the same size as the board, because can only add panels, not array of jbuttons */
				JPanel circuitPanel = new JPanel();
				circuitPanel.setLayout(new GridLayout(guiBoard.numRows(), guiBoard.numCols()));
				for (int rowGUI = 0; rowGUI < guiBoard.numRows(); rowGUI++) {
					for (int col = 0; col < guiBoard.numCols(); col++) {
						circuitPanel.add(boardGrid[rowGUI][col]);
					}
				}
				this.add(circuitPanel, BorderLayout.CENTER);


				/*Solution area on right that allows user to select a certain solution */
				solutionPanel = new JPanel();
				solutionPanel.setLayout(new BoxLayout(solutionPanel, BoxLayout.Y_AXIS));
				JLabel solutionLabel = new JLabel("Solutions");
				solutionPanel.add(solutionLabel);
				for (int i = 0; i < bestPaths.size(); i++) {

					solutions = new JButton("Solution " + (i+1));
					solutions.addActionListener(new SolutionsButtonListener(boardGrid,i, guiBoard));
					solutionPanel.add(solutions);

				}

				this.add(solutionPanel, BorderLayout.EAST);




			} //end statement for gui based code



		}
		else { // if "-c" or "-g" were not found
			printUsage();
			System.exit(0);
		}

	}
	/**
	 * Method that runs the pseudocode from the search algorithm.
	 * @param inputFileName
	 * @return all the bests paths found as a result of the algorithm
	 */
	private ArrayList<TraceState> searchPaths(String inputFileName) {
		CircuitBoard board;
		try {
			board = new CircuitBoard(inputFileName);

			bestPaths = new ArrayList<TraceState>();
			TraceState path;
			int startingPointX = board.getStartingPoint().x;
			int startingPointY = board.getStartingPoint().y;
			/* checks each spot adjacent to the starting spot of the board */
			if (board.isOpen(startingPointX+1, startingPointY)) {//south
				path = new TraceState(board, startingPointX+1, startingPointY);
				stateStore.store(path);
			}
			if (board.isOpen(startingPointX-1, startingPointY)) {//north
				path = new TraceState(board, startingPointX-1, startingPointY);
				stateStore.store(path);
			}
			if (board.isOpen(startingPointX, startingPointY-1)) {//west
				path = new TraceState(board, startingPointX, startingPointY-1);
				stateStore.store(path);
			}
			if (board.isOpen(startingPointX, startingPointY+1)) {//east
				path = new TraceState(board, startingPointX, startingPointY+1);
				stateStore.store(path);
			}
			while (!stateStore.isEmpty()) {
				TraceState nextTrace = stateStore.retrieve();
				if (nextTrace.isComplete()) {

					if (bestPaths.isEmpty() || nextTrace.pathLength() == bestPaths.get(0).pathLength() ) {//bestPaths should all have the same length, thus just use first one
						bestPaths.add(nextTrace);
					}
					else if (nextTrace.pathLength() < bestPaths.get(0).pathLength()){
						bestPaths.clear();
						bestPaths.add(nextTrace);
					}
				}else { //if the path isn't a complete path
					/* Checks to see if adjacent spot is open, and if adjacent spot is not occupied */
					if (board.isOpen(nextTrace.getRow()+1, nextTrace.getCol()) ) {//south
						if (nextTrace.isOpen(nextTrace.getRow()+1, nextTrace.getCol())) {
							TraceState afterTrace = new TraceState(nextTrace, nextTrace.getRow()+1, nextTrace.getCol());
							stateStore.store(afterTrace);
						}
					}
					if (board.isOpen(nextTrace.getRow()-1, nextTrace.getCol()) ) {//north
						if (nextTrace.isOpen(nextTrace.getRow()-1, nextTrace.getCol())) {
							TraceState afterTrace  = new TraceState(nextTrace, nextTrace.getRow()-1, nextTrace.getCol());
							stateStore.store(afterTrace);
						}
					}
					if (board.isOpen(nextTrace.getRow(), nextTrace.getCol()-1) ) {//west
						if (nextTrace.isOpen(nextTrace.getRow(), nextTrace.getCol()-1)) {
							TraceState afterTrace  = new TraceState(nextTrace, nextTrace.getRow(), nextTrace.getCol()-1);
							stateStore.store(afterTrace);
						}
					}
					if (board.isOpen(nextTrace.getRow(), nextTrace.getCol()+1) ) {//east
						if (nextTrace.isOpen(nextTrace.getRow(), nextTrace.getCol()+1)) {
							TraceState afterTrace  = new TraceState(nextTrace, nextTrace.getRow(), nextTrace.getCol()+1);
							stateStore.store(afterTrace);
						}
					}

				}

			}//end while loop
		} catch (FileNotFoundException e) {
			System.out.println(e);
			System.exit(0);
		} catch (InvalidFileFormatException e) {

			System.exit(0);
		}
		return bestPaths;
	}//end searchPaths
	/**
	 * The Button Listener for the Solutions. Allows the buttons to change appropriately
	 * upon clicking the solution button
	 * @author austenhale
	 *
	 */
	private class SolutionsButtonListener implements ActionListener{

		private JButton[][] boardGrid;
		private int arrayNumber;
		private CircuitBoard copyBoard;

		/**
		 * Constructor to allow parameters passed in to be used when clicking on a solution
		 * @param boardGrid - passed in to allow changes to the buttons
		 * @param arrayNumber - used to know which path is to be used
		 * @param copyBoard - the original board, untouched. Used to briefly reset board back to original state
		 * 						before changing to next solution
		 */
		public SolutionsButtonListener(JButton[][] boardGrid, int arrayNumber, CircuitBoard copyBoard) {
			this.boardGrid = boardGrid;
			this.arrayNumber = arrayNumber;
			this.copyBoard = copyBoard;



		}
		/**
		 * This action performed method changes all the O's in a path to a T.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			//this listener should make all the 0's in the shortest path to a T
			frame.setTitle("Showing solution " + (arrayNumber+1));
			/* This code resets the current board to the original board */
			for (int row = 0; row < copyBoard.numRows(); row++) {
				for (int col = 0; col < copyBoard.numCols(); col++) {
					String text = Character.toString(copyBoard.charAt(row, col));
					boardGrid[row][col].setText(text);
					boardGrid[row][col].setForeground(new Color(0,0,0)); //black
				}
			}
			/* For every point in the certain best path, changes that point to have a T */
			for (Point p :bestPaths.get(arrayNumber).getPath()) {

				boardGrid[p.x][p.y].setText("T");
				boardGrid[p.x][p.y].setForeground(new Color(220,20,60)); //red

			}



		}



	}
} // class CircuitTracer