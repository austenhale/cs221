import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
/**
 * This is the driver, and only class, for the FormatChecker program. It will take in however many files a user inputs, and read through the file. If the program
 * finds an invalid input, such as an X, less than or more than 2 numbers on the first line, or that the row/column length does not match the inputed
 * row/column length, it will declare the file invalid. At the end of the program, it will print out all the inputed file names, and if they were valid or not.
 * @author austenhale
 *
 */
public class FormatChecker {

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		if (args.length == 0) { //checks to see if there is any input
			System.out.println("Please input a file name. (java FormatChecker file1 [file2 ... fileN)");
		}


		for (int i = 0; i<args.length; i++) { //for loop for each file inputed in command line argument
			
			double rowSize = 0;
			double colSize = 0;
			boolean valid = true;
			double[][] sizeChecker;
			int columnIncrementer = 0;
			String error = "";
			String fileName = args[i];
			File file = new File(fileName);
		
			try {
				if(file.exists() && file.isFile()) {
					Scanner fileScanner = new Scanner(file);

					try {
						String rowNCol = fileScanner.nextLine(); //takes whole first line of file and converts to string

						String [] rowCol = rowNCol.trim().split("\\s+"); 

						if (rowCol.length != 2) { //if statement that checks to see if only two numbers have been inputed for row and column
							error += "Invalid input for rows and column sizes. (Too many or too little)";
							valid = false;

						}

						rowSize = Integer.parseInt(rowCol[0]); //assigns first inputed value to the row size
						colSize = Integer.parseInt(rowCol[1]); //assigns 2nd inputed value to column size
					}catch (NumberFormatException e) { //states the file as invalid if non-number is found
						
						error += e.toString() + " in row/column size line.";
						valid = false;

					}

					sizeChecker = new double[(int) rowSize][(int) colSize];

					while (fileScanner.hasNextLine() && valid != false) { //while loop checking to see if the file has become false, or if the file has more lines to read

						try {

							String numberLine = fileScanner.nextLine();
							String [] numbers = numberLine.trim().split("\\s+");

							if (numbers != null && numbers.length > 1) { //ensures that the line of numbers has at least 1 number in it

								for (int numberSpot = 0; numberSpot < numbers.length; numberSpot++) {
									sizeChecker[columnIncrementer][numberSpot] = Double.parseDouble(numbers[numberSpot]);
									
								}
								columnIncrementer++;
							}
						}
						/* Catches for exceptions that would result in invalid files */
						catch (ArrayIndexOutOfBoundsException e) {

							error += "File does not have rows/columns matching to inputed row/column size.";
							valid = false;

						}
						catch (NumberFormatException e) {
							
							error += e.toString();
							valid = false;

						}

						catch (NoSuchElementException e) {

							error += e.toString();
							valid = false;

						}

			
						/* statements for checking rows and columns length */
						try {

							if (sizeChecker.length != rowSize) { //if statement for checking row size
								error += "Row length does not match inputted size.";
								valid = false;

							}
						}catch (NoSuchElementException e) {
							error += "Row length does not match inputted size.";
							valid = false;

						}

						for (int columnChecker = 0; columnChecker < rowSize; columnChecker++){
							if (sizeChecker[columnChecker].length != colSize) { //if statement for checking column size

								error += "Column length does not match inputted size.";
								valid = false;

							}
						}

					}//end while loop
					fileScanner.close();
				}
				else {
					error += "File of " + fileName + " not found.";
					valid = false;

				}
			}catch(FileNotFoundException e) {
				error += e.toString();
				valid = false;

			}

			if (valid == true) { //if statement for if the file stayed valid throughout the program
				System.out.println(fileName);
				System.out.println("VALID");
			}
			if (valid == false){ //if statement for if the file was declared invalid at any point in the program
				System.out.println(fileName);
				System.out.println(error);
				System.out.println("INVALID");
			}
			System.out.println();
		} //end of forloop for args

	}//end main

}//end FormatChecker class
