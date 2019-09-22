import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.IntStream;
/**
 * The main code for the magic square class. Allows the user to have two main options: check a given file to see if it's a magic square
 * or create their own magic square given an odd number. If a user checks a file, the program will read in the file, then add
 * the sums of each row, column, and diagonal, individually, to see if it matches the magic sum. If all conditions are met, the check 
 * method will return true. The create method will take the given odd number, create a magic square using that number, and write
 * it to a file.
 * 
 * @author austenhale
 *
 */
public class MagicSquare {

	private static int[][] matrix;
	private static  int matrixSize;
	private static String matrixFile;
	private int matrixSized;
	/**
	 * 
	 * @param squareSize
	 * @param fileName
	 */
	/* Constructor */
	public MagicSquare(int squareSize, String fileName) {
		matrixSize = squareSize;
		//createMagicSquare(fileName);
	}
	/**
	 * Checks a matrix to see if it is a magic square using the given filename. Returns true if the file is a magic square, else false.
	 * @param fileName
	 * @return true or false, depending on if the file is a magic square or not.
	 */
	public static boolean checkMatrix(String fileName) {
		matrixFile = fileName;

		readMatrix(matrixFile);

		//		for (int[] print : matrix) {
		//			for (int prints : print) {
		//				System.out.print(prints + " ");
		//			}
		//			System.out.println();
		//		}
		//System.out.println("test number two");
		//System.out.println("Checking matrix after reading file.");
		int magicNumber = matrixSize * ((matrixSize*matrixSize) + 1)/2;
		int checkRow = 0;
		int checkCol = 0;
		int rowSize = 0;
		int colSize = 0;
		int checkDiagLeft = 0;
		int checkDiagRight = 0;
		int squareNumber = 1;
		boolean matrixCondition = false;
		int squareHighNumber = (int) Math.pow(matrixSize, 2);
		ArrayList<Integer> usedNumbers = new ArrayList<Integer>();
		//while (squareHighNumber+1 > squareNumber) {
		for (int i =0; i< matrixSize; i++) {// do numbers occur?
			for (int j=0; j<matrixSize; j++) {

				squareNumber = matrix[i][j];

				if (squareNumber > 0 && squareNumber < squareHighNumber +1) {

					//	squareNumber = matrix[i][j];

					//						System.out.println("Square number in if statement: " + squareNumber);

					matrixCondition = true;
					if (usedNumbers.contains(squareNumber)) {
						System.out.println("Duplicate number found." + squareNumber);
						return false;
					}else {
						usedNumbers.add(squareNumber);
					}


				}


				else {
					//						System.out.println("Magic number: " + squareHighNumber);
					//						System.out.println("Square number: " + squareNumber);
					return false;
				}

			}

			//}
		}
		//System.out.println("Entering while loop to add rows.");
		if (matrixCondition == true) {
			while (rowSize < matrixSize) {
				for (int i=0; i<matrixSize; i++) {//adding totals of rows
					checkRow += matrix[rowSize][i];
					//System.out.println("Row numbers : " + matrix[i][rowSize]);

				}

				rowSize++;
				//			System.out.println("Increaseing row size to " + rowSize);
				//			System.out.println("Row sum: " + checkRow);
				if (checkRow == magicNumber) {
					matrixCondition = true;
				}
				else {
					//System.out.println("File is not a magic square. ROW");
					return false;
				}
				checkRow = 0;
			}//while loop to check col numbers

			while (colSize < matrixSize) {
				for (int j=0; j<matrixSize; j++) {//adding totals of columns
					checkCol += matrix[j][colSize];
					//	System.out.println("Column numbers: " + matrix[colSize][j]);
				}
				colSize++;
				if (checkCol == magicNumber) {
					matrixCondition = true;
				}
				else {
					//System.out.println("File is not a magic square. COLUMN");
					return false;
				}
				checkCol = 0;
			}//while loop to check columns numbers

			for (int i = 0; i< matrixSize; i++) {
				checkDiagLeft += matrix[i][i];
			}
			if (checkDiagLeft == magicNumber) {
				//			System.out.println("Diagonal number: " + checkDiagLeft);
				//			System.out.println("Magic Number: " + magicNumber);
				matrixCondition = true;
			}
			else {
				//System.out.println("File is not a magic square. Diag.");
				return false;
			}
			int diagRight = 0;
			for (int i = matrixSize-1; i >= 0; i--) {
				checkDiagRight += matrix[i][diagRight];
				//System.out.println("Matrix value for diagright: " + matrix[i][diagRight]);
				diagRight++;
			}
			//		System.out.println("diagonal number right: " + checkDiagRight);
			//		System.out.println("Magic number: " + magicNumber);

			if (checkDiagRight == magicNumber) {
				//			System.out.println("diagonal number right: " + checkDiagRight);
				//			System.out.println("Magic number: " + magicNumber);
				matrixCondition = true;
			}
			else {
				//System.out.println("File is not a magic square. Diag right.");
				return false;
			}
		}
		//System.out.println("File is not a magic square.");
		return matrixCondition;

	}
	/**
	 * Creates a magic square using a given number. The number must  be odd. Calls on the write method to make that magic square into a file.
	 * @param fileName
	 */
	public static void createMagicSquare(String fileName){

		//		System.out.println("Enter size of magic square. Must be odd.");

		//System.out.println("matrix size: " + matrixSize);
		if (matrixSize%2 != 0) {
			matrix =  new int[matrixSize][matrixSize];



			int row = matrixSize - 1;
			int col = matrixSize / 2;

			int oldRow;
			int oldCol;
			for (int i = 1; i<= Math.pow(matrixSize, 2); i++) {
				//			System.out.println("Row: " + row);
				//			System.out.println("Col " + col);
				matrix[row][col] = i;
				oldRow = row;
				oldCol = col;
				row++;
				col++;
				//System.out.println(Arrays.deepToString(matrix).replace("], ", "]\n"));
				//snr of stackoverflow
				//			System.out.println("------------");
				//			for (int[] print : matrix) {
				//				for (int prints : print) {
				//					System.out.print(prints + " ");
				//				}
				//				System.out.println();
				//			}
				//			System.out.println("------------");
				if (row == matrixSize) {
					row = 0;
				}
				if (col == matrixSize) {
					col = 0;
				}

				if (matrix[row][col] != 0) {
					row = oldRow;
					col = oldCol;
					row--;
				}
			}
			matrixFile = fileName;
			writeMatrix();

		}
		else {
			System.out.println("Size must be odd.");
		}
	}
	/**
	 * Reads a file and creates a matrix based off the integers contained within the file.
	 * @param matrixFile
	 * @return the matrix from the file that was read in
	 */
	private static int[][] readMatrix(String matrixFile) {
		//		Scanner kbd = new Scanner(System.in);
		//		
		//		System.out.println("Enter name of filename: ");
		//	System.out.println("File name: " + fileName);

		//		System.out.println("Matrix file: " + matrixFile);
		//System.out.println("Read matrix method.");
		File matrixReader = new File(matrixFile);
		try {
			//	System.out.println("Entering try.");
			if (matrixReader.exists() && matrixReader.isFile()) {
				//System.out.println("Entering if after try.");
				Scanner matrixScanner = new Scanner(matrixReader);
				matrixSize = matrixScanner.nextInt();
				matrix = new int[matrixSize][matrixSize];
				//System.out.println("Matrix size in readMatrix: " + matrixSize);
				//				while (matrixScanner.hasNextLine()) {
				//					String matrixLine = new String(matrixScanner.nextLine());
				//					Scanner scannerOfMatrix = new Scanner (matrixLine);
				//				System.out.println("Matrix before reading the file.");	
				//				for (int[] print : matrix) {
				//					for (int prints : print) {
				//						System.out.print(prints + " ");
				//					}
				//					System.out.println();
				//				}
				while (matrixScanner.hasNextInt() ) {
					//System.out.println("Entering while loop");
					for (int i = 0; i<matrixSize; i++) {
						//System.out.println("Entering first for loop.");
						for (int j=0; j< matrixSize; j++) {
							//System.out.println("Entering second for loop.");
							//System.out.println(matrix[i][j]);
							//	System.out.println("Next number in file: " + matrixScanner.nextInt());
							matrix[i][j] = matrixScanner.nextInt();
							//								for (int[] print : matrix) {
							//									for (int prints : print) {
							//										System.out.print(prints + " ");
							//									}
							//									System.out.println();
							//								}

						}
						//System.out.println("Exiting 2nd for loop.");
					}
					//						System.out.println("Matrix after reading the file.");
					for (int[] print : matrix) {
						for (int prints : print) {
							System.out.print(prints + " ");
						}
						System.out.println();
					}


				}
				matrixScanner.close();
				//System.out.println("File completely read.");
			}
		}catch (FileNotFoundException e){
			System.out.println("File not found.");
		}

		return matrix;	
	}
	/**
	 * Takes a file name and writes a created matrix to that file
	 */
	private static void writeMatrix() {
		File file = new File(matrixFile);
		try {
			PrintWriter outFile = new PrintWriter(new FileWriter(file));
			outFile.println(matrixSize);
			for (int[] print : matrix) {
				for (int prints : print) {
					outFile.print(prints + " " );
				}
				outFile.println();
			}
			outFile.close();
			//System.out.println("Created file with name of " + matrixFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
