import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.TreeSet;

/** Indexing -- compiles a book's index, given its contents in the correct format.

@author  Izan Ahmed
Grinnell College
ahmediza@grinnell.edu   

An object of this class has functions to compile the contents and give out an index.
*/

public class Indexing {

	/** contentsList is an array list in which the program initially reads the data into from the file.
*/
	
	private ArrayList<String> contentsList;

	/** finalContentsList is an array list in which the program adds the finished index, line by line
	 * to be printed.
*/
	
	private ArrayList<String> finalContentsList;
	
	/** mainTree is a tree map in which the program adds the key as the heading or heading
	 * with sub-heading, which is a string and the corresponding page numbers as values in
	 * a tree set, as integers.
*/
	
	private TreeMap<String, TreeSet<Integer>> mainTree;

	/** intTree is a tree set in which the program adds the page numbers of the corresponding 
	 * keys, it adds them as integers.
*/
	
	private TreeSet<Integer> intTree;

	/** The Indexing constructor instantiates
		objects of type ArrayList, ArrayList, TreeMap, TreeSet respectively.
*/
	
	public Indexing() {
		contentsList = new ArrayList<String>();
		finalContentsList = new ArrayList<String>();
		mainTree = new TreeMap<String, TreeSet<Integer>>();
		intTree = new TreeSet<Integer>();
	}
	
	/** The fileReader method takes in the file path to be read, and reads the data into 
	 * contentsList array list, in the format "PageNo., Heading/sub-heading, 
	 * PageNo., Heading 2/sub-heading 2". It is a void function as it just modifies the array list.
*/
	
