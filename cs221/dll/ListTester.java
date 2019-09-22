import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * A unit test class for lists that implement IndexedUnsortedList. 
 * This is a set of black box tests that should work for any implementation
 * of this interface.
 * 
 * NOTE: One example test is given for each interface method using a new list to
 * get you started.
 * 
 * @author mvail, mhthomas, amussell (lambdas)
 * @author austenhale
 */
public class ListTester {
	//possible lists that could be tested
	private static enum ListToUse {
		goodList, badList, arrayList, singleLinkedList, doubleLinkedList
	};
	// TODO: THIS IS WHERE YOU CHOOSE WHICH LIST TO TEST
	private final static ListToUse LIST_TO_USE = ListToUse.doubleLinkedList;

	// possible results expected in tests
	private enum Result {
		IndexOutOfBounds, IllegalState, NoSuchElement, 
		ConcurrentModification, UnsupportedOperation, 
		NoException, UnexpectedException,
		True, False, Pass, Fail, 
		MatchingValue,
		ValidString
	};

	// named elements for use in tests
	private static final Integer ELEMENT_A = new Integer(1);
	private static final Integer ELEMENT_B = new Integer(2);
	private static final Integer ELEMENT_C = new Integer(3);
	private static final Integer ELEMENT_D = new Integer(4);
	private static final Integer ELEMENT_X = new Integer(-1);//element that should appear in no lists
	private static final Integer ELEMENT_Z = new Integer(-2);//element that should appear in no lists

	// determine whether to include ListIterator functionality tests
	private final boolean SUPPORTS_LIST_ITERATOR; //initialized in constructor
	
	//tracking number of tests and test results
	private int passes = 0;
	private int failures = 0;
	private int totalRun = 0;

	private int secTotal = 0;
	private int secPasses = 0;
	private int secFails = 0;

	//control output - modified by command-line args
	private boolean printFailuresOnly = true;
	private boolean showToString = true;
	private boolean printSectionSummaries = true;

	/**
	 * Valid command line args include:
	 *  -a : print results from all tests (default is to print failed tests, only)
	 *  -s : hide Strings from toString() tests
	 *  -m : hide section summaries in output
	 * @param args not used
	 */
	public static void main(String[] args) {
		// to avoid every method being static
		ListTester tester = new ListTester(args);
		tester.runTests();
	}

	/** tester constructor
	 * @param args command line args
	 */
	public ListTester(String[] args) {
		for (String arg : args) {
			if (arg.equalsIgnoreCase("-a"))
				printFailuresOnly = false;
			if (arg.equalsIgnoreCase("-s"))
				showToString = false;
			if (arg.equalsIgnoreCase("-m"))
				printSectionSummaries = false;
		}
		switch (LIST_TO_USE) {
		case doubleLinkedList:
			SUPPORTS_LIST_ITERATOR = true;
			break;
		default:
			SUPPORTS_LIST_ITERATOR = false;
			break;
		}
	}

	/** Print test results in a consistent format
	 * @param testDesc description of the test
	 * @param result indicates if the test passed or failed
	 */
	private void printTest(String testDesc, boolean result) {
		totalRun++;
		if (result) { passes++; }
		else { failures++; }
		if (!result || !printFailuresOnly) {
			System.out.printf("%-46s\t%s\n", testDesc, (result ? "   PASS" : "***FAIL***"));
		}
	}

	/** Print a final summary */
	private void printFinalSummary() {
		String verdict = String.format("\nTotal Tests Run: %d,  Passed: %d (%.1f%%),  Failed: %d\n",
				totalRun, passes, passes*100.0/totalRun, failures);
		String line = "";
		for (int i = 0; i < verdict.length(); i++) {
			line += "-";
		}
		System.out.println(line);
		System.out.println(verdict);
	}

	/** Print a section summary */
	private void printSectionSummary() {
		secTotal = totalRun - secTotal;
		secPasses = passes - secPasses;
		secFails = failures - secFails;
		System.out.printf("\nSection Tests: %d,  Passed: %d,  Failed: %d\n", secTotal, secPasses, secFails);
		secTotal = totalRun; //reset for next section
		secPasses = passes;
		secFails = failures;		
		System.out.printf("Tests Run So Far: %d,  Passed: %d (%.1f%%),  Failed: %d\n",
				totalRun, passes, passes*100.0/totalRun, failures);
	}
	
	/////////////////////
	// XXX runTests()
	/////////////////////

	/** Run tests to confirm required functionality from list constructors and methods */
	private void runTests() {
		//Possible list contents after a scenario has been set up
		/* Possible One Element Outcomes of Scenarios */
		Integer[] LIST_A = {ELEMENT_A};
		String STRING_A = "A";
		Integer[] LIST_B = {ELEMENT_B};
		String STRING_B = "B";
		
		/* Possible Two Element Outcomes of Scenarios */
		Integer[] LIST_BA = {ELEMENT_B, ELEMENT_A};
		String STRING_BA = "BA";
		Integer[] LIST_AB = {ELEMENT_A, ELEMENT_B};
		String STRING_AB = "AB";
		Integer[] LIST_CB = {ELEMENT_C, ELEMENT_B};
		String STRING_CB = "CB";
		Integer[] LIST_AC = {ELEMENT_A, ELEMENT_C};
		String STRING_AC = "AC";
		Integer[] LIST_BC = {ELEMENT_B, ELEMENT_C};
		String STRING_BC = "BC";
		
		/* Possible Three Element Outcomes of Scenarios */
		Integer[] LIST_CAB = {ELEMENT_C, ELEMENT_A, ELEMENT_B};
		String STRING_CAB = "CAB";
		Integer[] LIST_ABC = {ELEMENT_A, ELEMENT_B, ELEMENT_C};
		String STRING_ABC = "ABC";
		Integer[] LIST_ACB = {ELEMENT_A, ELEMENT_C, ELEMENT_B};
		String STRING_ACB = "ACB";
		Integer[] LIST_DBC = {ELEMENT_D, ELEMENT_B, ELEMENT_C};
		String STRING_DBC = "DBC";
		Integer[] LIST_ADC = {ELEMENT_A, ELEMENT_D, ELEMENT_C};
		String STRING_ADC = "ADC";
		Integer[] LIST_ABD = {ELEMENT_A, ELEMENT_B, ELEMENT_D};
		String STRING_ABD = "ABD";

		//newly constructed empty list
		testEmptyList(newList, "newList_S1"); //scenario 1
		
		//empty to 1-element list
		testSingleElementList(emptyList_addToFrontA_A, "emptyList_addToFrontA_A_S2", LIST_A, STRING_A); //scenario 2
		testSingleElementList(emptyList_addToRearA_A, "emptyList_addToRearA_A_S3", LIST_A, STRING_A); //scenario 3
		testSingleElementList(emptyList_addA_A, "emptyList_addA_A_S4", LIST_A, STRING_A); //scenario 4
		testSingleElementList(emptyList_add0A_A, "emptyList_add0A_A_S5", LIST_A, STRING_A); //scenario 5
		testSingleElementList(emptyList_listIterAddA_A, "emptyList_listIterAddA_A_S62", LIST_A, STRING_A); //scenario 62
		//1-element to empty list
		testEmptyList(A_removeFirst_emptyList, "A_removeFirst_emptyList_S12"); //scenario 12
		testEmptyList(A_removeLast_emptyList, "A_removeLast_emptyList_S13"); //scenario 13
		testEmptyList(A_removeA_emptyList, "A_removeA_emptyList_S14"); //scenario 14
		testEmptyList(A_remove0_emptyList, "A_remove0_emptyList_S15"); //scenario 15
		testEmptyList(A_iterRemoveAfterNextA_emptyList, "A_iterRemoveAfterNextA_emptyList_S44"); // scenario 44
		testEmptyList(A_listIterRemoveAfterNextA_emptyList, "A_listIterRemoveAfterNextA_emptyList_S50"); //scenario 50
		testEmptyList(A_listIterRemoveAfterPreviousA_emptyList,"A_listIterRemoveAfterPreviousA_emptyList_S56"); //scenario 56 
		//1-element to 2-element
		testTwoElementList(A_addToFrontB_BA, "A_addToFrontB_BA_S6", LIST_BA, STRING_BA); //scenario 6
		testTwoElementList(A_addToRearB_AB, "A_addToRearB_AB_S7", LIST_AB, STRING_AB); //scenario 7
		testTwoElementList(A_addAfterBA_AB, "A_addAfterBA_AB_S8", LIST_AB, STRING_AB); //scenario 8
		testTwoElementList(A_addB_AB, "A_addB_AB_S9", LIST_AB, STRING_AB); //scenario 9
		testTwoElementList(A_add0B_BA, "A_add0B_BA_S10",LIST_BA, STRING_BA); //scenario 10
		testTwoElementList(A_add1B_AB,"A_ad1B_AB_S11", LIST_AB, STRING_AB); //scenario 11
		testTwoElementList(A_listIterAddBNewIter_BA, "A_listIterAddBNewIter_BA_S63", LIST_BA, STRING_BA); //scenario 63
		testTwoElementList(A_listIterAddBAfterNextA_AB, "A_listIterAddBAfterNextA_AB_S64", LIST_AB, STRING_AB); //scenario 64
		testTwoElementList(A_listIterAddBAfterPreviousA_BA, "A_listIterAddBAfterPreviousA_BA_S65", LIST_BA, STRING_BA); //scenario 65
		//1-element to changed 1-element via set()
		testSingleElementList(A_set0B_B, "A_set0B_S16", LIST_B, STRING_B); //scenario 16
		testSingleElementList(A_listIterSetBAfterNextA_B, "A_listIterSetBAfterNextA_B_S71", LIST_B, STRING_B); //scenario 71
		testSingleElementList(A_listIterSetBAfterPreviousA_B, "A_listIterSetBAfterPreviousA_B_S72", LIST_B, STRING_B); //scenario 72
		//2-element to 1-element
		testSingleElementList(AB_removeFirst_B, "AB_removeFirst_B_S25", LIST_B, STRING_B); //scenario 25
		testSingleElementList(AB_removeLast_A, "AB_removeLast_A_S26", LIST_A, STRING_A); //scenario 26
		testSingleElementList(AB_removeA_B, "AB_removeA_B_S27", LIST_B, STRING_B); //scenario 27
		testSingleElementList(AB_removeB_A, "AB_removeB_A_S28", LIST_A, STRING_A); //scenario 28
		testSingleElementList(AB_remove0_B, "AB_remove0_B_S29", LIST_B, STRING_B); //scenario 29
		testSingleElementList(AB_remove1_A, "AB_remove1_A_S30", LIST_A, STRING_A); //scenario 30
		testSingleElementList(AB_iterRemoveAfterNextA_B, "AB_iterRemoveAfterNextA_B_S45", LIST_B, STRING_B); //scenario 45
		testSingleElementList(AB_iterRemoveAfterNextB_A, "AB_iterRemoveAfterNextB_A_S46", LIST_A, STRING_A); //scenario 46
		testSingleElementList(AB_listIterRemoveAfterNextA_B, "AB_listIterRemoveAfterNextA_B_S51", LIST_B, STRING_B); //scenario 51
		testSingleElementList(AB_listIterRemoveAfterNextB_A, "AB_listIterRemoveAfterNextB_A_S52", LIST_A, STRING_A); //scenario 52
		testSingleElementList(AB_listIterRemoveAfterPreviousA_B , "AB_listIterRemoveAfterPreviousA_B_S57", LIST_B, STRING_B); //scenario 57
		testSingleElementList(AB_listIterRemoveAfterPreviousB_A, "AB_listIterRemoveAfterPreviousB_A_S58", LIST_A, STRING_A); //scenario 58
		//2-element to 3-element
		testThreeElementList(AB_addToFrontC_CAB, "AB_addToFrontC_CAB_S17", LIST_CAB, STRING_CAB); //scenario 17
		testThreeElementList(AB_addToRearC_ABC, "AB_addToRearC_ABC_S18", LIST_ABC, STRING_ABC); //scenario 18
		testThreeElementList(AB_addAfterCA_ACB, "AB_addAfterCA_ACB_S19", LIST_ACB, STRING_ACB); //scenario 19
		testThreeElementList(AB_addAfterCB_ABC, "AB_addAfterCB_ABC_S20", LIST_ABC, STRING_ABC); //scenario 20
		testThreeElementList(AB_addC_ABC, "AB_addC_ABC_S21", LIST_ABC, STRING_ABC); //scenario 21
		testThreeElementList(AB_add0C_CAB, "AB_add0C_CAB_S22", LIST_CAB, STRING_CAB); //scenario 22
		testThreeElementList(AB_add1C_ACB, "AB_add1C_ACB_S23", LIST_ACB, STRING_ACB); //scenario 23
		testThreeElementList(AB_add2C_ABC, "AB_add2C_ABC_S24", LIST_ABC, STRING_ABC); //scenario 24
		testThreeElementList(AB_listIterAddCNewIter_CAB, "AB_listIterAddCNewIter_CAB_S66", LIST_CAB, STRING_CAB); //scenario 66
		testThreeElementList(AB_listIterAddCAfterNextA_ACB, "AB_listIterAddCAfterNextA_ACB_S67", LIST_ACB, STRING_ACB); //scenario 67
		testThreeElementList(AB_listIterAddCAfterNextB_ABC, "AB_listIterAddCAfterNextB_ABC_S68", LIST_ABC, STRING_ABC); //scenario 68
		testThreeElementList(AB_listIterAddCAfterPreviousA_CAB, "AB_listIterAddCAfterPreviousA_CAB_S69", LIST_CAB, STRING_CAB); //scenario 69
		testThreeElementList(AB_listIterAddCAfterPreviousB_ACB, "AB_listIterAddCAfterPreviousB_ACB_S70", LIST_ACB, STRING_ACB); //scenario 70
		//2-element to changed 2-element via set()
		testTwoElementList(AB_set0C_CB, "AB_set0C_CB_S31", LIST_CB, STRING_CB); //scenario 31
		testTwoElementList(AB_set1C_AC, "AB_set1C_AC_S32", LIST_AC, STRING_AC); //scenario 32
		testTwoElementList(AB_listIterSetCAfterNextA_CB, "AB_listIterSetCAfterNextA_CB_S73", LIST_CB, STRING_CB); //scenario 73
		testTwoElementList(AB_listIterSetCAfterNextB_AC, "AB_listIterSetCAfterNextB_AC_S74", LIST_AC, STRING_AC); //scenario 74
		testTwoElementList(AB_listIterSetCAfterPreviousA_CB, "AB_listIterSetCAfterPreviousA_CB_S75", LIST_CB, STRING_CB); //scenario 75
		testTwoElementList(AB_listIterSetCAfterPreviousB_AC, "AB_listIterSetCAfterPreviousB_AC_S76", LIST_AC, STRING_AC); //scenario 76
		//3-element to 2-element
		testTwoElementList(ABC_removeFirst_BC, "ABC_removeFirst_BC_S33", LIST_BC, STRING_BC); //scenario 33
		testTwoElementList(ABC_removeLast_AB, "ABC_removeLast_AB_S34", LIST_AB, STRING_AB); //scenario 34
		testTwoElementList(ABC_removeA_BC, "ABC_removeA_BC_S35", LIST_BC, STRING_BC); //scenario 35
		testTwoElementList(ABC_removeB_AC, "ABC_removeB_AC_S36", LIST_AC, STRING_AC); //scenario 36
		testTwoElementList(ABC_removeC_AB, "ABC_removeC_AB_S37", LIST_AB, STRING_AB); //scenario 37
		testTwoElementList(ABC_remove0_BC, "ABC_remove0_BC_S38", LIST_BC, STRING_BC); //scenario 38
		testTwoElementList(ABC_remove1_AC, "ABC_remove1_AC_S39", LIST_AC, STRING_AC); //scenario 39
		testTwoElementList(ABC_remove2_AB, "ABC_remove2_AB_S40", LIST_AB, STRING_AB); //scenario 40
		testTwoElementList(ABC_iterRemoveAfterNextA_BC, "ABC_iterRemoveAfterNextA_BC_S47", LIST_BC, STRING_BC); //scenario 47
		testTwoElementList(ABC_iterRemoveAfterNextB_AC, "ABC_iterRemoveAfterNextB_AC_S48", LIST_AC, STRING_AC); //scenario 48
		testTwoElementList(ABC_iterRemoveAfterNextC_AB, "ABC_iterRemoveAfterNextC_AB_S49", LIST_AB, STRING_AB); //scenario 49
		testTwoElementList(ABC_listIterRemoveAfterNextA_BC, "ABC_listIterRemoveAfterNextA_BC_S53", LIST_BC, STRING_BC); //scenario 53
		testTwoElementList(ABC_listIterRemoveAfterNextB_AC, "ABC_listIterRemoveAfterNextB_AC_S54", LIST_AC, STRING_AC); //scenario 54
		testTwoElementList(ABC_listIterRemoveAfterNextC_AB, "ABC_listIterRemoveAfterNextC_AB_S55", LIST_AB, STRING_AB); //scenario 55
		testTwoElementList(ABC_listIterRemoveAfterPreviousA_BC, "ABC_listIterRemoveAfterPreviousA_BC_S59", LIST_BC, STRING_BC); //scenario 59
		testTwoElementList(ABC_listIterRemoveAfterPreviousB_AC, "ABC_listIterRemoveAfterPreviousB_AC_S60", LIST_AC, STRING_AC); //scenario 60
		testTwoElementList(ABC_listIterRemoveAfterPreviousC_AB, "ABC_listIterRemoveAfterPreviousC_AB_S61", LIST_AB, STRING_AB); //scenario 61
		//3-element to changed 3-element via set()
		testThreeElementList(ABC_set0D_DBC, "ABC_set0D_DBC_S41", LIST_DBC, STRING_DBC); //scenario 41
		testThreeElementList(ABC_set1D_ADC, "ABC_set1D_ADC_S42", LIST_ADC, STRING_ADC); //scenario 42
		testThreeElementList(ABC_set2D_ABD, "ABC_set2D_ABD_S43", LIST_ABD, STRING_ABD); //scenario 43
		testThreeElementList(ABC_listIterSetDAfterNextA_DBC, "ABC_listIterSetDAfterNextA_DBC_S77", LIST_DBC, STRING_DBC); //scenario 77
		testThreeElementList(ABC_listIterSetDAfterNextB_ADC, "ABC_listIterSetDAfterNextB_ADC_S78", LIST_ADC, STRING_ADC); //scenario 78
		testThreeElementList(ABC_listIterSetDAfterNextC_ABD, "ABC_listIterSetDAfterNextC_ABD_S79", LIST_ABD, STRING_ABD); //scenario 79
		testThreeElementList(ABC_listIterSetDAfterPreviousA_DBC, "ABC_listIterSetDAfterPreviousA_DBC_S80", LIST_DBC, STRING_DBC); //scenario 80
		testThreeElementList(ABC_listIterSetDAfterPreviousB_ADC, "ABC_listIterSetDAfterPreviousB_ADC_S81", LIST_ADC, STRING_ADC); //scenario 81
		testThreeElementList(ABC_listIterSetDAfterPreviousC_ABD, "ABC_listIterSetDAfterPreviousC_ABD_S82", LIST_ABD, STRING_ABD); //scenario 82
		//Iterator concurrency tests
		
		test_IterConcurrency();
		if (SUPPORTS_LIST_ITERATOR) {
			test_ListIterConcurrency();
		}

		// report final verdict
		printFinalSummary();
	}

