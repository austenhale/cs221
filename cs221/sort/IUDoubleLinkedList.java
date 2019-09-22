import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
/**
 * Class that implements methods for a double linked list.
 * @author austenhale
 *
 * @param <T>
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T>{

	private LinearNode<T> head, tail;
	private int size;
	private int modCount;

	/* Constructor for Double Linked List */
	public IUDoubleLinkedList() {
		head = null;
		tail = null;
		size = 0;
		modCount = 0;
	}
	
	@Override
	public void addToFront(T element) {

		LinearNode<T> newNode = new LinearNode<T>(element);
		newNode.setNext(head);
		newNode.setPrevious(null);
		if (head != null) {
			head.setPrevious(newNode);
		}
		head = newNode;

		if (tail == null) {
			tail = head;
		}
		size++;
		modCount++;
	}

	
	@Override
	public void addToRear(T element) {
		LinearNode<T> newNode = new LinearNode<T>(element);
		if(!isEmpty()) {
			tail.setNext(newNode);
		}
		else {
			head = newNode;
		}
		newNode.setPrevious(tail);
		tail = newNode;
		size++;
		modCount++;

	}

	
	@Override
	public void add(T element) {
		addToRear(element);

	}

	@Override
	public void addAfter(T element, T target) {

		LinearNode<T> current = head;
		while (current != null && !current.getElement().equals(target)) {
			current = current.getNext();
		}
		if (current == null) { //didn't find target
			throw new NoSuchElementException();
		}
		//found target
		LinearNode<T> newNode = new LinearNode<T>(element);
		newNode.setNext(current.getNext());
		newNode.setPrevious(current);
		current.setNext(newNode);
		if (current == tail) {
			tail = newNode;
		}
		else {
			newNode.getNext().setPrevious(newNode);
		}

		size++;
		modCount++;
	}

	@Override
	public void add(int index, T element) {
		if (index < 0 || index > size) {
			throw new IndexOutOfBoundsException();
		}

		LinearNode<T> current = head;
		int currentIndex = 0;
		LinearNode<T> newNode = new LinearNode<T>(element);
		if (index == 0) {
			newNode.setNext(head);
			newNode.setPrevious(null);
			if (head != null) {
				head.setPrevious(newNode);
			}
			head = newNode;
			if (tail == null) {
				tail = newNode;
			}
		}else
		{
			while (currentIndex < index -1) {
				current = current.getNext();
				currentIndex++;
			}
			newNode.setNext(current.getNext());
			newNode.setPrevious(current);
			current.setNext(newNode);

			if (newNode.getNext() == null) {

				tail = newNode;
			}
			else {
				newNode.getNext().setPrevious(newNode);

			}
		}

		size++;
		modCount++;
	}

	@Override
	public T removeFirst() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal = head.getElement();
		if(size == 1) {

			head = tail = null;
		}else {

			LinearNode<T> current = head.getNext();
			current.setNext(head.getNext().getNext());
			current.setPrevious(null);
			head = current;
		}
		size--;
		modCount++;
		return retVal;
	}

	
	@Override
	public T removeLast() {
		if(isEmpty()) {
			throw new NoSuchElementException();
		}
		T retVal = tail.getElement();
		if (size == 1) {
			head = tail = null;
		}
		else {

			tail.getPrevious().setNext(null);
			tail = tail.getPrevious();

		}
		size--;
		modCount++;

		return retVal;
	}

	@Override
	public T remove(T element) {

		ListIterator<T> lit = listIterator(0);
		T retVal = null;
		boolean foundIt = false;
		while (lit.hasNext() && !foundIt) {
			retVal = lit.next();
			if (retVal.equals(element)) {
				foundIt = true;
			}
		}
		if (!foundIt) {
			throw new NoSuchElementException();
		}
		lit.remove();
		return retVal;
	}

	
	@Override
	public T remove(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		LinearNode<T> current = head;
		for (int i = 0; i < index; i++) {
			current = current.getNext();
		}
		if (current == head) { //removing first index
			head = current.getNext();
		}else {
			current.getPrevious().setNext(current.getNext());
		}
		if (current == tail) { //removing last index
			tail = current.getPrevious();
		}
		else {
			current.getNext().setPrevious(current.getPrevious());
		}
		size--;
		modCount++;
		return current.getElement();

	}

	@Override
	public void set(int index, T element) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}


		ListIterator<T> lit = listIterator(index);
		lit.next();
		lit.set(element);
		
		}


	@Override
	public T get(int index) {
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}

		ListIterator<T> lit = listIterator(index);
		return lit.next();
	}

	
	@Override
	public int indexOf(T element) {
		int index = -1;
		LinearNode<T> current = head;
		int currentIndex = 0;
		while (current != null && index < 0) {
			if(current.getElement().equals(element)) {
				index = currentIndex;

			}
			current = current.getNext();
			currentIndex++;
		}
		return index;
	}

	
	@Override
	public T first() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return head.getElement();
	}

	
	@Override
	public T last() {
		if (isEmpty()) {
			throw new NoSuchElementException();
		}
		return tail.getElement();
	}

	
	@Override
	public boolean contains(T target) {
		LinearNode<T> current = head;
		boolean foundIt = false;
		if (current == null) {
			foundIt = false;
		}
		else if (current.getElement() == target) {
			foundIt = true;
		}
		while (current != null && !foundIt) {
			if (current.getElement() == target) {
				foundIt = true;
			}
			else {
				current = current.getNext();
			}
		}
		return foundIt;
	}

	
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	
	@Override
	public int size() {

		return size;
	}

	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("[");
		for (T element : this) {
			str.append(element.toString() + ", ");
		}
		if (size() > 0) {
			str.delete(str.length() -2, str.length());
		}
		str.append("]");
		return str.toString();
	}

	@Override
	public Iterator<T> iterator() {
		return new IUDoubleLinkedIterator();
	}

	
	@Override
	public ListIterator<T> listIterator() {
		return new IUDoubleLinkedIterator();
	}

	
	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		return new IUDoubleLinkedIterator(startingIndex);
	}

	private class IUDoubleLinkedIterator implements ListIterator<T> {

		private LinearNode<T> nextNode;
		private int iterModCount;
		private int nextIndex;
		private LinearNode<T> lastReturnedNode;
		private boolean nextLast;
		private boolean removedLast;


		/**
		 * Constructor for ListIterator for a DoubleLinkedList
		 */
		public IUDoubleLinkedIterator() {
			this(0);

		}
		/**
		 * Constructor for List Iterator for Double Linked List
		 * @param startingIndex - the constructor will start the iterator at this index
		 */
		public IUDoubleLinkedIterator(int startingIndex) {
			if (startingIndex < 0 || startingIndex > size) {
				throw new IndexOutOfBoundsException();
			}
			nextLast = false;
			nextNode = head;
			iterModCount = modCount;
			nextIndex = 0;
			lastReturnedNode = null;
			removedLast = false;

			for (; nextIndex < startingIndex; nextIndex++) {
				nextNode = nextNode.getNext();
			}
		}

		@Override
		public boolean hasNext() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextNode != null);
		}

		@Override
		public T next() {
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			lastReturnedNode = nextNode;
			nextNode = nextNode.getNext();

			nextIndex++;
			nextLast = true;
			removedLast = false;
			if (nextNode == null) {
				return tail.getElement();
			}else {
				return nextNode.getPrevious().getElement();
			}
		}

		
		@Override
		public boolean hasPrevious() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return (nextNode != head);
		}

		
		@Override
		public T previous() {
			if (!hasPrevious()) {
				throw new NoSuchElementException();
			}
			if (nextNode == null) {
				nextNode = tail;

			}
			else {
				nextNode = nextNode.getPrevious();

			}
			lastReturnedNode = nextNode;

			nextLast = false;
			removedLast = false;
			nextIndex--;
			return nextNode.getElement();
		}

	
		@Override
		public int nextIndex() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return nextIndex;
		}

		
		@Override
		public int previousIndex() {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			return nextIndex - 1;
		}

		
		@Override
		public void remove() {

			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (lastReturnedNode == null) {
				throw new IllegalStateException();
			}
			if (lastReturnedNode == head) {
				head = lastReturnedNode.getNext();
				if (head != null) {
					head.setPrevious(null);
				}
			}else {
				lastReturnedNode.getPrevious().setNext(lastReturnedNode.getNext());
			}
			if (lastReturnedNode == tail) {
				tail = lastReturnedNode.getPrevious();
				if (tail != null) {
					tail.setNext(null);
				}
			}
			else {
				lastReturnedNode.getNext().setPrevious(lastReturnedNode.getPrevious());

			}
			removedLast = true;
			lastReturnedNode = null;
			modCount++;
			iterModCount++;
			size--;
		}

		
		@Override
		public void set(T e) {
			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}
			if (lastReturnedNode == null) {
				throw new IllegalStateException();
			}
			removedLast = false;
			lastReturnedNode.setElement(e);
			iterModCount++;
			modCount++;
		}

		
		@Override
		public void add(T e) {

			if (iterModCount != modCount) {
				throw new ConcurrentModificationException();
			}

			LinearNode<T> addNode = new LinearNode<T>(e);
			if (lastReturnedNode == null && !removedLast) { //if there was no element returned, and remove didn't set last returned to null
				addNode.setNext(head);
				if (head != null) { //if there's a head node
					head.setPrevious(addNode);
				}
				head = addNode;
				if (tail == null) {
					tail = addNode;
				}
			}
			else
			{
				if (nextLast == true) { //if next was called last, not previous
					addNode.setNext(lastReturnedNode.getNext());
					addNode.setPrevious(lastReturnedNode);
					lastReturnedNode.setNext(addNode);
				}
				else { //if previous was called last
					addNode.setNext(lastReturnedNode);
					if (lastReturnedNode.getPrevious() != null) { //if element before LRN isn't null, set it as previous
						lastReturnedNode.getPrevious().setNext(addNode); //set the element before LRN to have addNode as its next
					}
					else { //LRN's previous is null, thus addNode becomes the new head
						head = addNode;
					}
					addNode.setPrevious(lastReturnedNode.getPrevious());
					lastReturnedNode.setPrevious(addNode);
				}
				if (addNode.getNext() == null) { //if addNode next is null, it becomes the new tail
					tail = addNode;
				}
				else {
					addNode.getNext().setPrevious(addNode);
				}

			}
			removedLast = false;
			lastReturnedNode = null;
			size++;
			modCount++;
			iterModCount++;
		}



	}
}
