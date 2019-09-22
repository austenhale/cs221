/**
 * A collection of methods that work with arrays of ints.
 * 
 * @author mvail
 */
public class MethodsToAnalyze {

	private static long numStatements;
	private static long findStatements;
	private static long sortStatements;
	/**
	 * Return index where value is found in array or -1 if not found.
	 * @param array ints where value may be found
	 * @param value int that may be in array
	 * @return index where value is found or -1 if not found
	 */
	public static int find(int[] array, int value) {
		//data collection
		findStatements = 2; // 2 for initializing i, and for condition check
		for (int i = 0; i < array.length; i++) {
			//data collection
			findStatements += 3; //one for incrementing i, one for condition check, one for checking if statement
			if (array[i] == value) {
				
				return i;//return statements don't count
			}
		}
		
		
		return -1; //does not add to statement as it is part of the cost of a method
	}

	/**
	 * Replace all occurrences of oldValue with newValue in array.
	 * @param array ints where oldValue may be found
	 * @param oldValue value to replace
	 * @param newValue new value
	 */
	public static void replaceAll(int[] array, int oldValue, int newValue) {
		//data collection
		numStatements = 2 + findStatements; //1 for initializing index, 1 for the while loop condition, and findStatements is however many statements were in find
//		System.out.println("Num statements after adding 2 for replaceAll: " + numStatements);
		int index = find(array, oldValue);
		while (index > -1) {
			//data collection
			numStatements += 3 + findStatements; //1 for assigning a value to the index in the array, 1 for the while loop condition check, 1 for assigning a value to index,
												 //	and however many statements in find
			array[index] = newValue;
			index = find(array, oldValue);
		}
		//System.out.println("Replace statements: " + numStatements);
	}
	
	/**
	 * Take an int[] and reorganize it so they are in ascending order.
	 * @param array ints that need to be ordered 
	 */
	public static void sortIt(int[] array) {
		//data collection
		sortStatements = 2; //one for initializing next, and one for condition
		for (int next = 1; next < array.length; next++) {
			//data collection
			sortStatements += 6; //one for incrementing next, 2 for assigning value and index, two for condition checks, and one for assigning array[index] to value
			int value = array[next];
			int index = next;
			while (index > 0 && value < array[index - 1]) {
				//data collection
				sortStatements += 3; //one for condition check, one for array[index] = array[index -1], and one for decreasing index
				array[index] = array[index - 1];
				index--;
			}
			array[index] = value;
		}
		//System.out.println("Sort statements: " + sortStatements);
	}
	/**
	 * gets the total amount of statements in a method and returns it
	 * @return total statements
	 */
	public static long getStatements() {
		return numStatements;
	}
	public static long getFindStatements() {
		//System.out.println("Find statements: " + findStatements);
		return findStatements;
	}
	public static long getSortStatements() {
		return sortStatements;
	}
}