	//////////////////////////////////////
	// XXX SCENARIO BUILDERS
	//////////////////////////////////////

	/**
	 * Returns a IndexedUnsortedList for the "new empty list" scenario.
	 * Scenario: no list -> constructor -> [ ]
	 * 
	 * NOTE: Comment out cases for any implementations not currently available
	 *
	 * @return a new, empty IndexedUnsortedList
	 */
	private IndexedUnsortedList<Integer> newList() {
		IndexedUnsortedList<Integer> listToUse;
		switch (LIST_TO_USE) {
//		case goodList:
//			listToUse = new GoodList<Integer>();
//			break;
//		case badList:
//			listToUse = new BadList<Integer>();
//			break;
//		case arrayList:
//			listToUse = new IUArrayList<Integer>();
//			break;
//		case singleLinkedList:
//			listToUse = new IUSingleLinkedList<Integer>();
//			break;
		case doubleLinkedList:
			listToUse = new IUDoubleLinkedList<Integer>();
			break;
		default:
			listToUse = null;
		}
		return listToUse;
	}
	// The following creates a "lambda" reference that allows us to pass a scenario
	//  builder method as an argument. You don't need to worry about how it works -
	//  just make sure each scenario building method has a corresponding Scenario 
	//  assignment statement as in these examples. 
	private Scenario<Integer> newList = () -> newList();

