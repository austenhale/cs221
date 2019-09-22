import java.util.Comparator;


/**
 * Class for sorting lists that implement the IndexedUnsortedList interface,
 * using ordering defined by class of objects in list or a Comparator.
 * As written uses Quicksort algorithm.
 *
 * @author CS221
 */
public class Sort
{	
	/**
	 * Returns a new list that implements the IndexedUnsortedList interface. 
	 * As configured, uses WrappedDLL. Must be changed if using 
	 * your own IUDoubleLinkedList class. 
	 * 
	 * @return a new list that implements the IndexedUnsortedList interface
	 */
	private static <T> IndexedUnsortedList<T> newList() 
	{
		return new IUDoubleLinkedList<T>(); 
	}

	/**
	 * Sorts a list that implements the IndexedUnsortedList interface 
	 * using compareTo() method defined by class of objects in list.
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param <T>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @see IndexedUnsortedList 
	 */
	public static <T extends Comparable<T>> void sort(IndexedUnsortedList<T> list) 
	{
		quicksort(list);
	}

	/**
	 * Sorts a list that implements the IndexedUnsortedList interface 
	 * using given Comparator.
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param <T>
	 *            The class of elements in the list
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @param c
	 *            The Comparator used
	 * @see IndexedUnsortedList 
	 */
	public static <T> void sort(IndexedUnsortedList <T> list, Comparator<T> c) 
	{
		quicksort(list, c);
	}

	/**
	 * Quicksort algorithm to sort objects in a list 
	 * that implements the IndexedUnsortedList interface, 
	 * using compareTo() method defined by class of objects in list.
	 * DO NOT MODIFY THIS METHOD SIGNATURE
	 * 
	 * @param <T>
	 *            The class of elements in the list, must extend Comparable
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 */
	private static <T extends Comparable<T>> void quicksort(IndexedUnsortedList<T> list)
	{

		if (list.size() < 2) { //base case, if there's 1 or 0 elements the list is sorted

			return;
		}

		IndexedUnsortedList<T> rightList = newList();
		IndexedUnsortedList<T> leftList = newList();

		T pivotElement = list.first();
		list.removeFirst(); //removing first from list so that the pivot is not included in the for each loop
		for (T e : list) {
			if (pivotElement.compareTo(e) == 1) { //if pivotElement is bigger than the compared element, add to leftList
				leftList.add(e);
			}
			else {
				rightList.add(e);
			}
		}
		quicksort(leftList);
		quicksort(rightList);
		list.addToFront(pivotElement); //adding pivot back so that the list remains the same
		leftList.add(pivotElement); //combining elements smaller than pivot and the pivot into a single list in proper order

		for (T e: rightList) { //adding all the elements bigger than pivot to the end of leftList, in order
			leftList.add(e);
		}


		for (T e: leftList) { //1 by 1 replacing elements in list by removing the first element in list, and adding a new element to the list
			list.removeFirst(); //by the end of the loop, list has been made to look like leftList and is sorted order
			list.add(e);

		}
		
	}

	/**
	 * Quicksort algorithm to sort objects in a list 
	 * that implements the IndexedUnsortedList interface,
	 * using the given Comparator.
	 * DO NOT MODIFY THIS METHOD SIGNATURE
	 * 
	 * @param <T>
	 *            The class of elements in the list
	 * @param list
	 *            The list to be sorted, implements IndexedUnsortedList interface 
	 * @param c
	 *            The Comparator used
	 */
	private static <T> void quicksort(IndexedUnsortedList<T> list, Comparator<T> c)
	{
		
		if (list.size() < 2) { //base case, if there's 1 or 0 elements the list is already sorted

			return;
		}
		IndexedUnsortedList<T> rightList = newList();
		IndexedUnsortedList<T> leftList = newList();

		T pivotElement = list.first();
		list.removeFirst(); //removed so that the for each loop does not include the pivot element
		for (T e : list) {
			if (c.compare(pivotElement, e) == 1) { //if pivotElement is bigger than the compared element, add to leftList
				leftList.add(e);
			}
			else {
				rightList.add(e);
			}
		}
		quicksort(leftList,c);
		quicksort(rightList,c);
		list.addToFront(pivotElement); //adds the previously removed head back to list
		leftList.add(pivotElement); //adding pivot to the middle of the sorted list

		while (!rightList.isEmpty()) { //combines right and left list to form a sorted list
			leftList.add(rightList.removeFirst());
		}

		while (!leftList.isEmpty()) { //takes elements from leftList, and adds them to list
			list.removeFirst(); //treats list as a queue, first goes out, adds new to back, repeats until leftList has no more elements
			list.add(leftList.removeFirst());
		}
	}
}