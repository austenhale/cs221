import java.io.File;

/**
 * The driver class of the magic square class. It will take in command line arguement and call the approriate methods.
 * @author austenhale
 *
 */
public class MagicSquareDriver {

	public static void main(String[] args) {

		//		Scanner kbd = new Scanner(System.in);
		//		
		//		System.out.println("What would you like to do? (Create a square, "
		//				+ "or check a square) ");
		//		System.out.println("Enter '-create' to create a square, or '-check' to check for"
		//				+ " a magic square.");
		//		String userAction = kbd.nextLine();
		//		if (userAction.toLowerCase().equals("-create")){
		//			//System.out.println("create a square.");
		//			System.out.println("Enter size of magic square. Must be odd.");
		//			int squareSize = kbd.nextInt();
		//			if (squareSize%2 != 0) {
		//				MagicSquare ms = new MagicSquare(squareSize);
		//				ms.createMagicSquare();
		//			}else {
		//				System.out.println("The size of the magic square must be odd.");
		//			}
		//		}
		//		else if (userAction.toLowerCase().equals("-check")) {
		//				//System.out.println("check a square.");
		//			System.out.println("Enter file name of square to check.");
		//			String fileName = kbd.next();
		//			MagicSquare.checkMatrix(fileName);
		//			if (MagicSquare.checkMatrix(fileName) == false) {
		//				System.out.println("File is not a magic square.");
		//			}
		//			else {
		//				System.out.println("File is a magic square.");
		//			}
		//			}
		//			else {
		//				System.out.println("Invalid command.");
		//			}
		//		System.out.println("End of program.");
		//		kbd.close();
		try {
			if(args[0].equalsIgnoreCase("-check")) {
				try {
					if (args[1] != null) {

						String fileName = args[1];
						File checkFile = new File(fileName);
						if (checkFile.exists() && checkFile.isFile()) {
							//System.out.println("File name read from console is: " + fileName);
							//MagicSquare.checkMatrix(fileName);
							System.out.println("The matrix: ");
							if (MagicSquare.checkMatrix(fileName)== false) {

								System.out.println("File is not a magic square.");
							}
							else {
								System.out.println("File is a magic square.");
							}
						}else {
							System.out.println("File name not found.");
						}
					}else {
						System.out.println("Invalid command entry. No filename found.");
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Too little or too many command prompts. Enter proper format of java MagicSquareDriver [ -check | -create] [filename.txt] [ |x]");
				}

			}else if (args[0].equalsIgnoreCase("-create")) {
				try {
					if (args[1] != null) {
						if (args[2] != null) {
							String fileName = args[1];
							int squareSize = Integer.parseInt(args[2]);
							if (squareSize%2 != 0) {
								MagicSquare ms = new MagicSquare(squareSize, fileName);
								ms.createMagicSquare(fileName);
							}
							else {
								System.out.println("Square size must be an odd number.");
							}
						}
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
					System.out.println("Too little or too many command prompts. Enter proper format of java MagicSquareDriver [ -check | -create] [filename.txt] [ |x].");
				}
			}	
			else {
				System.out.println("Invalid command entry. Proper format is 'java MagicSquareDriver [ -check | -create] [filename.txt] [ |x]");
			}
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Too little commands entered. Enter proper format of java MagicSquareDriver [ -check | -create] [filename.txt] [ |x].");
		}
		catch (NumberFormatException e) {
			System.out.println("Error in command format. Use proper format of ava MagicSquareDriver [ -check | -create] [filename.txt] [ |x].");
		}



	}
}