	/** Scenario: empty list -> addToFront(A) -> [A] //scenario 2
	 * @return [A] after addToFront(A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addToFrontA_A() {
		IndexedUnsortedList<Integer> list = newList(); 
		list.addToFront(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> emptyList_addToFrontA_A = () -> emptyList_addToFrontA_A();

	/**
	 * Scenario: empty list -> addToRear(A) -> [A] //scenario 3
	 * @return [A] after addToRear(A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addToRearA_A(){
		IndexedUnsortedList<Integer> list = newList();
		list.addToRear(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> emptyList_addToRearA_A = () -> emptyList_addToRearA_A();
	
	/**
	 * Scenario: empty list -> add(A) -> [A] //scenario 4
	 * @return [A] after add(A)
	 */
	private IndexedUnsortedList<Integer> emptyList_addA_A(){
		IndexedUnsortedList<Integer> list = newList();
		list.add(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> emptyList_addA_A = () -> emptyList_addA_A();
	
	/**
	 * Scenario: empty list -> add(0, A) -> [A] //scenario 5
	 * @return [A] after add(0, A)
	 */
	private IndexedUnsortedList<Integer> emptyList_add0A_A(){
		IndexedUnsortedList<Integer> list = newList();
		list.add(0, ELEMENT_A);
		return list;
	}
	private Scenario<Integer> emptyList_add0A_A = () -> emptyList_add0A_A();
	
	/** Scenario: [A] -> addToFront(B) -> [B,A] //scenario 6
	 * @return [B,A] after addToFront(B)
	 */
	private IndexedUnsortedList<Integer> A_addToFrontB_BA() {
		IndexedUnsortedList<Integer> list = emptyList_add0A_A(); 
//		list.addToFront(ELEMENT_B);
		list.add(0, ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_addToFrontB_BA = () -> A_addToFrontB_BA();
	
	/** Scenario: [A] -> addToFront(B) -> [B,A] //scenario 7
	 * @return [A,B] after addToFront(B)
	 */
	private IndexedUnsortedList<Integer> A_addToRearB_AB() {
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A(); 
		list.addToRear(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_addToRearB_AB = () -> A_addToRearB_AB();
	/**
	 *  Scenario: [A] -> addAfter(B,A) -> [A,B] //scenario 8
	 * @return [A,B] after addAfter(B,A)
	 */
	private IndexedUnsortedList<Integer> A_addAfterBA_AB(){
		IndexedUnsortedList<Integer> list = emptyList_add0A_A();
		list.addAfter(ELEMENT_B, ELEMENT_A);
		return list;
	}
	private Scenario<Integer> A_addAfterBA_AB = () -> A_addAfterBA_AB();
	
	/**
	 * Scenario: [A] -> add(B) -> [A,B] //scenario 9
	 * @return [A,B] after add(B)
	 */
	private IndexedUnsortedList<Integer> A_addB_AB(){
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.add(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_addB_AB = () -> A_addB_AB();
	/**
	 * Scenario: [A] -> add(0,B) -> [B,A] //scenario 10
	 * @return [B,A] after add(0,B)
	 */
	private IndexedUnsortedList<Integer> A_add0B_BA(){
		IndexedUnsortedList<Integer> list = emptyList_add0A_A();
		list.add(0, ELEMENT_B);
	
		return list;
	}
	private Scenario<Integer> A_add0B_BA = () -> A_add0B_BA();
	
	/**
	 * Scenario: [A] -> add(1,B) -> [A,B] //scenario 11
	 * @return [A,B] after add(1,B)
	 */
	private IndexedUnsortedList<Integer> A_add1B_AB(){
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.add(1, ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_add1B_AB = () -> A_add1B_AB();
	/**
	 * Scenario: [A] -> removeFirst() -> [] //scenario 12
	 * @return [] after removeFirst
	 */
	private IndexedUnsortedList<Integer> A_removeFirst_emptyList(){
		IndexedUnsortedList<Integer> list = emptyList_add0A_A();
		list.removeFirst();
		return list;
	}
	private Scenario<Integer> A_removeFirst_emptyList = () -> A_removeFirst_emptyList();
	/**
	 * Scenario: [A] -> removeLast() -> [] //scenario 13
	 * @return [] after removeLast
	 */
	private IndexedUnsortedList<Integer> A_removeLast_emptyList(){
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.removeLast();
		return list;
	}
	private Scenario<Integer> A_removeLast_emptyList = () -> A_removeLast_emptyList();
	/**
	 * Scenario: [A] -> remove(A) -> [] //scenario 14
	 * @return [] after remove(A)
	 */
	private IndexedUnsortedList<Integer> A_removeA_emptyList(){
		IndexedUnsortedList<Integer> list = emptyList_add0A_A();
		list.remove(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> A_removeA_emptyList = () -> A_removeA_emptyList();
	/**
	 * Scenario: [A] -> remove(0) -> [] //scenario 15
	 * @return [] after remove(0)
	 */
	private IndexedUnsortedList<Integer> A_remove0_emptyList(){
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.remove(0);
		return list;
	}
	private Scenario<Integer> A_remove0_emptyList = () -> A_remove0_emptyList();
	/**
	 * Scenario: [A] -> set(0,B) -> [B] //scenario 16
	 * @return [B] after set(0,B)
	 */
	private IndexedUnsortedList<Integer> A_set0B_B(){
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		list.set(0, ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_set0B_B = () -> A_set0B_B();
	/**
	 * Scenario: [A,B] -> addToFront(C) -> [C,A,B] //scenario 17
	 * @return [C,A,B] after addToFront(C)
	 */
	private IndexedUnsortedList<Integer> AB_addToFrontC_CAB(){
		IndexedUnsortedList<Integer> list = A_addB_AB();
		list.addToFront(ELEMENT_C);
		
		return list;
	}
	private Scenario<Integer> AB_addToFrontC_CAB = () -> AB_addToFrontC_CAB();
	/**
	 * Scenario: [A,B] -> addToRear(C) -> [A,B,C] //scenario 18
	 * @return [A,B,C] after addToRear(C)
	 */
	private IndexedUnsortedList<Integer> AB_addToRearC_ABC(){
		IndexedUnsortedList<Integer> list = A_add1B_AB();
		list.addToRear(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_addToRearC_ABC = () -> AB_addToRearC_ABC();
	/**
	 * Scenario: [A,B] -> addAfter(C,A) -> [A,C,B] //scenario 19
	 * @return [A,C,B] after addAfter(C,A)
	 */
	private IndexedUnsortedList<Integer> AB_addAfterCA_ACB(){
		IndexedUnsortedList<Integer> list = A_addB_AB();
		list.addAfter(ELEMENT_C, ELEMENT_A);
		return list;
	}
	private Scenario<Integer> AB_addAfterCA_ACB = () -> AB_addAfterCA_ACB();
	/**
	 * Scenario: [A,B] -> addAfter(C,B) -> [A,B,C] //scenario 20
	 * @return [A,B,C] after addAfter(C,B)
	 */
	private IndexedUnsortedList<Integer> AB_addAfterCB_ABC(){
		IndexedUnsortedList<Integer> list = A_add1B_AB();
		list.addAfter(ELEMENT_C, ELEMENT_B);
		return list;
	}
	private Scenario<Integer> AB_addAfterCB_ABC = () -> AB_addAfterCB_ABC();
	/**
	 * Scenario: [A,B] -> add(C) -> [A,B,C] //scenario 21
	 * @return [A,B,C} after add(C)
	 */
	private IndexedUnsortedList<Integer> AB_addC_ABC(){
		IndexedUnsortedList<Integer> list = A_addB_AB();
		list.add(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_addC_ABC = () -> AB_addC_ABC();
	/**
	 * Scenario: [A,B] -> add(0,C) -> [C,A,B] //scenario 22
	 * @return [C,A,B] after add(0,C)
	 */
	private IndexedUnsortedList<Integer> AB_add0C_CAB(){
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.add(0, ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_add0C_CAB = () -> AB_add0C_CAB();
	/**
	 * Scenario: [A,B] -> add(1,C) -> [A,C,B] //scenario 23
	 * @return [A,C,B] after add(1,C)
	 */
	private IndexedUnsortedList<Integer> AB_add1C_ACB(){
		IndexedUnsortedList<Integer> list = A_addB_AB();
		list.add(1, ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_add1C_ACB = () -> AB_add1C_ACB();
	/**
	 * Scenario: [A,B] -> add (2,C) -> [A,B,C] //scenario 24
	 * @return [A,B,C] after add(1,C)
	 */
	private IndexedUnsortedList<Integer> AB_add2C_ABC(){
		IndexedUnsortedList<Integer> list = A_add1B_AB();
		list.add(2, ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_add2C_ABC = () -> AB_add2C_ABC();
	/**
	 * Scenario: [A,B] -> removeFirst() -> [B] //scenario 25
	 * @return [B] after removeFirst()
	 */
	private IndexedUnsortedList<Integer> AB_removeFirst_B(){
		IndexedUnsortedList<Integer> list = A_addB_AB();
		list.removeFirst();
		return list;
	}
	private Scenario<Integer> AB_removeFirst_B = () -> AB_removeFirst_B();
	/**
	 * Scenario: [A,B] -> removeLast() -> [A] //scenario 26
	 * @return [A] after removeLast()
	 */
	private IndexedUnsortedList<Integer> AB_removeLast_A(){
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.removeLast();
		return list;
	}
	private Scenario<Integer> AB_removeLast_A = () -> AB_removeLast_A();
	/**
	 * Scenario: [A,B] -> remove(A) -> [B] //scenario 27
	 * @return [B] after remove(A)
	 */
	private IndexedUnsortedList<Integer> AB_removeA_B(){
		IndexedUnsortedList<Integer> list = A_addB_AB();
		list.remove(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> AB_removeA_B = () -> AB_removeA_B();
	/**
	 * Scenario: [A,B] -> remove(B) -> [A] //scenario 28
	 * @return [A] after remove(B)
	 */
	private IndexedUnsortedList<Integer> AB_removeB_A(){
		IndexedUnsortedList<Integer> list = A_add1B_AB();
		list.remove(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> AB_removeB_A = () -> AB_removeB_A();
	/**
	 * Scenario: [A,B] -> remove(0) -> [B] //scenario 29
	 * @return [B] after remove(0)
	 */
	private IndexedUnsortedList<Integer> AB_remove0_B(){
		IndexedUnsortedList<Integer> list = A_addB_AB();
		list.remove(0);
		return list;
	}
	private Scenario<Integer> AB_remove0_B = () -> AB_remove0_B();
	/**
	 * Scenario: [A,B] -> remove(1) -> [A] //scenario 30
	 * @return [A] after remove(1)
	 */
	private IndexedUnsortedList<Integer> AB_remove1_A(){
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		list.remove(1);
		return list;
	}
	private Scenario<Integer> AB_remove1_A = () -> AB_remove1_A();
	/**
	 * Scenario: [A,B] -> set(0,C) -> [C,B] //scenario 31
	 * @return [C,B] after set(0,C)
	 */
	private IndexedUnsortedList<Integer> AB_set0C_CB(){
		IndexedUnsortedList<Integer> list = A_add1B_AB();
		list.set(0, ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_set0C_CB = () -> AB_set0C_CB();
	/**
	 * Scenario: [A,B] -> set(1,C) -> [A,C] //scenario 32
	 * @return [A,C] after set(1,C)
	 */
	private IndexedUnsortedList<Integer> AB_set1C_AC(){
		IndexedUnsortedList<Integer> list = A_addB_AB();
		list.set(1, ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_set1C_AC = () -> AB_set1C_AC();
	/**
	 * Scenario: [A,B,C] -> removeFirst() -> [B,C] //Scenario 33
	 * @return [B,C] after removeFirst()
	 */
	private IndexedUnsortedList<Integer> ABC_removeFirst_BC(){
		IndexedUnsortedList<Integer> list = AB_addAfterCB_ABC();
		list.removeFirst();
		return list;
	}
	private Scenario<Integer> ABC_removeFirst_BC = () -> ABC_removeFirst_BC();
	/**
	 * Scenario: [A,B,C] -> removeLast() -> [A,B] //scenario 34
	 * @return [A,B] after removeLast()
	 */
	private IndexedUnsortedList<Integer> ABC_removeLast_AB(){
		IndexedUnsortedList<Integer> list = AB_addAfterCB_ABC();
		list.removeLast();
		return list;
	}
	private Scenario<Integer> ABC_removeLast_AB = () -> ABC_removeLast_AB();
	/**
	 * Scenario: [A,B,C] -> remove(A) -> [B,C] //scenario 35
	 * @return [B,C] after remove(A)
	 */
	private IndexedUnsortedList<Integer> ABC_removeA_BC(){
		IndexedUnsortedList<Integer> list = AB_add2C_ABC();
		list.remove(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> ABC_removeA_BC = () -> ABC_removeA_BC();
	/**
	 * Scenario: [A,B,C] -> remove(B) -> [A,C] //scenario 36
	 * @return [A,C] after remove(B)
	 */
	private IndexedUnsortedList<Integer> ABC_removeB_AC(){
		IndexedUnsortedList<Integer> list = AB_addC_ABC();
		list.remove(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> ABC_removeB_AC = () -> ABC_removeB_AC();
	/**
	 * Scenario: [A,B,C] -> remove(C) -> [A,B] //scenario 37
	 * @return [A,B] after remove(C)
	 */
	private IndexedUnsortedList<Integer> ABC_removeC_AB(){
		IndexedUnsortedList<Integer> list = AB_addAfterCB_ABC();
		list.remove(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> ABC_removeC_AB = () -> ABC_removeC_AB();
	/**
	 * Scenario: [A,B,C] -> remove(0) -> [B,C] //scenario 38
	 * @return [B,C] after remove(0)
	 */
	private IndexedUnsortedList<Integer> ABC_remove0_BC(){
		IndexedUnsortedList<Integer> list = AB_addC_ABC();
		list.remove(0);
		return list;
	}
	private Scenario<Integer> ABC_remove0_BC = () -> ABC_remove0_BC();
	/**
	 * Scenario: [A,B,C] -> remove(1) -> [A,C] //scenario 39
	 * @return [A,C] after remove(1)
	 */
	private IndexedUnsortedList<Integer> ABC_remove1_AC(){
		IndexedUnsortedList<Integer> list = AB_addAfterCB_ABC();
		list.remove(1);
		return list;
	}
	private Scenario<Integer> ABC_remove1_AC = () -> ABC_remove1_AC();
	/**
	 * Scenario: [A,B,C] -> remove(2) -> [A,B] //scenario 40
	 * @return [A,B] after remove(2)
	 */
	private IndexedUnsortedList<Integer> ABC_remove2_AB(){
		IndexedUnsortedList<Integer> list = AB_add2C_ABC();
		list.remove(2);
		return list;
	}
	private Scenario<Integer> ABC_remove2_AB = () -> ABC_remove2_AB();
	/**
	 * Scenario: [A,B,C] -> set(0,D) -> [D,B,C] //scenario 41
	 * @return [D,B,C] after set(0,D)
	 */
	private IndexedUnsortedList<Integer> ABC_set0D_DBC(){
		IndexedUnsortedList<Integer> list = AB_addAfterCB_ABC();
		list.set(0, ELEMENT_D);
		return list;
	}
	private Scenario<Integer> ABC_set0D_DBC = () -> ABC_set0D_DBC();
	/**
	 * Scenario: [A,B,C] -> set(1,D) -> [A,D,C] //scenario 42
	 * @return [A,D,C] after set(1,D)
	 */
	private IndexedUnsortedList<Integer> ABC_set1D_ADC(){
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		list.set(1, ELEMENT_D);
		return list;
	}
	private Scenario<Integer> ABC_set1D_ADC = () -> ABC_set1D_ADC();
	/**
	 * Scenario: [A,B,C] -> set(2,D) -> [A,B,D] //scenario 43
	 * @return [A,B,D] after set(2,D)
	 */
	private IndexedUnsortedList<Integer> ABC_set2D_ABD(){
		IndexedUnsortedList<Integer> list = AB_addAfterCB_ABC();
		list.set(2, ELEMENT_D);
		return list;
	}
	private Scenario<Integer> ABC_set2D_ABD = () -> ABC_set2D_ABD();
	/**
	 * Scenario: [A] -> iterator remove() after next() returns A -> [] //scenario 44
	 * @return [] after iteratorRemoveAfterNextA
	 */
	private IndexedUnsortedList<Integer> A_iterRemoveAfterNextA_emptyList(){
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		Iterator<Integer> it = list.iterator();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> A_iterRemoveAfterNextA_emptyList = () -> A_iterRemoveAfterNextA_emptyList();
	/**
	 * Scenario: [A,B] -> iterator remove() after next() returns A -> [B] //scenario 45
	 * @return [B] after iterRemoveAfterNextA
	 */
	private IndexedUnsortedList<Integer> AB_iterRemoveAfterNextA_B(){
		IndexedUnsortedList<Integer> list = ABC_removeLast_AB();
		Iterator<Integer> it = list.iterator();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> AB_iterRemoveAfterNextA_B = () -> AB_iterRemoveAfterNextA_B();
	/**
	 * Scenario: [A,B] -> iterator remove() after next() returns B -> [A] //scenario 46
	 * @return [A] after iterRemoveAfterNextB
	 */
	private IndexedUnsortedList<Integer> AB_iterRemoveAfterNextB_A(){
		IndexedUnsortedList<Integer> list = ABC_removeC_AB();
		Iterator<Integer> it = list.iterator();
		it.next();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> AB_iterRemoveAfterNextB_A = () -> AB_iterRemoveAfterNextB_A();
	/**
	 * Scenario: [A,B,C] -> iterator remove() after next() returns A -> [B,C] //scenario 47
	 * @return [B,C] after iterRemoveAfterNextA
	 */
	private IndexedUnsortedList<Integer> ABC_iterRemoveAfterNextA_BC(){
		IndexedUnsortedList<Integer> list = AB_addAfterCB_ABC();
		Iterator<Integer> it = list.iterator();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> ABC_iterRemoveAfterNextA_BC = () -> ABC_iterRemoveAfterNextA_BC();
	/**
	 * Scenario: [A,B,C] -> iterator remove() after next() returns B -> [A,C] //scenario 48
	 * @return [A,C] after iterRemoveAfterNextB
	 */
	private IndexedUnsortedList<Integer> ABC_iterRemoveAfterNextB_AC(){
		IndexedUnsortedList<Integer> list = AB_addC_ABC();
		Iterator<Integer> it = list.iterator();
		it.next();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> ABC_iterRemoveAfterNextB_AC = () -> ABC_iterRemoveAfterNextB_AC();
	/**
	 * Scenario: [A,B,C] -> iterator remove() after next() returns C -> [A,B] //scenario 49
	 * @return [A,B] after iterRemoveAfterNextC
	 */
	private IndexedUnsortedList<Integer> ABC_iterRemoveAfterNextC_AB(){
		IndexedUnsortedList<Integer> list = AB_addAfterCB_ABC();
		Iterator<Integer> it = list.iterator();
		it.next();
		it.next();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> ABC_iterRemoveAfterNextC_AB = () -> ABC_iterRemoveAfterNextC_AB();
	/**
	 * Scenario: [A] -> List Iterator remove() after next() returns A -> [] //scenario 50
	 * @return [] after listIterRemoveAfterNextA
	 */
	private IndexedUnsortedList<Integer> A_listIterRemoveAfterNextA_emptyList(){
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> A_listIterRemoveAfterNextA_emptyList = () -> A_listIterRemoveAfterNextA_emptyList();
	/**
	 * Scenario: [A,B] -> list iterator remove() after next() returns A -> [B] //scenario 51
	 * @return [B] after listIterRemoveAfterNextA
	 */
	private IndexedUnsortedList<Integer> AB_listIterRemoveAfterNextA_B(){
		IndexedUnsortedList<Integer> list = ABC_removeLast_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> AB_listIterRemoveAfterNextA_B = () -> AB_listIterRemoveAfterNextA_B();
	/**
	 * Scenario: [A,B] -> list iterator remove() after next() returns B -> [A] //scenario 52
	 * @return [A] after listIterRemoveAfterNextB
	 */
	private IndexedUnsortedList<Integer> AB_listIterRemoveAfterNextB_A(){
		IndexedUnsortedList<Integer> list = ABC_removeC_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> AB_listIterRemoveAfterNextB_A = () -> AB_listIterRemoveAfterNextB_A();
	/**
	 * Scenario: [A,B,C] -> list iterator remove() after next() returns A -> [B,C] //scenario 53
	 * @return [B,C] after listIterRemoveAfterNextA
	 */
	private IndexedUnsortedList<Integer> ABC_listIterRemoveAfterNextA_BC(){
		IndexedUnsortedList<Integer> list = AB_addAfterCB_ABC();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> ABC_listIterRemoveAfterNextA_BC = () -> ABC_listIterRemoveAfterNextA_BC();
	/**
	 * Scenario: [A,B,C] -> list iterator remove() after next() returns B -> [A,C] //scenario 54
	 * @return [A,C] after listIterRemoveAfterNextB
	 */
	private IndexedUnsortedList<Integer> ABC_listIterRemoveAfterNextB_AC(){
		IndexedUnsortedList<Integer> list = AB_add2C_ABC();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> ABC_listIterRemoveAfterNextB_AC = () -> ABC_listIterRemoveAfterNextB_AC();
	/**
	 * Scenario: [A,B,C] -> list iterator remove() after next() returns C -> [A,B] //scenario 55
	 * @return [A,B] after listIterRemoveAfterNextC
	 */
	private IndexedUnsortedList<Integer> ABC_listIterRemoveAfterNextC_AB(){
		IndexedUnsortedList<Integer> list = AB_addAfterCB_ABC();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.next();
		it.remove();
		return list;
	}
	private Scenario<Integer> ABC_listIterRemoveAfterNextC_AB = () -> ABC_listIterRemoveAfterNextC_AB();
	/**
	 * Scenario: [A] -> list iterator remove() after previous() returns A -> [] //scenario 56
	 * @return [] after listIterRemoveAfterPreviousA
	 */
	private IndexedUnsortedList<Integer> A_listIterRemoveAfterPreviousA_emptyList(){
		IndexedUnsortedList<Integer> list = emptyList_addA_A();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.previous();
		it.remove();
		return list;
	}
	private Scenario<Integer> A_listIterRemoveAfterPreviousA_emptyList = () -> A_listIterRemoveAfterPreviousA_emptyList();
	/**
	 * Scenario: [A,B] -> list iterator remove() after previous() returns A -> [B] //scenario 57
	 * @return [B] after listIterRemoveAfterPreviousA
	 */
	private IndexedUnsortedList<Integer> AB_listIterRemoveAfterPreviousA_B(){
		IndexedUnsortedList<Integer> list = A_add1B_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.previous();
		it.remove();
		return list;
	}
	private Scenario<Integer> AB_listIterRemoveAfterPreviousA_B = () -> AB_listIterRemoveAfterPreviousA_B();
	/**
	 * Scenario: [A,B] -> list iterator remove() after previous() returns B -> [A] //scenario 58
	 * @return [A] after listIterRemoveAfterPreviousB
	 */
	private IndexedUnsortedList<Integer> AB_listIterRemoveAfterPreviousB_A(){
		IndexedUnsortedList<Integer> list = A_addB_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.previous();
		it.remove();
		return list;
	}
	private Scenario<Integer> AB_listIterRemoveAfterPreviousB_A = () -> AB_listIterRemoveAfterPreviousB_A();
	/**
	 * Scenario: [A,B,C] -> list iterator remove() after previous() returns A -> [B,C] //scenario 59
	 * @return [B,C] after listIterRemoveAfterPreviousA
	 */
	private IndexedUnsortedList<Integer> ABC_listIterRemoveAfterPreviousA_BC(){
		IndexedUnsortedList<Integer> list = AB_addAfterCB_ABC();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.previous();
		it.remove();
		return list;
	}
	private Scenario<Integer> ABC_listIterRemoveAfterPreviousA_BC = () -> ABC_listIterRemoveAfterPreviousA_BC();
	/**
	 * Scenario: [A,B,C] -> list iterator remove() after previous() returns B -> [A,C] //scenario 60
	 * @return [A,C] after listIterRemoveAfterPreviousB
	 */
	private IndexedUnsortedList<Integer> ABC_listIterRemoveAfterPreviousB_AC(){
		IndexedUnsortedList<Integer> list = AB_add2C_ABC();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.previous();
		it.remove();
		return list;
		
	}
	private Scenario<Integer> ABC_listIterRemoveAfterPreviousB_AC = () -> ABC_listIterRemoveAfterPreviousB_AC();
	/**
	 * Scenario: [A,B,C] -> list iterator remove() after previous() returns C -> [A,B] //scenario 61
	 * @return [A,B] after listIterRemoveAfterPreviousC
	 */
	private IndexedUnsortedList<Integer> ABC_listIterRemoveAfterPreviousC_AB(){
		IndexedUnsortedList<Integer> list = AB_addAfterCB_ABC();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.next();
		it.previous();
		it.remove();
		return list;
	}
	private Scenario<Integer> ABC_listIterRemoveAfterPreviousC_AB = () -> ABC_listIterRemoveAfterPreviousC_AB();
	/**
	 * Scenario: [] -> list iterator addA -> [A] //scenario 62
	 * @return [A] after list iterator addA
	 */
	private IndexedUnsortedList<Integer> emptyList_listIterAddA_A(){
		IndexedUnsortedList<Integer> list = newList();
		ListIterator<Integer> it = list.listIterator();
		it.add(ELEMENT_A);
		return list;
	}
	private Scenario<Integer> emptyList_listIterAddA_A = () -> emptyList_listIterAddA_A();
	/**
	 * Scenario: [A] -> list iterator addB with new iterator -> [B,A] //scenario 63
	 * @return [B,A] after new iterator
	 */
	private IndexedUnsortedList<Integer> A_listIterAddBNewIter_BA(){
		IndexedUnsortedList<Integer> list = emptyList_addA_A();
		ListIterator<Integer> it = list.listIterator();
		
		it.add(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_listIterAddBNewIter_BA = () -> A_listIterAddBNewIter_BA();
	/**
	 * Scenario: [A] -> list iterator addB after next returns A -> [A,B] //scenario 64
	 * @return [A,B] after addB after next returns A
	 */
	private IndexedUnsortedList<Integer> A_listIterAddBAfterNextA_AB(){
		IndexedUnsortedList<Integer> list = emptyList_add0A_A();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.add(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_listIterAddBAfterNextA_AB = () -> A_listIterAddBAfterNextA_AB();
	/**
	 * Scenario: [A] -> list iterator addB after previous returns A -> [B,A] //scenario 65
	 * @return [B,A] after addB after previous returns A
	 */
	private IndexedUnsortedList<Integer> A_listIterAddBAfterPreviousA_BA(){
		IndexedUnsortedList<Integer> list = emptyList_addA_A();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.previous();
		it.add(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_listIterAddBAfterPreviousA_BA = () -> A_listIterAddBAfterPreviousA_BA();
	/**
	 * Scenario: [A,B] -> list iterator addC with new Iterator -> [C,A,B] //scenario 66
	 * @return [C,A,B] after addC with new iterator
	 */
	private IndexedUnsortedList<Integer> AB_listIterAddCNewIter_CAB(){
		IndexedUnsortedList<Integer> list = A_add1B_AB();
		ListIterator<Integer> it = list.listIterator();
		
		it.add(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_listIterAddCNewIter_CAB = () -> AB_listIterAddCNewIter_CAB();
	/**
	 * Scenario: [A,B] -> list iterator addC after next returns A -> [A,C,B] //scenario 67
	 * @return [A,C,B] after addC after next returns A
	 */
	private IndexedUnsortedList<Integer> AB_listIterAddCAfterNextA_ACB(){
		IndexedUnsortedList<Integer> list = A_addB_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.add(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_listIterAddCAfterNextA_ACB = () -> AB_listIterAddCAfterNextA_ACB();
	/**
	 * Scenario:  [A,B] -> iterator add(C) after next() returns B -> [A,B,C] //scenario 68
	 * @return [A,B,C] after iterator addC after next() returns B
	 */
	private IndexedUnsortedList<Integer> AB_listIterAddCAfterNextB_ABC(){
		IndexedUnsortedList<Integer> list = A_add1B_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.add(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_listIterAddCAfterNextB_ABC = () -> AB_listIterAddCAfterNextB_ABC();
	/**
	 * Scenario: [A,B] -> iterator add(C) after previous() returns A -> [C,A,B] //scenario 69
	 * @return [C,A,B] after addC after previous returns A
	 */
	private IndexedUnsortedList<Integer> AB_listIterAddCAfterPreviousA_CAB(){
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.previous();
		it.add(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_listIterAddCAfterPreviousA_CAB = () -> AB_listIterAddCAfterPreviousA_CAB();
	/**
	 * Scenario:  [A,B] -> iterator add(C) after previous() returns B -> [A,C,B] //scenario 70
	 * @return [A,C,B] after addC after previous returns B
	 */
	private IndexedUnsortedList<Integer> AB_listIterAddCAfterPreviousB_ACB(){
		IndexedUnsortedList<Integer> list = A_add1B_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.previous();
		it.add(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_listIterAddCAfterPreviousB_ACB = () -> AB_listIterAddCAfterPreviousB_ACB();
	/**
	 * Scenario: [A] -> iterator set(B) after next() returns A -> [B] //scenario 71
	 * @return [B] after setB after next returns A
	 */
	private IndexedUnsortedList<Integer> A_listIterSetBAfterNextA_B(){
		IndexedUnsortedList<Integer> list = emptyList_addA_A();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.set(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_listIterSetBAfterNextA_B = () -> A_listIterSetBAfterNextA_B();
	/**
	 * Scenario: [A] -> iterator set(B) after previous() returns A -> [B] //scenario 72
	 * @return [B] after setB after previous returns A
	 */
	private IndexedUnsortedList<Integer> A_listIterSetBAfterPreviousA_B(){
		IndexedUnsortedList<Integer> list = emptyList_addToFrontA_A();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.previous();
		it.set(ELEMENT_B);
		return list;
	}
	private Scenario<Integer> A_listIterSetBAfterPreviousA_B = () -> A_listIterSetBAfterPreviousA_B();
	/**
	 * Scenario: [A,B] -> iterator set(C) after next() returns A -> [C,B] //scenario 73
	 * @return [C,B] after setC after next returns A
	 */
	private IndexedUnsortedList<Integer> AB_listIterSetCAfterNextA_CB(){
		IndexedUnsortedList<Integer> list = A_add1B_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.set(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_listIterSetCAfterNextA_CB = () -> AB_listIterSetCAfterNextA_CB();
	/**
	 * Scenario: [A,B] -> iterator set(C) after next() returns B -> [A,C] //scenario 74
	 * @return [A,C] after setC after next returns B
	 */
	private IndexedUnsortedList<Integer> AB_listIterSetCAfterNextB_AC(){
		IndexedUnsortedList<Integer> list = A_addB_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.set(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_listIterSetCAfterNextB_AC = () -> AB_listIterSetCAfterNextB_AC();
	/**
	 * Scenario: [A,B] -> iterator set(C) after previous() returns A -> [C,B] //scenario 75
	 * @return [C,B] after setC after previous returns A
	 */
	private IndexedUnsortedList<Integer> AB_listIterSetCAfterPreviousA_CB(){
		IndexedUnsortedList<Integer> list = A_add1B_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.previous();
		it.set(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_listIterSetCAfterPreviousA_CB = () -> AB_listIterSetCAfterPreviousA_CB();
	/**
	 * Scenario: [A,B] -> iterator set(C) after previous() returns B -> [A,C] //scenario 76
	 * @return [A,C] after setC after previous returns B
	 */
	private IndexedUnsortedList<Integer> AB_listIterSetCAfterPreviousB_AC(){
		IndexedUnsortedList<Integer> list = A_addToRearB_AB();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.previous();
		it.set(ELEMENT_C);
		return list;
	}
	private Scenario<Integer> AB_listIterSetCAfterPreviousB_AC = () -> AB_listIterSetCAfterPreviousB_AC();
	/**
	 * Scenario: [A,B,C] -> iterator set(D) after next() returns A -> [D,B,C] //scenario 77
	 * @return [D,B,C] after setD after next returns A
	 */
	private IndexedUnsortedList<Integer> ABC_listIterSetDAfterNextA_DBC(){
		IndexedUnsortedList<Integer> list = AB_add2C_ABC();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.set(ELEMENT_D);
		return list;
	}
	private Scenario<Integer> ABC_listIterSetDAfterNextA_DBC = () -> ABC_listIterSetDAfterNextA_DBC();
	/**
	 * Scenario: [A,B,C] -> iterator set(D) after next() returns B -> [A,D,C] //scenario 78
	 * @return [A,D,C] after setD after next returns B
	 */
	private IndexedUnsortedList<Integer> ABC_listIterSetDAfterNextB_ADC(){
		IndexedUnsortedList<Integer> list = AB_addC_ABC();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.set(ELEMENT_D);
		return list;
	}
	private Scenario<Integer> ABC_listIterSetDAfterNextB_ADC = () -> ABC_listIterSetDAfterNextB_ADC();
	/**
	 * Scenario: [A,B,C] -> iterator set(D) after next() returns C -> [A,B,D] //scenario 79
	 * @return [A,B,D] after setD after next returns C
	 */
	private IndexedUnsortedList<Integer> ABC_listIterSetDAfterNextC_ABD(){
		IndexedUnsortedList<Integer> list = AB_add2C_ABC();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.next();
		it.set(ELEMENT_D);
		return list;
	}
	private Scenario<Integer> ABC_listIterSetDAfterNextC_ABD = () -> ABC_listIterSetDAfterNextC_ABD();
	/**
	 * Scenario: [A,B,C] -> iterator set(D) after previous() returns A -> [D,B,C] //scenario 80
	 * @return [D,B,C] after setD after previous returns A
	 */
	private IndexedUnsortedList<Integer> ABC_listIterSetDAfterPreviousA_DBC(){
		IndexedUnsortedList<Integer> list = AB_addToRearC_ABC();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.previous();
		it.set(ELEMENT_D);
		return list;
	}
	private Scenario<Integer> ABC_listIterSetDAfterPreviousA_DBC = () -> ABC_listIterSetDAfterPreviousA_DBC();
	/**
	 * Scenario: [A,B,C] -> iterator set(D) after previous() returns B -> [A,D,C] //scenario 81
	 * @return [A,D,C] after setD after previous returns B
	 */
	private IndexedUnsortedList<Integer> ABC_listIterSetDAfterPreviousB_ADC(){
		IndexedUnsortedList<Integer> list = AB_add2C_ABC();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.previous();
		it.set(ELEMENT_D);
		return list;
	}
	private Scenario<Integer> ABC_listIterSetDAfterPreviousB_ADC = () -> ABC_listIterSetDAfterPreviousB_ADC();
	/**
	 * Scenario: [A,B,C] -> iterator set(D) after previous() returns C -> [A,B,D] //scenario 82
	 * @return [A,B,D] after setD after previous returns C
	 */
	private IndexedUnsortedList<Integer> ABC_listIterSetDAfterPreviousC_ABD(){
		IndexedUnsortedList<Integer> list = AB_addC_ABC();
		ListIterator<Integer> it = list.listIterator();
		it.next();
		it.next();
		it.next();
		it.previous();
		it.set(ELEMENT_D);
		return list;
	}
	private Scenario<Integer> ABC_listIterSetDAfterPreviousC_ABD = () -> ABC_listIterSetDAfterPreviousC_ABD();
	/////////////////////////////////
	//XXX Tests for 0-element list
	/////////////////////////////////
	
	/** Run all tests on scenarios resulting in an empty list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 */
	private void testEmptyList(Scenario<Integer> scenario, String scenarioName) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// IndexedUnsortedList
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.True));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 0));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddX", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0", testRemoveIndex(scenario.build(), 0, null, Result.IndexOutOfBounds));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.False));
			printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), null, Result.NoSuchElement));
			printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));
			// ListIterator
			if (SUPPORTS_LIST_ITERATOR) {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
			    printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
			    printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
			    printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.IndexOutOfBounds));
			    printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.False));
			    printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), null, Result.NoSuchElement));
			    printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
			    printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
			    printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
			    printTest(scenarioName + "_testListIterAdd", testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
			    printTest(scenarioName + "_testListIterSet", testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
			    printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
			    printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
			    printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
			    printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}
	
	//////////////////////////////////
	//XXX Tests for 1-element list
	//////////////////////////////////
	
	/** Run all tests on scenarios resulting in a single element list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 * @param contents elements expected in the list after scenario has been set up
	 * @param contentsString contains character labels corresponding to values in contents
	 */
	private void testSingleElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents, String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			// IndexedUnsortedList
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(0), testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0), testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 1));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0), testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2", testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0), testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0", testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove1", testRemoveIndex(scenario.build(), 1, null, Result.IndexOutOfBounds));
			// Iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 1), Result.False));
			printTest(scenarioName + "_iterNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 1), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			printTest(scenarioName + "_iterNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.False));
			printTest(scenarioName + "_iterNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextRemove_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			// ListIterator
			if (SUPPORTS_LIST_ITERATOR) {
				  printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
			      printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
			      printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
			      printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
			      printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.IndexOutOfBounds));
			      printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.True));
			      printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
			      printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
			      printTest(scenarioName + "_testListIterAdd", testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterSet", testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIterNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
			      printTest(scenarioName + "_testListIterNextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterNextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIterNextRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)), Result.IllegalState));
			      printTest(scenarioName + "_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)), Result.IllegalState));
			      printTest(scenarioName + "_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter0HasNext", testIterHasNext(scenario.build().listIterator(0), Result.True));
			      printTest(scenarioName + "_testListIter0Next", testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0HasPrevious", testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
			      printTest(scenarioName + "_testListIter0Previous", testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0Remove", testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
			      printTest(scenarioName + "_testListIter0Add", testListIterAdd(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0Set", testListIterSet(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIter0NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter0NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter1HasNext", testIterHasNext(scenario.build().listIterator(1), Result.False));
			      printTest(scenarioName + "_testListIter1Next", testIterNext(scenario.build().listIterator(1), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIter1NextIndex", testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1HasPrevious", testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
			      printTest(scenarioName + "_testListIter1Previous", testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1Remove", testIterRemove(scenario.build().listIterator(1), Result.IllegalState));
			      printTest(scenarioName + "_testListIter1Add", testListIterAdd(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1Set", testListIterSet(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIter1PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter1PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter1PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter1PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	/////////////////////////////////
	//XXX Tests for 2-element list
	/////////////////////////////////
	
	/** Run all tests on scenarios resulting in a two-element list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 * @param contents elements expected in the list after scenario has been set up
	 * @param contentsString contains character labels corresponding to values in contents 
	 */
	private void testTwoElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents, String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(0), testRemoveElement(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(1), testRemoveElement(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0), testContains(scenario.build(), contents[0], Result.True));
			printTest(scenarioName + "_testContains" + contentsString.charAt(1), testContains(scenario.build(), contents[1], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 2));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0), testAddAfter(scenario.build(), contents[0], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(1), testAddAfter(scenario.build(), contents[1], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2", testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex3", testAddAtIndex(scenario.build(), 3, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet2", testSet(scenario.build(), 2, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testGet2", testGet(scenario.build(), 2, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0), testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(1), testIndexOf(scenario.build(), contents[1], 1));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0", testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove1", testRemoveIndex(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove2", testRemoveIndex(scenario.build(), 2, null, Result.IndexOutOfBounds));
			// Iterator
			//newly initialized iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));
			//iterator after one call to next()
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 1), Result.True));
			printTest(scenarioName + "_iterNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 1), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			//iterator after one call to next() followed by remove()
			printTest(scenarioName + "_iterNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.True));
			printTest(scenarioName + "_iterNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNextRemove_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			//iterator after two consecutive calls to next
			printTest(scenarioName + "_iterNext_testIterHasNext2", testIterHasNext(iterAfterNext(scenario.build(), 2), Result.False));
			printTest(scenarioName + "_iterNext_testIterNext2", testIterNext(iterAfterNext(scenario.build(), 2), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNext_testIterRemove2", testIterRemove(iterAfterNext(scenario.build(), 2), Result.NoException));
			//iterator after two consecutive calls to next followed by remove
			printTest(scenarioName + "_iterNextRemove_testIterHasNext2", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.False));
			printTest(scenarioName + "_iterNextRemove_testIterNext2", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextRemove_testIterRemove2", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.IllegalState));
			//iterator after one call to next(), remove(), then next()
			printTest(scenarioName + "_iterNextRemoveNext_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNextRemove(scenario.build(), 1)), Result.False));
			printTest(scenarioName + "_iterNextRemoveNext_testIterNext", testIterNext(iterAfterRemove(iterAfterNextRemove(scenario.build(), 1)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextRemoveNext_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNextRemove(scenario.build(), 1)), Result.IllegalState));
			if (SUPPORTS_LIST_ITERATOR) {
				//TODO: update from single to double
				  printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
			      printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
			      printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
			      printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
			      printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.NoException));
			      printTest(scenarioName + "_testListIter3", testListIter(scenario.build(), 3, Result.IndexOutOfBounds));
			     
			      //without starting index
			      printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.True));
			      printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
			      printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
			      printTest(scenarioName + "_testListIterAdd", testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterSet", testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIterNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
			      printTest(scenarioName + "_testListIterNextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterNextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIterNextRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)), Result.IllegalState));
			      printTest(scenarioName + "_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)), Result.IllegalState));
			      printTest(scenarioName + "_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));			
			      
			      //with starting index
			      printTest(scenarioName + "_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter0HasNext", testIterHasNext(scenario.build().listIterator(0), Result.True));
			      printTest(scenarioName + "_testListIter0Next", testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0HasPrevious", testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
			      printTest(scenarioName + "_testListIter0Previous", testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0Remove", testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
			      printTest(scenarioName + "_testListIter0Add", testListIterAdd(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0Set", testListIterSet(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIter0NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter0NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));
			      //starting index 1
			      printTest(scenarioName + "_testListIter1HasNext", testIterHasNext(scenario.build().listIterator(1), Result.True));
			      printTest(scenarioName + "_testListIter1Next", testIterNext(scenario.build().listIterator(1), contents[1], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1NextIndex", testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1HasPrevious", testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
			      printTest(scenarioName + "_testListIter1Previous", testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1Remove", testIterRemove(scenario.build().listIterator(1), Result.IllegalState));
			      printTest(scenarioName + "_testListIter1Add", testListIterAdd(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1Set", testListIterSet(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIter1PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter1PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter1PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter1PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));
			      
			      //starting index 2
			      printTest(scenarioName + "_testListIter2HasNext", testIterHasNext(scenario.build().listIterator(2), Result.False));
			      printTest(scenarioName + "_testListIter2Next", testIterNext(scenario.build().listIterator(2), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIter2NextIndex", testListIterNextIndex(scenario.build().listIterator(2), 2, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter2HasPrevious", testListIterHasPrevious(scenario.build().listIterator(2), Result.True));
			      printTest(scenarioName + "_testListIter2Previous", testListIterPrevious(scenario.build().listIterator(2), contents[1], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter2PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(2), 1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter2Remove", testIterRemove(scenario.build().listIterator(2), Result.IllegalState));
			      printTest(scenarioName + "_testListIter2Add", testListIterAdd(scenario.build().listIterator(2), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter2Set", testListIterSet(scenario.build().listIterator(2), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIter2PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(2), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter2PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter2PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter2PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter2PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter2PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	//////////////////////////////////
	//XXX Tests for 3-element list
	//////////////////////////////////
	
	/** Run all tests on scenarios resulting in a three-element list
	 * @param scenario lambda reference to scenario builder method
	 * @param scenarioName name of the scenario being tested
	 * @param contents elements expected in the list after scenario has been set up
	 * @param contentsString contains character labels corresponding to values in contents 
	 */
	private void testThreeElementList(Scenario<Integer> scenario, String scenarioName, Integer[] contents, String contentsString) {
		System.out.printf("\nSCENARIO: %s\n\n", scenarioName);
		try {
			//TODO: tests for scenarios resulting in a 3-element list
			//TODO: add tests for all indexes
			printTest(scenarioName + "_testRemoveFirst", testRemoveFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveLast", testRemoveLast(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(0), testRemoveElement(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(1), testRemoveElement(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemove" + contentsString.charAt(2), testRemoveElement(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemoveX", testRemoveElement(scenario.build(), ELEMENT_X, Result.NoSuchElement));
			printTest(scenarioName + "_testFirst", testFirst(scenario.build(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testLast", testLast(scenario.build(), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testContains" + contentsString.charAt(0), testContains(scenario.build(), contents[2], Result.True));
			printTest(scenarioName + "_testContainsX", testContains(scenario.build(), ELEMENT_X, Result.False));
			printTest(scenarioName + "_testIsEmpty", testIsEmpty(scenario.build(), Result.False));
			printTest(scenarioName + "_testSize", testSize(scenario.build(), 3));
			printTest(scenarioName + "_testToString", testToString(scenario.build(), Result.ValidString));
			printTest(scenarioName + "_testAddToFront", testAddToFront(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddToRear", testAddToRear(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfter" + contentsString.charAt(0), testAddAfter(scenario.build(), contents[2], ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAfterX", testAddAfter(scenario.build(), ELEMENT_X, ELEMENT_Z, Result.NoSuchElement));
			printTest(scenarioName + "_testAddAtIndexNeg1", testAddAtIndex(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAddAtIndex0", testAddAtIndex(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex1", testAddAtIndex(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex2", testAddAtIndex(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex3", testAddAtIndex(scenario.build(), 3, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testAddAtIndex4", testAddAtIndex(scenario.build(), 4, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSetNeg1", testSet(scenario.build(), -1, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testSet0", testSet(scenario.build(), 0, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet1", testSet(scenario.build(), 1, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet2", testSet(scenario.build(), 2, ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testSet3", testSet(scenario.build(), 3, ELEMENT_X, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testAdd", testAdd(scenario.build(), ELEMENT_X, Result.NoException));
			printTest(scenarioName + "_testGetNeg1", testGet(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testGet0", testGet(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testGet1", testGet(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testGet2", testGet(scenario.build(), 2, contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testGet3", testGet(scenario.build(), 3, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(0), testIndexOf(scenario.build(), contents[0], 0));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(1), testIndexOf(scenario.build(), contents[1], 1));
			printTest(scenarioName + "_testIndexOf" + contentsString.charAt(2), testIndexOf(scenario.build(), contents[2], 2));
			printTest(scenarioName + "_testIndexOfX", testIndexOf(scenario.build(), ELEMENT_X, -1));
			printTest(scenarioName + "_testRemoveNeg1", testRemoveIndex(scenario.build(), -1, null, Result.IndexOutOfBounds));
			printTest(scenarioName + "_testRemove0", testRemoveIndex(scenario.build(), 0, contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testRemove1", testRemoveIndex(scenario.build(), 1, contents[1], Result.MatchingValue));
			printTest(scenarioName + "_testRemove2", testRemoveIndex(scenario.build(), 2, contents[2], Result.MatchingValue));
			printTest(scenarioName + "_testRemove3", testRemoveIndex(scenario.build(), 3, null, Result.IndexOutOfBounds));
			// Iterator
			//Newly Initialized iterator
			printTest(scenarioName + "_testIter", testIter(scenario.build(), Result.NoException));
			printTest(scenarioName + "_testIterHasNext", testIterHasNext(scenario.build().iterator(), Result.True));
			printTest(scenarioName + "_testIterNext", testIterNext(scenario.build().iterator(), contents[0], Result.MatchingValue));
			printTest(scenarioName + "_testIterRemove", testIterRemove(scenario.build().iterator(), Result.IllegalState));
			//Iterator after one call to next()
			printTest(scenarioName + "_iterNext_testIterHasNext", testIterHasNext(iterAfterNext(scenario.build(), 1), Result.True));
			printTest(scenarioName + "_iterNext_testIterNext", testIterNext(iterAfterNext(scenario.build(), 1), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNext_testIterRemove", testIterRemove(iterAfterNext(scenario.build(), 1), Result.NoException));
			//Iterator after one call to next() followed by remove()
			printTest(scenarioName + "_iterNextRemove_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.True));
			printTest(scenarioName + "_iterNextRemove_testIterNext", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 1)), contents[1], Result.MatchingValue));
			printTest(scenarioName + "_iterNextRemove_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 1)), Result.IllegalState));
			//Iterator after one call to next() followed by remove() followed by next()
			printTest(scenarioName + "_iterNextRemoveNext_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNextRemove(scenario.build(), 1)), Result.True));
			printTest(scenarioName + "_iterNextRemoveNext_testIterNext", testIterNext(iterAfterRemove(iterAfterNextRemove(scenario.build(), 1)), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_iterNextRemoveNext_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNextRemove(scenario.build(), 1)), Result.IllegalState));
			//Iterator after two consecutive calls to next()
			printTest(scenarioName + "_iterNext_testIterHasNext2", testIterHasNext(iterAfterNext(scenario.build(), 2), Result.True));
			printTest(scenarioName + "_iterNext_testIterNext2", testIterNext(iterAfterNext(scenario.build(), 2), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_iterNext_testIterRemove2", testIterRemove(iterAfterNext(scenario.build(), 2), Result.NoException));
			//Iterator after two consecutive calls to next then remove()
			printTest(scenarioName + "_iterNextRemove_testIterHasNext2", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.True));
			printTest(scenarioName + "_iterNextRemove_testIterNext2", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 2)), contents[2], Result.MatchingValue));
			printTest(scenarioName + "_iterNextRemove_testIterRemove2", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 2)), Result.IllegalState));
			//Iterator after two calls to next then remove() then next()
			printTest(scenarioName + "_iterNextNextRemoveNext_testIterHasNext", testIterHasNext(iterAfterRemove(iterAfterNextRemove(scenario.build(), 2)), Result.False));
			printTest(scenarioName + "_iterNextNextRemoveNext_testIterNext", testIterNext(iterAfterRemove(iterAfterNextRemove(scenario.build(), 2)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextNextRemoveNext_testIterRemove", testIterRemove(iterAfterRemove(iterAfterNextRemove(scenario.build(), 2)), Result.IllegalState));
			//Iterator after three consecutive calls to next()
			printTest(scenarioName + "_iterNext_testIterHasNext3", testIterHasNext(iterAfterNext(scenario.build(), 3), Result.False));
			printTest(scenarioName + "_iterNext_testIterNext3", testIterNext(iterAfterNext(scenario.build(), 3), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNext_testIterRemove3", testIterRemove(iterAfterNext(scenario.build(), 3), Result.NoException));
			//Iterator after three consecutive calls to next() followed by remove()
			printTest(scenarioName + "_iterNextRemove_testIterHasNext3", testIterHasNext(iterAfterRemove(iterAfterNext(scenario.build(), 3)), Result.False));
			printTest(scenarioName + "_iterNextRemove_testIterNext3", testIterNext(iterAfterRemove(iterAfterNext(scenario.build(), 3)), null, Result.NoSuchElement));
			printTest(scenarioName + "_iterNextRemove_testIterRemove3", testIterRemove(iterAfterRemove(iterAfterNext(scenario.build(), 3)), Result.IllegalState));
			
			if (SUPPORTS_LIST_ITERATOR) {
				//TODO: update from two to three elements
				  printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.NoException));
			      printTest(scenarioName + "_testListIterNeg1", testListIter(scenario.build(), -1, Result.IndexOutOfBounds));
			      printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.NoException));
			      printTest(scenarioName + "_testListIter1", testListIter(scenario.build(), 1, Result.NoException));
			      printTest(scenarioName + "_testListIter2", testListIter(scenario.build(), 2, Result.NoException));
			      printTest(scenarioName + "_testListIter3", testListIter(scenario.build(), 3, Result.NoException));
			      printTest(scenarioName + "_testListIter4", testListIter(scenario.build(), 4, Result.IndexOutOfBounds));
			     
			      //without starting index
			      printTest(scenarioName + "_testListIterHasNext", testIterHasNext(scenario.build().listIterator(), Result.True));
			      printTest(scenarioName + "_testListIterNext", testIterNext(scenario.build().listIterator(), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIterNextIndex", testListIterNextIndex(scenario.build().listIterator(), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIterHasPrevious", testListIterHasPrevious(scenario.build().listIterator(), Result.False));
			      printTest(scenarioName + "_testListIterPrevious", testListIterPrevious(scenario.build().listIterator(), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIterPreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(), -1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIterRemove", testIterRemove(scenario.build().listIterator(), Result.IllegalState));
			      printTest(scenarioName + "_testListIterAdd", testListIterAdd(scenario.build().listIterator(), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterSet", testListIterSet(scenario.build().listIterator(), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIterNextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(), 1), Result.NoException));
			      printTest(scenarioName + "_testListIterNextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIterNextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIterNextRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterNext(scenario.build().listIterator(), 1)), Result.IllegalState));
			      printTest(scenarioName + "_testListIterNextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIterNextPreviousRemoveRemove", testIterRemove(listIterAfterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1)), Result.IllegalState));
			      printTest(scenarioName + "_testListIterNextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));			
			      
			      //with starting index
			      printTest(scenarioName + "_testListIterNextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(), 1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter0HasNext", testIterHasNext(scenario.build().listIterator(0), Result.True));
			      printTest(scenarioName + "_testListIter0Next", testIterNext(scenario.build().listIterator(0), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0NextIndex", testListIterNextIndex(scenario.build().listIterator(0), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0HasPrevious", testListIterHasPrevious(scenario.build().listIterator(0), Result.False));
			      printTest(scenarioName + "_testListIter0Previous", testListIterPrevious(scenario.build().listIterator(0), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIter0PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(0), -1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter0Remove", testIterRemove(scenario.build().listIterator(0), Result.IllegalState));
			      printTest(scenarioName + "_testListIter0Add", testListIterAdd(scenario.build().listIterator(0), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0Set", testListIterSet(scenario.build().listIterator(0), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIter0NextRemove", testIterRemove(listIterAfterNext(scenario.build().listIterator(0), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter0NextAdd", testListIterAdd(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0NextSet", testListIterSet(listIterAfterNext(scenario.build().listIterator(0), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter0NextPreviousRemove", testIterRemove(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter0NextPreviousAdd", testListIterAdd(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter0NextPreviousSet", testListIterSet(listIterAfterPrevious(listIterAfterNext(scenario.build().listIterator(0), 1), 1), ELEMENT_X, Result.NoException));
			      //starting index 1
			      printTest(scenarioName + "_testListIter1HasNext", testIterHasNext(scenario.build().listIterator(1), Result.True));
			      printTest(scenarioName + "_testListIter1Next", testIterNext(scenario.build().listIterator(1), contents[1], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1NextIndex", testListIterNextIndex(scenario.build().listIterator(1), 1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1HasPrevious", testListIterHasPrevious(scenario.build().listIterator(1), Result.True));
			      printTest(scenarioName + "_testListIter1Previous", testListIterPrevious(scenario.build().listIterator(1), contents[0], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(1), 0, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter1Remove", testIterRemove(scenario.build().listIterator(1), Result.IllegalState));
			      printTest(scenarioName + "_testListIter1Add", testListIterAdd(scenario.build().listIterator(1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1Set", testListIterSet(scenario.build().listIterator(1), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIter1PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter1PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(1), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter1PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter1PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter1PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(1), 1), 1), ELEMENT_X, Result.NoException));
			      
			      //starting index 2
			      printTest(scenarioName + "_testListIter2HasNext", testIterHasNext(scenario.build().listIterator(2), Result.True));
			      printTest(scenarioName + "_testListIter2Next", testIterNext(scenario.build().listIterator(2), contents[2], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter2NextIndex", testListIterNextIndex(scenario.build().listIterator(2), 2, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter2HasPrevious", testListIterHasPrevious(scenario.build().listIterator(2), Result.True));
			      printTest(scenarioName + "_testListIter2Previous", testListIterPrevious(scenario.build().listIterator(2), contents[1], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter2PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(2), 1, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter2Remove", testIterRemove(scenario.build().listIterator(2), Result.IllegalState));
			      printTest(scenarioName + "_testListIter2Add", testListIterAdd(scenario.build().listIterator(2), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter2Set", testListIterSet(scenario.build().listIterator(2), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIter2PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(2), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter2PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter2PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(2), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter2PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter2PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter2PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(2), 1), 1), ELEMENT_X, Result.NoException));
			      
			    //starting index 3
			      printTest(scenarioName + "_testListIter3HasNext", testIterHasNext(scenario.build().listIterator(3), Result.False));
			      printTest(scenarioName + "_testListIter3Next", testIterNext(scenario.build().listIterator(3), null, Result.NoSuchElement));
			      printTest(scenarioName + "_testListIter3NextIndex", testListIterNextIndex(scenario.build().listIterator(3), 3, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter3HasPrevious", testListIterHasPrevious(scenario.build().listIterator(3), Result.True));
			      printTest(scenarioName + "_testListIter3Previous", testListIterPrevious(scenario.build().listIterator(3), contents[2], Result.MatchingValue));
			      printTest(scenarioName + "_testListIter3PreviousIndex", testListIterPreviousIndex(scenario.build().listIterator(3), 2, Result.MatchingValue));
			      printTest(scenarioName + "_testListIter3Remove", testIterRemove(scenario.build().listIterator(3), Result.IllegalState));
			      printTest(scenarioName + "_testListIter3Add", testListIterAdd(scenario.build().listIterator(3), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter3Set", testListIterSet(scenario.build().listIterator(3), ELEMENT_X, Result.IllegalState));
			      printTest(scenarioName + "_testListIter3PreviousRemove", testIterRemove(listIterAfterPrevious(scenario.build().listIterator(3), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter3PreviousAdd", testListIterAdd(listIterAfterPrevious(scenario.build().listIterator(3), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter3PreviousSet", testListIterSet(listIterAfterPrevious(scenario.build().listIterator(3), 1), ELEMENT_X, Result.NoException));
			      printTest(scenarioName + "_testListIter3PreviousNextRemove", testIterRemove(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 1), 1), Result.NoException));
			      printTest(scenarioName + "_testListIter3PreviousNextAdd", testListIterAdd(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 1), 1), ELEMENT_X, Result.NoException));			
			      printTest(scenarioName + "_testListIter3PreviousNextSet", testListIterSet(listIterAfterNext(listIterAfterPrevious(scenario.build().listIterator(3), 1), 1), ELEMENT_X, Result.NoException));
			} else {
				printTest(scenarioName + "_testListIter", testListIter(scenario.build(), Result.UnsupportedOperation));
				printTest(scenarioName + "_testListIter0", testListIter(scenario.build(), 0, Result.UnsupportedOperation));
			}
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", scenarioName + " TESTS");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	////////////////////////////
	// XXX LIST TEST METHODS
	////////////////////////////

	/** Runs removeFirst() method on given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveFirst(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.removeFirst();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveFirst", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs removeLast() method on given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveLast(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.removeLast();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveLast", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs removeLast() method on given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element element to remove
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveElement(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.remove(element);
			if (retVal.equals(element)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveElement", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs first() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testFirst(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.first();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testFirst", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs last() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedElement element or null if expectedResult is an Exception
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testLast(IndexedUnsortedList<Integer> list, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.last();
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testLast", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs contains() method on a given list and element and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testContains(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			if (list.contains(element)) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testContains", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs isEmpty() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIsEmpty(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			if (list.isEmpty()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIsEmpty", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs size() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedSize
	 * @return test success
	 */
	private boolean testSize(IndexedUnsortedList<Integer> list, int expectedSize) {
		try {
			return (list.size() == expectedSize);
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testSize", e.toString());
			e.printStackTrace();
			return false;
		}
	}

	/** Runs toString() method on given list and attempts to confirm non-default or empty String
	 * difficult to test - just confirm that default address output has been overridden
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testToString(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			String str = list.toString().trim();
			if (showToString) {
				System.out.println("toString() output: " + str);
			}
			if (str.length() < (list.size() + list.size()/2 + 2)) { //elements + commas + '[' + ']'
				result = Result.Fail;
			} else {
				char lastChar = str.charAt(str.length() - 1);
				char firstChar = str.charAt(0);
				if (firstChar != '[' || lastChar != ']') {
					result = Result.Fail;
				} else if (str.contains("@")
						&& !str.contains(" ")
						&& Character.isLetter(str.charAt(0))
						&& (Character.isDigit(lastChar) || (lastChar >= 'a' && lastChar <= 'f'))) {
					result = Result.Fail; // looks like default toString()
				} else {
					result = Result.ValidString;
				}
			}
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testToString", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs addToFront() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddToFront(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addToFront(element);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddToFront",  e.toString());
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs addToRear() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddToRear(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addToRear(element);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddToRear", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs addAfter() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param target
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddAfter(IndexedUnsortedList<Integer> list, Integer target, Integer element, Result expectedResult) {
		Result result;
		try {
			list.addAfter(element, target);
			result = Result.NoException;
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAfter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs add(int, T) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAddAtIndex(IndexedUnsortedList<Integer> list, int index, Integer element, Result expectedResult) {
		Result result;
		try {
			list.add(index, element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAtIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs add(T) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testAdd(IndexedUnsortedList<Integer> list, Integer element, Result expectedResult) {
		Result result;
		try {
			list.add(element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testAddAtIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs set(int, T) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testSet(IndexedUnsortedList<Integer> list, int index, Integer element, Result expectedResult) {
		Result result;
		try {
			list.set(index, element);
			result = Result.NoException;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testSet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs get() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param expectedElement
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testGet(IndexedUnsortedList<Integer> list, int index, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.get(index);
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testGet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs remove(index) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index
	 * @param expectedElement
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testRemoveIndex(IndexedUnsortedList<Integer> list, int index, Integer expectedElement, Result expectedResult) {
		Result result;
		try {
			Integer retVal = list.remove(index);
			if (retVal.equals(expectedElement)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testRemoveIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs indexOf() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param element
	 * @param expectedIndex
	 * @return test success
	 */
	private boolean testIndexOf(IndexedUnsortedList<Integer> list, Integer element, int expectedIndex) {
		try {
			return list.indexOf(element) == expectedIndex;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIndexOf", e.toString());
			e.printStackTrace();
			return false;
		}
	}

	////////////////////////////
	// XXX ITERATOR TESTS
	////////////////////////////

	/** Runs iterator() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIter(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.iterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs list's iterator hasNext() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasNext()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterHasNext(Iterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			if (iterator.hasNext()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterHasNext", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs list's iterator next() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasNext()
	 * @param expectedValue the Integer expected from next() or null if an exception is expected
	 * @param expectedResult MatchingValue or expected exception
	 * @return test success
	 */
	private boolean testIterNext(Iterator<Integer> iterator, Integer expectedValue, Result expectedResult) {
		Result result;
		try {
			Integer retVal = iterator.next();
			if (retVal.equals(expectedValue)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterNext", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs list's iterator remove() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to remove()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterRemove(Iterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			iterator.remove();
			result = Result.NoException;
		} catch (IllegalStateException e) {
			result = Result.IllegalState;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterRemove", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs iterator() method twice on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testIterConcurrent(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it1 = list.iterator();
			@SuppressWarnings("unused")
			Iterator<Integer> it2 = list.iterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	//////////////////////////////////////////////////////////
	//XXX HELPER METHODS FOR TESTING ITERATORS
	//////////////////////////////////////////////////////////
	
	/** Helper for testing iterators. Return an Iterator that has been advanced numCallsToNext times.
	 * @param list
	 * @param numCallsToNext
	 * @return Iterator for given list, after numCallsToNext
	 */
	private Iterator<Integer> iterAfterNext(IndexedUnsortedList<Integer> list, int numCallsToNext) {
		Iterator<Integer> it = list.iterator();
		for (int i = 0; i < numCallsToNext; i++) {
			it.next();
		}
		return it;
	}
	/** Helper for testing iterators. Return an Iterator that has been advanced numCallsToNext times, calls remove
	 * , then next
	 * @param list
	 * @param numCallsToNext, how many nexts before remove
	 * @return Iterator for given list, after numCallsToNext
	 */
	private Iterator<Integer> iterAfterNextRemove(IndexedUnsortedList<Integer> list, int numCallsToNext) {
		Iterator<Integer> it = list.iterator();
		for (int i = 0; i < numCallsToNext; i++) {
			it.next();
		}
		it.remove();
		it.next();
		return it;
	}

	/** Helper for testing iterators. Return an Iterator that has had remove() called once.
	 * @param iterator
	 * @return same Iterator following a call to remove()
	 */
	private Iterator<Integer> iterAfterRemove(Iterator<Integer> iterator) {
		iterator.remove();
		return iterator;
	}

	////////////////////////////////////////////////////////////////////////
	// XXX LISTITERATOR TESTS
	// Note: can use Iterator tests for hasNext(), next(), and remove()
	////////////////////////////////////////////////////////////////////////

	/** Runs listIterator() method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIter(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.listIterator();
			result = Result.NoException;
		} catch (UnsupportedOperationException e) {
			result = Result.UnsupportedOperation;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs listIterator(index) method on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @param startingIndex
	 * @return test success
	 */
	private boolean testListIter(IndexedUnsortedList<Integer> list, int startingIndex, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			Iterator<Integer> it = list.listIterator(startingIndex);
			result = Result.NoException;
		} catch (UnsupportedOperationException e) {
			result = Result.UnsupportedOperation;
		} catch (IndexOutOfBoundsException e) {
			result = Result.IndexOutOfBounds;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIter", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator's hasPrevious() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasPrevious()
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterHasPrevious(ListIterator<Integer> iterator, Result expectedResult) {
		Result result;
		try {
			if (iterator.hasPrevious()) {
				result = Result.True;
			} else {
				result = Result.False;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterHasPrevious", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator previous() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to hasPrevious()
	 * @param expectedValue the Integer expected from next() or null if an exception is expected
	 * @param expectedResult MatchingValue or expected exception
	 * @return test success
	 */
	private boolean testListIterPrevious(ListIterator<Integer> iterator, Integer expectedValue, Result expectedResult) {
		Result result;
		try {
			Integer retVal = iterator.previous();
			if (retVal.equals(expectedValue)) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (NoSuchElementException e) {
			result = Result.NoSuchElement;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterPrevious", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator add() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to add()
	 * @param element new Integer for insertion
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterAdd(ListIterator<Integer> iterator, Integer element, Result expectedResult) {
		Result result;
		try {
			iterator.add(element);
			result = Result.NoException;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterAdd", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator set() method and checks result against expectedResult
	 * @param iterator an iterator already positioned for the call to set()
	 * @param element replacement Integer for last returned element
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterSet(ListIterator<Integer> iterator, Integer element, Result expectedResult) {
		Result result;
		try {
			iterator.set(element);
			result = Result.NoException;
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (IllegalStateException e) {
			result = Result.IllegalState;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterSet", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator nextIndex() and checks result against expected Result
	 * @param iterator already positioned for the call to nextIndex()
	 * @param expectedIndex
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterNextIndex(ListIterator<Integer> iterator, int expectedIndex, Result expectedResult) {
		Result result;
		try {
			int idx = iterator.nextIndex();
			if (idx == expectedIndex) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterNextIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs ListIterator previousIndex() and checks result against expected Result
	 * @param iterator already positioned for the call to previousIndex()
	 * @param expectedIndex
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterPreviousIndex(ListIterator<Integer> iterator, int expectedIndex, Result expectedResult) {
		Result result;
		try {
			int idx = iterator.previousIndex();
			if (idx == expectedIndex) {
				result = Result.MatchingValue;
			} else {
				result = Result.Fail;
			}
		} catch (ConcurrentModificationException e) {
			result = Result.ConcurrentModification;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterPreviousIndex", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs listIterator() method twice on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterConcurrent(IndexedUnsortedList<Integer> list, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			ListIterator<Integer> it1 = list.listIterator();
			@SuppressWarnings("unused")
			ListIterator<Integer> it2 = list.listIterator();
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	/** Runs listIterator(index) method twice on a given list and checks result against expectedResult
	 * @param list a list already prepared for a given change scenario
	 * @param index1
	 * @param index2
	 * @param expectedResult
	 * @return test success
	 */
	private boolean testListIterConcurrent(IndexedUnsortedList<Integer> list, int index1, int index2, Result expectedResult) {
		Result result;
		try {
			@SuppressWarnings("unused")
			ListIterator<Integer> it1 = list.listIterator(index1);
			@SuppressWarnings("unused")
			ListIterator<Integer> it2 = list.listIterator(index2);
			result = Result.NoException;
		} catch (Exception e) {
			System.out.printf("%s caught unexpected %s\n", "testListIterConcurrent", e.toString());
			e.printStackTrace();
			result = Result.UnexpectedException;
		}
		return result == expectedResult;
	}

	//////////////////////////////////////////////////////////
	//XXX HELPER METHODS FOR TESTING LISTITERATORS
	//////////////////////////////////////////////////////////
	
	/** Helper for testing ListIterators. Return a ListIterator that has been advanced numCallsToNext times.
	 * @param iterator
	 * @param numCallsToNext
	 * @return same iterator after numCallsToNext
	 */
	private ListIterator<Integer> listIterAfterNext(ListIterator<Integer> iterator, int numCallsToNext) {
		for (int i = 0; i < numCallsToNext; i++) {
			iterator.next();
		}
		return iterator;
	}

	/** Helper for testing ListIterators. Return a ListIterator that has been backed up numCallsToPrevious times.
	 * @param iterator
	 * @param numCallsToPrevious
	 * @return same iterator after numCallsToPrevious
	 */
	private ListIterator<Integer> listIterAfterPrevious(ListIterator<Integer> iterator, int numCallsToPrevious) {
		for (int i = 0; i < numCallsToPrevious; i++) {
			iterator.previous();
		}
		return iterator;
	}

	/** Helper for testing ListIterators. Return a ListIterator that has had remove() called once.
	 * @param iterator
	 * @return same Iterator following a call to remove()
	 */
	private ListIterator<Integer> listIterAfterRemove(ListIterator<Integer> iterator) {
		iterator.remove();
		return iterator;
	}

	////////////////////////////////////////////////////////
	// XXX Iterator Concurrency Tests
	// Can simply use as given. Don't need to add more.
	////////////////////////////////////////////////////////

	/** run Iterator concurrency tests */
	private void test_IterConcurrency() {
		System.out.println("\nIterator Concurrency Tests\n");		
		try {
			printTest("emptyList_testConcurrentIter", testIterConcurrent(newList(), Result.NoException));
			IndexedUnsortedList<Integer> list = newList();
			Iterator<Integer> it1 = list.iterator();
			Iterator<Integer> it2 = list.iterator();
			it1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2HasNext", testIterHasNext(it2, Result.False));
			list = newList();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2Next", testIterNext(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("emptyList_iter1HasNext_testIter2Remove", testIterRemove(it2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("A_iter1HasNext_testIter2HasNext", testIterHasNext(it2, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("A_iter1HasNext_testIter2Next", testIterNext(it2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.hasNext();
			printTest("A_iter1HasNext_testIter2Remove", testIterRemove(it2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			printTest("A_iter1Next_testIter2HasNext", testIterHasNext(it2, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			printTest("A_iter1Next_testIter2Next", testIterNext(it2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			printTest("A_iter1Next_testIter2Remove", testIterRemove(it2, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			it1.remove();
			printTest("A_iter1NextRemove_testIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			it1.remove();
			printTest("A_iter1NextRemove_testIter2Next", testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			it2 = list.iterator();
			it1.next();
			it1.remove();
			printTest("A_iter1NextRemove_testIter2Remove", testIterRemove(it2, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeFirst();
			printTest("A_removeFirst_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeFirst();
			printTest("A_removeFirst_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeFirst();
			printTest("A_removeLast_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.removeLast();
			printTest("A_removeLast_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));			

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_remove_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(ELEMENT_A);
			printTest("A_remove_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.first();
			printTest("A_first_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.first();
			printTest("A_first_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.first();
			printTest("A_first_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.last();
			printTest("A_last_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.last();
			printTest("A_last_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.last();
			printTest("A_last_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.isEmpty();
			printTest("A_isEmpty_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.size();
			printTest("A_size_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.size();
			printTest("A_size_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.size();
			printTest("A_size_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.toString();
			printTest("A_toString_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterNextConcurrent", testIterNext(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.get(0);
			printTest("A_get0_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.get(0);
			printTest("A_get0_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.get(0);
			printTest("A_get_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));

			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.iterator();
			list.remove(0);
			printTest("A_remove0_testIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", "test_IteratorConcurrency");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}

	////////////////////////////////////////////////////////
	// XXX ListIterator Concurrency Tests
	// Will add tests for double-linked list
	////////////////////////////////////////////////////////

	/** run ListIterator concurrency tests */
	private void test_ListIterConcurrency() {
		System.out.println("\nListIterator Concurrency Tests\n");
		try {
			printTest("emptyList_testConcurrentListIter", testListIterConcurrent(newList(), Result.NoException));
			printTest("emptyList_testConcurrentListIter00", testListIterConcurrent(newList(), 0, 0, Result.NoException));

			IndexedUnsortedList<Integer> list = newList();
			ListIterator<Integer> it1 = list.listIterator();
			ListIterator<Integer> it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2HasNext", testIterHasNext(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2Next", testIterNext(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2Remove", testIterRemove(it2, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2Previous", testListIterPrevious(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2AddA", testListIterAdd(it2, ELEMENT_A, Result.NoException));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2SetA", testListIterSet(it2, ELEMENT_A, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.MatchingValue));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasNext();
			printTest("emptyList_ListIter1HasNext_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.MatchingValue));

			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2HasNext", testIterHasNext(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2Next", testIterNext(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2Remove", testIterRemove(it2, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2Previous", testListIterPrevious(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2AddA", testListIterAdd(it2, ELEMENT_A, Result.NoException));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2SetA", testListIterSet(it2, ELEMENT_A, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.MatchingValue));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.hasPrevious();
			printTest("emptyList_ListIter1HasPrevious_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.MatchingValue));

			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2HasNext", testIterHasNext(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2Next", testIterNext(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2Remove", testIterRemove(it2, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2Previous", testListIterPrevious(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2AddA", testListIterAdd(it2, ELEMENT_A, Result.NoException));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2SetA", testListIterSet(it2, ELEMENT_A, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.MatchingValue));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.nextIndex();
			printTest("emptyList_ListIter1NextIndex_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.MatchingValue));

			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2HasNext", testIterHasNext(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2Next", testIterNext(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2Remove", testIterRemove(it2, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.False));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2Previous", testListIterPrevious(it2, null, Result.NoSuchElement));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2AddA", testListIterAdd(it2, ELEMENT_A, Result.NoException));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2SetA", testListIterSet(it2, ELEMENT_A, Result.IllegalState));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.MatchingValue));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.previousIndex();
			printTest("emptyList_ListIter1PreviousIndex_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.MatchingValue));

			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2Next", testIterNext(it2, null, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2Remove", testIterRemove(it2, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2Previous", testListIterPrevious(it2, null, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2SetA", testListIterSet(it2, ELEMENT_A, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.ConcurrentModification));
			list = newList();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.add(ELEMENT_A);
			printTest("emptyList_ListIter1AddA_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2HasNext", testIterHasNext(it2, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2Next", testIterNext(it2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2Remove", testIterRemove(it2, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2Previous", testListIterPrevious(it2, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2SetB", testListIterSet(it2, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.MatchingValue));
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			printTest("A_ListIter1Next_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2HasNext", testIterHasNext(it2, Result.True));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2Next", testIterNext(it2, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2Remove", testIterRemove(it2, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.False));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2Previous", testListIterPrevious(it2, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2SetB", testListIterSet(it2, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.MatchingValue));
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			printTest("A_ListIter1Previous_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_add0A_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2Next", testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2Remove", testIterRemove(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2Previous", testListIterPrevious(it2, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2SetB", testListIterSet(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.remove();
			printTest("A_ListIter1NextRemove_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2Next", testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2Remove", testIterRemove(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2Previous", testListIterPrevious(it2, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2SetB", testListIterSet(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.remove();
			printTest("A_ListIter1PreviousRemove_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2Next", testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2Remove", testIterRemove(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2Previous", testListIterPrevious(it2, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2SetB", testListIterSet(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			it2 = list.listIterator();
			it1.next();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1NextSetB_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2Next", testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2Remove", testIterRemove(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2Previous", testListIterPrevious(it2, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2SetB", testListIterSet(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.previous();
			it1.set(ELEMENT_B);
			printTest("A_ListIter1PreviousSetB_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2HasNext", testIterHasNext(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2Next", testIterNext(it2, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2Remove", testIterRemove(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2HasPrevious", testListIterHasPrevious(it2, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2Previous", testListIterPrevious(it2, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2AddB", testListIterAdd(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2SetB", testListIterSet(it2, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2NextIndex", testListIterNextIndex(it2, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it2 = list.listIterator();
			it1 = list.listIterator(1);
			it1.add(ELEMENT_B);
			printTest("A_ListIter11AddB_testListIter2PreviousIndex", testListIterPreviousIndex(it2, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeLast_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterPreviousConcurrent", testListIterPrevious(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeFirst();
			printTest("A_removeFirst_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterPreviousConcurrent", testListIterPrevious(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.removeLast();
			printTest("A_removeLast_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeLast_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterPreviousConcurrent", testListIterPrevious(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(ELEMENT_A);
			printTest("A_removeA_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.first();
			printTest("A_first_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.last();
			printTest("A_last_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.contains(ELEMENT_A);
			printTest("A_containsA_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.isEmpty();
			printTest("A_isEmpty_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.size();
			printTest("A_size_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.toString();
			printTest("A_toString_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterNextConcurrent", testIterNext(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToFront(ELEMENT_B);
			printTest("A_addToFrontB_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterNextConcurrent", testIterNext(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addToRear(ELEMENT_B);
			printTest("A_addToRearB_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.addAfter(ELEMENT_B, ELEMENT_A);
			printTest("A_addAfterAB_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0,ELEMENT_B);
			printTest("A_add0B_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(0, ELEMENT_B);
			printTest("A_add0B_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0,ELEMENT_B);
			printTest("A_set0B_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.set(0, ELEMENT_B);
			printTest("A_set0B_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.add(ELEMENT_B);
			printTest("A_addB_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.get(0);
			printTest("A_get0_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterHasNextConcurrent", testIterHasNext(it1, Result.True));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterRemoveConcurrent", testIterRemove(it1, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.False));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.NoSuchElement));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.NoException));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.IllegalState));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.MatchingValue));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.indexOf(ELEMENT_A);
			printTest("A_indexOfA_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.MatchingValue));

			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterHasNextConcurrent", testIterHasNext(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterNextConcurrent", testIterNext(it1, ELEMENT_A, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterRemoveConcurrent", testIterRemove(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterHasPreviousConcurrent", testListIterHasPrevious(it1, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterPreviousConcurrent", testListIterPrevious(it1, null, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterAddBConcurrent", testListIterAdd(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterSetBConcurrent", testListIterSet(it1, ELEMENT_B, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterNextIndexConcurrent", testListIterNextIndex(it1, 0, Result.ConcurrentModification));
			list = emptyList_addToFrontA_A();
			it1 = list.listIterator();
			list.remove(0);
			printTest("A_remove0_testListIterPreviousIndexConcurrent", testListIterPreviousIndex(it1, -1, Result.ConcurrentModification));
		} catch (Exception e) {
			System.out.printf("***UNABLE TO RUN/COMPLETE %s***\n", "test_ListIterConcurrency");
			e.printStackTrace();
		} finally {
			if (printSectionSummaries) {
				printSectionSummary();
			}
		}
	}
}// end class IndexedUnsortedListTester

/** Interface for builder method Lambda references used above */
interface Scenario<T> {
	IndexedUnsortedList<T> build();
}