	public void fileReader(String path) {
		File file = new File(path);

		try {
			Scanner input = new Scanner(file);
			while(input.hasNext()) {	
				String line = input.nextLine();
				if (line.length() == 0) {
					System.err.println("Empty line found and skipped!");
					continue;
				}
				String[] words = line.split(": ");
				if (words.length != 2) {
					System.err.println("Error in format found! Some values not printed!");
					continue;
				}
				for (String item : words) {
					contentsList.add(item);
				}
			}
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/** The fixInput method basically adds the Topic/sub-heading in the TreeMap mainTree, as keys and 
	 * adds the corresponding values (page numbers and thus, converting them to integers) in a TreeSet 
	 * intTree to get the page numbers sorted in ascending order. It is a void function as it 
	 * just modifies the TreeMap and the TreeSet.
*/
	
	public void fixInput() {
		int i = 1;
		while (i < contentsList.size()) {
			int j = 1;
			while (j < contentsList.size()) {
				if (contentsList.get(j).equals(contentsList.get(i))) {
					intTree.add(Integer.valueOf(contentsList.get(j-1)));
				}
				j=j+2;
			}
			TreeSet<Integer> T_new = new TreeSet<Integer>();
			T_new = (TreeSet<Integer>)intTree.clone();
			mainTree.put(contentsList.get(i), T_new);
			intTree.clear();
			i=i+2;
		}
	}
	
	/** The addIndex method is where the separation of the Heading and the sub-heading happens.
	 * So, I have considered the 4 cases, depending on it, I have added each heading and 
	 * sub-heading appropriately (4 spaces etc). It is a type of iteration of the TreeMap mainTree using
	 * the keySet and the corresponding values. This iteration involves 2 entries of the TreeMap, and
	 * I iterate through the TreeMap two entries at the same time. To which, I came up with 4 cases, to
	 * accurately add them to finalContentsList as 1 string, consisting of key and values.
	 * In this function, I have also checked for errors as wrong format. 
*/
	
	public void addIndex () {
		for (int j = 1; j < mainTree.size(); j++) {	
			String key = (String) (mainTree.keySet()).toArray()[j-1];
			String nextKey = (String) (mainTree.keySet()).toArray()[j];
			String value = mainTree.get(key).toString().replace("[", "").replace("]", ""); //to remove the square brackets
			String nextValue = mainTree.get(nextKey).toString().replace("[", "").replace("]", ""); //to remove the square brackets
			String[] temp = key.split("/");
			String[] temp2 = nextKey.split("/");
			
			if (temp.length == 1 && temp2.length == 1) {//Case 1
				
				if (!Character.isUpperCase(key.charAt(0)) || !Character.isUpperCase(nextKey.charAt(0))) {
					System.err.println("Incorrect Format! Please fix the file! The following result is not correct!");
					continue;
				}
				
				if (finalContentsList.contains(key + ", " + value)) {
					finalContentsList.add(nextKey + ", " + nextValue);
				} else {
					finalContentsList.add(key + ", " + value);
					finalContentsList.add(nextKey + ", " + nextValue);
				}
				
			} else if (temp.length == 1 && temp2.length == 2) {//Case 2
				String first2 = temp2[0];
				String second2 = temp2[1];
				
				if (!Character.isUpperCase(key.charAt(0)) || !Character.isUpperCase(first2.charAt(0))) {
					System.err.println("Incorrect Format! Please fix the file! The following result is not correct!");
					continue;
				}
				
				if (!Character.isLowerCase(second2.charAt(0))) {
					System.err.println("Incorrect Format! Please fix the file! The following result is not correct!");
					continue;
				}
				
				if (finalContentsList.isEmpty() && key.equals(first2)) {
					finalContentsList.add(key + ", " + value);
					finalContentsList.add("    " + second2 + ", " + nextValue);
				} else if (finalContentsList.isEmpty() && !key.equals(first2)) {
					finalContentsList.add(key + ", " + value);
					finalContentsList.add(first2);
					finalContentsList.add("    " + second2 + ", " + nextValue);
				} else if (!finalContentsList.isEmpty() && key.equals(first2)) {
					finalContentsList.add("    " + second2 + ", " + nextValue);
				} else if (!finalContentsList.isEmpty() && !key.equals(first2)) {
					finalContentsList.add(first2);
					finalContentsList.add("    " + second2 + ", " + nextValue);
				}
			} else if (temp.length == 2 && temp2.length == 1) {//Case 3
				String first = temp[0];
				String second = temp[1];
				
				if (!Character.isUpperCase(first.charAt(0)) || !Character.isUpperCase(nextKey.charAt(0))) {
					System.err.println("Incorrect Format! Please fix the file! The following result is not correct!");
					continue;
				}
				
				if (!Character.isLowerCase(second.charAt(0))) {
					System.err.println("Incorrect Format! Please fix the file! The following result is not correct!");
					continue;
				}
				
				if (finalContentsList.isEmpty()) {
					finalContentsList.add(first);
					finalContentsList.add("    " + second + ", " + value);
				} else {
					finalContentsList.add(nextKey + ", " + nextValue);
				}
			} else if (temp.length == 2 && temp2.length == 2) {//Case 4
				String first = temp[0];
				String second = temp[1];
				String first2 = temp2[0];
				String second2 = temp2[1];
				
				if (!Character.isUpperCase(first.charAt(0)) || !Character.isUpperCase(first2.charAt(0))) {
					System.err.println("Incorrect Format! Please fix the file! The following result is not correct!");
					continue;
				}
				
				if (!Character.isLowerCase(second.charAt(0)) || !Character.isLowerCase(second2.charAt(0))) {
					System.err.println("Incorrect Format! Please fix the file! The following result is not correct!");
					continue;
				}
				
				if (finalContentsList.isEmpty() && first.equals(first2)) {
					finalContentsList.add(first);
					finalContentsList.add("    " + second + ", " + value);
					finalContentsList.add("    " + second2 + ", " + nextValue);
				} else if (finalContentsList.isEmpty() && !first.equals(first2)) {
					finalContentsList.add(first);
					finalContentsList.add("    " + second + ", " + value);
					finalContentsList.add(first2);
					finalContentsList.add("    " + second2 + ", " + nextValue);
				} else if (!finalContentsList.isEmpty()) {
					if (first.equals(first2) && finalContentsList.contains(first)) {
						finalContentsList.add("    " + second2 + ", " + nextValue);
					} else if (first.equals(first2) && !finalContentsList.contains(first)) {
						finalContentsList.add("    " + second2 + ", " + nextValue);
					} else if (!first.equals(first2) && finalContentsList.contains(first)) {
						finalContentsList.add(first2);
						finalContentsList.add("    " + second2 + ", " + nextValue);
					} else if (!first.equals(first2) && !finalContentsList.contains(first)) {
						finalContentsList.add(first2);
						finalContentsList.add("    " + second2 + ", " + nextValue);
					}
				}
			}
		}
	}
	
	/** The printIndex method just prints the finalContentsList array list, line by line.
*/
	
	public void printIndex() {
		System.out.println("\nIndex Below:\n");
		for (String s: finalContentsList) {
			System.out.println(s);
		}
	}

	/** The indexCompiler method just calls all the previously mentioned functions and gives the output.
*/
	
	public void indexCompiler() {
		System.out.print("Enter path of the correctly formatted file to be indexed: ");
		Scanner in = new Scanner(System.in);   
        String path = in.nextLine();
		this.fileReader(path);
		this.fixInput();
		this.addIndex();
		this.printIndex();
		in.close();
	}
	
	/** The main method instantiates an object I of type Indexing and
	 * implements the indexCompiler function which allows the user to add
	 * the file path of the data to be indexed. 
*/
	
	public static void main (String[] args) {
		Indexing I = new Indexing(); 
		I.indexCompiler();
	}
	
}
/* Copyright © 2019 Izan Ahmed */
/* I used help from Shervin Hajiamini to understand the assignment. */