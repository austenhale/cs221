import java.awt.Point;
import java.util.Arrays;

import javax.swing.JButton;
/**
 * Implements the methods used in the TicTacToeTester and TicTacToeGUI
 * @author austenhale
 *
 */
public class TicTacToeGame implements TicTacToe{

	/* Instance Variables */
	private Player[][] gameGrid;
	private Player[][] newGameGrid;
	private Point[] moves;
	private Point[] newMoves;
	private Winner winner;
	private static final int MAXMOVES = 9;
	private Point point;
	private int moveCount;
	
	
	
	public TicTacToeGame() {
		newGame();
	}
	/**
	 * Reset the game. It is player X's turn.
	 */
	@Override
	public void newGame() {
		gameGrid = new Player[3][3];
		for (int i = 0; i < 3; i ++) {
			for (int j=0; j< 3; j++) {
				gameGrid[i][j] = Player.OPEN; 
				
			}
			
		}
		moveCount = 0;
		moves = new Point[MAXMOVES];
		winner = Winner.IN_PROGRESS;
		
	}
	/**
	 * Return true if either player X or O has achieved
	 * 3-in-a-row, whether vertically, horizontally, or diagonally,
	 * or if all positions have been claimed without a winner.
	 * 
	 * @return true if the game is over, else false
	 */
	@Override
	public boolean gameOver() {
	
			
			/* PLAYER X CHECKS */
			if (gameGrid[0][0] == Player.X) {//checks first column for horizonital match
				if (gameGrid[1][0] == Player.X) {
					if (gameGrid[2][0] == Player.X) {
						winner = Winner.X;
						return true;
					}
				}
			}//if statement for checking first column matches
			if (gameGrid[0][1] == Player.X) {//checks second column for matches
				if (gameGrid[1][1] == Player.X) {
					if (gameGrid[2][1] == Player.X) {
						winner = Winner.X;
						return true;
					}
				}
			}//if statement checking for second column matches
			if (gameGrid[0][2] == Player.X) {
				if (gameGrid[1][2] == Player.X) {
					if (gameGrid[2][2] == Player.X) {
						winner = Winner.X;
						return true;
					}
				}
			}//if statement checking for 3rd column matches
			if (gameGrid[0][0] == Player.X) {//checks first row for matches
				if (gameGrid[0][1] == Player.X) {
					if (gameGrid[0][2] == Player.X) {
						winner = Winner.X;
						return true;
					}
				}
			}//if statement checking for first row matches
			if (gameGrid[1][0] == Player.X) {//checks 2nd row for matches
				if (gameGrid[1][1] == Player.X) {
					if (gameGrid[1][2] == Player.X) {
						winner = Winner.X;
						return true;
					}
				}
			}//if statement checking for 2nd row matches
			if (gameGrid[2][0] == Player.X) {//checks 3rd row for matches
				if (gameGrid[2][1] == Player.X) {
					if (gameGrid[2][2] == Player.X) {
						winner = Winner.X;
						return true;
					}
				}
			}// if statement checking for 3rd row matches
			if (gameGrid[0][0] == Player.X) {//checks top left-bottom right diagonal
				if (gameGrid[1][1] == Player.X) {
					if (gameGrid[2][2] == Player.X) {
						winner = Winner.X;
						return true;
					}
				}
			}// if statement checking for top left-bottom right diagonal match
			if (gameGrid[0][2] == Player.X) {//checks top right-bottom left diagonal
				if (gameGrid[1][1] == Player.X) {
					if (gameGrid[2][0] == Player.X) {
						winner = Winner.X;
						return true;
					}
				}
			}//if statement checking for top right-bottom left diagonal match
			
			/* PLAYER O CHECKS */
			if (gameGrid[0][0] == Player.O) {//checks first column for horizonital match
				if (gameGrid[1][0] == Player.O) {
					if (gameGrid[2][0] == Player.O) {
						winner = Winner.O;
						return true;
					}
				}
			}//if statement for checking first column matches
			if (gameGrid[0][1] == Player.O) {//checks second column for matches
				if (gameGrid[1][1] == Player.O) {
					if (gameGrid[2][1] == Player.O) {
						winner = Winner.O;
						return true;
					}
				}
			}//if statement checking for second column matches
			if (gameGrid[0][2] == Player.O) {
				if (gameGrid[1][2] == Player.O) {
					if (gameGrid[2][2] == Player.O) {
						winner = Winner.O;
						return true;
					}
				}
			}//if statement checking for 3rd column matches
			if (gameGrid[0][0] == Player.O) {//checks first row for matches
				if (gameGrid[0][1] == Player.O) {
					if (gameGrid[0][2] == Player.O) {
						winner = Winner.O;
						return true;
					}
				}
			}//if statement checking for first row matches
			if (gameGrid[1][0] == Player.O) {//checks 2nd row for matches
				if (gameGrid[1][1] == Player.O) {
					if (gameGrid[1][2] == Player.O) {
						winner = Winner.O;
						return true;
					}
				}
			}//if statement checking for 2nd row matches
			if (gameGrid[2][0] == Player.O) {//checks 3rd row for matches
				if (gameGrid[2][1] == Player.O) {
					if (gameGrid[2][2] == Player.O) {
						winner = Winner.O;
						return true;
					}
				}
			}// if statement checking for 3rd row matches
			if (gameGrid[0][0] == Player.O) {//checks top left-bottom right diagonal
				if (gameGrid[1][1] == Player.O) {
					if (gameGrid[2][2] == Player.O) {
						winner = Winner.O;
						return true;
					}
				}
			}// if statement checking for top left-bottom right diagonal match
			if (gameGrid[0][2] == Player.O) {//checks top right-bottom left diagonal
				if (gameGrid[1][1] == Player.O) {
					if (gameGrid[2][0] == Player.O) {
						winner = Winner.O;
						return true;
					}
				}
			}//if statement checking for top right-bottom left diagonal match
			
			if (gameGrid[0][0] != Player.OPEN && gameGrid[0][1] != Player.OPEN && gameGrid[0][2] != Player.OPEN //check first row for being filled in
					&& gameGrid[1][0] != Player.OPEN && gameGrid[1][1] != Player.OPEN && gameGrid[1][2] != Player.OPEN //check 2nd for being filled
					&& gameGrid[2][0] != Player.OPEN && gameGrid[2][1] != Player.OPEN && gameGrid[2][2] != Player.OPEN) {//check 3rd for being filled
				winner = Winner.TIE;
				return true;	
		}// end if statement for full board
		return false;
	}

