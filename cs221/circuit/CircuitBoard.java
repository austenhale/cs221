
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Represents a 2D circuit board as read from an input file.
 *  
 * @author mvail, austenhale
 */
public class CircuitBoard {
	/** current contents of the board */
	private char[][] board;
	/** location of row,col for '1' */
	private Point startingPoint;
	/** location of row,col for '2' */
	private Point endingPoint;

	//constants you may find useful
	private final int ROWS; //initialized in constructor
	private final int COLS; //initialized in constructor
	private final char OPEN = 'O'; //capital 'o'
	private final char CLOSED = 'X';
	private final char TRACE = 'T';
	private final char START = '1';
	private final char END = '2';
	private final String ALLOWED_CHARS = "OXT12";
	private boolean oneStart = false;
	private boolean oneEnd = false;

	/** Construct a CircuitBoard from a given board input file, where the first
	 * line contains the number of rows and columns as ints and each subsequent
	 * line is one row of characters representing the contents of that position.
	 * Valid characters are as follows:
	 *  'O' an open position
	 *  'X' an occupied, unavailable position
	 *  '1' first of two components needing to be connected
	 *  '2' second of two components needing to be connected
	 *  'T' is not expected in input files - represents part of the trace
	 *   connecting components 1 and 2 in the solution
	 * 
	 * @param filename
	 * 		file containing a grid of characters
	 * @throws FileNotFoundException if Scanner cannot read the file
	 * @throws InvalidFileFormatException for any other format or content issue that prevents reading a valid input file
	 */
	@SuppressWarnings("resource")
	public CircuitBoard(String filename) throws FileNotFoundException{

		
		
		Scanner fileScan = new Scanner(new File(filename));
		
		try {
			String rowAndCol = fileScan.nextLine();
			String [] rowCol = rowAndCol.trim().split("\\s+");
			if (rowCol.length != 2) { //checks to see if input such as 4 4 5 was inputed for row and column size (too many inputs)
				System.out.println("Too many inputs on row/column length line.");
				throw new InvalidFileFormatException(filename);
			}
			ROWS = Integer.parseInt(rowCol[0]); //amount of rows the board should have
			COLS = Integer.parseInt(rowCol[1]); //amount of columns the board should have
		}catch (NumberFormatException e) {
			System.out.println(e.toString() + " is an invalid character.");
			throw new InvalidFileFormatException(filename);
		}
		board = new char[ROWS][COLS];
		int rowIncrementer = 0; //int counter to keep track of current row
		while (fileScan.hasNextLine()) { //Continues while there is another line on the file's board
			try {
				String numberLine = fileScan.nextLine();
				String[] characters = numberLine.trim().split("\\s+");
				if (characters.length != COLS) {
					System.out.println("Row length does not match specificed row length.");
					throw new InvalidFileFormatException(filename);
				}

				if (characters != null && characters.length > 1) { //checks to see if there is actually anything in the next line
					for (int numberSpot = 0; numberSpot < characters.length; numberSpot++) {
						board[rowIncrementer][numberSpot] = characters[numberSpot].charAt(0); //assigns the character from the file to the generated board
						switch(characters[numberSpot].charAt(0)) { //switch statement for assigning starting and ending points
						/* 0 X and T don't need points assigned; can be more than one of those in the board*/
						case 'O':
							break;
						case 'X':
							break;
						case 'T':
							break;
						case '1':

							if (oneStart == true) { //only one '1' is allowed, if this is reached a 2nd time throws an exception
								System.out.println("Duplicate '1' found in file; only one '1' allowed.");
								throw new InvalidFileFormatException(filename);
							}

							startingPoint = new Point(rowIncrementer, numberSpot);
							oneStart = true;
							break;
						case '2':

							if (oneEnd == true) { //only one '2' is allowed, if this is reached a 2nd time throws an exception
								System.out.println("Duplicate '2' found in file; only one '2' allowed.");
								throw new InvalidFileFormatException(filename);
							}
							endingPoint = new Point(rowIncrementer, numberSpot);
							oneEnd = true;
							break;
						default: //if a character not in ALLOWED_CHARS is found
							System.out.println("Inva0lid character found.");
							throw new InvalidFileFormatException(filename);
						}
					}
					rowIncrementer++;
				}

				if (board.length != ROWS) { //if statement checking to see that the actual row length matches the specified row length
					System.out.println("Actual row length does not match specificed row length.");
					throw new InvalidFileFormatException(filename);
				}
				for (int i = 0; i<ROWS; i++) {
					if (board[i].length != COLS) { //checks every column for actual length
						System.out.println("Actual column length does not match specificed column length.");
						throw new InvalidFileFormatException(filename);
					}
				}
			}
			catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("Actual board size does not match specificed size.");
				throw new InvalidFileFormatException(filename);
			}
			catch (NumberFormatException e) {
				System.out.println(e + " is an invalid character.");
				throw new InvalidFileFormatException(filename);
			}
			catch (NoSuchElementException e) {
				System.out.println(e);
				throw new InvalidFileFormatException(filename);
			}
		}
		if (oneEnd == false || oneStart == false) { //if a '1' or '2' was never found
			if (oneEnd == false) {
				System.out.println("No '2' found; the file is invalid.");
			}
			if (oneStart == false) {
				System.out.println("No '1' found; the file is invalid.");
			}
			throw new InvalidFileFormatException(filename);
		}

		fileScan.close();

	}

	/** Copy constructor - duplicates original board
	 * 
	 * @param original board to copy
	 */
	public CircuitBoard(CircuitBoard original) {
		board = original.getBoard();
		startingPoint = new Point(original.startingPoint);
		endingPoint = new Point(original.endingPoint);
		ROWS = original.numRows();
		COLS = original.numCols();
	}

	/** utility method for copy constructor
	 * @return copy of board array */
	private char[][] getBoard() {
		char[][] copy = new char[board.length][board[0].length];
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				copy[row][col] = board[row][col];
			}
		}
		return copy;
	}

	/** Return the char at board position x,y
	 * @param row row coordinate
	 * @param col col coordinate
	 * @return char at row, col
	 */
	public char charAt(int row, int col) {
		return board[row][col];
	}

	/** Return whether given board position is open
	 * @param row
	 * @param col
	 * @return true if position at (row, col) is open 
	 */
	public boolean isOpen(int row, int col) {
		if (row < 0 || row >= board.length || col < 0 || col >= board[row].length) {
			return false;
		}
		return board[row][col] == OPEN;
	}

	/** Set given position to be a 'T'
	 * @param row
	 * @param col
	 * @throws OccupiedPositionException if given position is not open
	 */
	public void makeTrace(int row, int col) {
		if (isOpen(row, col)) {
			board[row][col] = TRACE;
		} else {
			throw new OccupiedPositionException("row " + row + ", col " + col + "contains '" + board[row][col] + "'");
		}
	}

	/** @return starting Point(row,col) */
	public Point getStartingPoint() {
		return new Point(startingPoint);
	}

	/** @return ending Point(row,col) */
	public Point getEndingPoint() {
		return new Point(endingPoint);
	}

	/** @return number of rows in this CircuitBoard */
	public int numRows() {
		return ROWS;
	}

	/** @return number of columns in this CircuitBoard */
	public int numCols() {
		return COLS;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				str.append(board[row][col] + " ");
			}
			str.append("\n");
		}
		return str.toString();
	}

}// class CircuitBoard
