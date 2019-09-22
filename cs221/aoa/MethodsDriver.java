import java.util.Arrays;

public class MethodsDriver {

	/**
	 * Driver class for MethodsToAnalyze that passes an array to allow the methods to 
	 * calculate total amount of statements within each method. 
	 * @param args
	 */
	public static void main(String[] args) {

		for (int size = 1; size < 10; size += 1) {
			int[] array = ArrayOfInts.descendingArray(size);
			System.out.println("Before sort: " + Arrays.toString(array));
			// Commented out for testing methods using random array, but currently using descending
			//int[] array = ArrayOfInts.randomizedArray(size);
			
			MethodsToAnalyze.find(array, size);
			long statementFind = MethodsToAnalyze.getFindStatements();
			MethodsToAnalyze.replaceAll(array, size, 100);
			long statementReplace = MethodsToAnalyze.getStatements();
			MethodsToAnalyze.sortIt(array);
			long statementSort = MethodsToAnalyze.getSortStatements();

			System.out.println(Arrays.toString(array));
			

			System.out.printf("\nfor n = %d, took %d statements\n",
					size, statementFind);
			System.out.printf("\nfor n = %d, took %d statements\n",
					size, statementReplace);
			System.out.println("-----------");
			System.out.printf("\nfor n = %d, took %d statements\n",
					size, statementSort);
			System.out.println("-----------");


		}

	}

}