	/**
	 * Return the Winner (X, O, or TIE) if the game is over, or
	 * IN_PROGRESS if the game is not over.
	 * 
	 * @return the winner of a completed game or IN_PROGRESS
	 */
	@Override
	public Winner winner() {

		if (gameOver() == true) {
			return winner;
		}
		else {
			return Winner.IN_PROGRESS;

		}
		
	}
	/**
	 * Get the current game board with each position marked as
	 * belonging to X, O, or OPEN.
	 * Preserve encapsulation by returning a copy of the original data.
	 * 
	 * @return array showing the current game board
	 */
	
	@Override
	public Player[][] getGameGrid() {
		newGameGrid = new Player[gameGrid.length][];
		for (int i =0; i<gameGrid.length; i++) {
			newGameGrid[i] = gameGrid[i].clone();
		}
		//Source: http://www.java2s.com/Code/Java/Collections-Data-Structure/clonetwodimensionalarray.htm
		//System.out.println("New Game grid after copying: " + Arrays.toString(newGameGrid));
		
		return newGameGrid;
	}

	/**
	 * Get the sequence of moves, where even indexes correspond to the
	 * first player's moves and odd indexes correspond to the second
	 * player's moves.
	 * NOTE: Move rows are stored in the first coordinate, x, and move
	 * columns are stored in the second coordinate, y.
	 * Preserve encapsulation by returning a copy of the original data.
	 * 
	 * @return array showing the sequence of claimed positions
	 */
	@Override
	public Point[] getMoves() {
		newMoves = new Point[moveCount];
		//System.out.println("Move count: " + moveCount);
		//System.out.println("New Moves array before copying: " + Arrays.toString(newMoves));
		//credit to arshajii of stackoverflow
		//https://stackoverflow.com/questions/10423134/java-copy-section-of-array
		newMoves = Arrays.copyOfRange(moves, 0, moveCount);
		//System.out.println("New Moves array after copying: " + Arrays.toString(newMoves));
		return newMoves;
	}

	/**
	 * If the move is invalid for any reason, return false.
	 * Cannot claim any position when the game is over or the
	 * position has already been claimed.
	 * If the chosen row, column position is not already claimed
	 * and the game is not already over, claim it for the player.
	 * 
	 * @param player either Player.X or Player.O
	 * @param row value from 0 to 2
	 * @param col value from 0 to 2
	 * @return true if the choice was a valid move, else false
	 */
	@Override
	public boolean choose(Player player, int row, int col) {

		if ((player != player.X && player != player.O) //player somehow not X or O
				|| row > 2 || col > 2  //row or column somehow greater than 2
				|| row < 0 || col < 0 || //row or column somehow less than 0
				gameOver() == true || gameGrid[row][col] != Player.OPEN ) {//if game is over, or slot is taken
			//System.out.println("Return false.");
			return false;
		}
		else {
			//System.out.println("Return true.");
			
			point = new Point(row, col);
			gameGrid[row][col] = player;
			moves[moveCount] = point;
			moveCount++;
			
			}
			return true;
		}
		
	}


